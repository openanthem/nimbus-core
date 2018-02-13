import { Validators, ValidatorFn } from '@angular/forms';
import { CustomValidators } from './custom.validators';
import { Param, Constraint } from '../../../shared/app-config.interface';
import { FormControl } from '@angular/forms/src/model';

export class ValidationUtils {

    static buildStaticValidations(element:Param) :ValidatorFn[] {
        var checks: ValidatorFn[] = [];
         if (element.config.validation) {
            element.config.validation.constraints.forEach(validator => {
                if(validator.group == null || validator.group == '') {
                    checks.push(this.constructValidations(validator, element.config.uiStyles.attributes.alias));
                }
            });
         }
         return checks;
     }

     static buildDynamicValidations(element:Param, group: string) :ValidatorFn[] {
        var checks: ValidatorFn[] = [];
         if (element.config.validation) {
            element.config.validation.constraints.forEach(validator => {
                if(validator.group === group) {
                    checks.push(this.constructValidations(validator, element.config.uiStyles.attributes.alias));
                }
            });
         }
         return checks;
     }

     static constructValidations(validator: Constraint, controlAlias: string) : ValidatorFn {
        if (validator.name === 'NotNull') {
            if(controlAlias == 'CheckBox') {
              return Validators.requiredTrue;
            } else {
              return Validators.required;
            }
          }
          else if (validator.name === 'Pattern') {
            return Validators.pattern(validator.attribute.regexp);
          }
          else if (validator.name === 'Size') {
            return CustomValidators.minMaxSelection(controlAlias, validator.attribute);
          }
          else if (validator.name === 'isNumber') {
            return CustomValidators.isNumber;
          }
          else if (validator.name === 'isZip') {
            return CustomValidators.isZip;
          }    
    }
}