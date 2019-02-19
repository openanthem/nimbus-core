import { TestBed, async } from '@angular/core/testing';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JL } from 'jsnlog';
import { SESSION_STORAGE, StorageServiceModule } from 'angular-webstorage-service';
import { Location } from '@angular/common';

import { CustomHttpClient } from './httpclient.service';
import { LoaderService } from './loader.service';
import { LoggerService } from './logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from './session.store';

import { PrintService } from './print.service';

let http, backend, service;

class MockLocation {
  back() { }
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
        { provide: Location, useClass: MockLocation},
        PrintService,
        CustomHttpClient
      ],
      imports: [HttpClientTestingModule, HttpModule, StorageServiceModule, HttpClientTestingModule]
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
        const uiEvent: any = { "isTrusted": true };
        const printConfig: any = { "autoPrint": true, "closeAfterPrint": true, "delay": 300, "useAppStyles": false, "useDelay": true };
        spyOn(service.printClickUpdate, 'next').and.callThrough();
        service.emitPrintEvent(printPath, uiEvent, printConfig);
        expect(service.printClickUpdate.next).toHaveBeenCalledWith({
            path: printPath,
            uiEvent: uiEvent,
            printConfig: printConfig
        });
    }));

}); 
