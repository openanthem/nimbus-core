import { ValidationConstraint } from './../../shared/validationconstraints.enum';
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
import { Component, Input } from '@angular/core';
import { FormGroup, AbstractControlDirective, NgModel } from '@angular/forms';
import { Param, Constraint } from '../../shared/app-config.interface';

var counter = 0;

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-element',
    templateUrl: './form-element.component.html'
})

export class FormElement {
    @Input() element: Param;
    @Input() form: FormGroup;
    id: String = 'form-control' + counter++;
    @Input() elementCss: String;
    get isValid() {
        if (this.form.controls[this.element.config.code] != null) {
            return this.form.controls[this.element.config.code].valid;
        } else {
            return true;
        }
    }
    get isPristine() {
        if (this.element.config.code.startsWith('{')) {
            return true;
        }
        if (this.form.controls[this.element.config.code] != null) {
            return this.form.controls[this.element.config.code].pristine;
        } else {
            return true;
        }
    }
    get showErrors() {
        return (!this.isPristine && !this.isValid);
    }
    get showWarnings() {
        return (this.element.message != null && this.element.message.text != null && this.element.message.type === 'WARNING');
    }
    constructor() { }

    ngOnInit() {
        //console.log(JSON.stringify(this.elements));
        if (this.element.config.uiStyles && this.element.config.uiStyles.attributes.controlId !== null) {
            if (Number(this.element.config.uiStyles.attributes.controlId) % 2 === 0) {
                this.elementCss = this.elementCss + ' even';
            } else {
                this.elementCss = this.elementCss + ' odd';
            }
        }
    }

    getErrorStyles() {
        if (this.showErrors) {
            return 'alert alert-danger';
        }
        if (this.showWarnings) {
            return 'alert alert-warning';
        }
    }

    getElementStyle() {
        if (this.element.config.uiStyles != null && this.element.config.uiStyles.attributes.alias === 'MultiSelectCard') {
            return 'col-lg-12 col-md-6';
        } else {
            return '';
        }
    }
    getWarnings() {
        const warnings: string[] = [];
        if (this.element.message != null && this.element.message.type === 'WARNING' ) {
            warnings.push(this.element.message.text);
        }
       return warnings;
    }
    getErrors() {
        let errors: string[] = [];

        if (this.form.controls[this.element.config.code].invalid) {
            if (this.element.config.validation) {
                this.element.config.validation.constraints.forEach(validator => {
                    // Defaults
                    // TODO - Consider moving this logic to a lookup service.
                    if (this.form.controls[this.element.config.code].errors.required && validator.name === ValidationConstraint._notNull.value) {
                        if (!this.checkforCustomMessages(validator, errors))
                            errors.push('Field is required.');
                    }
                    if (this.form.controls[this.element.config.code].errors.pattern && validator.name === ValidationConstraint._pattern.value) {
                        if (!this.checkforCustomMessages(validator, errors))
                            errors.push('RegEx is invalid.');  
                    }
                    if (this.form.controls[this.element.config.code].errors.minMaxSelection && validator.name === ValidationConstraint._size.value) {
                        if (!this.checkforCustomMessages(validator, errors))
                            errors.push('Required number of fields not met.'); 
                    }
                    if (this.form.controls[this.element.config.code].errors.isNumber) {
                        //if (!this.checkforCustomMessages(validator, errors))
                            errors.push('Value must be a number.');
                    }
                    //}
                });
            }
            return errors;
        }
    }

    checkforCustomMessages(validator: Constraint, errors: string[]): boolean {
        if (validator.attribute.message) {
            errors.push(validator.attribute.message);
            return true;
        }
        return false;
    }

    ngModelState(ngm: AbstractControlDirective): string {
        let ret = ngm instanceof NgModel ? `name: ${ngm.name};  ` : '';
        return ret + `touched: ${ngm.touched};  pristine: ${ngm.pristine};  valid: ${ngm.valid};  errors: ${JSON.stringify(ngm.errors)};  value: ${JSON.stringify(ngm.value)}`;
    }
}
