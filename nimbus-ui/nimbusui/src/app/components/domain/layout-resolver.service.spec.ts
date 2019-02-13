/**
 * @license
 * Copyright 2016-2018 the original author or authors.
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


import { NmMessageService } from './../../services/toastmessage.service';
import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing'
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';

import { LayoutResolver } from './layout-resolver.service';
import { PageService } from '../../services/page.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { BreadcrumbService } from '../platform/breadcrumb/breadcrumb.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../services/session.store';
import { LoggerService } from '../../services/logger.service';
import { AppInitService } from '../../services/app.init.service'

class MockConfigService {
    getFlowConfig(a) {
        const test = {
            model: 123
        }
        return test;
    }
}

class MockConfigSvc {
    getFlowConfig(a) {
        const test = {        }
        return test;
    }
}

class MockRouterStateSnapshot {
    firstChild: any;
    constructor() {
        this.firstChild = {
            params: {
                pageId: 123
            }
        }
    }
}

class MockPageService {
    public layout$: Subject<any>;
    constructor() {
        this.layout$ = new Subject<any>();
    }
    getLayoutConfigForFlow(a) {
        this.layout$.next({test:123});
    }
    getFlowLayoutConfig(a) {
        return new Promise(
            (resolve, reject) => {
                resolve('abcd');
            }
        );
    }
}

class MockLoggerService {
    debug() { }
}

let http, backend, service, pagesvc, route, state, configsvc;

describe('LayoutResolver', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, HttpModule, RouterTestingModule, StorageServiceModule],
      providers: [
        LayoutResolver,
        { provide: PageService, useClass: MockPageService },
        { provide: RouterStateSnapshot, useClass: MockRouterStateSnapshot },
        { provide: ConfigService, useClass: MockConfigService },
        {
          provide: ActivatedRouteSnapshot,
          useValue: {
            params: {
              domain: "test"
            },
            firstChild: {
              params: {
                pageId: 123
              }
            }
          }
        },
        {provide: ConfigService, useClass: MockConfigService},
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        { provide: 'JSNLOG', useValue: JL },
        {provide: LoggerService, useClass: MockLoggerService},
        CustomHttpClient,
        LoaderService,
        BreadcrumbService,
        NmMessageService,
        SessionStoreService,
        AppInitService
      ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(LayoutResolver);
    pagesvc = TestBed.get(PageService);
    route = TestBed.get(ActivatedRouteSnapshot);
    state = TestBed.get(RouterStateSnapshot);
    configsvc = TestBed.get(ConfigService);
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('resolve() should call pageservice flowLayoutConfig', async(() => {
      spyOn(pagesvc, 'getFlowLayoutConfig').and.callThrough();
      service.resolve(route, state);
      expect(pagesvc.getFlowLayoutConfig).toHaveBeenCalled();
  }));

  it('resolve() should call pageservice flowLayoutConfig without routeToDefaultPage flag', async(() => {
    spyOn(pagesvc, 'getFlowLayoutConfig').and.callThrough();
    route.firstChild.params = {};
    service.resolve(route, state);
    expect(pagesvc.getFlowLayoutConfig).toHaveBeenCalled(); 
  }));

  it('resolve() should call pageservice getLayoutConfigForFlow', async(() => {
    spyOn(configsvc, 'getFlowConfig').and.returnValue({    });
    spyOn(pagesvc, 'getLayoutConfigForFlow').and.callThrough();
    service.resolve(route, state);
    expect(pagesvc.getLayoutConfigForFlow).toHaveBeenCalled();
  }));

});