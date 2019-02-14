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
import { AngularSvgIconModule } from 'angular-svg-icon';

import { Image } from './image.component';
import { ServiceConstants } from './../../services/service.constants';
import { SvgComponent } from './svg/svg.component';
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite } from 'ng-bullet';

const declarations = [
  Image,
  SvgComponent
 ];
const imports = [  AngularSvgIconModule ];
const providers = [];

let fixture, hostComponent;

describe('Image', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(Image);
    hostComponent = fixture.debugElement.componentInstance;
  });

  it('should create the Image',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('ngOnInit() should update the imagesPath',  async(() => {
    hostComponent.ngOnInit();
    expect(hostComponent.imagesPath).not.toEqual(null);
  }));

});
