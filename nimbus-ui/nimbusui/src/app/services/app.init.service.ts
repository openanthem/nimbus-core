/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { ServiceConstants } from './service.constants';
import { Injectable, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/platform-browser';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/toPromise';
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Injectable()
export class AppInitService {
    headers: Headers;
    options: RequestOptions;
    logOptions:any;

    constructor(@Inject(DOCUMENT) private document: any, private http: Http) { 
        this.headers = new Headers({ 'Content-Type': 'application/json'});
        this.options = new RequestOptions({ headers: this.headers });
    }

    loadConfig(): Promise<any> {
        ServiceConstants.APP_HOST = this.document.location.hostname;
        ServiceConstants.APP_PORT = this.document.location.port;
        ServiceConstants.APP_CONTEXT = this.document.location.pathname.split('/').splice(1, 1);
        ServiceConstants.LOCALE_LANGUAGE = "en-US"; //TODO This locale should be read dynamically. Currently defaulting to en-US
        ServiceConstants.APP_PROTOCOL = this.document.location.protocol;
        const docObj = {
            message: 'Values are updated from document',
            host: ServiceConstants.APP_HOST,
            port: ServiceConstants.APP_PORT,
            appContext: ServiceConstants.APP_CONTEXT,
            appProtocol: ServiceConstants.APP_PROTOCOL
        };
        console.log(JSON.stringify(docObj));
        
        return this.http
            .get(ServiceConstants.APP_LOG_OPTIONS, this.options)
            .toPromise()
            .then(res => {
                this.logOptions = res.json();
            })
            .catch(err => {
                this.logOptions = {
                    'bufferSize': 20, 
                    'storeInBufferLevel': 1000,
                    'logToConsole': true,
                    'level': 4000,
                    'sendWithBufferLevel': 6000
                };
            });
    }

    getLogOptions() {
        return this.logOptions;
    }
}
