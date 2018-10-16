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