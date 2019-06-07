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
import { tabElement } from 'mockdata';
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
import { Subject } from 'rxjs';
import { AppInitService } from '../../../services/app.init.service';
import { ConfigService } from '../../../services/config.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { LoggerService } from '../../../services/logger.service';
import { PageService } from '../../../services/page.service';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from '../../../services/session.store';
import { setup } from '../../../setup.spec';
import { NmAutocomplete } from '../form/elements/autocomplete.component';
import { Section } from '../section.component';
import { DisplayValueDirective } from './../../../directives/display-value.directive';
import { PrintDirective } from './../../../directives/print.directive';
import { DateTimeFormatPipe } from './../../../pipes/date.pipe';
import { SelectItemPipe } from './../../../pipes/select-item.pipe';
import { Param } from './../../../shared/param-state';
import { CardDetailsFieldGroupComponent } from './../card/card-details-field-group.component';
import { CardDetailsFieldComponent } from './../card/card-details-field.component';
import { CardDetailsGrid } from './../card/card-details-grid.component';
import { CardDetailsComponent } from './../card/card-details.component';
import { NmChart } from './../charts/chart.component';
import { FileUploadComponent } from './../fileupload/file-upload.component';
import { FormElement } from './../form-element.component';
import { FormErrorMessage } from './../form-error-message.component';
import { FrmGroupCmp } from './../form-group.component';
import { Form } from './../form.component';
import {
  ActionDropdown,
  ActionLink
} from './../form/elements/action-dropdown.component';
import { ButtonGroup } from './../form/elements/button-group.component';
import { Button } from './../form/elements/button.component';
import { Calendar } from './../form/elements/calendar.component';
import { CheckBoxGroup } from './../form/elements/checkbox-group.component';
import { CheckBox } from './../form/elements/checkbox.component';
import { ComboBox } from './../form/elements/combobox.component';
import { EventPropagationDirective } from './../form/elements/event-propagation.directive';
import { HeaderCheckBox } from './../form/elements/header-checkbox.component';
import { InPlaceEditorComponent } from './../form/elements/inplace-editor.component';
import { InputLabel } from './../form/elements/input-label.component';
import { InputLegend } from './../form/elements/input-legend.component';
import { InputMaskComp } from './../form/elements/input-mask.component';
import { InputSwitch } from './../form/elements/input-switch.component';
import { MultiselectCard } from './../form/elements/multi-select-card.component';
import { MultiSelectListBox } from './../form/elements/multi-select-listbox.component';
import { OrderablePickList } from './../form/elements/picklist.component';
import { RadioButton } from './../form/elements/radio.component';
import { RichText } from './../form/elements/rich-text.component';
import { Signature } from './../form/elements/signature.component';
import { TextArea } from './../form/elements/textarea.component';
import { InputText } from './../form/elements/textbox.component';
import { FormGridFiller } from './../form/form-grid-filler.component';
import { TableHeader } from './../grid/table-header.component';
import { DataTable } from './../grid/table.component';
import { Image } from './../image.component';
import { Link } from './../link.component';
import { Menu } from './../menu.component';
import { MessageComponent } from './../message/message.component';
import { SvgComponent } from './../svg/svg.component';
import { TooltipComponent } from './../tooltip/tooltip.component';
import { TreeGrid } from './../tree-grid/tree-grid.component';
import { Accordion } from './accordion.component';
import { Header } from './header.component';
import { Label } from './label.component';
import { Paragraph } from './paragraph.component';
import { StaticText } from './static-content.component';
import { Tab } from './tab.component';

let pageService;

class MockPageService {
  eventUpdate$: Subject<any>;

  constructor() {
    this.eventUpdate$ = new Subject();
  }
  postOnChange(a, b, c) {}
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
  NmAutocomplete,
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
  TooltipModule,
  FileUploadModule,
  AngularSvgIconModule
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
