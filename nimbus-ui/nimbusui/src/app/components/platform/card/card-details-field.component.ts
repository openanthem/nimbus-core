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
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ComponentTypes } from '../../../shared/param-annotations.enum';
import { Param } from '../../../shared/param-state';
import { ViewConfig } from './../../../shared/param-annotations.enum';
import { UiNature } from './../../../shared/param-config';
import { ParamUtils } from './../../../shared/param-utils';
import { BaseElement } from './../base-element.component';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => CardDetailsFieldComponent),
  multi: true
};

/**
 * \@author Dinakar.Meda
 * @author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-card-details-field',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
  templateUrl: './card-details-field.component.html'
})
export class CardDetailsFieldComponent extends BaseElement
  implements ControlValueAccessor {
  @Input() element: Param;
  @Input('value') _value = '';
  private fieldClass: string = 'col-sm-3'; // occupies 1 col of 4
  componentTypes = ComponentTypes;
  toolTip: UiNature;

  ngOnInit() {
    super.ngOnInit();
    this.toolTip = ParamUtils.getUiNature(
      this.element,
      ViewConfig.tooltip.toString()
    );
  }

  onChange: any = () => {};
  onTouched: any = () => {};
  get value() {
    return this._value;
  }

  set value(val) {
    if (this.element.values.length > 0) {
      let desc: string = undefined;
      let indexVal: any = undefined;
      for (let i = 0; i < this.element.values.length; i++) {
        indexVal = this.element.values[i];
        if (indexVal.code === this.element.leafState) {
          desc = indexVal.label;
          break;
        }
      }
      if (desc) {
        this._value = desc;
      } else {
        this._value = this.element.leafState;
      }
    } else {
      this._value = this.element.leafState;
    }
    this.onChange(val);
    this.onTouched();
  }

  registerOnChange(fn) {
    this.onChange = fn;
  }

  writeValue(value) {
    if (value) {
      this.value = value;
    }
  }

  registerOnTouched(fn) {
    this.onTouched = fn;
  }
}
