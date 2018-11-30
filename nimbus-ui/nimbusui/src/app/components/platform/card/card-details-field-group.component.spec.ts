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
import { CardDetailsFieldGroupComponent } from './card-details-field-group.component';
import { WebContentSvc } from '../../../services/content-management.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { Param } from '../../../shared/param-state';

let param: Param;
let fixture,hostComponent;

const declarations = [
  CardDetailsFieldGroupComponent,
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
const imports = [
    FormsModule, 
    DropdownModule, 
    HttpClientModule, 
    HttpModule
  ];
const providers = [
    CustomHttpClient,
    WebContentSvc
  ];

describe('CardDetailsFieldGroupComponent', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
   fixture = TestBed.createComponent(CardDetailsFieldGroupComponent);
   hostComponent = fixture.debugElement.componentInstance;
      let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);
   //let payload: Param = new Param(this.configService).deserialize((<any>data).payload, (<any>data).payload.path);
   hostComponent.element = param;
  });

  it('should create the CardDetailsFieldGroupComponent',  async(() => {
    expect(hostComponent).toBeTruthy(); 
  }));

  it('Label should be created on providing the element.labelconfig display the value provided',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Label should not be created on if element.labelconfig is empty',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-card-details-field should be created if element?.type?.model?.params[0].config?.uiStyles?.attributes?.alias === FieldValue',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-card-details-field should not be created if element?.type?.model?.params[0].config?.uiStyles?.attributes?.alias !== FieldValue',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  // it('getComponentClass() should return array [testClass, col-sm-12]',  async(() => {
  //     const res = hostComponent.getComponentClass();
  //     expect(res).toEqual(['testClass', 'col-sm-12']);
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-6]',  async(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '2';
  //     expect(hostComponent.getComponentClass()).toEqual(['testClass', 'col-sm-6']);
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-4]',  async(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '3';
  //     expect(hostComponent.getComponentClass()).toEqual(['testClass', 'col-sm-4']);
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-3]',  async(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '4';
  //     expect(hostComponent.getComponentClass()).toEqual(['testClass', 'col-sm-3']);
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-2]',  async(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '6';
  //     expect(hostComponent.getComponentClass()).toEqual(['testClass', 'col-sm-2']);
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-3] when cols is empty',  async(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '';
  //     expect(hostComponent.getComponentClass()).toEqual(['testClass', 'col-sm-3']);
  // }));

  // it('getComponentClass() should return array [col-sm-3] when cols and cssClass is empty',  async(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = '';
  //     expect(hostComponent.getComponentClass()).toEqual(['col-sm-3']);
  // }));

});
