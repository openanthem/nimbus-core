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

import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ExecuteResponse } from '../shared/app-config.interface';
import { ServiceConstants } from './service.constants';
import { SessionStoreService } from './session.store';

/**
 * \@author Swetha.Vemuri
 * \@whatItDoes
 *
 * \@howToUse
 *
 */

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'X-Csrf-Token': ''
  }),
  params: new HttpParams()
};

@Injectable()
export class CustomHttpClient {
  constructor(
    public http: HttpClient,
    public customHttpClient: HttpClient,
    private sessionStore: SessionStoreService
  ) {
    this.http = http;
  }

  get(url: string, searchParams?: URLSearchParams) {
    return this.customHttpClient.get<ExecuteResponse>(url, this.buildOptions());
  }

  post(url, data) {
    return this.customHttpClient.post<ExecuteResponse>(
      url,
      data,
      this.buildOptions()
    );
  }

  put(url, data) {
    return this.customHttpClient.put<ExecuteResponse>(
      url,
      data,
      this.buildOptions()
    );
  }

  buildOptions() {
    let headers = new HttpHeaders();
    headers = headers.append('Content-Type', 'application/json');
    headers = headers.append(
      'Authorization',
      `Bearer ${this.sessionStore.get(ServiceConstants.AUTH_TOKEN_KEY)}`
    );
    return { headers: headers };
  }

  postFileData(url, data) {
    return this.http.post(url, data).pipe(catchError(this.errorHandler));
  }

  getCookie(name) {
    let value = '; ' + document.cookie;
    let parts = value.split('; ' + name + '=');

    if (parts.length === 2) {
      return parts
        .pop()
        .split(';')
        .shift();
    }
  }

  errorHandler(error: Response) {
    console.error(error);
    return Observable.throw(error);
  }
}
