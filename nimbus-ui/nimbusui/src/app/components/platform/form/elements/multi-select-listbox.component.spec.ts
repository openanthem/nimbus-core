'use strict';
import { TestBed, async } from '@angular/core/testing';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule  } from 'primeng/primeng';
import { FormsModule, ReactiveFormsModule, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { Subject } from 'rxjs';
import { of as observableOf,  Observable } from 'rxjs';

import { MultiSelectListBox } from './multi-select-listbox.component';
import { TooltipComponent } from '../../tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { ValidationUtils } from '../../validators/ValidationUtils';
import { InputLabel } from './input-label.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import * as data from '../../../../payload.json';

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

    notifyErrorEvent(a) {
        this.validationUpdate$.next(a);
    }
    processEvent(a, b, c, d) { }
    postOnChange(a, b, c) {  }
}

const declarations = [
  MultiSelectListBox,
  TooltipComponent,
  InputLabel
];
const imports = [
   ListboxModule,
   FormsModule, 
   ReactiveFormsModule,
   HttpModule,
   HttpClientModule,
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

describe('MultiSelectListBox', () => {
  configureTestSuite();
  setup(MultiSelectListBox, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<MultiSelectListBox>) {
    const fg = new FormGroup({});
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    fg.addControl(param.config.code, new FormControl(param.leafState, checks));
    this.hostComponent.form = fg;
    this.hostComponent.element = param;
    pageService = TestBed.get(PageService);
  });

  it('should create the MultiSelectListBox', async function (this: TestContext<MultiSelectListBox>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('serState() should update the formInp.element.leafState and call cd.markForCheck()', async function (this: TestContext<MultiSelectListBox>) {
    const formInp = { element: { leafState: '' } };
    const spy = spyOn((this.hostComponent as any).cd, 'markForCheck').and.returnValue('');
    this.hostComponent.setState('t', formInp);
    expect(formInp.element.leafState).toEqual('t');
    expect(spy).toHaveBeenCalled();
  });

  it('emitValueChangedEvent() should call controlValueChanged.emit with formcontrol.element', async function (this: TestContext<MultiSelectListBox>) {
    const controlValue: any = { emit: () => {} };
    this.hostComponent.controlValueChanged = controlValue;
    const formControl = { element: 't' };
    spyOn(this.hostComponent.controlValueChanged, 'emit').and.callThrough();
    this.hostComponent.emitValueChangedEvent(formControl, '');
    expect(this.hostComponent.controlValueChanged.emit).toHaveBeenCalled();
    expect(this.hostComponent.controlValueChanged.emit).toHaveBeenCalledWith('t');    
  });

    it('ngOnInit() should call the setState()', async function (this: TestContext<MultiSelectListBox>) {
      this.fixture.whenStable().then(() => {
        const values: any = [{ label: 'l', code: 'c' }];
        this.hostComponent.element.values = values;
        this.hostComponent.element.activeValidationGroups = [''];
        this.hostComponent.element.leafState = 'b';
        this.hostComponent.element.path = '/t';
        spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
        spyOn(this.hostComponent, 'setState').and.callThrough();
        this.hostComponent.ngOnInit();
        this.hostComponent.form.get('firstName').setValue('newValue');
        expect(this.hostComponent.setState).toHaveBeenCalled();
      });
    });
    
    it('ngOnInit() should call the setState and update the targetList', async function (this: TestContext<MultiSelectListBox>) {
      this.fixture.whenStable().then(() => {
        const values: any = [{ label: 'l', code: 'c' }];
        this.hostComponent.element.values = values;
        this.hostComponent.element.activeValidationGroups = [];
        this.hostComponent.element.leafState = null;
        this.hostComponent.element.path = '/t';
        spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
        spyOn(this.hostComponent, 'setState').and.callThrough();
        this.hostComponent.ngOnInit();
        this.hostComponent.form.get('firstName').setValue('newValue');
        expect(this.hostComponent.setState).toHaveBeenCalled();
        expect((this.hostComponent as any).targetList).toEqual([]);
      });
    });

    it('ngOnInit() should call the form.controls.firstName.setValue()', async function (this: TestContext<MultiSelectListBox>) {
      this.fixture.whenStable().then(() => {
        const values: any = [{ label: 'l', code: 'c' }];
        this.hostComponent.element.values = values;
        this.hostComponent.element.activeValidationGroups = [''];
        this.hostComponent.element.leafState = 'b';
        this.hostComponent.element.path = '/t';
        const eve = { config: { code: 'a' }, path: '/test', leafState: '' };
        spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
        spyOn(this.hostComponent.form.controls.firstName, 'setValue').and.callThrough();
        this.hostComponent.ngOnInit();
        pageService.logError(eve);
        expect(this.hostComponent.form.controls.firstName.setValue).toHaveBeenCalled();
      });
    });

    it('ngOnInit() should call the form.controls.firstName.reset()', async function (this: TestContext<MultiSelectListBox>) {
      this.fixture.whenStable().then(() => {
        const values: any = [{ label: 'l', code: 'c' }];
        this.hostComponent.element.values = values;
        this.hostComponent.element.activeValidationGroups = [''];
        this.hostComponent.element.leafState = 'b';
        this.hostComponent.element.path = '/t';
        const eve = { config: { code: 'firstName' }, path: '/test', leafState: null };
        spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
        spyOn(this.hostComponent.form.controls.firstName, 'reset').and.callThrough();
        this.hostComponent.ngOnInit();
        pageService.logError(eve);
        expect(this.hostComponent.form.controls.firstName.reset).toHaveBeenCalled();
      });
    });

    it('ngOnInit() should call the ValidationUtils.rebindValidations()', async function (this: TestContext<MultiSelectListBox>) {
      const values: any = [{ label: 'l', code: 'c' }];
      this.hostComponent.element.values = values;
      this.hostComponent.element.activeValidationGroups = [''];
      this.hostComponent.element.leafState = 'b';
      this.hostComponent.element.path = '/t';
      const eve = { activeValidationGroups: [1], config: { code: 'a' }, path: '/t', leafState: '' };
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue('test');
      spyOn(ValidationUtils, 'assessControlValidation').and.returnValue('');
      this.hostComponent.ngOnInit();
      pageService.notifyErrorEvent(eve);
      expect(ValidationUtils.rebindValidations).toHaveBeenCalled();
    });

    it('ngOnInit() should call the form.controls.firstName.setValidators', async function (this: TestContext<MultiSelectListBox>) {
      this.fixture.whenStable().then(() => {
        const values: any = [{ label: 'l', code: 'c' }];
        this.hostComponent.element.values = values;
        this.hostComponent.element.activeValidationGroups = [''];
        this.hostComponent.element.leafState = 'b';
        this.hostComponent.element.path = '/t';
        const eve = { activeValidationGroups: null, config: { code: 'firstName' }, path: '/t', leafState: '' };
        spyOn(ValidationUtils, 'rebindValidations').and.returnValue('test');
        spyOn(ValidationUtils, 'assessControlValidation').and.returnValue('');
        spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue('');
        spyOn(ValidationUtils, 'applyelementStyle').and.returnValue('');
        spyOn(this.hostComponent.form.controls.firstName, 'setValidators').and.callThrough();
        this.hostComponent.ngOnInit();
        pageService.notifyErrorEvent(eve);
        expect(this.hostComponent.form.controls.firstName.setValidators).toHaveBeenCalled();
      });
    });

    it('ngOnInit() should call the pageService.postOnChange()', async function (this: TestContext<MultiSelectListBox>) {
      const values: any = [{ label: 'l', code: 'c' }];
      this.hostComponent.element.values = values;
      this.hostComponent.element.activeValidationGroups = [''];
      this.hostComponent.element.leafState = 'b';
      this.hostComponent.element.path = '/t';
      const eve = { leafState: 's', path: '/t', config: { uiStyles: { attributes: { postEventOnChange: true } } } };
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
      spyOn(pageService, 'postOnChange').and.callThrough();
      this.hostComponent.ngOnInit();
      this.hostComponent.controlValueChanged.emit(eve);
      expect(pageService.postOnChange).toHaveBeenCalled();
    });

    it('ngOnInit() should call the pageService.processEvent()', async function (this: TestContext<MultiSelectListBox>) {
      this.fixture.whenStable().then(() => {
        const values: any = [{ label: 'l', code: 'c' }];
        this.hostComponent.element.values = values;
        this.hostComponent.element.activeValidationGroups = [''];
        this.hostComponent.element.leafState = 'b';
        this.hostComponent.element.path = '/t';
        this.hostComponent.element.config.uiStyles.attributes.postButtonUrl = '';
        const eve = { leafState: 's', path: '/t', config: { uiStyles: { attributes: { postEventOnChange: false, postButtonUrl: true } } } };
        spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
        spyOn(pageService, 'processEvent').and.callThrough();
        this.hostComponent.ngOnInit();
        this.hostComponent.controlValueChanged.emit(eve);
        expect(pageService.processEvent).toHaveBeenCalled();     
      }); 
    });

});
