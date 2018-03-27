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
import { Component, Input, ViewChild } from '@angular/core';
import { Param, LabelConfig } from '../../../shared/app-config.interface';
import { WebContentSvc } from '../../../services/content-management.service';
import { BaseElement } from '../base-element.component';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component( {
    selector: 'nm-accordion',
    providers: [WebContentSvc],
    template: `
        <div class="text-sm-right" *ngIf="element.config?.uiStyles?.attributes?.showExpandAll">
            <button type="button" class="btn btn-expand" (click)="openAll()">Expand All</button>
            <span class="btn-pipe">|</span>
            <button type="button" class="btn btn-expand" (click)="closeAll()">Collapse All</button>
        </div>

        <p-accordion #accordion [multiple]="multiple" [activeIndex]="index">
            <ng-template ngFor let-tab [ngForOf]="nestedParams">
                <nm-accordion-tab [element]="tab"></nm-accordion-tab>
            </ng-template>
        </p-accordion>
    `
})

export class AccordionMain extends BaseElement {

    protected _multiple: boolean;
    index: number[]; 
    @ViewChild('accordion') accordion: AccordionMain;

    constructor(private wcsvc: WebContentSvc) {
        super(wcsvc);
    }

    ngOnInit() {
        super.ngOnInit();
    }

    /**
     * Expand Multiple Tabs?
     */
    public get multiple(): boolean {
        return this.element.config.uiStyles.attributes.multiple;
    }

    /**
     * Get Tab label
     */
    protected getTabLabel(param: Param): string {
        let labelConfig: LabelConfig = this.wcsvc.findLabelContent(param);
        return labelConfig.text;
    }

    /**
     * Close All Tabs
     */
    public closeAll() {
        if(this.accordion['tabs']){
            this.index = [];
            this.index.push(-1);
        }
    }

    /**
     * Open All Tabs
     */
    public openAll() {
        if(this.accordion['tabs']){
            this.index = [];
            for (let t =0; t<this.accordion['tabs'].length; t++){
                this.index.push(t);
            }
        }
    }
}
