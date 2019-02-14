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


import { Param } from './../../../../shared/param-state';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Subject } from 'rxjs';
import { of as observableOf,  Observable } from 'rxjs';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule } from 'primeng/primeng';
import { FormsModule } from '@angular/forms';
import { TooltipComponent } from '../../tooltip/tooltip.component';

import { InputLabel } from './input-label.component';
import { InputSwitch } from './input-switch.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { inputSwitchElement } from 'mockdata';
import { By } from '@angular/platform-browser';
import { ServiceConstants } from '../../../../services/service.constants';

let pageService;

class MockPageService {
    eventUpdate$: Subject<any>;

    constructor() {
        this.eventUpdate$ = new Subject();
    }
    postOnChange(a, b, c) { }
    logError(a) {
        this.eventUpdate$.next(a);
    }
}

const declarations = [
  InputSwitch,
  InputLabel,
  TooltipComponent
 ];
const imports = [
     HttpModule,
     HttpClientTestingModule,
     StorageServiceModule,
     DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule,
FormsModule
 ];
const providers = [
    { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
    { provide: 'JSNLOG', useValue: JL },
    {provide: PageService, useClass: MockPageService},
     CustomHttpClient,
     LoaderService,
     ConfigService,
     LoggerService,
     AppInitService,
     SessionStoreService
 ];
 let fixture, hostComponent;

describe('InputSwitch', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InputSwitch);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = inputSwitchElement as Param;
    pageService = TestBed.get(PageService);
  });

  it('should create the InputSwitch', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('nm-input-label should be created if the label is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle).toBeTruthy();
  }));

  it('nm-input-label should not be created if the label is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle).toBeFalsy();
  }));

  it('p-inputSwitch should be created', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pinputSwitchEle = debugElement.query(By.css('p-inputSwitch'));
    expect(pinputSwitchEle).toBeTruthy();
  }));

  it('orientation should be left', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.orientation = 'LEFT';
      expect(hostComponent.orientation).toEqual('left');
    });
  });

  it('orientation should be right', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.orientation = 'RIGHT';
      expect(hostComponent.orientation).toEqual('right');
    });
  });

  it('orientation should be empty string', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.orientation = '';
      expect(hostComponent.orientation).toEqual('');
    });
  });

});

