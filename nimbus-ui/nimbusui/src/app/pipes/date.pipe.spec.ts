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

import { DateTimeFormatPipe } from './date.pipe';
import { TestBed, async } from '@angular/core/testing';
import { Param } from '../shared/param-state';

describe('DateTimeFormatPipe', () => {

  it('create an DateTimeFormatPipe instance', () => {
    const pipe = new DateTimeFormatPipe('en');
    expect(pipe).toBeTruthy();
  });

  it('should return date in localDate format', () => {
    const pipe = new DateTimeFormatPipe('en');
    const result = pipe.transform('12/2/2017', '', 'LocalDate');
    expect(result).toEqual('12/02/2017');
  });

  it('should return date in default format', () => {
    const pipe = new DateTimeFormatPipe('en');
    const result = pipe.transform('12/2/2017', '', '');
    expect(result).toEqual('12/02/2017 12:00 AM');
  });

  it('should return date in default format on sending invalid date format', () => {
    const pipe = new DateTimeFormatPipe('en');
    const result = pipe.transform('12/2/2017', '', 'test');
    expect(result).toEqual('12/02/2017 12:00 AM');
  });

  it('should return date in specific format', () => {
    const pipe = new DateTimeFormatPipe('en');
    const result = pipe.transform('12/2/2017', 'dd/MM', 'test');
    expect(result).toEqual('02/12');
  });

});
