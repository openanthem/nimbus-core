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

import { Component, Input } from '@angular/core';
import { Param } from '../../shared/param-state';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-menu',
  template: `
    <nav class="nav navbar navbar-toggleable-md navbar-light" role="navigation">
      <button
        class="navbar-toggler collapsed"
        type="button"
        data-toggle="collapse"
        data-target="#navbarToggler"
        aria-controls="navbarToggler"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div
        class="navbar-collapse nav-block collapse in"
        id="navbarToggler"
        aria-expanded="false"
      >
        <ng-template ngFor let-element [ngForOf]="element?.type?.model?.params">
          <nm-link
            id="{{ element.config?.code }}"
            [element]="element"
            inClass="nav-link"
          ></nm-link>
        </ng-template>
      </div>
    </nav>
  `
})
export class Menu {
  @Input() element: Param;

  constructor() {}

  ngOnInit() {}
}
