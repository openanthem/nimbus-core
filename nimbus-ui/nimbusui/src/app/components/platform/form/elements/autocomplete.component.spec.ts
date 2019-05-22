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
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Subject } from 'rxjs';
import { NmAutocomplete } from './autocomplete.component';
import { SessionStoreService } from './../../../../services/session.store';
import { of as observableOf, Observable } from 'rxjs';
import {
    DataTableModule, TreeTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule,
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule,
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, InputMaskModule, TabViewModule, AutoCompleteModule, TooltipModule
} from 'primeng/primeng';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { tabElement } from './../../../../mockdata/tab.component.mockdata.spec';
import { TreeGrid } from './../../tree-grid/tree-grid.component';
import { Button } from './button.component';
import { ButtonGroup } from './button-group.component';
import { InputText } from './textbox.component';
import { ComboBox } from './combobox.component';
import { InputSwitch } from './input-switch.component';
import { ToastModule } from 'primeng/toast';
import { CardDetailsFieldComponent } from './../../card/card-details-field.component';
import { CardDetailsFieldGroupComponent } from './../../card/card-details-field-group.component';
import { FrmGroupCmp } from './../../form-group.component';
import { FormErrorMessage } from './../../form-error-message.component';
import { Image } from './../../image.component';
import { InputLabel } from './input-label.component';
import { ChartModule } from 'primeng/chart';
import { ActionLink, ActionDropdown } from './action-dropdown.component';
import { HeaderCheckBox } from './header-checkbox.component';
import { TableHeader } from './../../grid/table-header.component';
import { DisplayValueDirective } from './../../../../directives/display-value.directive';
import { KeyFilterModule } from 'primeng/keyfilter';
import { EventPropagationDirective } from './event-propagation.directive';
import { SelectItemPipe } from './../../../../pipes/select-item.pipe';
import { InPlaceEditorComponent } from './inplace-editor.component';
import { TextArea } from './textarea.component';
import { DateTimeFormatPipe } from './../../../../pipes/date.pipe';
import { InputLegend } from './checkbox-group.component.spec';
import { FormElement } from './../../form-element.component';
import { FormGridFiller } from './../form-grid-filler.component';
import { Header } from './../../content/header.component';
import { SvgComponent } from './../../svg/svg.component';
import { InputMaskComp } from './input-mask.component';
import { RichText } from './rich-text.component';
import { Signature } from './signature.component';
import { Calendar } from './calendar.component';
import { RadioButton } from './radio.component';
import { CheckBox } from './checkbox.component';
import { CheckBoxGroup } from './checkbox-group.component';
import { MultiSelectListBox } from './multi-select-listbox.component';
import { MultiselectCard } from './multi-select-card.component';
import { FileUploadComponent } from './../../fileupload/file-upload.component';
import { OrderablePickList } from './picklist.component';
import { Editor } from 'primeng/editor';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { TableModule } from 'primeng/table';
import { NmChart } from './../../charts/chart.component';
import { Accordion, DataTable } from './../../../domain/domain-flow.component.spec';
import { Menu } from './../../menu.component';
import { Link } from './../../link.component';
import { Tab } from './../../content/tab.component';
import { Form } from './../../form.component';
import { StaticText } from './../../content/static-content.component';
import { Paragraph } from './../../content/paragraph.component';
import { PrintDirective } from './../../../../directives/print.directive';
import { CardDetailsComponent } from './../../card/card-details.component';
import { CardDetailsGrid } from './../../card/card-details-grid.component';
import { MessageComponent } from './../../grid/table.component.spec';
import { TooltipComponent } from './../../tooltip/tooltip.component';
import { Section } from './../../section.component';
import { Label } from './../../content/label.component';
import { Param } from './../../../../shared/param-state';
import { setup } from './../../../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import { AppInitService } from './../../../../services/app.init.service';
import { CUSTOM_STORAGE } from './../../../../services/session.store';
import { LoggerService } from './../../../../services/logger.service';
import { ConfigService } from './../../../../services/config.service';
import { AutoCompleteService } from './../../../../services/autocomplete.service';
import { ControlSubscribers } from '../../../../services/control-subscribers.service';

import { LoaderService } from './../../../../services/loader.service';
import { CustomHttpClient } from './../../../../services/httpclient.service';
import { BreadcrumbService } from './../../breadcrumb/breadcrumb.service';
import { PageService } from './../../../../services/page.service';
import { PageResolver } from './../../content/page-resolver.service';
import { WebContentSvc } from '../../../../services/content-management.service';
import { autocompleteElement } from './../../../../mockdata/autocomplete.component.mockdata.spec';
import { CounterMessageService } from '../../../../services/counter-message.service';







let pageService, autoCompleteService;

class MockPageService {
    eventUpdate$: Subject<any>;
    validationUpdate$: Subject<any>;

    constructor() {
        this.eventUpdate$ = new Subject();
        this.validationUpdate$ = new Subject();
    }
    postOnChange(a, b, c) { }
    logError(a) {
        this.eventUpdate$.next(a);
    }
}

class MockAutoCompleteService {

    constructor() {
    }

    search(a, b) {
        let data = [{ code: "0", label: "label0" }, { code: "1", label: "label1" }]
        return Observable.of(data);
    }

}

const declarations = [
    NmAutocomplete,
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
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, TabViewModule, AutoCompleteModule, TreeTableModule,
    FormsModule, ReactiveFormsModule, InputMaskModule, ToastModule, ChartModule, DataTableModule, TableModule,
    KeyFilterModule, InputSwitchModule, FileUploadModule, AngularSvgIconModule, TooltipModule
];
const providers = [
    { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
    { provide: 'JSNLOG', useValue: JL },
    { provide: AutoCompleteService, useClass: MockAutoCompleteService },
    { provide: PageService, useClass: MockPageService },
    CustomHttpClient,
    LoaderService,
    ConfigService,
    LoggerService,
    AppInitService,
    SessionStoreService,
    WebContentSvc,
    ControlSubscribers,
    CounterMessageService

];
let fixture, hostComponent;

describe('Autocomplete', () => {
    configureTestSuite(() => {
        setup(declarations, imports, providers);
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(NmAutocomplete);
        hostComponent = fixture.debugElement.componentInstance;
        hostComponent.element = autocompleteElement as Param;
        pageService = TestBed.get(PageService);
        autoCompleteService = TestBed.get(AutoCompleteService);
    });

    it('should create the Autocomplete Component', async(() => {
        expect(hostComponent).toBeTruthy();
    }));

    it('should not display the component when visible is set to false', async(() => {
        hostComponent.element.visible = false;
        fixture.detectChanges();
        const autoCompleteInput = document.getElementsByTagName('p-autocomplete');
        expect(autoCompleteInput.length).toBe(0);
    }));

    it('should set the list of results received from the server', async(() => {
        fixture.detectChanges();
        autoCompleteService.search("", "");
        expect(hostComponent.results.length).toBe(2);
    }));

});






