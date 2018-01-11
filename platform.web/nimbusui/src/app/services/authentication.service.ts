/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

import { Injectable, EventEmitter } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/share';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';

@Injectable()
export class AuthenticationService {

    login$: EventEmitter<any>;
    loginDataStore: any[];

    constructor(public http: Http) {
        // Create Observable Stream to output our data
        this.login$ = new EventEmitter();
        this.loginDataStore = [];
    }

    logError(err) {
        console.error('Failure making server call : ' + JSON.stringify(err));
    }

    login(user: string, password: string, remember: boolean) {
        console.log('AuthenticationService#login(user, password, remember): Logging in...');
    }

    logout(): Observable<boolean> {
        console.log('AuthenticationService#logout(): Logging out...');
        return Observable.of(true);
    }
}
