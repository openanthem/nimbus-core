
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
import { NgModel, NG_VALUE_ACCESSOR, FormGroup, ControlValueAccessor, Validators } from '@angular/forms';
import { Component, ViewChild, ChangeDetectorRef, forwardRef, Input } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { ControlSubscribers } from '../../../../services/control-subscribers.service';
import { InputMask } from 'primeng/primeng';


/**
 * \@author Vivek.Kamineni
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
	provide: NG_VALUE_ACCESSOR,
	useExisting: forwardRef(() => InputMaskComp),
	multi: true
};

@Component({
	selector: 'nm-mask',
	providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc, ControlSubscribers],
	template: `
	<div >
		<nm-input-label *ngIf="!isLabelEmpty && (hidden != true)"
		[element]="element" 
		[for]="element.config?.code" 
		[required]="requiredCss">
		</nm-input-label>
	</div>	

	<p-inputMask #inputMask 
		[disabled]="disabled" 
		[(ngModel)]="value" 
		(onBlur)="checkAndSetModelValue($event)"
		(onComplete)="emitValueChangedEvent(this,$event)"
		mask="{{element?.config?.uiStyles?.attributes?.mask}}"
		slotChar="{{element?.config?.uiStyles?.attributes?.slotChar}}" 
		placeholder="{{element?.config?.uiStyles?.attributes?.maskPlaceHolder}}"
		characterPattern="{{element?.config?.uiStyles?.attributes?.charRegex}}">
	</p-inputMask>
	
     `
})


export class InputMaskComp extends BaseControl<String>  {

	@ViewChild(NgModel) model: NgModel;
	@ViewChild('inputMask') inputMask: InputMask;

	constructor(wcs: WebContentSvc, controlService: ControlSubscribers, cd: ChangeDetectorRef) {
		super(controlService, wcs, cd);
	}

	ngOnInit() {
		 super.ngOnInit();
		 this.inputMask.updateModel = function(e){

			let updatedValue = this.unmask ? this.getUnmaskedValue() : e.target.value;
			/* Test to see if there is atleast one alphanumeric character in the i/p field */
			if(!((/[a-z]/i.test(updatedValue)) || (/[0-9]/).test(updatedValue))){
				this.value = updatedValue;
				this.onModelChange(null);
			}
			else if(updatedValue !== null || updatedValue !== undefined) {
				this.value = updatedValue;				 
				this.onModelChange(updatedValue);			    
			}
		 }

	}

	checkAndSetModelValue(e){
		if(!this.inputMask.isCompleted()){
			this.inputMask.onModelChange("");
		}		
	}
}