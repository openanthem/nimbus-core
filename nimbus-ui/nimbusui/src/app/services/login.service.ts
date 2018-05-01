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

import { Injectable, EventEmitter } from '@angular/core';
import { Http, Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/share';
import { LoggerService } from './logger.service';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Injectable()
export class LoginSvc {

    login$: EventEmitter<any>;
    loginDataStore: any[];

	constructor(public http: Http, private logger: LoggerService) {
        // Create Observable Stream to output our data
        this.login$ = new EventEmitter();
        this.loginDataStore = [];
	}

    logError(err) {
		console.error('Failure making server call : ' + JSON.stringify(err));
	}

    login(user: string, password: string, remember: boolean) {
        let headers = new Headers({'Content-Type': 'application/x-www-form-urlencoded'});
        let body = 'username='+user+'&password='+password+'&rememberme='+remember;
        let options = new RequestOptions({headers: headers});
        this.http.post('http://30.138.86.234:8080/login/platform', body, options)
			.map(res => res.json())
			.subscribe(data => {
                // Push the session into the Observable stream
                this.login$.next(data);
            },
				err => this.logError(err),
				() => this.logger.info('Config query completed..')
			);
    }
}
