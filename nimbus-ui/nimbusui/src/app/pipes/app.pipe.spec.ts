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

import { KeysPipe } from './app.pipe';
import { TestBed, async } from '@angular/core/testing';
import { Param } from '../shared/param-state';
import { ConfigService } from './../services/config.service';

describe('KeysPipe', () => {

beforeEach(
  async(() => {
    TestBed.configureTestingModule({
      providers: [ConfigService]
    }).compileComponents();
  })
);


  it('create an KeysPipe instance', () => {
    const pipe = new KeysPipe();
    expect(pipe).toBeTruthy();
  });

  it('KeysPipe should not return anything on passing string', () => {
    const pipe = new KeysPipe();
    const result = pipe.transform('abcd');
    expect(result).toBeFalsy();
  });

  it('KeysPipe should return result', () => {
    const pipe = new KeysPipe();
    const data = new Map<string,  Param[]>();
    const service = TestBed.get(ConfigService);
    const p = new Param(service);
    data.set('hello', [p]);
    const result = pipe.transform(data);
    expect(result).toBeTruthy();
  });
});
