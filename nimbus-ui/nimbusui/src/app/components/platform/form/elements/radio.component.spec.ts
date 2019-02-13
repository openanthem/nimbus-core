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
import { RadioButtonModule } from 'primeng/primeng';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { RadioButton } from './radio.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLegend } from '../../../platform/form/elements/input-legend.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { Param } from '../../../../shared/param-state';
import { radioElement } from 'mockdata';
import { By } from '@angular/platform-browser';

let param: Param;

const declarations = [
  RadioButton,
  TooltipComponent,
  InputLegend
];
const imports = [
   RadioButtonModule,
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
   SessionStoreService,
   PageService,
   CustomHttpClient,
   LoaderService,
   ConfigService,
   LoggerService,
   NmMessageService,
   AppInitService
];

let fixture, hostComponent;

describe('RadioButton', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RadioButton);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = radioElement as Param;
  });

    it('should create the RadioButton', async(() => {
        expect(hostComponent).toBeTruthy();
    }));

    it('nm-input-legend should be created', async(() => {
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const inputLegendEle = debugElement.query(By.css('nm-input-legend'));
        expect(inputLegendEle).toBeTruthy();
    }));

    it('p-radioButton should be created', async(() => {
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const pRadioEle = debugElement.query(By.css('p-radioButton'));
        expect(pRadioEle).toBeTruthy();
    }));

});