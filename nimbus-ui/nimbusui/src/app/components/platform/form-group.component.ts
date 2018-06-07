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
import { LabelConfig } from './../../shared/param-config';
import { Component, Input, forwardRef } from '@angular/core';
import { Param } from '../../shared/param-state';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { WebContentSvc } from '../../services/content-management.service';
import { BaseControl } from './form/elements/base-control.component';
import { PageService } from '../../services/page.service';

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
        <div class="col-lg-12 clearfix colorBox" [hidden]="!hasParams()">
            <ng-template ngFor let-element let-isFirst="first" [ngForOf]="elements">
                <ng-template [ngIf]="isFirst">
                    <legend *ngIf="label && element.visible">
                        {{label}}
                    </legend>
                </ng-template>
                <ng-template [ngIf]="!element.type?.model?.params?.length || element.config?.type?.collection">
                    <nm-element id="{{id}}" [element]="element" [elementCss]="elementCss" [form]="form"></nm-element>
                </ng-template>
                <ng-template [ngIf]="element.type?.model?.params?.length && element.config?.uiStyles?.attributes?.alias!='ButtonGroup' && !element?.config?.type?.collection">
                    <fieldset class="subQuestion" [hidden]="!element?.visible">
                        <nm-frm-grp [elements]="element.type?.model?.params" [form]="form.controls[element.config?.code]" [elementCss]="elementCss" [parentElement]="element"></nm-frm-grp>
                    </fieldset>
                </ng-template>
                 <ng-template [ngIf]="element.config?.uiStyles?.attributes?.alias=='Button'">
                    <nm-button [form]="form" [element]="element"> </nm-button>
                </ng-template>
                <ng-template [ngIf]="element.config?.uiStyles?.attributes?.alias=='ButtonGroup'">
                    <nm-button-group [form]="form" [buttonList]="element.type?.model?.params" [cssClass]="element.config?.uiStyles?.attributes?.cssClass"> </nm-button-group>
                </ng-template>
            </ng-template>
        </div>
    `
})
export class FrmGroupCmp {
    
       @Input() elements: Param[] = [];
       @Input() form: FormGroup;
       @Input() elementCss : String;
       @Input() parentElement: Param
       private label: string;
       private helpText : string;

       constructor(private wcs: WebContentSvc) {
           
       }

       ngOnInit() {
            if (this.hasParagraph(this.parentElement)) {
                let labelConfig: LabelConfig = this.wcs.findLabelContent(this.parentElement);
                this.label = labelConfig.text;
                this.helpText = labelConfig.helpText;
            }
       }

       hasParagraph(element: Param): boolean {
           return element && element.config && element.config.uiStyles && element.config.uiStyles.attributes && element.config.uiStyles.attributes.alias=='Paragraph';
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
