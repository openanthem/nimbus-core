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
let fixture, hostComponent;
const declarations = [HeaderCheckBox];
const imports = [];
const providers = [];

describe('HeaderCheckBox', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

     let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);

  beforeEach(async function(this: TestContext<HeaderCheckBox>){
    fixture = TestBed.createComponent(HeaderCheckBox);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = param;
  });

  it('should create the HeaderCheckBox', async function(this: TestContext<HeaderCheckBox>) {
    expect(hostComponent).toBeTruthy();
  });

  it('selectingAll() should update dt.selection and headerChckbxState', async function(this: TestContext<HeaderCheckBox>) {
    fixture.whenStable().then(() => {
      hostComponent.dt = { selection: [], value: [1, 2, 3, 4, 5], toggleRowsWithCheckbox: (a, b) => {} } as Table;
      hostComponent.element.config.uiStyles.attributes.pageSize = 5;
      hostComponent.selectingAll('', true);
      expect(hostComponent.dt.selection).toEqual([]);
      expect(hostComponent.headerChckbxState).toBeFalsy();
    });
  });

  it('selectingAll() should update dt.selection and headerChckbxState and call onClickSelectAll()', async function(this: TestContext<HeaderCheckBox>) {
    fixture.whenStable().then(() => {
      hostComponent.dt = { selection: [], value: [1, 2, 3, 4, 5], toggleRowsWithCheckbox: (a, b) => {}, first: 0 } as Table;
      hostComponent.element.config.uiStyles.attributes.pageSize = 5;
      spyOn(hostComponent, 'onClickSelectAll').and.returnValue('');
      hostComponent.selectingAll('', false);
      expect(hostComponent.dt.selection).toEqual([1, 2, 3, 4, 5]);
      expect(hostComponent.headerChckbxState).toBeTruthy();
      expect(hostComponent.onClickSelectAll).toHaveBeenCalled();
    });
  });

  it('onClickSelectAll() should call dt.toggleRowsWithCheckbox()', async function(this: TestContext<HeaderCheckBox>) {
    hostComponent.dt = { toggleRowsWithCheckbox: (a, b) => {}, value: [1] } as Table;
    spyOn(hostComponent.dt, 'toggleRowsWithCheckbox').and.callThrough();
    hostComponent.onClickSelectAll('', true);
    expect(hostComponent.dt.toggleRowsWithCheckbox).toHaveBeenCalled();
  });

  it('onClickSelectAll() should not call dt.toggleRowsWithCheckbox()', async function(this: TestContext<HeaderCheckBox>) {
    hostComponent.dt = { toggleRowsWithCheckbox: (a, b) => {}, value: [] } as Table;
    spyOn(hostComponent.dt, 'toggleRowsWithCheckbox').and.callThrough();
    hostComponent.onClickSelectAll('', true);
    expect(hostComponent.dt.toggleRowsWithCheckbox).not.toHaveBeenCalled();
  });

  it('updateToggleRowsWithCheckbox() should update dt.toggleRowsWithCheckbox', async function(this: TestContext<HeaderCheckBox>) {
    hostComponent.dt = {} as Table;
    hostComponent.dt['selection'] = [];
    hostComponent.updateToggleRowsWithCheckbox();
    expect(hostComponent.dt.toggleRowsWithCheckbox).toBeTruthy();
  });

  it('dt.toggleRowsWithCheckbox() should update dt.preventSelectionSetterPropagation and call dt.updateSelectionKeys(), dt.tableService.onSelectionChange', async function(this: TestContext<HeaderCheckBox>) {
    hostComponent.dt = { updateSelectionKeys: () => {}, tableService: { onSelectionChange: () => {} } } as Table;
    hostComponent.dt['selection'] = [];
    const testEventEmitter: any = new EventEmitter();
    hostComponent.dt['selectionChange'] = testEventEmitter;
    hostComponent.dt['onHeaderCheckboxToggle'] = testEventEmitter;
    hostComponent.updateToggleRowsWithCheckbox();
    spyOn(hostComponent.dt, 'updateSelectionKeys').and.callThrough();
    spyOn(hostComponent.dt.tableService, 'onSelectionChange').and.callThrough();
    const testEvent: any = new Event('');
    hostComponent.dt.toggleRowsWithCheckbox(testEvent, true);
    expect(hostComponent.dt.preventSelectionSetterPropagation).toBeTruthy();
    expect(hostComponent.dt.updateSelectionKeys).toHaveBeenCalled();
    expect(hostComponent.dt.tableService.onSelectionChange).toHaveBeenCalled();
  });

  // it('ngOnInit() should update headerChckbxState as true', async function(this: TestContext<HeaderCheckBox>) {
  //   fixture.whenStable().then(() => {
  //     const sChange = observableOf([1, 2, 3, 4, 5]);
  //     hostComponent.dt = { selectionChange: sChange, first: 0, value: [1, 2, 3, 4, 5], onPage: observableOf(
  //         {
  //           first: ''
  //         }
  //       ) } as Table;
  //     hostComponent.element.config.uiStyles.attributes.pageSize = 5;
  //     hostComponent.ngOnInit();
  //     expect(hostComponent.headerChckbxState).toBeTruthy();
  //   });
  // });

  // it('ngOnInit() should update headerChckbxState as false', async function(this: TestContext<HeaderCheckBox>) {
  //   fixture.whenStable().then(() => {
  //     const sChange = observableOf([1, 2, 3, 4, 5]);
  //     hostComponent.dt = { selectionChange: sChange, first: 0, value: [1, 2, 3, 4, 6], onPage: observableOf(
  //         {
  //           first: 3
  //         }
  //       ) } as Table;
  //     hostComponent.currentSelection = [2];
  //     hostComponent.element.config.uiStyles.attributes.pageSize = 5;
  //     hostComponent.ngOnInit();
  //     expect(hostComponent.headerChckbxState).toBeFalsy();
  //   });
  // });

  // it('ngOnInit() should update headerChckbxState and call updateToggleRowsWithCheckbox()', async function(this: TestContext<HeaderCheckBox>) {
  //   fixture.whenStable().then(() => {
  //     const sChange = observableOf([1, 2, 3, 4, 5]);
  //     hostComponent.dt = { selectionChange: sChange, first: 0, value: [1, 2, 3, 4, 6], onPage: observableOf(
  //         {
  //           first: ''
  //         }
  //       ) } as Table;
  //     hostComponent.element.config.uiStyles.attributes.pageSize = 5;
  //     spyOn(hostComponent, 'updateToggleRowsWithCheckbox').and.callThrough();
  //     hostComponent.ngOnInit();
  //     expect(hostComponent.headerChckbxState).toBeFalsy();
  //     expect(hostComponent.updateToggleRowsWithCheckbox).toHaveBeenCalled();
  //   });
  // });
});
