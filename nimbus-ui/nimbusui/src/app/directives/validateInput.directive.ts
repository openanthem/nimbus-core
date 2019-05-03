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
import {ElementRef, Renderer2, Input, SimpleChanges } from '@angular/core';
import { ParamConfig } from '../shared/param-config';
import { ValidationErrors } from '@angular/forms';
import { Param } from '../shared/param-state';
/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */






import { Directive, forwardRef, Attribute } from '@angular/core';
import { Validator, AbstractControl, NG_VALIDATORS } from '@angular/forms';

@Directive({
    selector: '[nmValidator][formControlName],[nmValidator][formControl],[nmValidator][ngModel]',
    providers: [
        { provide: NG_VALIDATORS, useExisting: forwardRef(() => NmValidator), multi: true }
    ]
})
export class NmValidator implements Validator {
    constructor( @Attribute('nmValidator') public nmValidator: string) {}

    validate(c: AbstractControl): { [key: string]: any } {
        // self value (e.g. retype password)
        let v = c.value;
console.log('NmValidator is working', c);

        // control value (e.g. password)
        // let e = c.root.get(this.validateEqual);

        // value not equal
        // if (e && v !== e.value) return {
        //     validateEqual: false
        // }
        return {nmValidator:  'this is invalid...'};
    }
}




// @Directive({
//   selector: 'validateInput, [validateInput]'
// })
// export class ValidateInput implements Validator {

//     @Input("validateInput") element: Param;


//     constructor(
//         @Attribute('validateEqual') public validateEqual: string,
//         private elementRef: ElementRef
//     ) {    }
//     validate(control: AbstractControl): ValidationErrors| null {
// console.log('control...validateInput', control);
// return null;
//     }

//     validateInput(test) {
//         console.log('test', test);
        
//     }

//     ngOnInit() {
//         // console.log('this.element---validateInput', this.element);
//         // console.log('this.element.config.validation.constraints', this.element.config.validation.constraints, this.element);
//         if (this.element.config && this.element.config.validation && this.element.config.validation.constraints) {
//             for (let i = 0; i < this.element.config.validation.constraints.length; i++){
//                 if (this.element.config.validation.constraints[i].name == "NotNull"){
//                     console.log('notnull', this.element.config.code);
                    
//                 }
//             }
//         }

//         console.log('this.elementRef', this.elementRef);
        

//     }

    

// }
