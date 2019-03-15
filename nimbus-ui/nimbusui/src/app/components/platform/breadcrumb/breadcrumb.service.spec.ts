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

import { NmMessageService } from './../../../services/toastmessage.service';
import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { BreadcrumbService } from './breadcrumb.service';
import { PageService } from './../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { AppInitService } from '../../../services/app.init.service';

let http, backend, service, sessionStore;

describe('BreadcrumbService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
          { provide: 'JSNLOG', useValue: JL },
          { provide: LocationStrategy, useClass: HashLocationStrategy },
          NmMessageService,
          Location,
          BreadcrumbService,
          PageService,
          CustomHttpClient,
          LoaderService,
          ConfigService,
          LoggerService,
          SessionStoreService,
          AppInitService
        ],
      imports: [ HttpClientTestingModule, HttpModule, StorageServiceModule ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(BreadcrumbService);
    sessionStore = TestBed.get(SessionStoreService);
  });

    it('should be created', async(() => {
      expect(service).toBeTruthy();
    }));

    it('addCrump() should return crump', async(() => {
      const res = { id: 'test1', label: 'test2', params: null, url: 'test3' };
      expect(service.addCrumb('test1', 'test2', 'test3')).toEqual(res);
    }));

    it('getAll() should return false', async(() => {
      sessionStorage.setItem('breadcrumbs', JSON.stringify({
          crumbs: false
        }));
      expect(service.getAll()).toEqual(false);
    }));

    it('getAll() should return undefined', async(() => {
      sessionStorage.setItem('breadcrumbs', '');
      expect(service.getAll()).toEqual(undefined);
    }));

    it('getByPageId(pageId) should return _breadcrumbs[pageId]', async(() => {
      service.push('test1', 'test2', 'test3');
      expect(service.getByPageId('test1')).toEqual({
        id: 'test1',
        label: 'test2',
        params: null,
        url: 'test3'
      });
    }));

    it('getByPageId(pageId) should return undefined', async(() => {
      sessionStorage.setItem('breadcrumbs', '');
      expect(service.getByPageId('test1')).toEqual(undefined);
    }));

    it('getHomeBreadcrumb() should return this._breadcrumbs[this._homePageId]', async(() => {
      service.push('test1', 'test2', 'test3');
      expect(service.getHomeBreadcrumb()).toEqual({
        id: 'test1',
        label: 'test2',
        params: null,
        url: 'test3'
      });
    }));

    it('getHomeBreadcrumb() should return undefined based on breadcrumb.crumbs', async(() => {
      sessionStorage.setItem('breadcrumbs', JSON.stringify({
          crumbs: false
        }));
      expect(service.getHomeBreadcrumb()).toEqual(undefined);
    }));

    it('getHomeBreadcrumb() should return undefined', async(() => {
      expect(service.getHomeBreadcrumb()).toEqual(undefined);
    }));

    it('isHomeRoute() should return false', async(() => {
      const test = { firstChild: { snapshot: { params: { pageId: 123 } } } };
      sessionStorage.setItem('breadcrumbs', JSON.stringify({
          homePage: 123
        }));
      expect(service.isHomeRoute(test)).toEqual(false);
    }));

    it('isHomeRoute() should return true', async(() => {
      const test = { firstChild: { snapshot: { params: { pageId: 123 } } } };
      sessionStorage.setItem('breadcrumbs', JSON.stringify({
          homePage: 123,
          crumbs: {}
        }));
      expect(service.isHomeRoute(test)).toEqual(true);
    }));

    it('push() should return false', async(() => {
      sessionStorage.setItem('breadcrumbs', JSON.stringify({
          crumbs: { test1: 123, id: 123 }
        }));
      service.push('test1', 'test2', 'test3');
      expect(service.push('test1', 'test2', 'test3')).toEqual(false);
    }));

    it('push() should return false based on breadcrumbs.crumbs', async(() => {
      sessionStorage.setItem('breadcrumbs', JSON.stringify({
          crumbs: { test1: 123 }
        }));
      service.push('test1', 'test2', 'test3');
      expect(service.push('test1', 'test2', 'test3')).toEqual(false);
    }));

});
