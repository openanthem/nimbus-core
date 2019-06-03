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
  APP_BASE_HREF,
  HashLocationStrategy,
  LocationStrategy
} from '@angular/common';
import {
  HttpClient,
  HttpClientModule,
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { APP_INITIALIZER, ErrorHandler, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserXhr, HttpModule } from '@angular/http';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { MessageService } from 'primeng/api';
import { ChartModule } from 'primeng/chart';
import { EditorModule } from 'primeng/editor';
import { KeyFilterModule } from 'primeng/keyfilter';
import { MessageModule } from 'primeng/message';
import { MessagesModule } from 'primeng/messages';
import {
  AccordionModule,
  AutoCompleteModule,
  CalendarModule,
  CheckboxModule,
  DataTableModule,
  DialogModule,
  DragDropModule,
  DropdownModule,
  FileUploadModule,
  GrowlModule,
  InputMaskModule,
  InputSwitchModule,
  ListboxModule,
  OverlayPanelModule,
  PickListModule,
  ProgressBarModule,
  ProgressSpinnerModule,
  RadioButtonModule,
  SharedModule,
  TabViewModule,
  TooltipModule,
  TreeTableModule
} from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routing.module';
import { DomainFlowCmp } from './components/domain/domain-flow.component';
import { SubDomainFlowCmp } from './components/domain/subdomain-flow.component';
import { HomeLayoutCmp } from './components/home/home-layout.component';
import { LandingPage } from './components/login/auth-landingpage';
import { LoginLayoutCmp } from './components/login/login-layout.component';
import { LoginCmp } from './components/login/login.component';
import { NavigationComponent } from './components/navigation/navigation.component';
import { ActionTray } from './components/platform/actiontray.component';
import { BaseElement } from './components/platform/base-element.component';
import { BaseTableElement } from './components/platform/base-table-element.component';
import { BreadcrumbComponent } from './components/platform/breadcrumb/breadcrumb.component';
import { BreadcrumbService } from './components/platform/breadcrumb/breadcrumb.service';
import { CardDetailsFieldGroupComponent } from './components/platform/card/card-details-field-group.component';
import { CardDetailsFieldComponent } from './components/platform/card/card-details-field.component';
import { CardDetailsGrid } from './components/platform/card/card-details-grid.component';
import { CardDetailsComponent } from './components/platform/card/card-details.component';
import { NmChart } from './components/platform/charts/chart.component';
import { AccordionTab } from './components/platform/content/accordion-tab.component';
import { Accordion } from './components/platform/content/accordion.component';
import { ContentContainer } from './components/platform/content/content-container.component';
import { FieldValue } from './components/platform/content/field-value.component';
import { FlowWrapper } from './components/platform/content/flow-wrapper.component';
import { Header } from './components/platform/content/header.component';
import { Label } from './components/platform/content/label.component';
import { PageContent } from './components/platform/content/page-content.component';
import { PageNotfoundComponent } from './components/platform/content/page-notfound.component';
import { Paragraph } from './components/platform/content/paragraph.component';
import { StaticText } from './components/platform/content/static-content.component';
import { Tab } from './components/platform/content/tab.component';
import { FileUploadComponent } from './components/platform/fileupload/file-upload.component';
import { FooterGlobal } from './components/platform/footer/footer-global.component';
import { FormElement } from './components/platform/form-element.component';
import { FormErrorMessage } from './components/platform/form-error-message.component';
import { FrmGroupCmp } from './components/platform/form-group.component';
import { Form } from './components/platform/form.component';
import {
  ActionDropdown,
  ActionLink
} from './components/platform/form/elements/action-dropdown.component';
import { NmAutocomplete } from './components/platform/form/elements/autocomplete.component';
import { ButtonGroup } from './components/platform/form/elements/button-group.component';
import { Button } from './components/platform/form/elements/button.component';
import { Calendar } from './components/platform/form/elements/calendar.component';
import { CheckBoxGroup } from './components/platform/form/elements/checkbox-group.component';
import { CheckBox } from './components/platform/form/elements/checkbox.component';
import { ComboBox } from './components/platform/form/elements/combobox.component';
import { EventPropagationDirective } from './components/platform/form/elements/event-propagation.directive';
import { FilterButton } from './components/platform/form/elements/filter-button.component';
import { HeaderCheckBox } from './components/platform/form/elements/header-checkbox.component';
import { InPlaceEditorComponent } from './components/platform/form/elements/inplace-editor.component';
import { InputLabel } from './components/platform/form/elements/input-label.component';
import { InputLegend } from './components/platform/form/elements/input-legend.component';
import { InputMaskComp } from './components/platform/form/elements/input-mask.component';
import { InputSwitch } from './components/platform/form/elements/input-switch.component';
import { MultiselectCard } from './components/platform/form/elements/multi-select-card.component';
import { MultiSelectListBox } from './components/platform/form/elements/multi-select-listbox.component';
import { OrderablePickList } from './components/platform/form/elements/picklist.component';
import { RadioButton } from './components/platform/form/elements/radio.component';
import { RichText } from './components/platform/form/elements/rich-text.component';
import { Signature } from './components/platform/form/elements/signature.component';
import { TextArea } from './components/platform/form/elements/textarea.component';
import { InputText } from './components/platform/form/elements/textbox.component';
import { Value } from './components/platform/form/elements/value.component';
import { FormGridFiller } from './components/platform/form/form-grid-filler.component';
import { TableHeader } from './components/platform/grid/table-header.component';
import { DataTable } from './components/platform/grid/table.component';
import { HeaderGlobal } from './components/platform/header/header-global.component';
import { Image } from './components/platform/image.component';
import { Link } from './components/platform/link.component';
import { LoaderComponent } from './components/platform/loader/loader.component';
import { Menu } from './components/platform/menu.component';
import { MessageComponent } from './components/platform/message/message.component';
import { ToastMessageComponent } from './components/platform/message/toastmessage.component';
import { Modal } from './components/platform/modal/modal.component';
import {
  NmPanelMenu,
  NmPanelMenuSub
} from './components/platform/panelmenu.component';
import { Section } from './components/platform/section.component';
import { SubHeaderCmp } from './components/platform/sub-header.component';
import { SvgComponent } from './components/platform/svg/svg.component';
import { Tile } from './components/platform/tile.component';
import { TooltipComponent } from './components/platform/tooltip/tooltip.component';
import { TreeGrid } from './components/platform/tree-grid/tree-grid.component';
import { CustomBrowserXhr } from './custom.browserxhr';
import { DisplayValueDirective } from './directives/display-value.directive';
import { GridMouseEventDirective } from './directives/gridhover.directive';
import { NavLinkRouter } from './directives/nav-link-router.directive';
import { PrintDirective } from './directives/print.directive';
import { MenuRouterLinkActive } from './directives/routes/route-active.component';
import { MenuRouteLink } from './directives/routes/route-link.component';
import { NmValidator } from './directives/validateInput.directive';
import { KeysPipe } from './pipes/app.pipe';
import { DateTimeFormatPipe } from './pipes/date.pipe';
import { LinkPipe } from './pipes/link.pipe';
import { SelectItemPipe } from './pipes/select-item.pipe';
import { AppInitService } from './services/app.init.service';
import { AuthenticationService } from './services/authentication.service';
import { AutoCompleteService } from './services/autocomplete.service';
import { ConfigService } from './services/config.service';
import { CounterMessageService } from './services/counter-message.service';
import { FileService } from './services/file.service';
import { GridService } from './services/grid.service';
import { CustomHttpClientInterceptor } from './services/httpclient-interceptor.service';
import { CustomHttpClient } from './services/httpclient.service';
import { LayoutService } from './services/layout.service';
import { LoaderService } from './services/loader.service';
import { LoggerService } from './services/logger.service';
import { PageService } from './services/page.service';
import { PrintService } from './services/print.service';
import { RouteService } from './services/route.service';
import { ServiceConstants } from './services/service.constants';
import { CUSTOM_STORAGE, SessionStoreService } from './services/session.store';
import { STOMPStatusComponent } from './services/stomp-status.component';
import { NmMessageService } from './services/toastmessage.service';
import { WindowRefService } from './services/window-ref.service';
import { CustomErrorHandler } from './shared/custom.error.handler';
import { GridUtils } from './shared/grid-utils';
import { StyleGuideCmp } from './styleguide/style-guide.component';

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
  };
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
    AutoCompleteModule,
    TabViewModule,
    DataTableModule,
    TableModule,
    TreeTableModule,
    TooltipModule,
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
  declarations: [
    AppComponent,
    STOMPStatusComponent,
    FlowWrapper,
    PageContent,
    PageNotfoundComponent,
    StaticText,
    Tile,
    Section,
    Header,
    Form,
    FormElement,
    InputText,
    InputMaskComp,
    NmAutocomplete,
    Tab,
    ComboBox,
    RadioButton,
    Signature,
    CheckBoxGroup,
    InPlaceEditorComponent,
    Paragraph,
    Value,
    BaseElement,
    FormGridFiller,
    MultiselectCard,
    Link,
    Menu,
    CardDetailsComponent,
    CardDetailsFieldGroupComponent,
    CardDetailsFieldComponent,
    CardDetailsGrid,
    FieldValue,
    Accordion,
    AccordionTab,
    FrmGroupCmp,
    Button,
    ButtonGroup,
    FilterButton,
    OrderablePickList,
    STOMPStatusComponent,
    DataTable,
    SubHeaderCmp,
    TextArea,
    LandingPage,
    RichText,
    LayoutService,
    ContentContainer,
    DomainFlowCmp,
    HeaderGlobal,
    FooterGlobal,
    BreadcrumbComponent,
    NavLinkRouter,
    Modal,
    ActionDropdown,
    ActionLink,
    GridMouseEventDirective,
    NmValidator,
    DisplayValueDirective,
    PrintDirective,
    HomeLayoutCmp,
    LoginCmp,
    LoginLayoutCmp,
    StyleGuideCmp,
    KeysPipe,
    LinkPipe,
    DateTimeFormatPipe,
    SelectItemPipe,
    MultiSelectListBox,
    CheckBox,
    FileUploadComponent,
    BreadcrumbComponent,
    TooltipComponent,
    Calendar,
    LoaderComponent,
    MessageComponent,
    HeaderCheckBox,
    SvgComponent,
    ActionTray,
    SubDomainFlowCmp,
    Image,
    NmPanelMenu,
    NmPanelMenuSub,
    MenuRouterLinkActive,
    NmChart,
    ToastMessageComponent,
    MenuRouteLink,
    Label,
    InputLabel,
    InputSwitch,
    TreeGrid,
    InputLegend,
    FormErrorMessage,
    BaseTableElement,
    EventPropagationDirective,
    TableHeader,
    NavigationComponent
  ],
  entryComponents: [
    FlowWrapper,
    PageContent,
    PageNotfoundComponent,
    LoginCmp,
    HomeLayoutCmp,
    SubDomainFlowCmp
  ],
  providers: [
    PageService,
    ConfigService,
    HttpClient,
    HttpClientModule,
    AppInitService,
    CounterMessageService,
    CustomHttpClient,
    { provide: BrowserXhr, useClass: CustomBrowserXhr },
    {
      provide: APP_INITIALIZER,
      useFactory: init_app,
      deps: [AppInitService],
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CustomHttpClientInterceptor,
      multi: true
    },
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    GridService,
    { provide: APP_BASE_HREF, useValue: ServiceConstants.APP_CONTEXT },
    { provide: 'JSNLOG', useValue: JL },
    { provide: ErrorHandler, useClass: CustomErrorHandler },
    { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
    SessionStoreService,
    AutoCompleteService,
    AuthenticationService,
    BreadcrumbService,
    LoaderService,
    FileService,
    LayoutService,
    WindowRefService,
    LoggerService,
    NmMessageService,
    RouteService,
    MessageService,
    GridUtils,
    DateTimeFormatPipe,
    PrintService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
