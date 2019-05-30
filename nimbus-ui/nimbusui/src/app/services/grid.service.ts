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
import { Subject } from 'rxjs';
import { CustomHttpClient } from './httpclient.service';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Injectable()
export class GridService {
  eventUpdate = new Subject<any>();
  eventUpdate$ = this.eventUpdate.asObservable();
  gridData$: EventEmitter<any>;

  constructor(private http: CustomHttpClient) {
    this.gridData$ = new EventEmitter();
  }

  setSummaryObject(object: any) {
    this.eventUpdate.next(object);
  }

  getSummaryDetails(id: string, url: string) {
    // return this.http.get('app/resources/data/cars-medium.json')
    //             .toPromise()
    //             .then(res => <Car[]> res.json().data)
    //             .then(data => { return data; });
    // return this.http.get(url);
    //  .map(res => res.json());
  }
}
