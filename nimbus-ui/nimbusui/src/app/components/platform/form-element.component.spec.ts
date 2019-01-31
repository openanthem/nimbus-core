import { ChartModule } from 'primeng/chart';
import { NmChart } from './charts/chart.component';
import { RichText } from './form/elements/rich-text.component';
import { TableHeader } from './grid/table-header.component';
'use strict';
import { FormsModule, ReactiveFormsModule, AbstractControlDirective, Validators, ValidatorFn, FormGroup, FormControl, NG_VALUE_ACCESSOR, NgModel } from '@angular/forms';
import { GrowlModule, AccordionModule, PickListModule, ListboxModule, CalendarModule, 
    DataTableModule, DropdownModule, FileUploadModule, RadioButtonModule, CheckboxModule,
    InputSwitchModule, TreeTableModule, EditorModule } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {ToastModule} from 'primeng/toast';
import { HttpClientModule } from '@angular/common/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { HttpModule } from '@angular/http';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';
import { MessageService } from 'primeng/api';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren, forwardRef, NO_ERRORS_SCHEMA, ChangeDetectorRef } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing'

import { SessionStoreService, CUSTOM_STORAGE } from '../../services/session.store';
import { FormElement } from './form-element.component';
import { MessageComponent } from '../platform/message/message.component';
import { DataTable } from './grid/table.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { CheckBoxGroup } from '../platform/form/elements/checkbox-group.component';
import { CheckBox } from '../platform/form/elements/checkbox.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { Calendar } from '../platform/form/elements/calendar.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { Signature } from '../platform/form/elements/signature.component'
import { InputText } from '../platform/form/elements/textbox.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { Header } from '../platform/content/header.component';
import { Section } from '../platform/section.component';
import { ActionLink } from '../platform/form/elements/action-dropdown.component';
import { ActionDropdown } from '../platform/form/elements/action-dropdown.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { Accordion } from '../platform/content/accordion.component';
import { Menu } from '../platform/menu.component';
import { Link } from '../platform/link.component';
import { Form } from '../platform/form.component';
import { StaticText } from '../platform/content/static-content.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { FrmGroupCmp } from './form-group.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { InPlaceEditorComponent } from '../platform/form/elements/inplace-editor.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { HeaderCheckBox } from '../platform/form/elements/header-checkbox.component';
import { SvgComponent } from './svg/svg.component';
import { Image } from './image.component';
import { InputSwitch } from './form/elements/input-switch.component';
import { FormGridFiller } from './form/form-grid-filler.component';
import { DisplayValueDirective } from '../../directives/display-value.directive';
import { InputLabel } from './form/elements/input-label.component';
import { Label } from './content/label.component';
import { CardDetailsFieldGroupComponent } from './card/card-details-field-group.component';
import { WebContentSvc } from '../../services/content-management.service';
import { InputLegend } from './form/elements/input-legend.component';
import { Param } from '../../shared/param-state';
import { By } from '@angular/platform-browser';
import { PageService } from '../../services/page.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { LoggerService } from '../../services/logger.service';
import { AppInitService } from '../../services/app.init.service'
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite, createStableTestContext, createTestContext } from 'ng-bullet';
import * as data from '../../payload.json';
import { FormErrorMessage } from './form-error-message.component';
import { Message } from '../../shared/message';
import { PrintDirective } from '../../directives/print.directive';
import { async, TestBed, ComponentFixture } from '@angular/core/testing';
import { FileService } from '../../services/file.service';
import { GridService } from '../../services/grid.service';
import { PrintService } from '../../services/print.service';
import { GridUtils } from '../../shared/grid-utils';
import { formInput, formPicklist, formTreeGrid, formTable, formInputSwitch, formUpload, formCheckBoxGrp, formRadio, formCalendar, formSignature, formTextArea, formComboBox, formCheckBox, formMultiSelectCard, formMultiSelect, formRichText } from 'mockdata'

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
      }]
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
  debug() { }
  info() { }
  error() { }
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
    {provide: LoggerService, useClass: MockLoggerService},
    Location,
    WebContentSvc,
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
    { 
        provide: NG_VALUE_ACCESSOR,
        multi: true,
        useExisting: forwardRef(() => TreeGrid),
      }
   ];


describe('FormElement', () => {

  configureTestSuite(() => {
    TestBed.configureTestingModule({
      declarations: declarations,
      providers: providers,
      imports:  imports,
      schemas:      [ NO_ERRORS_SCHEMA ]
      })
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

  it('two way binding',  () => {
    fixture.whenStable().then(() => {
      hostComponent.elementCss = '';
      hostComponent.getComponentClass();
      fixture.detectChanges();
      let textBox;
      textBox = fixture.debugElement.query(By.css('.form-control.text-input')).nativeElement;
      textBox.value = 'abcd123';
      textBox.dispatchEvent(new Event('input'));
      textBox.dispatchEvent(new Event('focusout'));
      fixture.detectChanges();
      expect(hostComponent.form.controls[param.config.code].value).toEqual('abcd123');
    });
  });

  it('getErrorStyles() should return alert string', () => {
    fixture.whenStable().then(() => {
      hostComponent.elementCss = '';
      hostComponent.getComponentClass();
      fixture.detectChanges();
      let textBox;
      textBox = fixture.debugElement.query(By.css('.form-control.text-input')).nativeElement;
      textBox.value = null;
      textBox.dispatchEvent(new Event('input'));
      fixture.detectChanges();
      expect(hostComponent.getErrorStyles()).toEqual('alert alert-danger');
    });
  });

  it('should create the FormElement',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('isValid property should be updated from form.controls',  async(() => {
    hostComponent.elementCss = '';
    hostComponent.getComponentClass();
    hostComponent.form.controls[hostComponent.element.config.code].setValue('test');
    fixture.detectChanges();
      expect(hostComponent.isValid).toBeTruthy();
  }));

  it('isValid property should not be updated from form.controls',  async(() => {
    hostComponent.form.controls[hostComponent.element.config.code] = null;
    expect(hostComponent.isValid).toBeTruthy();
  }));

  it('isPristine property should be updated from form.controls',  async(() => {
    hostComponent.form.controls[hostComponent.element.config.code].setValue('test');
    expect(hostComponent.isPristine).toEqual(true);
  }));

  it('isPristine property should be updated from element even if it returns false',  async(() => {
    hostComponent.form.controls[hostComponent.element.config.code].setValue(null);
    expect(hostComponent.isPristine).toEqual(true);
  }));

  it('getMessages() should return message from the element',  async(() => {
    const message = new Message();
    message.text = 'test'
    hostComponent.element.message = [message];
    expect(hostComponent.getMessages()).toEqual(hostComponent.element.message);
  }));

  it('getMessages() should return return empty array',  async(() => {
    hostComponent.element.message = [];
    expect(hostComponent.getMessages()).toEqual([]);
  }));

  it('showMessages should be updated based on the elemMessages',  async(() => {
    const message = new Message();
    message.text = 't'
    hostComponent.elemMessages = [message];
    expect(hostComponent.showMessages).toEqual(true);
  }));

  it('getErrorStyles() should return empty string',  async(() => {
    expect(hostComponent.getErrorStyles()).toEqual('');
  }));

  it('elementCss should be undefined', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.controlId = null;
      expect(hostComponent.elementCss).toEqual(undefined);
    });
  });

  it('getElementStyle() should return col-lg-12 col-md-6',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias = 'MultiSelectCard';
      expect(hostComponent.getElementStyle()).toEqual('col-lg-12 col-md-6');
    });
  });

  it('getElementStyle() should return empty array',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias = '';
      expect(hostComponent.getElementStyle()).toEqual('');
    });
  });

  it('addErrorMessages() should update the elemMessages[0].messageArray[0].detail',  async(() => {
    hostComponent.elemMessages = [];
    hostComponent.addErrorMessages('testing error message');
    expect(hostComponent.elemMessages[0].messageArray[0].detail).toEqual('testing error message');
  }));

  it('ngModelState() should return string with touched',  async(() => {
    let ngm: any;
    const res = hostComponent.ngModelState(ngm);
    expect(res.indexOf('touched')).toEqual(0);
  }));

  it('form element should be created if the dataentryfield is configured',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const divEle = debugElement.query(By.css('div'));
    expect(divEle).toBeTruthy();
  }));

  it('nm-input should be created if the text is configured',async(() => {
    hostComponent.element = formInput as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeTruthy();
  }));

  it('nm-input should not be created if the text is not configured',async(() => {
    hostComponent.element = formInput as Param;
    hostComponent.element.config.uiStyles.attributes.type = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeFalsy();
  }));

  it('signature component should be created if the signature is configured',async(() => {
    hostComponent.element = formSignature as Param;
    fixture.detectChanges();
    const signatureEle = document.getElementsByTagName('nm-signature');
    expect(signatureEle.length > 0).toBeTruthy();
  }));

  it('signature component should not be created if the signature is not configured',async(() => {
    hostComponent.element = formSignature as Param;
    hostComponent.element.config.uiStyles.attributes.type = '';
    fixture.detectChanges()
    const signatureEle = document.getElementsByTagName('nm-signature');
    expect(signatureEle.length === 0 ).toBeTruthy();
  }));

  it('textarea component should be created if the textarea is configured',async(() => {
    hostComponent.element = formTextArea as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const textAreaEle = debugElement.query(By.css('nm-input-textarea'));
    expect(textAreaEle).toBeTruthy();
  }));

  it('textarea component should not be created if the textarea is not configured',async(() => {
    hostComponent.element = formTextArea as Param;
    hostComponent.element.config.uiStyles.attributes.type = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const textAreaEle = debugElement.query(By.css('nm-input-textarea'));
    expect(textAreaEle).toBeFalsy();
  }));

  it('calendar component should be created if the calendar is configured',async(() => {
    hostComponent.element = formCalendar as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const calendarEle = debugElement.query(By.css('nm-input-calendar'));
    expect(calendarEle).toBeTruthy();
  }));

  it('calendar component should not be created if the calendar is not configured',async(() => {
    hostComponent.element = formCalendar as Param;
    hostComponent.element.config.uiStyles.attributes.type = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const calendarEle = debugElement.query(By.css('nm-input-calendar'));
    expect(calendarEle).toBeFalsy();
  }));

  it('comboBox component should be created if the comboBox is configured',async(() => {
    hostComponent.element = formComboBox as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const comboBoxEle = debugElement.query(By.css('nm-comboBox'));
    expect(comboBoxEle).toBeTruthy();
  }));

  it('comboBox component should not be created if the comboBox is not configured',async(() => {
    hostComponent.element = formComboBox as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const comboBoxEle = debugElement.query(By.css('nm-comboBox'));
    expect(comboBoxEle).toBeFalsy();
  }));

  it('radio component should be created if the radio is configured',async(() => {
    hostComponent.element = formRadio as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const radioEle = debugElement.query(By.css('nm-input-radio'));
    expect(radioEle).toBeTruthy();
  }));

  it('radio component should not be created if the radio is not configured',async(() => {
    hostComponent.element = formRadio as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const radioEle = debugElement.query(By.css('nm-input-radio'));
    expect(radioEle).toBeFalsy();
  }));

  it('checkBoxGroup component should be created if the checkBoxGroup is configured',async(() => {
    hostComponent.element = formCheckBoxGrp as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const checkBoxGroupEle = debugElement.query(By.css('nm-input-checkbox'));
    expect(checkBoxGroupEle).toBeTruthy();
  }));

  it('checkBoxGroup component should not be created if the checkBoxGroup is not configured',async(() => {
    hostComponent.element = formCheckBoxGrp as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const checkBoxGroupEle = debugElement.query(By.css('nm-input-checkbox'));
    expect(checkBoxGroupEle).toBeFalsy();
  }));

  it('checkbox component should be created if the checkbox is configured',async(() => {
    hostComponent.element = formCheckBox as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const checkBoxEle = debugElement.query(By.css('nm-single-checkbox'));
    expect(checkBoxEle).toBeTruthy();
  }));

  it('checkbox component should not be created if the checkbox is not configured',async(() => {
    hostComponent.element = formCheckBox as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const checkBoxEle = debugElement.query(By.css('nm-single-checkbox'));
    expect(checkBoxEle).toBeFalsy();
  }));

  it('nm-multi-select-listbox component should be created if the multiSelect is configured',async(() => {
    hostComponent.element = formMultiSelect as Param;
    hostComponent.element.config.uiStyles.attributes.alias = 'MultiSelect';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const multiSelectListBoxEle = debugElement.query(By.css('nm-multi-select-listbox'));
    expect(multiSelectListBoxEle).toBeTruthy();
  }));

  it('nm-multi-select-listbox component should not be created if the multiSelect is not configured',async(() => {
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const multiSelectListBoxEle = debugElement.query(By.css('nm-multi-select-listbox'));
    expect(multiSelectListBoxEle).toBeFalsy();
  }));

  it('nm-multiselect-card component should be created if the multiSelectCard is configured',async(() => {
    hostComponent.element = formMultiSelectCard as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const multiSelectCardEle = debugElement.query(By.css('nm-multiselect-card'));
    expect(multiSelectCardEle).toBeTruthy();
  }));

  it('nm-multiselect-card component should not be created if the multiSelectCard is not configured',async(() => {
    hostComponent.element = formMultiSelectCard as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const multiSelectCardEle = debugElement.query(By.css('nm-multiselect-card'));
    expect(multiSelectCardEle).toBeFalsy();
  }));

  it('nm-upload component should be created if the fileUpload is configured',async(() => {
    hostComponent.element = formUpload as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const uploadEle = debugElement.query(By.css('nm-upload'));
    expect(uploadEle).toBeTruthy();
  }));

  it('nm-upload component should not be created if the fileUpload is not configured',async(() => {
    hostComponent.element = formUpload as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const uploadEle = debugElement.query(By.css('nm-upload'));
    expect(uploadEle).toBeFalsy();
  }));

  it('inputSwitch component should be created if the inputSwitch is configured',async(() => {
    hostComponent.element = formInputSwitch as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const inputSwitchEle = debugElement.query(By.css('nm-input-switch'));
    expect(inputSwitchEle).toBeTruthy();
  }));

  it('inputSwitch component should not be created if the inputSwitch is not configured',async(() => {
    hostComponent.element = formInputSwitch as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const inputSwitchEle = debugElement.query(By.css('nm-input-switch'));
    expect(inputSwitchEle).toBeFalsy();
  }));

  it('nm-table component should be created if the grid is configured',async(() => {
    hostComponent.element = formTable as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const tableEle = debugElement.query(By.css('nm-table'));
    expect(tableEle).toBeTruthy();
  }));

  it('nm-table component should not be created if the grid is not configured',async(() => {
    hostComponent.element = formTable as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const tableEle = debugElement.query(By.css('nm-table'));
    expect(tableEle).toBeFalsy();
  }));

  it('nm-treegrid component should be created if the treeGrid is configured',async(() => {
    hostComponent.element = formTreeGrid as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const treeGridEle = debugElement.query(By.css('nm-treegrid'));
    expect(treeGridEle).toBeTruthy();
  }));

  it('nm-treegrid component should not be created if the treeGrid is not configured',async(() => {
    hostComponent.element = formTreeGrid as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const treeGridEle = debugElement.query(By.css('nm-treegrid'));
    expect(treeGridEle).toBeFalsy();
  }));

  it('nm-message component should be created if the element.messages is configured',async(() => {
    hostComponent.element = formMultiSelect as Param;
    hostComponent.element.message = [{"messageArray":[{"severity":"warn","summary":"Warn Message","detail":"Message is set","life":5000,"styleClass":""}],"context":"TOAST","type":"WARNING","styleClass":""}];
    hostComponent.element.config.uiStyles.attributes.dataEntryField = true;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const nmMessageEle = debugElement.query(By.css('nm-message'));
    expect(nmMessageEle).toBeTruthy();
  }));

  it('nm-message component should not be created if the element.messages is not configured',async(() => {
    hostComponent.element = formMultiSelect as Param;
    hostComponent.element.message = [];
    hostComponent.element.config.uiStyles.attributes.dataEntryField = true;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const nmMessageEle = debugElement.query(By.css('nm-message'));
    expect(nmMessageEle).toBeFalsy();
  }));

  it('pickList component should be created if the picklist is configured',async(() => {
    hostComponent.element = formPicklist as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const pickListEle = debugElement.query(By.css('nm-pickList'));
    expect(pickListEle).toBeTruthy();
  }));

  it('pickList component should not be created if the picklist is not configured',async(() => {
    hostComponent.element = formPicklist as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const pickListEle = debugElement.query(By.css('nm-pickList'));
    expect(pickListEle).toBeFalsy();
  }));

  it('form element should not be created if the dataentryfield is not configured',async(() => {
    hostComponent.element.config.uiStyles.attributes.dataEntryField = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;  
    const divEle = debugElement.query(By.css('div'));
    expect(divEle).toBeFalsy();
  }));

  it('isPristine property should be updated from element',  async(() => {
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
