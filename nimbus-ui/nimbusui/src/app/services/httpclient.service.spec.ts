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

import { CustomHttpClient } from './httpclient.service';
import { SessionStoreService, CUSTOM_STORAGE } from './session.store';

let backend, service;

describe('CustomHttpClient', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        CustomHttpClient,
        SessionStoreService
    ],
      imports: [ 
          HttpClientTestingModule,
          StorageServiceModule
        ]
    });
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(CustomHttpClient);
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('get() should make GET call', async(() => {
    service.get('/test', {test: 123}).subscribe (data => {
    });
    const req = backend.expectOne('/test');
    expect(req.request.method).toEqual('GET');
    req.flush(123);
    backend.verify();
  }));

  it('postFileData() should make a POST call', async(() => {
    service.postFileData('/testing', {}).subscribe (data => {
    });
  const req = backend.expectOne ( r=> {
    return true;
  });
    req.flush(123);
    backend.verify();
  }));

  it('post() should make a POST call', async(() => {
    service.post('/test', {test: 123}).subscribe (data => {
    });
    const req = backend.expectOne('/test');
    expect(req.request.method).toEqual('POST');
    req.flush(123);
    backend.verify();
  }));

  it('getCookie(test:123=t) should return undefined', async(() => {
    expect(service.getCookie('test:123=t')).toBeFalsy();
  }));

});