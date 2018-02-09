import { FormControl, ValidatorFn, FormGroup, ValidationErrors } from '@angular/forms';
import { Attribute } from './../../../shared/app-config.interface';

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
}
