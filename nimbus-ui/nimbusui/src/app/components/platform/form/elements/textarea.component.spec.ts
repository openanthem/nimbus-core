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

import { NmMessageService } from './../../../../services/toastmessage.service';
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
import { textAreaElement } from 'mockdata';

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
  NmMessageService,
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

    it("should create the TextArea", async(() => {
      expect(hostComponent).toBeTruthy();
    }));

    it("nm-input-label should be created if the label is configured", async(() => {
      ServiceConstants.LOCALE_LANGUAGE = "en-US";
      fixture.detectChanges();
      const labelEle = document.getElementsByTagName("nm-input-label");
      expect(labelEle.length).toEqual(1);
    }));

    it("nm-input-label should not be created if the label is not configured", async(() => {
      hostComponent.element.labels = [];
      ServiceConstants.LOCALE_LANGUAGE = "en-US";
      fixture.detectChanges();
      const labelEle = document.getElementsByTagName("nm-input-label");
      expect(labelEle.length).toEqual(0);
    }));

    it("textarea should be created if the readOnly attribute is configured as false", async(() => {
      ServiceConstants.LOCALE_LANGUAGE = "en-US";
      fixture.detectChanges();
      const debugElement = fixture.debugElement;
      const textareaEle = debugElement.query(By.css("textarea"));
      expect(textareaEle).toBeTruthy();
    }));

    it("span should be created if the @max is configured", async(() => {
      ServiceConstants.LOCALE_LANGUAGE = "en-US";
      fixture.detectChanges();
      const debugElement = fixture.debugElement;
      const spanEle = debugElement.query(By.css("span"));
      expect(spanEle).toBeTruthy();
      expect(spanEle.nativeElement.innerText).toEqual("483 Characters left");
    }));

    it("span should not be created if the @max is not configured", async(() => {
      hostComponent.element.config.validation.constraints = [];
      ServiceConstants.LOCALE_LANGUAGE = "en-US";
      fixture.detectChanges();
      const debugElement = fixture.debugElement;
      const spanEle = debugElement.query(By.css("span"));
      expect(spanEle).toBeFalsy();
    }));

    it("pre should be created if the readOnly is configured as false", async(() => {
      fixture.detectChanges();
      const debugElement = fixture.debugElement;
      const preEle = debugElement.query(By.css("pre"));
      expect(preEle).toBeTruthy();
      expect(preEle.nativeElement.innerText).toEqual(hostComponent.element.leafState);
    }));

    it("p should be created and display leafState if the leafState is configured and readOnly is true", async(() => {
      hostComponent.element.config.uiStyles.attributes.readOnly = true;
      fixture.detectChanges();
      const debugElement = fixture.debugElement;
      const pEle = debugElement.query(By.css("p"));
      expect(pEle.nativeElement.innerText).toEqual("testing leafstate");
    }));

    it('p should not be created if the readOnly is configured as false', async(() => {
        hostComponent.element.config.uiStyles.attributes.readOnly = false;
        fixture.detectChanges();
        const pEles = document.getElementsByTagName('p');
        expect(pEles.length).toEqual(0);
    }));

    it("pre should not be created if the readOnly is configured as true", async(() => {
      hostComponent.element.config.uiStyles.attributes.readOnly = true;
      fixture.detectChanges();
      const debugElement = fixture.debugElement;
      const preEle = debugElement.query(By.css("pre"));
      expect(preEle).toBeFalsy();
    }));

    it("div should be created if the controlId is configured", async(() => {
      fixture.detectChanges();
      const debugElement = fixture.debugElement;
      const divEles = debugElement.queryAll(By.css("div"));
      expect(divEles[1].nativeElement.innerText).toEqual("test");
      expect(divEles[1].nativeElement.classList[0]).toEqual("number");
      expect(divEles.length).toEqual(2);
    }));

    it("div should not be created if the controlId is not configured", async(() => {
      hostComponent.element.config.uiStyles.attributes.controlId = "";
      fixture.detectChanges();
      const debugElement = fixture.debugElement;
      const divEles = debugElement.queryAll(By.css("div"));
      expect(divEles.length).toEqual(1);
    }));

    it("textarea should not be created if the readOnly attribute is configured as true", async(() => {
      hostComponent.element.config.uiStyles.attributes.readOnly = true;
      ServiceConstants.LOCALE_LANGUAGE = "en-US";
      fixture.detectChanges();
      const debugElement = fixture.debugElement;
      const textareaEle = debugElement.query(By.css("textarea"));
      expect(textareaEle).toBeFalsy();
    }));

});
