import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { WebContentSvc } from './content-management.service';
import { LabelConfig } from './../shared/param-config';
import { ServiceConstants } from './service.constants';

let http, backend, service;

describe('WebContentSvc', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [WebContentSvc],
      imports: [ HttpClientTestingModule, HttpModule ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(WebContentSvc);
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('findLabelContent() should call findLabelContentFromConfig()', async(() => {
    service.findLabelContentFromConfig = () => {};
    spyOn(service, 'findLabelContentFromConfig').and.callThrough();
    const param = {
      labels: 'test',
      config: {
        code: 123
      }
    };
    service.findLabelContent(param);
    expect(service.findLabelContentFromConfig).toHaveBeenCalled();
  }));

  it('findLabelContent() should call  findLabelContentFromConfig()', async(() => {
    spyOn(service, 'findLabelContentFromConfig').and.returnValue('test');
    const param = {
      labels: 'a',
        config: {
            labelConfigs: '',
            code: ''
        }
    };
    expect(service.findLabelContent(param)).toEqual('test');
  }));

  it('findLabelContent() should return undefined', async(() => {
    const param = {  };
    expect(service.findLabelContent(param)).toEqual(undefined);
  }));

});