/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
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
