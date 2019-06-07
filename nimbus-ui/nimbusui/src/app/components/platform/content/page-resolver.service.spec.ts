/**
 * @license
 * Copyright 2016-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { HttpClient } from '@angular/common/http';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { async, TestBed } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import {
  ActivatedRoute,
  ActivatedRouteSnapshot,
  ParamMap,
  Params,
  Route,
  Router,
  UrlSegment
} from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Observable } from 'rxjs/Observable';
import { AppInitService } from '../../../services/app.init.service';
import { ConfigService } from '../../../services/config.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { LoggerService } from '../../../services/logger.service';
import { PageService } from '../../../services/page.service';
import { CUSTOM_STORAGE } from '../../../services/session.store';
import { BreadcrumbService } from './../breadcrumb/breadcrumb.service';
import { PageResolver } from './page-resolver.service';

let http,
  backend,
  service,
  rustate,
  breadcrumpservice,
  pageservice,
  wcservice,
  router,
  loggerService,
  activatedRoute;

const route = {
  _routerState: {
    url: '/test'
  },
  parent: {
    url: [
      {
        path: 'test'
      }
    ]
  },
  params: {
    pageId: 123
  }
};

class MockLoggerService {
  debug() {}
  info() {}
  error() {}
}

class MockPageService {
  getPageConfigById() {
    const res = {
      config: {
        code: 1234
      }
    };
    return new Promise((resolve, reject) => {
      resolve(res);
    });
  }
}

class MockBreadcrumbService {
  push(a, b, c) {}
}

class MockRouter {
  navigate() {}
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
        { provide: PageService, useClass: MockPageService },
        { provide: BreadcrumbService, useValue: breadcrumpservice },
        { provide: Router, useClass: MockRouter },
        { provide: 'JSNLOG', useValue: JL },
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        { provide: LoggerService, useClass: MockLoggerService },
        { provide: ActivatedRoute, useClass: MockActivatedRoute },
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
    router = TestBed.get(Router);
    loggerService = TestBed.get(LoggerService);
    activatedRoute = TestBed.get(ActivatedRoute);
    service = new PageResolver(
      pageservice,
      router,
      breadcrumpservice,
      activatedRoute,
      loggerService
    );
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
    let result = service.resolve(route, rustate);
    result.then(data => {
      expect(breadcrumpservice.push).toHaveBeenCalled();
    });
  }));

  it('resolve() should call router.navigate()', async(() => {
    spyOn(pageservice, 'getPageConfigById').and.returnValue(
      new Promise((resolve, reject) => {
        resolve(null);
      })
    );
    spyOn(router, 'navigate').and.callThrough();
    let result = service.resolve(route, rustate);
    result.then(data => {
      expect(router.navigate).toHaveBeenCalled();
    });
  }));
});
