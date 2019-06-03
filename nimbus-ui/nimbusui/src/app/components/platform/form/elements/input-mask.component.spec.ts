/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

'use strict';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { By } from '@angular/platform-browser';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
// import { inputMaskElement } from './../../../../mockdata/input-mask.component.mockdata.spec';
import { inputMaskElement } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import {
  AccordionModule,
  CalendarModule,
  CheckboxModule,
  DataTableModule,
  DialogModule,
  DragDropModule,
  DropdownModule,
  FileUploadModule,
  GrowlModule,
  InputMaskModule,
  InputSwitchModule,
  ListboxModule,
  OverlayPanelModule,
  PickListModule,
  ProgressBarModule,
  ProgressSpinnerModule,
  RadioButtonModule,
  SharedModule,
  TreeTableModule
} from 'primeng/primeng';
import { Subject } from 'rxjs';
import { AppInitService } from '../../../../services/app.init.service';
import { ConfigService } from '../../../../services/config.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { LoggerService } from '../../../../services/logger.service';
import { PageService } from '../../../../services/page.service';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from '../../../../services/session.store';
import { setup } from '../../../../setup.spec';
import { TooltipComponent } from '../../tooltip/tooltip.component';
import { CounterMessageService } from './../../../../services/counter-message.service';
import { ServiceConstants } from './../../../../services/service.constants';
import { Param } from './../../../../shared/param-state';
import { InputLabel } from './input-label.component';
import { InputMaskComp } from './input-mask.component';
import { InputSwitch } from './input-switch.component';

let pageService;

class MockPageService {
  eventUpdate$: Subject<any>;

  constructor() {
    this.eventUpdate$ = new Subject();
  }
  postOnChange(a, b, c) {}
  logError(a) {
    this.eventUpdate$.next(a);
  }
}

const declarations = [InputSwitch, InputLabel, TooltipComponent, InputMaskComp];
const imports = [
  HttpModule,
  HttpClientTestingModule,
  StorageServiceModule,
  DataTableModule,
  SharedModule,
  OverlayPanelModule,
  PickListModule,
  DragDropModule,
  CalendarModule,
  FileUploadModule,
  ListboxModule,
  DialogModule,
  CheckboxModule,
  DropdownModule,
  RadioButtonModule,
  ProgressBarModule,
  ProgressSpinnerModule,
  AccordionModule,
  GrowlModule,
  InputSwitchModule,
  TreeTableModule,
  FormsModule,
  InputMaskModule
];
const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: PageService, useClass: MockPageService },
  CustomHttpClient,
  LoaderService,
  ConfigService,
  LoggerService,
  AppInitService,
  SessionStoreService,
  CounterMessageService
];
let fixture, hostComponent;

describe('InputMaskComp', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InputMaskComp);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = inputMaskElement as Param;
    pageService = TestBed.get(PageService);
  });

  it('should create the InputMaskComp', async(() => {
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

  it('p-inputMask should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pInputMaskElement = debugElement.query(By.css('p-inputMask'));
    expect(pInputMaskElement).toBeTruthy();
  }));
});
