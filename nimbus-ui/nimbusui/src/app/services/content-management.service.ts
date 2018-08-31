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

import { Injectable } from '@angular/core';
import { ServiceConstants } from './service.constants';
import { LabelConfig } from './../shared/param-config';
import { Param } from './../shared/param-state';
import { Converter } from './../shared/object.conversion';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Injectable()
export class WebContentSvc {

	constructor() {

	}

    /**
     * Retrieve the label config found for the provided param for the active browser Locale. If
     * no label config is found, the param config code will be used as the default label text.
     * This method is an facade for traversing the param for it's label config.
     * @param param the param to return label config for
     */
    findLabelContent(param: Param): LabelConfig {
        if (!param || !param.labels) {
            return undefined;
        }
        return this.findLabelContentFromConfig(param.labels, param.config.code);
    }

    /**
     * Retrieve the label config found the active browser Locale from the provided labelConfigs. If
     * no label config is found for the active browser Locale, the defaultLabel will be used as 
     * the default label text if isDefaultLabelsEnabled is true.
     * @param labelConfigs the label configs to search through
     * @param defaultLabel the default label to use if a label config is not found
     */
    findLabelContentFromConfig(labelConfigs : LabelConfig[], defaultLabel?: string): LabelConfig {
        let labelContent: LabelConfig = new LabelConfig();
        if (labelConfigs != null && labelConfigs.length > 0) {
            let labelConfig = labelConfigs.find(c => c.locale == ServiceConstants.LOCALE_LANGUAGE);
            labelContent = Converter.convert(labelConfig, labelContent);
        } else if(this.isDefaultLabelsEnabled) {
            labelContent.text = defaultLabel;
        } 
        return labelContent;
    }

    /**
     * Return whether or not default labels are to be used.
     */
    get isDefaultLabelsEnabled(): boolean {
        // TODO Is this scenario needed? Maybe for testing? If so, make this as a configurable property and
        // return the value based on that property.
        return false;
    }
    
}
