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

import {
  HashLocationStrategy,
  Location,
  LocationStrategy
} from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { async, TestBed } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { fieldValueParam } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { AppInitService } from '../../../services/app.init.service';
import { ConfigService } from '../../../services/config.service';
import { CounterMessageService } from '../../../services/counter-message.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { LoggerService } from '../../../services/logger.service';
import { PageService } from '../../../services/page.service';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from '../../../services/session.store';
import { setup } from '../../../setup.spec';
import { ParamUtils } from '../../../shared/param-utils';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { NmMessageService } from './../../../services/toastmessage.service';
import { Label } from './label.component';
'use strict';

const declarations = [Label, TooltipComponent];
const imports = [HttpClientModule, HttpModule, StorageServiceModule];
const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  PageService,
  CustomHttpClient,
  NmMessageService,
  SessionStoreService,
  LoaderService,
  ConfigService,
  LoggerService,
  AppInitService,
  Location,
  CounterMessageService
];
let fixture, hostComponent;
describe('Label', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Label);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
  });

  it('should create the Label', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('app.cssClass should return labelClass', async(() => {
    hostComponent.labelClass = 'test';
    spyOn(ParamUtils, 'getLabelCss').and.returnValue('a');
    expect(hostComponent.cssClass).toEqual('a test');
  }));
});
