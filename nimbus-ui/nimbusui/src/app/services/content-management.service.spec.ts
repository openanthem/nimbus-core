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

  it('findLabelContent() should call  findLabelContentFromConfig()', async(() => {
    spyOn(service, 'findLabelContentFromConfig').and.returnValue('test');
    const param = {
        config: {
            labelConfigs: '',
            code: ''
        }
    };
    expect(service.findLabelContent(param)).toEqual('test');
  }));

  it('findLabelContentFromConfig() should return object with updated key value pairs', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'test';
    service.findLabelContentFromConfig(123, [{locale: 'ttest'}]);
    const labelContent = service.findLabelContentFromConfig(123, [{
        locale: 'test',
        text: 'tText',
        helpText: 'helpText'
    }]);
    expect(labelContent.text).toEqual('tText');
    expect(labelContent.helpText).toEqual('helpText');
  }));

  it('findLabelContentFromConfig() should return object with only text property', async(() => {
    const labelContent = service.findLabelContentFromConfig(123, []);
    expect(labelContent.text).toEqual(123);
  }));

});