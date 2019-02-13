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
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Subject } from 'rxjs';
import { of as observableOf, Observable } from 'rxjs';
import {
    DataTableModule, TreeTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule,
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule,
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, InputMaskModule, TabViewModule
} from 'primeng/primeng';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { PageResolver } from './page-resolver.service';
import { PageService } from '../../../services/page.service';
import { WebContentSvc } from './../../../services/content-management.service';
import { BreadcrumbService } from './../breadcrumb/breadcrumb.service'
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { AppInitService } from '../../../services/app.init.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import { Param } from './../../../shared/param-state';
import { Label } from './label.component';
import { Section } from '../section.component';
import { TooltipComponent } from './../tooltip/tooltip.component';
import { MessageComponent } from './../message/message.component';
import { CardDetailsGrid } from './../card/card-details-grid.component';
import { CardDetailsComponent } from './../card/card-details.component';
import { PrintDirective } from './../../../directives/print.directive';
import { Paragraph } from './paragraph.component';
import { StaticText } from './static-content.component';
import { Form } from './../form.component';
import { Tab } from './tab.component';
import { Link } from './../link.component';
import { Menu } from './../menu.component';
import { Accordion } from './accordion.component';
import { NmChart } from './../charts/chart.component';
import { TableModule } from 'primeng/table';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { Editor } from 'primeng/editor';
import { OrderablePickList } from './../form/elements/picklist.component';
import { FileUploadComponent } from './../fileupload/file-upload.component';
import { MultiselectCard } from './../form/elements/multi-select-card.component';
import { MultiSelectListBox } from './../form/elements/multi-select-listbox.component';
import { CheckBoxGroup } from './../form/elements/checkbox-group.component';
import { CheckBox } from './../form/elements/checkbox.component';
import { RadioButton } from './../form/elements/radio.component';
import { Calendar } from './../form/elements/calendar.component';
import { Signature } from './../form/elements/signature.component';
import { RichText } from './../form/elements/rich-text.component';
import { InputMaskComp } from './../form/elements/input-mask.component';
import { SvgComponent } from './../svg/svg.component';
import { Header } from './header.component';
import { FormGridFiller } from './../form/form-grid-filler.component';
import { FormElement } from './../form-element.component';
import { InputLegend } from './../form/elements/input-legend.component';


import { DateTimeFormatPipe } from './../../../pipes/date.pipe';
import { TextArea } from './../form/elements/textarea.component';
import { InPlaceEditorComponent } from './../form/elements/inplace-editor.component';
import { SelectItemPipe } from './../../../pipes/select-item.pipe';
import { EventPropagationDirective } from './../form/elements/event-propagation.directive';
import { KeyFilterModule } from 'primeng/keyfilter';
import { DisplayValueDirective } from './../../../directives/display-value.directive';
import { TableHeader } from './../grid/table-header.component';
import { HeaderCheckBox } from './../form/elements/header-checkbox.component';
import { ActionDropdown, ActionLink } from './../form/elements/action-dropdown.component';
import { ChartModule } from 'primeng/chart';
import { InputLabel } from './../form/elements/input-label.component';
import { Image } from './../image.component';
import { FormErrorMessage } from './../form-error-message.component';
import { FrmGroupCmp } from './../form-group.component';
import { CardDetailsFieldGroupComponent } from './../card/card-details-field-group.component';
import { CardDetailsFieldComponent } from './../card/card-details-field.component';
import { ToastModule } from 'primeng/toast';
import { InputSwitch } from './../form/elements/input-switch.component';
import { ComboBox } from './../form/elements/combobox.component';
import { InputText } from './../form/elements/textbox.component';
import { DataTable } from './../grid/table.component';

import { ButtonGroup } from './../form/elements/button-group.component';
import { Button } from './../form/elements/button.component';
import { TreeGrid } from './../tree-grid/tree-grid.component';
import {tabElement} from 'mockdata';
import { componentFactoryName } from '@angular/compiler';


let pageService;

class MockPageService {
    eventUpdate$: Subject<any>;

    constructor() {
        this.eventUpdate$ = new Subject();
    }
    postOnChange(a, b, c) { }
    logError(a) {
        this.eventUpdate$.next(a);
    }
}

const declarations = [
    Tab,
    Label,
    Section,
    TooltipComponent,
    MessageComponent,
    CardDetailsGrid,
    CardDetailsComponent,
    PrintDirective,
    Paragraph,
    StaticText,
    Form,
    Link,
    Menu,
    Accordion,
    NmChart,
    DataTable,
    TreeGrid,
    Button,
    ButtonGroup,
    InputText,
    ComboBox,
    InputSwitch,
    CardDetailsFieldComponent,
    CardDetailsFieldGroupComponent,
    FrmGroupCmp,
    FormErrorMessage,
    Image,
    InputLabel,
    ActionDropdown,
    HeaderCheckBox,
    TableHeader,
    DisplayValueDirective,
    EventPropagationDirective,
    SelectItemPipe,
    InPlaceEditorComponent,
    TextArea,
    DateTimeFormatPipe,
    InputLegend,
    FormElement,
    FormGridFiller,
    Header,
    SvgComponent,
    ActionLink,
    InputMaskComp,
    RichText,
    Signature,
    Calendar,
    RadioButton,
    CheckBox,
    CheckBoxGroup,
    MultiSelectListBox,
    MultiselectCard,
    FileUploadComponent,
    OrderablePickList,
    Editor

];
const imports = [
    HttpModule,
    HttpClientTestingModule,
    StorageServiceModule,
    DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule,
    FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule,
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, TabViewModule, TreeTableModule,
    FormsModule, ReactiveFormsModule, InputMaskModule, ToastModule, ChartModule, DataTableModule, TableModule,
    KeyFilterModule, InputSwitchModule, FileUploadModule, AngularSvgIconModule
];
const providers = [
    { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
    { provide: 'JSNLOG', useValue: JL },
    { provide: PageService, useClass: MockPageService },
    CustomHttpClient,
    LoaderService,
    ConfigService,
    LoggerService,
    AppInitService,
    SessionStoreService
];
let fixture, hostComponent;

describe('Tab', () => {
    configureTestSuite(() => {
        setup(declarations, imports, providers);
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(Tab);
        hostComponent = fixture.debugElement.componentInstance;
        hostComponent.element = tabElement as Param;
        pageService = TestBed.get(PageService);
    });

    it('should create the Tab Component', async(() => {
        expect(hostComponent).toBeTruthy();
    }));

});

