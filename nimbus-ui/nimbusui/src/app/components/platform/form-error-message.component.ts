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
import { Component, Directive, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Param } from './../../shared/param-state';
import { ValidationUtils } from './validators/ValidationUtils';
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component( {
    selector: 'nm-counter-message',
    template: `
        <div>
            {{displayMessage()}}
        </div>
    `
})
export class FormErrorMessage {
    @Input() element: Param;
    @Input() form: FormGroup;
    mandatoryLeft: number = 0;
    totalCount: number = 0;
    totalMandtoryCount: number = 0;
    constructor() {
    }

    displayMessage() {
        this.mandatoryLeft = 0;
        this.totalCount = 0;
        this.totalMandtoryCount = 0;
        this.calculateFieldCount(this.element);
        return 'Required: '+ (this.totalMandtoryCount - this.mandatoryLeft) +' of '+ this.totalMandtoryCount;
    }

    calculateFieldCount(param: Param) {
        if(param.type.model) {
            param.type.model.params.forEach(element => {
                if(element.visible) {
                    if(element.type.model && element.type.model.params ) {
                        this.calculateFieldCount(element);
                    } else {
                        this.totalCount++;
                        //below condition will evaluate static and dynamic validation(groups)
                        if(ValidationUtils.applyelementStyle(element) || ValidationUtils.createRequired(element,element.activeValidationGroups)) {
                            this.totalMandtoryCount++;
                            this.checkControlValidity(this.form,element.config.code)
                        }
                    }
                } 
                
            });
        }
             
    }

    checkControlValidity(formGroup: FormGroup, code: string){
        Object.keys(formGroup.controls).forEach(field => {
            let ctrl = formGroup.controls[field];
            if(ctrl instanceof FormControl) {
                if(field === code && ctrl.errors) {
                        this.mandatoryLeft++;
                }
            } else if (ctrl instanceof FormGroup) {
                this.checkControlValidity(ctrl, code);
            }
       });
    }

}
