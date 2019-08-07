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

import { HttpClientModule } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { async, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { fieldValueParam } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { ChartModule } from 'primeng/chart';
import { KeyFilterModule } from 'primeng/keyfilter';
import {
  AccordionModule,
  AutoCompleteModule,
  CalendarModule,
  CheckboxModule,
  DataTableModule,
  DropdownModule,
  EditorModule,
  FileUploadModule,
  GrowlModule,
  InputMaskModule,
  InputSwitchModule,
  ListboxModule,
  PickListModule,
  RadioButtonModule,
  TabViewModule,
  TooltipModule,
  TreeTableModule
} from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { DisplayValueDirective } from '../../directives/display-value.directive';
import { PrintDirective } from '../../directives/print.directive';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { AppInitService } from '../../services/app.init.service';
import { ConfigService } from '../../services/config.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { LoggerService } from '../../services/logger.service';
import { PageService } from '../../services/page.service';
import { CUSTOM_STORAGE } from '../../services/session.store';
import { Param } from '../../shared/param-state';
import { CardDetailsFieldGroupComponent } from '../platform/card/card-details-field-group.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { Accordion } from '../platform/content/accordion.component';
import { Header } from '../platform/content/header.component';
import { Label } from '../platform/content/label.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { StaticText } from '../platform/content/static-content.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
import { Form } from '../platform/form.component';
import {
  ActionDropdown,
  ActionLink
} from '../platform/form/elements/action-dropdown.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { Calendar } from '../platform/form/elements/calendar.component';
import { CheckBoxGroup } from '../platform/form/elements/checkbox-group.component';
import { CheckBox } from '../platform/form/elements/checkbox.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { HeaderCheckBox } from '../platform/form/elements/header-checkbox.component';
import { InPlaceEditorComponent } from '../platform/form/elements/inplace-editor.component';
import { InputLabel } from '../platform/form/elements/input-label.component';
import { InputLegend } from '../platform/form/elements/input-legend.component';
import { InputSwitch } from '../platform/form/elements/input-switch.component';
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { Signature } from '../platform/form/elements/signature.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { InputText } from '../platform/form/elements/textbox.component';
import { FormGridFiller } from '../platform/form/form-grid-filler.component';
import { Link } from '../platform/link.component';
import { Menu } from '../platform/menu.component';
import { MessageComponent } from '../platform/message/message.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { TreeGrid } from '../platform/tree-grid/tree-grid.component';
import { setup } from './../../setup.spec';
import { NmChart } from './charts/chart.component';
import { Tab } from './content/tab.component';
import { FormElement } from './form-element.component';
import { FormErrorMessage } from './form-error-message.component';
import { FrmGroupCmp } from './form-group.component';
import { NmAutocomplete } from './form/elements/autocomplete.component';
import { InputMaskComp } from './form/elements/input-mask.component';
import { RichText } from './form/elements/rich-text.component';
import { TableHeader } from './grid/table-header.component';
import { DataTable } from './grid/table.component';
import { Image } from './image.component';
import { Section } from './section.component';
import { SvgComponent } from './svg/svg.component';

@Component({
  template: '<div></div>',
  selector: 'nm-button'
})
class Button {
  @Input() element: any;
  @Input() payload: string;
  @Input() form: any;
  @Input() actionTray?: boolean;

  @Output() buttonClickEvent = new EventEmitter();

  @Output() elementChange = new EventEmitter();
  private imagesPath: string;
  private btnClass: string;
  private disabled: boolean;
  files: any;
  differ: any;
  componentTypes;
}

class MockPageService {
  processEvent() {}
}

class MockLoggerService {
  debug() {}
  info() {}
  error() {}
}

const declarations = [
  Section,
  ComboBox,
  InputText,
  ButtonGroup,
  Button,
  Menu,
  Link,
  Form,
  StaticText,
  Paragraph,
  CardDetailsComponent,
  CardDetailsGrid,
  MessageComponent,
  TooltipComponent,
  SelectItemPipe,
  ActionDropdown,
  DateTimeFormatPipe,
  FrmGroupCmp,
  Accordion,
  CardDetailsFieldComponent,
  ActionLink,
  FormElement,
  InPlaceEditorComponent,
  TextArea,
  FileUploadComponent,
  OrderablePickList,
  MultiselectCard,
  MultiSelectListBox,
  CheckBox,
  CheckBoxGroup,
  RadioButton,
  Calendar,
  Signature,
  Header,
  DataTable,
  TableHeader,
  HeaderCheckBox,
  SvgComponent,
  Image,
  InputLabel,
  Label,
  TreeGrid,
  InputSwitch,
  CardDetailsFieldGroupComponent,
  DisplayValueDirective,
  FormGridFiller,
  InputLegend,
  FormErrorMessage,
  PrintDirective,
  InputMaskComp,
  NmAutocomplete,
  Tab,
  NmChart,
  RichText
];
const imports = [
  FormsModule,
  DropdownModule,
  DataTableModule,
  AccordionModule,
  ReactiveFormsModule,
  GrowlModule,
  TooltipModule,
  PickListModule,
  FileUploadModule,
  ListboxModule,
  CheckboxModule,
  RadioButtonModule,
  CalendarModule,
  HttpModule,
  AutoCompleteModule,
  HttpClientModule,
  TableModule,
  KeyFilterModule,
  StorageServiceModule,
  AngularSvgIconModule,
  ToastModule,
  TreeTableModule,
  InputSwitchModule,
  InputMaskModule,
  TabViewModule,
  ChartModule,
  EditorModule
];
const providers = [
  { provide: PageService, useClass: MockPageService },
  { provide: 'JSNLOG', useValue: JL },
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: LoggerService, useClass: MockLoggerService },
  CustomHttpClient,
  LoaderService,
  ConfigService,
  AppInitService
];

let fixture, hostComponent;

describe('Section', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Section);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
  });

  it('should create the Section', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('ngOnInit() should call pageSvc.processEvent()', async(() => {
    const service = TestBed.get(PageService);
    hostComponent.element.config.initializeComponent = () => {
      return true;
    };
    spyOn(service, 'processEvent').and.callThrough();
    hostComponent.ngOnInit();
    expect(service.processEvent).toHaveBeenCalled();
  }));

  it('ngOnInit() should not call pageSvc.processEvent()', async(() => {
    const service = TestBed.get(PageService);
    hostComponent.element = {} as Param;
    spyOn(service, 'processEvent').and.callThrough();
    hostComponent.ngOnInit();
    expect(service.processEvent).not.toHaveBeenCalled();
  }));
});
