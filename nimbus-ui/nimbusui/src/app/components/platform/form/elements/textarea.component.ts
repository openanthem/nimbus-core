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

import {
  ChangeDetectorRef,
  Component,
  forwardRef,
  ViewChild
} from '@angular/core';
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';
import { CounterMessageService } from './../../../../services/counter-message.service';
import { ValidationConstraint } from './../../../../shared/validationconstraints.enum';
import { BaseControl } from './base-control.component';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => TextArea),
  multi: true
};

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *  By providing a value for @Max on top of @TextArea would restrict
 *  characters max length in @TextArea
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-input-textarea',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, ControlSubscribers],
  template: `
    <div
      class="textarea-holder"
      [hidden]="!element?.visible"
      *ngIf="element.config?.uiStyles?.attributes?.hidden == false"
    >
      <div
        class="number"
        *ngIf="element.config?.uiStyles?.attributes?.controlId != ''"
      >
        {{ element.config?.uiStyles?.attributes?.controlId }}
      </div>
      <nm-input-label
        *ngIf="!isLabelEmpty"
        [element]="element"
        [for]="element.config?.code"
        [required]="requiredCss"
      >
      </nm-input-label>
      <textarea
        [(ngModel)]="value"
        autocomplete="off"
        autocorrect="off"
        autocapitalize="off"
        spellcheck="false"
        [rows]="element.config?.uiStyles?.attributes?.rows"
        (focusout)="emitValueChangedEvent(this,value)"
        [disabled]="disabled"
        [maxlength]="maxlength"
        [id]="element.config?.code"
        class="form-control textarea-input"
        *ngIf="element.config?.uiStyles?.attributes?.readOnly == false"
      ></textarea>

      <span class="charCount" *ngIf="maxlength > 0"
        >{{ maxlength - (value ? value.length : 0) }} Characters left</span
      >
      <pre
        class="print-only"
        *ngIf="element.config?.uiStyles?.attributes?.readOnly == false"
        >{{ this.value }}</pre
      >
      <p
        style="margin-bottom:0rem;"
        *ngIf="element.config?.uiStyles?.attributes?.readOnly == true"
      >
        {{ element.leafState }}
      </p>
    </div>
  `
})
export class TextArea extends BaseControl<String> {
  @ViewChild(NgModel) model: NgModel;
  maxlength: number;

  constructor(
    controlService: ControlSubscribers,
    cd: ChangeDetectorRef,
    cms: CounterMessageService
  ) {
    super(controlService, cd, cms);
  }

  ngOnInit() {
    super.ngOnInit();
    this.maxlength = this.getMaxLength();
  }

  /**
   * Get Max length of the attribute
   */
  public getMaxLength(): number {
    let constraint = this.getConstraint(ValidationConstraint._max.value);
    if (constraint) {
      return constraint.attribute.value;
    } else {
      return;
    }
  }
}
