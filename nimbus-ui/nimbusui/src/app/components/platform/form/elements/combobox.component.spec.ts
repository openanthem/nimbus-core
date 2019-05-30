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
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { By } from '@angular/platform-browser';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { comboBoxElement } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { DropdownModule } from 'primeng/primeng';
import { SelectItemPipe } from '../../../../pipes/select-item.pipe';
import { AppInitService } from '../../../../services/app.init.service';
import { ConfigService } from '../../../../services/config.service';
import { CounterMessageService } from '../../../../services/counter-message.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { LoggerService } from '../../../../services/logger.service';
import { PageService } from '../../../../services/page.service';
import { ServiceConstants } from '../../../../services/service.constants';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from '../../../../services/session.store';
import { setup } from '../../../../setup.spec';
import { Param } from '../../../../shared/param-state';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { NmMessageService } from './../../../../services/toastmessage.service';
import { ComboBox } from './combobox.component';
import { InputLabel } from './input-label.component';
'use strict';

const declarations = [ComboBox, TooltipComponent, SelectItemPipe, InputLabel];
const imports = [
  DropdownModule,
  FormsModule,
  HttpClientModule,
  HttpModule,
  StorageServiceModule
];
const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  Location,
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  LoggerService,
  SessionStoreService,
  NmMessageService,
  AppInitService,
  CounterMessageService
];
let fixture, hostComponent;
describe('ComboBox', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComboBox);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = comboBoxElement as Param;
  });

  it('should create the ComboBox', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('nm-input-label should be created if the label is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.visible = true;
    fixture.detectChanges();
    const labelEle = document.getElementsByTagName('nm-input-label');
    expect(labelEle.length > 0).toBeTruthy();
  }));

  it('p-dropdown should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pDropDownEle = debugElement.query(By.css('p-dropdown'));
    expect(pDropDownEle).toBeTruthy();
  }));

  it('nm-input-label should not be created if the label is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle).toBeFalsy();
  }));
});
