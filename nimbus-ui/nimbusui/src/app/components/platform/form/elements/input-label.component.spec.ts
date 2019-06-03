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
import { labelElement } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { CalendarModule } from 'primeng/primeng';
import { AppInitService } from '../../../../services/app.init.service';
import { ConfigService } from '../../../../services/config.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { LoggerService } from '../../../../services/logger.service';
import { PageService } from '../../../../services/page.service';
import { ServiceConstants } from '../../../../services/service.constants';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from '../../../../services/session.store';
import { WindowRefService } from '../../../../services/window-ref.service';
import { setup } from '../../../../setup.spec';
import { Param } from '../../../../shared/param-state';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { NmMessageService } from './../../../../services/toastmessage.service';
import { InputLabel } from './input-label.component';
'use strict';

const declarations = [InputLabel, TooltipComponent, InputLabel];
const imports = [
  CalendarModule,
  FormsModule,
  HttpModule,
  HttpClientModule,
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
  AppInitService,
  NmMessageService,
  WindowRefService
];

let fixture, hostComponent;

describe('InputLabel', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InputLabel);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = labelElement as Param;
  });

  it('should create the InputLabel', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('nm-tooltip should be created if helpText is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const tooltipEle = debugElement.query(By.css('nm-tooltip'));
    expect(tooltipEle).toBeTruthy();
  }));

  it('label should be created if the label is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('label'));
    expect(labelEle).toBeTruthy();
  }));

  it('label should be updated if they is a change in element.labels', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels[0].helpText = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('label'));
    expect(labelEle.nativeElement.innerText.toString()).toEqual(
      'First Name---127...'
    );
    hostComponent.element.labels[0].text = 'last name';
    fixture.detectChanges();
    const updatedLabelEle = debugElement.query(By.css('label'));
    expect(updatedLabelEle.nativeElement.innerText).toEqual('last name');
  }));

  it('nm-tooltip should not be created if helpText is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const tooltipEle = debugElement.query(By.css('nm-tooltip'));
    expect(tooltipEle).toBeFalsy();
  }));

  it('label should not be created if the label is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('label'));
    expect(labelEle).toBeFalsy();
  }));
});
