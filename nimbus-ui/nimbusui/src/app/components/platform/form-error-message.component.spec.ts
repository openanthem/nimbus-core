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
import { TestBed } from '@angular/core/testing';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidatorFn,
  Validators
} from '@angular/forms';
import { HttpModule } from '@angular/http';
import { By } from '@angular/platform-browser';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { StorageServiceModule } from 'angular-webstorage-service';
import { formErrorMessageParam } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { ChartModule } from 'primeng/chart';
import { EditorModule } from 'primeng/editor';
import { KeyFilterModule } from 'primeng/keyfilter';
import {
  AccordionModule,
  AutoCompleteModule,
  CalendarModule,
  CheckboxModule,
  DataTableModule,
  DialogModule,
  DropdownModule,
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
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { Accordion } from '../platform/content/accordion.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { StaticText } from '../platform/content/static-content.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
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
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { Signature } from '../platform/form/elements/signature.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { InputText } from '../platform/form/elements/textbox.component';
import { Link } from '../platform/link.component';
import { Menu } from '../platform/menu.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { CounterMessageService } from './../../services/counter-message.service';
import { setup } from './../../setup.spec';
import { Param } from './../../shared/param-state';
import { CardDetailsFieldGroupComponent } from './card/card-details-field-group.component';
import { NmChart } from './charts/chart.component';
import { Header } from './content/header.component';
import { Label } from './content/label.component';
import { Tab } from './content/tab.component';
import { FormElement } from './form-element.component';
import { FormErrorMessage } from './form-error-message.component';
import { FrmGroupCmp } from './form-group.component';
import { Form } from './form.component';
import { NmAutocomplete } from './form/elements/autocomplete.component';
import { InputLabel } from './form/elements/input-label.component';
import { InputLegend } from './form/elements/input-legend.component';
import { InputMaskComp } from './form/elements/input-mask.component';
import { InputSwitch } from './form/elements/input-switch.component';
import { RichText } from './form/elements/rich-text.component';
import { FormGridFiller } from './form/form-grid-filler.component';
import { TableHeader } from './grid/table-header.component';
import { DataTable } from './grid/table.component';
import { Image } from './image.component';
import { MessageComponent } from './message/message.component';
import { Section } from './section.component';
import { SvgComponent } from './svg/svg.component';
import { TreeGrid } from './tree-grid/tree-grid.component';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
let param: Param;

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

const declarations = [
  Form,
  FrmGroupCmp,
  Accordion,
  ButtonGroup,
  Button,
  FormElement,
  MessageComponent,
  DataTable,
  TableHeader,
  FileUploadComponent,
  OrderablePickList,
  MultiselectCard,
  MultiSelectListBox,
  CheckBox,
  CheckBoxGroup,
  RadioButton,
  ComboBox,
  Calendar,
  TextArea,
  Signature,
  InputText,
  Paragraph,
  Header,
  Section,
  ActionDropdown,
  TooltipComponent,
  SelectItemPipe,
  Menu,
  Link,
  StaticText,
  CardDetailsComponent,
  CardDetailsGrid,
  ActionLink,
  CardDetailsFieldComponent,
  InPlaceEditorComponent,
  DateTimeFormatPipe,
  HeaderCheckBox,
  SvgComponent,
  Image,
  TreeGrid,
  InputSwitch,
  FormGridFiller,
  DisplayValueDirective,
  InputLabel,
  FormErrorMessage,
  Label,
  CardDetailsFieldGroupComponent,
  InputLegend,
  PrintDirective,
  InputMaskComp,
  NmAutocomplete,
  Tab,
  NmChart,
  RichText
];
const imports = [
  FormsModule,
  ReactiveFormsModule,
  GrowlModule,
  MessagesModule,
  DialogModule,
  ReactiveFormsModule,
  AccordionModule,
  DataTableModule,
  DropdownModule,
  FileUploadModule,
  PickListModule,
  TooltipModule,
  ListboxModule,
  CheckboxModule,
  RadioButtonModule,
  CalendarModule,
  TableModule,
  KeyFilterModule,
  HttpModule,
  HttpClientTestingModule,
  StorageServiceModule,
  AngularSvgIconModule,
  ToastModule,
  InputSwitchModule,
  TreeTableModule,
  InputMaskModule,
  TabViewModule,
  AutoCompleteModule,
  ChartModule,
  EditorModule
];
const providers = [CounterMessageService];

let fixture, hostComponent;
describe('form error message component', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormErrorMessage);
    hostComponent = fixture.debugElement.componentInstance;
    const fg = new FormGroup({});
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    formErrorMessageParam.type.model.params[0].visible = true;
    fg.addControl(
      formErrorMessageParam.type.model.params[0].config.code,
      new FormControl(
        formErrorMessageParam.type.model.params[0].leafState,
        checks
      )
    );
    hostComponent.form = fg;
    hostComponent.element = formErrorMessageParam;
  });

  it('check if message is displayed', () => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      let messageDom = fixture.debugElement.query(By.css('div'));
      expect(hostComponent).toBeTruthy();
      expect(messageDom.nativeElement.innerHTML).toEqual(' Remaining 0 of 1 ');
    });
  });

  it('check if message is re-evaluated on form value change', () => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      hostComponent.form.controls[
        formErrorMessageParam.type.model.params[0].config.code
      ].setValue('');
      fixture.detectChanges();
      let messageDom = fixture.debugElement.query(By.css('div'));
      expect(hostComponent).toBeTruthy();
      expect(messageDom.nativeElement.innerHTML).toEqual(' Remaining 1 of 1 ');
    });
  });
});
