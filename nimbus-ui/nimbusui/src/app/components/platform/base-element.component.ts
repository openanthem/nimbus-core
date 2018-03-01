import { ValidationConstraint } from './../../shared/validationconstraints.enum';
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
import { Param } from '../../shared/app-config.interface';
import { WebContentSvc } from '../../services/content-management.service';
import { LabelConfig } from './../../shared/app-config.interface';
import { ValidationUtils } from './validators/ValidationUtils';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-base-element',
    template:`
    `,
    providers: [
        WebContentSvc
    ]
})
/**
 * \@author Dinakar.Meda
 * \@whatItDoes A Base class for params. Example - Page, Tile, Section, Form, FormElements etc.
 * 
 * \@howToUse 
 * 
 * Every UI Component will extend this base class. This class provides the accessors for generic properties 
 * and behaviors for all params
 */
export class BaseElement {
    @Input() element: Param;
    public label: string;
    public helpText : string;
    protected _nestedParams: Param[];
    protected _imgSrc: string;
    protected _code: string;
    protected _visible: any;
    protected _enabled: any;
    protected _cssClass: string;
    protected _type: string;
    protected _elementStyle: string;
    protected requiredCss: boolean = false;
    constructor(private wcs: WebContentSvc) {
        
    }

    /**
     * Initialization activities this Param
     */
    ngOnInit() {
        this.loadLabelConfig(this.element);
        this.requiredCss = ValidationUtils.applyelementStyle(this.element);
    }

    /**
     * Traverses the provided param and stores the label config into this class' appropriate values.
     * @param param The param for which to load label content for.
     */
    protected loadLabelConfig(param: Param): void {
        let labelConfig: LabelConfig = this.wcs.findLabelContent(param);
        this.label = labelConfig.text;
        this.helpText = labelConfig.helpText;
    }

    protected loadLabelConfigByCode(code: string, labelConfigs: LabelConfig[]): void {
        let labelConfig: LabelConfig = this.wcs.findLabelContentFromConfig(code, labelConfigs);
        this.label = labelConfig.text;
        this.helpText = labelConfig.helpText;
    }

    /**
     * Nested Params if any within this Param
     */
    public get nestedParams(): Param[] {
        if (this.element.type && this.element.type.model) {
            return this.element.type.model.params;    
        }
        return undefined;
    }

    /**
     * Image source (if any) that goes with this Param
     */
    public get imgSrc(): string {
        return this.element.config.uiStyles.attributes.imgSrc;
    }
    
    /**
     * Unique code for this Param
     */
    public get code(): string {
        return this.element.config.code;
    }

    /**
     * Visibility of this Param. Visible by default
     */
    public get visible(): boolean {
        return this.element.visible;
    }

    /**
     * Enabled property of this Param. Enabled by default.
     */
    public get enabled(): boolean {
        return this.element.enabled;
    }

    /**
     * Custom css for this Param
     */
    public get cssClass(): string {
        return this.element.config.uiStyles.attributes.cssClass;
    }

    /**
     * Type definition for the Param
     */
    public get type(): string {
        return this.element.config.uiStyles.attributes.type;
    }

    /**
     * Check if control is required
     */
    public get elementStyle(): string {
        let style = '';
        if (this.element.config.validation) {
            this.element.config.validation.constraints.forEach(validator => {
                if (validator.name === ValidationConstraint._notNull.value) {
                    style = 'required';
                }
            });
        }
        return style;
    }
}

