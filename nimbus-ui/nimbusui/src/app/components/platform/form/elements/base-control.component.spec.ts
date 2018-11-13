'use strict';
import { TestBed, async } from '@angular/core/testing';
import { ChangeDetectorRef, ViewChild, Component, EventEmitter } from '@angular/core';
import { FormGroup, NgModel, Validators, FormControl, ValidatorFn } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of as observableOf,  Observable } from 'rxjs';

import { BaseControl } from './base-control.component';
import { WebContentSvc } from '../../../../services/content-management.service';
import { ControlSubscribers } from '../../../../services/control-subscribers.service';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { ValidationUtils } from '../../validators/ValidationUtils';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import * as data from '../../../../payload.json';
import { Constraint } from '../../../../shared/param-config';

let changeDetectorRef, controlSubscribers, param;

@Component({ 
    template: ``
 })
class BaseControlClass extends BaseControl<any> {
    @ViewChild(NgModel) model: NgModel;
    constructor(wcs: WebContentSvc, controlService: ControlSubscribers, cd:ChangeDetectorRef) {
        super(controlService, wcs, cd);
    }
    ngOnInit() { 
        super.ngOnInit();
    }
}

class MockChangeDetectorRef {
    markForCheck() {
     }
}

class MockControlSubscribers {
    controlValueChanged: EventEmitter<any>;

    constructor() {
        this.controlValueChanged = new EventEmitter();
    }

    stateUpdateSubscriber(a) { }
    validationUpdateSubscriber(b) { }
    onChangeEventSubscriber(a) { }
}

class MockWebContentSvc {
    findLabelContent(a) {
        return {
            text: 'testing',
            helpText: 'help'
        };
     }
}

class MockValidationUtils {
    applyelementStyle(a) {
        return '';
    }
    rebindValidations(a, b, c) {
        return '';
    }
}

const declarations = [
  BaseControlClass
 ];
 const imports = [
  HttpModule,
  HttpClientTestingModule
 ];
 const providers = [
  {provide: ChangeDetectorRef, useClass: MockChangeDetectorRef},
  {provide: ControlSubscribers, useClass: MockControlSubscribers},
  {provide: WebContentSvc, useClass: MockWebContentSvc},
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService
 ];

describe('BaseControl', () => {

  configureTestSuite();
  setup(BaseControlClass, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<BaseControlClass>) {
    const fg = new FormGroup({});
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    fg.addControl(param.config.code, new FormControl(param.leafState, checks));
    this.hostComponent.form = fg;
    this.hostComponent.element = param;
    changeDetectorRef = TestBed.get(ChangeDetectorRef);
    controlSubscribers = TestBed.get(ControlSubscribers);
  });

  it('should create the BaseControlClass', async function (this: TestContext<BaseControlClass>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('setState() should update the frminp.element.leafState.test', async function (this: TestContext<BaseControlClass>) {
    const frminp = { element: { leafState: {} } };
    const eve = { test: 123 };
    this.hostComponent.setState(eve, frminp);
    expect(frminp.element.leafState['test']).toEqual(123);
  });

  it('emitValueChangedEvent(formControl, test) should call the controlSubscribers.controlValueChanged.emit()', async function (this: TestContext<BaseControlClass>) {
    const formControl = { value: 'test', element: 'tElement' };
    spyOn(controlSubscribers.controlValueChanged, 'emit').and.callThrough();
    this.hostComponent.emitValueChangedEvent(formControl, 'test');
    expect(controlSubscribers.controlValueChanged.emit).toHaveBeenCalled();
  });

  it('emitValueChangedEvent(formControl, test) should call the controlSubscribers.controlValueChanged.emit() with no value in inPlaceEditContext', async function (this: TestContext<BaseControlClass>) {
    const formControl = { value: 'test', element: 'tElement' };
    this.hostComponent.inPlaceEditContext = { value: '' };
    spyOn(controlSubscribers.controlValueChanged, 'emit').and.callThrough();
    this.hostComponent.emitValueChangedEvent(formControl, 'test');
    expect(controlSubscribers.controlValueChanged.emit).toHaveBeenCalled();
  });

  it('ngOnInit() should update the value property', async function (this: TestContext<BaseControlClass>) {
    this.hostComponent.element.leafState = 'testState';
    this.hostComponent.ngOnInit();
    expect(this.hostComponent.value).toEqual('testState');
  });

  it('ngOnInit() should not update requiredCss Property from ValidationUtils.rebindValidations()', async function (this: TestContext<BaseControlClass>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.activeValidationGroups = ['1'];
      this.hostComponent.element.leafState = 'testState';
      this.hostComponent.element.config.uiStyles.attributes.alias = '';
      this.hostComponent.element.config.validation.constraints[0].name = '';
      this.hostComponent.element.config.validation.constraints[0].attribute.groups = ['1'];
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue(false);
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.requiredCss).toBeFalsy();
    });
  });

  it('ngOnInit() should not update requiredCss Property from ValidationUtils.applyelementStyle()', async function (this: TestContext<BaseControlClass>) {
    this.hostComponent.element.activeValidationGroups = ['1'];
    this.hostComponent.element.leafState = 'testState';
    this.hostComponent.form = null;
    this.hostComponent.ngOnInit();
    expect(this.hostComponent.requiredCss).toBeFalsy();
  });

  it('ngAfterViewInit() should call controlSubscribers.validationUpdateSubscriber() and controlSubscribers.onChangeEventSubscriber()', async function (this: TestContext<BaseControlClass>) {
    spyOn(this.hostComponent, 'setState').and.returnValue('');
    spyOn(controlSubscribers, 'validationUpdateSubscriber').and.callThrough();
    spyOn(controlSubscribers, 'onChangeEventSubscriber').and.callThrough();
    this.hostComponent.ngAfterViewInit();
    expect(controlSubscribers.validationUpdateSubscriber).toHaveBeenCalled();
    expect(controlSubscribers.onChangeEventSubscriber).toHaveBeenCalled();
  });

  it('ngAfterViewInit() should not call controlSubscribers.validationUpdateSubscriber() and call controlSubscribers.onChangeEventSubscriber()', async function (this: TestContext<BaseControlClass>) {
    this.hostComponent.element.config.code = '';
    spyOn(this.hostComponent, 'setState').and.returnValue('');
    spyOn(controlSubscribers, 'validationUpdateSubscriber').and.callThrough();
    spyOn(controlSubscribers, 'onChangeEventSubscriber').and.callThrough();
    this.hostComponent.ngAfterViewInit();
    expect(controlSubscribers.validationUpdateSubscriber).not.toHaveBeenCalled();
    expect(controlSubscribers.onChangeEventSubscriber).toHaveBeenCalled();
  });

  it('setInPlaceEditContext() should update the inPlaceEditContext property', async function (this: TestContext<BaseControlClass>) {
    this.hostComponent.setInPlaceEditContext('test');
    expect(this.hostComponent.showLabel).toBeFalsy();
    expect(this.hostComponent.inPlaceEditContext).toEqual('test');
  });

  it('hidden property should be updated from element', async function (this: TestContext<BaseControlClass>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.hidden = true;
      expect(this.hostComponent.hidden).toBeTruthy();
    });
  });

  it('readOnly property should be updated from element', async function (this: TestContext<BaseControlClass>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.readOnly = true;
      expect(this.hostComponent.readOnly).toBeTruthy();
    });
  });

  it('type property should be updated from element', async function (this: TestContext<BaseControlClass>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.type = 'type';
      expect(this.hostComponent.type).toEqual('type');
    });
  });

  it('help property should be updated from element', async function (this: TestContext<BaseControlClass>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.help = 'help';
      expect(this.hostComponent.help).toEqual('help');
    });
  });

  it('getConstraint() should return undefined', async function (this: TestContext<BaseControlClass>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.validation.constraints = [];
      expect(this.hostComponent.getConstraint('test')).toBeFalsy();
    });
  });

  it('getConstraint() should return constraints object', async function (this: TestContext<BaseControlClass>) {
    this.fixture.whenStable().then(() => {
      const constraint = new Constraint();
      constraint.name = 'test';
      this.hostComponent.element.config.validation.constraints = [constraint];
      expect(this.hostComponent.getConstraint('test')).toEqual(constraint);
    });
  });

  it('getConstraint() should throw error', async function (this: TestContext<BaseControlClass>) {
    this.fixture.whenStable().then(() => {
      const constraint = new Constraint();
      constraint.name = 'test';
      const constraint1 = new Constraint();
      constraint1.name = 'test';
      this.hostComponent.element.config.validation.constraints = [constraint, constraint1];
      expect(() => {
        this.hostComponent.getConstraint('test');
      }).toThrow();
    });
  });

  it('getMaxLength() should return constraint.attribute.value', async function (this: TestContext<BaseControlClass>) {
    const constraint = { attribute: { value: 123 } };
    spyOn(this.hostComponent, 'getConstraint').and.returnValue(constraint);
    expect(this.hostComponent.getMaxLength()).toEqual(123);
  });

  it('getMaxLength() should return undefined', async function (this: TestContext<BaseControlClass>) {
    spyOn(this.hostComponent, 'getConstraint').and.returnValue('');
    expect(this.hostComponent.getMaxLength()).toBeFalsy();
  });
 
});