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
import { Location } from '@angular/common';
import { HttpClient } from '@angular/common/http';
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
import { CustomHttpClient } from './httpclient.service';
import { LoaderService } from './loader.service';
import { LoggerService } from './logger.service';
import { PrintService } from './print.service';
import { CUSTOM_STORAGE, SessionStoreService } from './session.store';

let http, backend, service;

class MockLocation {
  back() {}
}

class MockLoggerService {
  error(a) {}
  info(a) {}
  debug(a) {}
  warn(a) {}
}

class MockSessionStoreService {
  setSessionId(a) {}
  get(a) {
    if (a === 'test1') {
      return null;
    }
    return 'test';
  }
  set(a, b) {}
}

class MockLoaderService {
  show() {}
  hide() {}
}

describe('PrintService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: 'JSNLOG', useValue: JL },
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        { provide: LoggerService, useClass: MockLoggerService },
        { provide: SessionStoreService, useClass: MockSessionStoreService },
        { provide: LoaderService, useClass: MockLoaderService },
        { provide: Location, useClass: MockLocation },
        PrintService,
        CustomHttpClient
      ],
      imports: [
        HttpClientTestingModule,
        HttpModule,
        StorageServiceModule,
        HttpClientTestingModule
      ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(PrintService);
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('emitPrintEvent() should update the printClickUpdate subject', async(() => {
    const printPath = 'printPth /ownerlandingview/vpOwners';
    const uiEvent: any = { isTrusted: true };
    const printConfig: any = {
      autoPrint: true,
      closeAfterPrint: true,
      delay: 300,
      useAppStyles: false,
      useDelay: true
    };
    spyOn(service.printClickUpdate, 'next').and.callThrough();
    service.emitPrintEvent(printPath, uiEvent, printConfig);
    expect(service.printClickUpdate.next).toHaveBeenCalledWith({
      path: printPath,
      uiEvent: uiEvent,
      printConfig: printConfig
    });
  }));
});
