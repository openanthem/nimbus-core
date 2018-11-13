'use strict';
import { FormsModule, ReactiveFormsModule, AbstractControlDirective, Validators, ValidatorFn, FormGroup, FormControl } from '@angular/forms';
import { GrowlModule, AccordionModule, PickListModule, ListboxModule, CalendarModule, 
    DataTableModule, DropdownModule, FileUploadModule, RadioButtonModule, CheckboxModule,
    InputSwitchModule, TreeTableModule } from 'primeng/primeng';
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
import { DateControl } from '../platform/form/elements/date.component';
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
import { Button } from '../platform/form/elements/button.component';
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
import { TreeGrid } from './tree-grid/tree-grid.component';
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
import { configureTestSuite } from 'ng-bullet';
import * as data from '../../payload.json';
import { FormErrorMessage } from './form-error-message.component';
import { Message } from '../../shared/message';

let param: Param;

const declarations = [
    FormElement,
    MessageComponent,
    DataTable,
    FileUploadComponent,
    OrderablePickList,
    MultiSelectListBox,
    MultiselectCard,
    CheckBox,
    CheckBoxGroup,
    RadioButton,
    ComboBox,
    Calendar,
    DateControl,
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
    FormErrorMessage
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
    BrowserAnimationsModule
  ];
  const providers = [
    { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
    { provide: 'JSNLOG', useValue: JL },
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    Location,
    WebContentSvc,
    PageService,
    CustomHttpClient,
    SessionStoreService,
    LoaderService,
    ConfigService,
    LoggerService,
    AppInitService,
    MessageService
   ];


describe('FormElement', () => {

    configureTestSuite();
    setup(FormElement, declarations, imports, providers);
    param = (<any>data).payload;
  
    beforeEach(async function(this: TestContext<FormElement>){
        const fg = new FormGroup({});
        const checks: ValidatorFn[] = [];
        checks.push(Validators.required);
        fg.addControl(param.config.code, new FormControl(param.leafState,checks));
        this.hostComponent.form = fg;
        this.hostComponent.element = param;
    });

  it('two way binding', function (this: TestContext<FormElement>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.elementCss = '';
      this.hostComponent.getComponentClass();
      this.fixture.detectChanges();
      let textBox;
      textBox = this.fixture.debugElement.query(By.css('.form-control.text-input')).nativeElement;
      textBox.value = 'abcd123';
      textBox.dispatchEvent(new Event('input'));
      textBox.dispatchEvent(new Event('focusout'));
      this.fixture.detectChanges();
      expect(this.hostComponent.form.controls[param.config.code].value).toEqual('abcd123');
      this.hostComponent.form.controls[param.config.code].setValue('testtt');
      this.fixture.detectChanges();
      expect(textBox.value).toEqual('testtt');
    })
  });

  it('getErrorStyles() should return alert string', async function (this: TestContext<FormElement>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.elementCss = '';
      this.hostComponent.getComponentClass();
      this.fixture.detectChanges();
      let textBox;
      textBox = this.fixture.debugElement.query(By.css('.form-control.text-input')).nativeElement;
      textBox.value = null;
      textBox.dispatchEvent(new Event('input'));
      this.fixture.detectChanges();
      expect(this.hostComponent.getErrorStyles()).toEqual('alert alert-danger');
    })
  });

  it('should create the FormElement', function (this: TestContext<FormElement>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('isValid property should be updated from form.controls', function (this: TestContext<FormElement>) {
    this.hostComponent.elementCss = '';
    this.hostComponent.getComponentClass();
    this.hostComponent.form.controls[this.hostComponent.element.config.code].setValue('test');
    this.fixture.detectChanges();
    this.fixture.whenStable().then(() => {
      expect(this.hostComponent.isValid).toBeTruthy();
    })
  });

  it('isValid property should not be updated from form.controls', function (this: TestContext<FormElement>) {
    this.hostComponent.form.controls[this.hostComponent.element.config.code] = null;
    expect(this.hostComponent.isValid).toBeTruthy();
  });

  it('isPristine property should be updated from element', function (this: TestContext<FormElement>) {
    this.hostComponent.element.config.code = '{';
    expect(this.hostComponent.isPristine).toBeTruthy();
  });

  it('isPristine property should be updated from form.controls', function (this: TestContext<FormElement>) {
    this.hostComponent.form.controls[this.hostComponent.element.config.code].setValue('test');
    expect(this.hostComponent.isPristine).toEqual(true);
  });

  it('isPristine property should be updated from element even if it returns false', function (this: TestContext<FormElement>) {
    this.hostComponent.form.controls[this.hostComponent.element.config.code].setValue(null);
    expect(this.hostComponent.isPristine).toEqual(true);
  });

  it('getMessages() should return message from the element', function (this: TestContext<FormElement>) {
    const message = new Message();
    message.text = 'test'
    this.hostComponent.element.message = [message];
    expect(this.hostComponent.getMessages()).toEqual(this.hostComponent.element.message);
  });

  it('getMessages() should return return empty array', function (this: TestContext<FormElement>) {
    this.hostComponent.element.message = [];
    expect(this.hostComponent.getMessages()).toEqual([]);
  });

  it('showMessages should be updated based on the elemMessages', function (this: TestContext<FormElement>) {
    const message = new Message();
    message.text = 't'
    this.hostComponent.elemMessages = [message];
    expect(this.hostComponent.showMessages).toEqual(true);
  });

  it('getErrorStyles() should return empty string', function (this: TestContext<FormElement>) {
    expect(this.hostComponent.getErrorStyles()).toEqual('');
  });

  it('elementCss should be undefined', function (this: TestContext<FormElement>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.controlId = null;
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.elementCss).toEqual(undefined);
    });
  });

  it('getElementStyle() should return col-lg-12 col-md-6', function (this: TestContext<FormElement>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.alias = 'MultiSelectCard';
      expect(this.hostComponent.getElementStyle()).toEqual('col-lg-12 col-md-6');
    });
  });

  it('getElementStyle() should return empty array', function (this: TestContext<FormElement>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.alias = '';
      expect(this.hostComponent.getElementStyle()).toEqual('');
    });
  });

  it('addErrorMessages() should update the elemMessages[0].messageArray[0].detail', function (this: TestContext<FormElement>) {
    this.hostComponent.elemMessages = [];
    this.hostComponent.addErrorMessages('testing error message');
    expect(this.hostComponent.elemMessages[0].messageArray[0].detail).toEqual('testing error message');
  });

  it('ngModelState() should return string with touched', function (this: TestContext<FormElement>) {
    let ngm: any;
    const res = this.hostComponent.ngModelState(ngm);
    expect(res.indexOf('touched')).toEqual(0);
  });

});