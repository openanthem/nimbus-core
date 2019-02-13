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
import { RichText } from './components/platform/form/elements/rich-text.component';
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
import { PrintDirective } from './directives/print.directive';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule, BrowserXhr } from '@angular/http';
import { ReactiveFormsModule }  from '@angular/forms';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { APP_BASE_HREF } from '@angular/common';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, TabViewModule,
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, InputMaskModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { EditorModule } from 'primeng/editor';
import { KeyFilterModule } from 'primeng/keyfilter';
import { NavLinkRouter } from './directives/nav-link-router.directive';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { WindowRefService } from './services/window-ref.service';
import { HeaderCheckBox } from './components/platform/form/elements/header-checkbox.component';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { ToastModule } from 'primeng/toast';
import { ChartModule } from 'primeng/chart';

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
import { FormGridFiller } from './components/platform/form/form-grid-filler.component';
import { Image } from './components/platform/image.component';
import { Paragraph } from './components/platform/content/paragraph.component';
import { Value } from './components/platform/form/elements/value.component';
import { ComboBox } from './components/platform/form/elements/combobox.component';
import { RadioButton } from './components/platform/form/elements/radio.component';
import { Signature } from './components/platform/form/elements/signature.component';
import { InputText } from './components/platform/form/elements/textbox.component';
import { InputMaskComp } from './components/platform/form/elements/input-mask.component';
import { Tab } from './components/platform/content/tab.component';
import { CheckBoxGroup } from './components/platform/form/elements/checkbox-group.component';
import { MultiselectCard } from './components/platform/form/elements/multi-select-card.component';
import { ActionDropdown, ActionLink } from './components/platform/form/elements/action-dropdown.component';
import { DataTable } from './components/platform/grid/table.component';
import { TreeGrid } from './components/platform/tree-grid/tree-grid.component';
import { Link } from './components/platform/link.component';
import { Menu } from './components/platform/menu.component';
import { FlowWrapper } from './components/platform/content/flow-wrapper.component';
import { SubHeaderCmp } from './components/platform/sub-header.component';
import { FieldValue } from './components/platform/content/field-value.component';
import { StaticText } from './components/platform/content/static-content.component';
import { CardDetailsComponent } from './components/platform/card/card-details.component';
import { CardDetailsFieldGroupComponent } from './components/platform/card/card-details-field-group.component';
import { CardDetailsFieldComponent } from './components/platform/card/card-details-field.component';
import { CardDetailsGrid } from './components/platform/card/card-details-grid.component';
import { Accordion } from './components/platform/content/accordion.component';
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
import { MessageComponent } from './components/platform/message/message.component';
import { ActionTray } from './components/platform/actiontray.component';
import { BaseLabel } from './components/platform/base-label.component';
import { Label } from './components/platform/content/label.component';
import { InputLabel } from './components/platform/form/elements/input-label.component';
import { BaseTableElement } from './components/platform/base-table-element.component';
import { TableHeader } from './components/platform/grid/table-header.component';
import { ToastMessageComponent } from './components/platform/message/toastmessage.component';

// Services
import { WebContentSvc } from './services/content-management.service';
import { STOMPStatusComponent } from './services/stomp-status.component';
import { AuthenticationService } from './services/authentication.service';
import { FileService } from './services/file.service';
import { ServiceConstants } from "./services/service.constants";
import { AppInitService } from "./services/app.init.service";
import { LoggerService } from './services/logger.service';
import { RouteService } from './services/route.service';
import { MessageService } from 'primeng/api';
import { PrintService } from './services/print.service';

//Utility Services
import { GridUtils } from './shared/grid-utils';

// Routes
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app.routing.module';
import { CustomHttpClient } from './services/httpclient.service';
import { CustomHttpClientInterceptor } from './services/httpclient-interceptor.service';
import { CustomBrowserXhr } from './custom.browserxhr';
import { HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { JL } from 'jsnlog';
import { ErrorHandler } from '@angular/core';
import { CustomErrorHandler } from './shared/custom.error.handler';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { SessionStoreService, CUSTOM_STORAGE } from './services/session.store';
import { SvgComponent } from './components/platform/svg/svg.component';

// Declarations
import { LoginCmp } from './components/login/login.component';
import { LandingPage } from './components/login/auth-landingpage';
import { KeysPipe } from './pipes/app.pipe';
import { LinkPipe } from './pipes/link.pipe';
import { SelectItemPipe } from './pipes/select-item.pipe';
import { LoaderComponent } from './components/platform/loader/loader.component';
import { SubDomainFlowCmp } from './components/domain/subdomain-flow.component';
import { PageResolver } from './components/platform/content/page-resolver.service';
import {DateTimeFormatPipe} from './pipes/date.pipe';
import { DisplayValueDirective } from './directives/display-value.directive';
import { NmPanelMenu, NmPanelMenuSub } from './components/platform/panelmenu.component';
import { MenuRouteLink } from './directives/routes/route-link.component';
import { MenuRouterLinkActive } from './directives/routes/route-active.component';
import { InputSwitch } from './components/platform/form/elements/input-switch.component';
import { InputLegend } from './components/platform/form/elements/input-legend.component';
import { FormErrorMessage } from './components/platform/form-error-message.component';
import { EventPropagationDirective } from './components/platform/form/elements/event-propagation.directive';
import { NmChart } from './components/platform/charts/chart.component';
import { NmMessageService } from './services/toastmessage.service';
/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

export function init_app(appinitservice: AppInitService) {
    return () => {
        return appinitservice.loadConfig();
    }
}

@NgModule({
    imports: [
        BrowserModule,
        HttpClientModule,
        ReactiveFormsModule,
        AppRoutingModule,
        HttpModule,
        FormsModule,
        DropdownModule,
        InputMaskModule,
        TabViewModule,
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
        ChartModule,
        EditorModule
    ],
    declarations: [ AppComponent, STOMPStatusComponent, FlowWrapper, PageContent, PageNotfoundComponent, StaticText,
        Tile, Section, Header, Form, FormElement, InputText, InputMaskComp, Tab, ComboBox, RadioButton, Signature, CheckBoxGroup,
        InPlaceEditorComponent, Paragraph, Value, BaseElement, FormGridFiller, 
        MultiselectCard, Link, Menu, CardDetailsComponent, CardDetailsFieldGroupComponent, CardDetailsFieldComponent, CardDetailsGrid, FieldValue,
        Accordion, AccordionTab, FrmGroupCmp, Button, ButtonGroup, FilterButton, OrderablePickList,
        STOMPStatusComponent, DataTable, SubHeaderCmp, TextArea, LandingPage, RichText,
        LayoutService, ContentContainer,
        DomainFlowCmp, HeaderGlobal, FooterGlobal,
        BreadcrumbComponent, NavLinkRouter,
        Modal, ActionDropdown, ActionLink,
        GridMouseEventDirective, DisplayValueDirective, PrintDirective,
        HomeLayoutCmp, LoginCmp, LoginLayoutCmp, StyleGuideCmp, 
        KeysPipe, LinkPipe, DateTimeFormatPipe, SelectItemPipe, MultiSelectListBox, 
        CheckBox, FileUploadComponent, BreadcrumbComponent, TooltipComponent, Calendar, LoaderComponent, MessageComponent,
        HeaderCheckBox, SvgComponent, ActionTray, SubDomainFlowCmp, Image, NmPanelMenu,NmPanelMenuSub, MenuRouterLinkActive, NmChart, ToastMessageComponent,
        MenuRouteLink, Label, InputLabel,InputSwitch,TreeGrid,InputLegend, FormErrorMessage, BaseTableElement, EventPropagationDirective, TableHeader
    ],
    entryComponents: [ FlowWrapper, PageContent, PageNotfoundComponent, LoginCmp, HomeLayoutCmp, SubDomainFlowCmp],
    providers: [ PageService, ConfigService, WebContentSvc, HttpClient,  HttpClientModule, AppInitService,
         CustomHttpClient, { provide: BrowserXhr, useClass: CustomBrowserXhr },
         { provide: APP_INITIALIZER, useFactory: init_app, deps: [AppInitService], multi: true },
         { provide: HTTP_INTERCEPTORS, useClass: CustomHttpClientInterceptor, multi: true },
         { provide: LocationStrategy, useClass: HashLocationStrategy }, GridService,
         { provide: APP_BASE_HREF, useValue: ServiceConstants.APP_CONTEXT },
         { provide: 'JSNLOG', useValue: JL },
         { provide: ErrorHandler, useClass: CustomErrorHandler },
         { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
         SessionStoreService,
         AuthenticationService, BreadcrumbService, LoaderService, FileService, LayoutService, WindowRefService, LoggerService, NmMessageService,
         RouteService, MessageService, GridUtils, DateTimeFormatPipe, PrintService],
    bootstrap: [ AppComponent ]
})
export class AppModule { }

