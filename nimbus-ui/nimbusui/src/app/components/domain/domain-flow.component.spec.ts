'use strict';
import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing'
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { Router, ActivatedRoute, Route, ActivatedRouteSnapshot, UrlSegment, Params, Data, ParamMap, PRIMARY_OUTLET } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';

import { DomainFlowCmp } from './domain-flow.component';
import { BreadcrumbComponent } from '../platform/breadcrumb/breadcrumb.component';
import { SubHeaderCmp } from '../platform/sub-header.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { CustomHttpClient } from '../../services/httpclient.service';
import { WebContentSvc } from '../../services/content-management.service';
import { PageService } from '../../services/page.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { BreadcrumbService } from '../platform/breadcrumb/breadcrumb.service';
import { LayoutService } from '../../services/layout.service';
import { LoggerService } from '../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../services/session.store';
import { AppInitService } from '../../services/app.init.service'

let app, fixture, layoutservice, pageservice, router, route;

class MockLayoutService {
  public layout$: Subject<any>;

  constructor() {
    this.layout$ = new Subject();
  }

  getLayout(a) {    }
  parseLayoutConfig(result) {
    this.layout$.next(result);
  }
}

class MockPageService {
  public config$: Subject<any>;

  constructor() {
    this.config$ = new Subject();
  }

  logError(res) {
    this.config$.next(res);
  }

}

export class MockActivatedRoute implements ActivatedRoute {
    snapshot: ActivatedRouteSnapshot;
    url: Observable<UrlSegment[]>;
    params: Observable<Params>;
    queryParams: Observable<Params>;
    fragment: Observable<string>;
    outlet: string;
    component: any;
    routeConfig: Route;
    root: any = {
        children: [{
            outlet: PRIMARY_OUTLET,
            component: 'test',
            children: [],
            snapshot: {
                params: {
                    pageId: 1
                }
            }
        }]
    };
    parent: ActivatedRoute;
    firstChild: ActivatedRoute;
    children: ActivatedRoute[];
    pathFromRoot: ActivatedRoute[];
    data = Observable.of({
            layout: 'test'
      });
    paramMap: Observable<ParamMap>;
    queryParamMap: Observable<ParamMap>;
  }

  class MockLoggerService {
    debug() { }
    info() { }
    error() { }
}

  export class MockActivatedRoute1 implements ActivatedRoute {
    snapshot: ActivatedRouteSnapshot;
    url: Observable<UrlSegment[]>;
    params: Observable<Params>;
    queryParams: Observable<Params>;
    fragment: Observable<string>;
    outlet: string;
    component: any;
    routeConfig: Route;
    root: any = {
        children: [{
            outlet: PRIMARY_OUTLET,
            component: 'test',
            children: [],
            snapshot: {
                params: {
                    pageId: 1
                }
            }
        }]
    };    
    parent: ActivatedRoute;
    firstChild: ActivatedRoute;
    children: ActivatedRoute[];
    pathFromRoot: ActivatedRoute[];
    data = Observable.of({
            
      });
    paramMap: Observable<ParamMap>;
    queryParamMap: Observable<ParamMap>;
  }

  class MockRouter {
    events = {
      filter: () => {
        return Observable.of({         })
      }
    };
    navigate() {    }
  }

describe('DomainFlowCmp', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          DomainFlowCmp,
          BreadcrumbComponent,
          SubHeaderCmp,
          DateTimeFormatPipe
       ],
       imports: [
           RouterTestingModule,
           HttpClientModule,
           HttpModule,
           StorageServiceModule
       ],
       providers: [
           {provide: LayoutService, useClass: MockLayoutService},
           {provide: ActivatedRoute, useClass: MockActivatedRoute},
           {provide: PageService, useClass: MockPageService},
           {provide: Router, useClass: MockRouter},
           { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
           { provide: 'JSNLOG', useValue: JL },
           {provide: LoggerService, useClass: MockLoggerService},
           CustomHttpClient,
           WebContentSvc,
           LoaderService,
           ConfigService,
           BreadcrumbService,
           SessionStoreService,
           AppInitService
        ]
    }).compileComponents();
    fixture = TestBed.createComponent(DomainFlowCmp);
    app = fixture.debugElement.componentInstance;
    layoutservice = TestBed.get(LayoutService);
    pageservice = TestBed.get(PageService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('ngOnInit() should not update main-content', async(() => {
    spyOn(document, 'getElementById').and.returnValue({ 
        classList: {
            add: () => {
            }
        }
     });
    const res = {
      topBar: {
        headerMenus: 'theaderMenus',
      }
    };
    app.ngOnInit();
    layoutservice.parseLayoutConfig(res);
    expect(document.getElementById).not.toHaveBeenCalled();
  }));

  it('ngOnInit() should call router.navigate', async(() => {
    spyOn(document, 'getElementById').and.returnValue({ 
        classList: {
            remove: () => {
            },
            add: () => {
            }
        }
    });
   const res = {
      pageConfig: {
        config: {
          code: 321
        }
      }
    };
    spyOn(router, 'navigate').and.callThrough();
    app.ngOnInit();
    pageservice.logError(res);
    expect(router.navigate).toHaveBeenCalled();
  }));

  it('ngOnInit() should not call router.navigate', async(() => {
    spyOn(document, 'getElementById').and.returnValue({
      classList: {
        remove: () => {},
        add: () => {}
      }
    });
    const res = {};
    spyOn(router, 'navigate').and.callThrough();
    app.ngOnInit();
    pageservice.logError(res);
    expect(router.navigate).not.toHaveBeenCalled();
  }));

});

describe('DomainFlowCmp', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          DomainFlowCmp,
          BreadcrumbComponent,
          SubHeaderCmp,
          DateTimeFormatPipe
       ],
       imports: [
           RouterTestingModule,
           HttpClientModule,
           HttpModule,
           StorageServiceModule
       ],
       providers: [
           {provide: LayoutService, useClass: MockLayoutService},
           {provide: ActivatedRoute, useClass: MockActivatedRoute1},
           {provide: PageService, useClass: MockPageService},
           {provide: Router, useClass: MockRouter},
           { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
           CustomHttpClient,
           WebContentSvc,
           LoaderService,
           ConfigService,
           BreadcrumbService,
           LoggerService,
           SessionStoreService
        ]
    }).compileComponents();
    fixture = TestBed.createComponent(DomainFlowCmp);
    app = fixture.debugElement.componentInstance;
    layoutservice = TestBed.get(LayoutService);
    pageservice = TestBed.get(PageService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);
  }));

  it('ngOnInit should not call layoutservice.getLayout()', async(() => {
    spyOn(layoutservice, 'getLayout').and.callThrough();
    spyOn(document, 'getElementById').and.returnValue({
      classList: {
          remove: () => {
          },
          add: () => {
          }
      }
  });
    app.ngOnInit();
    expect(layoutservice.getLayout).not.toHaveBeenCalled();
  }));

});