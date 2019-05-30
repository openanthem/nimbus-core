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
import {
  HashLocationStrategy,
  Location,
  LocationStrategy
} from '@angular/common';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { fieldValueParam } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
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
import { CounterMessageService } from './../../../../services/counter-message.service';
import { NmMessageService } from './../../../../services/toastmessage.service';
import { FilterButton } from './filter-button.component';

let pageService, configService;

const declarations = [FilterButton];
const imports = [HttpModule, HttpClientTestingModule, StorageServiceModule];
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
  NmMessageService,
  AppInitService,
  CounterMessageService
];
let fixture, hostComponent;
describe('FilterButton', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterButton);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
    pageService = TestBed.get(PageService);
    configService = TestBed.get(ConfigService);
  });

  it('should create the FilterButton', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  // it('ngOnInit() should call loadLabelConfig()', async(() => {
  //   const spy = spyOn(hostComponent as any, 'loadLabelConfig').and.returnValue('');
  //   const config = new ParamConfig(configService);
  //   config.uiStyles = new UiStyle();
  //   config.uiStyles.attributes = new UiAttribute();
  //   config.uiStyles.attributes.alias = 'Button';
  //   spyOn(configService, 'getViewConfigById').and.returnValue(config);
  //   hostComponent.filterButton = new Param(configService);
  //   const nestedParam = new Param(configService);
  //   hostComponent.filterButton.type = new Type(configService);
  //   hostComponent.filterButton.type.model = new Model(configService);
  //   hostComponent.filterButton.type.model.params = [nestedParam];
  //   hostComponent.ngOnInit();
  //   expect(spy).toHaveBeenCalled();
  // }));

  // it('ngOnInit() should update the fText property', async(() => {
  //   spyOn(hostComponent as any, 'loadLabelConfig').and.returnValue('');
  //   const config = new ParamConfig(configService);
  //   config.uiStyles = new UiStyle();
  //   config.uiStyles.attributes = new UiAttribute();
  //   config.uiStyles.attributes.alias = 'TextBox';
  //   spyOn(configService, 'getViewConfigById').and.returnValue(config);
  //   hostComponent.filterButton = new Param(configService);
  //   const nestedParam = new Param(configService);
  //   hostComponent.filterButton.type = new Type(configService);
  //   hostComponent.filterButton.type.model = new Model(configService);
  //   hostComponent.filterButton.type.model.params = [nestedParam];
  //   const test = new Param(configService);
  //   config.uiStyles = new UiStyle();
  //   config.uiStyles.attributes = new UiAttribute();
  //   config.uiStyles.attributes.alias = 'TextBox';
  //   hostComponent.ngOnInit();
  //   expect(hostComponent.fText).toEqual(test);
  // }));

  // it('ngOnInit() should subscribe to buttonClickEvent', async(() => {
  //   spyOn(hostComponent.buttonClickEvent, 'subscribe').and.callThrough();
  //   hostComponent.filterButton = new Param(configService);
  //   hostComponent.filterButton.type = new Type(configService);
  //   hostComponent.filterButton.type.model = new Model(configService);
  //   hostComponent.filterButton.type.model.params = [];
  //   hostComponent.ngOnInit();
  //   expect(hostComponent.buttonClickEvent.subscribe).toHaveBeenCalled();
  // }));

  // it('ngOnInit() should call pageservice.processEvent()', async(() => {
  //   spyOn(pageService, 'processEvent').and.returnValue('');
  //   hostComponent.filterButton = new Param(configService);
  //   hostComponent.filterButton.type = new Type(configService);
  //   hostComponent.filterButton.type.model = new Model(configService);
  //   hostComponent.filterButton.type.model.params = [];
  //   hostComponent.ngOnInit();
  //   const eve = { fbutton: { path: '', config: { uiStyles: { attributes: { b: '', method: '' } } } } };
  //   hostComponent.buttonClickEvent.emit(eve);
  //   expect(pageService.processEvent).toHaveBeenCalled();
  // }));

  it('emitEvent() should call buttonClickEvent.emit()', async(() => {
    spyOn(hostComponent.buttonClickEvent, 'emit').and.callThrough();
    hostComponent.emitEvent('test');
    expect(hostComponent.buttonClickEvent.emit).toHaveBeenCalled();
  }));
});
