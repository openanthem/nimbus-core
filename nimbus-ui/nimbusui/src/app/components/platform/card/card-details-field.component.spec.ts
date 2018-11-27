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
let fixture, hostComponent;

describe('CardDetailsFieldComponent', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

     let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);

  beforeEach( async(() => {
    fixture = TestBed.createComponent(CardDetailsFieldComponent);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = param;
  }));

  it('should create the CardDetailsFieldComponent',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  // it('ngOnInit() should update fieldClass property for cols:6',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '6';
  //     hostComponent.ngOnInit();
  //     expect((hostComponent as any).fieldClass).toEqual('col-sm-3');
  //   });
  // }));

  // it('ngOnInit() should update fieldClass property for cols:4',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '4';
  //     hostComponent.ngOnInit();
  //     expect((hostComponent as any).fieldClass).toEqual('col-sm-3');
  //   });
  // }));

  // it('ngOnInit() should update fieldClass property for cols:3',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '3';
  //     hostComponent.ngOnInit();
  //     expect((hostComponent as any).fieldClass).toEqual('col-sm-3');
  //   });
  // }));

  // it('ngOnInit() should update fieldClass property for cols:2',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '2';
  //     hostComponent.ngOnInit();
  //     expect((hostComponent as any).fieldClass).toEqual('col-sm-3');
  //   });
  // }));

  // it('ngOnInit() should update fieldClass property for cols:1',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '1';
  //     hostComponent.ngOnInit();
  //     expect((hostComponent as any).fieldClass).toEqual('col-sm-3');
  //   });
  // }));

  // it('value property should be updated with element.leafstate',  async(() => {
  //   hostComponent.element.leafState = 'test';
  //   hostComponent.element.values = [];
  //   hostComponent.value = '';
  //   expect(hostComponent.value).toEqual('test');
  // }));

  // it('value property should be updated with element.values.label',  async(() => {
  //   hostComponent.element.leafState = 'test';
  //   const testValue = new Values();
  //   testValue.code = 'test';
  //   testValue.label = 'tLabel';
  //   hostComponent.element.values = [testValue];
  //   hostComponent.value = '';
  //   expect(hostComponent.value).toEqual('tLabel');
  // }));
  // it('value property should be updated with element.leafstate based on code',  async(() => {
  //   hostComponent.element.leafState = 'test';
  //   const testValue = new Values();
  //   testValue.code = 'test1';
  //   testValue.label = 'tLabel';
  //   hostComponent.element.values = [testValue];
  //   hostComponent.value = '';
  //   expect(hostComponent.value).toEqual('test');
  // }));

  // it('registerOnChange() should update the onChange property',  async(() => {
  //   const test = () => {
  //     return true;
  //   };
  //   hostComponent.registerOnChange(test);
  //   expect(hostComponent.onChange).toEqual(test);
  // }));

  // it('writeValue() shouls call onChange()',  async(() => {
  //   hostComponent.element.leafState = 'test';
  //   hostComponent.element.values = [];
  //   spyOn(hostComponent, 'onChange').and.callThrough();
  //   hostComponent.writeValue(123);
  //   hostComponent.writeValue(undefined);
  //   expect(hostComponent.onChange).toHaveBeenCalled();
  // }));
  // it('registerOnTouched() should update the onTouched property',  async(() => {
  //   const test = () => {
  //     return true;
  //   };
  //   hostComponent.registerOnTouched(test);
  //   expect(hostComponent.onTouched).toEqual(test);
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-12, p-0, clearfix]',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '1';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-12'
  //     ]);
  //   });
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-6, p-0, clearfix]',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '2';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-6'
  //     ]);
  //   });
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-4, p-0, clearfix]',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '3';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-4'
  //     ]);
  //   });
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-3, p-0, clearfix]',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '4';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-3'
  //     ]);
  //   });
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-2, p-0, clearfix]',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '6';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-2'
  //     ]);
  //   });
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-3, p-0, clearfix] when cols is empty',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-3'
  //     ]);
  //   });
  // }));

  // it('value getter() should return _value property value',  async(() => {
  //   hostComponent.value = 'test';
  //   expect(hostComponent._value).toEqual('test');
  // }));

  // it('set value() should update the value property',  async(() => {
  //   hostComponent.element.leafState = 'test';
  //   const testValue = new Values();
  //   testValue.code = 'test1';
  //   hostComponent.element.values = [testValue];
  //   hostComponent.value = '';
  //   expect(hostComponent.value).toEqual('test');
  // }));

});
