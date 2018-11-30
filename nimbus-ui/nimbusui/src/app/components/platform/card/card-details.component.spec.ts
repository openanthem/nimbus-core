import { Param } from './../../../shared/param-state';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { CardDetailsComponent } from './card-details.component';
import { Link } from '../link.component';
import { CardDetailsFieldComponent } from './card-details-field.component';
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
// import { Button } from '../../platform/form/elements/button.component';
import { Image } from '../../platform/image.component';
import { SvgComponent } from '../../platform/svg/svg.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';

let param, pageService;

class MockPageService {
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
  AngularSvgIconModule
];
const providers = [
  { provide: PageService, useClass: MockPageService },
  CustomHttpClient,
  LoaderService,
  ConfigService
];
let fixture, hostComponent;

describe('CardDetailsComponent', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

     let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);

  beforeEach(() => {
    fixture = TestBed.createComponent(CardDetailsComponent);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = element;
    pageService = TestBed.get(PageService);
  });

  it('should create the CardDetailsComponent',async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('toggle() should updated opened property',async(() => {
    hostComponent.opened = true;
    hostComponent.toggle();
    expect(hostComponent.opened).toEqual(false);
  }));

  it('processOnClick() should call pageService.processEvent',async(() => {
    hostComponent.element.path = '/a';
    spyOn(pageService, 'processEvent').and.callThrough();
    hostComponent.processOnClick();
    expect(pageService.processEvent).toHaveBeenCalled();
  }));

  it('getAllURLParams should return null matching the regexp',async(() => {
    expect(hostComponent.getAllURLParams('/webhp?hl=en')).toEqual(null);
  }));

  it('getAllURLParams should return string matching the regexp',async(() => {
    expect(hostComponent.getAllURLParams('{ /webhp?hl=en}')).toEqual(['{ /webhp?hl=en}']);
  }));

  it('toggleState() should update isHidden and _state properties',async(() => {
    hostComponent.state = 'closedPanel';
    hostComponent.isHidden = true;
    hostComponent.toggleState();
    expect(hostComponent.isHidden).toBeFalsy();
    expect((hostComponent as any)._state).toEqual('openPanel');
  }));

  it('toggleState() should update _state property',async(() => {
    hostComponent.state = 'openPanel';
    hostComponent.toggleState();
    expect((hostComponent as any)._state).toEqual('closedPanel');
  }));

  it('animationDone() should update the isHidden property',async(() => {
    hostComponent.state = 'closedPanel';
    hostComponent.animationDone('a');
    expect(hostComponent.isHidden).toBeTruthy();
  }));

  it('Label should be created on providing the element.labelconfig and display the value provided', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Label should not be created on if element.labelconfig is empty', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Carddetails header should be created if param.config?.uiStyles?.attributes?.alias === CardDetailsHeader', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Carddetails header should not be created if param.config?.uiStyles?.attributes?.alias !== CardDetailsHeader', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Expandable Button should be created created if element.config.uiStyles.attributes.alias === Carddetail and element.config.uiStyles.attributes.expandable === true', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Expandable Button should not be created created if element.config.uiStyles.attributes.alias === Carddetail and element.config.uiStyles.attributes.expandable === false', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('OnClick of Expandable Button it should call toggle()', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Button group should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === ButtonGroup', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Button group should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== ButtonGroup', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Paragraph should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === Paragraph', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Paragraph should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== Paragraph', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('card-details-field should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === FieldValue', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('card-details-field should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== FieldValue', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('StaticText in card details body should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === StaticText', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('StaticText in card details body should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== StaticText', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Paragraph in card details body should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === Paragraph', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Paragraph in card details body should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== Paragraph', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('CardDetailsFieldGroup in card details body should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === CardDetailsFieldGroup', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('CardDetailsFieldGroup in card details body should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== CardDetailsFieldGroup', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('CardDetailsField in card details body should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias === CardDetailsField  ', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('CardDetailsField in card details body should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.attributes?.alias !== CardDetailsField', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Link should be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.isLink is valid', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Link should not be created if element.type.model.params[0].type.model.params[0].config?.uiStyles?.isLink is invalid', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));


});

const element: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "3980",
      "code": "vcdOwnerInfo",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.CardDetail",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "border": false,
              "cssClass": "contentBox right-gutter bg-alternate mt-0",
              "draggable": false,
              "expandable": true,
              "editable": false,
              "modelPath": "",
              "alias": "CardDetail",
              "imgSrc": ""
          }
      },
      "type": {
          "collection": false,
          "nested": true,
          "name": "VPOwnerInfo.VCDOwnerInfo",
          "model": {
              "paramConfigIds": [
                  "3982",
                  "3992"
              ]
          }
      }
  },
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "3980",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo",
  "type": {
      "model": {
          "params": [
              {
                  "enabled": true,
                  "visible": true,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "3982",
                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner",
                  "type": {
                      "model": {
                          "params": [                          ]
                      }
                  },
                  "message": [],
                  "values": [],
                  "labels": [],
                  "elemLabels": {}
              },
              {
                  "enabled": true,
                  "visible": true,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "3992",
                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner1",
                  "type": {
                      "model": {
                          "params": [
                              {
                                  "enabled": true,
                                  "visible": true,
                                  "activeValidationGroups": [],
                                  "collectionParams": [],
                                  "configId": "3994",
                                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner1/vbgDefault1",
                                  "type": {
                                      "model": {
                                          "params": [
                                              {
                                                  "enabled": true,
                                                  "visible": true,
                                                  "activeValidationGroups": [],
                                                  "collectionParams": [],
                                                  "configId": "3996",
                                                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner1/vbgDefault1/submit",
                                                  "type": {
                                                      "nested": false,
                                                      "name": "string",
                                                      "collection": false
                                                  },
                                                  "message": [],
                                                  "values": [],
                                                  "labels": [
                                                      {
                                                          "locale": "en-US",
                                                          "text": "Submit"
                                                      }
                                                  ],
                                                  "elemLabels": {}
                                              },
                                              {
                                                  "enabled": true,
                                                  "visible": true,
                                                  "activeValidationGroups": [],
                                                  "collectionParams": [],
                                                  "configId": "3997",
                                                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner1/vbgDefault1/back",
                                                  "type": {
                                                      "nested": false,
                                                      "name": "string",
                                                      "collection": false
                                                  },
                                                  "message": [],
                                                  "values": [],
                                                  "labels": [
                                                      {
                                                          "locale": "en-US",
                                                          "text": "Back"
                                                      }
                                                  ],
                                                  "elemLabels": {}
                                              }
                                          ]
                                      }
                                  },
                                  "message": [],
                                  "values": [],
                                  "labels": [],
                                  "elemLabels": {}
                              }
                          ]
                      }
                  },
                  "message": [],
                  "values": [],
                  "labels": [],
                  "elemLabels": {}
              }
          ]
      }
  },
  "message": [],
  "values": [],
  "labels": [
      {
          "locale": "en-US",
          "text": "testing card details label"
      }
  ],
  "elemLabels": {}
};