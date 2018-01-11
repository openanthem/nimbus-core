/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    protected label: string;
    protected _nestedParams: Param[];
    protected _imgSrc: string;
    protected _code: string;
    protected _visible: any;
    protected _enabled: any;
    protected _cssClass: string;
    protected _type: string;
    
    constructor(private wcs: WebContentSvc) {
        wcs.content$.subscribe(result => {
            if (this.element && result.id === this.element.config.code) {
                this.label = result.label;
            }
        });
    }

    /**
     * Initialization activities this Param
     */
    ngOnInit() {
        if (this.element.config && this.element.config.code) {
            this.wcs.getContent(this.element.config.code);
        }
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
}

