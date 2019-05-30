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

import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { StateLookup, STOMPService } from './stomp.service';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 * STOMP connection status as a component
 */
@Component({
  selector: 'stomp-status',
  template: `
    <div class="col-2 float-right">
      <p>
        STOMP Status : <span id="status"> {{ state | async }} </span>
      </p>
    </div>
  `
})
export class STOMPStatusComponent implements OnInit {
  public state: Observable<string>;

  constructor(private _stompService: STOMPService) {}

  ngOnInit() {
    this.state = this._stompService.state.pipe(
      map((state: number) => StateLookup[state])
    );
  }
}
