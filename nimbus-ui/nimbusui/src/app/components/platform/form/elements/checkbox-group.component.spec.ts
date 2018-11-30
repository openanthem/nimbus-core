import { Param } from './../../../../shared/param-state';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { GrowlModule, AccordionModule, PickListModule, ListboxModule, CalendarModule, DataTableModule, DropdownModule, FileUploadModule, RadioButtonModule, CheckboxModule } from 'primeng/primeng';
import { FormsModule, ReactiveFormsModule, Validators, FormGroup, ValidatorFn, FormControl } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { EventEmitter } from '@angular/core';

import { CheckBoxGroup } from './checkbox-group.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { WebContentSvc } from '../../../../services/content-management.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { Subject } from 'rxjs';
import { InputLegend } from '../../../platform/form/elements/input-legend.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import * as data from '../../../../payload.json';
import { ValidationUtils } from '../../validators/ValidationUtils';

let pageService, param;

class MockPageService {
  public eventUpdate$: Subject<any>;
  public validationUpdate$: Subject<any>;

  constructor() {
    this.eventUpdate$ = new Subject();
    this.validationUpdate$ = new Subject();
  }

  logError(a) {
    this.eventUpdate$.next(a);
  }

  notifyErrorEvent(a) {
    this.validationUpdate$.next(a);
  }

  postOnChange(a, b, c) {

  }
}

const declarations = [
  CheckBoxGroup,
  TooltipComponent,
  InputLegend
];
const imports = [
GrowlModule,
AccordionModule, 
PickListModule, 
ListboxModule, 
CalendarModule, 
DataTableModule, 
DropdownModule, 
FileUploadModule, 
RadioButtonModule, 
CheckboxModule,
FormsModule, 
ReactiveFormsModule,
HttpModule,
HttpClientTestingModule
];
const providers = [
{provide: PageService, useClass: MockPageService},
WebContentSvc,
CustomHttpClient,
LoaderService,
ConfigService,
LoggerService
];
let fixture, hostComponent;
describe('CheckBoxGroup', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

     let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckBoxGroup);
    hostComponent = fixture.debugElement.componentInstance;
    const fg = new FormGroup({});
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    fg.addControl(param.config.code, new FormControl(param.leafState, checks));
    hostComponent.form = fg;
    hostComponent.element = param;
    pageService = TestBed.get(PageService);
});

  it('should create the CheckBoxGroup',async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('value property should be updated',async(() => {
    hostComponent.value = 'test';
    expect(hostComponent.value).toEqual('test');
  }));

  it('registerOnChange() should update the onChange property',async(() => {
    const test = () => {};
    hostComponent.registerOnChange(test);
    expect(hostComponent.onChange).toEqual(test);
  }));

  it('registerOnTouched() should update the onTouched property',async(() => {
    const test = () => {};
    hostComponent.registerOnTouched(test);
    expect(hostComponent.onTouched).toEqual(test);
  }));

  it('setState() should update call cd.markForCheck()',async(() => {
    const test = { element: { leafState: '' } };
    const spy = spyOn((hostComponent as any).cd, 'markForCheck').and.callThrough();
    hostComponent.setState('test', test);
    expect(spy).toHaveBeenCalled();
    expect(test.element.leafState).toEqual('test');
  }));

  it('emitValueChangedEvent() should call the controlValueChanged.emit()',async(() => {
    spyOn(hostComponent.controlValueChanged, 'emit').and.callThrough();
    hostComponent.form = null;
    hostComponent.emitValueChangedEvent({ element: '' }, '');
    expect(hostComponent.controlValueChanged.emit).toHaveBeenCalled();
  }));

  // it('ngOnInit() should update the requiredCss property as false based on element.activeValidationGroups',async(() => {
  //   hostComponent.element.activeValidationGroups = ['1'];
  //   spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue('');
  //   spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
  //   hostComponent.ngOnInit();
  //   expect(hostComponent.requiredCss).toBeFalsy();
  // }));

  // it('ngOnInit() should update the value',async(() => {
  //   hostComponent.element.leafState = [1];
  //   spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue('');
  //   spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
  //   hostComponent.ngOnInit();
  //   expect(hostComponent.value).toEqual([1]);
  // }));

  // it('ngOnInit() should not subscribe to pageService.eventUpdate$',async(() => {
  //   spyOn(pageService.eventUpdate$, 'subscribe').and.callThrough();
  //   hostComponent.form.controls[hostComponent.element.config.code] = null;
  //   hostComponent.ngOnInit();
  //   expect(pageService.eventUpdate$.subscribe).not.toHaveBeenCalled();
  // }));

  // it('ngOnInit() should call form.controls[eve.config.code].setValue()',() => {
  //   fixture.whenStable().then(() => {
  //     const eve = { leafState: 'tLeaf', path: 'atest', config: { code: 'firstName' } };
  //     spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue('');
  //     spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
  //     spyOn(ValidationUtils, 'applyelementStyle').and.returnValue('');
  //     spyOn(ValidationUtils, 'assessControlValidation').and.returnValue('');
  //     console.log('hostComponent.form.controls[eve.config.code]', hostComponent.form.controls['firstName']);
  //     const frmCtrl = hostComponent.form.controls[eve.config.code];
  //     hostComponent.element.path = 'atest';
  //     spyOn(frmCtrl, 'setValue').and.returnValue('');
  //     hostComponent.ngOnInit();
  //     pageService.logError(eve);
  //     expect(frmCtrl.setValue).toHaveBeenCalled();
  //   });
  // });

  // it('ngOnInit() should call form.controls[eve.config.code].reset()',() => {
  //   fixture.whenStable().then(() => {
  //     const eve = { leafState: null, path: 'atest', config: { code: 'firstName' } };
  //     const frmCtrl = hostComponent.form.controls[eve.config.code];
  //     hostComponent.element.path = 'atest';
  //     spyOn(frmCtrl, 'reset').and.returnValue('');
  //     hostComponent.ngOnInit();
  //     pageService.logError(eve);
  //     expect(frmCtrl.reset).toHaveBeenCalled();
  //   });
  // });

  // it('ngOnInit() should not call form.controls[eve.config.code].setValue() and reset() in form',() => {
  //   fixture.whenStable().then(() => {
  //     const eve = { leafState: null, path: '1atest', config: { code: 'firstName' } };
  //     const frmCtrl = hostComponent.form.controls[eve.config.code];
  //     hostComponent.element.path = 'atest';
  //     spyOn(frmCtrl, 'reset').and.returnValue('');
  //     spyOn(frmCtrl, 'setValue').and.returnValue('');
  //     hostComponent.ngOnInit();
  //     pageService.logError(eve);
  //     expect(frmCtrl.reset).not.toHaveBeenCalled();
  //     expect(frmCtrl.setValue).not.toHaveBeenCalled();
  //   });
  // });

  // it('ngOnInit() should update requiredCss property',async(() => {
  //   hostComponent.element.path = 'test';
  //   const eve = { activeValidationGroups: [1], leafState: 'tLeaf', path: 'test', config: { code: 'firstName' } };
  //   const frmCtrl = hostComponent.form.controls[eve.config.code];
  //   spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
  //   hostComponent.ngOnInit();
  //   pageService.notifyErrorEvent(eve);
  //   expect(hostComponent.requiredCss).toBeFalsy();
  //   expect(ValidationUtils.rebindValidations).toHaveBeenCalled();
  // }));

  // it('ngOnInit() should update requiredCss property on eve.activeValidationGroups.length is 0',() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.path = 'test';
  //     const eve = { activeValidationGroups: [], leafState: 'tLeaf', path: 'test', config: { code: 'firstName' } };
  //     const frmCtrl = hostComponent.form.controls[eve.config.code];
  //     spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
  //     hostComponent.ngOnInit();
  //     pageService.notifyErrorEvent(eve);
  //     expect(hostComponent.requiredCss).toBeFalsy();
  //     expect(ValidationUtils.applyelementStyle).toHaveBeenCalled();
  //   });
  // });

  // it('ngOnInit() should call pageService.postOnChange',async(() => {
  //   const eve = { config: { uiStyles: { attributes: { postEventOnChange: true } } } };
  //   spyOn(pageService, 'postOnChange').and.callThrough();
  //   spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
  //   hostComponent.ngOnInit();
  //   hostComponent.controlValueChanged.next(eve);
  //   expect(pageService.postOnChange).toHaveBeenCalled();
  // }));

  // it('ngOnInit() should not call pageService.postOnChange',async(() => {
  //   const eve = { config: { uiStyles: { attributes: { postEventOnChange: false } } } };
  //   spyOn(pageService, 'postOnChange').and.callThrough();
  //   spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
  //   hostComponent.ngOnInit();
  //   hostComponent.controlValueChanged.next(eve);
  //   expect(pageService.postOnChange).not.toHaveBeenCalled();
  // }));

});