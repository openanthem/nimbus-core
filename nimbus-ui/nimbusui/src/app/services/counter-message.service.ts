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

import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Param } from './../shared/param-state';

/**
 * \@author Sandeep.Mantha
 *
 */
@Injectable()
export class CounterMessageService {
  private subject = new Subject<boolean>();
  counterMessageSubject$ = this.subject.asObservable();

  private formErrorMessages = new Subject<Param>();
  formErrorMessages$ = this.formErrorMessages.asObservable();

  evalCounterMessage(val: boolean) {
    this.subject.next(val);
  }

  evalFormParamMessages(param: Param) {
    this.formErrorMessages.next(param);
  }
}
