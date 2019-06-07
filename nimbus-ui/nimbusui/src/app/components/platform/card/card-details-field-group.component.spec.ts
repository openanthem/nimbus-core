/**
 * @license
 * Copyright 2016-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';
import {
  HashLocationStrategy,
  Location,
  LocationStrategy
} from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { async, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { By } from '@angular/platform-browser';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import {
  cardDetailsFieldGroupElement,
  newCardDetailsFieldGroupElement
} from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { DropdownModule, TooltipModule } from 'primeng/primeng';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { AppInitService } from '../../../services/app.init.service';
import { ConfigService } from '../../../services/config.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { LoggerService } from '../../../services/logger.service';
import { PageService } from '../../../services/page.service';
import { ServiceConstants } from '../../../services/service.constants';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from '../../../services/session.store';
import { setup } from '../../../setup.spec';
import { Param } from '../../../shared/param-state';
import { InputLabel } from '../../platform/form/elements/input-label.component';
import { InputLegend } from '../../platform/form/elements/input-legend.component';
import { Paragraph } from '../content/paragraph.component';
import { ComboBox } from '../form/elements/combobox.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { TextArea } from '../form/elements/textarea.component';
import { InputText } from '../form/elements/textbox.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { NmMessageService } from './../../../services/toastmessage.service';
import { CardDetailsFieldGroupComponent } from './card-details-field-group.component';
import { CardDetailsFieldComponent } from './card-details-field.component';

let fixture, hostComponent;

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
  InputLabel,
  Paragraph,
  InputLegend
];
const imports = [
  FormsModule,
  DropdownModule,
  TooltipModule,
  HttpClientModule,
  HttpModule,
  StorageServiceModule
];
const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  Location,
  AppInitService,
  SessionStoreService,
  CustomHttpClient,
  NmMessageService,
  PageService,
  LoaderService,
  ConfigService,
  LoggerService
];

describe('CardDetailsFieldGroupComponent', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CardDetailsFieldGroupComponent);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = cardDetailsFieldGroupElement as Param;
  });

  it('should create the CardDetailsFieldGroupComponent', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('legend should be created on providing the element.labelconfig display the value provided', async(() => {
    hostComponent.element = newCardDetailsFieldGroupElement as Param;
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const legendEle = debugElement.query(By.css('nm-input-legend'));
    expect(legendEle).toBeTruthy();
    expect(legendEle.nativeElement.innerText.toString().trim()).toEqual(
      'Case ID'
    );
  }));

  it('legend should not be created on if element.labelconfig is empty', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.type.model.params[0].labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const legendEle = debugElement.query(By.css('nm-input-legend'));
    expect(legendEle).toBeFalsy();
  }));

  it('nm-card-details-field should be created if element?.type?.model?.params[0].config?.uiStyles?.attributes?.alias === FieldValue', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsFieldEle = debugElement.query(
      By.css('nm-card-details-field')
    );
    expect(cardDetailsFieldEle.name).toEqual('nm-card-details-field');
  }));

  it('nm-card-details-field should not be created if element?.type?.model?.params[0].config?.uiStyles?.attributes?.alias !== FieldValue', async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.alias =
      '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsFieldEle = debugElement.query(
      By.css('nm-card-details-field')
    );
    expect(cardDetailsFieldEle).toBeFalsy();
  }));

  it('another nm-card-details-field should be added on updating the param', async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.alias =
      'FieldValue';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allCardDetailsFieldEles = debugElement.queryAll(
      By.css('nm-card-details-field')
    );
    expect(allCardDetailsFieldEles.length).toEqual(1);
    hostComponent.element.type.model.params.push(
      newCardDetailsFieldGroupElement
    );
    fixture.detectChanges();
    const newAllCardDetailsFieldEles = debugElement.queryAll(
      By.css('nm-card-details-field')
    );
    expect(newAllCardDetailsFieldEles.length).toEqual(2);
  }));
});
