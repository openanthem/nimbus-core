
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

/**
 * \@author Vivek.Kamineni
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
	provide: NG_VALUE_ACCESSOR,
	useExisting: forwardRef(() => InputMask),
	multi: true
};

@Component({
	selector: 'nm-mask',
	providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc, ControlSubscribers],
	template: `
	<div>
		<nm-input-label *ngIf="!isLabelEmpty && (hidden != true)"
		[element]="element" 
		[for]="element.config?.code" 
		[required]="requiredCss">
		</nm-input-label>
	</div>	

	<p-inputMask #inputMask 
		[disabled]="disabled" 
		[(ngModel)]="value" 
		mask="{{element?.config?.uiStyles?.attributes?.mask}}"
		slotChar="{{element?.config?.uiStyles?.attributes?.slotChar}}" 
		placeholder="{{element?.config?.uiStyles?.attributes?.maskPlaceHolder}}"
		characterPattern="{{element?.config?.uiStyles?.attributes?.charRegex}}">
	</p-inputMask>
	
     `
})


export class InputMask extends BaseControl<String> implements ControlValueAccessor {

	@ViewChild(NgModel) model: NgModel;
	@ViewChild('inputMask') inputMask: any;

	@Input() form: FormGroup;
	@Input('value') _value;

	constructor(wcs: WebContentSvc, controlService: ControlSubscribers, cd: ChangeDetectorRef) {
		super(controlService, wcs, cd);
	}

	ngOnInit() {
		super.ngOnInit();
		this.form.get(this.element.config.code).valueChanges.subscribe(
			(data) => {

				if (this.form.get(this.element.config.code).validator) {
					if (this.inputMask.isCompleted()) {
						this.form.get(this.element.config.code).setErrors(null);

					}
					else {
						this.form.get(this.element.config.code).setErrors({ 'required': true });
					}
				}
			});
	}

	public onChange: any = (_) => { /*Empty*/ }
	public onTouched: any = () => { /*Empty*/ }

	get value() {
		return this._value;
	}

	set value(val) {
		this._value = val;
		this.onChange(val);
		this.onTouched();
	}

	writeValue(value) {
		if (value) {
		}
	}

	registerOnChange(fn) {
		this.onChange = fn;
	}

	registerOnTouched(fn) {
		this.onTouched = fn;
	}

}