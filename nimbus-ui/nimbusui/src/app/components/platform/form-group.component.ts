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
import { Component, Input, forwardRef } from '@angular/core';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { WebContentSvc } from '../../services/content-management.service';
import { ViewComponent } from '../../shared/param-annotations.enum';
import { BaseElement } from './base-element.component';
import { Param } from './../../shared/param-state';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => FrmGroupCmp),
    multi: true
};

/**
* \@author Dinakar.Meda
* \@author Sandeep.Mantha
* \@whatItDoes 
* 
* \@howToUse 
* 
*/
@Component({
    selector: 'nm-frm-grp',
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc],
    template: `
        <span [hidden]="!this.element.visible" [ngClass]="getCssClass()">
            <ng-template [ngIf]="element?.config?.uiStyles?.attributes?.alias == viewComponent.formElementGroup.toString()">
                <fieldset>
                    <legend *ngIf="labelConfig?.text">{{labelConfig?.text}}</legend>
                    <ng-template ngFor let-frmElem [ngForOf]="element.type.model.params">
                        <nm-frm-grp [element]="frmElem" [form]="form" [elementCss]="elementCss" [position]="position"> 
                        </nm-frm-grp>
                    </ng-template>
                </fieldset>
            </ng-template>

            <ng-template [ngIf]="!element.type?.model?.params?.length || element.config?.type?.collection">
                <nm-element [position]="position+1" id="{{id}}" [element]="element" [elementCss]="elementCss" [form]="form"></nm-element>
            </ng-template>
            
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.alias == viewComponent.button.toString()">
                <nm-button [form]="form" [element]="element"> </nm-button>
            </ng-template>

            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.alias == viewComponent.buttongroup.toString()">
                <nm-button-group [form]="form" [buttonList]="element.type?.model?.params" [cssClass]="element.config?.uiStyles?.attributes?.cssClass"> 
                </nm-button-group>
            </ng-template>
        </span>
    `
})
export class FrmGroupCmp extends BaseElement {
    
    @Input() elements: Param[] = [];
    @Input() form: FormGroup;
    @Input() elementCss : String;
    @Input() parentElement: Param;

    viewComponent = ViewComponent;

    constructor(private wcsv: WebContentSvc) {
        super(wcsv);
    }

    ngOnInit() {
        super.ngOnInit();
        this.updatePosition();
    }

    getCssClass() {
        if (this.element.config.uiStyles.attributes.alias == ViewComponent.formElementGroup.toString()) {
            if (this.element.config.uiStyles.attributes.cssClass) {
                return this.element.config.uiStyles.attributes.cssClass
            } else {
                return this.elementCss;
            }
        }
        return '';
    }

}
