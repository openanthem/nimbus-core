'use strict';
import { TestBed, async } from '@angular/core/testing';
import { CalendarModule } from 'primeng/primeng';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { WebContentSvc } from '../../../../services/content-management.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { Param } from '../../../../shared/param-state';
import { ServiceConstants } from '../../../../services/service.constants';
import { By } from '@angular/platform-browser';
import { WindowRefService } from '../../../../services/window-ref.service';

const declarations = [
  InputLabel,
  TooltipComponent,
  InputLabel
 ];
 const imports = [
  CalendarModule,
  FormsModule,
  HttpModule,
  HttpClientModule,
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
  AppInitService,
  WebContentSvc,
  WindowRefService
 ];

 let fixture, hostComponent;

describe('InputLabel', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(InputLabel);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = labelElement as Param;
  });

  it('should create the InputLabel', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('label should be created if the label is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('label'));
    expect(labelEle).toBeTruthy();
  }));

  it('nm-tooltip should be created if helpText is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const tooltipEle = debugElement.query(By.css('nm-tooltip'));
    expect(tooltipEle).toBeTruthy();
  }));

  it('nm-tooltip should not be created if helpText is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const tooltipEle = debugElement.query(By.css('nm-tooltip'));
    expect(tooltipEle).toBeFalsy();
  }));

  it('label should not be created if the label is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('label'));
    expect(labelEle).toBeFalsy();
  }));

  it('getCssClass() should return required', async(() => {
    hostComponent.required = true;
    expect(hostComponent.getCssClass()).toEqual('required');
  }));

});

const labelElement: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "513",
      "code": "firstName",
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
              "cssClass": "",
              "datePattern": "",
              "alias": "FieldValue",
              "applyValueStyles": true,
              "placeholder": "",
              "inplaceEdit": true,
              "type": "Field",
              "cols": "2",
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
  "configId": "513",
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
          "text": "First Name---127...",
          "helpText": "testing tooltip"
      }
  ],
  "elemLabels": {}
};