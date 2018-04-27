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
import { Component, ViewChild, forwardRef, ChangeDetectorRef } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/param-state';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => TextArea),
  multi: true
};

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
  selector: 'nm-input-textarea',
  providers: [ CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc, ControlSubscribers ],
  template: `
    <!--<div class='textarea-holder' [hidden]="!element?.visible || !element?.visible" *ngIf="element.config?.uiStyles?.attributes?.hidden==false">-->
    <div class='textarea-holder' [hidden]="!element?.visible" *ngIf="element.config?.uiStyles?.attributes?.hidden==false">
        <div class="number" *ngIf="element.config?.uiStyles?.attributes?.controlId!=''">{{element.config?.uiStyles?.attributes?.controlId}}</div>
        <label [attr.for]="element.config?.code" [ngClass]="{'required': requiredCss, '': !requiredCss}">{{label}}
            <nm-tooltip *ngIf="helpText" [helpText]='helpText'></nm-tooltip>
        </label>
        <textarea [(ngModel)] = "value" 
        rows="element.config?.uiStyles?.attributes?.rows"  
            (focusout)="emitValueChangedEvent(this,value)"
            [disabled]="disabled"
            [id]="element.config?.code" class="form-control" *ngIf="element.config?.uiStyles?.attributes?.readOnly==false"></textarea>
        <p style="margin-bottom:0rem;" *ngIf="element.config?.uiStyles?.attributes?.readOnly==true">{{element.leafState}}</p>
    </div>
   `
})
export class TextArea extends BaseControl<String> {

    @ViewChild(NgModel) model: NgModel;

    constructor(wcs: WebContentSvc, controlService: ControlSubscribers,cd:ChangeDetectorRef) {
        super(controlService,wcs,cd);
    }
}
