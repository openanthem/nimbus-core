import { TestBed, inject, async } from '@angular/core/testing';
import { CustomHttpClient } from './httpclient.service';
import { HttpClientModule, HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JL } from 'jsnlog';
import { SESSION_STORAGE, StorageServiceModule } from 'angular-webstorage-service';
import { of as observableOf,  Observable } from 'rxjs';


import { FileService } from './file.service';
import { LoggerService } from './logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from './session.store';
import { AppInitService } from './app.init.service';

let http, backend, service, mHttpClient, logger;

class MockHttpClient {
  constructor() {  }
  
  postFileData(a, b) {

    if(a=='/test') {      
       let err = new Error("why now");
      throw err;
    }

    return observableOf({fileId: 123});
  }
}

class MockLoggerService {
  debug() { }
  info() { }
  error() { }
}

describe('FileService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
        imports: [
            HttpModule,
            HttpClientModule,
            HttpClientTestingModule,
            StorageServiceModule
        ],
        providers: [
          { provide: 'JSNLOG', useValue: JL },
          { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
          {provide: CustomHttpClient, useClass: MockHttpClient},
          {provide: LoggerService, useClass: MockLoggerService},
          FileService, 
          AppInitService
        ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(FileService);
    mHttpClient = TestBed.get(CustomHttpClient);
    logger = TestBed.get(LoggerService);
  });

  it('FileService should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('uploadFile() should post the file', async(() => {
    const testFile = new File([], 'tFile');
    testFile['postUrl'] = '/postFile';
    service.metaData = [];
    service.uploadFile(testFile, {value: {}}).subscribe(val => {
      expect(val).toEqual(123);
    });
  }));

});