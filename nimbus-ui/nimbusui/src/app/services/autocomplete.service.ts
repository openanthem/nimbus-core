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
import { Observable } from 'rxjs';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/Rx';
import { Result } from './../shared/app-config.interface';
import { Action, Behavior } from './../shared/command.enum';
import { ConfigService } from './config.service';
import { CustomHttpClient } from './httpclient.service';
import { LoggerService } from './logger.service';
import { ServiceConstants } from './service.constants';
import { SessionStoreService } from './session.store';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Injectable()
export class AutoCompleteService {
  constructor(
    private http: CustomHttpClient,
    private configService: ConfigService,
    private sessionStore: SessionStoreService,
    private logger: LoggerService
  ) {}

  search(searchValues: Observable<string>, url) {
    return searchValues
      .debounceTime(400)
      .switchMap(term => this.fetchResults(term, url))
      .catch(err => {
        return Observable.throw(err);
      });
  }

  fetchResults(val, url) {
    let flowName = url.substring(1, url.indexOf('/', 2));
    let rootDomainId = this.sessionStore.get(flowName);
    if (rootDomainId != null) {
      let flowNameWithId = flowName.concat(':' + rootDomainId);
      url = url.replace(flowName, flowNameWithId);
    }
    let httpUrl =
      ServiceConstants.PLATFORM_BASE_URL +
      url +
      '/' +
      Action._get.toString() +
      '?autocompletesearchvalue=' +
      val;

    return this.http
      .get(httpUrl)
      .map(res => {
        return this.processData(res);
      })
      .catch(err => {
        return Observable.throw(err);
      })
      .finally(() =>
        this.logger.info('http response for ' + httpUrl + 'is processed')
      );
  }

  processData(response: any): any[] {
    let index = 0;
    let data = [];
    let result = response.result;

    while (result[index]) {
      let subResponse: any = result[index];
      if (subResponse.b === Behavior.execute.value) {
        let resp = new Result(this.configService).deserialize(
          subResponse.result
        );
        resp.outputs.forEach(e => {
          data = e.value;
        });
      }
      index++;
    }
    return data;
  }
}
