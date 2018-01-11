/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import { Param } from '../../../../shared/app-config.interface';
/**
 * \@author Sandeep Mantha
 * \@whatItDoes A control to be used when the user input is in the form of date or date with time or just the time.
 */

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => Calendar),
  multi: true
};

@Component({
  selector: 'nm-input-calendar',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR,WebContentSvc],
  template: `
        <label [attr.for]="element.config?.code" class="" *ngIf="this.showLabel">{{label}}
            <nm-tooltip *ngIf="element.config?.uiStyles?.attributes?.help!=''" [helpText]='element.config?.uiStyles?.attributes?.help'></nm-tooltip>
        </label>
        <p-calendar [(ngModel)]="value"  
            (focusout)="emitValueChangedEvent(this,$event)" 
            [minDate]="min"
            [maxDate]="max"
            [disabled]="!element?.enabled?.currState"
            [timeOnly]="element.config?.uiStyles?.attributes?.timeOnly"
            [showTime]="element.config?.uiStyles?.attributes?.showTime" 
            [hourFormat]="element.config?.uiStyles?.attributes?.hourFormat" >
        </p-calendar>
   `
})
export class Calendar extends BaseControl<String> {

    @ViewChild(NgModel) model: NgModel;

    element: Param;

    constructor(wcs: WebContentSvc, pageService: PageService, cd:ChangeDetectorRef) {
        super(pageService,wcs,cd);
    }

    // ngOnInit(){
 
    // }

}
