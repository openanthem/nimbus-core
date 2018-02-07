import { LabelConfig } from './../../shared/app-config.interface';
/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { Component, Input } from '@angular/core';
import { Param } from '../../shared/app-config.interface';
import { WebContentSvc } from '../../services/content-management.service';

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
    
    constructor(private wcs: WebContentSvc) {
        
    }

    /**
     * Initialization activities this Param
     */
    ngOnInit() {
        this.loadLabelConfig(this.element);
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
     * Visibility of this Param
     */
    public get visible(): boolean {
        return this.element.visible.currState;
    }

    /**
     * Enabled property of this Param
     */
    public get enabled(): boolean {
        return this.element.enabled.currState;
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
                if (validator.name === 'NotNull') {
                    style = 'required';
                }
            });
        }
        return style;
    }
}

