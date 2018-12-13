import { Param } from './../../../shared/param-state';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';

import { CardDetailsGrid } from './card-details-grid.component';
import { PageService } from '../../../services/page.service';
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
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { Label } from '../content/label.component';
import { CardDetailsFieldGroupComponent } from './card-details-field-group.component';
import { Paragraph } from '../content/paragraph.component';
import { ButtonGroup } from '../../platform/form/elements/button-group.component';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { InputLabel } from '../../platform/form/elements/input-label.component';
// import { Button } from '../../platform/form/elements/button.component';
import { Image } from '../../platform/image.component';
import { SvgComponent } from '../../platform/svg/svg.component';
import { WebContentSvc } from '../../../services/content-management.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { PrintDirective } from '../../../directives/print.directive';
import { By } from '@angular/platform-browser';
import { ServiceConstants } from '../../../services/service.constants';
import { Subject } from 'rxjs';
import { LoggerService } from '../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { AppInitService } from '../../../services/app.init.service';
import { PrintService } from '../../../services/print.service';

let param, pageService;

class MockPageService {
    eventUpdate$: Subject<any>;

    constructor() {
        this.eventUpdate$ = new Subject();
    }

    processEvent() {

    }
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
    PrintDirective
    ];
const imports = [ 
    FormsModule,
    DropdownModule,
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
     WebContentSvc,
     LoggerService,
     AppInitService,
     PrintService
     ];

let fixture, hostComponent;

describe('CardDetailsGrid', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     
  let param: Param = JSON.parse(payload);

  beforeEach(() => {
    fixture = TestBed.createComponent(CardDetailsGrid);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = cardDetailsGridElement;
    pageService = TestBed.get(PageService);
  });

  it('should create the CardDetailsGrid', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('Label should be created on providing the element.labels display the value provided',async(() => {
      hostComponent.position = 1;
      ServiceConstants.LOCALE_LANGUAGE = "en-US";
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-label'));
    expect(labelEle.name).toEqual('nm-label');
    expect(labelEle.nativeElement.innerText.toString().trim()).toEqual('testing grid label-181');    
  }));

  it('nm-card-details should be created if element?.type?.model?.params[0].type?.model?.params[0].config?.uiStyles?.attributes?.alias === CardDetail',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsComponentEle = debugElement.query(By.css('nm-card-details'));    
    expect(cardDetailsComponentEle.name).toEqual('nm-card-details');
  }));

  it('compare the values rendered in the view with param object provided',async(() => {
    ServiceConstants.LOCALE_LANGUAGE = "en-US";
    hostComponent.position = 1;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsComponentEle = debugElement.query(By.css('nm-card-details'));
    const labelEles = debugElement.queryAll(By.css('nm-label'));    
    expect(cardDetailsComponentEle.name).toEqual('nm-card-details');
    expect(labelEles[1].nativeElement.innerText.toString().trim()).toEqual('testing card details label 108-');    
  }));

  it('Updating the param object should add a row',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allCardDetailsEles = debugElement.queryAll(By.css('nm-card-details'));
    expect(allCardDetailsEles.length).toEqual(2);
    hostComponent.element.type.model.params.push(newElement);
    fixture.detectChanges();
    const newAllCardDetailsEles = debugElement.queryAll(By.css('nm-card-details'));
    expect(newAllCardDetailsEles.length).toEqual(3);
  }));

  it('nm-card-details should not be created if element?.type?.model?.params[0].type?.model?.params[0].config?.uiStyles?.attributes?.alias !== CardDetail',async(() => {
    hostComponent.element.type.model.params[0].type.model.params[0].config.uiStyles.attributes.alias = '';
    hostComponent.element.type.model.params[1].type.model.params[0].config.uiStyles.attributes.alias = '';
    hostComponent.element.type.model.params[2].type.model.params[0].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsComponentEle = debugElement.query(By.css('nm-card-details'));
    expect(cardDetailsComponentEle).toBeFalsy();
  }));

  it('Label should not be created on if element.labels is empty',async(() => {
    hostComponent.position = 1;
    ServiceConstants.LOCALE_LANGUAGE = "en-US";
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

const cardDetailsGridElement = {
    "config": {
        "active": false,
        "required": false,
        "id": "1475",
        "code": "vcdgConcerns",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.CardDetailsGrid",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "editUrl": "",
                "cssClass": "",
                "draggable": false,
                "alias": "CardDetailsGrid",
                "onLoad": true
            }
        },
        "type": {
            "collection": true,
            "nested": true,
            "name": "ArrayList",
            "collectionType": "list",
            "model": {
                "paramConfigIds": []
            },
            "elementConfig": {
                "id": "1478",
                "type": {
                    "collection": false,
                    "nested": true,
                    "name": "VPOwnerInfo.VSOwnerInfo",
                    "model": {
                        "paramConfigIds": [
                            "1480"
                        ]
                    }
                }
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "1475",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns",
    "type": {
        "model": {
            "params": [
                {
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "1478",
                    "collectionElem": true,
                    "elemId": "0",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "1480",
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
                                              "expandable": false,
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
                                                  "1482"
                                              ]
                                          }
                                      }
                                  },
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "1480",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "1482",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "1484",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/firstName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "test",
                                                                    "previousLeafState": "test",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "First Name"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "1485",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/lastName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "1",
                                                                    "previousLeafState": "1",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Last Name"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "1486",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/divider2",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
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
                                                                    "configId": "1487",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "1489",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/addressGroup/address",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Address"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "1490",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/addressGroup/city",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "City"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "1491",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/addressGroup/state",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "State"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "1492",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Zip"
                                                                                        }
                                                                                    ],
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
                                                                            "text": "Address Group"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "1493",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/telephone",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "1231231231",
                                                                    "previousLeafState": "1231231231",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Telephone"
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
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "testing card details label 108-"
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
                },
                {
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "1478",
                    "collectionElem": true,
                    "elemId": "1",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "1480",
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
                                              "expandable": false,
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
                                                  "1482"
                                              ]
                                          }
                                      }
                                  },
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "1480",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "1482",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "1484",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/firstName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "test",
                                                                    "previousLeafState": "test",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "First Name"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "1485",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/lastName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "1",
                                                                    "previousLeafState": "1",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Last Name"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "1486",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/divider2",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
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
                                                                    "configId": "1487",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "1489",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/address",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Address"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "1490",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/city",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "City"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "1491",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/state",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "State"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "1492",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Zip"
                                                                                        }
                                                                                    ],
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
                                                                            "text": "Address Group"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "1493",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/telephone",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "1231231231",
                                                                    "previousLeafState": "1231231231",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Telephone"
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
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "testing card details label 108-"
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
    "labels": [
        {
            "locale": "en-US",
            "text": "testing grid label-181"
        }
    ],
    "elemLabels": {}
  };
  
  const newElement = {
      "enabled": true,
      "visible": true,
      "activeValidationGroups": [],
      "collectionParams": [],
      "configId": "1478",
      "collectionElem": true,
      "elemId": "1",
      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1",
      "type": {
          "model": {
              "params": [
                  {
                      "config": {
                        "active": false,
                        "required": false,
                        "id": "1480",
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
                                "expandable": false,
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
                                    "1482"
                                ]
                            }
                        }
                    },
                      "enabled": true,
                      "visible": true,
                      "activeValidationGroups": [],
                      "collectionParams": [],
                      "configId": "1480",
                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo",
                      "type": {
                          "model": {
                              "params": [
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "1482",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "1484",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/firstName",
                                                      "type": {
                                                          "nested": false,
                                                          "name": "string",
                                                          "collection": false
                                                      },
                                                      "leafState": "test",
                                                      "previousLeafState": "test",
                                                      "message": [],
                                                      "values": [],
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "First Name"
                                                          }
                                                      ],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "1485",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/lastName",
                                                      "type": {
                                                          "nested": false,
                                                          "name": "string",
                                                          "collection": false
                                                      },
                                                      "leafState": "1",
                                                      "previousLeafState": "1",
                                                      "message": [],
                                                      "values": [],
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Last Name"
                                                          }
                                                      ],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "1486",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/divider2",
                                                      "type": {
                                                          "nested": false,
                                                          "name": "string",
                                                          "collection": false
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
                                                      "configId": "1487",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "1489",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/address",
                                                                      "type": {
                                                                          "nested": false,
                                                                          "name": "string",
                                                                          "collection": false
                                                                      },
                                                                      "leafState": "",
                                                                      "previousLeafState": "",
                                                                      "message": [],
                                                                      "values": [],
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "Address"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "1490",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/city",
                                                                      "type": {
                                                                          "nested": false,
                                                                          "name": "string",
                                                                          "collection": false
                                                                      },
                                                                      "leafState": "",
                                                                      "previousLeafState": "",
                                                                      "message": [],
                                                                      "values": [],
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "City"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "1491",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/state",
                                                                      "type": {
                                                                          "nested": false,
                                                                          "name": "string",
                                                                          "collection": false
                                                                      },
                                                                      "leafState": "",
                                                                      "previousLeafState": "",
                                                                      "message": [],
                                                                      "values": [],
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "State"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "1492",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
                                                                      "type": {
                                                                          "nested": false,
                                                                          "name": "string",
                                                                          "collection": false
                                                                      },
                                                                      "leafState": "",
                                                                      "previousLeafState": "",
                                                                      "message": [],
                                                                      "values": [],
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "Zip"
                                                                          }
                                                                      ],
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
                                                              "text": "Address Group"
                                                          }
                                                      ],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "1493",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/telephone",
                                                      "type": {
                                                          "nested": false,
                                                          "name": "string",
                                                          "collection": false
                                                      },
                                                      "leafState": "1231231231",
                                                      "previousLeafState": "1231231231",
                                                      "message": [],
                                                      "values": [],
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Telephone"
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
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "testing card details label 108-"
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
  };
