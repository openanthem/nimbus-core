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
import { FormGroup } from '@angular/forms';
import { LabelConfig } from '../../../shared/param-config';
import { Param } from '../../../shared/param-state';
import { WebContentSvc } from '../../../services/content-management.service';
import { BaseElement } from '../base-element.component';
import { PageService } from '../../../services/page.service';
import { ViewComponent } from '../../../shared/param-annotations.enum';

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
                <p-accordionTab  [selected]="tab?.config?.uiStyles?.attributes?.selected" *ngIf="tab?.visible == true">
                    <p-header>
                        <h2>{{getTabHeader(tab)}}</h2>
                        <span [ngClass]="getTabInfoClass(tab)" *ngIf="getInfoText(tab)">
                            {{getInfoText(tab)}}
                        </span>
                        <nm-image class='nm-accordion-headerimage' *ngIf="getImageSrc(tab)" [name]="getImageSrc(tab)" [type]="getImageType(tab)" [title]="getTitle(tab)" [cssClass]="getcssClass(tab)"></nm-image>
                        <div style='clear: both'></div>
                    </p-header>
                    <div class="accordionBtn" *ngIf="tab?.config?.uiStyles?.attributes?.editable">
                        <button  (click)="processOnClick(tab)" type="button" class="btn btn-plain">
                            <i class="fa fa-fw fa-pencil" aria-hidden="true"></i>Edit
                        </button>
                    </div>
                    <!-- Form Elements -->
                    <ng-template [ngIf]="form !== undefined">
                        <nm-frm-grp [elements]="tab?.type?.model?.params" [form]="form.controls[tab.config?.code]" [elementCss]="elementCss"> </nm-frm-grp>
                    </ng-template>
                    <ng-template [ngIf]="form === undefined">
                        <ng-template ngFor let-tabElement [ngForOf]="tab?.type?.model?.params">
                            <!-- ButtonGroup -->
                            <ng-template [ngIf]="tabElement.alias == 'ButtonGroup'">
                                <div class="">
                                    <nm-button-group [buttonList]="tabElement.type?.model?.params" [cssClass]="tabElement.config?.uiStyles?.attributes?.cssClass">
                                    </nm-button-group>
                                </div>
                            </ng-template>
                            <!-- Grid Param -->
                            <ng-template [ngIf]="tabElement.alias == 'Grid'">
                                <nm-table
                                    [element]="tabElement" 
                                    [params]="tabElement?.config?.type?.elementConfig?.type?.model?.paramConfigs"
                                    (onScrollEvent)="onScrollEvent()">
                                </nm-table>
                            </ng-template>
                            <!-- Card Content -->
                            <ng-template [ngIf]="tabElement.alias == 'CardDetail'">
                                <nm-card-details [element]="tabElement"></nm-card-details>
                            </ng-template>
                        </ng-template>
                    </ng-template>
                </p-accordionTab>
            </ng-template>
        </p-accordion>
    `
})

export class AccordionMain extends BaseElement {

    @Input() form: FormGroup;
    @Input() elementCss: string;

    protected _multiple: boolean;
    index: number[]; 
    @ViewChild('accordion') accordion: AccordionMain;

    constructor(private wcsvc: WebContentSvc, private pageSvc: PageService) {
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
    protected getTabHeader(param: Param): string {
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

    /**
     * Process configs on the click event
     */
    processOnClick(param: Param) {
        this.pageSvc.processEvent(param.path, '$execute', null, 'POST');
    }

    getInfoText(tab) {
        for (let i = 0; i < tab.type.model.params.length; i++) {
            if (tab.type.model.params[i].alias === ViewComponent.tabInfo.toString()) {
                if (!tab.type.model.params[i].visible) {
                    return;
                }
                else if(tab.type.model.params[i].leafState){
                    return tab.type.model.params[i].leafState;
                }
                
                return tab.type.model.params[i].config.uiStyles.attributes.info ;
            }
        }
    }

    getTabInfoClass(tab) {
        for (let i = 0; i < tab.type.model.params.length; i++) {
            let tabParam: Param = tab.type.model.params[i];
            if (tabParam.alias === ViewComponent.tabInfo.toString()) {
                if(tabParam.config && tabParam.config.uiStyles.attributes.cssClass){
                    return tabParam.config.uiStyles.attributes.cssClass;
                } else {
                    return 'nm-accordion-headertext' ;
                }
            }
        }
    }

    getImageSrc(tab) {
        for (let i = 0; i < tab.type.model.params.length; i++) {
            if (tab.type.model.params[i].alias === ViewComponent.image.toString()) {
                if (!tab.type.model.params[i].visible) {
                    return;
                }
                else if(tab.type.model.params[i].leafState){
                    return tab.type.model.params[i].leafState;
                }
                
                return tab.type.model.params[i].config.uiStyles.attributes.imgSrc;
            }
        }
    }

    getImageType(tab) {
        for (let i = 0; i < tab.type.model.params.length; i++) {
            if (tab.type.model.params[i].alias === ViewComponent.image.toString()) {
                return tab.type.model.params[i].config.uiStyles.attributes.type;
            }
        }
    }

    getTitle(tab) {
        for (let i = 0; i < tab.type.model.params.length; i++) {
            if (tab.type.model.params[i].alias === ViewComponent.image.toString()) {
                return tab.type.model.params[i].config.uiStyles.attributes.title;
            }
        }
    }

    getcssClass(tab) {
        for (let i = 0; i < tab.type.model.params.length; i++) {
            if (tab.type.model.params[i].alias === ViewComponent.image.toString()) {
                return tab.type.model.params[i].config.uiStyles.attributes.cssClass;
            }
        }
    }

}
