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

'use strict';

import { HttpClientModule } from '@angular/common/http';
import { async, TestBed } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { fieldValueParam } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { setup } from '../../../setup.spec';
import { Param } from '../../../shared/param-state';
import { FormErrorMessage } from '../form-error-message.component';
import { InputLabel } from '../form/elements/input-label.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { FieldValue } from './field-value.component';

const declarations = [
  FieldValue,
  FormErrorMessage,
  InputLabel,
  TooltipComponent
];
const imports = [HttpModule, HttpClientModule];
const providers = [];
let fixture, hostComponent;
describe('FieldValue', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FieldValue);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam as Param;
  });

  it('should create the FieldValue', async(() => {
    expect(hostComponent).toBeTruthy();
  }));
});
