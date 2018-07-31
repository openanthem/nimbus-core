import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JL } from 'jsnlog';
import { SESSION_STORAGE, StorageServiceModule } from 'angular-webstorage-service';

import { LoggerService } from './logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from './session.store';
import { AppInitService } from './app.init.service';

let http, backend, service;

describe('LoggerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          { provide: 'JSNLOG', useValue: JL },
          { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
          LoggerService,
          SessionStoreService,
          AppInitService
        ],
      imports: [ 
          HttpClientTestingModule, 
          HttpModule,
          StorageServiceModule
        ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(LoggerService);
  });

    it('should be created', async(() => {
      expect(service).toBeTruthy();
    }));

    it('info() should call write()', async(() => {
      spyOn(service, 'write').and.returnValue('');
      service.info('');
      expect(service.write).toHaveBeenCalled();
    }));

    it('warn() should call write()', async(() => {
      spyOn(service, 'write').and.returnValue('');
      service.warn('');
      expect(service.write).toHaveBeenCalled();
    }));

    it('error() should call write()', async(() => {
      spyOn(service, 'write').and.returnValue('');
      service.error('');
      expect(service.write).toHaveBeenCalled();
    }));

    it('debug() should call write()', async(() => {
      spyOn(service, 'write').and.returnValue('');
      service.debug('');
      expect(service.write).toHaveBeenCalled();
    }));

    it('trace() should call write()', async(() => {
      spyOn(service, 'write').and.returnValue('');
      service.trace('');
      expect(service.write).toHaveBeenCalled();
    }));

    it('write() should call JL()', async(() => {
      service.promiseDone = true;
      spyOn(service, 'JL').and.callThrough();
      service.write('', 1000);
      expect(service.JL).toHaveBeenCalled();
    }));

});
