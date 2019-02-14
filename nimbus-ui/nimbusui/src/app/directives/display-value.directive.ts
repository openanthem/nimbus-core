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
import { Directive, ElementRef, Renderer2, Input, SimpleChanges } from '@angular/core';
import { ParamConfig } from '../shared/param-config';
/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Directive({
  selector: '[nmDisplayValue]'
})
export class DisplayValueDirective {
    @Input('nmDisplayValue') displayValue: any;
    @Input('config') config: ParamConfig;
    static placeholder: string = 'placeholder';

    constructor(private el: ElementRef, private renderer: Renderer2) {
    }

    /*
        Initialize the component with the styles. The field name and value are applied as style classes 
        if the applyValueStyles attribute is set to true.
    */
    ngOnInit() {
        if (this.config && this.config.uiStyles.attributes.applyValueStyles) {
            this.renderer.addClass(this.el.nativeElement, this.config.code); // Field Name

            if (this.displayValue && ((this.displayValue instanceof String && this.displayValue.trim() !== '')
                                || this.displayValue === '')) {
                this.renderer.addClass(this.el.nativeElement, this.getValue(this.displayValue)); // Field Value
            } else {
                this.renderer.addClass(this.el.nativeElement, DisplayValueDirective.placeholder); // placeholder Value
            }
        }
    }

    /*
        Handle value changes 
    */
    ngOnChanges(changes: SimpleChanges) {
        if (this.config && this.config.uiStyles.attributes.applyValueStyles) {
            // Remove the previous value styles
            if (changes.displayValue.previousValue === undefined) {
                this.renderer.removeClass(this.el.nativeElement, DisplayValueDirective.placeholder);
            } else {
                this.renderer.removeClass(this.el.nativeElement, this.getValue(changes.displayValue.previousValue));
            }
            // Add current value styles
            if (changes.displayValue.currentValue === undefined || changes.displayValue.currentValue === '' ||
                (changes.displayValue.currentValue instanceof String && changes.displayValue.currentValue.trim() === '') ||
                changes.displayValue.currentValue === null) {
                this.renderer.addClass(this.el.nativeElement, DisplayValueDirective.placeholder);
            } else {
                this.renderer.addClass(this.el.nativeElement, this.getValue(changes.displayValue.currentValue));
            }
        }
    }

     /*
        Remove spaces from value since style class cannot take spaces
     */
    getValue(val: any): any {
        var re = / /gi;
        if (typeof val === 'string') {
            return val.replace(re, "");
        } else {
            return val;
        }
    }
}
