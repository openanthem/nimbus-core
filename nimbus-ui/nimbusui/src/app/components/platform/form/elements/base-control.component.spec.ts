'use strict';
import { TestBed, async } from '@angular/core/testing';
import { ChangeDetectorRef, ViewChild, Component, EventEmitter } from '@angular/core';
import { FormGroup, NgModel } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';

import { BaseControl } from './base-control.component';
import { WebContentSvc } from '../../../../services/content-management.service';
import { ControlSubscribers } from '../../../../services/control-subscribers.service';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { ValidationUtils } from '../../validators/ValidationUtils';

let fixture, app, changeDetectorRef, controlSubscribers;

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

describe('BaseControl', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        BaseControlClass
       ],
       imports: [
        HttpModule,
        HttpClientTestingModule
       ],
       providers: [
        {provide: ChangeDetectorRef, useClass: MockChangeDetectorRef},
        {provide: ControlSubscribers, useClass: MockControlSubscribers},
        {provide: WebContentSvc, useClass: MockWebContentSvc},
        PageService,
        CustomHttpClient,
        LoaderService,
        ConfigService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(BaseControlClass);
    app = fixture.debugElement.componentInstance;
    changeDetectorRef = TestBed.get(ChangeDetectorRef);
    controlSubscribers = TestBed.get(ControlSubscribers);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('setState() should update the frminp.element.leafState.test', async(() => {
    const frminp = { element: { leafState: {} } };
    const eve = { test: 123 };
    app.setState(eve, frminp);
    expect(frminp.element.leafState['test']).toEqual(123);
  }));

  it('emitValueChangedEvent(formControl, test) should call the controlSubscribers.controlValueChanged.emit()', async(() => {
    const formControl = { value: 'test', element: 'tElement' };
    spyOn(controlSubscribers.controlValueChanged, 'emit').and.callThrough();
    app.emitValueChangedEvent(formControl, 'test');
    expect(controlSubscribers.controlValueChanged.emit).toHaveBeenCalled();
  }));

  it('emitValueChangedEvent(formControl, test) should call the controlSubscribers.controlValueChanged.emit() with no value in inPlaceEditContext', async(() => {
    const formControl = { value: 'test', element: 'tElement' };
    app.inPlaceEditContext = { value: '' };
    spyOn(controlSubscribers.controlValueChanged, 'emit').and.callThrough();
    app.emitValueChangedEvent(formControl, 'test');
    expect(controlSubscribers.controlValueChanged.emit).toHaveBeenCalled();
  }));

  it('ngOnInit() should update the app.value', async(() => {
    app.element = { leafState: 'testState', config: { validation: { constraints: [{ name: '', attribute: '' }] } } };
    app.ngOnInit();
    expect(app.value).toEqual('testState');
  }));

  it('ngOnInit() should not update requiredCss Property from ValidationUtils.rebindValidations()', async(() => {
    app.element = { activeValidationGroups: [1], leafState: 'testState', config: { uiStyles: { attributes: { alias: '' } }, validation: { constraints: [{ name: '', attribute: { groups: [1] } }] }, code: 'test' } };
    app.form = { controls: { test: { setValidators: () => {} } } };
    app.ngOnInit();
    expect(app.requiredCss).toBeFalsy();
  }));

  it('ngOnInit() should not update requiredCss Property from ValidationUtils.applyelementStyle()', async(() => {
    app.element = { activeValidationGroups: [1], leafState: 'testState', config: { uiStyles: { attributes: { alias: '' } }, validation: { constraints: [{ name: '', attribute: { groups: [1] } }] }, code: 'test' } };
    app.form = { controls: { test: null } };
    app.ngOnInit();
    expect(app.requiredCss).toBeFalsy();
  }));

  it('ngAfterViewInit() should call controlSubscribers.validationUpdateSubscriber() and controlSubscribers.onChangeEventSubscriber()', async(() => {
    app.form = { controls: { test: { valueChanges: Observable.of('') } } };
    app.element = { config: { code: 'test' } };
    spyOn(app, 'setState').and.returnValue('');
    spyOn(controlSubscribers, 'validationUpdateSubscriber').and.callThrough();
    spyOn(controlSubscribers, 'onChangeEventSubscriber').and.callThrough();
    app.ngAfterViewInit();
    expect(controlSubscribers.validationUpdateSubscriber).toHaveBeenCalled();
    expect(controlSubscribers.onChangeEventSubscriber).toHaveBeenCalled();
  }));

  it('ngAfterViewInit() should not call controlSubscribers.validationUpdateSubscriber() and call controlSubscribers.onChangeEventSubscriber()', async(() => {
    app.form = { controls: { test: { valueChanges: Observable.of('') } } };
    app.element = { config: { code: 'testa' } };
    spyOn(app, 'setState').and.returnValue('');
    spyOn(controlSubscribers, 'validationUpdateSubscriber').and.callThrough();
    spyOn(controlSubscribers, 'onChangeEventSubscriber').and.callThrough();
    app.ngAfterViewInit();
    expect(controlSubscribers.validationUpdateSubscriber).not.toHaveBeenCalled();
    expect(controlSubscribers.onChangeEventSubscriber).toHaveBeenCalled();
  }));

  it('setInPlaceEditContext() should update the inPlaceEditContext property', async(() => {
    app.setInPlaceEditContext('test');
    expect(app.showLabel).toBeFalsy();
    expect(app.inPlaceEditContext).toEqual('test');
  }));

  it('hidden property should be updated from element', async(() => {
    app.element = { config: { uiStyles: { attributes: { hidden: true } } } };
    expect(app.hidden).toBeTruthy();
  }));

  it('readOnly property should be updated from element', async(() => {
    app.element = { config: { uiStyles: { attributes: { readOnly: true } } } };
    expect(app.readOnly).toBeTruthy();
  }));

  it('type property should be updated from element', async(() => {
    app.element = { config: { uiStyles: { attributes: { type: true } } } };
    expect(app.type).toBeTruthy();
  }));

  it('help property should be updated from element', async(() => {
    app.element = { config: { uiStyles: { attributes: { help: true } } } };
    expect(app.help).toBeTruthy();
  }));

});