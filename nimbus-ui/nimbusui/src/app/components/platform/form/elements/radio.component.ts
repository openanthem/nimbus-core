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
import { BaseControl } from './base-control.component';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => RadioButton),
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
  selector: 'nm-input-radio',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, ControlSubscribers],
  template: `
    <fieldset>
      <div class="fieldsetFlex">
        <nm-input-legend [element]="element" [required]="requiredCss">
        </nm-input-legend>
        <div class="checkboxHolder">
          <div
            class="form-checkrow"
            *ngFor="let val of element?.values; let i = index"
          >
            <p-radioButton
              name="{{ element?.config?.code }}"
              [(ngModel)]="value"
              [disabled]="disabled"
              [value]="val.code"
              [label]="val.label"
              (ngModelChange)="emitValueChangedEvent(this,$event)"
            >
            </p-radioButton>
          </div>
        </div>
      </div>
    </fieldset>
  `
})
export class RadioButton extends BaseControl<String> {
  @ViewChild(NgModel) model: NgModel;

  constructor(
    controlService: ControlSubscribers,
    cd: ChangeDetectorRef,
    cms: CounterMessageService
  ) {
    super(controlService, cd, cms);
  }
}
