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
import { ValueStylesDirective } from '../../../directives/value-styles.directive';
import { InputLabel } from '../../platform/form/elements/input-label.component';

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
        SelectItemPipe,
        ValueStylesDirective,
        InputLabel
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
      expect(app.fieldClass).toEqual('col-sm-3');
    }));

    it('ngOnInit() should update fieldClass property for cols:4', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '4' } } } };
      app.ngOnInit();
      expect(app.fieldClass).toEqual('col-sm-3');
    }));

    it('ngOnInit() should update fieldClass property for cols:3', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '3' } } } };
      app.ngOnInit();
      expect(app.fieldClass).toEqual('col-sm-3');
    }));

    it('ngOnInit() should update fieldClass property for cols:2', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '2' } } } };
      app.ngOnInit();
      expect(app.fieldClass).toEqual('col-sm-3');
    }));

    it('ngOnInit() should update fieldClass property for cols:1', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '1' } } } };
      app.ngOnInit();
      expect(app.fieldClass).toEqual('col-sm-3');
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

    it('getComponentClass() should return array [testClass, col-sm-12, p-0, clearfix]', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '1', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-12'
      ]);
    }));

    it('getComponentClass() should return array [testClass, col-sm-6, p-0, clearfix]', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '2', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-6'
      ]);
    }));

    it('getComponentClass() should return array [testClass, col-sm-4, p-0, clearfix]', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '3', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-4'
      ]);
    }));

    it('getComponentClass() should return array [testClass, col-sm-3, p-0, clearfix]', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '4', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-3'
      ]);
    }));

    it('getComponentClass() should return array [testClass, col-sm-2, p-0, clearfix]', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '6', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-2'
      ]);
    }));

    it('getComponentClass() should return array [testClass, col-sm-3, p-0, clearfix] when cols is empty', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-3'
      ]);
    }));

    it('value getter() should return _value property value', async(() => {
      app._value = 'test';
      expect(app.value).toEqual('test');
    }));

    it('set value() should update the app.value property', async(() => {
      app.element = { leafState: 'test', values: [{ code: 'test' }] };
      app.value = '';
      expect(app.value).toEqual('test');
    }));

});
