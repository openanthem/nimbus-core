import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { BreadcrumbService } from './breadcrumb.service';
import { PageService } from './../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';

let http, backend, service;

describe('BreadcrumbService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          BreadcrumbService,
          PageService,
          CustomHttpClient,
          LoaderService,
          ConfigService,
          LoggerService
        ],
      imports: [ HttpClientTestingModule, HttpModule ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(BreadcrumbService);
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('addCrump() should return crump', async(() => {
      const res = {
        id: 'test1',
        label: 'test2',
        params: null,
        url: 'test3'
      };
    expect(service.addCrumb('test1', 'test2', 'test3')).toEqual(res);
  }));

  it('getAll() should return _breadcrumbs', async(() => {
    service.push('test1', 'test2', 'test3');
    expect(service.getAll()).toEqual({test1: {id: 'test1', label: 'test2', params: null, url: 'test3'}});
  }));

  it('getByPageId(pageId) should return _breadcrumbs[pageId]', async(() => {
    service.push('test1', 'test2', 'test3');
    expect(service.getByPageId('test1')).toEqual({id: 'test1', label: 'test2', params: null, url: 'test3'});
  }));

  it('getHomeBreadcrumb() should return this._breadcrumbs[this._homePageId]', async(() => {
      service.push('test1', 'test2', 'test3');
      expect(service.getHomeBreadcrumb()).toEqual({id: 'test1', label: 'test2', params: null, url: 'test3'});
  }));

  it('getHomeBreadcrumb() should return undefined', async(() => {
    expect(service.getHomeBreadcrumb()).toEqual(undefined);
  }));

  it('isHomeRoute() should assign _homePageId to route.firstChild.snapshot.params', async(() => {
    const test = {
        firstChild: {
            snapshot: {
                params: {
                    pageId: 123
                }
            }
        }
    };
    service.push(123, 'test2', 'test3');
    expect(service.isHomeRoute(test)).toEqual(true);
  }));

  it('push() should update _breadcrumbs', async(() => {
    service.push('test1', 'test2', 'test3');
    service.push('test1', 'test2', 'test3');
    expect(service._breadcrumbs).toEqual({test1: {id: 'test1', label: 'test2', params: null, url: 'test3'}});
  }));

  it('push() should return false', async(() => {
    expect(service.push('test1', 'test2', 'test3')).toEqual(false);
  }));

  it('push() should update _breadcrumbs, _homePageId, _breadcrumbs[pageId] ', async(() => {
    service.push('test1', 'test2', 'test3');
    service.push('test4', 'test2', 'test3');
    expect(service._breadcrumbs).toEqual({test1: {id: 'test1', label: 'test2', params: null, url: 'test3'}, test4: {id: 'test4', label: 'test2', params: null, url: 'test3'}});
  }));

  it('push() should not create breadcrump if pageid already exists', async(() => {
    service.push('test1', 'test2', 'test3');
    service.push('test4', 'test2', 'test3');
    service.push('test4', 'test2', 'test3');
    expect(service._breadcrumbs).toEqual({test1: {id: 'test1', label: 'test2', params: null, url: 'test3'}, test4: {id: 'test4', label: 'test2', params: null, url: 'test3'}});
  }));

});
