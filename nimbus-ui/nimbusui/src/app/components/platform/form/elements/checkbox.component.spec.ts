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

'use strict';
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
import { checkboxElement } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
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
import { setup } from '../../../../setup.spec';
import { Param } from '../../../../shared/param-state';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { CounterMessageService } from './../../../../services/counter-message.service';
import { NmMessageService } from './../../../../services/toastmessage.service';
import { WindowRefService } from './../../../../services/window-ref.service';
import { CheckBox } from './checkbox.component';

let param: Param;

class MockLoggerService {
  debug() {}
  info() {}
  error() {}
}

const declarations = [CheckBox, TooltipComponent];
const imports = [
  FormsModule,
  HttpClientModule,
  HttpModule,
  StorageServiceModule
];
const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  { provide: LoggerService, useClass: MockLoggerService },
  Location,
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  SessionStoreService,
  AppInitService,
  NmMessageService,
  WindowRefService,
  CounterMessageService
];
let fixture, hostComponent;
describe('CheckBox', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckBox);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = checkboxElement as Param;
  });

  it('should create the CheckBox', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('input should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputEle = debugElement.query(By.css('input'));
    expect(inputEle).toBeTruthy();
  }));

  it('change event on input should call emitValueChangedEvent()', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    spyOn(hostComponent, 'emitValueChangedEvent').and.callThrough();
    const inputEle = debugElement.query(By.css('input')).nativeElement;
    inputEle.click();
    expect(hostComponent.emitValueChangedEvent).toHaveBeenCalled();
  }));

  it('nm-tooltip should be created if helpText is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const tooltipEle = debugElement.query(By.css('nm-tooltip'));
    expect(tooltipEle).toBeTruthy();
  }));

  it('nm-tooltip should not be created if helpText is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const tooltipEle = debugElement.query(By.css('nm-tooltip'));
    expect(tooltipEle).toBeFalsy();
  }));
});
