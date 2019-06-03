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
  Attribute,
  Directive,
  ElementRef,
  EventEmitter,
  forwardRef,
  Input,
  OnInit,
  Output
} from '@angular/core';
import { AbstractControl, NG_VALIDATORS, Validator } from '@angular/forms';
import { ValidationUtils } from '../components/platform/validators/ValidationUtils';
import { ControlSubscribers } from '../services/control-subscribers.service';
import { PageService } from '../services/page.service';
import { Param } from '../shared/param-state';
import { ValidationConstraint } from '../shared/validationconstraints.enum';

/**
 * \@author Purnachander.Mashetty
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Directive({
  selector:
    '[nmValidator][formControlName],[nmValidator][formControl],[nmValidator][ngModel]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => NmValidator),
      multi: true
    },
    ControlSubscribers
  ]
})
export class NmValidator implements Validator, OnInit {
  @Input('nmValidator') element: Param;
  @Output() nmValidate: EventEmitter<any> = new EventEmitter();
  vFun: any;

  constructor(
    @Attribute('nmValidator') public nmValidator: string,
    private pageSvc: PageService,
    private elementRef: ElementRef
  ) {}

  validate(c: AbstractControl): { [key: string]: any } {
    const err = { nmValidator: [], param: this.element };
    let hasError = false;
    if (this.vFun.length > 0) {
      for (const key in this.vFun) {
        if (this.vFun.hasOwnProperty(key)) {
          const validator = this.vFun[key](c);
          const value = c.value;

          // not null validator
          if (validator && validator['required']) {
            if (!value) {
              hasError = true;
              for (const constraint of this.element.config.validation
                .constraints) {
                if (
                  constraint.name === ValidationConstraint._notNull.toString()
                ) {
                  err.nmValidator.push(constraint.attribute.message);
                }
              }
            }
          }

          // regexp validator
          if (
            validator &&
            validator['pattern'] &&
            validator['pattern']['requiredPattern']
          ) {
            const regex = new RegExp(validator['pattern']['requiredPattern']);
            if (!regex.test(value)) {
              hasError = true;
              for (const constraint of this.element.config.validation
                .constraints) {
                if (
                  constraint.name === ValidationConstraint._pattern.toString()
                ) {
                  err.nmValidator.push(constraint.attribute.message);
                }
              }
            }
          }
        }
      }
    }

    this.nmValidate.emit({ nmValidator: err.nmValidator, param: this.element });
    if (!hasError) {
      return null;
    }
    return err;
  }

  ngOnInit() {
    this.vFun = ValidationUtils.buildStaticValidations(this.element);
  }
}
