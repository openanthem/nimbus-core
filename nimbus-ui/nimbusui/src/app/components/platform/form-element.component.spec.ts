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

import {
  HashLocationStrategy,
  Location,
  LocationStrategy
} from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import {
  Component,
  EventEmitter,
  forwardRef,
  Input,
  NO_ERRORS_SCHEMA,
  Output
} from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {
  FormControl,
  FormGroup,
  FormsModule,
  NG_VALUE_ACCESSOR,
  ReactiveFormsModule
} from '@angular/forms';
import { HttpModule } from '@angular/http';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import {
  formCalendar,
  formCheckBox,
  formCheckBoxGrp,
  formComboBox,
  formInput,
  formInputSwitch,
  formMultiSelect,
  formMultiSelectCard,
  formPicklist,
  formRadio,
  formRichText,
  formSignature,
  formTable,
  formTextArea,
  formTreeGrid,
  formUpload
} from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { MessageService } from 'primeng/api';
import { ChartModule } from 'primeng/chart';
import { KeyFilterModule } from 'primeng/keyfilter';
import {
  AccordionModule,
  CalendarModule,
  CheckboxModule,
  DataTableModule,
  DropdownModule,
  EditorModule,
  FileUploadModule,
  GrowlModule,
  InputSwitchModule,
  ListboxModule,
  PickListModule,
  RadioButtonModule,
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
import { CounterMessageService } from '../../services/counter-message.service';
import { FileService } from '../../services/file.service';
import { GridService } from '../../services/grid.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { LoggerService } from '../../services/logger.service';
import { PageService } from '../../services/page.service';
import { PrintService } from '../../services/print.service';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from '../../services/session.store';
import { GridUtils } from '../../shared/grid-utils';
import { Message } from '../../shared/message';
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
import { NmMessageService } from './../../services/toastmessage.service';
import { CardDetailsFieldGroupComponent } from './card/card-details-field-group.component';
import { NmChart } from './charts/chart.component';
import { Label } from './content/label.component';
import { FormElement } from './form-element.component';
import { FormErrorMessage } from './form-error-message.component';
import { FrmGroupCmp } from './form-group.component';
import { InputLabel } from './form/elements/input-label.component';
import { InputLegend } from './form/elements/input-legend.component';
import { InputSwitch } from './form/elements/input-switch.component';
import { RichText } from './form/elements/rich-text.component';
import { FormGridFiller } from './form/form-grid-filler.component';
import { TableHeader } from './grid/table-header.component';
import { DataTable } from './grid/table.component';
import { Image } from './image.component';
import { SvgComponent } from './svg/svg.component';
'use strict';

let param: Param;
let fixture: ComponentFixture<any>, hostComponent;

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
  template: '<div></div>',
  selector: 'nm-treegrid',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: forwardRef(() => TreeGrid)
    }
  ]
})
class TreeGrid {
  @Input() params: any[];
  @Input() form: any;
  firstColumn: any;
  viewComponent: any;
  treeData: any;

  writeValue(a) {}
  registerOnChange(z) {}
  registerOnTouched(a) {}
  setDisabledState(a) {}
}

class MockLoggerService {
  debug() {}
  info() {}
  error() {}
}

const declarations = [
  FormElement,
  MessageComponent,
  DataTable,
  TableHeader,
  FileUploadComponent,
  OrderablePickList,
  MultiSelectListBox,
  MultiselectCard,
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
  ActionLink,
  ActionDropdown,
  TooltipComponent,
  SelectItemPipe,
  ButtonGroup,
  Button,
  Accordion,
  Menu,
  Link,
  Form,
  StaticText,
  CardDetailsComponent,
  CardDetailsGrid,
  FrmGroupCmp,
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
  Label,
  CardDetailsFieldGroupComponent,
  InputLegend,
  FormErrorMessage,
  PrintDirective,
  NmChart,
  RichText
];
const imports = [
  FormsModule,
  ReactiveFormsModule,
  GrowlModule,
  AccordionModule,
  PickListModule,
  ListboxModule,
  CalendarModule,
  DataTableModule,
  DropdownModule,
  FileUploadModule,
  RadioButtonModule,
  CheckboxModule,
  TableModule,
  KeyFilterModule,
  AngularSvgIconModule,
  ToastModule,
  InputSwitchModule,
  TreeTableModule,
  HttpClientModule,
  StorageServiceModule,
  HttpModule,
  BrowserAnimationsModule,
  RouterTestingModule,
  ChartModule,
  EditorModule
];
const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  { provide: LoggerService, useClass: MockLoggerService },
  Location,
  PageService,
  CustomHttpClient,
  SessionStoreService,
  LoaderService,
  ConfigService,
  AppInitService,
  MessageService,
  FileService,
  GridService,
  PrintService,
  GridUtils,
  DateTimeFormatPipe,
  NmMessageService,
  {
    provide: NG_VALUE_ACCESSOR,
    multi: true,
    useExisting: forwardRef(() => TreeGrid)
  },
  CounterMessageService
];

describe('FormElement', () => {
  configureTestSuite(() => {
    TestBed.configureTestingModule({
      declarations: declarations,
      providers: providers,
      imports: imports,
      schemas: [NO_ERRORS_SCHEMA]
    });
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormElement);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.form = new FormGroup({
      userGroups: new FormControl(),
      firstName: new FormControl(),
      lastName: new FormControl(),
      testingmulticard: new FormControl(),
      shouldUseNickname: new FormControl(),
      notificationPreference: new FormControl(),
      notes: new FormControl(),
      q13: new FormControl(),
      dob: new FormControl(),
      q1_a: new FormControl(),
      q9_b: new FormControl(),
      medications1: new FormControl(),
      calls: new FormControl(),
      treegrid: new FormControl(),
      selected: new FormControl(),
      richTextbox: new FormControl()
    });
    hostComponent.element = formMultiSelect as Param;
  });

  it('two way binding', () => {
    fixture.whenStable().then(() => {
      hostComponent.elementCss = '';
      fixture.detectChanges();
      let textBox;
      textBox = fixture.debugElement.query(By.css('.form-control.text-input'))
        .nativeElement;
      textBox.value = 'abcd123';
      textBox.dispatchEvent(new Event('input'));
      textBox.dispatchEvent(new Event('focusout'));
      fixture.detectChanges();
      expect(hostComponent.form.controls[param.config.code].value).toEqual(
        'abcd123'
      );
    });
  });

  it('getErrorStyles() should return alert string', () => {
    fixture.whenStable().then(() => {
      hostComponent.elementCss = '';
      fixture.detectChanges();
      let textBox;
      textBox = fixture.debugElement.query(By.css('.form-control.text-input'))
        .nativeElement;
      textBox.value = null;
      textBox.dispatchEvent(new Event('input'));
      fixture.detectChanges();
      expect(hostComponent.getErrorStyles()).toEqual('alert alert-danger');
    });
  });

  it('should create the FormElement', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('isValid property should be updated from form.controls', async(() => {
    hostComponent.elementCss = '';
    hostComponent.form.controls[hostComponent.element.config.code].setValue(
      'test'
    );
    fixture.detectChanges();
    expect(hostComponent.isValid).toBeTruthy();
  }));

  it('isValid property should not be updated from form.controls', async(() => {
    hostComponent.form.controls[hostComponent.element.config.code] = null;
    expect(hostComponent.isValid).toBeTruthy();
  }));

  it('isPristine property should be updated from form.controls', async(() => {
    hostComponent.form.controls[hostComponent.element.config.code].setValue(
      'test'
    );
    expect(hostComponent.isPristine).toEqual(true);
  }));

  it('isPristine property should be updated from element even if it returns false', async(() => {
    hostComponent.form.controls[hostComponent.element.config.code].setValue(
      null
    );
    expect(hostComponent.isPristine).toEqual(true);
  }));

  it('getMessages() should return message from the element', async(() => {
    const message = new Message();
    message.text = 'test';
    hostComponent.element.message = [message];
    expect(hostComponent.getMessages()).toEqual(hostComponent.element.message);
  }));

  it('getMessages() should return return empty array', async(() => {
    hostComponent.element.message = [];
    expect(hostComponent.getMessages()).toEqual([]);
  }));

  it('showMessages should be updated based on the elemMessages', async(() => {
    const message = new Message();
    message.text = 't';
    hostComponent.elemMessages = [message];
    expect(hostComponent.showMessages).toEqual(true);
  }));

  it('getErrorStyles() should return empty string', async(() => {
    expect(hostComponent.getErrorStyles()).toEqual('');
  }));

  it('elementCss should be undefined', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.controlId = null;
      expect(hostComponent.elementCss).toEqual(undefined);
    });
  });

  it('getElementStyle() should return col-lg-12 col-md-6', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias =
        'MultiSelectCard';
      expect(hostComponent.getElementStyle()).toEqual('col-lg-12 col-md-6');
    });
  });

  it('getElementStyle() should return empty array', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias = '';
      expect(hostComponent.getElementStyle()).toEqual('');
    });
  });

  it('addErrorMessages() should update the elemMessages[0].messageArray[0].detail', async(() => {
    hostComponent.elemMessages = [];
    hostComponent.addErrorMessages('testing error message');
    expect(hostComponent.elemMessages[0].messageArray[0].detail).toEqual(
      'testing error message'
    );
  }));

  it('ngModelState() should return string with touched', async(() => {
    let ngm: any;
    const res = hostComponent.ngModelState(ngm);
    expect(res.indexOf('touched')).toEqual(0);
  }));

  it('form element should be created if the dataentryfield is configured', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const divEle = debugElement.query(By.css('div'));
    expect(divEle).toBeTruthy();
  }));

  it('nm-input should be created if the text is configured', async(() => {
    hostComponent.element = formInput as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeTruthy();
  }));

  it('nm-input should not be created if the text is not configured', async(() => {
    hostComponent.element = formInput as Param;
    hostComponent.element.config.uiStyles.attributes.type = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeFalsy();
  }));

  it('signature component should be created if the signature is configured', async(() => {
    hostComponent.element = formSignature as Param;
    fixture.detectChanges();
    const signatureEle = document.getElementsByTagName('nm-signature');
    expect(signatureEle.length > 0).toBeTruthy();
  }));

  it('signature component should not be created if the signature is not configured', async(() => {
    hostComponent.element = formSignature as Param;
    hostComponent.element.config.uiStyles.attributes.type = '';
    fixture.detectChanges();
    const signatureEle = document.getElementsByTagName('nm-signature');
    expect(signatureEle.length === 0).toBeTruthy();
  }));

  it('textarea component should be created if the textarea is configured', async(() => {
    hostComponent.element = formTextArea as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const textAreaEle = debugElement.query(By.css('nm-input-textarea'));
    expect(textAreaEle).toBeTruthy();
  }));

  it('textarea component should not be created if the textarea is not configured', async(() => {
    hostComponent.element = formTextArea as Param;
    hostComponent.element.config.uiStyles.attributes.type = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const textAreaEle = debugElement.query(By.css('nm-input-textarea'));
    expect(textAreaEle).toBeFalsy();
  }));

  it('calendar component should be created if the calendar is configured', async(() => {
    hostComponent.element = formCalendar as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const calendarEle = debugElement.query(By.css('nm-input-calendar'));
    expect(calendarEle).toBeTruthy();
  }));

  it('calendar component should not be created if the calendar is not configured', async(() => {
    hostComponent.element = formCalendar as Param;
    hostComponent.element.config.uiStyles.attributes.type = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const calendarEle = debugElement.query(By.css('nm-input-calendar'));
    expect(calendarEle).toBeFalsy();
  }));

  it('comboBox component should be created if the comboBox is configured', async(() => {
    hostComponent.element = formComboBox as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const comboBoxEle = debugElement.query(By.css('nm-comboBox'));
    expect(comboBoxEle).toBeTruthy();
  }));

  it('comboBox component should not be created if the comboBox is not configured', async(() => {
    hostComponent.element = formComboBox as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const comboBoxEle = debugElement.query(By.css('nm-comboBox'));
    expect(comboBoxEle).toBeFalsy();
  }));

  it('radio component should be created if the radio is configured', async(() => {
    hostComponent.element = formRadio as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const radioEle = debugElement.query(By.css('nm-input-radio'));
    expect(radioEle).toBeTruthy();
  }));

  it('radio component should not be created if the radio is not configured', async(() => {
    hostComponent.element = formRadio as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const radioEle = debugElement.query(By.css('nm-input-radio'));
    expect(radioEle).toBeFalsy();
  }));

  it('checkBoxGroup component should be created if the checkBoxGroup is configured', async(() => {
    hostComponent.element = formCheckBoxGrp as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const checkBoxGroupEle = debugElement.query(By.css('nm-input-checkbox'));
    expect(checkBoxGroupEle).toBeTruthy();
  }));

  it('checkBoxGroup component should not be created if the checkBoxGroup is not configured', async(() => {
    hostComponent.element = formCheckBoxGrp as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const checkBoxGroupEle = debugElement.query(By.css('nm-input-checkbox'));
    expect(checkBoxGroupEle).toBeFalsy();
  }));

  it('checkbox component should be created if the checkbox is configured', async(() => {
    hostComponent.element = formCheckBox as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const checkBoxEle = debugElement.query(By.css('nm-single-checkbox'));
    expect(checkBoxEle).toBeTruthy();
  }));

  it('checkbox component should not be created if the checkbox is not configured', async(() => {
    hostComponent.element = formCheckBox as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const checkBoxEle = debugElement.query(By.css('nm-single-checkbox'));
    expect(checkBoxEle).toBeFalsy();
  }));

  it('nm-multi-select-listbox component should be created if the multiSelect is configured', async(() => {
    hostComponent.element = formMultiSelect as Param;
    hostComponent.element.config.uiStyles.attributes.alias = 'MultiSelect';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const multiSelectListBoxEle = debugElement.query(
      By.css('nm-multi-select-listbox')
    );
    expect(multiSelectListBoxEle).toBeTruthy();
  }));

  it('nm-multi-select-listbox component should not be created if the multiSelect is not configured', async(() => {
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const multiSelectListBoxEle = debugElement.query(
      By.css('nm-multi-select-listbox')
    );
    expect(multiSelectListBoxEle).toBeFalsy();
  }));

  it('nm-multiselect-card component should be created if the multiSelectCard is configured', async(() => {
    hostComponent.element = formMultiSelectCard as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const multiSelectCardEle = debugElement.query(
      By.css('nm-multiselect-card')
    );
    expect(multiSelectCardEle).toBeTruthy();
  }));

  it('nm-multiselect-card component should not be created if the multiSelectCard is not configured', async(() => {
    hostComponent.element = formMultiSelectCard as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const multiSelectCardEle = debugElement.query(
      By.css('nm-multiselect-card')
    );
    expect(multiSelectCardEle).toBeFalsy();
  }));

  it('nm-upload component should be created if the fileUpload is configured', async(() => {
    hostComponent.element = formUpload as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const uploadEle = debugElement.query(By.css('nm-upload'));
    expect(uploadEle).toBeTruthy();
  }));

  it('nm-upload component should not be created if the fileUpload is not configured', async(() => {
    hostComponent.element = formUpload as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const uploadEle = debugElement.query(By.css('nm-upload'));
    expect(uploadEle).toBeFalsy();
  }));

  it('inputSwitch component should be created if the inputSwitch is configured', async(() => {
    hostComponent.element = formInputSwitch as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputSwitchEle = debugElement.query(By.css('nm-input-switch'));
    expect(inputSwitchEle).toBeTruthy();
  }));

  it('inputSwitch component should not be created if the inputSwitch is not configured', async(() => {
    hostComponent.element = formInputSwitch as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputSwitchEle = debugElement.query(By.css('nm-input-switch'));
    expect(inputSwitchEle).toBeFalsy();
  }));

  it('nm-table component should be created if the grid is configured', async(() => {
    hostComponent.element = formTable as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const tableEle = debugElement.query(By.css('nm-table'));
    expect(tableEle).toBeTruthy();
  }));

  it('nm-table component should not be created if the grid is not configured', async(() => {
    hostComponent.element = formTable as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const tableEle = debugElement.query(By.css('nm-table'));
    expect(tableEle).toBeFalsy();
  }));

  it('nm-treegrid component should be created if the treeGrid is configured', async(() => {
    hostComponent.element = formTreeGrid as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const treeGridEle = debugElement.query(By.css('nm-treegrid'));
    expect(treeGridEle).toBeTruthy();
  }));

  it('nm-treegrid component should not be created if the treeGrid is not configured', async(() => {
    hostComponent.element = formTreeGrid as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const treeGridEle = debugElement.query(By.css('nm-treegrid'));
    expect(treeGridEle).toBeFalsy();
  }));

  it('nm-message component should be created if the element.messages is configured', async(() => {
    hostComponent.element = formMultiSelect as Param;
    hostComponent.element.message = [
      {
        messageArray: [
          {
            severity: 'warn',
            summary: 'Warn Message',
            detail: 'Message is set',
            life: 5000,
            styleClass: ''
          }
        ],
        context: 'TOAST',
        type: 'WARNING',
        styleClass: ''
      }
    ];
    hostComponent.element.config.uiStyles.attributes.dataEntryField = true;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmMessageEle = debugElement.query(By.css('nm-message'));
    expect(nmMessageEle).toBeTruthy();
  }));

  it('nm-message component should not be created if the element.messages is not configured', async(() => {
    hostComponent.element = formMultiSelect as Param;
    hostComponent.element.message = [];
    hostComponent.element.config.uiStyles.attributes.dataEntryField = true;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmMessageEle = debugElement.query(By.css('nm-message'));
    expect(nmMessageEle).toBeFalsy();
  }));

  it('pickList component should be created if the picklist is configured', async(() => {
    hostComponent.element = formPicklist as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pickListEle = debugElement.query(By.css('nm-pickList'));
    expect(pickListEle).toBeTruthy();
  }));

  it('pickList component should not be created if the picklist is not configured', async(() => {
    hostComponent.element = formPicklist as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pickListEle = debugElement.query(By.css('nm-pickList'));
    expect(pickListEle).toBeFalsy();
  }));

  it('form element should not be created if the dataentryfield is not configured', async(() => {
    hostComponent.element.config.uiStyles.attributes.dataEntryField = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const divEle = debugElement.query(By.css('div'));
    expect(divEle).toBeFalsy();
  }));

  it('isPristine property should be updated from element', async(() => {
    hostComponent.element.config.code = '{';
    expect(hostComponent.isPristine).toBeTruthy();
  }));

  it('nm-input-rich-text should not be rendered if the RichText element is not configured', async(() => {
    hostComponent.element = formRichText;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('nm-input-rich-text'))).toBeNull();
  }));

  // it('nm-input-rich-text should be rendered if the RichText element is configured', async(() => {
  //   hostComponent.element = formRichText;
  //   fixture.detectChanges();
  //   expect(fixture.debugElement.query(By.css('nm-input-rich-text'))).toBeTruthy();
  // }));
});
