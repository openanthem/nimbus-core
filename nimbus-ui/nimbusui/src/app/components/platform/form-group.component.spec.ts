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
  HashLocationStrategy,
  Location,
  LocationStrategy
} from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { async, TestBed } from '@angular/core/testing';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule
} from '@angular/forms';
import { HttpModule } from '@angular/http';
import { By } from '@angular/platform-browser';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import {
  formGroupNestedFrmGrpEle,
  formGroupNmButtonParam,
  formGroupNmElementInputParam,
  formGroupNmFormGridFiller,
  formGroupNmHeaderParam,
  formGroupNmLinkParam,
  formGroupnmParagraphParam,
  formGroupNmPickListParam,
  formGroupParam
} from 'mockdata';
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
import { ConfigService } from '../../services/config.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { LoggerService } from '../../services/logger.service';
import { PageService } from '../../services/page.service';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from '../../services/session.store';
import { Param } from '../../shared/param-state';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { Accordion } from '../platform/content/accordion.component';
import { Header } from '../platform/content/header.component';
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
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { Signature } from '../platform/form/elements/signature.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { InputText } from '../platform/form/elements/textbox.component';
import { Link } from '../platform/link.component';
import { Menu } from '../platform/menu.component';
import { MessageComponent } from '../platform/message/message.component';
import { Section } from '../platform/section.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { CounterMessageService } from './../../services/counter-message.service';
import { NmMessageService } from './../../services/toastmessage.service';
import { setup } from './../../setup.spec';
import { CardDetailsFieldGroupComponent } from './card/card-details-field-group.component';
import { NmChart } from './charts/chart.component';
import { Label } from './content/label.component';
import { Tab } from './content/tab.component';
import { FormElement } from './form-element.component';
import { FormErrorMessage } from './form-error-message.component';
import { FrmGroupCmp } from './form-group.component';
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
import { SvgComponent } from './svg/svg.component';
import { TreeGrid } from './tree-grid/tree-grid.component';

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

class MockLoggerService {
  debug() {}
  info() {}
  error() {}
}

const declarations = [
  FrmGroupCmp,
  FormElement,
  Button,
  ButtonGroup,
  Header,
  Paragraph,
  InputText,
  TextArea,
  Calendar,
  ComboBox,
  RadioButton,
  CheckBoxGroup,
  CheckBox,
  MultiSelectListBox,
  MultiselectCard,
  OrderablePickList,
  FileUploadComponent,
  TooltipComponent,
  SelectItemPipe,
  ActionDropdown,
  DateTimeFormatPipe,
  Section,
  ActionLink,
  MessageComponent,
  CardDetailsGrid,
  Accordion,
  Menu,
  CardDetailsComponent,
  StaticText,
  Form,
  Link,
  CardDetailsFieldComponent,
  InPlaceEditorComponent,
  Signature,
  DataTable,
  TableHeader,
  HeaderCheckBox,
  SvgComponent,
  Image,
  TreeGrid,
  InputSwitch,
  FormGridFiller,
  NmAutocomplete,
  DisplayValueDirective,
  InputLabel,
  Label,
  CardDetailsFieldGroupComponent,
  InputLegend,
  FormErrorMessage,
  PrintDirective,
  InputMaskComp,
  Tab,
  NmChart,
  RichText
];
const imports = [
  FormsModule,
  ReactiveFormsModule,
  CalendarModule,
  DropdownModule,
  RadioButtonModule,
  CheckboxModule,
  ListboxModule,
  PickListModule,
  FileUploadModule,
  DataTableModule,
  GrowlModule,
  AccordionModule,
  HttpModule,
  HttpClientModule,
  TooltipModule,
  TableModule,
  KeyFilterModule,
  AngularSvgIconModule,
  ToastModule,
  InputSwitchModule,
  TreeTableModule,
  StorageServiceModule,
  InputMaskModule,
  TabViewModule,
  AutoCompleteModule,
  ChartModule,
  EditorModule
];
const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: LoggerService, useClass: MockLoggerService },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  Location,
  SessionStoreService,
  PageService,
  CustomHttpClient,
  LoaderService,
  NmMessageService,
  ConfigService,
  CounterMessageService
];

let fixture, hostComponent;

describe('FrmGroupCmp', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FrmGroupCmp);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = formGroupNmElementInputParam as Param;
    hostComponent.form = hostComponent.form = new FormGroup({
      question123: new FormControl(),
      selected: new FormControl(),
      question13: new FormControl(),
      question14: new FormControl(),
      question15: new FormControl()
    });
  });

  it('should create the FrmGroupCmp', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('getCssClass() should return the element.config.uiStyles.attributes.cssClass', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias =
        'FormElementGroup';
      hostComponent.element.config.uiStyles.attributes.cssClass = 'test';
      expect(hostComponent.getCssClass()).toEqual('test');
    });
  });

  it('getCssClass() should return elementCss', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias =
        'FormElementGroup';
      hostComponent.element.config.uiStyles.attributes.cssClass = 'test1';
      expect(hostComponent.getCssClass()).toEqual('test1');
    });
  });

  it('getCssClass() should return empty string', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias =
        'FormElementGroup1';
      expect(hostComponent.getCssClass()).toEqual('');
    });
  });

  it('All elements in form group should be created if the element.visible==true', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const spanEle = debugElement.query(By.css('span'));
    expect(spanEle).toBeTruthy();
  }));

  it('Span should get the css class from getCssClass()', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const spanEle = debugElement.query(By.css('span'));
    expect(spanEle).toBeTruthy();
  }));

  it('nm-input-legend inside should be created', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputLegendEle = debugElement.query(By.css('nm-input-legend'));
    expect(inputLegendEle).toBeTruthy();
  }));

  it('fieldset should be created if @FormElementGroup is configured', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const fieldSetEle = debugElement.query(By.css('fieldset'));
    expect(fieldSetEle).toBeTruthy();
  }));

  it('fieldset should not be created if @FormElementGroup is not configured', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const fieldSetEle = debugElement.query(By.css('fieldset'));
    expect(fieldSetEle).toBeFalsy();
  }));

  it('Nested nm-frm-grp should be created based on the element.type.model.params', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.query(By.css('nm-frm-grp'));
    expect(frmGrpEle).toBeTruthy();
  }));

  it('Nested nm-frm-grp should not be created based on the element.type.model.params', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
    hostComponent.element.type.model.params = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.query(By.css('nm-frm-grp'));
    expect(frmGrpEle).toBeFalsy();
  }));

  it('nm-element should be created based on the nestedparams length', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl()
    });
    hostComponent.element = formGroupNmElementInputParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.query(By.css('nm-element'));
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeTruthy();
    expect(nmElementEle).toBeTruthy();
  }));

  it('nm-element should be created based on the collection', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl()
    });
    formGroupNmElementInputParam.type['model'] = { params: [1] };
    formGroupNmElementInputParam.config.type.collection = true;
    hostComponent.element = formGroupNmElementInputParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.query(By.css('nm-element'));
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeTruthy();
    expect(nmElementEle).toBeTruthy();
  }));

  it('nm-element should not be created based on the collection or with no nestedparams', async(() => {
    formGroupNmElementInputParam.config.type.collection = false;
    hostComponent.element = formGroupNmElementInputParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.query(By.css('nm-element'));
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeFalsy();
    expect(nmElementEle).toBeFalsy();
  }));

  it('nm-element should be created if @Picklist is configured', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
      selected: new FormControl()
    });
    hostComponent.element = formGroupNmPickListParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.queryAll(By.css('nm-element'));
    const picklictEle = debugElement.query(By.css('nm-pickList'));
    expect(nmElementEle).toBeTruthy();
    expect(picklictEle).toBeTruthy();
  }));

  it('nm-element should not be created if @Picklist is not configured', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
      selected: new FormControl()
    });
    formGroupNmPickListParam.config.uiStyles.attributes.alias = '';
    hostComponent.element = formGroupNmPickListParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const picklictEle = debugElement.query(By.css('nm-pickList'));
    expect(picklictEle).toBeFalsy();
  }));

  it('nm-button should be created if @Button is configured', async(() => {
    hostComponent.element = formGroupNmButtonParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmBtnEle = debugElement.query(By.css('nm-button'));
    expect(nmBtnEle).toBeTruthy();
  }));

  it('nm-button should not be created if @Button is not configured', async(() => {
    formGroupNmButtonParam.config.uiStyles.attributes.alias = '';
    hostComponent.element = formGroupNmButtonParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmBtnEle = debugElement.query(By.css('nm-button'));
    expect(nmBtnEle).toBeFalsy();
  }));

  it('nm-form-grid-filler should be created if @FormGridFiller is configured', async(() => {
    hostComponent.element = formGroupNmFormGridFiller as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGridFillerEle = debugElement.query(By.css('nm-form-grid-filler'));
    expect(frmGridFillerEle).toBeTruthy();
  }));

  it('nm-form-grid-filler should not be created if @FormGridFiller is not configured', async(() => {
    formGroupNmFormGridFiller.config.uiStyles.attributes.alias = '';
    hostComponent.element = formGroupNmFormGridFiller as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGridFillerEle = debugElement.query(By.css('nm-form-grid-filler'));
    expect(frmGridFillerEle).toBeFalsy();
  }));

  it('nm-link should be created if @Link is configured', async(() => {
    hostComponent.element = formGroupNmLinkParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeTruthy();
  }));

  it('nm-link should not be created if @Link is not configured', async(() => {
    hostComponent.element = formGroupNmLinkParam as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeFalsy();
  }));

  it('nm-paragraph should be created if @Paragraph is configured', async(() => {
    hostComponent.element = formGroupnmParagraphParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const paragraphEle = debugElement.query(By.css('nm-paragraph'));
    expect(paragraphEle).toBeTruthy();
  }));

  it('nm-paragraph should not be created if @Paragraph is not configured', async(() => {
    formGroupnmParagraphParam.config.uiStyles.attributes.alias = '';
    hostComponent.element = formGroupnmParagraphParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const paragraphEle = debugElement.query(By.css('nm-paragraph'));
    expect(paragraphEle).toBeFalsy();
  }));

  it('nm-header should be created if @Header is configured', async(() => {
    hostComponent.element = formGroupNmHeaderParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const headerEle = debugElement.query(By.css('nm-header'));
    expect(headerEle).toBeTruthy();
  }));

  it('nm-header should not be created if @Header is not configured', async(() => {
    hostComponent.element = formGroupNmHeaderParam as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const headerEle = debugElement.query(By.css('nm-header'));
    expect(headerEle).toBeFalsy();
  }));

  it('nm-button-group should be created if @ButtonGroup is configured', async(() => {
    hostComponent.element = formGroupParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const btnGrpEle = debugElement.query(By.css('nm-button-group'));
    expect(btnGrpEle).toBeTruthy();
  }));

  it('nm-button-group should not be created if @ButtonGroup is not configured', async(() => {
    hostComponent.element = formGroupParam as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const btnGrpEle = debugElement.query(By.css('nm-button-group'));
    expect(btnGrpEle).toBeFalsy();
  }));

  it('All elements in form group should be hidden if the element.visible!==false', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    hostComponent.element.visible = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const spanEle: any = debugElement.query(By.css('span'));
    expect(spanEle.nativeElement.attributes[2].name).toEqual('hidden');
  }));
});
