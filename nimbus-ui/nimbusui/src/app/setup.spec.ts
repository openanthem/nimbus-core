import { AppRoutingModule } from './app.routing.module';
import { TableModule } from 'primeng/table';
import { MessagesModule } from 'primeng/messages';
import { KeyFilterModule } from 'primeng/keyfilter';
import { AngularSvgIconModule } from 'angular-svg-icon';
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
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
import { Type, ErrorHandler } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BreadcrumbService } from './components/platform/breadcrumb/breadcrumb.service';
import { PageService } from './services/page.service';
import { ConfigService } from './services/config.service';
import { GridService } from './services/grid.service';
import { APP_INITIALIZER } from '@angular/core';
import { BrowserXhr, HttpModule } from '@angular/http';
import { LocationStrategy, HashLocationStrategy, Location } from '@angular/common';
import { APP_BASE_HREF } from '@angular/common';
import { WindowRefService } from './services/window-ref.service';
import { LayoutService } from './services/layout.service';
import { WebContentSvc } from './services/content-management.service';
import { AuthenticationService } from './services/authentication.service';
import { FileService } from './services/file.service';
import { AppInitService } from "./services/app.init.service";
import { LoggerService } from './services/logger.service';
import { RouteService } from './services/route.service';
import { MessageService } from 'primeng/api';
import { LoaderService } from './services/loader.service';
import { JL } from 'jsnlog';
import { SessionStoreService, CUSTOM_STORAGE } from './services/session.store';
import { DateTimeFormatPipe } from './pipes/date.pipe';
import { GridUtils } from './shared/grid-utils';
import { SESSION_STORAGE, StorageServiceModule } from 'angular-webstorage-service';
import { CustomErrorHandler } from './shared/custom.error.handler';
import { ServiceConstants } from './services/service.constants';
import { CustomHttpClientInterceptor } from './services/httpclient-interceptor.service';
import { CustomBrowserXhr } from './custom.browserxhr';
import { HttpClient, HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { CustomHttpClient } from './services/httpclient.service';
import { ControlSubscribers } from './services/control-subscribers.service';
import { InputSwitchModule, GrowlModule, AccordionModule, ProgressSpinnerModule, ProgressBarModule, RadioButtonModule, CalendarModule, CheckboxModule, DialogModule, FileUploadModule, SharedModule, ListboxModule, DragDropModule, PickListModule, OverlayPanelModule, TreeTableModule, DataTableModule, DropdownModule } from 'primeng/primeng';
import { ToastModule } from 'primeng/toast';
import { MessageModule } from 'primeng/message';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser/';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

export interface TestContext<H> {
    create(testType: Type<H>, declarations: any[], imports?: any[], providers?: any[], componentWithTemplateUrl?: boolean): void;
    fixture: ComponentFixture<H>;
    hostComponent: H;
    hostElement: any;
}

export function setup(): void {
    
    let allproviders =  [ PageService, ConfigService, WebContentSvc, HttpClient, AppInitService,
        CustomHttpClient, { provide: BrowserXhr, useClass: CustomBrowserXhr },
       // { provide: APP_INITIALIZER, useFactory: init_app, deps: [AppInitService], multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: CustomHttpClientInterceptor, multi: true },
        { provide: LocationStrategy, useClass: HashLocationStrategy }, GridService, Location,
        { provide: APP_BASE_HREF, useValue: ServiceConstants.APP_CONTEXT },
        { provide: 'JSNLOG', useValue: JL },
        { provide: ErrorHandler, useClass: CustomErrorHandler },
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE},
        SessionStoreService, ControlSubscribers, 
        AuthenticationService, BreadcrumbService, LoaderService, FileService, LayoutService, WindowRefService, LoggerService, 
        RouteService, MessageService, GridUtils, DateTimeFormatPipe]

    let allimports = [
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
        InputSwitchModule
    ]
    beforeEach(function<H>(this: TestContext<H>) {
        this.create = (testType: Type<H>, declarations:any[], imports?: any[], providers?: any[], componentWithTemplateUrl?: boolean) => {
            if(componentWithTemplateUrl) {
                TestBed.configureTestingModule({
                    declarations: declarations,
                    providers: providers? providers: allproviders,
                    imports: imports? imports : allimports
    
                }).compileComponents();
            } else {
                TestBed.configureTestingModule({
                    declarations: declarations,
                    providers: providers? providers: allproviders,
                    imports: imports? imports : allimports
    
                });
            }
            this.fixture = TestBed.createComponent(testType);
            this.hostComponent = this.fixture.componentInstance;
            this.hostElement = this.fixture.nativeElement;

        };
        
    });

    afterEach(function<H>(this: TestContext<H>) {
        if (this.fixture) {
            this.fixture.destroy();
        }
    });
}