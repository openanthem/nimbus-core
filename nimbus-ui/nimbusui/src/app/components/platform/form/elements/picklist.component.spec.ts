'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule  } from 'primeng/primeng';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { of as observableOf,  Observable } from 'rxjs';
import { Subject } from 'rxjs';
import { EventEmitter } from '@angular/core';

import { OrderablePickList } from './picklist.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { TooltipComponent } from '../../tooltip/tooltip.component';
import { GenericDomain } from '../../../../model/generic-domain.model';
import { ValidationUtils } from './../../validators/ValidationUtils';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import * as data from '../../../../payload.json';
import { Validators, FormControl, ValidatorFn, FormGroup } from '@angular/forms';

let pageService, param;

class MockPageService {
    eventUpdate$: Subject<any>;
    validationUpdate$: Subject<any>;

    constructor() {
        this.eventUpdate$ = new Subject();
        this.validationUpdate$ = new Subject();
    }

    logError(a) {
        this.eventUpdate$.next(a);
    }

    buildBaseURL(a) {
        this.validationUpdate$.next(a);
    }

    postOnChange(a, b, c) {

    }
}

const declarations = [
  OrderablePickList,
  InputLabel,
  TooltipComponent
];
const imports = [
   PickListModule,
   HttpModule,
   HttpClientTestingModule,
   StorageServiceModule
];
const providers = [
   { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
   { provide: 'JSNLOG', useValue: JL },
   { provide: PageService, useClass: MockPageService },
   CustomHttpClient,
   LoaderService,
   ConfigService,
   LoggerService,
   AppInitService,
   SessionStoreService
];

describe('OrderablePickList', () => {

  configureTestSuite();
  setup(OrderablePickList, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<OrderablePickList>) {
    const fg = new FormGroup({});
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    fg.addControl(param.config.code, new FormControl(param.leafState, checks));
    this.hostComponent.form = fg;
    this.hostComponent.element = param;
    pageService = TestBed.get(PageService);
  });

  it('should create the OrderablePickList', async function (this: TestContext<OrderablePickList>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('setState() should update the frmInp.element.leafState', async function (this: TestContext<OrderablePickList>) {
    const frmInp = { element: { leafState: '' } };
    this.hostComponent.setState('t', frmInp);
    expect(frmInp.element.leafState).toEqual('t');
  });

  it('value property should be updated from set value()', async function (this: TestContext<OrderablePickList>) {
    this.hostComponent.value = 'test';
    expect(this.hostComponent.value).toEqual('test');
  });


  it('dragStart() should update the draggedItm property', async function (this: TestContext<OrderablePickList>) {
    this.hostComponent.element.enabled = true;
    this.hostComponent.dragStart('', 't');
    expect((this.hostComponent as any).draggedItm).toEqual('t');
  });

  it('findIndexInList() should return 0', async function (this: TestContext<OrderablePickList>) {
    const list: any = [{ code: 123 }];
    const item: any = { code: 123 };
    expect(this.hostComponent.findIndexInList(item, list)).toEqual(0);
  });

  it('registerOnChange() should update the onChange property', async function (this: TestContext<OrderablePickList>) {
    const onChange = () => {};
    this.hostComponent.registerOnChange(onChange);
    expect(this.hostComponent.onChange).toEqual(onChange);
  });

  it('registerOnTouched() should update the onTouched property', async function (this: TestContext<OrderablePickList>) {
    const onTouched = () => {};
    this.hostComponent.registerOnTouched(onTouched);
    expect(this.hostComponent.onTouched).toEqual(onTouched);
  });

  it('setDisabledState() should update the disabled property', async function (this: TestContext<OrderablePickList>) {
    this.hostComponent.setDisabledState(false);
    expect(this.hostComponent.disabled).toEqual(false);
  });

  it('dragEnd() should update the pickListControl.target', async function (this: TestContext<OrderablePickList>) {
    (this.hostComponent as any).draggedItm = true;
    this.hostComponent.targetList = [];
    const picklist: any = { source: ['a', 'b'], target: [] };
    this.hostComponent.pickListControl = picklist;
    spyOn(this.hostComponent, 'findIndexInList').and.returnValue(1);
    this.hostComponent.dragEnd('');
    expect(this.hostComponent.pickListControl.target).toEqual([true]);
  });

  it('dragEnd() should update the pickListControl.source', async function (this: TestContext<OrderablePickList>) {
    (this.hostComponent as any).draggedItm = true;
    this.hostComponent.targetList = [{ code: 'c' }];
    const picklist: any = { source: ['a', 'b'], target: [] };
    this.hostComponent.pickListControl = picklist;
    this.hostComponent.findIndexInList = (a, b) => {
      if (b === this.hostComponent.pickListControl.source) {
        return -1;
      }
      return 1;
    };
    this.hostComponent.dragEnd('');
    expect(this.hostComponent.pickListControl.source).toEqual(['a', 'b', true]);
  });

  it('emitValueChangedEvent() should update the controlValueChanged eventemitter', async function (this: TestContext<OrderablePickList>) {
    this.fixture.whenStable().then(() => {
      const controlValueChanged: any = { emit: () => {} };
      this.hostComponent.controlValueChanged = controlValueChanged;
      spyOn(this.hostComponent.controlValueChanged, 'emit').and.callThrough();
      this.hostComponent.emitValueChangedEvent();
      expect(this.hostComponent.controlValueChanged.emit).toHaveBeenCalled();
    });
  });

  it('emitValueChangedEvent() should not update the controlValueChanged eventemitter', async function (this: TestContext<OrderablePickList>) {
    this.fixture.whenStable().then(() => {
      const controlValueChanged: any = { emit: () => {} };
      this.hostComponent.controlValueChanged = controlValueChanged;
      this.hostComponent.form.controls['firstName'].setErrors({'incorrect': true});
      spyOn(this.hostComponent.controlValueChanged, 'emit').and.callThrough();
      this.hostComponent.emitValueChangedEvent();
      expect(this.hostComponent.controlValueChanged.emit).not.toHaveBeenCalled();
    });
  });
  
  it('updateListValues() should call updateData', async function (this: TestContext<OrderablePickList>) {
    (this.hostComponent as any).updateData = () => {};
    this.hostComponent.emitValueChangedEvent = () => {};
    this.hostComponent.element.leafState = [{ code: '2' }];
    const spy = spyOn((this.hostComponent as any), 'updateData').and.callThrough();
    this.hostComponent.updateListValues('');
    expect(spy).toHaveBeenCalled();
  });

  it('updateListValues should update element.leafState', async function (this: TestContext<OrderablePickList>) {
    this.hostComponent.emitValueChangedEvent = () => {};
    (this.hostComponent as any).updateData = () => {};
    this.hostComponent.element.leafState = false;    
    this.hostComponent.value = true;
    this.hostComponent.updateListValues('');
    expect(this.hostComponent.element.leafState).toBeTruthy();
  });

  it('getDesc() should return selectedvalues.label', async function (this: TestContext<OrderablePickList>) {
    const selectedvalues: any = [{ code: 'test', label: 'tlabel' }];
    this.hostComponent.selectedvalues = selectedvalues;
    expect(this.hostComponent.getDesc('test')).toEqual('tlabel');
  });

  it('getDesc() should return selectedvalues.code', async function (this: TestContext<OrderablePickList>) {
    const selectedvalues: any = [{ code: 'test', label: undefined }];
    this.hostComponent.selectedvalues = selectedvalues;
    expect(this.hostComponent.getDesc('test')).toEqual('test');
  });

  it('refreshSourceList() should update targetList', async function (this: TestContext<OrderablePickList>) {
    this.hostComponent.element.leafState = ['test'] ;
    const parent: any = { values: [{ code: 't' }] };
    this.hostComponent.parent = parent;
    (this.hostComponent as any).refreshSourceList();
    expect(this.hostComponent.targetList).toEqual(['test']);
  });

  it('updateData() should update value to null', async function (this: TestContext<OrderablePickList>) {
    this.hostComponent.targetList = [];
    (this.hostComponent as any).updateData();
    expect(this.hostComponent.value).toEqual(null);
  });

  it('updateData() should update value to array based on targetList', async function (this: TestContext<OrderablePickList>) {
    this.hostComponent.targetList = [{ code: 'test' }, { a: 'b' }];
    (this.hostComponent as any).updateData();
    expect(this.hostComponent.value).toBeTruthy(['test', { a: 'b' }]);
  });

  it('updateParentValue() should return GenericDomain based on element.config.code and targetList', async function (this: TestContext<OrderablePickList>) {
    this.hostComponent.targetList = [{ code: 'test' }, { a: 'b' }];
    const selectedOptions = [];
    selectedOptions.push(this.hostComponent.targetList[0].code);
    selectedOptions.push(this.hostComponent.targetList[1]);
    const item: GenericDomain = new GenericDomain();
    item.addAttribute(this.hostComponent.element.config.code, selectedOptions);
    expect((this.hostComponent as any).updateParentValue('')).not.toEqual(undefined);
    expect((this.hostComponent as any).updateParentValue('')).toEqual(item);
  });

  it('ngOnInit() should call refreshSourceList() two times', async function (this: TestContext<OrderablePickList>) {
    (this.hostComponent as any).loadLabelConfigFromConfigs = () => {};
    spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
    this.hostComponent.form = null;
    (this.hostComponent as any).refreshSourceList = () => {};
    this.hostComponent.element.leafState = 'test';
    const parent: any = { path: '/test', labels: '', config: { code: '' } };
    this.hostComponent.parent = parent;
    const spy = spyOn((this.hostComponent as any), 'refreshSourceList').and.callThrough();
    this.hostComponent.ngOnInit();
    pageService.logError({ path: '/test' });
    expect(spy).toHaveBeenCalledTimes(2);
  });

  it('ngOnInit() should call refreshSourceList() two times and update targetList', async function (this: TestContext<OrderablePickList>) {
    (this.hostComponent as any).loadLabelConfigFromConfigs = () => {};
    spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
    this.hostComponent.form = null;
    (this.hostComponent as any).refreshSourceList = () => {};
    this.hostComponent.element.leafState = null;
    const parent: any = { path: '/test', labels: '', config: { code: '' } };
    this.hostComponent.parent = parent;
    const spy = spyOn((this.hostComponent as any), 'refreshSourceList').and.callThrough();
    this.hostComponent.ngOnInit();
    pageService.logError({ path: '/test' });
    expect(spy).toHaveBeenCalledTimes(2);
    expect(this.hostComponent.targetList).toEqual([]);
  });

  it('ngOnInit() should call refreshSourceList() two times and update targetList, requiredCss', async function (this: TestContext<OrderablePickList>) {
    this.fixture.whenStable().then(() => {
      (this.hostComponent as any).loadLabelConfigFromConfigs = () => {};
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
      const testSubject = new Subject();
      (this.hostComponent as any).refreshSourceList = () => {};
      this.hostComponent.element.leafState = null;
      this.hostComponent.element.activeValidationGroups = ['abc'];
      const parent: any = { path: '/test', labels: '', config: { code: 'a' } };
      this.hostComponent.parent = parent;
      const spy = spyOn((this.hostComponent as any), 'refreshSourceList').and.callThrough();
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue(true);
      this.hostComponent.ngOnInit();
      pageService.logError({ path: '/test' });
      expect(spy).toHaveBeenCalledTimes(2);
      expect(this.hostComponent.targetList).toEqual([]);
      expect(this.hostComponent.requiredCss).toEqual(true);
    });
  });

  it('ngOnInit() should call refreshSourceList(), setState(), form.controls.a.setValue and update targetList, requiredCss', async function (this: TestContext<OrderablePickList>) {
    this.fixture.whenStable().then(() => {
      (this.hostComponent as any).loadLabelConfigFromConfigs = () => {};
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
      (this.hostComponent as any).refreshSourceList = () => {};
      this.hostComponent.element.leafState = null;
      this.hostComponent.element.activeValidationGroups = ['abc'];
      const parent: any = { path: '/test', labels: '', config: { code: 'firstName' } };
      this.hostComponent.parent = parent;
      this.hostComponent.setState = (a, b) => {};
      (this.hostComponent as any).updateParentValue = a => {
        return a;
      };
      spyOn(this.hostComponent, 'setState').and.callThrough();
      spyOn(this.hostComponent.form.controls.firstName, 'setValue').and.callThrough();
      const spy = spyOn((this.hostComponent as any), 'refreshSourceList').and.callThrough();
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue(true);
      this.hostComponent.ngOnInit();
      this.hostComponent.form.get('firstName').setValue('newValue');
      pageService.logError({ path: '/test' });
      expect(spy).toHaveBeenCalledTimes(2);
      expect(this.hostComponent.targetList).toEqual([]);
      expect(this.hostComponent.requiredCss).toEqual(true);
      expect(this.hostComponent.setState).toHaveBeenCalled();
      expect(this.hostComponent.form.controls.firstName.setValue).toHaveBeenCalled();  
    });
  });

  it('ngOnInit() should call refreshSourceList(), form.controls.b.setValue() with element.path and update targetList', async function (this: TestContext<OrderablePickList>) {
    this.fixture.whenStable().then(() => {
      (this.hostComponent as any).loadLabelConfigFromConfigs = () => {};
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
      const testSubject = new Subject();
      (this.hostComponent as any).refreshSourceList = () => {};
      const parent: any = { path: 'test', labels: '', config: { code: 'firstName' } };
      this.hostComponent.parent = parent;
      const spy = spyOn((this.hostComponent as any), 'refreshSourceList').and.callThrough();
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue('true');
      spyOn(this.hostComponent.form.controls.firstName, 'setValue').and.callThrough();
      this.hostComponent.element.path = 't';
      this.hostComponent.ngOnInit();
      pageService.logError({ path: 'test', leafState: 't' });
      expect(spy).toHaveBeenCalledTimes(2);
      expect(this.hostComponent.targetList).toEqual([]);
      expect(this.hostComponent.form.controls.firstName.setValue).toHaveBeenCalled();
      expect(this.hostComponent.form.controls.firstName.setValue).toHaveBeenCalledWith('t');
    });
  });

  it('ngOnInit() should call refreshSourceList(), form.controls.b.reset and update targetList', async function (this: TestContext<OrderablePickList>) {
    this.fixture.whenStable().then(() => {
      (this.hostComponent as any).loadLabelConfigFromConfigs = () => {};
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
      const testSubject = new Subject();
      (this.hostComponent as any).refreshSourceList = () => {};
      this.hostComponent.element.leafState = null;
      this.hostComponent.element.activeValidationGroups = [];
      this.hostComponent.element.path = 't';
      const parent: any = { path: 'test', labels: '', config: { code: 'a' } };
      this.hostComponent.parent = parent;
      const spy = spyOn((this.hostComponent as any), 'refreshSourceList').and.callThrough();
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue('true');
      spyOn(this.hostComponent.form.controls.firstName, 'reset').and.callThrough();
      this.hostComponent.ngOnInit();
      pageService.logError({ path: 'test', leafState: null });
      expect(spy).toHaveBeenCalledTimes(2);
      expect(this.hostComponent.targetList).toEqual([]);
      expect(this.hostComponent.form.controls.firstName.reset).toHaveBeenCalled();
    });
  });

  it('ngOnInit() should call refreshSourceList, ValidationUtils.rebindValidations, ValidationUtils.assessControlValidation and update targetList', async function (this: TestContext<OrderablePickList>) {
    (this.hostComponent as any).loadLabelConfigFromConfigs = () => {};
    spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
    const testSubject = new Subject();
    (this.hostComponent as any).refreshSourceList = () => {};
    this.hostComponent.element.leafState = null;
    this.hostComponent.element.activeValidationGroups = [];
    this.hostComponent.element.path = 'test'
    const parent: any = { path: 'test', labels: '', config: { code: 'a' } };
    this.hostComponent.parent = parent;
    const spy = spyOn((this.hostComponent as any), 'refreshSourceList').and.callThrough();
    spyOn(ValidationUtils, 'rebindValidations').and.returnValue('true');
    spyOn(ValidationUtils, 'assessControlValidation').and.returnValue('true');
    this.hostComponent.ngOnInit();
    pageService.buildBaseURL({
      path: 'test',
      leafState: null,
      activeValidationGroups: ['a']
    });
    expect(spy).toHaveBeenCalled();
    expect(this.hostComponent.targetList).toEqual([]);
    expect(ValidationUtils.rebindValidations).toHaveBeenCalled();
    expect(ValidationUtils.assessControlValidation).toHaveBeenCalled();
  });

});