'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { By } from '@angular/platform-browser';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

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
import { ServiceConstants } from '../../../services/service.constants';
import { PageService } from '../../../services/page.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { AppInitService } from '../../../services/app.init.service';

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
      WebContentSvc,
      PageService,
      LoaderService,
      ConfigService,
      LoggerService
    ];

describe('CardDetailsFieldGroupComponent', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
   fixture = TestBed.createComponent(CardDetailsFieldGroupComponent);
   hostComponent = fixture.debugElement.componentInstance;
   hostComponent.element = cardDetailsFieldGroupElement as Param;;
  });

  it('should create the CardDetailsFieldGroupComponent',  async(() => {
    expect(hostComponent).toBeTruthy(); 
  }));

  it('Label should be created on providing the element.labelconfig display the value provided',async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle.name).toEqual('nm-input-label');
    expect(labelEle.nativeElement.innerText.toString().trim()).toEqual('Case ID');
  }));

  it('Label should not be created on if element.labelconfig is empty',async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.type.model.params[0].labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'))
    expect(labelEle.nativeElement.innerText.toString()).toEqual('');
  }));

  it('nm-card-details-field should be created if element?.type?.model?.params[0].config?.uiStyles?.attributes?.alias === FieldValue',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsFieldEle = debugElement.query(By.css('nm-card-details-field'));
    expect(cardDetailsFieldEle.name).toEqual('nm-card-details-field');
  }));

  it('nm-card-details-field should not be created if element?.type?.model?.params[0].config?.uiStyles?.attributes?.alias !== FieldValue',async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsFieldEle = debugElement.query(By.css('nm-card-details-field'));
    expect(cardDetailsFieldEle).toBeFalsy();
  }));

  it('another nm-card-details-field should be added on updating the param',async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.alias = 'FieldValue';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allCardDetailsFieldEles = debugElement.queryAll(By.css('nm-card-details-field'));
    expect(allCardDetailsFieldEles.length).toEqual(1);
    hostComponent.element.type.model.params.push(newElement);
    fixture.detectChanges();
    const newAllCardDetailsFieldEles = debugElement.queryAll(By.css('nm-card-details-field'));
    expect(newAllCardDetailsFieldEles.length).toEqual(2);
  }));

  it('getComponentClass() should return array [testClass]',  async(() => {
      hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass'
      const res = hostComponent.getComponentClass();
      expect(res).toEqual(['testClass']);
  }));

  it('getComponentClass() should return array []',  async(() => {
    hostComponent.element.config.uiStyles.attributes.cssClass = null;
    const res = hostComponent.getComponentClass();
    expect(res).toEqual([]);
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

const cardDetailsFieldGroupElement: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "3750",
      "code": "fgCardBodyCase1",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.FieldValueGroup",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "cssClass": "",
              "alias": "FieldValueGroup",
              "cols": "5"
          }
      },
      "type": {
          "collection": false,
          "nested": true,
          "name": "VPOwnerInfo.FieldGroup_CardBodyCase1",
          "model": {
              "paramConfigIds": [
                  "3752"
              ]
          }
      }
  },
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "3750",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/fgCardBodyCase1",
  "type": {
      "model": {
          "params": [
              {
                "config": {
                  "active": false,
                  "required": false,
                  "id": "3752",
                  "code": "id",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": false,
                      "isHidden": false,
                      "name": "ViewConfig.FieldValue",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": false,
                          "inplaceEditType": "",
                          "cssClass": "label-left align-right",
                          "datePattern": "",
                          "alias": "FieldValue",
                          "applyValueStyles": false,
                          "placeholder": "",
                          "inplaceEdit": false,
                          "type": "Field",
                          "cols": "1",
                          "imgSrc": ""
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  }
              },
                  "enabled": true,
                  "visible": true,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "3752",
                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/fgCardBodyCase1/id",
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
                          "text": "Case ID"
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
  ],
  "elemLabels": {}
};

const newElement =               {
  "config": {
    "active": false,
    "required": false,
    "id": "3752",
    "code": "id",
    "validations": null,
    "uiNatures": [],
    "uiStyles": {
        "isLink": false,
        "isHidden": false,
        "name": "ViewConfig.FieldValue",
        "attributes": {
            "hidden": false,
            "readOnly": false,
            "submitButton": true,
            "showName": true,
            "pageSize": 25,
            "browserBack": false,
            "showAsLink": false,
            "inplaceEditType": "",
            "cssClass": "label-left align-right",
            "datePattern": "",
            "alias": "FieldValue",
            "applyValueStyles": false,
            "placeholder": "",
            "inplaceEdit": false,
            "type": "Field",
            "cols": "1",
            "imgSrc": ""
        }
    },
    "type": {
        "collection": false,
        "nested": false,
        "name": "string"
    }
},
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "3752",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/fgCardBodyCase1/id",
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
            "text": "Case ID"
        }
    ],
    "elemLabels": {}
};