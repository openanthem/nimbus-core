// 'use strict';
// import { TestBed, async } from '@angular/core/testing';
// import { HttpModule } from '@angular/http';
// import { HttpClientTestingModule } from '@angular/common/http/testing';
// import { Location } from '@angular/common';
// import { RouterTestingModule } from '@angular/router/testing';
// import { JL } from 'jsnlog';
// import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
// import { AngularSvgIconModule } from 'angular-svg-icon';

// import { Button } from './button.component';
// import { PageService } from '../../../../services/page.service';
// import { CustomHttpClient } from '../../../../services/httpclient.service';
// import { LoaderService } from '../../../../services/loader.service';
// import { ConfigService } from '../../../../services/config.service';
// import { LoggerService } from '../../../../services/logger.service';
// import { FileService } from '../../../../services/file.service';
// import { Subject } from 'rxjs';
// import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
// import { AppInitService } from '../../../../services/app.init.service';
// import { SvgComponent } from '../../svg/svg.component';
// import { Image } from '../../image.component';

// let fixture, app, location, pageService;

// class MockLocation {
//     back() { }
// }

// class MockPageService {
//     validationUpdate$: Subject<any>

//     constructor() {
//         this.validationUpdate$ = new Subject();
//     }
//     processEvent(a, b, c, d) {

//     }

//     logError(a) {
//         this.validationUpdate$.next(a);
//     }
// }

// describe('Button', () => {
//   beforeEach(async(() => {
//     TestBed.configureTestingModule({
//       declarations: [
//           Button,
//           SvgComponent,
//           Image
//        ],
//        imports: [
//            HttpModule,
//            HttpClientTestingModule,
//            RouterTestingModule,
//            StorageServiceModule,
//            AngularSvgIconModule
//        ],
//        providers: [
//            {provide: Location, useClass: MockLocation},
//            {provide: PageService, useClass: MockPageService},
//            { provide: 'JSNLOG', useValue: JL },
//            { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
//            CustomHttpClient,
//            LoaderService,
//            ConfigService,
//            LoggerService,
//            FileService,
//            AppInitService,
//            SessionStoreService
//        ]
//     }).compileComponents();
//     fixture = TestBed.createComponent(Button);
//     app = fixture.debugElement.componentInstance;
//     location = TestBed.get(Location);
//     pageService = TestBed.get(PageService);
//   }));

//     it('should create the app', async(() => {
//       expect(app).toBeTruthy();
//     }));

//     it('emitEvent() should update the location', async(() => {
//       app.element = { config: { uiStyles: { attributes: { browserBack: 123 } } } };
//       spyOn(location, 'back').and.callThrough();
//       app.emitEvent('eve');
//       expect(location.back).toHaveBeenCalled();
//     }));

//     it('emitEvent() should emit buttonClickEvent', async(() => {
//       app.element = { config: { uiStyles: { attributes: { browserBack: null } } } };
//       spyOn(app.buttonClickEvent, 'emit').and.callThrough();
//       app.emitEvent('eve');
//       expect(app.buttonClickEvent.emit).toHaveBeenCalled();
//     }));

//     it('ngOnInit() should update disabled property as false', async(() => {
//       app.element = { config: { code: 's', uiStyles: { attributes: { payload: 'a' } } }, enabled: true };
//       app.ngOnInit();
//       expect(app.disabled).toBeFalsy();
//     }));

//     it('ngOnInit() should update call pageService.processEvent()', async(() => {
//       app.element = { config: { code: 's', uiStyles: { attributes: { payload: 'a' } } }, enabled: true };
//       const eve = { element: { config: { uiStyles: { attributes: { b: 'a', method: 'method' } } }, path: 'test' } };
//       app.buttonClickEvent = new Subject();
//       spyOn(pageService, 'processEvent').and.callThrough();
//       app.ngOnInit();
//       app.buttonClickEvent.next(eve);
//       expect(pageService.processEvent).toHaveBeenCalled();
//     }));

//     it('ngOnInit() should update disabled property as true based on pageService.validationUpdate$ subject', async(() => {
//       app.element = { path: 'test', config: { code: 's', uiStyles: { attributes: { payload: 'a' } } }, enabled: true };
//       const eve = { path: 'test', enabled: false };
//       app.ngOnInit();
//       pageService.logError(eve);
//       expect(app.disabled).toBeTruthy();
//     }));

//     it('ngOnInit() should update disabled property as false based on pageService.validationUpdate$ subject', async(() => {
//       app.element = { path: 'test', config: { code: 's', uiStyles: { attributes: { payload: 'a' } } }, enabled: true };
//       const eve = { path: '1test', enabled: false };
//       app.ngOnInit();
//       pageService.logError(eve);
//       expect(app.disabled).toBeFalsy();
//     }));

//     it('checkObjectType() should return false', async(() => {
//       expect(app.checkObjectType('test', { test: 'test' })).toBeFalsy();
//     }));

//     it('checkObjectType() should return true', async(() => {
//       expect(app.checkObjectType('String', 'testtest1')).toBeTruthy();
//     }));

//     it('getFileParameter() should call checkObjectType()', async(() => {
//       const item = { a: [1] };
//       spyOn(app, 'checkObjectType').and.returnValue(true);
//       const key = app.getFileParameter(item);
//       expect(app.checkObjectType).toHaveBeenCalled();
//       expect(key).toEqual('a');
//     }));

//     it('getFileParameter() should return null', async(() => {
//       const item = {};
//       spyOn(app, 'checkObjectType').and.returnValue(true);
//       const key = app.getFileParameter(item);
//       expect(key).toBeFalsy();
//     }));

//     it('getFileParameter() should call checkObjectType() and return null', async(() => {
//       const item = { a: [1] };
//       spyOn(app, 'checkObjectType').and.returnValue(false);
//       const key = app.getFileParameter(item);
//       expect(app.checkObjectType).toHaveBeenCalled();
//       expect(key).toBeFalsy();
//     }));

//     it('getFileParameter() should call checkObjectType() and return null on empty array', async(() => {
//       const item = { a: [] };
//       spyOn(app, 'checkObjectType').and.returnValue(true);
//       const key = app.getFileParameter(item);
//       expect(app.checkObjectType).toHaveBeenCalled();
//       expect(key).toBeFalsy();
//     }));

//     it('getFileParameter() should return false if a is not File', async(() => {
//       const item = { a: [1] };
//       app.checkObjectType = (a, b) => {
//         if (a === 'File') {
//           return false;
//         }
//         return true;
//       };
//       const key = app.getFileParameter(item);
//       expect(key).toBeFalsy();
//     }));

//     it('onSubmit() should call pageService.processEvent()', async(() => {
//       app.form = { value: { fileId: '', name: '', fileControl: false } };
//       app.element = { path: '', config: { uiStyles: { attributes: { b: '' } } } };
//       spyOn(pageService, 'processEvent').and.returnValue('');
//       app.onSubmit();
//       expect(pageService.processEvent).toHaveBeenCalled();
//     }));

//     it('onSubmit() should call pageService.processEvent() without form.value.formControl also', async(() => {
//       app.form = { value: { fileId: '', name: '', fileControl: '' } };
//       app.element = { path: '', config: { uiStyles: { attributes: { b: '' } } } };
//       spyOn(pageService, 'processEvent').and.returnValue('');
//       app.onSubmit();
//       expect(pageService.processEvent).toHaveBeenCalled();
//     }));

//     it('reset() should call form.reset()', async(() => {
//       app.form = { reset: () => {} };
//       app.element = { config: { uiStyles: { attributes: { formReset: true } } } };
//       spyOn(app.form, 'reset').and.callThrough();
//       app.reset();
//       expect(app.form.reset).toHaveBeenCalled();
//     }));

//     it('reset() should not call form.reset(),', async(() => {
//       app.reset();
//       app.form = { reset: () => {} };
//       app.element = { config: { uiStyles: { attributes: { formReset: false } } } };
//       spyOn(app.form, 'reset').and.callThrough();
//       app.reset();
//       expect(app.form.reset).not.toHaveBeenCalled();
//     }));

//     it('getAllURLParams() should return null', async(() => {
//       expect(app.getAllURLParams('www.test.com')).toBeFalsy();
//     }));

// });