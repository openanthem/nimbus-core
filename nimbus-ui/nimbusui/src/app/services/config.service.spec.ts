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

import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ConfigService } from './config.service';
import { ViewRoot } from '../shared/app-config.interface';
import { ParamConfig, ConfigType, ModelConfig } from '../shared/param-config';
import { ViewConfig } from './../shared/param-annotations.enum';
import { Model } from '../shared/param-state';

let http, backend, service;

describe('ConfigService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ConfigService],
      imports: [ HttpClientTestingModule, HttpModule ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(ConfigService);
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('setLayoutToAppConfig() should update  flowConfigs object', async(() => {
    service.flowConfigs = { test: '' };
    service.setLayoutToAppConfig('test', 123);
    expect(service.flowConfigs.test).toEqual(123);
  }));


  it('setLayoutToAppConfigByModel() should update flowConfigs object', async(() => {
    service.flowConfigs = {
        test: ''
    };
    const tModel = new Model(service);
    service.setLayoutToAppConfigByModel('test', tModel);
    expect(service.flowConfigs.test).toBeTruthy();
  }));

  it('getFlowConfig should return flowConfigs object', async(() => {
    service.flowConfigs = {
        test: 456
    };
    expect(service.getFlowConfig('test')).toEqual(456);
    expect(service.getFlowConfig('test123')).toEqual(undefined);
  }));

  it('setViewConfigToParamConfigMap() should update paramConfigs', async(() => {
    service.setViewConfigToParamConfigMap('test', 123);
    expect(service.paramConfigs).toEqual({test: 123});
  }));

  it('getViewConfigById() should return paramConfigs[id]', async(() => {
    service.setViewConfigToParamConfigMap('test', 123);
    expect(service.getViewConfigById('test')).toEqual(123);
  }));

});