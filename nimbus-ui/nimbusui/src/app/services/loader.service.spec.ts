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

import { TestBed, inject } from '@angular/core/testing';

import { LoaderService } from './loader.service';

describe('LoaderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LoaderService]
    });
  });

  it('LoaderService should be created', inject([LoaderService], (service: LoaderService) => {
    expect(service).toBeTruthy();
  }));

  it('show() should update loaderUpdate subject with true value', inject([LoaderService], (service: LoaderService) => {
    let test;
    service.loaderUpdate.subscribe(val => {
        test = val;
    });
    service.show();
    expect(test.show).toEqual(true);
  }));

  it('hide() should update loaderUpdate subject with false value', inject([LoaderService], (service: LoaderService) => {
    let test;
    service.loaderUpdate.subscribe(val => {
        test = val;
    });
    service.hide();
    expect(test.show).toEqual(false);
  }));

});
