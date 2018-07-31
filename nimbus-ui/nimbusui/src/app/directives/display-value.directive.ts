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
import { Directive, ElementRef, Renderer, Input } from '@angular/core';
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

    constructor(private el: ElementRef, private renderer: Renderer) {
    }

    ngOnInit() {
        if (this.config && this.config.uiStyles.attributes.applyValueStyles) {
            this.renderer.setElementClass(this.el.nativeElement, this.config.code, true); // Field Name
            this.renderer.setElementClass(this.el.nativeElement, this.displayValue, true); // Field Value
        }
    }
}
