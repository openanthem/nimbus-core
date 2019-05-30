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

import { EventEmitter, Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable, of as observableOf } from 'rxjs';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Injectable()
export class AuthenticationService {
  login$: EventEmitter<any>;
  loginDataStore: any[];

  constructor(public http: Http) {
    // Create Observable Stream to output our data
    this.login$ = new EventEmitter();
    this.loginDataStore = [];
  }

  logError(err) {}

  login(user: string, password: string, remember: boolean) {}

  logout(): Observable<boolean> {
    return observableOf(true);
  }
}
