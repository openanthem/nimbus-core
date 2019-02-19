import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { WebContentSvc } from './content-management.service';
import { LabelConfig } from './../shared/param-config';
import { ServiceConstants } from './service.constants';
import { cmsParam } from 'mockdata';

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
    spyOn(service, 'findLabelContentFromConfig').and.callThrough();
    service.findLabelContent(cmsParam);
    expect(service.findLabelContentFromConfig).toHaveBeenCalledWith(cmsParam.labels, cmsParam.config.code);
  }));

  it('findLabelContentFromConfig() should return labels', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    const labels = new LabelConfig();
    labels.locale = 'en-US';
    labels.text = 'Owners';
    const res = service.findLabelContentFromConfig(cmsParam.labels, cmsParam.config.code);
    expect(res).toEqual(labels);
  }));

  it('findLabelContent() should return undefined', async(() => {
    const param = {  };
    expect(service.findLabelContent(param)).toEqual(undefined);
  }));

});
