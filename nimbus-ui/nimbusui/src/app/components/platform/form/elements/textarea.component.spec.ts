'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, FormGroup, FormControl } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { KeyFilterModule } from 'primeng/keyfilter';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { TextArea } from './textarea.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { configureTestSuite, TestCtx } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { Param } from '../../../../shared/param-state';
import { By } from '@angular/platform-browser';
import { ServiceConstants } from '../../../../services/service.constants';

let fixture, hostComponent;

const declarations = [
  TextArea,
  TooltipComponent,
  InputLabel
 ];
const imports = [
  FormsModule,
  HttpClientModule,
  HttpModule,
  StorageServiceModule
 ];
const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  Location,
  SessionStoreService,
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  KeyFilterModule,
  LoggerService,
  AppInitService
 ];

describe('TextArea', () => {

  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TextArea);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = textAreaElement as Param;
    hostComponent.form = new FormGroup({
      notes: new FormControl()
   });
  });

  it('should create the TextArea', async(() =>  {
    expect(hostComponent).toBeTruthy();
  }));

  it('nm-input-label should be created if the label is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    console.log('labelConfig', hostComponent.labelConfig);
    
    const debugElement = fixture.debugElement;
    // const labelEle = debugElement.query(By.css('nm-input-label'));
    const labelEle = document.getElementsByTagName('nm-input-label');
    
    console.log('labelEle---82-aaaa', labelEle.length, labelEle[0]);
    expect(labelEle.length > 0).toBeTruthy();
    // expect(labelEle).toBeTruthy();
  }));

  it('textarea should be created if the readOnly attribute is configured as false', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const textareaEle = debugElement.query(By.css('textarea'));
    expect(textareaEle).toBeTruthy();
  }));

  it('span should be created if the @max is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const spanEle = debugElement.query(By.css('span'));
    expect(spanEle).toBeTruthy();
    expect(spanEle.nativeElement.innerText).toEqual('483 Characters left');
  }));

  it('textarea should not be created if the readOnly attribute is configured as true', async(() => {
    hostComponent.element.config.uiStyles.attributes.readOnly = true;
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const textareaEle = debugElement.query(By.css('textarea'));
    expect(textareaEle).toBeFalsy();
  }));


});

const textAreaElement: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "786",
      "code": "noteDescription1",
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.TextArea",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "help": "",
              "cssClass": "",
              "dataEntryField": true,
              "labelClass": "anthem-label",
              "alias": "TextArea",
              "controlId": "test",
              "type": "textarea",
              "rows": "5",
              "postEventOnChange": false,
              "cols": ""
          }
      },
      "type": {
          "collection": false,
          "nested": false,
          "name": "string"
      },
      "validation": {
          "constraints": [
              {
                  "name": "NotNull",
                  "attribute": {
                      "message": "Field is required.",
                      "groups": []
                  }
              },
              {
                "name": "Max",
                "attribute": {
                    "message": "Field does not meet min/max requirement.",
                    "value": 500,
                    "groups": []
                }
            }
          ]
      }
  },
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "786",
  "path": "/petview/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/noteDescription1",
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
          "text": "Note Description"
      }
  ],
  "elemLabels": {},
  "leafState": "testing leafstate"
}