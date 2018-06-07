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
import { ViewChild, Component, forwardRef, ChangeDetectorRef } from '@angular/core';
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { BaseControl } from './base-control.component';
import { Param } from '../../../../shared/param-state';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
  selector: 'nm-single-checkbox',
  template: ` <label class="custom-control custom-check">
                 <input type="checkbox" [disabled]="disabled" class="custom-control-input" (change)="emitValueChangedEvent(this,$event)" [(ngModel)] = "value" [attr.checked]="value">
                 <span class="custom-control-indicator"></span>
                 <span class="custom-control-description"  [ngClass]="{'required': requiredCss, '': !requiredCss}">{{label}}
                      <nm-tooltip *ngIf="helpText" [helpText]='helpText'></nm-tooltip>
                 </span>
             </label>
            `,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CheckBox),
      multi: true
    }, WebContentSvc, ControlSubscribers
  ]
})
export class CheckBox extends BaseControl<boolean> {

    @ViewChild(NgModel) model: NgModel;

    constructor(wcs: WebContentSvc, controlService: ControlSubscribers, cd:ChangeDetectorRef) {
      super(controlService,wcs,cd);
    }

}
