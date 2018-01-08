/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
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
