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
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { Label } from './label.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { WebContentSvc } from '../../../services/content-management.service';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { AppInitService } from '../../../services/app.init.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import { Param } from '../../../shared/param-state';
import { fieldValueParam } from 'mockdata';

const declarations = [
  Label,
  TooltipComponent
];
const imports = [
   HttpClientModule,
    HttpModule,
    StorageServiceModule
];
const providers = [
{ provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
{ provide: 'JSNLOG', useValue: JL },
{ provide: LocationStrategy, useClass: HashLocationStrategy },
WebContentSvc,
PageService,
CustomHttpClient,
NmMessageService,
SessionStoreService,
LoaderService,
ConfigService,
LoggerService,
AppInitService,
Location
];
let fixture, hostComponent;
describe('Label', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Label);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
  });

  it('should create the Label', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('app.cssClass should return labelClass',async(() => {
    hostComponent.labelClass = 'test';
    hostComponent.getCssClass = () => { return 'a' }
    expect(hostComponent.cssClass).toEqual('test')
  }));

});