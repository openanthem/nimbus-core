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
import { ValidationConstraint } from '../../../../shared/validationconstraints.enum';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';
import { CounterMessageService } from './../../../../services/counter-message.service';
import { BaseControl } from './base-control.component';

/**
 * \@author Sandeep Mantha
 * \@whatItDoes A control to be used when the user input is in the form of date or date with time or just the time.
 */

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => Calendar),
  multi: true
};

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-input-calendar',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, ControlSubscribers],
  template: `
    <nm-input-label
      *ngIf="!isLabelEmpty && !this.hideLabel"
      [element]="element"
      [for]="element.config?.code"
      [required]="requiredCss"
    >
    </nm-input-label>
    <p-calendar
      [(ngModel)]="value"
      (onSelect)="emitValueChangedEvent(this,$event)"
      (change)="emitValueChangedEvent(this,$event)"
      [showIcon]="true"
      [timeOnly]="element.config?.uiStyles?.attributes?.timeOnly"
      [showTime]="element.config?.uiStyles?.attributes?.showTime"
      [hourFormat]="element.config?.uiStyles?.attributes?.hourFormat"
      [monthNavigator]="element.config?.uiStyles?.attributes?.monthNavigator"
      [yearNavigator]="element.config?.uiStyles?.attributes?.yearNavigator"
      [readonlyInput]="element.config?.uiStyles?.attributes?.readonlyInput"
      [yearRange]="element.config?.uiStyles?.attributes?.yearRange"
      [maxDate]="maxDate"
      [minDate]="minDate"
      [disabled]="disabled"
    >
    </p-calendar>
  `
})
export class Calendar extends BaseControl<Date> {
  minDate: Date;
  maxDate: Date;
  @ViewChild(NgModel) model: NgModel;

  constructor(
    controlService: ControlSubscribers,
    cd: ChangeDetectorRef,
    cms: CounterMessageService
  ) {
    super(controlService, cd, cms);
  }

  ngOnInit() {
    super.ngOnInit();
    this.applyDateConstraint();
  }

  private applyDateConstraint() {
    let pastConstraint = this.getConstraint(ValidationConstraint._past.value);
    let futureConstraint = this.getConstraint(
      ValidationConstraint._future.value
    );

    if (pastConstraint) {
      this.maxDate = new Date();
    }
    if (futureConstraint) {
      this.minDate = new Date();
    }
  }
}
