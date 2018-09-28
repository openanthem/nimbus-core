/**
 * @license
 * Copyright 2016-2018 the original author or authors.
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
import { FormControl, ValidatorFn, FormGroup, ValidationErrors } from '@angular/forms';
import { Attribute } from './../../../shared/param-config';

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

    static formGroupNotNull = (validator: ValidatorFn) => (group: FormGroup): ValidationErrors | null => {
        const hasValue = group && group.controls && Object.keys(group.controls)
            .some(k => !validator(group.controls[k]));

        return hasValue ? null : {
            formGroupNotNull: true,
        };
    };

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
