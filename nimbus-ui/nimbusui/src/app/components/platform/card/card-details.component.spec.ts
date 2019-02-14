/**
 * @license
 * Copyright 2016-2018 the original author or authors.
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
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { CardDetailsComponent } from './card-details.component';
import { Link } from '../link.component';
import { StaticText } from '../content/static-content.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { InputText } from '../form/elements/textbox.component';
import { TextArea } from '../form/elements/textarea.component';
import { ComboBox } from '../form/elements/combobox.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { CardDetailsFieldGroupComponent } from './card-details-field-group.component';
import { Paragraph } from '../content/paragraph.component';
import { ButtonGroup } from '../../platform/form/elements/button-group.component';
import { Label } from '../content/label.component';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { InputLabel } from '../../platform/form/elements/input-label.component';
import { Image } from '../../platform/image.component';
import { SvgComponent } from '../../platform/svg/svg.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { By } from '@angular/platform-browser';
import { ServiceConstants } from '../../../services/service.constants';
import { Subject } from 'rxjs';
import { ComponentTypes } from '../../../shared/param-annotations.enum';
import { cardDetailsBodyElement, cardDetailsHeaderElement } from 'mockdata';
import { NmMessageService } from './../../../services/toastmessage.service';import { Param } from './../../../shared/param-state';

class MockPageService {
    public eventUpdate$: Subject<any>;

    constructor() {
        this.eventUpdate$ = new Subject();
    }
    processEvent() {    }
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

@Component({
    template: '<div></div>',
    selector: 'nm-card-details-field'
  })
  class CardDetailsFieldComponent {
  
    @Input() element: any;
    @Input() value: string;

  }

const declarations = [
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
  CardDetailsFieldGroupComponent,
  Paragraph,
  ButtonGroup,
  Label,
  DisplayValueDirective,
  InputLabel,
  Button,
  Image,
  SvgComponent
];
const imports = [
  FormsModule,
  DropdownModule,
  HttpModule,
  HttpClientModule,
  AngularSvgIconModule,
  BrowserAnimationsModule
];
const providers = [
  { provide: PageService, useClass: MockPageService },
  CustomHttpClient,
  LoaderService,
  NmMessageService,
  ConfigService
];

let fixture, hostComponent, pageService;

describe('CardDetailsComponent', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CardDetailsComponent);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = cardDetailsBodyElement as Param;    
    pageService = TestBed.get(PageService);
  });

  it('should create the CardDetailsComponent', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('toggle() should updated opened property', async(() => {
    hostComponent.opened = true;
    hostComponent.toggle();
    expect(hostComponent.opened).toEqual(false);
  }));

  it('processOnClick() should call pageService.processEvent', async(() => {
    hostComponent.element.path = '/a';
    spyOn(pageService, 'processEvent').and.callThrough();
    hostComponent.processOnClick();
    expect(pageService.processEvent).toHaveBeenCalled();
  }));

  it('getAllURLParams should return null matching the regexp', async(() => {
    expect(hostComponent.getAllURLParams('/webhp?hl=en')).toEqual(null);
  }));

  it('getAllURLParams should return string matching the regexp', async(() => {
    expect(hostComponent.getAllURLParams('{ /webhp?hl=en}')).toEqual(['{ /webhp?hl=en}']);
  }));

  it('toggleState() should update isHidden and _state properties', async(() => {
    hostComponent.state = 'closedPanel';
    hostComponent.isHidden = true;
    hostComponent.toggleState();
    expect(hostComponent.isHidden).toBeFalsy();
    expect((hostComponent as any)._state).toEqual('openPanel');
  }));

  it('toggleState() should update _state property', async(() => {
    hostComponent.state = 'openPanel';
    hostComponent.toggleState();
    expect((hostComponent as any)._state).toEqual('closedPanel');
  }));

  it('animationDone() should update the isHidden property', async(() => {
    hostComponent.state = 'closedPanel';
    hostComponent.animationDone('a');
    expect(hostComponent.isHidden).toBeTruthy();
  }));

  it('Label should be created on providing the element.labels and display the value provided', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.position = 1;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-label'));
    expect(labelEle.name).toEqual('nm-label');
    expect(labelEle.nativeElement.innerText.toString().trim()).toEqual('testing card details label');
  }));

  it('Label should not be created on if element.labels is empty', async(() => {
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-label'));
    expect(labelEle).toBeFalsy();
  }));

  it('Expandable Button should be created created if element.config.uiStyles.attributes.alias === Carddetail and element.config.uiStyles.attributes.expandable === true', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    expect(buttonEle.name).toEqual('button');
  }));

  it('OnClick of Expandable Button it should call toggleState()', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    spyOn(hostComponent, 'toggleState').and.callThrough();
    buttonEle.nativeElement.click()
    expect(hostComponent.toggleState).toHaveBeenCalled();
  }));

  it('Expandable Button should not be created created if element.config.uiStyles.attributes.alias === Carddetail and element.config.uiStyles.attributes.expandable === false', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    hostComponent.element.config.uiStyles.attributes.expandable = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    expect(buttonEle).toBeFalsy();
  }));

  it('Button group should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === ButtonGroup', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonGroupEle = debugElement.query(By.css('nm-button-group'));
    expect(buttonGroupEle.name).toEqual('nm-button-group');
  }));

  it('Button group should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== ButtonGroup', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    hostComponent.element.type.model.params[0].type.model.params[0].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonGroupEle = debugElement.query(By.css('nm-button-group'));
    expect(buttonGroupEle).toBeFalsy();
  }));

  it('Paragraph should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === Paragraph', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const paragraphEle = debugElement.query(By.css('nm-paragraph'));
    expect(paragraphEle.name).toEqual('nm-paragraph');
  }));

  it('Paragraph should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== Paragraph', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    hostComponent.element.type.model.params[0].type.model.params[1].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const paragraphEle = debugElement.query(By.css('nm-paragraph'));
    expect(paragraphEle).toBeFalsy();
  }));

  it('card-details-field should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === FieldValue', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsFieldEle = debugElement.query(By.css('nm-card-details-field'));
    expect(cardDetailsFieldEle.name).toEqual('nm-card-details-field');
  }));

  it('card-details-field should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== FieldValue', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    hostComponent.element.type.model.params[0].type.model.params[2].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsFieldEle = debugElement.query(By.css('nm-card-details-field'));
    expect(cardDetailsFieldEle).toBeFalsy();
  }));

  it('StaticText in card details body should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === StaticText', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const staticTextEle = debugElement.query(By.css('nm-static-text'));
    expect(staticTextEle.name).toEqual('nm-static-text');
  }));

  it('StaticText in card details body should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== StaticText', async(() => {
    hostComponent.element.type.model.params[0].type.model.params[0].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const staticTextEle = debugElement.query(By.css('nm-static-text'));
    expect(staticTextEle).toBeFalsy();
  }));

  it('Paragraph in card details body should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === Paragraph', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const paragraphEle = debugElement.query(By.css('nm-paragraph'));
    expect(paragraphEle.name).toEqual('nm-paragraph');
  }));

  it('Paragraph in card details body should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== Paragraph', async(() => {
    hostComponent.element.type.model.params[0].type.model.params[2].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const paragraphEle = debugElement.query(By.css('nm-paragraph'));
    expect(paragraphEle).toBeFalsy();
  }));

  it('On updating param another paragraph need to be added in carddetails body', async(() => {
    let param = hostComponent.element;
    param.type.model.params[0].type.model.params[2].config.uiStyles.attributes.alias = 'Paragraph';
    hostComponent.element = param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allParagraphEle = debugElement.queryAll(By.css('nm-paragraph'));
    const paragraphs = document.getElementsByTagName('nm-paragraph');
    expect(paragraphs.length).toEqual(1);
    let newParam = hostComponent.element;
    let paragraphParam = newParam.type.model.params[0].type.model.params[2];
    paragraphParam = Object.assign({}, paragraphParam);
    paragraphParam.labels[0].text = '2nd paragraph'
    newParam.type.model.params[0].type.model.params.push(paragraphParam);
    newParam.type.model.params[0].type.model.params[2].labels[0].text = '1st paragraph...';
    hostComponent.element = newParam;
    fixture.detectChanges();
    const updatedAllParagraphEle = debugElement.queryAll(By.css('nm-paragraph'));
    const newParagraphs = document.getElementsByTagName('nm-paragraph');
    expect(newParagraphs.length).toEqual(2);
  }));

  it('CardDetailsFieldGroup in card details body should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === CardDetailsFieldGroup', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const CardDetailsFieldGroupEle = debugElement.query(By.css('nm-card-details-field-group'));
    expect(CardDetailsFieldGroupEle.name).toEqual('nm-card-details-field-group');
  }));

  it('CardDetailsFieldGroup in card details body should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== CardDetailsFieldGroup', async(() => {
    hostComponent.element.type.model.params[0].type.model.params[3].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const CardDetailsFieldGroupEle = debugElement.query(By.css('nm-card-details-field-group'));
    expect(CardDetailsFieldGroupEle).toBeFalsy();
  }));

  it('CardDetailsField in card details body should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === CardDetailsField  ', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const CardDetailsFieldEle = debugElement.query(By.css('nm-card-details-field'));
    expect(CardDetailsFieldEle.name).toEqual('nm-card-details-field');
  }));

  it('CardDetailsField in card details body should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== CardDetailsField', async(() => {
    hostComponent.element.type.model.params[0].type.model.params[1].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const CardDetailsFieldEle = debugElement.query(By.css('nm-card-details-field'));
    expect(CardDetailsFieldEle).toBeFalsy;
  }));

  it('Link should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.isLink is valid', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle.name).toEqual('nm-link');
    expect(linkEle.nativeElement.innerText.toString().trim()).toEqual('Delete Note');
  }));

  it('Link should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.isLink is invalid', async(() => {
    hostComponent.element.type.model.params[0].type.model.params[5].config.uiStyles.isLink = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeFalsy();
  }));


  it('On updating param another static-text need to be added in carddetails body', async(() => {
    hostComponent.element.type.model.params[0].type.model.params[0].config.uiStyles.attributes.alias = 'StaticText';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const staticTextEle = document.getElementsByTagName('nm-static-text');
    expect(staticTextEle.length).toEqual(1);
    hostComponent.element.type.model.params[0].type.model.params.push(hostComponent.element.type.model.params[0].type.model.params[0])
    fixture.detectChanges();
    const newStaticTextEle = document.getElementsByTagName('nm-static-text');
    expect(newStaticTextEle.length).toEqual(2);
  }));

  it('On updating param another card-details-field-group need to be added in carddetails body', async(() => {
    hostComponent.element.type.model.params[0].type.model.params[3].config.uiStyles.attributes.alias = 'FieldValueGroup';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsFieldGroupEle = document.getElementsByTagName('nm-card-details-field-group');
    expect(cardDetailsFieldGroupEle.length).toEqual(1);
    hostComponent.element.type.model.params[0].type.model.params.push(hostComponent.element.type.model.params[0].type.model.params[3]);
    fixture.detectChanges();
    const updatedCardDetailsFieldGroupEle = document.getElementsByTagName('nm-card-details-field-group');
    expect(cardDetailsFieldGroupEle.length).toEqual(2);
  }));

  it('On updating param another nm-card-details-field need to be added in carddetails body', async(() => {
    hostComponent.element.type.model.params[0].type.model.params[1].config.uiStyles.attributes.alias = 'FieldValue';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsFieldEle = document.getElementsByTagName('nm-card-details-field');
    expect(cardDetailsFieldEle.length).toEqual(2);
    hostComponent.element.type.model.params[0].type.model.params.push(hostComponent.element.type.model.params[0].type.model.params[1]);
    fixture.detectChanges();
    const updatedCardDetailsFieldEle = document.getElementsByTagName('nm-card-details-field');
    expect(updatedCardDetailsFieldEle.length).toEqual(3);
  }));

  it('On updating param another button group need to be added in carddetails header', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    hostComponent.element.type.model.params[0].type.model.params[0].config.uiStyles.attributes.alias = 'ButtonGroup';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonGroupEle = document.getElementsByTagName('nm-button-group');
    expect(buttonGroupEle.length).toEqual(1);
    hostComponent.element.type.model.params[0].type.model.params.push(hostComponent.element.type.model.params[0].type.model.params[0]);
    fixture.detectChanges();
    const updatedButtonGroupEle = document.getElementsByTagName('nm-button-group');
    expect(updatedButtonGroupEle.length).toEqual(2);
  }));

  it('On updating param another paragraph need to be added in carddetails header', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    hostComponent.element.type.model.params[0].type.model.params[1].config.uiStyles.attributes.alias = 'Paragraph';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const paragraphEle = document.getElementsByTagName('nm-paragraph');
    expect(paragraphEle.length).toEqual(1);
    const newEle = hostComponent.element.type.model.params[0].type.model.params[1];
    newEle.labels[0].text = 'new paragraph';
    hostComponent.element.type.model.params[0].type.model.params.push(newEle);
    fixture.detectChanges();
    const updatedParagraphEle = document.getElementsByTagName('nm-paragraph');
    expect(updatedParagraphEle.length).toEqual(2);
    expect(updatedParagraphEle[1].getElementsByTagName("p")[0].innerHTML).toEqual('new paragraph');
  }));

  it('On updating param another card-details-field need to be added in carddetails header', async(() => {
    hostComponent.element = cardDetailsHeaderElement as Param;
    hostComponent.element.type.model.params[0].type.model.params[2].config.uiStyles.attributes.alias = 'FieldValue';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsFieldEle = document.getElementsByTagName('nm-card-details-field');
    expect(cardDetailsFieldEle.length).toEqual(1);
    hostComponent.element.type.model.params[0].type.model.params.push(hostComponent.element.type.model.params[0].type.model.params[2]);
    fixture.detectChanges();
    const updatedCardDetailsFieldEle = document.getElementsByTagName('nm-card-details-field');
    expect(updatedCardDetailsFieldEle.length).toEqual(2);
  }));

});
