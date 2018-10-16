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
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Component, ViewChild, forwardRef, Input, ChangeDetectorRef } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => InputText),
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
  selector: 'nm-input',
  providers: [ CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc, ControlSubscribers ],
  template: `
    <nm-input-label *ngIf="!isLabelEmpty && (hidden != true)"
        [element]="element" 
        [for]="element.config?.code" 
        [required]="requiredCss">

    </nm-input-label>

    <input *ngIf="hidden!=true && readOnly==false"
        [(ngModel)] = "value"
        [id]="element.config?.code" 
        (focusout)="emitValueChangedEvent(this,value)"
        [value]="type"
        [disabled]="disabled"
        class="form-control text-input"/>

    <input *ngIf="hidden==true"
        [id]="element.config?.code" 
        type="hidden" 
        [value]="element.leafState" />

    <pre class="print-only" *ngIf="hidden !=true && readOnly == false">{{this.value}}</pre>

    <p style="margin-bottom:0rem;" *ngIf="readOnly==true">{{element.leafState}}</p>
   `
})
export class InputText extends BaseControl<String> {

    @ViewChild(NgModel) model: NgModel;

    constructor(wcs: WebContentSvc, controlService: ControlSubscribers, cd:ChangeDetectorRef) {
        super(controlService,wcs,cd);
    }
}
