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

import { HttpClientModule } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { async, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { By } from '@angular/platform-browser';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { cardDetailsGridElement, cardDetailsGridNewElement } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { DropdownModule, TooltipModule } from 'primeng/primeng';
import { Subject } from 'rxjs';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { PrintDirective } from '../../../directives/print.directive';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { AppInitService } from '../../../services/app.init.service';
import { ConfigService } from '../../../services/config.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { LoggerService } from '../../../services/logger.service';
import { PageService } from '../../../services/page.service';
import { PrintService } from '../../../services/print.service';
import { ServiceConstants } from '../../../services/service.constants';
import { CUSTOM_STORAGE } from '../../../services/session.store';
import { setup } from '../../../setup.spec';
import { ButtonGroup } from '../../platform/form/elements/button-group.component';
import { InputLabel } from '../../platform/form/elements/input-label.component';
import { InputLegend } from '../../platform/form/elements/input-legend.component';
import { Image } from '../../platform/image.component';
import { SvgComponent } from '../../platform/svg/svg.component';
import { Label } from '../content/label.component';
import { Paragraph } from '../content/paragraph.component';
import { StaticText } from '../content/static-content.component';
import { ComboBox } from '../form/elements/combobox.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { TextArea } from '../form/elements/textarea.component';
import { InputText } from '../form/elements/textbox.component';
import { Link } from '../link.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { CardDetailsFieldGroupComponent } from './card-details-field-group.component';
import { CardDetailsFieldComponent } from './card-details-field.component';
import { CardDetailsGrid } from './card-details-grid.component';
import { CardDetailsComponent } from './card-details.component';
'use strict';

class MockPageService {
  eventUpdate$: Subject<any>;

  constructor() {
    this.eventUpdate$ = new Subject();
  }

  processEvent() {}
}

@Component({
  template: '<div></div>',
  selector: 'nm-button'
})
class Button {
  @Input() element: any;
  @Input() payload: string;
  @Input() form: any;
  @Input() actionTray?: boolean;

  @Output() buttonClickEvent = new EventEmitter();

  @Output() elementChange = new EventEmitter();
  private imagesPath: string;
  private btnClass: string;
  private disabled: boolean;
  files: any;
  differ: any;
  componentTypes;
}

const declarations = [
  CardDetailsGrid,
  CardDetailsComponent,
  Link,
  CardDetailsFieldComponent,
  StaticText,
  InPlaceEditorComponent,
  InputText,
  TextArea,
  ComboBox,
  DateTimeFormatPipe,
  TooltipComponent,
  SelectItemPipe,
  Label,
  CardDetailsFieldGroupComponent,
  Paragraph,
  ButtonGroup,
  DisplayValueDirective,
  InputLabel,
  Button,
  Image,
  SvgComponent,
  PrintDirective,
  InputLegend
];
const imports = [
  FormsModule,
  DropdownModule,
  TooltipModule,
  HttpClientModule,
  HttpModule,
  AngularSvgIconModule,
  StorageServiceModule
];
const providers = [
  { provide: PageService, useClass: MockPageService },
  { provide: 'JSNLOG', useValue: JL },
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  CustomHttpClient,
  LoaderService,
  ConfigService,
  LoggerService,
  AppInitService,
  PrintService
];

let fixture, hostComponent, pageService;

describe('CardDetailsGrid', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CardDetailsGrid);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = cardDetailsGridElement;
    pageService = TestBed.get(PageService);
  });

  it('should create the CardDetailsGrid', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('Label should be created on providing the element.labels display the value provided', async(() => {
    hostComponent.position = 1;
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-label'));
    expect(labelEle.name).toEqual('nm-label');
    expect(labelEle.nativeElement.innerText.toString().trim()).toEqual(
      'testing grid label-181'
    );
  }));

  it('nm-card-details should be created if element?.type?.model?.params[0].type?.model?.params[0].config?.uiStyles?.attributes?.alias === CardDetail', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsComponentEle = debugElement.query(
      By.css('nm-card-details')
    );
    expect(cardDetailsComponentEle.name).toEqual('nm-card-details');
  }));

  it('compare the values rendered in the view with param object provided', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.position = 1;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsComponentEle = debugElement.query(
      By.css('nm-card-details')
    );
    const labelEles = debugElement.queryAll(By.css('nm-label'));
    expect(cardDetailsComponentEle.name).toEqual('nm-card-details');
    expect(labelEles[1].nativeElement.innerText.toString().trim()).toEqual(
      'testing card details label 108-'
    );
  }));

  it('Updating the param object should add a row', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allCardDetailsEles = debugElement.queryAll(By.css('nm-card-details'));
    expect(allCardDetailsEles.length).toEqual(2);
    hostComponent.element.type.model.params.push(cardDetailsGridNewElement);
    fixture.detectChanges();
    const newAllCardDetailsEles = debugElement.queryAll(
      By.css('nm-card-details')
    );
    expect(newAllCardDetailsEles.length).toEqual(3);
  }));

  it('nm-card-details should not be created if element?.type?.model?.params[0].type?.model?.params[0].config?.uiStyles?.attributes?.alias !== CardDetail', async(() => {
    hostComponent.element.type.model.params[0].type.model.params[0].config.uiStyles.attributes.alias =
      '';
    hostComponent.element.type.model.params[1].type.model.params[0].config.uiStyles.attributes.alias =
      '';
    hostComponent.element.type.model.params[2].type.model.params[0].config.uiStyles.attributes.alias =
      '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsComponentEle = debugElement.query(
      By.css('nm-card-details')
    );
    expect(cardDetailsComponentEle).toBeFalsy();
  }));

  it('Label should not be created on if element.labels is empty', async(() => {
    hostComponent.position = 1;
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-label'));
    expect(labelEle).toBeFalsy();
  }));

  it('ngonint() should call pageService.processEvent', () => {
    hostComponent.element.config.uiStyles.attributes.onLoad = true;
    spyOn(pageService, 'processEvent').and.callThrough();
    hostComponent.ngOnInit();
    expect(pageService.processEvent).toHaveBeenCalled();
  });

  it('ngonint() should not call pageSvc.processEvent', () => {
    hostComponent.element.config.uiStyles.attributes.onLoad = false;
    spyOn(pageService, 'processEvent').and.callThrough();
    hostComponent.ngOnInit();
    expect(pageService.processEvent).not.toHaveBeenCalled();
  });
});
