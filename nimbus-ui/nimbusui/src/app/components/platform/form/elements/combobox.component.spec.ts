'use strict';
import { TestBed, async } from '@angular/core/testing';
import { DropdownModule } from 'primeng/primeng';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { ComboBox } from './combobox.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { SelectItemPipe } from '../../../../pipes/select-item.pipe';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { Param } from '../../../../shared/param-state';
import { fieldValueParam } from 'mockdata';
import { ServiceConstants } from '../../../../services/service.constants';
import { By } from '@angular/platform-browser';


const declarations = [
  ComboBox,
  TooltipComponent,
  SelectItemPipe,
  InputLabel
 ];
 const imports = [
  DropdownModule,
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
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  LoggerService,
  SessionStoreService,
  AppInitService
 ];
 let fixture, hostComponent;
describe('ComboBox', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComboBox);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = comboBoxElement as Param;
  });

  it('should create the ComboBox', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  // ngoninit method in base control is nopt getting triggered
  it('nm-input-label should be created if the label is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.visible = true;
    fixture.detectChanges();
    // fixture.whenStable().then(() => {
      console.log('isLabelEmpty', hostComponent.isLabelEmpty);
      console.log('showLabel', hostComponent.showLabel);      
      const debugElement = fixture.debugElement;
    //   const labelEle = debugElement.query(By.css('nm-input-label'));
      const labelEle = document.getElementsByTagName('nm-input-label');
      console.log('labelEle---84', labelEle);
      
      expect(labelEle.length > 0).toBeTruthy();
    // });
  }));

  it('p-dropdown should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pDropDownEle = debugElement.query(By.css('p-dropdown'));
    expect(pDropDownEle).toBeTruthy();
  }));

  it('nm-input-label should not be created if the label is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle).toBeFalsy();
  }));

});

const comboBoxElement: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "895",
      "code": "q2_b",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.ComboBox",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "help": "",
              "postButtonUrl": "",
              "cssClass": "",
              "dataEntryField": true,
              "labelClass": "anthem-label",
              "alias": "ComboBox",
              "controlId": "",
              "postEventOnChange": true,
              "cols": ""
          }
      },
      "type": {
          "collection": false,
          "nested": false,
          "name": "string"
      }
  },
  "enabled": false,
  "visible": false,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "895",
  "path": "/petassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petForm/petFormBody/petAssessment_Accordion_tab/q2/q2_b",
  "type": {
      "nested": false,
      "name": "string",
      "collection": false
  },
  "message": [],
  "values": [
      {
          "code": "Internet Search",
          "label": "Internet Search"
      },
      {
          "code": "Blog Post",
          "label": "Blog Post"
      },
      {
          "code": "Colleague",
          "label": "Colleague"
      },
      {
          "code": "Friend",
          "label": "Friend"
      },
      {
          "code": "Other",
          "label": "Other"
      }
  ],
  "labels": [
      {
          "locale": "en-US",
          "text": "How did you hear about the Pet Clinic?"
      }
  ],
  "elemLabels": {}
};