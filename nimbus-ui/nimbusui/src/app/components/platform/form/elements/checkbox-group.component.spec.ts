'use strict';
import { TestBed, async } from '@angular/core/testing';
import { GrowlModule, AccordionModule, PickListModule, ListboxModule, CalendarModule, DataTableModule, DropdownModule, FileUploadModule, RadioButtonModule, CheckboxModule } from 'primeng/primeng';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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

let fixture, app, pageService;

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

describe('CheckBoxGroup', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          CheckBoxGroup,
          TooltipComponent
       ],
       imports: [
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
       ],
       providers: [
        {provide: PageService, useClass: MockPageService},
        WebContentSvc,
        CustomHttpClient,
        LoaderService,
        ConfigService,
        LoggerService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(CheckBoxGroup);
    app = fixture.debugElement.componentInstance;
    pageService = TestBed.get(PageService);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('valiue property should be updated', async(() => {
    app.value = 'test';
    expect(app.value).toEqual('test');
  }));

  it('registerOnChange() should update the onChange property', async(() => {
    const test = () => {};
    app.registerOnChange(test);
    expect(app.onChange).toEqual(test);
  }));

  it('registerOnTouched() should update the onTouched property', async(() => {
    const test = () => {};
    app.registerOnTouched(test);
    expect(app.onTouched).toEqual(test);
  }));

  it('setState() should update call cd.markForCheck()', async(() => {
    const test = { element: { leafState: '' } };
    spyOn(app.cd, 'markForCheck').and.callThrough();
    app.setState('test', test);
    expect(app.cd.markForCheck).toHaveBeenCalled();
    expect(test.element.leafState).toEqual('test');
  }));

  it('emitValueChangedEvent() should call the antmControlValueChanged.emit()', async(() => {
    spyOn(app.antmControlValueChanged, 'emit').and.callThrough();
    app.emitValueChangedEvent({ element: '' }, '');
    expect(app.antmControlValueChanged.emit).toHaveBeenCalled();
  }));

  it('ngOnInit() should update the requiredCss property as false based on element.activeValidationGroups', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { activeValidationGroups: [1], config: { code: 'test' } };
    app.form = { controls: { test: { valueChanges: testSubject, setValidators: () => {}, reset: () => {} } } };
    app.ngOnInit();
    updateTestSubject();
    expect(app.requiredCss).toBeFalsy();
  }));

  it('ngOnInit() should update the requiredCss property as false if element.activeValidationGroups.length is 0', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { activeValidationGroups: [], config: { code: 'test' } };
    app.form = { controls: { test: { valueChanges: testSubject, setValidators: () => {}, reset: () => {} } } };
    app.ngOnInit();
    updateTestSubject();
    expect(app.requiredCss).toBeFalsy();
  }));

  it('ngOnInit() should update the value', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { leafState: [1, 2], activeValidationGroups: [], config: { code: 'test' } };
    app.form = { controls: { test: { valueChanges: testSubject, setValidators: () => {}, reset: () => {} } } };
    app.ngOnInit();
    updateTestSubject();
    expect(app.value).toEqual([1, 2]);
  }));

  it('updateTestSubject() should subscribe to pageService.eventUpdate$', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { leafState: [], activeValidationGroups: [], config: { code: 'test' } };
    app.form = { controls: { test: null } };
    spyOn(pageService.eventUpdate$, 'subscribe').and.callThrough();
    app.ngOnInit();
    updateTestSubject();
    expect(pageService.eventUpdate$.subscribe).not.toHaveBeenCalled();
  }));

  it('ngOnInit() should call form.controls.abc.setValue()', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { path: 'a', leafState: [1, 2], activeValidationGroups: [], config: { code: 'test' } };
    app.form = { controls: { abc: { setValue: () => {} }, test: { valueChanges: testSubject, setValidators: () => {}, reset: () => {} } } };
    const eve = { leafState: 'tLeaf', path: 'atest', config: { code: 'abc' } };
    spyOn(app.form.controls.abc, 'setValue').and.callThrough();
    app.ngOnInit();
    pageService.logError(eve);
    updateTestSubject();
    expect(app.form.controls.abc.setValue).toHaveBeenCalled();
  }));

  it('ngOnInit() should not call form.controls.abc.setValue()', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { path: 'a', leafState: [1, 2], activeValidationGroups: [], config: { code: 'test' } };
    app.form = { controls: { abc: { setValue: () => {} }, test: { valueChanges: testSubject, setValidators: () => {}, reset: () => {} } } };
    const eve = { leafState: 'tLeaf', path: 'test', config: { code: 'abc' } };
    spyOn(app.form.controls.abc, 'setValue').and.callThrough();
    app.ngOnInit();
    pageService.logError(eve);
    updateTestSubject();
    expect(app.form.controls.abc.setValue).not.toHaveBeenCalled();
  }));

  it('ngOnInit() should not call form.controls.abc.setValue() even with reset() in form', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { path: 'a', leafState: [1, 2], activeValidationGroups: [], config: { code: 'test' } };
    app.form = { controls: { abc: { setValue: () => {}, reset: () => {} }, test: { valueChanges: testSubject, setValidators: () => {}, reset: () => {} } } };
    const eve = { path: 'atest', config: { code: 'abc' } };
    spyOn(app.form.controls.abc, 'setValue').and.callThrough();
    app.ngOnInit();
    pageService.logError(eve);
    updateTestSubject();
    expect(app.form.controls.abc.setValue).not.toHaveBeenCalled();
  }));

  it('ngOnInit() should update requiredCss property', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { path: 'a', leafState: [1, 2], activeValidationGroups: [], config: { code: 'test' } };
    app.form = { controls: { abc: { setValue: () => {} }, test: { valueChanges: testSubject, setValidators: () => {}, reset: () => {} } } };
    const eve = { activeValidationGroups: [1], leafState: 'tLeaf', path: 'test', config: { code: 'abc' } };
    app.ngOnInit();
    pageService.notifyErrorEvent(eve);
    updateTestSubject();
    expect(app.requiredCss).toBeFalsy();
  }));

  it('ngOnInit() should update requiredCss property on eve.activeValidationGroups.length is 0', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { path: 'test', leafState: [1, 2], activeValidationGroups: [], config: { code: 'test' } };
    app.form = { 
      controls: { 
        test: { 
          valueChanges: testSubject, 
          setValidators: (a) => {}, 
          reset: () => {} 
        } 
      } 
    };
    const eve = { activeValidationGroups: [], leafState: 'tLeaf', path: 'test', config: { code: 'abc' } };
    app.ngOnInit();
    pageService.notifyErrorEvent(eve);
    updateTestSubject();
    expect(app.requiredCss).toBeFalsy();
  }));

  it('ngOnInit() should update requiredCss as false', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { path: 'test', leafState: [1, 2], activeValidationGroups: [], config: { code: 'test' } };
    app.form = { controls: { abc: { setValue: () => {} }, test: { valueChanges: testSubject, setValidators: () => {}, reset: () => {}, disable: () => {} } } };
    const eve = { activeValidationGroups: [1], leafState: 'tLeaf', path: 'test', config: { code: 'test' } };
    app.ngOnInit();
    pageService.notifyErrorEvent(eve);
    updateTestSubject();
    expect(app.requiredCss).toBeFalsy();
  }));

  it('ngOnInit() should call app.form.controls.test.setValidators()', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { path: 'test', leafState: [1, 2], activeValidationGroups: [], config: { code: 'test' } };
    app.form = { controls: { abc: { setValue: () => {} }, test: { valueChanges: testSubject, setValidators: () => {}, reset: () => {}, disable: () => {} } } };
    const eve = { activeValidationGroups: [], leafState: 'tLeaf', path: 'test', config: { code: 'test' } };
    spyOn(app.form.controls.test, 'setValidators').and.callThrough();
    app.ngOnInit();
    pageService.notifyErrorEvent(eve);
    updateTestSubject();
    expect(app.requiredCss).toBeFalsy();
    expect(app.form.controls.test.setValidators).toHaveBeenCalled();
  }));

  it('ngOnInit() should call pageService.postOnChange', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { leafState: [], activeValidationGroups: [], config: { code: 'test' } };
    app.form = { controls: { test: null } };
    const eve = { config: { uiStyles: { attributes: { postEventOnChange: true } } } };
    spyOn(pageService, 'postOnChange').and.callThrough();
    app.antmControlValueChanged = new EventEmitter();
    app.ngOnInit();
    app.antmControlValueChanged.emit(eve);
    expect(pageService.postOnChange).toHaveBeenCalled();
  }));

  it('ngOnInit() should not call pageService.postOnChange', async(() => {
    const testSubject = new Subject();
    const updateTestSubject = () => {
      testSubject.next(123);
    };
    app.element = { leafState: [], activeValidationGroups: [], config: { code: 'test' } };
    app.form = { controls: { test: null } };
    const eve = { config: { uiStyles: { attributes: { postEventOnChange: false } } } };
    spyOn(pageService, 'postOnChange').and.callThrough();
    app.antmControlValueChanged = new EventEmitter();
    app.ngOnInit();
    app.antmControlValueChanged.emit(eve);
    expect(pageService.postOnChange).not.toHaveBeenCalled();
  }));

});