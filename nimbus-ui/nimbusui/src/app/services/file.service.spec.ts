import { TestBed, inject, async } from '@angular/core/testing';
import { CustomHttpClient } from './httpclient.service';
import { HttpClientModule, HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { FileService } from './file.service';
import { LoggerService } from './logger.service';

let http, backend, service;

describe('FileService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
        imports: [
            HttpModule,
            HttpClientModule,
            HttpClientTestingModule
        ],
        providers: [
          FileService, 
          CustomHttpClient,
          LoggerService
        ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(FileService);
  });

  it('FileService should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('uploadFile(), post call should throw error', async(() => {
    const testFile = new File([], 'tFile');
    testFile['postUrl'] = '/postFile';
    service.uploadFile(testFile);

    const req = backend.expectOne(testFile['postUrl']);
    expect(req.request.method).toEqual('POST');
    req.flush({errorMessage: 'Uh oh!'}, { status: 500, statusText: 'Server Error'});
    backend.verify();
  }));

  it('uploadFile() should post the file', async(() => {
    const testFile = new File([], 'tFile');
    testFile['postUrl'] = '/postFile';
    service.uploadFile(testFile);
    const req = backend.expectOne(testFile['postUrl']);
    expect(req.request.method).toEqual('POST');
    req.flush(123);
    backend.verify();
  }));

});