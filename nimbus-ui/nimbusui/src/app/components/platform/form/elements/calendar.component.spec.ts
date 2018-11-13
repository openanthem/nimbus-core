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
import * as data from '../../../../payload.json';

let fixture, app, param;

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
  LoaderService,
  ConfigService,
  LoggerService,
  SessionStoreService,
  AppInitService
 ];

describe('Calendar', () => {

  configureTestSuite();
  setup(Calendar, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<Calendar>) {
    this.hostComponent.element = param;
  });

  it('should create the Calendar', async function (this: TestContext<Calendar>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('ngOnInit() should call applyDateConstraint()', async function (this: TestContext<Calendar>) {
    const spy = spyOn((this.hostComponent as any), 'applyDateConstraint').and.returnValue('');
    this.hostComponent.ngOnInit();
    expect(spy).toHaveBeenCalled();
  });

  it('ngOnInit() should update minDate and maxDate', async function (this: TestContext<Calendar>) {
    (this.hostComponent as any).getConstraint = () => {
      return true;
    };
    (this.hostComponent as any).applyDateConstraint();
    const presentTime = new Date();
    expect(this.hostComponent.minDate).toEqual(presentTime);
    expect(this.hostComponent.maxDate).toEqual(presentTime);
  });

});