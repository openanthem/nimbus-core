// 'use strict';
// import { TestBed, async } from '@angular/core/testing';
// import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
//     FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
//     ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule  } from 'primeng/primeng';
// import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// import { HttpModule } from '@angular/http';
// import { HttpClientModule } from '@angular/common/http';
// import { JL } from 'jsnlog';
// import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
// import { Subject } from 'rxjs';
// import { Observable } from 'rxjs/Observable';
// import 'rxjs/add/observable/of';

// import { MultiSelectListBox } from './multi-select-listbox.component';
// import { TooltipComponent } from '../../tooltip/tooltip.component';
// import { PageService } from '../../../../services/page.service';
// import { CustomHttpClient } from '../../../../services/httpclient.service';
// import { LoaderService } from '../../../../services/loader.service';
// import { ConfigService } from '../../../../services/config.service';
// import { LoggerService } from '../../../../services/logger.service';
// import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
// import { AppInitService } from '../../../../services/app.init.service';
// import { ValidationUtils } from '../../validators/ValidationUtils';
// import { InputLabel } from './input-label.component';

// let fixture, app, pageService;

// class MockPageService {
//     eventUpdate$: Subject<any>;
//     validationUpdate$: Subject<any>;

//     constructor() {
//         this.eventUpdate$ = new Subject();
//         this.validationUpdate$ = new Subject();
//     }

//     logError(a) {
//         this.eventUpdate$.next(a);
//     }

//     notifyErrorEvent(a) {
//         this.validationUpdate$.next(a);
//     }
//     processEvent(a, b, c, d) { }
//     postOnChange(a, b, c) {  }
// }

// describe('MultiSelectListBox', () => {
//   beforeEach(async(() => {
//     TestBed.configureTestingModule({
//       declarations: [
//           MultiSelectListBox,
//           TooltipComponent,
//           InputLabel
//        ],
//        imports: [
//            ListboxModule,
//            FormsModule, 
//            ReactiveFormsModule,
//            HttpModule,
//            HttpClientModule,
//            StorageServiceModule
//        ],
//        providers: [
//            { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
//            { provide: 'JSNLOG', useValue: JL },
//            { provide: PageService, useClass: MockPageService },
//            CustomHttpClient,
//            LoaderService,
//            ConfigService,
//            LoggerService,
//            AppInitService,
//            SessionStoreService
//        ]
//     }).compileComponents();
//     fixture = TestBed.createComponent(MultiSelectListBox);
//     app = fixture.debugElement.componentInstance;
//     pageService = TestBed.get(PageService);
//   }));

//     it('should create the app', async(() => {
//       expect(app).toBeTruthy();
//     }));

//     it('serState() should update the formInp.element.leafState and call cd.markForCheck()', async(() => {
//       const formInp = { element: { leafState: '' } };
//       spyOn(app.cd, 'markForCheck').and.returnValue('');
//       app.setState('t', formInp);
//       expect(formInp.element.leafState).toEqual('t');
//       expect(app.cd.markForCheck).toHaveBeenCalled();
//     }));

//     it('emitValueChangedEvent() should call controlValueChanged.emit with formcontrol.element', async(() => {
//       app.controlValueChanged = { emit: () => {} };
//       const formControl = { element: 't' };
//       spyOn(app.controlValueChanged, 'emit').and.callThrough();
//       app.emitValueChangedEvent(formControl, '');
//       expect(app.controlValueChanged.emit).toHaveBeenCalled();
//       expect(app.controlValueChanged.emit).toHaveBeenCalledWith('t');
//     }));

//     it('ngOnInit() should call the setState()', async(() => {
//       app.element = { values: [{ label: 'l', code: 'c' }], config: { code: 'a' }, activeValidationGroups: [''], leafState: 'b', path: '/t' };
//       app.form = { controls: { a: { setValue: a => {}, reset: () => {}, valueChanges: Observable.of('') } } };
//       spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
//       spyOn(app, 'setState').and.callThrough();
//       app.ngOnInit();
//       expect(app.setState).toHaveBeenCalled();
//     }));

//     it('ngOnInit() should call the setState and update the targetList', async(() => {
//       app.element = { values: [{ label: 'l', code: 'c' }], config: { code: 'a' }, activeValidationGroups: [], leafState: null, path: '/t' };
//       app.form = { controls: { a: { setValue: a => {}, reset: () => {}, valueChanges: Observable.of('') } } };
//       spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
//       spyOn(app, 'setState').and.callThrough();
//       app.ngOnInit();
//       expect(app.setState).toHaveBeenCalled();
//       expect(app.targetList).toEqual([]);
//     }));

//     it('ngOnInit() should call the form.controls.a.setValue()', async(() => {
//       app.element = { values: [{ label: 'l', code: 'c' }], config: { code: 'a' }, activeValidationGroups: [''], leafState: 'b', path: '/t' };
//       app.form = { controls: { a: { setValue: a => {}, reset: () => {}, valueChanges: Observable.of('') } } };
//       const eve = { config: { code: 'a' }, path: '/test', leafState: '' };
//       spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
//       spyOn(app.form.controls.a, 'setValue').and.callThrough();
//       app.ngOnInit();
//       pageService.logError(eve);
//       expect(app.form.controls.a.setValue).toHaveBeenCalled();
//     }));

//     it('ngOnInit() should call the form.controls.a.reset()', async(() => {
//       app.element = { values: [{ label: 'l', code: 'c' }], config: { code: 'a' }, activeValidationGroups: [''], leafState: 'b', path: '/t' };
//       app.form = { controls: { a: { setValue: a => {}, reset: () => {}, valueChanges: Observable.of('') } } };
//       const eve = { config: { code: 'a' }, path: '/test', leafState: null };
//       spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
//       spyOn(app.form.controls.a, 'reset').and.callThrough();
//       app.ngOnInit();
//       pageService.logError(eve);
//       expect(app.form.controls.a.reset).toHaveBeenCalled();
//     }));

//     it('ngOnInit() should call the ValidationUtils.rebindValidations()', async(() => {
//       app.element = { values: [{ label: 'l', code: 'c' }], config: { code: 'a' }, activeValidationGroups: [''], leafState: 'b', path: '/t' };
//       app.form = { controls: { a: { setValue: a => {}, reset: () => {}, valueChanges: Observable.of(''), setValidators: () => {} } } };
//       const eve = { activeValidationGroups: [1], config: { code: 'a' }, path: '/t', leafState: '' };
//       spyOn(ValidationUtils, 'rebindValidations').and.returnValue('test');
//       spyOn(ValidationUtils, 'assessControlValidation').and.returnValue('');
//       spyOn(app.form.controls.a, 'setValue').and.callThrough();
//       app.ngOnInit();
//       pageService.notifyErrorEvent(eve);
//       expect(ValidationUtils.rebindValidations).toHaveBeenCalled();
//     }));

//     it('ngOnInit() should call the form.controls.a.setValidators', async(() => {
//       app.element = { values: [{ label: 'l', code: 'c' }], config: { code: 'a' }, activeValidationGroups: [''], leafState: 'b', path: '/t' };
//       app.form = { controls: { a: { setValue: a => {}, reset: () => {}, valueChanges: Observable.of(''), setValidators: () => {} } } };
//       const eve = { activeValidationGroups: null, config: { code: 'a' }, path: '/t', leafState: '' };
//       spyOn(ValidationUtils, 'rebindValidations').and.returnValue('test');
//       spyOn(ValidationUtils, 'assessControlValidation').and.returnValue('');
//       spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue('');
//       spyOn(ValidationUtils, 'applyelementStyle').and.returnValue('');
//       spyOn(app.form.controls.a, 'setValidators').and.callThrough();
//       app.ngOnInit();
//       pageService.notifyErrorEvent(eve);
//       expect(app.form.controls.a.setValidators).toHaveBeenCalled();
//     }));

//     it('ngOnInit() should call the pageService.postOnChange()', async(() => {
//       app.element = { values: [{ label: 'l', code: 'c' }], config: { code: 'a' }, activeValidationGroups: [''], leafState: 'b', path: '/t' };
//       app.form = { controls: { a: { setValue: a => {}, reset: () => {}, valueChanges: Observable.of('') } } };
//       const eve = { leafState: 's', path: '/t', config: { uiStyles: { attributes: { postEventOnChange: true } } } };
//       spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
//       spyOn(pageService, 'postOnChange').and.callThrough();
//       app.ngOnInit();
//       app.controlValueChanged.emit(eve);
//       expect(pageService.postOnChange).toHaveBeenCalled();
//     }));

//     it('ngOnInit() should call the pageService.processEvent()', async(() => {
//       app.element = { values: [{ label: 'l', code: 'c' }], config: { code: 'a', uiStyles: { attributes: { postButtonUrl: '' } } }, activeValidationGroups: [''], leafState: 'b', path: '/t' };
//       app.form = { controls: { a: { setValue: a => {}, reset: () => {}, valueChanges: Observable.of('') } } };
//       const eve = { leafState: 's', path: '/t', config: { uiStyles: { attributes: { postEventOnChange: false, postButtonUrl: true } } } };
//       spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
//       spyOn(pageService, 'processEvent').and.callThrough();
//       app.ngOnInit();
//       app.controlValueChanged.emit(eve);
//       expect(pageService.processEvent).toHaveBeenCalled();
//     }));

// });
