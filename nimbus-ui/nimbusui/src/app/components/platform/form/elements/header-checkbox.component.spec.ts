'use strict';
import { TestBed, async } from '@angular/core/testing';
import { of as observableOf,  Observable } from 'rxjs';

import { HeaderCheckBox } from './header-checkbox.component';
import { EventEmitter } from 'events';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import * as data from '../../../../payload.json';
import { Table } from 'primeng/table';
import { Param } from '../../../../shared/param-state';

let param: Param;

const declarations = [HeaderCheckBox];
const imports = [];
const providers = [];

describe('HeaderCheckBox', () => {

  configureTestSuite();
  setup(HeaderCheckBox, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<HeaderCheckBox>){
    this.hostComponent.element = param;
  });

  it('should create the HeaderCheckBox', async function(this: TestContext<HeaderCheckBox>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('selectingAll() should update dt.selection and headerChckbxState', async function(this: TestContext<HeaderCheckBox>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.dt = { selection: [], value: [1, 2, 3, 4, 5], toggleRowsWithCheckbox: (a, b) => {} } as Table;
      this.hostComponent.element.config.uiStyles.attributes.pageSize = 5;
      this.hostComponent.selectingAll('', true);
      expect(this.hostComponent.dt.selection).toEqual([]);
      expect(this.hostComponent.headerChckbxState).toBeFalsy();
    });
  });

  it('selectingAll() should update dt.selection and headerChckbxState and call onClickSelectAll()', async function(this: TestContext<HeaderCheckBox>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.dt = { selection: [], value: [1, 2, 3, 4, 5], toggleRowsWithCheckbox: (a, b) => {}, first: 0 } as Table;
      this.hostComponent.element.config.uiStyles.attributes.pageSize = 5;
      spyOn(this.hostComponent, 'onClickSelectAll').and.returnValue('');
      this.hostComponent.selectingAll('', false);
      expect(this.hostComponent.dt.selection).toEqual([1, 2, 3, 4, 5]);
      expect(this.hostComponent.headerChckbxState).toBeTruthy();
      expect(this.hostComponent.onClickSelectAll).toHaveBeenCalled();
    });
  });

  it('onClickSelectAll() should call dt.toggleRowsWithCheckbox()', async function(this: TestContext<HeaderCheckBox>) {
    this.hostComponent.dt = { toggleRowsWithCheckbox: (a, b) => {}, value: [1] } as Table;
    spyOn(this.hostComponent.dt, 'toggleRowsWithCheckbox').and.callThrough();
    this.hostComponent.onClickSelectAll('', true);
    expect(this.hostComponent.dt.toggleRowsWithCheckbox).toHaveBeenCalled();
  });

  it('onClickSelectAll() should not call dt.toggleRowsWithCheckbox()', async function(this: TestContext<HeaderCheckBox>) {
    this.hostComponent.dt = { toggleRowsWithCheckbox: (a, b) => {}, value: [] } as Table;
    spyOn(this.hostComponent.dt, 'toggleRowsWithCheckbox').and.callThrough();
    this.hostComponent.onClickSelectAll('', true);
    expect(this.hostComponent.dt.toggleRowsWithCheckbox).not.toHaveBeenCalled();
  });

  it('updateToggleRowsWithCheckbox() should update dt.toggleRowsWithCheckbox', async function(this: TestContext<HeaderCheckBox>) {
    this.hostComponent.dt = {} as Table;
    this.hostComponent.dt['selection'] = [];
    this.hostComponent.updateToggleRowsWithCheckbox();
    expect(this.hostComponent.dt.toggleRowsWithCheckbox).toBeTruthy();
  });

  it('dt.toggleRowsWithCheckbox() should update dt.preventSelectionSetterPropagation and call dt.updateSelectionKeys(), dt.tableService.onSelectionChange', async function(this: TestContext<HeaderCheckBox>) {
    this.hostComponent.dt = { updateSelectionKeys: () => {}, tableService: { onSelectionChange: () => {} } } as Table;
    this.hostComponent.dt['selection'] = [];
    const testEventEmitter: any = new EventEmitter();
    this.hostComponent.dt['selectionChange'] = testEventEmitter;
    this.hostComponent.dt['onHeaderCheckboxToggle'] = testEventEmitter;
    this.hostComponent.updateToggleRowsWithCheckbox();
    spyOn(this.hostComponent.dt, 'updateSelectionKeys').and.callThrough();
    spyOn(this.hostComponent.dt.tableService, 'onSelectionChange').and.callThrough();
    const testEvent: any = new Event('');
    this.hostComponent.dt.toggleRowsWithCheckbox(testEvent, true);
    expect(this.hostComponent.dt.preventSelectionSetterPropagation).toBeTruthy();
    expect(this.hostComponent.dt.updateSelectionKeys).toHaveBeenCalled();
    expect(this.hostComponent.dt.tableService.onSelectionChange).toHaveBeenCalled();
  });

  it('ngOnInit() should update headerChckbxState as true', async function(this: TestContext<HeaderCheckBox>) {
    this.fixture.whenStable().then(() => {
      const sChange = observableOf([1, 2, 3, 4, 5]);
      this.hostComponent.dt = { selectionChange: sChange, first: 0, value: [1, 2, 3, 4, 5], onPage: observableOf(
          {
            first: ''
          }
        ) } as Table;
      this.hostComponent.element.config.uiStyles.attributes.pageSize = 5;
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.headerChckbxState).toBeTruthy();
    });
  });

  it('ngOnInit() should update headerChckbxState as false', async function(this: TestContext<HeaderCheckBox>) {
    this.fixture.whenStable().then(() => {
      const sChange = observableOf([1, 2, 3, 4, 5]);
      this.hostComponent.dt = { selectionChange: sChange, first: 0, value: [1, 2, 3, 4, 6], onPage: observableOf(
          {
            first: 3
          }
        ) } as Table;
      this.hostComponent.currentSelection = [2];
      this.hostComponent.element.config.uiStyles.attributes.pageSize = 5;
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.headerChckbxState).toBeFalsy();
    });
  });

  it('ngOnInit() should update headerChckbxState and call updateToggleRowsWithCheckbox()', async function(this: TestContext<HeaderCheckBox>) {
    this.fixture.whenStable().then(() => {
      const sChange = observableOf([1, 2, 3, 4, 5]);
      this.hostComponent.dt = { selectionChange: sChange, first: 0, value: [1, 2, 3, 4, 6], onPage: observableOf(
          {
            first: ''
          }
        ) } as Table;
      this.hostComponent.element.config.uiStyles.attributes.pageSize = 5;
      spyOn(this.hostComponent, 'updateToggleRowsWithCheckbox').and.callThrough();
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.headerChckbxState).toBeFalsy();
      expect(this.hostComponent.updateToggleRowsWithCheckbox).toHaveBeenCalled();
    });
  });
});
