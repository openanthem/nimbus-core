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

import { BaseControlValueAccessor } from './control-value-accessor.component';

let sut;

describe('BaseControlValueAccessor', () => {
  beforeEach(() => {
    sut = new BaseControlValueAccessor();
  });

  it('should create the app', async(() => {
    expect(sut).toBeTruthy();
  }));

  it('writeValue() should update the innerValue', async(() => {
    sut.writeValue('test');
    expect(sut.innerValue).toEqual('test');
  }));

  it('get value() should return the innerValue', async(() => {
    sut.writeValue('test');
    expect(sut.value).toEqual('test');
  }));

  it('set value() should return the innerValue', async(() => {
    sut.value = 'testing';
    sut.value = 'testing';
    expect(sut.innerValue).toEqual('testing');
  }));

  it('registerOnChange() should update changed array', async(() => {
    const test = () => {};
    sut.registerOnChange(test);
    expect(sut.changed.length).not.toEqual(0);
  }));

  it('registerOnTouched() should update touched array', async(() => {
    const test = () => {};
    sut.registerOnTouched(test);
    expect(sut.touched.length).not.toEqual(0);
  }));

});
