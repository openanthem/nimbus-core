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
  DialogModule,
  DropdownModule,
  EditorModule,
  FileUploadModule,
  GrowlModule,
  InputMaskModule,
  InputSwitchModule,
  ListboxModule,
  MessagesModule,
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
import { CardDetailsFieldGroupComponent } from '../platform/card/card-details-field-group.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { Accordion } from '../platform/content/accordion.component';
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
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { TreeGrid } from '../platform/tree-grid/tree-grid.component';
import { setup } from './../../setup.spec';
import { NmChart } from './charts/chart.component';
import { Header } from './content/header.component';
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
import { MessageComponent } from './message/message.component';
import { Modal } from './modal/modal.component';
import { Section } from './section.component';
import { SvgComponent } from './svg/svg.component';
import { Tile } from './tile.component';

let pageService;

class MockPageService {
  processEvent() {}
}

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

class MockLoggerService {
  debug() {}
  info() {}
  error() {}
}

const declarations = [
  Tile,
  MessageComponent,
  Header,
  Section,
  Modal,
  CardDetailsGrid,
  CardDetailsComponent,
  Paragraph,
  StaticText,
  Form,
  Link,
  Menu,
  Button,
  ButtonGroup,
  InputText,
  ComboBox,
  TooltipComponent,
  CardDetailsFieldComponent,
  FrmGroupCmp,
  Accordion,
  ActionDropdown,
  DateTimeFormatPipe,
  SelectItemPipe,
  InPlaceEditorComponent,
  TextArea,
  FormElement,
  ActionLink,
  FileUploadComponent,
  OrderablePickList,
  MultiselectCard,
  MultiSelectListBox,
  CheckBox,
  CheckBoxGroup,
  RadioButton,
  Calendar,
  Signature,
  TableHeader,
  DataTable,
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
  GrowlModule,
  MessagesModule,
  DialogModule,
  ReactiveFormsModule,
  AccordionModule,
  DataTableModule,
  TooltipModule,
  DropdownModule,
  FileUploadModule,
  PickListModule,
  ListboxModule,
  CheckboxModule,
  RadioButtonModule,
  CalendarModule,
  HttpModule,
  HttpClientTestingModule,
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
  EditorModule,
  AutoCompleteModule
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

describe('Tile', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Tile);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
    pageService = TestBed.get(PageService);
  });

  it('should create the Tile', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  // it('based on the xSmall size the styleWd and styleHt should be updated',  () => {
  //     fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.size = 'XSmall';
  //         hostComponent.element.config.initializeComponent = () => { return false };
  //         hostComponent.ngOnInit();
  //         expect(hostComponent.styleWd).toEqual('card-holder col-lg-3 col-md-6 XsmallCard');
  //         expect(hostComponent.styleHt).toEqual('height-md');
  //     });
  // });

  // it('based on the small size the styleWd and styleHt should be updated',  () => {
  //     fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.size = 'Small';
  //         hostComponent.element.config.initializeComponent = () => { return false };
  //         hostComponent.ngOnInit();
  //         expect(hostComponent.styleWd).toEqual('col-lg-4 col-md-6 smallCard');
  //         expect(hostComponent.styleHt).toEqual('height-md');
  //     });
  // });

  // it('based on the medium size the styleWd and styleHt should be updated',  () => {
  //     fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.size = 'Medium';
  //         hostComponent.element.config.initializeComponent = () => { return false };
  //         hostComponent.ngOnInit();
  //         expect(hostComponent.styleWd).toEqual('card-holder col-md-6 mediumCard');
  //         expect(hostComponent.styleHt).toEqual('height-md');
  //     });
  // });

  // it('based on the colorBox size the styleWd and styleHt should be updated',  () => {
  //     fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.size = 'Colorbox';
  //         hostComponent.element.config.initializeComponent = () => { return false };
  //         hostComponent.ngOnInit();
  //         expect(hostComponent.styleWd).toEqual('');
  //         expect(hostComponent.styleHt).toEqual('');
  //     });
  // });

  // it('based on the size the styleWd and styleHt should be updated',  () => {
  //     fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.size = '';
  //         hostComponent.element.config.initializeComponent = () => { return false };
  //         hostComponent.ngOnInit();
  //         expect(hostComponent.styleWd).toEqual('');
  //         expect(hostComponent.styleHt).toEqual('');
  //     });
  // });

  // it('ngOnInit() should update the styleWd',  () => {
  //     fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.size = '';
  //         hostComponent.element.config.initializeComponent = () => { return false };
  //         hostComponent.tileType = 'subcard';
  //         hostComponent.ngOnInit();
  //         expect(hostComponent.styleWd).toEqual(' subcard');
  //     });
  // });

  // it('ngOnInit() should call the pageService.processEvent',  () => {
  //     fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.size = '';
  //         hostComponent.element.config.initializeComponent = () => { return true };
  //         spyOn(pageService, 'processEvent').and.callThrough();
  //         hostComponent.ngOnInit();
  //         expect(pageService.processEvent).toHaveBeenCalled();
  //     });
  // });
});
