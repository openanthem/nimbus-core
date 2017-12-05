/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */
'use strict';

import { Input, Directive } from '@angular/core';
import { FormControl } from '@angular/forms';

/**
 * \@whatItDoes A Directive to handle the form control disable/enable.
 * 
 * \@howToUse 
 * <nm-input id="{{id}}" [formControlName]="controlName"  [disableFormControl]="true/false"
        [disableCondition]="true/false" [element]="element" [form]="form"></nm-input>
 * 
 * Form Controls that use this directive can be enabled/disabled
 */
@Directive({
    selector: '[disableFormControl][disableCondition]'
})
export class DisableFormControl {
    @Input() disableFormControl: FormControl;

    constructor() { }

    get disableCondition(): boolean {
        return !!this.disableFormControl && this.disableFormControl.disabled;
    }

    @Input('disableCondition') set disableCondition(s: boolean) {
        //TODO - check if the validator required is present in the form control and throw error if we are trying to disable a mandatory control.
        if (!this.disableFormControl) {
            return;
        } else if (!s) {
            this.disableFormControl.disable();
        } else {
            this.disableFormControl.enable();
        }
    }
}
