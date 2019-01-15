'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { KeyFilterModule } from 'primeng/keyfilter';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { TextArea } from './textarea.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { configureTestSuite, TestCtx } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { Param } from '../../../../shared/param-state';

let fixture, hostComponent;

const declarations = [
  TextArea,
  TooltipComponent,
  InputLabel
 ];
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
  Location,
  SessionStoreService,
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  KeyFilterModule,
  LoggerService,
  AppInitService
 ];

describe('TextArea', () => {

  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TextArea);
    hostComponent = fixture.debugElement.componentInstance;
  });

  it('should create the TextArea', async(() =>  {
    expect(hostComponent).toBeTruthy();
  }));

});