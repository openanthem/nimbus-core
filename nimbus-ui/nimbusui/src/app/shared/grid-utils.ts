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

import {Injectable} from "@angular/core";
import { ParamConfig } from './param-config';
import { BaseElement } from '../components/platform/base-element.component';
import { DateTimeFormatPipe } from '../pipes/date.pipe';
import { WebContentSvc } from '../services/content-management.service';
import { ViewComponent} from './param-annotations.enum';

/**
 * \@author Vivek Kamineni
 * \@whatItDoes   Utility class for Grids (Table and TreeGrid Component)
 * 
 * \@howToUse 
 * 
 */

@Injectable()
 export class GridUtils extends BaseElement{

    viewComponent = ViewComponent;
    
    constructor(private _wcs: WebContentSvc, private dtFormat: DateTimeFormatPipe) {
        super(_wcs);
    }

    getCellDisplayValue(rowData: any, col: ParamConfig) {
        let cellData = rowData[col.code];
        if (cellData) {
            if (super.isDate(col.type.name)) {
                return this.dtFormat.transform(cellData, col.uiStyles.attributes.datePattern, col.type.name);
            } else {
                return cellData;
            }
        }
        else {
            return col.uiStyles.attributes.placeholder;
        }
    }


    showHeader(col: ParamConfig) {
        if (col.uiStyles && col.uiStyles.attributes.hidden === false &&
            col.uiStyles.attributes.alias === this.viewComponent.gridcolumn.toString()) {
            return true;
        } 
        return false;
    }
 }