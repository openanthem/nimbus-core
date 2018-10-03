'use strict';
import { TestBed, async } from '@angular/core/testing';
import { of as observableOf,  Observable } from 'rxjs';

import { HeaderCheckBox } from './header-checkbox.component';
import { EventEmitter } from 'events';

let fixture, app;

describe('HeaderCheckBox', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderCheckBox]
    }).compileComponents();
    fixture = TestBed.createComponent(HeaderCheckBox);
    app = fixture.debugElement.componentInstance;
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('selectingAll() should update dt.selection and headerChckbxState', async(() => {
    app.dt = {
      selection: [],
      value: [1, 2, 3, 4, 5],
      toggleRowsWithCheckbox: (a, b) => {}
    };
    app.element = { config: { uiStyles: { attributes: { pageSize: 5 } } } };
    app.selectingAll('', true);
    expect(app.dt.selection).toEqual([]);
    expect(app.headerChckbxState).toBeFalsy();
  }));

  it('selectingAll() should update dt.selection and headerChckbxState and call onClickSelectAll()', async(() => {
    app.dt = {
      selection: [],
      value: [1, 2, 3, 4, 5],
      toggleRowsWithCheckbox: (a, b) => {},
      first: 0
    };
    app.element = { config: { uiStyles: { attributes: { pageSize: 5 } } } };
    spyOn(app, 'onClickSelectAll').and.returnValue('');
    app.selectingAll('', false);
    expect(app.dt.selection).toEqual([1, 2, 3, 4, 5]);
    expect(app.headerChckbxState).toBeTruthy();
    expect(app.onClickSelectAll).toHaveBeenCalled();
  }));

  it('onClickSelectAll() should call dt.toggleRowsWithCheckbox()', async(() => {
    app.dt = { toggleRowsWithCheckbox: (a, b) => {}, value: [1] };
    spyOn(app.dt, 'toggleRowsWithCheckbox').and.callThrough();
    app.onClickSelectAll('', true);
    expect(app.dt.toggleRowsWithCheckbox).toHaveBeenCalled();
  }));

  it('onClickSelectAll() should not call dt.toggleRowsWithCheckbox()', async(() => {
    app.dt = { toggleRowsWithCheckbox: (a, b) => {}, value: [] };
    spyOn(app.dt, 'toggleRowsWithCheckbox').and.callThrough();
    app.onClickSelectAll('', true);
    expect(app.dt.toggleRowsWithCheckbox).not.toHaveBeenCalled();
  }));

  it('updateToggleRowsWithCheckbox() should update dt.toggleRowsWithCheckbox', async(() => {
    app.dt = {};
    app.dt['selection'] = [];
    app.updateToggleRowsWithCheckbox();
    expect(app.dt.toggleRowsWithCheckbox).toBeTruthy();
  }));

  it('dt.toggleRowsWithCheckbox() should update dt.preventSelectionSetterPropagation and call dt.updateSelectionKeys(), dt.tableService.onSelectionChange', async(() => {
    app.dt = {
      updateSelectionKeys: () => {},
      tableService: { onSelectionChange: () => {} }
    };
    app.dt['selection'] = [];
    app.dt['selectionChange'] = new EventEmitter();
    app.dt['onHeaderCheckboxToggle'] = new EventEmitter();
    app.updateToggleRowsWithCheckbox();
    spyOn(app.dt, 'updateSelectionKeys').and.callThrough();
    spyOn(app.dt.tableService, 'onSelectionChange').and.callThrough();
    app.dt.toggleRowsWithCheckbox('', true);
    expect(app.dt.preventSelectionSetterPropagation).toBeTruthy();
    expect(app.dt.updateSelectionKeys).toHaveBeenCalled();
    expect(app.dt.tableService.onSelectionChange).toHaveBeenCalled();
  }));

  it('ngOnInit() should update headerChckbxState as true', async(() => {
    const sChange = observableOf([1, 2, 3, 4, 5]);
    app.dt = { selectionChange: sChange, first: 0, value: [1, 2, 3, 4, 5],
     onPage: observableOf({
       first: ''
     }) };
    app.element = { config: { uiStyles: { attributes: { pageSize: 5 } } } };
    app.ngOnInit();
    expect(app.headerChckbxState).toBeTruthy();
  }));

  it('ngOnInit() should update headerChckbxState as false', async(() => {
    const sChange = observableOf([1, 2, 3, 4, 5]);
    app.dt = { selectionChange: sChange, first: 0, value: [1, 2, 3, 4, 6],
     onPage: observableOf({
       first: 3
     }) };
     app.currentSelection= [2];
    app.element = { config: { uiStyles: { attributes: { pageSize: 5 } } } };
    app.ngOnInit();
    expect(app.headerChckbxState).toBeFalsy();
  }));

  it('ngOnInit() should update headerChckbxState and call updateToggleRowsWithCheckbox()', async(() => {
    const sChange = observableOf([1, 2, 3, 4, 5]);
    app.dt = { selectionChange: sChange, first: 0, value: [1, 2, 3, 4, 6],
      onPage: observableOf({
        first: ''
      }) };    app.element = { config: { uiStyles: { attributes: { pageSize: 5 } } } };
    spyOn(app, 'updateToggleRowsWithCheckbox').and.callThrough();
    app.ngOnInit();
    expect(app.headerChckbxState).toBeFalsy();
    expect(app.updateToggleRowsWithCheckbox).toHaveBeenCalled();
  }));
});
