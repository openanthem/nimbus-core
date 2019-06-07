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

import { Component } from '@angular/core';
import { BaseElement } from '../base-element.component';
import {
  ComponentTypes,
  ViewComponent
} from './../../../shared/param-annotations.enum';

/**
 * \@author Vivek.Kamineni
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-tab',
  template: `
    <p-tabView *ngIf="element?.visible">
      <ng-template
        ngFor
        let-tabPanel
        let-tabIndex="index"
        [ngForOf]="nestedParams"
      >
        <p-tabPanel
          *ngIf="tabPanel?.visible"
          [disabled]="!tabPanel?.enabled"
          [closable]="tabPanel?.config?.uiStyles?.attributes?.closable"
          [selected]="tabIndex == 0"
        >
          <ng-template pTemplate="header">
            <nm-label
              id="{{ tabPanel.config?.code }}"
              [element]="tabPanel"
            ></nm-label>
          </ng-template>

          <ng-template pTemplate="content">
            <ng-template
              ngFor
              let-tabContent
              [ngForOf]="tabPanel?.type?.model?.params"
            >
              <ng-template
                [ngIf]="tabContent.alias == viewComponent.section.toString()"
              >
                <nm-section
                  id="{{ tabContent.config?.code }}"
                  [element]="tabContent"
                ></nm-section>
              </ng-template>
            </ng-template>
            <ng-template
              ngFor
              let-tabContent
              [ngForOf]="tabPanel?.type?.model?.params"
            >
              <ng-template
                [ngIf]="
                  tabContent.alias == componentTypes.tab.toString() &&
                  tabContent?.visible
                "
              >
                <nm-tab
                  id="{{ tabContent.config?.code }}"
                  [element]="tabContent"
                ></nm-tab>
              </ng-template>
            </ng-template>
          </ng-template>
        </p-tabPanel>
      </ng-template>
    </p-tabView>
  `
})
export class Tab extends BaseElement {
  componentTypes = ComponentTypes;
  viewComponent = ViewComponent;

  ngOnInit() {
    super.ngOnInit();
  }
}
