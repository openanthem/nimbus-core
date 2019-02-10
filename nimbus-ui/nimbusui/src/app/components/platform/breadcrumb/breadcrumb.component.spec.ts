// As all methods are private. we wont be able test this component

// 'use strict';
// import { TestBed, async } from '@angular/core/testing';
// import { RouterTestingModule } from '@angular/router/testing'
// import { HttpModule } from '@angular/http';
// import { HttpClientTestingModule } from '@angular/common/http/testing';
// import { Observable } from 'rxjs/Observable';
// import 'rxjs/add/observable/of';
// import { Router, ActivatedRoute, Route, ActivatedRouteSnapshot, UrlSegment, Params, Data, ParamMap, PRIMARY_OUTLET } from '@angular/router';
// import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';

// import { BreadcrumbComponent } from './breadcrumb.component';
// import { BreadcrumbService } from './breadcrumb.service';
// import { PageService } from './../../../services/page.service';
// import { CustomHttpClient } from './../../../services/httpclient.service';
// import { LoaderService } from './../../../services/loader.service';
// import { ConfigService } from './../../../services/config.service';
// import { DomainFlowCmp } from './../../domain/domain-flow.component';
// import { SessionStoreService, CUSTOM_STORAGE } from './../../../services/session.store';

// let fixture, app, router, activatedRoute, breadcrumbService;

// export class MockActivatedRoute implements ActivatedRoute {
//     snapshot: ActivatedRouteSnapshot;
//     url: Observable<UrlSegment[]>;
//     params: Observable<Params>;
//     queryParams: Observable<Params>;
//     fragment: Observable<string>;
//     outlet: string;
//     component: any;
//     routeConfig: Route;
//     root: any = {
//         children: [{
//             outlet: PRIMARY_OUTLET,
//             component: 'test',
//             children: [],
//             snapshot: {
//                 params: {
//                     pageId: 1
//                 }
//             }
//         }]
//     };
//     parent: ActivatedRoute;
//     firstChild: ActivatedRoute;
//     children: ActivatedRoute[];
//     pathFromRoot: ActivatedRoute[];
//     data = Observable.of({
//             layout: 'test'
//       });
//     paramMap: Observable<ParamMap>;
//     queryParamMap: Observable<ParamMap>;
//   }

//   export class MockActivatedRoute1 implements ActivatedRoute {
//     snapshot: ActivatedRouteSnapshot;
//     url: Observable<UrlSegment[]>;
//     params: Observable<Params>;
//     queryParams: Observable<Params>;
//     fragment: Observable<string>;
//     outlet: string;
//     component: any;
//     routeConfig: Route;
//     root: any = {
//         children: [{
//             outlet: PRIMARY_OUTLET,
//             component: DomainFlowCmp,
//             children: [],
//             snapshot: {
//                 params: ''
//             }
//         }, {
//             outlet: '',
//             comsponent: DomainFlowCmp,
//             snapshot: {
//                 params: ''
//             }
//         },{
//             outlet: '',
//             component: '',
//             children: [],
//             snapshot: {
//                 params: ''
//             }
//         }]
//     };
//     parent: ActivatedRoute;
//     firstChild: ActivatedRoute;
//     children: ActivatedRoute[];
//     pathFromRoot: ActivatedRoute[];
//     data = Observable.of({
//             layout: 'test'
//       });
//     paramMap: Observable<ParamMap>;
//     queryParamMap: Observable<ParamMap>;
//   }

//   export class MockActivatedRoute2 implements ActivatedRoute {
//     snapshot: ActivatedRouteSnapshot;
//     url: Observable<UrlSegment[]>;
//     params: Observable<Params>;
//     queryParams: Observable<Params>;
//     fragment: Observable<string>;
//     outlet: string;
//     component: any;
//     routeConfig: Route;
//     root: any = {
//         children: [{
//             outlet: PRIMARY_OUTLET,
//             component: 'test',
//             children: [],
//             snapshot: {
//                 params: {
//                     pageId: 1
//                 }
//             }
//         }]
//     };
//     parent: ActivatedRoute;
//     firstChild: ActivatedRoute;
//     children: ActivatedRoute[];
//     pathFromRoot: ActivatedRoute[];
//     data = Observable.of({
//             layout: 'test'
//       });
//     paramMap: Observable<ParamMap>;
//     queryParamMap: Observable<ParamMap>;
//   }

// class MockRouter {
//     events = {
//         filter: () => {
//             return Observable.of('');
//         }
//     };
// }

// class MockBreadcrumbService {
//     isHomeRoute(a) {
//         return true;
//     }
//     getByPageId(a) {
//         console.log('getByPageId is being');
//         return '123';
//     }
//     getHomeBreadcrumb(a) { return 't'; }
// }

// class MockBreadcrumbService1 {
//     isHomeRoute(a) {
//         return true;
//     }
//     getByPageId(a) {
//         console.log('mock is getting called bs');
//         return 123;
//     }
//     getHomeBreadcrumb(a) { return 't'; }
// }

// describe('BreadcrumbComponent', () => {
//   beforeEach(() => {
//     TestBed.configureTestingModule({
//       declarations: [
//         BreadcrumbComponent
//        ],
//        imports: [
//         RouterTestingModule,
//         HttpModule,
//         HttpClientTestingModule,
//         StorageServiceModule
//        ],
//        providers: [
//         {provide: Router, useClass: MockRouter},
//         {provide: ActivatedRoute, useClass: MockActivatedRoute},
//         {provide: BreadcrumbService, useClass: MockBreadcrumbService},
//         { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
//         PageService,
//         CustomHttpClient,
//         LoaderService,
//         ConfigService
//        ]
//     }).compileComponents();
//     fixture = TestBed.createComponent(BreadcrumbComponent);
//     app = fixture.debugElement.componentInstance;
//     router = TestBed.get(Router);
//     activatedRoute = TestBed.get(ActivatedRoute);
//     breadcrumbService = TestBed.get(BreadcrumbService);
//   }));

//   it('should create the app', async(() => {
//     expect(app).toBeTruthy();
//   }));

//   it('ngOnInit() should call the _loadBreadcrumbs() method', async(() => {
//     spyOn(app, '_loadBreadcrumbs').and.callThrough();
//     app.ngOnInit();
//     expect(app._loadBreadcrumbs).toHaveBeenCalled();
//   }));

//   it('ngOnInit() should call the breadcrumbService.getHomeBreadcrumb() method', async(() => {
//     spyOn(breadcrumbService, 'getHomeBreadcrumb').and.callThrough();
//     spyOn(breadcrumbService, 'isHomeRoute').and.returnValue(false);
//     app.ngOnInit();
//     expect(breadcrumbService.getHomeBreadcrumb).toHaveBeenCalled();
//   }));

// });

// describe('BreadcrumbComponent', () => {
//     beforeEach(() => {
//       TestBed.configureTestingModule({
//         declarations: [
//           BreadcrumbComponent
//          ],
//          imports: [
//           RouterTestingModule,
//           HttpModule,
//           HttpClientTestingModule
//          ],
//          providers: [
//           {provide: Router, useClass: MockRouter},
//           {provide: ActivatedRoute, useClass: MockActivatedRoute1},
//           {provide: BreadcrumbService, useClass: MockBreadcrumbService1},
//           PageService,
//           CustomHttpClient,
//           LoaderService,
//           ConfigService
//          ]
//       }).compileComponents();
//       fixture = TestBed.createComponent(BreadcrumbComponent);
//       app = fixture.debugElement.componentInstance;
//       router = TestBed.get(Router);
//       activatedRoute = TestBed.get(ActivatedRoute);
//       breadcrumbService = TestBed.get(BreadcrumbService);
//     }));

//     it('ngOnInit() should call the breadcrumbService.getHomeBreadcrumb() method without pageId', async(() => {
//         spyOn(breadcrumbService, 'getHomeBreadcrumb').and.callThrough();
//         spyOn(breadcrumbService, 'getByPageId').and.returnValue('');
//         spyOn(breadcrumbService, 'isHomeRoute').and.returnValue(false);
//         app.ngOnInit();
//         expect(breadcrumbService.getHomeBreadcrumb).toHaveBeenCalled();
//       }));
  
//   });

//   describe('BreadcrumbComponent', () => {
//     beforeEach(() => {
//       TestBed.configureTestingModule({
//         declarations: [
//           BreadcrumbComponent
//          ],
//          imports: [
//           RouterTestingModule,
//           HttpModule,
//           HttpClientTestingModule
//          ],
//          providers: [
//           {provide: Router, useClass: MockRouter},
//           {provide: ActivatedRoute, useClass: MockActivatedRoute2},
//           {provide: BreadcrumbService, useClass: MockBreadcrumbService1},
//           PageService,
//           CustomHttpClient,
//           LoaderService,
//           ConfigService
//          ]
//       }).compileComponents();
//       fixture = TestBed.createComponent(BreadcrumbComponent);
//       app = fixture.debugElement.componentInstance;
//       router = TestBed.get(Router);
//       activatedRoute = TestBed.get(ActivatedRoute);
//       breadcrumbService = TestBed.get(BreadcrumbService);
//     }));

//     it('ngOnInit() should call the breadcrumbService.getHomeBreadcrumb() method with pageId as 123', async(() => {
//         spyOn(breadcrumbService, 'getHomeBreadcrumb').and.callThrough();
//         spyOn(breadcrumbService, 'getByPageId').and.returnValue('123');
//         spyOn(breadcrumbService, 'isHomeRoute').and.returnValue(false);
//         app.ngOnInit();
//         expect(breadcrumbService.getHomeBreadcrumb).toHaveBeenCalled();
//       }));

//       it('ngOnInit() should call the breadcrumbService.getHomeBreadcrumb() method with no pageid', async(() => {
//         spyOn(breadcrumbService, 'getHomeBreadcrumb').and.callThrough();
//         spyOn(breadcrumbService, 'getByPageId').and.returnValue('');
//         spyOn(breadcrumbService, 'isHomeRoute').and.returnValue(false);
//         app.ngOnInit();
//         expect(breadcrumbService.getHomeBreadcrumb).toHaveBeenCalled();
//       }));
  
//   });