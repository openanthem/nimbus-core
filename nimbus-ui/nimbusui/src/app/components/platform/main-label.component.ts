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
import { WebContentSvc } from '../../services/content-management.service';
import { Param } from './../../shared/param-state';
import { LabelConfig } from './../../shared/param-config';
import { PageService } from './../../services/page.service';

/**
 * \@author Tony Lopez
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-main-label',
    providers: [ PageService ],
    template:`
    `
})

export class MainLabel {

    @Input() element: Param;
    protected labelConfig: LabelConfig;

    constructor(private _wcs: WebContentSvc, private _pageService: PageService) {

    }

    ngOnInit() {
        this.labelConfig = this._wcs.findLabelContent(this.element);

        // Update the labels when an update for the param comes back from the server.
        this._pageService.eventUpdate$.subscribe(event => {
            if(event.path == this.element.path) {
               if (event.labels && event.labels.length !== 0) {
                   this.labelConfig = this._wcs.findLabelContent(event);
               }
            }
        });
    }

    /**
     * Get the tooltip help text for this element.
     */
    public get helpText(): string {
        if (!this.labelConfig) {
            return undefined;
        }
        return this.labelConfig.helpText;
    }

    /**
     * Get the label text for this element.
     */
    public get label(): string {
        if (!this.labelConfig) {
            return undefined;
        }
        return this.labelConfig.text;
    }

    /**
     * Get the css classes to apply for this element.
     */
    public getCssClass(): string {
        let cssClass = '';
        if (this.labelConfig && this.labelConfig.cssClass) {
            cssClass = this.labelConfig.cssClass;
        }
        return cssClass;
    }
}

