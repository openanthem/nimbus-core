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
import { WebContentSvc } from '../../../services/content-management.service';
import { TableComponentConstants } from './table.component.constants';
import { Param } from '../../../shared/param-state';
import { ParamConfig } from '../../../shared/param-config';
/**
 * \@author Sandeep Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
  selector: 'nm-th',
  template: `
   {{label}}
   `,
  providers: [WebContentSvc]
})
export class TableHeader {
    @Input() element: Param;
    @Input() paramConfig: ParamConfig;

    constructor(private _wcs: WebContentSvc) {

    }
    
    ngOnInit() {
        this.paramConfig['field'] = this.paramConfig.code;
        this.paramConfig.label = this._wcs.findLabelContentFromConfig(this.element.elemLabels.get(this.paramConfig.id), this.paramConfig.code).text;
        this.paramConfig['header'] = this.paramConfig.label;
        if (this.paramConfig.uiStyles.attributes.hidden) {
            this.paramConfig['exportable'] = false;
        } else {
            if (TableComponentConstants.allowedColumnStylesAlias.includes(this.paramConfig.uiStyles.attributes.alias)
                    || this.paramConfig.type.nested === true) {
                this.paramConfig['exportable'] = false;
            } else {
                this.paramConfig['exportable'] = true;
            }
        }
    }
    public get label(): string {
        return this.paramConfig.label;
    }

    
}
