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
import { Param } from '../../shared/param-state';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { WebContentSvc } from '../../services/content-management.service';
import { ViewComponent } from '../../shared/param-annotations.enum';
import { BaseLabel } from './base-label.component';

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
    template:`
        <div class="form-holder clearfix colorBox" [hidden]="!hasParams()">
            <ng-template ngFor let-element let-isFirst="first" [ngForOf]="elements">
                <ng-template [ngIf]="isFirst">
                    <legend *ngIf="label && element.visible">
                        {{label}}
                    </legend>
                </ng-template>
                <ng-template [ngIf]="!element.type?.model?.params?.length || element.config?.type?.collection">
                    <nm-element [position]="position+1" id="{{id}}" [element]="element" [elementCss]="elementCss" [form]="form"></nm-element>
                </ng-template>
                <ng-template [ngIf]="element.type?.model?.params?.length && element.config?.uiStyles?.attributes?.alias!=viewComponent.buttongroup.toString() && !element?.config?.type?.collection">
                    <fieldset class="subQuestion" [hidden]="!element?.visible">
                        <nm-frm-grp [elements]="element.type?.model?.params" [form]="form.controls[element.config?.code]" [elementCss]="elementCss" [parentElement]="element"></nm-frm-grp>
                    </fieldset>
                </ng-template>
                 <ng-template [ngIf]="element.config?.uiStyles?.attributes?.alias==viewComponent.button.toString()">
                    <nm-button [form]="form" [element]="element"> </nm-button>
                </ng-template>
                <ng-template [ngIf]="element.config?.uiStyles?.attributes?.alias==viewComponent.buttongroup.toString()">
                    <nm-button-group [form]="form" [buttonList]="element.type?.model?.params" [cssClass]="element.config?.uiStyles?.attributes?.cssClass"> </nm-button-group>
                </ng-template>
            </ng-template>
        </div>
    `
})
export class FrmGroupCmp extends BaseLabel {
    
       @Input() elements: Param[] = [];
       @Input() form: FormGroup;
       @Input() elementCss : String;
       @Input() parentElement: Param

       viewComponent = ViewComponent;

       constructor(private wcsv: WebContentSvc) {
           super(wcsv);
       }

       ngOnInit() {
            super.ngOnInit();
       }

       hasParams() {
           for (let p in this.elements) {
               if (this.elements[p].visible) {
                   return true;
               }
           }
           return false;
       }
   }
