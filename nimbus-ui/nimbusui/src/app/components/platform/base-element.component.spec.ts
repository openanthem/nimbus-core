'use strict';
import { TestBed, async } from '@angular/core/testing';

import { BaseElement } from './base-element.component';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Param } from '../../shared/param-state';
import { ConfigService } from '../../services/config.service';
import { setup, TestContext } from '../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import * as data from '../../payload.json';

let configService, param;

const declarations = [BaseElement];
const imports = [
   HttpModule,
   HttpClientTestingModule
];
const providers = [   ConfigService];

describe('BaseElement', () => {

  configureTestSuite();
  setup(BaseElement, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<BaseElement>){
    this.hostComponent.element = param;
    configService = TestBed.get(ConfigService);
  });

  it('should create the BaseElement', async function(this: TestContext<BaseElement>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('nestedParams should update from element.type.model.params', async function(this: TestContext<BaseElement>) {
    const params: any = 'test';
    this.hostComponent.element.type.model.params = params;
    expect(this.hostComponent.nestedParams).toEqual(params);
  });

  it('nestedParams should return undefined', async function(this: TestContext<BaseElement>) {
    this.hostComponent.element.type.model = null;
    expect(this.hostComponent.nestedParams).toEqual(undefined);
  });

  it('imgSrc should update from element.config.uiStyles.attributes.imgSrc', async function(this: TestContext<BaseElement>) {
    this.hostComponent.element.config.uiStyles.attributes.imgSrc = 'src1';
    expect(this.hostComponent.imgSrc).toEqual('src1');
  });

  it('code should update from element.config.code', async function(this: TestContext<BaseElement>) {
    expect(this.hostComponent.code).toEqual('firstName');
  });

  it('visible should update from element.visible', async function(this: TestContext<BaseElement>) {
    this.hostComponent.element.visible = true;
    expect(this.hostComponent.visible).toBeTruthy();
  });

  it('enabled should update from element.enabled', async function(this: TestContext<BaseElement>) {
    this.hostComponent.element.enabled = true;
    expect(this.hostComponent.enabled).toBeTruthy();
  });

  it('_visible should update from visible property', async function(this: TestContext<BaseElement>) {
    this.hostComponent.visible = true;
    expect((this.hostComponent as any)._visible).toEqual(true);    
  });

  it('_enabled should update from enabled property', async function(this: TestContext<BaseElement>) {
    this.hostComponent.enabled = true;
    expect((this.hostComponent as any)._enabled).toEqual(true);    
  });

  it('cssClass should update from element.config.uiStyles.attributes.cssClass', async function(this: TestContext<BaseElement>) {
    this.hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
    expect(this.hostComponent.cssClass).toEqual('testClass');
  });

  it('type should update from element.config.uiStyles.attributes.type', async function(this: TestContext<BaseElement>) {
    this.hostComponent.element.config.uiStyles.attributes.type = 'testType';
    expect(this.hostComponent.type).toEqual('testType');  
  });

  it('elementStyle should update based on element.config.validation.constraints[0].name', async function(this: TestContext<BaseElement>) {
    const constraints: any = [{ name: 'NotNull' }];
    this.hostComponent.element.config.validation.constraints = constraints;
    expect(this.hostComponent.elementStyle).toEqual('required');
  });

  it('elementStyle should be empty string based on element.config.validation.constraints[0].name', async function(this: TestContext<BaseElement>) {
    const constraints: any = [{ name: 'test' }];
    this.hostComponent.element.config.validation.constraints = constraints;
    expect(this.hostComponent.elementStyle).toEqual('');
  });

  it('elementStyle should be empty string if element.config.validation.constraints[0].name is not avialable', async function(this: TestContext<BaseElement>) {
      this.hostComponent.element.config.validation = null;
      expect(this.hostComponent.elementStyle).toEqual('');
  });

  it('this.hostComponent.isDate() should return true', async function(this: TestContext<BaseElement>) {
    expect(this.hostComponent.isDate('Date')).toBeTruthy();
  });

  it('placeholder should update from element.config.uiStyles.attributes.placeholder', async function(this: TestContext<BaseElement>) {
    this.hostComponent.element.config.uiStyles.attributes.placeholder = 'test';
    expect(this.hostComponent.placeholder).toEqual('test');
  });

  it('placeholder should be undefined if element.config.uiStyles.attributes is null', async function(this: TestContext<BaseElement>) {
    this.hostComponent.element.config.uiStyles.attributes = null;
    expect(this.hostComponent.placeholder).toEqual(undefined);
  });

});