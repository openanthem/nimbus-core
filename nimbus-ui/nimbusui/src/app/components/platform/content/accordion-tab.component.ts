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
import { FormGroup } from '@angular/forms';
import { Param } from '../../../shared/param-state';
import { WebContentSvc } from '../../../services/content-management.service';
import { BaseElement } from '../base-element.component';
import { PageService } from '../../../services/page.service';
import { ViewComponent, ComponentTypes } from '../../../shared/param-annotations.enum';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component( {
    selector: 'nm-accordion-tab',
    providers: [WebContentSvc],
    template: `
        <p-accordionTab [selected]="element?.config?.uiStyles?.attributes?.selected" *ngIf="element?.visible">
            <p-header>
                <nm-label *ngIf="element" [element]="element" [size]="labelSize"></nm-label>
                <span [ngClass]="getTabInfoClass()" *ngIf="getInfoText()">
                    {{getInfoText()}}
                </span>
                <nm-image class='nm-accordion-headerimage' *ngIf="getImageSrc()" [name]="getImageSrc()" [type]="getImageType()" [title]="getTitle()" [cssClass]="getcssClass()"></nm-image>
                <nm-counter-message *ngIf="showMessages" [element]="element" [form]="form"></nm-counter-message>
                <div style='clear: both'></div>
            </p-header>
            <div class="accordionBtn" *ngIf="element?.config?.uiStyles?.attributes?.editable">
                <button  (click)="processOnClick()" type="button" class="btn btn-plain">
                    <i class="fa fa-fw fa-pencil" aria-hidden="true"></i>Edit
                </button>
            </div>
            <!-- Form Elements -->
            <ng-template [ngIf]="form !== undefined">
                <ng-template ngFor let-frmElem [ngForOf]="element.type?.model?.params">
                    <nm-frm-grp [element]="frmElem" [form]="form" class="{{elementCss}}" [position]="position + 1"> 
                    </nm-frm-grp>
                </ng-template>
            </ng-template>
            <ng-template [ngIf]="form === undefined">
                <ng-template ngFor let-tabElement [ngForOf]="element?.type?.model?.params">
                    <!-- ButtonGroup -->
                    <ng-template [ngIf]="tabElement.alias == componentTypes.buttonGroup.toString()">
                        <div class="">
                            <nm-button-group [buttonList]="tabElement.type?.model?.params" [cssClass]="tabElement.config?.uiStyles?.attributes?.cssClass">
                            </nm-button-group>
                        </div>
                    </ng-template>
                    <!-- Link -->
                    <ng-template [ngIf]="tabElement.alias == componentTypes.link.toString()">
                        <nm-link [element] = "tabElement"> </nm-link>                                                                  
                    </ng-template>
                    <!-- Grid Param -->
                    <ng-template [ngIf]="tabElement.alias == componentTypes.grid.toString()">
                        <nm-table
                            [element]="tabElement" 
                            [params]="tabElement?.config?.type?.elementConfig?.type?.model?.paramConfigs"
                            (onScrollEvent)="onScrollEvent()"
                            [position]="position+1"
                            [nmPrint]="tabElement">
                        </nm-table>
                    </ng-template>
                    <!-- Card Content -->
                    <ng-template [ngIf]="tabElement.alias == componentTypes.cardDetail.toString()">
                        <nm-card-details [element]="tabElement" [position]="position+1" [nmPrint]="tabElement"></nm-card-details>
                    </ng-template>
                    <!-- Card Detaisl Grid -->
                    <ng-template [ngIf]="tabElement.alias == componentTypes.cardDetailsGrid.toString()">
                        <nm-card-details-grid [position]="position+1" [element]="tabElement" [nmPrint]="tabElement"></nm-card-details-grid>
                    </ng-template>
                    <!-- Form Param -->
                    <ng-template [ngIf]="tabElement.alias == viewComponent.form.toString()">
                        <nm-form [position]="position+1" [element]="tabElement" [model]="tabElement.type?.model" [nmPrint]="tabElement"></nm-form>
                    </ng-template>
                </ng-template>
            </ng-template>
        </p-accordionTab>
    `
})

export class AccordionTab  extends BaseElement {

    @Input() form: FormGroup;
    @Input() elementCss: string;
    @Input() showMessages: boolean;
    componentTypes = ComponentTypes;
    viewComponent = ViewComponent;

    constructor(private wcsvc: WebContentSvc, private pageSvc: PageService) {
        super(wcsvc);
    }

    ngOnInit() {
        super.ngOnInit();
    }

    getInfoText() {
        for (let i = 0; i < this.element.type.model.params.length; i++) {
            if (this.element.type.model.params[i].alias === ViewComponent.tabInfo.toString()) {
                if (!this.element.type.model.params[i].visible) {
                    return;
                }
                else if(this.element.type.model.params[i].leafState){
                    return this.element.type.model.params[i].leafState;
                }
                
                return this.element.type.model.params[i].config.uiStyles.attributes.info ;
            }
        }
    }

    getTabInfoClass() {
        for (let i = 0; i < this.element.type.model.params.length; i++) {
            let tabParam: Param = this.element.type.model.params[i];
            if (tabParam.alias === ViewComponent.tabInfo.toString()) {
                if(tabParam.config && tabParam.config.uiStyles.attributes.cssClass){
                    return tabParam.config.uiStyles.attributes.cssClass;
                } else {
                    return 'nm-accordion-headertext' ;
                }
            }
        }
    }

    getImageSrc() {
        for (let i = 0; i < this.element.type.model.params.length; i++) {
            if (this.element.type.model.params[i].alias === ViewComponent.image.toString()) {
                if (!this.element.type.model.params[i].visible) {
                    return;
                }
                else if(this.element.type.model.params[i].leafState){
                    return this.element.type.model.params[i].leafState;
                }
                
                return this.element.type.model.params[i].config.uiStyles.attributes.imgSrc;
            }
        }
    }

    getImageType() {
        for (let i = 0; i < this.element.type.model.params.length; i++) {
            if (this.element.type.model.params[i].alias === ViewComponent.image.toString()) {
                return this.element.type.model.params[i].config.uiStyles.attributes.type;
            }
        }
    }

    getTitle() {
        for (let i = 0; i < this.element.type.model.params.length; i++) {
            if (this.element.type.model.params[i].alias === ViewComponent.image.toString()) {
                return this.element.type.model.params[i].config.uiStyles.attributes.title;
            }
        }
    }

    getcssClass() {
        for (let i = 0; i < this.element.type.model.params.length; i++) {
            if (this.element.type.model.params[i].alias === ViewComponent.image.toString()) {
                return this.element.type.model.params[i].config.uiStyles.attributes.cssClass;
            }
        }
    }

    /**
     * Process configs on the click event
     */
    processOnClick(param: Param) {
        this.pageSvc.processEvent(param.path, '$execute', null, 'POST');
    }
}
