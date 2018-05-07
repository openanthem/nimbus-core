'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { CardDetailsFieldComponent } from './card-details-field.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { InputText } from '../form/elements/textbox.component';
import { TextArea } from '../form/elements/textarea.component';
import { ComboBox } from '../form/elements/combobox.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { CustomHttpClient } from '../../../services/httpclient.service';

let fixture, app;

describe('CardDetailsFieldComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        CardDetailsFieldComponent,
        InPlaceEditorComponent,
        InputText,
        TextArea,
        ComboBox,
        DateTimeFormatPipe,
        TooltipComponent,
        SelectItemPipe
      ],
      imports: [FormsModule, DropdownModule, HttpClientModule, HttpModule],
      providers: [CustomHttpClient]
    }).compileComponents();
    fixture = TestBed.createComponent(CardDetailsFieldComponent);
    app = fixture.debugElement.componentInstance;
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('ngOnInit() should update fieldClass property for cols:6', async(() => {
    app.element = { config: { uiStyles: { attributes: { cols: '6' } } } };
    app.ngOnInit();
    expect(app.fieldClass).toEqual('col-sm-2');
  }));

  it('ngOnInit() should update fieldClass property for cols:4', async(() => {
    app.element = { config: { uiStyles: { attributes: { cols: '4' } } } };
    app.ngOnInit();
    expect(app.fieldClass).toEqual('col-sm-3');
  }));

  it('ngOnInit() should update fieldClass property for cols:3', async(() => {
    app.element = { config: { uiStyles: { attributes: { cols: '3' } } } };
    app.ngOnInit();
    expect(app.fieldClass).toEqual('col-sm-4');
  }));

  it('ngOnInit() should update fieldClass property for cols:2', async(() => {
    app.element = { config: { uiStyles: { attributes: { cols: '2' } } } };
    app.ngOnInit();
    expect(app.fieldClass).toEqual('col-sm-6');
  }));

  it('ngOnInit() should update fieldClass property for cols:1', async(() => {
    app.element = { config: { uiStyles: { attributes: { cols: '1' } } } };
    app.ngOnInit();
    expect(app.fieldClass).toEqual('col-sm-12');
  }));

  it('ngOnInit() should update fieldClass property for cols:null', async(() => {
    spyOn(app, 'setIconClass').and.callThrough();
    app.element = { config: { uiStyles: { attributes: { cols: '' } } } };
    app.ngOnInit();
    expect(app.fieldClass).toEqual('col-sm-3');
    expect(app.setIconClass).toHaveBeenCalled();
  }));

  it('setIconClass() should update iconClass property for  iconField: test', async(() => {
    app.element = { config: { uiStyles: { attributes: { iconField: 'test' } } } };
    app.setIconClass();
    expect(app.iconClass).toEqual('test');
  }));

  it('setIconClass() should not update iconClass property for  iconField:null', async(() => {
    app.element = { config: { uiStyles: { attributes: { iconField: '' } } } };
    app.setIconClass();
    expect(app.iconClass).toBeFalsy();
  }));

  it('value property should be updated with element.leafstate', async(() => {
    app.element = { leafState: 'test', values: [] };
    app.value = '';
    expect(app.value).toEqual('test');
  }));

  it('value property should be updated with element.values.label', async(() => {
    app.element = { leafState: 'test', values: [{ code: 'test', label: 'tLabel' }] };
    app.value = '';
    expect(app.value).toEqual('tLabel');
  }));

  it('value property should be updated with element.leafstate based on code', async(() => {
    app.element = { leafState: 'test', values: [{ code: 'test1', label: 'tLabel' }] };
    app.value = '';
    expect(app.value).toEqual('test');
  }));

  it('registerOnChange() should update the onChange property', async(() => {
    const test = () => {
      return true;
    };
    app.registerOnChange(test);
    expect(app.onChange).toEqual(test);
  }));

  it('writeValue() shouls call asdasd', async(() => {
    app.element = { leafState: 'test', values: [] };
    spyOn(app, 'onChange').and.callThrough();
    app.writeValue(123);
    app.writeValue();
    expect(app.onChange).toHaveBeenCalled();
  }));

  it('registerOnTouched() should update the onTouched property', async(() => {
    const test = () => {
      return true;
    };
    app.registerOnTouched(test);
    expect(app.onTouched).toEqual(test);
  }));
});
