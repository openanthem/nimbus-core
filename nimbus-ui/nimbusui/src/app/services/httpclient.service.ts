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
import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, URLSearchParams } from '@angular/http';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { ExecuteResponse } from '../shared/app-config.interface';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

/**
 * \@author Swetha.Vemuri
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
    'X-Csrf-Token': ''
  }),
  params : new HttpParams()
};

@Injectable()
export class CustomHttpClient {
  constructor(public http: HttpClient, public customHttpClient: HttpClient ) {
    this.http = http;
  }

  get(url: string, searchParams?: URLSearchParams) {
    httpOptions.headers.set('X-Csrf-Token', this.getCookie('XSRF-Token'));
    return this.customHttpClient.get<ExecuteResponse>(url, httpOptions);
  }

  post(url, data) {
    httpOptions.headers.set('X-Csrf-Token', this.getCookie('XSRF-Token'));
    return this.customHttpClient.post<ExecuteResponse>(url, data, httpOptions);
  }

  postFileData(url, data) {

    return this.http.post(url, data)
          .catch(this.errorHandler);
  }

  getCookie(name) {
    let value = '; ' + document.cookie;
    let parts = value.split('; ' + name + '=');

    if (parts.length === 2) {
      return parts.pop().split(';').shift();
    }
  }

  errorHandler(error: Response){
    console.error(error);
    return Observable.throw(error);

  }
}
