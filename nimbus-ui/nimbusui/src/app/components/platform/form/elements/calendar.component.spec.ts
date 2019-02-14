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

'use strict';
import { TestBed, async } from '@angular/core/testing';
import { CalendarModule } from 'primeng/primeng';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { Calendar } from './calendar.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { calendarElement } from 'mockdata';
import { By } from '@angular/platform-browser';
import { ServiceConstants } from '../../../../services/service.constants';
import { NmMessageService } from './../../../../services/toastmessage.service';
import { Param } from './../../../../shared/param-state';

const declarations = [
  Calendar,
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
  NmMessageService,
  LoaderService,
  ConfigService,
  LoggerService,
  SessionStoreService,
  AppInitService
 ];
 let fixture, hostComponent;
describe('Calendar', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(Calendar);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = calendarElement as Param;
  });

  it('should create the Calendar', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('nm-input-label should be created if the label is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle).toBeTruthy();
  }));

  it('nm-input-label should not be created if the label is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle).toBeFalsy();
  }));

  it('p-calendar should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pCalendarEle = debugElement.query(By.css('p-calendar'));
    expect(pCalendarEle).toBeTruthy();
  }));

  it('ngOnInit() should call applyDateConstraint()', async(() => {
    const spy = spyOn((hostComponent as any), 'applyDateConstraint').and.returnValue('');
    hostComponent.ngOnInit();
    expect(spy).toHaveBeenCalled();
  }));

  it('ngOnInit() should update minDate and maxDate', async(() => {
    (hostComponent as any).getConstraint = () => {
      return true;
    };
    (hostComponent as any).applyDateConstraint();
    const presentTime = new Date();
    expect(hostComponent.minDate).toEqual(presentTime);
    expect(hostComponent.maxDate).toEqual(presentTime);
  }));

});
