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

import { Menu } from './menu.component';
import { Link } from '../platform/link.component';
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import { Param } from '../../shared/param-state';
import { fieldValueParam } from 'mockdata';

const declarations = [
  Menu,
  Link
 ];
const imports = [];
const providers = [];

let fixture, hostComponent;

describe('Menu', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach( () => {
    fixture = TestBed.createComponent(Menu);
    hostComponent = fixture.debugElement.componentInstance;
      hostComponent.element = fieldValueParam;
  });

  it('should create the Menu',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

});