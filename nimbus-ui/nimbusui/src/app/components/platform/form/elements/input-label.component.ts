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
import { PageService } from './../../../../services/page.service';
import { WebContentSvc } from './../../../../services/content-management.service';
import { BaseLabel } from '../../base-label.component';

/**
 * \@author Tony Lopez
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
  selector: 'nm-input-label',
  template: `
    <label *ngIf="label"
        [className]="getCssClass()"
        [attr.for]="for">
        
        {{label}} 
        
        <nm-tooltip *ngIf="helpText" [helpText]='helpText'></nm-tooltip>
    </label>
   `
})
export class InputLabel extends BaseLabel {

    @Input() for: string;
    @Input() required: boolean;
    
    constructor(private wcs: WebContentSvc, private pageService: PageService) {
        super(wcs, pageService);
    }

    /**
     * Get the css classes to apply for this element.
     */
    public getCssClass(): string {
        let cssClass = super.getCssClass();
        if (this.required) {
            if (cssClass.trim().length !== 0) {
                cssClass += ' ';
            }
            cssClass += 'required';
        }
        return cssClass;
    }
}
