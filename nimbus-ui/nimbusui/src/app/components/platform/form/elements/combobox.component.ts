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
import { Component, ViewChild, forwardRef, ChangeDetectorRef, Input } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => ComboBox),
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
  selector: 'nm-comboBox',
  providers: [ CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc, ControlSubscribers ],
  template: `
    <nm-input-label *ngIf="!isLabelEmpty && this.showLabel"
        [element]="element" 
        [for]="element.config?.code" 
        [required]="requiredCss">

    </nm-input-label>
    <p-dropdown 
        [options]="element.values | selectItemPipe" 
        [(ngModel)] = "value"
        [disabled]="disabled"
        (onChange)="emitValueChangedEvent(this,$event)"
        class="form-control" 
        [autoWidth]="autoWidth"
        [placeholder]="placeholder">
    </p-dropdown>
   `
})

export class ComboBox extends BaseControl<String> {

    @ViewChild(NgModel) model: NgModel;
    @Input() autoWidth: boolean = false;
    @Input() placeholder: string = 'Please Select...';

    constructor(wcs: WebContentSvc, controlService: ControlSubscribers, cd:ChangeDetectorRef) {
        super(controlService,wcs,cd);
    }

}
