import { ValidationConstraint } from './../../../shared/validationconstraints.enum';
import { Validators, ValidatorFn } from '@angular/forms';
import { CustomValidators } from './custom.validators';
import { Param, Constraint } from '../../../shared/app-config.interface';
import { FormControl } from '@angular/forms/src/model';

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
                    checks.push(this.constructValidations(validator, element.config.uiStyles.attributes.alias));
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
                        var check = this.constructValidations(validator, element.config.uiStyles.attributes.alias)
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
          else if (validator.name === ValidationConstraint._number.value) {
            return CustomValidators.isNumber;
          }
          else if (validator.name === ValidationConstraint._zip.value) {
            return CustomValidators.isZip;
          }    
    }
}