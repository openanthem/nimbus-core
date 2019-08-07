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
import { ComponentTypes } from '../../../shared/param-annotations.enum';
import { BaseElement } from '../base-element.component';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-card-details-field-group',
  template: `
    <fieldset>
      <div class="fieldsetFlex">
        <nm-input-legend
          *ngIf="!isLabelEmpty"
          [element]="element"
          [required]="false"
        >
        </nm-input-legend>
        <ng-template ngFor let-field [ngForOf]="element?.type?.model?.params">
          <!-- FieldValue-->
          <ng-template
            [ngIf]="
              field.config?.uiStyles?.attributes?.alias ==
              componentTypes.fieldValue.toString()
            "
          >
            <nm-card-details-field
              id="{{ field.config?.code }}"
              [element]="field"
              [(value)]="field.leafState"
              [ngClass]="field.config.uiStyles.attributes.cssClass"
              [hidden]="!field.visible"
            ></nm-card-details-field>
          </ng-template>
          <!-- Paragraph -->
          <ng-template
            [ngIf]="
              field.config?.uiStyles?.attributes?.alias ==
              componentTypes.paragraph.toString()
            "
          >
            <nm-paragraph
              id="{{ field.config?.code }}"
              [element]="field"
              [ngClass]="field.config.uiStyles.attributes.cssClass"
              [hidden]="!field.visible"
            ></nm-paragraph>
          </ng-template>
        </ng-template>
      </div>
    </fieldset>
  `
})
export class CardDetailsFieldGroupComponent extends BaseElement {
  componentTypes = ComponentTypes;
}
