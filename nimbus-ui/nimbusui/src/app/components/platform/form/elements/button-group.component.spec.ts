'use strict';
import { TestBed, async } from '@angular/core/testing';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { ButtonGroup } from './button-group.component';
// import { Button } from './button.component';
import { SvgComponent } from '../../svg/svg.component';
import { Image } from '../../image.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { FormControl, FormGroup } from '@angular/forms';
import { By } from '@angular/platform-browser';

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
  ButtonGroup,
  Button,
  SvgComponent,
  Image
 ];
 const imports = [
   AngularSvgIconModule
 ];
 const providers = []
 let fixture, hostComponent;
describe('ButtonGroup', () => {

  configureTestSuite();
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(ButtonGroup);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.buttonList = buttonList;
    hostComponent.cssClass = 'text-sm-center';
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
      txt1: new FormControl()
   });
  });

  it('should create the ButtonGroup', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('nm-button should be created if element.config?.uiStyles?.attributes?.alias == Button', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('nm-button'));
    expect(buttonEle).toBeTruthy();
  }));

  it('On updating the param the button need to be added', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allButtonEle = debugElement.queryAll(By.css('nm-button'));
    expect(allButtonEle.length).toEqual(1);
    hostComponent.buttonList.push(buttonList[0]);
    fixture.detectChanges();
    const updatedAllButtonEle = debugElement.queryAll(By.css('nm-button'));
    expect(updatedAllButtonEle.length).toEqual(2);
  }));

  it('nm-button should not be created if element.config?.uiStyles?.attributes?.alias !== Button', async(() => {
    hostComponent.buttonList[0].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('nm-button'));
    expect(buttonEle).toBeFalsy();
  }));

});

const buttonList: any = [
  {
      "config": {
        "active": false,
        "required": false,
        "id": "50003",
        "code": "cancel",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.Button",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "b": "$execute",
                "method": "GET",
                "formReset": true,
                "type": "button",
                "title": "",
                "url": "",
                "printPath": "",
                "cssClass": "",
                "payload": "",
                "alias": "Button",
                "style": "SECONDARY",
                "imgSrc": "",
                "imgType": "FA"
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
      "configId": "50003",
      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vbg1/cancel",
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
              "text": "Cancel"
          }
      ],
      "elemLabels": {}
  }
];