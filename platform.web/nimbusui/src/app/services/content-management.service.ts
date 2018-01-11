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
import { Http, Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/share';
import { ServiceConstants } from './service.constants';

// Web Content Service
@Injectable()
export class WebContentSvc {

    content$: EventEmitter<any>;
	constructor(public http: Http) {
        // Create Observable Stream to output our data
        this.content$ = new EventEmitter();
	}

    logError(err) {
		console.error('Failure making server call : ' + JSON.stringify(err));
	}

    getContent(id: string) {
        let headers = new Headers({'Content-Type': 'application/json'});
        let options = new RequestOptions({headers: headers});
        this.http.get(ServiceConstants.WEB_CONTENT_URL + id, options)
			.map(res => res.json())
			.subscribe(data => {
                // Push the new config into the Observable stream
                this.content$.next(data);
            },
				err =>  {
                    //this.logError(err);
                    this.content$.next({
                        'id': id, 
                        'label':id
                    });
                },
				() => console.log('Web Content query completed..')
			);
    }
}
