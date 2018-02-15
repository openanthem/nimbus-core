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
import { Http, Headers, RequestOptions} from '@angular/http';
import { HttpClient } from '@angular/common/http';

/**
 * \@author Swetha.Vemuri
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Injectable()
export class CustomHttpClient {
  constructor(public http: HttpClient, public customHttpClient: Http ) {
    this.http = http;
  }

  createAuthorizationHeader(headers:Headers) {
    headers.append('X-Csrf-Token', this.getCookie('XSRF-Token'));
  }

  get(url) {
    let headers = new Headers({'Content-Type': 'application/json'});
    this.createAuthorizationHeader(headers);
    let options = new RequestOptions({headers: headers, withCredentials: true});
    return this.customHttpClient.get(url, options);
  }

  post(url, data) {
    let headers = new Headers({'Content-Type': 'application/json'});
    this.createAuthorizationHeader(headers);
    let options = new RequestOptions({headers: headers, withCredentials: true});
    return this.customHttpClient.post(url, data, options);
  }

  postFileData(url, data) {

    return this.http.post(url, data);
  }

  getCookie(name) {
    let value = '; ' + document.cookie;
    let parts = value.split('; ' + name + '=');

    if (parts.length === 2) {
      return parts.pop().split(';').shift();
    }
  }
}
