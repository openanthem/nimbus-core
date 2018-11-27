import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing'
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot, ActivatedRoute, Route, UrlSegment, Params, Data, ParamMap } from '@angular/router';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { Observable } from 'rxjs/Observable';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { PageResolver } from './page-resolver.service';
import { PageService } from '../../../services/page.service';
import { WebContentSvc } from './../../../services/content-management.service';
import { BreadcrumbService } from './../breadcrumb/breadcrumb.service'
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { AppInitService } from '../../../services/app.init.service';

let http, backend, service, rustate, breadcrumpservice, pageservice, wcservice, router, loggerService, activatedRoute;

const route = {
    _routerState: {
        url: '/test'
    },
    parent: {
        url: [{
            path: 'test'
        }]
    },
    params: {
        pageId: 123
    }
};

class MockLoggerService {
    debug() { }
    info() { }
    error() { }
}

class MockPageService {

    getPageConfigById() {
        const res = {
            config: {
                code: 1234
            }
        }
        return new Promise(
            (resolve, reject) => {
                resolve(res);
            }
        );
    }

}

class MockWebContentSvc {
    findLabelContent(a) {
        const res = {
            text: 'testing'
        };
        return res;
    }
}

class MockBreadcrumbService {
    push(a, b, c) {
     }
}

class MockRouter {
    navigate() {
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
    root: any;
    parent: ActivatedRoute;
    firstChild: ActivatedRoute;
    children: ActivatedRoute[];
    pathFromRoot: ActivatedRoute[];
    data: any = {
      value: {
        layout: 'test'
      }
    };
    paramMap: Observable<ParamMap>;
    queryParamMap: Observable<ParamMap>;
  }

describe('PageResolver', () => {
  beforeEach(() => {
    breadcrumpservice = new MockBreadcrumbService();
    TestBed.configureTestingModule({
      imports: [ 
          HttpClientTestingModule, 
          HttpModule,
          RouterTestingModule,
          StorageServiceModule
        ],
      providers: [
        {provide: PageService, useClass: MockPageService},
        {provide: WebContentSvc, useClass: MockWebContentSvc},
        {provide: BreadcrumbService, useValue: breadcrumpservice},
        {provide: Router, useClass: MockRouter},
        { provide: 'JSNLOG', useValue: JL },
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        {provide: LoggerService, useClass: MockLoggerService},
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        PageResolver,
        CustomHttpClient,
        LoaderService,
        ConfigService,
        AppInitService
        ],
        schemas: [NO_ERRORS_SCHEMA]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    pageservice = TestBed.get(PageService);
    wcservice = TestBed.get(WebContentSvc);
    router = TestBed.get(Router);
    loggerService = TestBed.get(LoggerService);
    activatedRoute = TestBed.get(ActivatedRoute);
    service = new PageResolver(pageservice, router, breadcrumpservice, wcservice, activatedRoute, loggerService );
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('resolve() should call breadcrumpservice.push()', async(() => {
    let spy = spyOn(breadcrumpservice, 'push').and.callThrough();
    let result = service.resolve(route, rustate);
    result.then(data => {
        expect(breadcrumpservice.push).toHaveBeenCalled();
    });
  })); 

  it('resolve() should call breadcrumpservice.push() without labelText', async(() => {
    let spy = spyOn(breadcrumpservice, 'push').and.callThrough();
    spyOn(wcservice, 'findLabelContent').and.returnValue({});
    let result = service.resolve(route, rustate);
    result.then(data => {
        expect(breadcrumpservice.push).toHaveBeenCalled();
    });
  }));

  it('resolve() should call router.navigate()', async(() => {
    spyOn(wcservice, 'findLabelContent').and.returnValue({});
    spyOn(pageservice, 'getPageConfigById').and.returnValue(new Promise(
        (resolve, reject) => {
            resolve(null);
        }
    ));
    spyOn(router, 'navigate').and.callThrough();
    let result = service.resolve(route, rustate);
    result.then(data => {
        expect(router.navigate).toHaveBeenCalled();
    });
  }));

});