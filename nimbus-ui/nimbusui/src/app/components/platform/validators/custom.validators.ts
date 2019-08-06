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
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn
} from '@angular/forms';
import { Attribute } from './../../../shared/param-config';
import evaluate, { registerFunction } from 'ts-expression-evaluator'
import { RuleSet } from './../../../shared/param-config';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
export class CustomValidators {
  static isNumber(control: FormControl) {
    if (isNaN(+control.value)) {
      return { isNumber: true };
    }
    return null;
  }

  static isZip(control: FormControl) {
    let regex = /^\d{5}/;
    if (control.value) {
      return regex.test(control.value) ? null : { isZip: true };
    }
    return null;
  }

  static formGroupNotNull = (validator: ValidatorFn) => (
    group: FormGroup
  ): ValidationErrors | null => {
    const hasValue =
      group &&
      group.controls &&
      Object.keys(group.controls).some(k => !validator(group.controls[k]));

    return hasValue
      ? null
      : {
          formGroupNotNull: true
        };
  };

  static validationrule(ruleset: RuleSet[]) {
    return (grp: FormGroup) => {
      if(grp) {
        let error = [];
        if(grp.pristine) {
          return null;
        }
        registerFunction('findStateByPath', (code: string) => {
          let pathArray = code.split('/');
          let c = '';
          if(pathArray.length > 0) {
            c = pathArray[pathArray.length - 1]
          } else {
            return null;
          }
          if(grp.controls)
            return grp.controls[c]? grp.controls[c].value : null;
          else
            return null;
        });
        ruleset.forEach(rs => {
          let valid = evaluate(rs.rule, grp);
          if(!valid) {
            error.push(rs.message);
          }
        });
        if(error.length > 0) {
          return {validationrule: error};
        }
      }
    return null;
    };
  }
  /*
   * custom validator for @Size annotation
   * This will validate the minimum/ maximum fields selected in a checkboxgroup; can be exteded to Multi-select group or other group element
   * if a condition is violated, minMaxSelection flag will be set to true indicating a validation error.
   */
  static minMaxSelection(alias: string, attribute: Attribute) {
    return (control: FormControl) => {
      if (control.value != null) {
        if (attribute.min > 0 && control.value.length < attribute.min) {
          return {
            minMaxSelection: true
          };
        }
        if (attribute.max > 0 && control.value.length > attribute.max) {
          return {
            minMaxSelection: true
          };
        }
      }
      return null;
    };
  }

  static isNotEmpty(control: FormControl) {
    if (control.value && control.value.trim()) {
      return null;
    } else {
      return {
        isNotEmpty: true
      };
    }
  }

  static isPast(control: FormControl) {
    let today = new Date();
    today.setHours(0, 0, 0, 0);
    if (control.value != null) {
      if (control.value.getTime() > today.getTime()) {
        return { isPast: true };
      }
    }
    return null;
  }

  static isFuture(control: FormControl) {
    let today = new Date();
    today.setHours(0, 0, 0, 0);
    if (control.value != null) {
      if (control.value.getTime() < today.getTime()) {
        return { isFuture: true };
      }
    }
    return null;
  }
}
