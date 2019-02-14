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
import { FormBuilder } from '@angular/forms';

import { FormElementsService } from './form-builder.service';
import { ValidationUtils } from './validators/ValidationUtils';
import { ParamUtils } from './../../shared/param-utils';
import { Param, Type, Model } from '../../shared/param-state';
import { ParamConfig } from '../../shared/param-config';
import { ConfigService } from '../../services/config.service';

let http, backend, service, formBuilder, configService;

class MockFormBuilder {
    group(a, b) {
        return b ? b : a; 
     }
}

describe('FormElementsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          {provide: FormBuilder, useClass: MockFormBuilder},
          ConfigService,
          FormElementsService
        ],
      imports: [ 
          HttpClientTestingModule, 
          HttpModule 
        ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(FormElementsService);
    formBuilder = TestBed.get(FormBuilder);
    configService = TestBed.get(ConfigService);
  });

    it('should be created', async(() => {
      expect(service).toBeTruthy();
    }));

    it('toFormGroup() should return array based on buildFormGroup()', async(() => {
      const fValidations = [];
      spyOn(service, 'buildFormGroup').and.returnValue([1]);
      expect(service.toFormGroup([1], [])).toEqual([1]);
    }));

    it('toFormGroup() should return object with validator property', async(() => {
      const fValidations = [];
      spyOn(service, 'buildFormGroup').and.returnValue([1]);
      const res = service.toFormGroup([1], [1, 2]);
      expect(res.validator).toBeTruthy();
    }));

    it('buildFormGroup() should return object based on the createNewFormGroup() value', async(() => {
      const ele = [{ type: { model: { params: [1] } }, config: { type: { collection: '', nested: 'a' }, uiStyles: { attributes: { alias: 'test' } } } }];
      spyOn(service, 'createNewFormGroup').and.returnValue(132);
      const res = service.buildFormGroup(ele);
      for (const key in res) {
        if (res.hasOwnProperty(key)) {
          const element = res[key];
          expect(element).toEqual(132);
        }
      }
    }));

    it('buildFormGroup() should return object based on the createNewFormGroup() value and ValidationUtils.buildStaticValidations()', async(() => {
      const ele = [{ type: { model: { params: [] } }, config: { type: { collection: '', nested: 'a' }, uiStyles: { attributes: { alias: 'test' } } } }];
      spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue(132);
      spyOn(service, 'createNewFormGroup').and.returnValue(132);
      const res = service.buildFormGroup(ele);
      for (const key in res) {
        if (res.hasOwnProperty(key)) {
          const element = res[key];
          expect(element[1]).toEqual(132);
          expect(element[0].disabled).toEqual(true);
          expect(element[0].value).toEqual('');
        }
      }
    }));

    it('buildFormGroup() should return object based only on the createNewFormGroup() value if ValidationUtils.buildStaticValidations() is null', async(() => {
      const ele = [{ type: { model: { params: [] } }, config: { type: { collection: null, nested: 'a' }, uiStyles: { attributes: { alias: 'test' } } } }];
      spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue(null);
      spyOn(service, 'createNewFormGroup').and.returnValue(132);
      const res = service.buildFormGroup(ele);
      for (const key in res) {
        if (res.hasOwnProperty(key)) {
          const element = res[key];
          expect(element[0].disabled).toEqual(true);
          expect(element[0].value).toEqual('');
        }
      }
    }));

    it('buildFormGroup() should return empty object', async(() => {
      const ele = [{ type: { model: { params: [1] } }, config: { type: { collection: '', nested: 'a' }, uiStyles: { attributes: { alias: 'Button' } } } }];
      spyOn(service, 'createNewFormGroup').and.returnValue(132);
      const res = service.buildFormGroup(ele);
      expect(res).toEqual({});
    }));

    it('7should be created', async(() => {
      const element = new Param(configService);
      element.type = new Type(configService);
      element.type.model = new Model(configService);
      const cParam = new Param(configService);
      const cpConfig = new ParamConfig(configService);
      spyOn(configService, 'getViewConfigById').and.returnValue({
        type: {
          nested: false,
          code: 123
        }
      });
      element.type.model.params = [cParam];
      spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue(false);
      const res = service.createNewFormGroup(element);
      console.log('res000', res);
      expect(res).toBeTruthy();
    }));

    it('_getTypeSafeLeafState() should return param.leafState', async(() => {
      const param = { leafState: 'lState', gridList: 'gList', config: { type: { name: 'name1' } } };
      spyOn(ParamUtils, 'isKnownDateType').and.returnValue(true);
      const res = service._getTypeSafeLeafState(param);
      expect(res).toEqual('lState');
    }));

    it('_getTypeSafeLeafState() should return param.gridList', async(() => {
      const param = { alias: 'Grid', leafState: 'lState', gridList: [1], config: { type: { name: 'name1' } }, gridData: {leafState: [1]} };
      spyOn(ParamUtils, 'isKnownDateType').and.returnValue(false);
      const res = service._getTypeSafeLeafState(param);
      expect(res).toEqual([1]);
    }));

    it('_getTypeSafeLeafState() should return param.leafState even if ParamUtils.isKnownDateType() is null', async(() => {
      const param = { leafState: 'lState', gridList: 'gList', config: { type: { name: 'name1' } } };
      spyOn(ParamUtils, 'isKnownDateType').and.returnValue(false);
      const res = service._getTypeSafeLeafState(param);
      expect(res).toEqual('lState');
    }));

});