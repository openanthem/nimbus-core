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
import { MenuPanel } from '../../model/menu-meta.interface';

/**
 * \@author Tony Lopez
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-nav',
  template: `
    <ng-template [ngIf]="menuPanel && menuPanel.menuItems">
      <nav
        class="section-nav nav navbar navbar-expand-sm {{ cssClass }}"
        role="navigation"
      >
        <button
          aria-controls="navbarToggler"
          aria-expanded="false"
          aria-label="Toggle navigation"
          class="navbar-toggler collapsed"
          [attr.data-target]="'#navbarToggler-' + menuPanel.code"
          data-toggle="collapse"
          type="button"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div
          aria-expanded="false"
          class="navbar-collapse nav-block collapse"
          id="navbarToggler-{{ menuPanel.code }}"
        >
          <nm-panelMenu
            [model]="menuPanel.menuItems"
            [multiple]="false"
          ></nm-panelMenu>
        </div>
      </nav>
    </ng-template>
  `
})
export class NavigationComponent {
  @Input() cssClass: string = '';
  @Input() menuPanel: MenuPanel;

  ngOnInit() {}
}
