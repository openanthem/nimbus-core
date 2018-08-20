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
import { LabelConfig } from '../../../../shared/param-config';

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
    <label
        [ngClass]="{'required': required, '': !required}"
        [attr.for]="for">
        
        {{labelConfig?.text}} 
        
        <nm-tooltip *ngIf="labelConfig?.helpText" 
            [helpText]='labelConfig.helpText'>
        </nm-tooltip>
    </label>
   `
})
export class InputLabel {

    @Input() labelConfig: LabelConfig;
    @Input() for: string;
    @Input() required: boolean;

    constructor() {
        
    }
}
