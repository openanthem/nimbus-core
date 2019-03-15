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
