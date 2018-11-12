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

import { ParamUtils } from './../shared/param-utils';
import { Param } from './../shared/param-state';
import { Component, Input, TemplateRef, ContentChild } from '@angular/core';
import { PageService } from './../services/page.service';

/**
 * \@author Tony Lopez
 * \@whatItDoes 
 *  Handles any common transformations when displaying the value received for a param on the server.
 * 
 * \@howToUse
 *  Include [nmDisplayValue] as an attribute on an element and provide a 
 *  template reference to #body to be able to utilize a template instance variable via "let-displayValue".
 *  displayValue the can be used in accordance to the template reference.
 * 
 */
@Component({ 
    selector: '[nmDisplayValue]',
    template: `
        <ng-container *ngTemplateOutlet="bodyTemplate; context: { $implicit: displayValue }"></ng-container>
    `
})
export class DisplayValue {

    @Input("nmDisplayValue") element: Param;

    @ContentChild('body') bodyTemplate: TemplateRef<any>;

    displayValue: string;

    constructor(private pageService: PageService) {
        
    }

    ngOnInit() {
        this.displayValue = this.determineDisplayValue(this.element);
    }

    ngAfterViewInit() {
        this.pageService.eventUpdate.subscribe(event => {
            if (event.path == this.element.path) {
                this.displayValue = this.determineDisplayValue(event);
            }
        });
    }
    
    private determineDisplayValue(param: Param): string {
        let foundValuesLabel = ParamUtils.getValuesLabelMatchingLeafState(param)
        if (foundValuesLabel) {
            return foundValuesLabel;
        }

        let foundPlaceholder;
        if (!param.leafState && (foundPlaceholder = ParamUtils.getPlaceholder(param))) {
            return foundPlaceholder;
        }

        return param.leafState;
    }
}