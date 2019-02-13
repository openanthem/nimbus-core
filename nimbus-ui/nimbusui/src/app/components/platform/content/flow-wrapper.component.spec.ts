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


import { NmMessageService } from './../../../services/toastmessage.service';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {
  Router,
  ActivatedRoute,
  Route,
  ActivatedRouteSnapshot,
  UrlSegment,
  Params,
  Data,
  ParamMap
} from '@angular/router';
import { of as observableOf,  Observable } from 'rxjs';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';

import { FlowWrapper } from './flow-wrapper.component';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { Subject } from 'rxjs';
import { LoggerService } from '../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { AppInitService } from '../../../services/app.init.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';

let pageService, configService, router;

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
    children: []
  };
  parent: ActivatedRoute;
  firstChild: ActivatedRoute;
  children: ActivatedRoute[];
  pathFromRoot: ActivatedRoute[];
  data = observableOf({
    domain: 'test'
  });
  paramMap: Observable<ParamMap>;
  queryParamMap: Observable<ParamMap>;
}

class MockPageService {
  public config$: Subject<any>;

  constructor() {
    this.config$ = new Subject();
  }

  loadFlowConfig(a) {}
  loadDefaultPageForConfig(a) {}
  logError(a) {
    this.config$.next(a);
  }
  traverseFlowConfig(a, b) {}
}

class MockConfigService {
  getFlowConfig(a) {
    return a;
  }
}

class MockRouter {
  navigate() {}
}

const declarations = [FlowWrapper];
const imports = [RouterTestingModule, HttpModule, HttpClientTestingModule, StorageServiceModule];
const providers = [
  { provide: PageService, useClass: MockPageService },
  { provide: ConfigService, useClass: MockConfigService },
  { provide: Router, useClass: MockRouter },
  { provide: ActivatedRoute, useClass: MockActivatedRoute },
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  CustomHttpClient,
  NmMessageService,
  LoaderService,
  LoggerService,
  AppInitService
];
let fixture, hostComponent;
describe('FlowWrapper', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(FlowWrapper);
    hostComponent = fixture.debugElement.componentInstance;
    pageService = TestBed.get(PageService);
    configService = TestBed.get(ConfigService);
    router = TestBed.get(Router);
  });

  it('should create the FlowWrapper',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  // it('ngOnInit() call router.navigate() and pageService.loadDefaultPageForConfig()',  async(() => {
  //   const test = { pageConfig: { config: { code: 123, uiStyles: { attributes: { route: 'testRoute' } } } } };
  //   spyOn(router, 'navigate').and.callThrough();
  //   spyOn(pageService, 'loadDefaultPageForConfig').and.callThrough();
  //   hostComponent.ngOnInit();
  //   pageService.logError(test);
  //   expect(router.navigate).toHaveBeenCalled();
  //   expect(pageService.loadDefaultPageForConfig).toHaveBeenCalled();
  // }));

  // it('ngOnInit() call router.navigate() and pageService.loadFlowConfig()',  async(() => {
  //   const test = { pageConfig: { config: { code: 123, uiStyles: { attributes: {} } } } };
  //   spyOn(router, 'navigate').and.callThrough();
  //   spyOn(pageService, 'loadFlowConfig').and.callThrough();
  //   spyOn(configService, 'getFlowConfig').and.returnValue(undefined);
  //   hostComponent.ngOnInit();
  //   pageService.logError(test);
  //   expect(router.navigate).toHaveBeenCalled();
  //   expect(pageService.loadFlowConfig).toHaveBeenCalled();
  // }));

});
