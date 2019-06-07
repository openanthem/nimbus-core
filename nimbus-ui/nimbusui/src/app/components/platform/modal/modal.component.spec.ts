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
  TooltipModule,
  TreeTableModule
} from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { Subject } from 'rxjs';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { PrintDirective } from '../../../directives/print.directive';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { ConfigService } from '../../../services/config.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { PageService } from '../../../services/page.service';
import { setup } from '../../../setup.spec';
import { CardDetailsFieldGroupComponent } from '../../platform/card/card-details-field-group.component';
import { Label } from '../../platform/content/label.component';
import { ComboBox } from '../../platform/form/elements/combobox.component';
import { InputLabel } from '../../platform/form/elements/input-label.component';
import { InputLegend } from '../../platform/form/elements/input-legend.component';
import { InputSwitch } from '../../platform/form/elements/input-switch.component';
import { FormGridFiller } from '../../platform/form/form-grid-filler.component';
import { TreeGrid } from '../../platform/tree-grid/tree-grid.component';
import { CardDetailsFieldComponent } from '../card/card-details-field.component';
import { CardDetailsGrid } from '../card/card-details-grid.component';
import { CardDetailsComponent } from '../card/card-details.component';
import { Accordion } from '../content/accordion.component';
import { Header } from '../content/header.component';
import { Paragraph } from '../content/paragraph.component';
import { StaticText } from '../content/static-content.component';
import { FileUploadComponent } from '../fileupload/file-upload.component';
import { FormElement } from '../form-element.component';
import { FormErrorMessage } from '../form-error-message.component';
import { FrmGroupCmp } from '../form-group.component';
import { Form } from '../form.component';
import {
  ActionDropdown,
  ActionLink
} from '../form/elements/action-dropdown.component';
import { NmAutocomplete } from '../form/elements/autocomplete.component';
import { ButtonGroup } from '../form/elements/button-group.component';
import { Calendar } from '../form/elements/calendar.component';
import { CheckBoxGroup } from '../form/elements/checkbox-group.component';
import { CheckBox } from '../form/elements/checkbox.component';
import { HeaderCheckBox } from '../form/elements/header-checkbox.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { MultiselectCard } from '../form/elements/multi-select-card.component';
import { MultiSelectListBox } from '../form/elements/multi-select-listbox.component';
import { OrderablePickList } from '../form/elements/picklist.component';
import { RadioButton } from '../form/elements/radio.component';
import { RichText } from '../form/elements/rich-text.component';
import { Signature } from '../form/elements/signature.component';
import { TextArea } from '../form/elements/textarea.component';
import { InputText } from '../form/elements/textbox.component';
import { DataTable } from '../grid/table.component';
import { Image } from '../image.component';
import { Link } from '../link.component';
import { Menu } from '../menu.component';
import { MessageComponent } from '../message/message.component';
import { SvgComponent } from '../svg/svg.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { NmChart } from './../charts/chart.component';
import { InputMaskComp } from './../form/elements/input-mask.component';
import { TableHeader } from './../grid/table-header.component';
import { Modal } from './modal.component';

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

@Component({
  selector: 'nm-section',
  template: '<div></div>'
})
export class Section {
  @Input() position: any;
  @Input() element: any;
  viewComponent: any;
  componentTypes: any;
  constructor() {}
}

class MockPageService {
  public eventUpdate$: Subject<any>;

  constructor() {
    this.eventUpdate$ = new Subject();
  }
  processEvent(a, b, c, d) {}
}

let pageservice, param;

const declarations = [
  Modal,
  TooltipComponent,
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
  InputSwitch,
  TreeGrid,
  Label,
  InputLabel,
  CardDetailsFieldGroupComponent,
  DisplayValueDirective,
  FormGridFiller,
  InputLegend,
  FormErrorMessage,
  PrintDirective,
  InputMaskComp,
  NmAutocomplete,
  NmChart,
  RichText
];
const imports = [
  DialogModule,
  FormsModule,
  DropdownModule,
  DataTableModule,
  AccordionModule,
  ReactiveFormsModule,
  GrowlModule,
  TooltipModule,
  MessagesModule,
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
  AngularSvgIconModule,
  ToastModule,
  InputSwitchModule,
  TreeTableModule,
  InputMaskModule,
  AutoCompleteModule,
  ChartModule,
  EditorModule
];
const providers = [
  { provide: PageService, useClass: MockPageService },
  CustomHttpClient,
  LoaderService,
  ConfigService
];
let fixture, hostComponent;
describe('Modal', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Modal);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
    pageservice = TestBed.get(PageService);
  });

  it('should create the Modal', async(() => {
    // fixture.detectChanges();
    fixture.whenStable().then(() => {
      console.log('hostComponent.element', hostComponent.element);

      expect(hostComponent).toBeTruthy();
    });
  }));

  it('width should be 500 for small size', async(() => {
    // fixture.detectChanges();
    // fixture.whenStable().then(() => {
    // hostComponent.element.config.uiStyles.attributes.width = 'small';
    // expect(hostComponent.width).toEqual('500');
    // });
  }));

  // it('width should be 700 for medium size', async(() => {
  //     // fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.width = 'medium';
  //         expect(hostComponent.width).toEqual('700');
  //     // });
  // }));

  // it('width should be 900 for large size', async(() => {
  //     // fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.width = 'large';
  //         expect(hostComponent.width).toEqual('900');
  //     // });
  // }));

  // it('width property should be updated from element if size is not available', async(() => {
  //     // fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.width = '999';
  //         expect(hostComponent.width).toEqual('999');
  //     // });
  // }));

  // it('closeDialog() should call pageservice.processEvent', async(() => {
  //     hostComponent.element.visible = true;
  //     spyOn(pageservice, 'processEvent').and.callThrough();
  //     hostComponent.closeDialog(false);
  //     expect(pageservice.processEvent).toHaveBeenCalled();
  // }));

  // it('closeDialog() should not call pageservice.processEvent', async(() => {
  //     hostComponent.element.visible = false;
  //     spyOn(pageservice, 'processEvent').and.callThrough();
  //     hostComponent.closeDialog('a');
  //     expect(pageservice.processEvent).not.toHaveBeenCalled();
  // }));

  // it('resizable property should be updated from element', async(() => {
  //     // fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.resizable = true;
  //         expect(hostComponent.resizable).toEqual(true);

  //     // });
  // }));

  // it('closable property should updated from the element', function (this: TestContext<Modal>) {
  //     // fixture.whenStable().then(() => {
  //         hostComponent.element.config.uiStyles.attributes.closable = true;
  //         expect(hostComponent.closable).toEqual(true);
  //     // });
  // }));
});
