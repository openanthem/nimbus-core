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

import { TestBed, inject, async, tick, fakeAsync } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule, BaseRequestOptions, JsonpModule, Jsonp } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import {MockBackend} from '@angular/http/testing';
import { Http, Headers, RequestOptions, Response, ResponseOptions, ResponseType, Request } from '@angular/http';
import { JL } from 'jsnlog';
import { SESSION_STORAGE, StorageServiceModule } from 'angular-webstorage-service';

import { LoginSvc } from './login.service';
import { LoggerService } from './logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from './session.store';
import { AppInitService } from './app.init.service';

let http, backend, service;

class MockError extends Response implements Error {
  name:any
  message:any
}

class MockLoggerService {
  debug() { }
  info() { }
  error() { }
}

describe('LoginSvc', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: 'JSNLOG', useValue: JL },
        {
          provide: Jsonp,
          useFactory: (backend, options) => new Jsonp(backend, options),
          deps: [MockBackend, BaseRequestOptions]
        },
        {
          provide: Http,
          deps: [MockBackend, RequestOptions],
          useFactory: (mockBackend, requestOptions) => {
              return new Http(mockBackend, requestOptions);
          }
      },
      { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
      {provide: LoggerService, useClass: MockLoggerService},
      LoginSvc,
      MockBackend,
      BaseRequestOptions,
      AppInitService
      ],
      imports: [ HttpClientTestingModule, HttpModule, JsonpModule, StorageServiceModule]
    });
    http = TestBed.get(HttpClient);
    service = TestBed.get(LoginSvc);
    backend = TestBed.get(MockBackend);
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

 it('login() should update login$ subject', fakeAsync(() => {
    let response = {  test: 778  };

    // When the request subscribes for results on a connection, return a fake response
    backend.connections.subscribe(connection => {
      connection.mockRespond(new Response(<ResponseOptions>{
        body: JSON.stringify(response)
      }));
    });

    spyOn(service.login$, 'next').and.callThrough();

    // Perform a request and make sure we get the response we expect
    service.login('test', '123', true);
    tick();
    expect(service.login$.next).toHaveBeenCalled();
  }));

  it('login() should call logError()', fakeAsync(() => {
    let body = JSON.stringify({key:'val'});
    let opts = {type:ResponseType.Error, status:404, body: body};
    let responseOpts = new ResponseOptions(opts);
    backend.connections.subscribe(connection => {
      connection.mockError(new MockError(responseOpts));
    });
    spyOn(service, 'logError').and.callThrough();
    service.login('test', '123', true);
    tick();
    expect(service.logError).toHaveBeenCalled();
  }));


});