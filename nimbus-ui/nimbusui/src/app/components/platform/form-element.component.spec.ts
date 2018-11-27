'use strict';
import { TestBed, async } from '@angular/core/testing';
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

let fixture, app, param: Param, payload;

describe('FormElement', () => {
  payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';
  param = JSON.parse(payload);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
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
        InputLegend
       ],
       imports: [
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
        HttpModule
       ],
       providers: [
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
        AppInitService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(FormElement);
    app = fixture.debugElement.componentInstance;
    const fg = new FormGroup({});
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    fg.addControl(param.config.code, new FormControl(param.leafState,checks));
    app.form = fg;
    app.element = param;
  }));

  it('two way binding', async(() => {
      app.elementCss = '';
      app.getComponentClass();
      fixture.detectChanges();
      let textBox;
      textBox = fixture.debugElement.query(By.css('.form-control')).nativeElement;
      textBox.value = 'abcd123';
      textBox.dispatchEvent(new Event('input'));
      textBox.dispatchEvent(new Event('focusout'));
      fixture.detectChanges();      
      expect(app.form.controls[param.config.code].value).toEqual('abcd123');
      app.form.controls[param.config.code].setValue('testtt');
      fixture.detectChanges();
      setTimeout(() => {
        expect(textBox.value).toEqual('testtt');  
      }, 100);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('isValid property should be updated from form.controls', async(() => {
    app.element = { config: { code: 123 } };
    app.form = { controls: { 123: { valid: 'test' } } };
    expect(app.isValid).toEqual('test');
  }));

  it('isValid property should not be updated from form.controls', async(() => {
    app.element = { config: { code: 123 } };
    app.form = { controls: { 123: null } };
    expect(app.isValid).toBeTruthy();
  }));

  it('isPristine property should be updated from element', async(() => {
    app.element = { config: { code: { startsWith: () => {
            return true;
          } } } };
    expect(app.isPristine).toBeTruthy();
  }));

  it('isPristine property should be updated from form.controls', async(() => {
    app.element = { config: { code: 'testing' } };
    app.form = { controls: { testing: { pristine: true } } };
    expect(app.isPristine).toEqual(true);
  }));

  it('isPristine property should be updated from element even if it returns false', async(() => {
    app.element = { config: { code: { startsWith: () => {
            return false;
          } } } };
    app.form = { controls: {} };
    expect(app.isPristine).toBeTruthy();
  }));

  it('getMessages() should return message from the element', async(() => {
    app.element = { message: ['test'] };
    spyOn(app, 'getPristine').and.returnValue(true);
    expect(app.getMessages()).toEqual(['test']);
  }));

  it('getMessages() should return return empty array', async(() => {
    app.element = { message: [] };
    spyOn(app, 'getPristine').and.returnValue(true);
    expect(app.getMessages()).toEqual([]);
  }));

  it('showMessages should be updated based on the elemMessages', async(() => {
    app.elemMessages = [1];
    expect(app.showMessages).toEqual(true);
  }));

  it('getErrorStyles() should return undefined', async(() => {
    app.element = { config: { code: { startsWith: () => {
            return false;
          } } } };
    app.form = { controls: {} };
    expect(app.getErrorStyles()).toEqual('');
  }));

  it('getErrorStyles() should return alert string', async(() => {
    app.element = { config: { code: 'testing' } };
    app.form = { controls: { testing: { pristine: false, valid: false } } };
    expect(app.getErrorStyles()).toEqual('alert alert-danger');
  }));

  it('elementCss should be undefined', async(() => {
    app.element = { config: { uiStyles: { attributes: { controlId: null } } } };
    app.ngOnInit();
    expect(app.elementCss).toEqual(undefined);
  }));

  it('getElementStyle() should return col-lg-12 col-md-6', async(() => {
    app.element = { config: { uiStyles: { attributes: { alias: 'MultiSelectCard' } } } };
    expect(app.getElementStyle()).toEqual('col-lg-12 col-md-6');
  }));

  it('getElementStyle() should return empty array', async(() => {
    app.element = { config: { uiStyles: { attributes: { alias: '' } } } };
    expect(app.getElementStyle()).toEqual('');
  }));

  it('addErrorMessages() should update the elemMessages[0].messageArray[0].detail', async(() => {
    app.elemMessages = [];
    app.addErrorMessages('testing error message');
    expect(app.elemMessages[0].messageArray[0].detail).toEqual('testing error message');
  }));

  it('ngModelState() should return string with touched', async(() => {
    const res = app.ngModelState();
    expect(res.indexOf('touched')).toEqual(0);
  }));

});