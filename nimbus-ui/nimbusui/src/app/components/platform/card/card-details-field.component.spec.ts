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
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { InputLabel } from '../../platform/form/elements/input-label.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { Values, Param } from '../../../shared/param-state';

let param: Param;

const declarations = [
  CardDetailsFieldComponent,
  InPlaceEditorComponent,
  InputText,
  TextArea,
  ComboBox,
  DateTimeFormatPipe,
  TooltipComponent,
  SelectItemPipe,
  DisplayValueDirective,
  InputLabel
];
const imports = [FormsModule, DropdownModule, HttpClientModule, HttpModule];
const providers = [CustomHttpClient];

describe('CardDetailsFieldComponent', () => {

  configureTestSuite();
  setup(CardDetailsFieldComponent, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<CardDetailsFieldComponent>){
    this.hostComponent.element = param;
  });

  it('should create the CardDetailsFieldComponent', async function (this: TestContext<CardDetailsFieldComponent>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('ngOnInit() should update fieldClass property for cols:6', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '6';
      this.hostComponent.ngOnInit();
      expect((this.hostComponent as any).fieldClass).toEqual('col-sm-3');
    });
  });

  it('ngOnInit() should update fieldClass property for cols:4', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '4';
      this.hostComponent.ngOnInit();
      expect((this.hostComponent as any).fieldClass).toEqual('col-sm-3');
    });
  });

  it('ngOnInit() should update fieldClass property for cols:3', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '3';
      this.hostComponent.ngOnInit();
      expect((this.hostComponent as any).fieldClass).toEqual('col-sm-3');
    });
  });

  it('ngOnInit() should update fieldClass property for cols:2', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '2';
      this.hostComponent.ngOnInit();
      expect((this.hostComponent as any).fieldClass).toEqual('col-sm-3');
    });
  });

  it('ngOnInit() should update fieldClass property for cols:1', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '1';
      this.hostComponent.ngOnInit();
      expect((this.hostComponent as any).fieldClass).toEqual('col-sm-3');
    });
  });

  it('value property should be updated with element.leafstate', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.hostComponent.element.leafState = 'test';
    this.hostComponent.element.values = [];
    this.hostComponent.value = '';
    expect(this.hostComponent.value).toEqual('test');
  });

  it('value property should be updated with element.values.label', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.hostComponent.element.leafState = 'test';
    const testValue = new Values();
    testValue.code = 'test';
    testValue.label = 'tLabel';
    this.hostComponent.element.values = [testValue];
    this.hostComponent.value = '';
    expect(this.hostComponent.value).toEqual('tLabel');
  });
  it('value property should be updated with element.leafstate based on code', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.hostComponent.element.leafState = 'test';
    const testValue = new Values();
    testValue.code = 'test1';
    testValue.label = 'tLabel';
    this.hostComponent.element.values = [testValue];
    this.hostComponent.value = '';
    expect(this.hostComponent.value).toEqual('test');
  });

  it('registerOnChange() should update the onChange property', async function (this: TestContext<CardDetailsFieldComponent>) {
    const test = () => {
      return true;
    };
    this.hostComponent.registerOnChange(test);
    expect(this.hostComponent.onChange).toEqual(test);
  });

  it('writeValue() shouls call onChange()', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.hostComponent.element.leafState = 'test';
    this.hostComponent.element.values = [];
    spyOn(this.hostComponent, 'onChange').and.callThrough();
    this.hostComponent.writeValue(123);
    this.hostComponent.writeValue(undefined);
    expect(this.hostComponent.onChange).toHaveBeenCalled();
  });
  it('registerOnTouched() should update the onTouched property', async function (this: TestContext<CardDetailsFieldComponent>) {
    const test = () => {
      return true;
    };
    this.hostComponent.registerOnTouched(test);
    expect(this.hostComponent.onTouched).toEqual(test);
  });

  it('getComponentClass() should return array [testClass, col-sm-12, p-0, clearfix]', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '1';
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
      expect(this.hostComponent.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-12'
      ]);
    });
  });

  it('getComponentClass() should return array [testClass, col-sm-6, p-0, clearfix]', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '2';
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
      expect(this.hostComponent.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-6'
      ]);
    });
  });

  it('getComponentClass() should return array [testClass, col-sm-4, p-0, clearfix]', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '3';
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
      expect(this.hostComponent.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-4'
      ]);
    });
  });

  it('getComponentClass() should return array [testClass, col-sm-3, p-0, clearfix]', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '4';
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
      expect(this.hostComponent.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-3'
      ]);
    });
  });

  it('getComponentClass() should return array [testClass, col-sm-2, p-0, clearfix]', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '6';
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
      expect(this.hostComponent.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-2'
      ]);
    });
  });

  it('getComponentClass() should return array [testClass, col-sm-3, p-0, clearfix] when cols is empty', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '';
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
      expect(this.hostComponent.getComponentClass()).toEqual([
        'testClass',
        'p-0',
        'clearfix',
        'col-sm-3'
      ]);
    });
  });

  it('value getter() should return _value property value', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.hostComponent.value = 'test';
    expect(this.hostComponent._value).toEqual('test');
  });

  it('set value() should update the value property', async function (this: TestContext<CardDetailsFieldComponent>) {
    this.hostComponent.element.leafState = 'test';
    const testValue = new Values();
    testValue.code = 'test1';
    this.hostComponent.element.values = [testValue];
    this.hostComponent.value = '';
    expect(this.hostComponent.value).toEqual('test');
  });

});
