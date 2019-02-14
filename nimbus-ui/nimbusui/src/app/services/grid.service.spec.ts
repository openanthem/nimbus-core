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
import { SESSION_STORAGE, StorageServiceModule } from 'angular-webstorage-service';

import { GridService } from './grid.service';
import { CustomHttpClient } from './httpclient.service';
import { SessionStoreService, CUSTOM_STORAGE } from './session.store';

let http, backend, service;

describe('GridService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
          GridService, 
          CustomHttpClient,
          SessionStoreService
        ],
      imports: [ 
          HttpClientTestingModule, 
          HttpModule,
          StorageServiceModule
        ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(GridService);
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('setSummaryObject() should update eventupdate subject', async(() => {
    let res;
    service.eventUpdate.subscribe(val => {
        res = val;
    });
    service.setSummaryObject({test: 123});
    expect(res).toEqual({test: 123});
  }));

});