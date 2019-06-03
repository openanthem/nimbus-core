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

import { HttpClient } from '@angular/common/http';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { formBuilderServiceElements, formPicklist } from 'mockdata';
import { ConfigService } from '../../services/config.service';
import { ParamUtils } from './../../shared/param-utils';
import { FormElementsService } from './form-builder.service';
import { ValidationUtils } from './validators/ValidationUtils';

const picklistParam = Object.assign({}, formPicklist);

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
        { provide: FormBuilder, useClass: MockFormBuilder },
        ConfigService,
        FormElementsService
      ],
      imports: [HttpClientTestingModule, HttpModule]
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
    spyOn(formBuilder, 'group').and.returnValue(123);
    spyOn(service, 'buildFormGroup').and.returnValue([1]);
    const res = service.toFormGroup(formBuilderServiceElements, []);
    expect(res).toEqual(123);
    expect(service.buildFormGroup).toHaveBeenCalledWith(
      formBuilderServiceElements
    );
    expect(formBuilder.group).toHaveBeenCalledWith([1]);
  }));

  it('toFormGroup() should return object with validator property', async(() => {
    const fValidations = ['test'];
    spyOn(formBuilder, 'group').and.returnValue(123);
    spyOn(service, 'buildFormGroup').and.returnValue([1]);
    const res = service.toFormGroup(formBuilderServiceElements, fValidations);
    expect(res).toEqual(123);
    expect(service.buildFormGroup).toHaveBeenCalledWith(
      formBuilderServiceElements
    );
    expect(formBuilder.group).not.toHaveBeenCalledWith([1]);
    expect(formBuilder.group).toHaveBeenCalled();
  }));

  it('buildFormGroup() should return object based on the createNewFormGroup() value', async(() => {
    formBuilderServiceElements[0].config.uiStyles.attributes.dataEntryField = true;
    spyOn(service, 'createNewFormGroup').and.returnValue(132);
    const res = service.buildFormGroup(formBuilderServiceElements);
    for (const key in res) {
      if (res.hasOwnProperty(key)) {
        const element = res[key];
        expect(element).toEqual(132);
      }
    }
  }));

  it('createNewFormGroup() should return form formgroup with controls and should not call createNewFormGroup()', async(() => {
    spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue([]);
    spyOn(service, '_getTypeSafeLeafState').and.returnValue('leafState');
    spyOn(service, 'createNewFormGroup').and.callThrough();
    formBuilderServiceElements[0].type.model.params[0].type['model'] = {
      params: []
    };
    formBuilderServiceElements[0].type.model.params[0].config.type.nested = false;
    formBuilderServiceElements[0].type.model.params[1].config.type.nested = false;
    const res = service.createNewFormGroup(formBuilderServiceElements[0]);
    expect(service.createNewFormGroup).toHaveBeenCalledTimes(1);
    expect(res.controls.addOwner).toBeTruthy();
    expect(res.controls.search).toBeTruthy();
  }));

  it('createNewFormGroup() should return form formgroup with controls and call createNewFormGroup()', async(() => {
    spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue([]);
    spyOn(service, '_getTypeSafeLeafState').and.returnValue('leafState');
    spyOn(service, 'createNewFormGroup').and.callThrough();
    formBuilderServiceElements[0].type.model.params[0].type['model'] = {
      params: []
    };
    formBuilderServiceElements[0].type.model.params[0].config.type.nested = true;
    const res = service.createNewFormGroup(formBuilderServiceElements[0]);
    expect(service.createNewFormGroup).toHaveBeenCalledTimes(2);
    expect(res.controls.addOwner).toBeTruthy();
    expect(res.controls.search).toBeTruthy();
  }));

  it('buildFormGroup() should return object with category, selected and []', async(() => {
    formBuilderServiceElements[0] = picklistParam;
    formBuilderServiceElements[0].config.uiStyles.attributes.dataEntryField = true;
    formBuilderServiceElements[0].alias = 'PickList';
    formBuilderServiceElements[0].type.model.params[0].alias =
      'PickListSelected';
    spyOn(service, 'createNewFormGroup').and.returnValue(132);
    spyOn(service, '_getTypeSafeLeafState').and.returnValue('leafState');
    const res = service.buildFormGroup(formBuilderServiceElements);
    expect(res.category).toEqual(132);
    expect(res.selected[0]).toEqual({ value: 'leafState', disabled: false });
    expect(res.selected[1]).toEqual([]);
  }));

  it('buildFormGroup() should return object with category, selected', async(() => {
    formBuilderServiceElements[0] = picklistParam;
    formBuilderServiceElements[0].config.uiStyles.attributes.dataEntryField = true;
    formBuilderServiceElements[0].alias = 'PickList';
    formBuilderServiceElements[0].type.model.params[0].alias =
      'PickListSelected';
    spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue(false);
    spyOn(service, 'createNewFormGroup').and.returnValue(132);
    spyOn(service, '_getTypeSafeLeafState').and.returnValue('leafState');
    const res = service.buildFormGroup(formBuilderServiceElements);
    expect(res.category).toEqual(132);
    expect(res.selected[0]).toEqual({ value: 'leafState', disabled: false });
  }));

  it('buildFormGroup() should return object with category', async(() => {
    formBuilderServiceElements[0].config.type.nested = false;
    spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue(false);
    spyOn(service, 'createNewFormGroup').and.returnValue(132);
    spyOn(service, '_getTypeSafeLeafState').and.returnValue('leafState');
    const res = service.buildFormGroup(formBuilderServiceElements);
    expect(res.category).toEqual([{ value: 'leafState', disabled: false }]);
  }));

  it('buildFormGroup() should return object with [] in category', async(() => {
    formBuilderServiceElements[0].config.type.nested = false;
    spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue([]);
    spyOn(service, 'createNewFormGroup').and.returnValue(132);
    spyOn(service, '_getTypeSafeLeafState').and.returnValue('leafState');
    const res = service.buildFormGroup(formBuilderServiceElements);
    expect(res.category[0]).toEqual({ value: 'leafState', disabled: false });
    expect(res.category[1]).toEqual([]);
  }));

  it('_getTypeSafeLeafState() should return leafState if ParamUtils.isKnownDateType() returns true', async(() => {
    spyOn(ParamUtils, 'isKnownDateType').and.returnValue(true);
    formBuilderServiceElements[0].leafState = 'testing_getTypeSafeLeafState';
    expect(
      service._getTypeSafeLeafState(formBuilderServiceElements[0])
    ).toEqual('testing_getTypeSafeLeafState');
  }));

  it('_getTypeSafeLeafState() should return leafState if ParamUtils.isKnownDateType() returns false for gridParam', async(() => {
    spyOn(ParamUtils, 'isKnownDateType').and.returnValue(false);
    formBuilderServiceElements[0].alias = 'Grid';
    formBuilderServiceElements[0].tableBasedData = { values: [1] };
    expect(
      service._getTypeSafeLeafState(formBuilderServiceElements[0])
    ).toEqual([1]);
  }));

  it('_getTypeSafeLeafState() should return leafState if ParamUtils.isKnownDateType() returns false', async(() => {
    spyOn(ParamUtils, 'isKnownDateType').and.returnValue(false);
    formBuilderServiceElements[0].alias = 'Grid1';
    formBuilderServiceElements[0].leafState = 'testLeafState';
    expect(
      service._getTypeSafeLeafState(formBuilderServiceElements[0])
    ).toEqual('testLeafState');
  }));
});
