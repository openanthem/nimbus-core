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

import { Component, forwardRef, Input } from '@angular/core';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ViewComponent } from '../../shared/param-annotations.enum';
import { Param } from './../../shared/param-state';
import { BaseElement } from './base-element.component';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => FrmGroupCmp),
  multi: true
};

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-frm-grp',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
  template: `
    <span
      [hidden]="!element.visible"
      [ngClass]="formElementGroupCss ? formElementGroupCss : ''"
    >
      <ng-template
        [ngIf]="
          element?.config?.uiStyles?.attributes?.alias ==
          viewComponent.formElementGroup.toString()
        "
      >
        <fieldset>
          <div class="fieldsetFlex">
            <nm-input-legend [element]="element"></nm-input-legend>
            <ng-template
              ngFor
              let-frmElem
              [ngForOf]="element.type.model.params"
            >
              <nm-frm-grp
                [element]="frmElem"
                [ngClass]="getElementClass(frmElem)"
                [hidden]="!frmElem.visible"
                [form]="form"
                [position]="position"
              >
              </nm-frm-grp>
            </ng-template>
          </div>
        </fieldset>
      </ng-template>

      <ng-template
        [ngIf]="
          !element.type?.model?.params?.length ||
          element.config?.type?.collection
        "
      >
        <nm-element
          id="{{ element.config?.code }}"
          [position]="position + 1"
          id="{{ id }}"
          [element]="element"
          [form]="form"
        ></nm-element>
      </ng-template>

      <ng-template
        [ngIf]="
          element?.config?.uiStyles?.attributes?.alias ===
          viewComponent.picklist.toString()
        "
      >
        <nm-element
          id="{{ element.config?.code }}"
          [position]="position + 1"
          id="{{ id }}"
          [element]="element"
          [form]="form"
        ></nm-element>
      </ng-template>

      <ng-template
        [ngIf]="
          element.config?.uiStyles?.attributes?.alias ==
          viewComponent.button.toString()
        "
      >
        <nm-button
          id="{{ element.config?.code }}"
          [form]="form"
          [element]="element"
        >
        </nm-button>
      </ng-template>

      <ng-template
        [ngIf]="
          element?.config?.uiStyles?.attributes?.alias ===
          viewComponent.formGridFiller.toString()
        "
      >
        <nm-form-grid-filler></nm-form-grid-filler>
      </ng-template>

      <ng-template
        [ngIf]="
          element.config?.uiStyles?.attributes?.alias ==
          viewComponent.link.toString()
        "
      >
        <nm-link id="{{ element.config?.code }}" [element]="element"> </nm-link>
      </ng-template>

      <ng-template
        [ngIf]="
          element.config?.uiStyles?.attributes?.alias ==
          viewComponent.paragraph.toString()
        "
      >
        <nm-paragraph
          id="{{ element.config?.code }}"
          [element]="element"
          [ngClass]="element.config.uiStyles.attributes.cssClass"
          [hidden]="!element.visible"
        ></nm-paragraph>
      </ng-template>

      <ng-template
        [ngIf]="
          element.config?.uiStyles?.attributes?.alias ==
          viewComponent.header.toString()
        "
      >
        <nm-header
          id="{{ element.config?.code }}"
          [element]="element"
        ></nm-header>
      </ng-template>

      <ng-template
        [ngIf]="
          element.config?.uiStyles?.attributes?.alias ==
          viewComponent.buttongroup.toString()
        "
      >
        <nm-button-group
          [form]="form"
          [buttonList]="element.type?.model?.params"
          class="{{
            element.config?.uiStyles?.attributes?.cssClass
          }} buttonGroup"
        >
        </nm-button-group>
      </ng-template>
    </span>
  `
})
export class FrmGroupCmp extends BaseElement {
  @Input() elements: Param[] = [];
  @Input() form: FormGroup;
  @Input() parentElement: Param;
  formElementGroupCss: String = '';
  viewComponent = ViewComponent;

  ngOnInit() {
    super.ngOnInit();
    this.updatePosition();
    this.formElementGroupCss = this.getCssClass();
  }

  getCssClass() {
    if (
      this.element.config.uiStyles &&
      this.element.config.uiStyles.attributes.alias ==
        ViewComponent.formElementGroup.toString()
    ) {
      if (this.element.config.uiStyles.attributes.cssClass) {
        return this.element.config.uiStyles.attributes.cssClass;
      } else {
        return '';
      }
    }
    return '';
  }

  getElementClass(child: Param) {
    if (child.config.uiStyles) {
      if (
        child.config.uiStyles.attributes &&
        child.config.uiStyles.attributes.cssClass &&
        child.config.uiStyles.attributes.cssClass != ''
      ) {
        return child.config.uiStyles.attributes.cssClass;
      }
    }
    return '';
  }
}
