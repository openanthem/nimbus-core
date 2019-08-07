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

import { state, style, trigger } from '@angular/animations';
import {
  Component,
  HostListener,
  Input,
  QueryList,
  ViewChildren
} from '@angular/core';
import { LayoutService } from '../../services/layout.service';
import { LoggerService } from '../../services/logger.service';
import { PageService } from '../../services/page.service';
import { Param } from './../../shared/param-state';
import { BaseElement } from './base-element.component';

/**
 * \@author Vivek Kamineni
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-actiontray',
  template: `
    <div
      [@actionTrayAnimation]="state"
      [hidden]="!(element?.type?.model?.params != 0) && !showTray"
      [attr.id]="showTray ? 'actionTray' : null"
      [class]="showTray ? null : 'displayNone'"
    >
      <ng-template
        ngFor
        let-actionTrayItem
        [ngForOf]="element?.type?.model?.params"
      >
        <nm-button
          #actionButton
          id="{{ actionTrayItem?.config?.code }}"
          [element]="actionTrayItem"
          [actionTray]="true"
          (elementChange)="isTrayVisible()"
        >
        </nm-button>
      </ng-template>
    </div>
  `,
  animations: [
    trigger('actionTrayAnimation', [
      state(
        'trayFixed',
        style({
          position: 'fixed',
          top: 0
        })
      ),
      state(
        'trayScroll',
        style({
          position: 'absolute',
          top: '100px'
        })
      )
    ])
  ]
})
export class ActionTray extends BaseElement {
  @Input() element: Param;
  showTray: boolean = true;
  state: string;

  @ViewChildren('actionButton') actionButtons: QueryList<any>;

  constructor(
    private pageService: PageService,
    private _logger: LoggerService,
    private layoutSvc: LayoutService
  ) {
    super();
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    if ((window.pageYOffset || document.documentElement.scrollTop) > 100) {
      this.state = 'trayFixed';
    } else if (
      window.pageYOffset ||
      document.documentElement.scrollTop ||
      document.body.scrollTop < 100
    ) {
      this.state = 'trayScroll';
    }
  }

  ngOnInit() {
    this.subscribers.push(
      this.layoutSvc.layout$.subscribe(data => {
        if (data.actiontray) {
          this.showTray = data.actiontray.type.model.params.some(
            item => item.visible === true
          );
        }
      })
    );
  }

  isTrayVisible() {
    if (this.actionButtons) {
      this.showTray = this.actionButtons
        .toArray()
        .some(item => item.element.visible === true);
    }
  }
}
