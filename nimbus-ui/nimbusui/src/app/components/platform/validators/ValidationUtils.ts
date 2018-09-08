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
            element.config.validation.constraints.forEach(validator => {
                if(validator.attribute != null && validator.attribute.groups == null || validator.attribute.groups.length == 0) {
                    checks.push(ValidationUtils.constructValidations(validator, element.config.uiStyles.attributes.alias));
                }
            });
         }
         return checks;
     }

     static buildDynamicValidations(element:Param, groups: String[]) :ValidatorFn[] {
        var checks: ValidatorFn[] = [];
         if (element.config.validation) {
            element.config.validation.constraints.forEach(validator => {
                groups.forEach(group => {
                    if(validator.attribute.groups.some(x => x === group)) {
                        var check = ValidationUtils.constructValidations(validator, element.config.uiStyles.attributes.alias)
                        checks.push(check);
                    }
                });
            });
         }
         return checks;
     }

     static constructValidations(validator: Constraint, controlAlias: string) : ValidatorFn {
        if (validator.name === ValidationConstraint._notNull.value) {
            if(controlAlias == 'CheckBox') {
              return Validators.requiredTrue;
            } else {
              return Validators.required;
            }
          }
          else if (validator.name === ValidationConstraint._pattern.value) {
            return Validators.pattern(validator.attribute.regexp);
          }
          else if (validator.name === ValidationConstraint._size.value) {
            return CustomValidators.minMaxSelection(controlAlias, validator.attribute);
          }
         else if (validator.name === ValidationConstraint._max.value) {
              return Validators.maxLength(validator.attribute.value);
          }
          else if (validator.name === ValidationConstraint._number.value) {
            return CustomValidators.isNumber;
          }
          else if (validator.name === ValidationConstraint._zip.value) {
            return CustomValidators.isZip;
          }
          else if (validator.name === ValidationConstraint._past.value) {
            return  Validators.nullValidator;
          }
          else if (validator.name === ValidationConstraint._future.value) {
            return  Validators.nullValidator;
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

    /**
     * Retrieve the default error message for a given errorType.
     * @param validationName the errorType for which to retrieve an error message
     */
    public static getDefaultErrorMessage(validationName: string): string {
        return ServiceConstants.ERROR_MESSAGE_DEFAULTS[validationName];
    }
    
    /**
     * Retrieve all of the error names that validation rules are applied for.
     */
    public static getAllValidationNames(): string[] {
        return Object.keys(ServiceConstants.ERROR_MESSAGE_DEFAULTS);
    }
}