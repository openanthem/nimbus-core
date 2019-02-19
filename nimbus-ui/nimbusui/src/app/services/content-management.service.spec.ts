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

import { WebContentSvc } from './content-management.service';
import { LabelConfig } from './../shared/param-config';
import { ServiceConstants } from './service.constants';
import { cmsParam } from 'mockdata';

let http, backend, service;

describe('WebContentSvc', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [WebContentSvc],
      imports: [ HttpClientTestingModule, HttpModule ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(WebContentSvc);
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('findLabelContent() should call findLabelContentFromConfig()', async(() => {
    spyOn(service, 'findLabelContentFromConfig').and.callThrough();
    service.findLabelContent(cmsParam);
    expect(service.findLabelContentFromConfig).toHaveBeenCalledWith(cmsParam.labels, cmsParam.config.code);
  }));

  it('findLabelContentFromConfig() should return labels', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    const labels = new LabelConfig();
    labels.locale = 'en-US';
    labels.text = 'Owners';
    const res = service.findLabelContentFromConfig(cmsParam.labels, cmsParam.config.code);
    expect(res).toEqual(labels);
  }));

  it('findLabelContent() should return undefined', async(() => {
    const param = {  };
    expect(service.findLabelContent(param)).toEqual(undefined);
  }));

});
