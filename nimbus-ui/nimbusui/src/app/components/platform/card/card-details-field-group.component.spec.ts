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
  configureTestSuite();
  setup(CardDetailsFieldGroupComponent, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<CardDetailsFieldGroupComponent>){
    this.hostComponent.element = param;
  });

  it('should create the CardDetailsFieldGroupComponent', async function (this: TestContext<CardDetailsFieldGroupComponent>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('getComponentClass() should return array [testClass, col-sm-12]', async function (this: TestContext<CardDetailsFieldGroupComponent>) {
    this.fixture.whenStable().then(() => {
      const res = this.hostComponent.getComponentClass();
      expect(res).toEqual(['testClass', 'col-sm-12']);
    });
  });

  it('getComponentClass() should return array [testClass, col-sm-6]', async function (this: TestContext<CardDetailsFieldGroupComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '2';
      expect(this.hostComponent.getComponentClass()).toEqual(['testClass', 'col-sm-6']);
    });
  });

  it('getComponentClass() should return array [testClass, col-sm-4]', async function (this: TestContext<CardDetailsFieldGroupComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '3';
      expect(this.hostComponent.getComponentClass()).toEqual(['testClass', 'col-sm-4']);
    });
  });

  it('getComponentClass() should return array [testClass, col-sm-3]', async function (this: TestContext<CardDetailsFieldGroupComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '4';
      expect(this.hostComponent.getComponentClass()).toEqual(['testClass', 'col-sm-3']);
    });
  });

  it('getComponentClass() should return array [testClass, col-sm-2]', async function (this: TestContext<CardDetailsFieldGroupComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '6';
      expect(this.hostComponent.getComponentClass()).toEqual(['testClass', 'col-sm-2']);
    });
  });

  it('getComponentClass() should return array [testClass, col-sm-3] when cols is empty', async function (this: TestContext<CardDetailsFieldGroupComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '';
      expect(this.hostComponent.getComponentClass()).toEqual(['testClass', 'col-sm-3']);
    });
  });

  it('getComponentClass() should return array [col-sm-3] when cols and cssClass is empty', async function (this: TestContext<CardDetailsFieldGroupComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cols = '';
      this.hostComponent.element.config.uiStyles.attributes.cssClass = '';
      expect(this.hostComponent.getComponentClass()).toEqual(['col-sm-3']);
    });
  });

});
