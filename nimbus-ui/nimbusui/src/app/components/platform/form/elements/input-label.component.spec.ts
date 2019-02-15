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

import { NmMessageService } from './../../../../services/toastmessage.service';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { CalendarModule } from 'primeng/primeng';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { WebContentSvc } from '../../../../services/content-management.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { Param } from '../../../../shared/param-state';
import { ServiceConstants } from '../../../../services/service.constants';
import { By } from '@angular/platform-browser';
import { WindowRefService } from '../../../../services/window-ref.service';
import { labelElement } from 'mockdata';

const declarations = [
  InputLabel,
  TooltipComponent,
  InputLabel
 ];
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
  WebContentSvc,
  NmMessageService,
  WindowRefService
 ];

 let fixture, hostComponent;

describe('InputLabel', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
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
    expect(labelEle.nativeElement.innerText.toString()).toEqual('First Name---127...');
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

  it('getCssClass() should return required', async(() => {
    hostComponent.required = true;
    expect(hostComponent.getCssClass()).toEqual('required');
  }));

});

