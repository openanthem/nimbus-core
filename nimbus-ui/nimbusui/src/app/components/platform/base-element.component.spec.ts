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

'use strict';
import { TestBed, async } from '@angular/core/testing';

import { BaseElement } from './base-element.component';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Param } from '../../shared/param-state';
import { ConfigService } from '../../services/config.service';
import { setup, TestContext } from '../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import { fieldValueParam } from 'mockdata';

let configService, param;

const declarations = [BaseElement];
const imports = [
   HttpModule,
   HttpClientTestingModule
];
const providers = [   ConfigService];
let fixture, hostComponent;

describe('BaseElement', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BaseElement);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
    configService = TestBed.get(ConfigService);
  });

  it('should create the BaseElement',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('nestedParams should update from element.type.model.params',  async(() => {
    const params: any = 'test';
    hostComponent.element.type.model.params = params;
    expect(hostComponent.nestedParams).toEqual(params);
  }));

  it('nestedParams should return undefined',  async(() => {
    hostComponent.element.type.model = null;
    expect(hostComponent.nestedParams).toEqual(undefined);
  }));

  it('imgSrc should update from element.config.uiStyles.attributes.imgSrc',  async(() => {
    hostComponent.element.config.uiStyles.attributes.imgSrc = 'src1';
    expect(hostComponent.imgSrc).toEqual('src1');
  }));

  it('code should update from element.config.code',  async(() => {
    expect(hostComponent.code).toEqual('firstName');
  }));

  it('visible should update from element.visible',  async(() => {
    hostComponent.element.visible = true;
    expect(hostComponent.visible).toBeTruthy();
  }));

  it('enabled should update from element.enabled',  async(() => {
    hostComponent.element.enabled = true;
    expect(hostComponent.enabled).toBeTruthy();
  }));

  it('_visible should update from visible property',  async(() => {
    hostComponent.visible = true;
    expect((hostComponent as any)._visible).toEqual(true);    
  }));

  it('_enabled should update from enabled property',  async(() => {
    hostComponent.enabled = true;
    expect((hostComponent as any)._enabled).toEqual(true);    
  }));

  it('cssClass should update from element.config.uiStyles.attributes.cssClass',  async(() => {
    hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
    expect(hostComponent.cssClass).toEqual('testClass');
  }));

  it('type should update from element.config.uiStyles.attributes.type',  async(() => {
    hostComponent.element.config.uiStyles.attributes.type = 'testType';
    expect(hostComponent.type).toEqual('testType');  
  }));

  it('elementStyle should update based on element.config.validation.constraints[0].name',  async(() => {
    const constraints: any = [{ name: 'NotNull' }];
    hostComponent.element.config.validation.constraints = constraints;
    expect(hostComponent.elementStyle).toEqual('required');
  }));

  it('elementStyle should be empty string based on element.config.validation.constraints[0].name',  async(() => {
    const constraints: any = [{ name: 'test' }];
    hostComponent.element.config.validation.constraints = constraints;
    expect(hostComponent.elementStyle).toEqual('');
  }));

  it('elementStyle should be empty string if element.config.validation.constraints[0].name is not avialable',  async(() => {
      hostComponent.element.config.validation = null;
      expect(hostComponent.elementStyle).toEqual('');
  }));

  it('hostComponent.isDate() should return true',  async(() => {
    expect(hostComponent.isDate('Date')).toBeTruthy();
  }));

  it('placeholder should update from element.config.uiStyles.attributes.placeholder',  async(() => {
    hostComponent.element.config.uiStyles.attributes.placeholder = 'test';
    expect(hostComponent.placeholder).toEqual('test');
  }));

  it('placeholder should be undefined if element.config.uiStyles.attributes is null',  async(() => {
    hostComponent.element.config.uiStyles.attributes = null;
    expect(hostComponent.placeholder).toEqual(undefined);
  }));

});