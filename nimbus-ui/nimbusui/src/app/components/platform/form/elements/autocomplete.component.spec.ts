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
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { configureTestSuite } from 'ng-bullet';
import { ChartModule } from 'primeng/chart';
import { Editor } from 'primeng/editor';
import { KeyFilterModule } from 'primeng/keyfilter';
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
import { Observable, Subject } from 'rxjs';
import { ControlSubscribers } from '../../../../services/control-subscribers.service';
import { CounterMessageService } from '../../../../services/counter-message.service';
import { DisplayValueDirective } from './../../../../directives/display-value.directive';
import { PrintDirective } from './../../../../directives/print.directive';
import { autocompleteElement } from './../../../../mockdata/autocomplete.component.mockdata.spec';
import { DateTimeFormatPipe } from './../../../../pipes/date.pipe';
import { SelectItemPipe } from './../../../../pipes/select-item.pipe';
import { AppInitService } from './../../../../services/app.init.service';
import { AutoCompleteService } from './../../../../services/autocomplete.service';
import { ConfigService } from './../../../../services/config.service';
import { CustomHttpClient } from './../../../../services/httpclient.service';
import { LoaderService } from './../../../../services/loader.service';
import { LoggerService } from './../../../../services/logger.service';
import { PageService } from './../../../../services/page.service';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from './../../../../services/session.store';
import { setup } from './../../../../setup.spec';
import { Param } from './../../../../shared/param-state';
import {
  Accordion,
  DataTable
} from './../../../domain/domain-flow.component.spec';
import { CardDetailsFieldGroupComponent } from './../../card/card-details-field-group.component';
import { CardDetailsFieldComponent } from './../../card/card-details-field.component';
import { CardDetailsGrid } from './../../card/card-details-grid.component';
import { CardDetailsComponent } from './../../card/card-details.component';
import { NmChart } from './../../charts/chart.component';
import { Header } from './../../content/header.component';
import { Label } from './../../content/label.component';
import { Paragraph } from './../../content/paragraph.component';
import { StaticText } from './../../content/static-content.component';
import { Tab } from './../../content/tab.component';
import { FileUploadComponent } from './../../fileupload/file-upload.component';
import { FormElement } from './../../form-element.component';
import { FormErrorMessage } from './../../form-error-message.component';
import { FrmGroupCmp } from './../../form-group.component';
import { Form } from './../../form.component';
import { TableHeader } from './../../grid/table-header.component';
import { MessageComponent } from './../../grid/table.component.spec';
import { Image } from './../../image.component';
import { Link } from './../../link.component';
import { Menu } from './../../menu.component';
import { Section } from './../../section.component';
import { SvgComponent } from './../../svg/svg.component';
import { TooltipComponent } from './../../tooltip/tooltip.component';
import { TreeGrid } from './../../tree-grid/tree-grid.component';
import { FormGridFiller } from './../form-grid-filler.component';
import { ActionDropdown, ActionLink } from './action-dropdown.component';
import { NmAutocomplete } from './autocomplete.component';
import { ButtonGroup } from './button-group.component';
import { Button } from './button.component';
import { Calendar } from './calendar.component';
import { CheckBoxGroup } from './checkbox-group.component';
import { InputLegend } from './checkbox-group.component.spec';
import { CheckBox } from './checkbox.component';
import { ComboBox } from './combobox.component';
import { EventPropagationDirective } from './event-propagation.directive';
import { HeaderCheckBox } from './header-checkbox.component';
import { InPlaceEditorComponent } from './inplace-editor.component';
import { InputLabel } from './input-label.component';
import { InputMaskComp } from './input-mask.component';
import { InputSwitch } from './input-switch.component';
import { MultiselectCard } from './multi-select-card.component';
import { MultiSelectListBox } from './multi-select-listbox.component';
import { OrderablePickList } from './picklist.component';
import { RadioButton } from './radio.component';
import { RichText } from './rich-text.component';
import { Signature } from './signature.component';
import { TextArea } from './textarea.component';
import { InputText } from './textbox.component';

let pageService, autoCompleteService;

class MockPageService {
  eventUpdate$: Subject<any>;
  validationUpdate$: Subject<any>;

  constructor() {
    this.eventUpdate$ = new Subject();
    this.validationUpdate$ = new Subject();
  }
  postOnChange(a, b, c) {}
  logError(a) {
    this.eventUpdate$.next(a);
  }
}

class MockAutoCompleteService {
  constructor() {}

  search(a, b) {
    let data = [{ code: '0', label: 'label0' }, { code: '1', label: 'label1' }];
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
  DataTableModule,
  SharedModule,
  OverlayPanelModule,
  PickListModule,
  DragDropModule,
  CalendarModule,
  FileUploadModule,
  ListboxModule,
  DialogModule,
  CheckboxModule,
  DropdownModule,
  RadioButtonModule,
  ProgressBarModule,
  ProgressSpinnerModule,
  AccordionModule,
  GrowlModule,
  TabViewModule,
  AutoCompleteModule,
  TreeTableModule,
  FormsModule,
  ReactiveFormsModule,
  InputMaskModule,
  ToastModule,
  ChartModule,
  DataTableModule,
  TableModule,
  KeyFilterModule,
  InputSwitchModule,
  FileUploadModule,
  AngularSvgIconModule,
  TooltipModule
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
    autoCompleteService.search('', '');
    expect(hostComponent.results.length).toBe(2);
  }));
});
