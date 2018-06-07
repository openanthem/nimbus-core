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
import { NG_VALUE_ACCESSOR, NgModel } from '@angular/forms';
import { Component, forwardRef, ViewChild, ChangeDetectorRef } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/param-state';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';

/**
 * \@author Sandeep Mantha
 * \@whatItDoes A control to be used when the user input is in the form of date or date with time or just the time.
 */

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => Calendar),
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
  selector: 'nm-input-calendar',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR,WebContentSvc, ControlSubscribers],
  template: `
        <label [attr.for]="element.config?.code"  [ngClass]="{'required': requiredCss, '': !requiredCss}" *ngIf="this.showLabel">{{label}}
            <nm-tooltip *ngIf="helpText" [helpText]='helpText'></nm-tooltip>
        </label>
        <p-calendar [(ngModel)]="value"  
            (focusout)="emitValueChangedEvent(this,$event)" 
            [showIcon]="true"
            [timeOnly]="element.config?.uiStyles?.attributes?.timeOnly"
            [showTime]="element.config?.uiStyles?.attributes?.showTime" 
            [hourFormat]="element.config?.uiStyles?.attributes?.hourFormat" 
            [monthNavigator]="element.config?.uiStyles?.attributes?.monthNavigator"
            [yearNavigator]="element.config?.uiStyles?.attributes?.yearNavigator"
            [readonlyInput]="element.config?.uiStyles?.attributes?.readonlyInput"
            [yearRange]="element.config?.uiStyles?.attributes?.yearRange"
            [disabled]="disabled">
        </p-calendar>
   `
})
export class Calendar extends BaseControl<Date> {

    @ViewChild(NgModel) model: NgModel;

    constructor(wcs: WebContentSvc, controlService: ControlSubscribers, cd:ChangeDetectorRef) {
        super(controlService,wcs,cd);
    }

    // ngOnInit(){
 
    // }

}
