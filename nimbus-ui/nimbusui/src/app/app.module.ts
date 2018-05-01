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
import { LoaderService } from './services/loader.service';
import { InPlaceEditorComponent } from './components/platform/form/elements/inplace-editor.component';
import { TextArea } from './components/platform/form/elements/textarea.component';
import { MultiSelectListBox } from './components/platform/form/elements/multi-select-listbox.component';
import { BreadcrumbComponent } from './components/platform/breadcrumb/breadcrumb.component';
import { BreadcrumbService } from './components/platform/breadcrumb/breadcrumb.service';
import { HomeLayoutCmp } from './components/home/home-layout.component';
import { StyleGuideCmp } from './styleguide/style-guide.component';
import { LoginLayoutCmp } from './components/login/login-layout.component';
import { OrderablePickList } from './components/platform/form/elements/picklist.component';
import { PageService } from './services/page.service';
import { ConfigService } from './services/config.service';
import { PageNotfoundComponent } from './components/platform/content/page-notfound.component';
import { PageContent } from './components/platform/content/page-content.component';
import { GridService } from './services/grid.service';
import { GridMouseEventDirective } from './directives/gridhover.directive';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule, BrowserXhr } from '@angular/http';
import { ReactiveFormsModule }  from '@angular/forms';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { APP_BASE_HREF } from '@angular/common';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule  } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { NavLinkRouter } from './directives/nav-link-router.directive';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { WindowRefService } from './services/window-ref.service';

// Platform Imports
// Components
import { LayoutService } from './services/layout.service';
import { ContentContainer } from './components/platform/content/content-container.component';
import { BaseElement } from './components/platform/base-element.component';
import { AppComponent }  from './app.component';
import { Tile }  from './components/platform/tile.component';
import { Section } from './components/platform/section.component';
import { Header } from './components/platform/content/header.component';
import { Form }  from './components/platform/form.component';
import { FormElement }  from './components/platform/form-element.component';
import { Image } from './components/platform/image.component';
import { Paragraph } from './components/platform/content/paragraph.component';
import { Value } from './components/platform/form/elements/value.component';
import { ComboBox } from './components/platform/form/elements/combobox.component';
import { RadioButton } from './components/platform/form/elements/radio.component';
import { Signature } from './components/platform/form/elements/signature.component';
import { InputText } from './components/platform/form/elements/textbox.component';
import { DateControl } from './components/platform/form/elements/date.component';
import { CheckBoxGroup } from './components/platform/form/elements/checkbox-group.component';
import { MultiselectCard } from './components/platform/form/elements/multi-select-card.component';
import { ActionDropdown, ActionLink } from './components/platform/form/elements/action-dropdown.component';
import { InfiniteScrollGrid } from './components/platform/grid/grid.component';
import { DataTable } from './components/platform/grid/table.component';
import { Link } from './components/platform/link.component';
import { Menu } from './components/platform/menu.component';
import { FlowWrapper } from './components/platform/content/flow-wrapper.component';
import { SubHeaderCmp } from './components/platform/sub-header.component';
import { FieldValue } from './components/platform/content/field-value.component';
import { StaticText } from './components/platform/content/static-content.component';
import { CardDetailsComponent } from './components/platform/card/card-details.component';
import { CardDetailsFieldComponent } from './components/platform/card/card-details-field.component';
import { CardDetailsGrid } from './components/platform/card/card-details-grid.component';
import { Accordion } from './components/platform/accordion.component';
import { AccordionGroup } from './components/platform/accordion-group.component';
import { AccordionMain } from './components/platform/content/accordion.component';
import { AccordionTab } from './components/platform/content/accordion-tab.component';
import { FrmGroupCmp } from './components/platform/form-group.component';
import { Button } from './components/platform/form/elements/button.component';
import { ButtonGroup } from './components/platform/form/elements/button-group.component';
import { FilterButton } from './components/platform/form/elements/filter-button.component';
import { CheckBox } from './components/platform/form/elements/checkbox.component';
import { DomainFlowCmp } from './components/domain/domain-flow.component';
import { FileUploadComponent } from './components/platform/fileupload/file-upload.component';
import { Modal } from './components/platform/modal/modal.component';
import { TooltipComponent } from './components/platform/tooltip/tooltip.component';
import { HeaderGlobal } from './components/platform/header/header-global.component';
import { FooterGlobal } from './components/platform/footer/footer-global.component';
import { Calendar } from './components/platform/form/elements/calendar.component';
import { NavMenuGlobal } from './components/platform/globalNavMenu/nav-global-menu.component';
import { MessageComponent } from './components/platform/message/message.component';

// Services
import { WebContentSvc } from './services/content-management.service';
import { STOMPStatusComponent } from './services/stomp-status.component';
import { AuthenticationService } from './services/authentication.service';
import { FileService } from './services/file.service';
import { ServiceConstants } from "./services/service.constants";
import { LoggerService } from './services/logger.service';
// Routes
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app.routing.module';
import { CustomHttpClient } from './services/httpclient.service';
import { CustomHttpClientInterceptor } from './services/httpclient-interceptor.service';
import { CustomBrowserXhr } from './custom.browserxhr';
import { HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';

// Declarations
import { LoginCmp } from './components/login/login.component';
import { LandingPage } from './components/login/auth-landingpage';
import { KeysPipe } from './pipes/app.pipe';
import { LinkPipe } from './pipes/link.pipe';
import { SelectItemPipe } from './pipes/select-item.pipe';
import { LoaderComponent } from './components/platform/loader/loader.component';
import {DateTimeFormatPipe} from './pipes/date.pipe';
/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@NgModule({
    imports: [
        BrowserModule,
        HttpClientModule,
        ReactiveFormsModule,
        AppRoutingModule,
        HttpModule,
        FormsModule,
        DropdownModule,
        DataTableModule,
        TableModule,
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
        KeyFilterModule
    ],
    declarations: [ AppComponent, STOMPStatusComponent, FlowWrapper, PageContent, PageNotfoundComponent, StaticText,
        Tile, Section, Header, Form, FormElement, InputText, ComboBox, RadioButton, Signature, DateControl, CheckBoxGroup,
        InPlaceEditorComponent, Paragraph, Value, Image, BaseElement,
        MultiselectCard, Link, Menu, CardDetailsComponent, CardDetailsFieldComponent, CardDetailsGrid, FieldValue,
        AccordionGroup, Accordion, AccordionMain, AccordionTab, FrmGroupCmp, Button, ButtonGroup, FilterButton, OrderablePickList,
        STOMPStatusComponent, InfiniteScrollGrid, DataTable, SubHeaderCmp, TextArea, LandingPage,
        LayoutService, ContentContainer,
        DomainFlowCmp, HeaderGlobal, FooterGlobal,
        BreadcrumbComponent, NavLinkRouter,
        Modal, ActionDropdown, ActionLink,
        GridMouseEventDirective,
        HomeLayoutCmp, LoginCmp, LoginLayoutCmp, StyleGuideCmp, 
        KeysPipe, LinkPipe, DateTimeFormatPipe, SelectItemPipe, MultiSelectListBox, 
        CheckBox, FileUploadComponent, BreadcrumbComponent, TooltipComponent, Calendar, NavMenuGlobal, LoaderComponent, MessageComponent
    ],
    entryComponents: [ FlowWrapper, PageContent, PageNotfoundComponent, LoginCmp, HomeLayoutCmp],
    providers: [ PageService, ConfigService, WebContentSvc, HttpClient,  HttpClientModule,
         CustomHttpClient, { provide: BrowserXhr, useClass: CustomBrowserXhr },
         { provide: HTTP_INTERCEPTORS, useClass: CustomHttpClientInterceptor, multi: true },
         { provide: LocationStrategy, useClass: HashLocationStrategy }, GridService,
         { provide: APP_BASE_HREF, useValue: ServiceConstants.APP_CONTEXT },
         AuthenticationService, BreadcrumbService, LoaderService, FileService, LayoutService, WindowRefService, LoggerService ],
    bootstrap: [ AppComponent ]
})
export class AppModule { }

