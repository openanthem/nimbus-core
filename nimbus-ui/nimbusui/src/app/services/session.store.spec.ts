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

import { SessionStoreService, CUSTOM_STORAGE } from './session.store';

let http, backend, service;

describe('SessionStoreService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
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
    service = TestBed.get(SessionStoreService);
  });

    it('should be created', async(() => {
      expect(service).toBeTruthy();
    }));

    it('set() should set the value for the id provided', async(() => {
      service.set('test', 123);
      expect(service.get('test')).toEqual(123);
    }));

    it('remove() should remove the value of provided id', async(() => {
      service.set('test', 123);
      service.remove('test');
      expect(service.get('test')).toEqual(null);
    }));

    it('removeAll() should remove all the id and values', async(() => {
      service.set('test', 123);
      service.removeAll();
      expect(service.get('test')).toEqual(null);
    }));

    it('setSessionId() should update the value of sessionId', async(() => {
      service.setSessionId('test');
      expect(service.get('sessionId')).toEqual('test');
    }));

    it('setSessionId should call the removeAll()', async(() => {
      spyOn(service, 'removeAll').and.callThrough();
      service.set('sessionId', 1);
      service.setSessionId('');
      expect(service.removeAll).toHaveBeenCalled();
    }));

});