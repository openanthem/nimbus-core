'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule, AbstractControlDirective } from '@angular/forms';
import { GrowlModule, AccordionModule, PickListModule, ListboxModule, CalendarModule, DataTableModule, DropdownModule, FileUploadModule, RadioButtonModule, CheckboxModule } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';

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
import { Accordion } from '../platform/accordion.component';
import { AccordionMain } from '../platform/content/accordion.component';
import { Menu } from '../platform/menu.component';
import { Link } from '../platform/link.component';
import { Form } from '../platform/form.component';
import { StaticText } from '../platform/content/static-content.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { FrmGroupCmp } from './form-group.component';
import { AccordionGroup } from '../platform/accordion-group.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { InPlaceEditorComponent } from '../platform/form/elements/inplace-editor.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';

let fixture, app;

describe('FormElement', () => {
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
        AccordionMain,
        Menu,
        Link,
        Form,
        StaticText,
        CardDetailsComponent,
        CardDetailsGrid,
        FrmGroupCmp,
        AccordionGroup,
        CardDetailsFieldComponent,
        InPlaceEditorComponent,
        DateTimeFormatPipe
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
        KeyFilterModule
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(FormElement);
    app = fixture.debugElement.componentInstance;
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
    app.element = { message: 'test' };
    spyOn(app, 'getErrors').and.returnValue('');
    expect(app.getMessages()).toEqual(['test']);
  }));

  it('getMessages() should return return empty array', async(() => {
    app.element = { message: null };
    spyOn(app, 'getErrors').and.returnValue('');
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
    expect(app.getErrorStyles()).toEqual(undefined);
  }));

  it('getErrorStyles() should return alert string', async(() => {
    app.element = { config: { code: 'testing' } };
    app.form = { controls: { testing: { pristine: false, valid: false } } };
    expect(app.getErrorStyles()).toEqual('alert alert-danger');
  }));

  it('elementCss should be updated with even suffix', async(() => {
    app.elementCss = 'test';
    app.element = { config: { uiStyles: { attributes: { controlId: 10 } } } };
    app.ngOnInit();
    expect(app.elementCss).toEqual('test even');
  }));

  it('elementCss should be undefined', async(() => {
    app.element = { config: { uiStyles: { attributes: { controlId: null } } } };
    app.ngOnInit();
    expect(app.elementCss).toEqual(undefined);
  }));

  it('elementCss should be updated with odd suffix', async(() => {
    app.elementCss = 'test';
    app.element = { config: { uiStyles: { attributes: { controlId: 19 } } } };
    app.ngOnInit();
    expect(app.elementCss).toEqual('test odd');
  }));

  it('getElementStyle() should return col-lg-12 col-md-6', async(() => {
    app.element = { config: { uiStyles: { attributes: { alias: 'MultiSelectCard' } } } };
    expect(app.getElementStyle()).toEqual('col-lg-12 col-md-6');
  }));

  it('getElementStyle() should return empty array', async(() => {
    app.element = { config: { uiStyles: { attributes: { alias: '' } } } };
    expect(app.getElementStyle()).toEqual('');
  }));

  it('getErrors() should call addErrorMessages() with attribute from element', async(() => {
    app.element = { config: { validation: { constraints: [{ name: 'NotNull', attribute: { message: 'testing...' } }] }, code: 'test' } };
    app.form = { controls: { test: { invalid: true, errors: { required: true } } } };
    spyOn(app, 'addErrorMessages').and.returnValue('');
    app.getErrors();
    expect(app.addErrorMessages).toHaveBeenCalled();
    expect(app.addErrorMessages).toHaveBeenCalledWith('testing...');
  }));

  it('getErrors() should call addErrorMessages() with Field is required string', async(() => {
    app.element = { config: { validation: { constraints: [{ name: 'NotNull', attribute: { message: '' } }] }, code: 'test' } };
    app.form = { controls: { test: { invalid: true, errors: { required: true } } } };
    spyOn(app, 'addErrorMessages').and.returnValue('');
    app.getErrors();
    expect(app.addErrorMessages).toHaveBeenCalled();
    expect(app.addErrorMessages).toHaveBeenCalledWith('Field is required.');
  }));

  it('getErrors() should call addErrorMessages() with attribute from element on constraints.name as pattern', async(() => {
    app.element = { config: { validation: { constraints: [{ name: 'Pattern', attribute: { message: 'testing pattern' } }] }, code: 'test' } };
    app.form = { controls: { test: { invalid: true, errors: { pattern: true } } } };
    spyOn(app, 'addErrorMessages').and.returnValue('');
    app.getErrors();
    expect(app.addErrorMessages).toHaveBeenCalled();
    expect(app.addErrorMessages).toHaveBeenCalledWith('testing pattern');
  }));

  it('getErrors() should call addErrorMessages() with Field is required string on constraints.name as pattern', async(() => {
    app.element = { config: { validation: { constraints: [{ name: 'Pattern', attribute: { message: '' } }] }, code: 'test' } };
    app.form = { controls: { test: { invalid: true, errors: { pattern: true } } } };
    spyOn(app, 'addErrorMessages').and.returnValue('');
    app.getErrors();
    expect(app.addErrorMessages).toHaveBeenCalled();
    expect(app.addErrorMessages).toHaveBeenCalledWith('Field is required.');
  }));

  it('getErrors() should call addErrorMessages() with attribute from element on constraints.name as Size', async(() => {
    app.element = { config: { validation: { constraints: [{ name: 'Size', attribute: { message: 'testing Size' } }] }, code: 'test' } };
    app.form = { controls: { test: { invalid: true, errors: { minMaxSelection: true } } } };
    spyOn(app, 'addErrorMessages').and.returnValue('');
    app.getErrors();
    expect(app.addErrorMessages).toHaveBeenCalled();
    expect(app.addErrorMessages).toHaveBeenCalledWith('testing Size');
  }));

  it('getErrors() should call addErrorMessages() with Field is required string on constraints.name as Size', async(() => {
    app.element = { config: { validation: { constraints: [{ name: 'Size', attribute: { message: '' } }] }, code: 'test' } };
    app.form = { controls: { test: { invalid: true, errors: { minMaxSelection: true } } } };
    spyOn(app, 'addErrorMessages').and.returnValue('');
    app.getErrors();
    expect(app.addErrorMessages).toHaveBeenCalled();
    expect(app.addErrorMessages).toHaveBeenCalledWith('Field is required.');
  }));

  it('getErrors() should call addErrorMessages() with Value must be a number.', async(() => {
    app.element = { config: { validation: { constraints: [{ attribute: { message: '' } }] }, code: 'test' } };
    app.form = { controls: { test: { invalid: true, errors: { isNumber: true } } } };
    spyOn(app, 'addErrorMessages').and.returnValue('');
    app.getErrors();
    expect(app.addErrorMessages).toHaveBeenCalled();
    expect(app.addErrorMessages).toHaveBeenCalledWith('Value must be a number.');
  }));

  it('getErrors() should call addErrorMessages()', async(() => {
    app.element = { config: { code: 'test' } };
    app.form = { controls: { test: { invalid: false, errors: { isNumber: true } } } };
    spyOn(app, 'addErrorMessages').and.returnValue('');
    app.getErrors();
    expect(app.addErrorMessages).not.toHaveBeenCalled();
  }));

  it('getErrors() should not call addErrorMessages()', async(() => {
    app.element = { config: { code: 'test', validation: '' } };
    app.form = { controls: { test: { invalid: true, errors: { isNumber: true } } } };
    spyOn(app, 'addErrorMessages').and.returnValue('');
    app.getErrors();
    expect(app.addErrorMessages).not.toHaveBeenCalled();
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