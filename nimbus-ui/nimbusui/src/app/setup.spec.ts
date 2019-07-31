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
  APP_BASE_HREF,
  HashLocationStrategy,
  Location,
  LocationStrategy
} from '@angular/common';
import {
  HttpClient,
  HttpClientModule,
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { ErrorHandler } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserXhr, HttpModule } from '@angular/http';
import { BrowserModule } from '@angular/platform-browser/';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { MessageService } from 'primeng/api';
import { EditorModule } from 'primeng/editor';
import { KeyFilterModule } from 'primeng/keyfilter';
import { MessageModule } from 'primeng/message';
import { MessagesModule } from 'primeng/messages';
import {
  AccordionModule,
  CalendarModule,
  CheckboxModule,
  DataTableModule,
  DialogModule,
  DragDropModule,
  DropdownModule,
  FileUploadModule,
  GrowlModule,
  InputSwitchModule,
  ListboxModule,
  OverlayPanelModule,
  PickListModule,
  ProgressBarModule,
  ProgressSpinnerModule,
  RadioButtonModule,
  SharedModule,
  TreeTableModule
} from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { BreadcrumbService } from './components/platform/breadcrumb/breadcrumb.service';
import { CustomBrowserXhr } from './custom.browserxhr';
import { DateTimeFormatPipe } from './pipes/date.pipe';
import { AppInitService } from './services/app.init.service';
import { AuthenticationService } from './services/authentication.service';
import { ConfigService } from './services/config.service';
import { ControlSubscribers } from './services/control-subscribers.service';
import { FileService } from './services/file.service';
import { GridService } from './services/grid.service';
import { CustomHttpClientInterceptor } from './services/httpclient-interceptor.service';
import { CustomHttpClient } from './services/httpclient.service';
import { LayoutService } from './services/layout.service';
import { LoaderService } from './services/loader.service';
import { LoggerService } from './services/logger.service';
import { PageService } from './services/page.service';
import { RouteService } from './services/route.service';
import { ServiceConstants } from './services/service.constants';
import { CUSTOM_STORAGE, SessionStoreService } from './services/session.store';
import { NmMessageService } from './services/toastmessage.service';
import { NmValidator } from './directives/validateInput.directive';
import { WindowRefService } from './services/window-ref.service';
import { CustomErrorHandler } from './shared/custom.error.handler';
import { GridUtils } from './shared/grid-utils';

'use strict';

export const allproviders = [
  PageService,
  ConfigService,
  HttpClient,
  AppInitService,
  CustomHttpClient,
  { provide: BrowserXhr, useClass: CustomBrowserXhr },
  {
    provide: HTTP_INTERCEPTORS,
    useClass: CustomHttpClientInterceptor,
    multi: true
  },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  GridService,
  Location,
  { provide: APP_BASE_HREF, useValue: ServiceConstants.APP_CONTEXT },
  { provide: 'JSNLOG', useValue: JL },
  { provide: ErrorHandler, useClass: CustomErrorHandler },
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  SessionStoreService,
  ControlSubscribers,
  AuthenticationService,
  BreadcrumbService,
  LoaderService,
  FileService,
  LayoutService,
  WindowRefService,
  LoggerService,
  RouteService,
  MessageService,
  GridUtils,
  DateTimeFormatPipe,
  NmMessageService
];

export const commonComponents = [NmValidator];
export const allimports = [
  BrowserModule,
  HttpClientModule,
  ReactiveFormsModule,
  HttpModule,
  FormsModule,
  DropdownModule,
  DataTableModule,
  TableModule,
  TreeTableModule,
  OverlayPanelModule,
  PickListModule,
  DragDropModule,
  ListboxModule,
  SharedModule,
  FileUploadModule,
  DialogModule,
  BrowserModule,
  BrowserAnimationsModule,
  CheckboxModule,
  CalendarModule,
  RadioButtonModule,
  ProgressBarModule,
  ProgressSpinnerModule,
  AccordionModule,
  GrowlModule,
  MessagesModule,
  MessageModule,
  KeyFilterModule,
  StorageServiceModule,
  AngularSvgIconModule,
  ToastModule,
  InputSwitchModule,
  RouterTestingModule,
  EditorModule
];

export interface TestContext<H> {
  fixture: ComponentFixture<H>;
  hostComponent: H;
  hostElement: any;
}

export function setup(declarations: any[], imports?: any[], providers?: any[]) {
    declarations = declarations.concat(commonComponents)
    TestBed.configureTestingModule({
        declarations: declarations,
        providers: providers ? providers : allproviders,
        imports:  imports ? imports : allimports
        })
  TestBed.configureTestingModule({
    declarations: declarations,
    providers: providers ? providers : allproviders,
    imports: imports ? imports : allimports
  });
}
export function instantiateComponent(testType: any) {
  // fixture = TestBed.createComponent(testType);
  // hostComponent = fixture.componentInstance;
  // this.hostElement = fixture.nativeElement;
}
