'use strict';

import { Injectable, EventEmitter } from '@angular/core';
import { Http, Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/share';
import { ServiceConstants } from './service.constants';
import { Param, LabelConfig, ElementModelParam } from './../shared/app-config.interface';

// Web Content Service
@Injectable()
export class WebContentSvc {

	constructor(public http: Http) {

	}

    logError(err) {
		console.error('Failure making server call : ' + JSON.stringify(err));
	}

    findLabelContent(param: Param) {
        return this.findLabelContentFromConfig(param.config.code, param.config.labelConfigs);
    }

    findLabelContentFromConfig(code : string, labelConfigs : LabelConfig[]) {
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
