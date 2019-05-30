/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

import { HttpClient, HttpClientModule } from '@angular/common/http';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { of as observableOf } from 'rxjs';
import { AppInitService } from './app.init.service';
import { FileService } from './file.service';
import { CustomHttpClient } from './httpclient.service';
import { LoggerService } from './logger.service';
import { CUSTOM_STORAGE } from './session.store';

let http, backend, service, mHttpClient, logger;

class MockHttpClient {
  constructor() {}

  postFileData(a, b) {
    if (a == '/test') {
      let err = new Error('why now');
      throw err;
    }

    return observableOf({ fileId: 123 });
  }
}

class MockLoggerService {
  debug() {}
  info() {}
  error() {}
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
        { provide: CustomHttpClient, useClass: MockHttpClient },
        { provide: LoggerService, useClass: MockLoggerService },
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
    service.uploadFile(testFile, { value: {} }).subscribe(val => {
      expect(val).toEqual(123);
    });
  }));
});
