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