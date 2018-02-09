/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */
import { ControlValueAccessor } from '@angular/forms';
import {BaseElement} from '../../base-element.component';

/**
 * \@author Dinakar.Meda, Sandeep Mantha
 * \@whatItDoes A Base class for Controls that deal with ValueAccessors. Example- Textbox, Combobox etc.
 * 
 * \@howToUse 
 * 
 * Every UI Form Component will extend this base class. This class provides the value accessors
 */
//export class BaseControlValueAccessor<T> extends BaseElement implements ControlValueAccessor {
export class BaseControlValueAccessor<T> implements ControlValueAccessor {
    private innerValue: T;

  private changed = new Array<(value: T) => void>();
  private touched = new Array<() => void>();

  get value(): T {
    return this.innerValue;
  }

  set value(value: T) {
    if (this.innerValue !== value) {
      this.innerValue = value;
      this.changed.forEach(f => f(value));
    }
  }

  touch() {
    this.touched.forEach(f => f());
  }

  writeValue(value: T) {
    this.innerValue = value;
  }

  registerOnChange(fn: (value: T) => void) {
    this.changed.push(fn);
  }

  registerOnTouched(fn: () => void) {
    this.touched.push(fn);
  }
}
