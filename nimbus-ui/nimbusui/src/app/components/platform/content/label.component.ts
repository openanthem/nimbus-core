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
import { LabelConfig } from './../../../shared/param-config';

/**
 * \@author Purnachander.Mashetty
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-label',
    template: `
        <H1 *ngIf="size=='H1'" [className]="cssClass">{{label}}</H1>
        <H2 *ngIf="size=='H2'" [className]="cssClass">{{label}}</H2>
        <H3 *ngIf="size=='H3'" [className]="cssClass">{{label}}</H3>
        <H4 *ngIf="size=='H4'" [className]="cssClass">{{label}}</H4>
        <H5 *ngIf="size=='H5'" [className]="cssClass">{{label}}</H5>
        <H6 *ngIf="size=='H6'" [className]="cssClass">{{label}}</H6>
        <nm-tooltip *ngIf="helpText" [helpText]='helpText'></nm-tooltip>
    `
})

export class Label {

    @Input() labelConfig: LabelConfig;
    @Input() size: String;
    @Input() labelClass: String;

    constructor() {
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
    public get cssClass(): string {
        let cssClass = '';
        if (this.labelClass) {
            cssClass += this.labelClass;
        }
        if (this.labelConfig && this.labelConfig.cssClass) {
            cssClass += ' ' + this.labelConfig.cssClass;
        }
        return cssClass;
    }
}

