import { Param } from './../../../../shared/param-state';
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
import { Constraint } from '../../../../shared/param-config';
import { fieldValueParam } from 'mockdata';

let changeDetectorRef, controlSubscribers;

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
 let fixture, hostComponent;
describe('BaseControl', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BaseControlClass);
    hostComponent = fixture.debugElement.componentInstance;
    const fg = new FormGroup({});
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    fg.addControl(fieldValueParam.config.code, new FormControl(fieldValueParam.leafState, checks));
    hostComponent.form = fg;
    hostComponent.element = fieldValueParam;
    changeDetectorRef = TestBed.get(ChangeDetectorRef);
    controlSubscribers = TestBed.get(ControlSubscribers);
  });

  it('should create the BaseControlClass',async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('setState() should update the frminp.element.leafState.test',async(() => {
    const frminp = { element: { leafState: {} } };
    const eve = { test: 123 };
    hostComponent.setState(eve, frminp);
    expect(frminp.element.leafState['test']).toEqual(123);
  }));

  it('emitValueChangedEvent(formControl, test) should call the controlSubscribers.controlValueChanged.emit()',async(() => {
    const formControl = { value: 'test', element: 'tElement' };
    spyOn(controlSubscribers.controlValueChanged, 'emit').and.callThrough();
    hostComponent.emitValueChangedEvent(formControl, 'test');
    expect(controlSubscribers.controlValueChanged.emit).toHaveBeenCalled();
  }));

  it('emitValueChangedEvent(formControl, test) should call the controlSubscribers.controlValueChanged.emit() with no value in inPlaceEditContext',async(() => {
    const formControl = { value: 'test', element: 'tElement' };
    hostComponent.inPlaceEditContext = { value: '' };
    spyOn(controlSubscribers.controlValueChanged, 'emit').and.callThrough();
    hostComponent.emitValueChangedEvent(formControl, 'test');
    expect(controlSubscribers.controlValueChanged.emit).toHaveBeenCalled();
  }));

  // it('ngOnInit() should update the value property',async(() => {
  //   hostComponent.element.leafState = 'testState';
  //   hostComponent.ngOnInit();
  //   expect(hostComponent.value).toEqual('testState');
  // }));

  // it('ngOnInit() should not update requiredCss Property from ValidationUtils.rebindValidations()',() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.activeValidationGroups = ['1'];
  //     hostComponent.element.leafState = 'testState';
  //     hostComponent.element.config.uiStyles.attributes.alias = '';
  //     hostComponent.element.config.validation.constraints[0].name = '';
  //     hostComponent.element.config.validation.constraints[0].attribute.groups = ['1'];
  //     spyOn(ValidationUtils, 'rebindValidations').and.returnValue(false);
  //     hostComponent.ngOnInit();
  //     expect(hostComponent.requiredCss).toBeFalsy();
  //   });
  // });

  // it('ngOnInit() should not update requiredCss Property from ValidationUtils.applyelementStyle()',async(() => {
  //   hostComponent.element.activeValidationGroups = ['1'];
  //   hostComponent.element.leafState = 'testState';
  //   hostComponent.form = null;
  //   hostComponent.ngOnInit();
  //   expect(hostComponent.requiredCss).toBeFalsy();
  // }));

  // it('ngAfterViewInit() should call controlSubscribers.validationUpdateSubscriber() and controlSubscribers.onChangeEventSubscriber()',async(() => {
  //   spyOn(hostComponent, 'setState').and.returnValue('');
  //   spyOn(controlSubscribers, 'validationUpdateSubscriber').and.callThrough();
  //   spyOn(controlSubscribers, 'onChangeEventSubscriber').and.callThrough();
  //   hostComponent.ngAfterViewInit();
  //   expect(controlSubscribers.validationUpdateSubscriber).toHaveBeenCalled();
  //   expect(controlSubscribers.onChangeEventSubscriber).toHaveBeenCalled();
  // }));

  // it('ngAfterViewInit() should not call controlSubscribers.validationUpdateSubscriber() and call controlSubscribers.onChangeEventSubscriber()',async(() => {
  //   hostComponent.element.config.code = '';
  //   spyOn(hostComponent, 'setState').and.returnValue('');
  //   spyOn(controlSubscribers, 'validationUpdateSubscriber').and.callThrough();
  //   spyOn(controlSubscribers, 'onChangeEventSubscriber').and.callThrough();
  //   hostComponent.ngAfterViewInit();
  //   expect(controlSubscribers.validationUpdateSubscriber).not.toHaveBeenCalled();
  //   expect(controlSubscribers.onChangeEventSubscriber).toHaveBeenCalled();
  // }));

  it('setInPlaceEditContext() should update the inPlaceEditContext property',async(() => {
    hostComponent.setInPlaceEditContext('test');
    expect(hostComponent.showLabel).toBeFalsy();
    expect(hostComponent.inPlaceEditContext).toEqual('test');
  }));

  it('hidden property should be updated from element',() => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.hidden = true;
      expect(hostComponent.hidden).toBeTruthy();
    });
  });

  it('readOnly property should be updated from element',() => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.readOnly = true;
      expect(hostComponent.readOnly).toBeTruthy();
    });
  });

  it('type property should be updated from element',() => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.type = 'type';
      expect(hostComponent.type).toEqual('type');
    });
  });

  it('help property should be updated from element',() => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.help = 'help';
      expect(hostComponent.help).toEqual('help');
    });
  });

  it('getConstraint() should return undefined',() => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.validation.constraints = [];
      expect(hostComponent.getConstraint('test')).toBeFalsy();
    });
  });

  it('getConstraint() should return constraints object',() => {
    fixture.whenStable().then(() => {
      const constraint = new Constraint();
      constraint.name = 'test';
      hostComponent.element.config.validation.constraints = [constraint];
      expect(hostComponent.getConstraint('test')).toEqual(constraint);
    });
  });

  it('getConstraint() should throw error',() => {
    fixture.whenStable().then(() => {
      const constraint = new Constraint();
      constraint.name = 'test';
      const constraint1 = new Constraint();
      constraint1.name = 'test';
      hostComponent.element.config.validation.constraints = [constraint, constraint1];
      expect(() => {
        hostComponent.getConstraint('test');
      }).toThrow();
    });
  });

  it('getMaxLength() should return constraint.attribute.value',async(() => {
    const constraint = { attribute: { value: 123 } };
    spyOn(hostComponent, 'getConstraint').and.returnValue(constraint);
    expect(hostComponent.getMaxLength()).toEqual(123);
  }));

  it('getMaxLength() should return undefined',async(() => {
    spyOn(hostComponent, 'getConstraint').and.returnValue('');
    expect(hostComponent.getMaxLength()).toBeFalsy();
  }));
 
});