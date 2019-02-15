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
import { ChangeDetectorRef, Component, EventEmitter, Input, Output, ViewChild, forwardRef } from '@angular/core';
import { NG_VALUE_ACCESSOR, NgModel } from '@angular/forms';
import { WebContentSvc } from '../../../../services/content-management.service';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';
import { BaseControl } from './base-control.component';
 /**
 * \@author Rajesh Bandarupalli
 * \@whatItDoes 
 * 
 * This component can be used to capture user input as ON/OFF similar to checkbox.
 * 
 * \@howToUse 
 * 
 * This can be used inside the form/section as below example
 * 
 *      @InputSwitch
 *   	@Label("Hippa Verified?")
 *      private boolean hippaVerified;
 * 
 *      If no orientation is specified, it's considered as DEFAULT.
 *      On the need basis orientation can be supplied as the LEFT or RIGHT.
 * 
 *      orientation description:
 *          DEFAULT orientation places the component right next to the Label.
 *          LEFT orientation places the component left to the Label.
 *          RIGHT orientation places the component little away from the Label.
 *      
 */
export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InputSwitch),
    multi: true
};
 @Component({
    selector: 'nm-input-switch',
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc, ControlSubscribers],
    template: ` 
                <nm-input-label *ngIf="!isLabelEmpty && (hidden != true)"
                    [element]="element" 
                    [for]="element.config?.code" 
                    [required]="requiredCss">
                </nm-input-label>
                <p-inputSwitch 
                    [ngClass]="orientation" 
                    [(ngModel)]="value" 
                    [disabled]="disabled" 
                    [id]="element.config?.code" 
                    (onChange)="emitValueChangedEvent(this,$event)">
                </p-inputSwitch> 
             `
})
export class InputSwitch extends BaseControl<boolean> {

     @ViewChild(NgModel) model: NgModel;
     constructor(wcs: WebContentSvc, controlService: ControlSubscribers, cd: ChangeDetectorRef) {
         super(controlService, wcs, cd);
     }
     get orientation(): string {
         let style = this.element.config.uiStyles.attributes.orientation;

         if (style === 'LEFT') {
             return "left";
         }
         else if (style === 'RIGHT') {
             return "right";
         }
         else if (style === 'DOWN') {
            return "down";
        }
         else {
             return "";
         }
     }

}
