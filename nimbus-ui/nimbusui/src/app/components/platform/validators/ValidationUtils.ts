import { ServiceConstants } from './../../../services/service.constants';
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

import { ValidationConstraint } from './../../../shared/validationconstraints.enum';
import { Validators, ValidatorFn } from '@angular/forms';
import { CustomValidators } from './custom.validators';
import { Constraint } from '../../../shared/param-config';
import { Param } from '../../../shared/param-state';
import { FormControl, AbstractControl } from '@angular/forms/src/model';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

export class ValidationUtils {

    static buildStaticValidations(element:Param) :ValidatorFn[] {
        var checks: ValidatorFn[] = [];
         if (element.config.validation) {
            element.config.validation.constraints.forEach(constraint => {
                if(constraint.attribute != null && constraint.attribute.groups == null || constraint.attribute.groups.length == 0) {
                    ValidationUtils.addCheckSafely(checks, constraint, element);
                }
            });
         }
         return checks;
     }

     static buildDynamicValidations(element:Param, groups: String[]) :ValidatorFn[] {
        var checks: ValidatorFn[] = [];
         if (element.config.validation) {
            element.config.validation.constraints.forEach(constraint => {
                groups.forEach(group => {
                    if(constraint.attribute.groups.some(x => x === group)) {
                        ValidationUtils.addCheckSafely(checks, constraint, element);
                    }
                });
            });
         }
         return checks;
     }

    private static addCheckSafely(checks: ValidatorFn[], constraint: Constraint, element: Param) {
        var check = ValidationUtils.constructValidations(constraint, element.config.uiStyles.attributes.alias)
        if (check) {
            checks.push(check);
        } else {
            // TODO - Refactor ValidatonUtils to be a service so that we can inject the LoggerService and output
            // the error below. For now, we'll silence it.
            //console.error(`UI Validation is not supported for @${constraint.name}. Remove @${constraint.name} from '${element.config.code}'.`);
        }
    }

     static constructValidations(constraint: Constraint, controlAlias: string) : ValidatorFn {
        if (constraint.name === ValidationConstraint._notNull.value) {
            if(controlAlias == 'CheckBox') {
              return Validators.requiredTrue;
            } else {
              return Validators.required;
            }
          }
          else if (constraint.name === ValidationConstraint._pattern.value) {
            return Validators.pattern(constraint.attribute.regexp);
          }
          else if (constraint.name === ValidationConstraint._size.value) {
            return CustomValidators.minMaxSelection(controlAlias, constraint.attribute);
          }
         else if (constraint.name === ValidationConstraint._max.value) {
              return Validators.maxLength(constraint.attribute.value);
          }
          else if (constraint.name === ValidationConstraint._number.value) {
            return CustomValidators.isNumber;
          }
          else if (constraint.name === ValidationConstraint._zip.value) {
            return CustomValidators.isZip;
          }
          else if (constraint.name === ValidationConstraint._past.value) {
            return CustomValidators.isPast;
          }
          else if (constraint.name === ValidationConstraint._future.value) {
            return CustomValidators.isFuture;
          }
    }

    static createRequired(element:Param, groups: String[]) :boolean {
        var required: boolean = false;
         if (element.config.validation) {
            element.config.validation.constraints.forEach(validator => {
                groups.forEach(group => {
                    if(validator.attribute.groups.some(x => x === group) && validator.name == ValidationConstraint._notNull.value) {
                        required = true;
                    }
                });
            });
         }
         return required;
     }

     static assessControlValidation(event:Param,frmCtrl:AbstractControl) {
        if(event.enabled && event.visible) {
            frmCtrl.enable();   
        }
        else {
            frmCtrl.disable();
        }
     }

     static rebindValidations(frmCtrl:AbstractControl,groups:String[],param:Param) : boolean{
        var staticChecks: ValidatorFn[] = [];
        var dynamicChecks: ValidatorFn[] = [];
        let requiredCss:boolean = false;
        staticChecks = ValidationUtils.buildStaticValidations(param);
        //merge the static and dynamic validations and overwrite the form control's validators
        dynamicChecks = ValidationUtils.buildDynamicValidations(param, groups);
        requiredCss = ValidationUtils.createRequired(param, groups);
        frmCtrl.setValidators(dynamicChecks.concat(staticChecks));
        return requiredCss;
    }

    /**
     * Check if control is required
     */
    static applyelementStyle(element: Param): boolean {
        let requiredCss = false;
        if (element && element.config && element.config.validation) {
            element.config.validation.constraints.forEach(validator => {
                if (validator.name === ValidationConstraint._notNull.value && 
                    validator.attribute != null && validator.attribute.groups.length == 0) {
                    //style = 'required';
                    requiredCss = true;
                }
            });
        }
        return requiredCss;
    }
}