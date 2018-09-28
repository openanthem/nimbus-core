// 'use strict';
// import { TestBed, async } from '@angular/core/testing';
// import { HttpModule } from '@angular/http';
// import { HttpClientTestingModule } from '@angular/common/http/testing';
// import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
// import { JL } from 'jsnlog';
// import { Subject } from 'rxjs';
// import { Observable } from 'rxjs/Observable';
// import 'rxjs/add/observable/of';

// import { MultiselectCard } from './multi-select-card.component';
// import { PageService } from '../../../../services/page.service';
// import { CustomHttpClient } from '../../../../services/httpclient.service';
// import { LoaderService } from '../../../../services/loader.service';
// import { ConfigService } from '../../../../services/config.service';
// import { LoggerService } from '../../../../services/logger.service';
// import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
// import { AppInitService } from '../../../../services/app.init.service';

// let fixture, app, pageService;

// class MockPageService {
//     eventUpdate$: Subject<any>;

//     constructor() {
//         this.eventUpdate$ = new Subject();
//     }
//     postOnChange(a, b, c) { }
//     logError(a) {
//         this.eventUpdate$.next(a);
//     }
// }

// describe('MultiselectCard', () => {
//   beforeEach(async(() => {
//     TestBed.configureTestingModule({
//       declarations: [
//           MultiselectCard
//        ],
//        imports: [
//            HttpModule,
//            HttpClientTestingModule,
//            StorageServiceModule
//        ],
//        providers: [
//           { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
//           { provide: 'JSNLOG', useValue: JL },
//           {provide: PageService, useClass: MockPageService},
//            CustomHttpClient,
//            LoaderService,
//            ConfigService,
//            LoggerService,
//            AppInitService,
//            SessionStoreService
//        ]
//     }).compileComponents();
//     fixture = TestBed.createComponent(MultiselectCard);
//     app = fixture.debugElement.componentInstance;
//     pageService = TestBed.get(PageService);
//   }));

//     it('should create the app', async(() => {
//       expect(app).toBeTruthy();
//     }));

//     it('set value() should update the value property', async(() => {
//       app.value = 'test';
//       expect(app.value).toEqual('test');
//     }));

//     it('registerOnChange() should update the onChange property', async(() => {
//       app.registerOnChange('test');
//       expect(app.onChange).toEqual('test');
//     }));

//     it('writeValue() should update the value property', async(() => {
//       app.writeValue('test');
//       app.writeValue('test');
//       expect(app.value).toEqual('test');
//     }));

//     it('registerOnTouched() should update the onTouched property', async(() => {
//       app.registerOnTouched('test');
//       expect(app.onTouched).toEqual('test');
//     }));

//     it('toggleChecked() should return true', async(() => {
//       app.selectedOptions[0] = 'test';
//       expect(app.toggleChecked('test')).toBeTruthy();
//     }));

//     it('toggleChecked() should return false', async(() => {
//       app.selectedOptions[0] = 'test';
//       expect(app.toggleChecked('test1')).toBeFalsy();
//     }));

//     it('selectOption() should update the value property based on valid argumnet', async(() => {
//       app.selectedOptions[0] = 'test';
//       app.selectOption('test', '');
//       expect(app.value).toEqual(app.selectedOptions);
//     }));

//     it('selectOption() should update the value property', async(() => {
//       app.selectedOptions[0] = 'test';
//       app.selectOption('123', '');
//       expect(app.value).toEqual(app.selectedOptions);
//     }));

//     it('setState() should call the pageService.postOnChange()', async(() => {
//       app.element = { leafState: '', config: { uiStyles: { attributes: { postEventOnChange: true } } } };
//       spyOn(pageService, 'postOnChange').and.callThrough();
//       app.setState('t');
//       expect(pageService.postOnChange).toHaveBeenCalled();
//     }));

//     it('ngOnInit() should update the selectedOptions and call form.controls.a.setValue()', async(() => {
//       app.element = { leafState: '', config: { code: 't' }, path: 'test' };
//       const eve = { config: { code: 'a' }, path: 'test', leafState: '' };
//       app.form = { controls: { t: { valueChanges: Observable.of('') }, a: { setValue: a => {} } } };
//       spyOn(app, 'setState').and.returnValue('');
//       spyOn(app.form.controls.a, 'setValue').and.callThrough();
//       app.ngOnInit();
//       pageService.logError(eve);
//       expect(app.selectedOptions).toEqual('');
//       expect(app.form.controls.a.setValue).toHaveBeenCalled();
//     }));

// });