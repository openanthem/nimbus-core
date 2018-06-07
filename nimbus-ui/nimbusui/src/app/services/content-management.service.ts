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

import { Injectable, EventEmitter } from '@angular/core';
import { Http, Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/share';
import { ServiceConstants } from './service.constants';
import { LabelConfig } from './../shared/param-config';
import { Param } from './../shared/param-state';
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

	constructor(public http: Http) {

	}

    logError(err) {
		// console.error('Failure making server call : ' + JSON.stringify(err));
	}

    findLabelContent(param: Param): LabelConfig {
        return this.findLabelContentFromConfig(param.config.code, param.config.labelConfigs);
    }

    findLabelContentFromConfig(code : string, labelConfigs : LabelConfig[]): LabelConfig {
        let labelContent: LabelConfig = new LabelConfig();
        if(labelConfigs == null || (labelConfigs != null && labelConfigs.length < 1)) {
            labelContent.text = code;
        } else if (labelConfigs != null && labelConfigs.length > 0) {
            labelConfigs.forEach (labelConfig => {
                 if(labelConfig.locale == ServiceConstants.LOCALE_LANGUAGE) {
                    labelContent.text = labelConfig.text;
                    labelContent.helpText = labelConfig.helpText;
                    labelContent.locale = ServiceConstants.LOCALE_LANGUAGE;
                 }
            });
        }
        return labelContent;
    }
    
}
