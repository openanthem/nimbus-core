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

import {
	Component, Input, ViewChild, ViewEncapsulation,
	ElementRef, ViewChildren, QueryList, forwardRef, ChangeDetectorRef
} from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { FormGroup, FormControl, FormArray, FormBuilder, Validators } from '@angular/forms';
import { FormArrayName } from '@angular/forms/src/directives/reactive_directives/form_group_name';
import { Param } from '../../../shared/param-state';
import { FileService } from './../../../services/file.service';
import { BaseElement } from '../base-element.component';
import { WebContentSvc } from './../../../services/content-management.service';
import { LoggerService } from './../../../services/logger.service';
import { Message } from '../../../shared/message';
import { CounterMessageService } from './../../../services/counter-message.service';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
	provide: NG_VALUE_ACCESSOR,
	useExisting: forwardRef(() => FileUploadComponent),
	multi: true
};

/**
 * \@author Dinakar.Meda
 * \@author Vivek.Kamineni
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
	selector: 'nm-upload',
	providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc ],
	styles: [
	],
	encapsulation: ViewEncapsulation.None,
	template: `
			<p-fileUpload #pfu name="pfileupload" 
				[showUploadButton]="false" 
				[showCancelButton]="false"
				[customUpload]="true"
				(onSelect)="addFiles($event)"
				(onRemove)="removeFiles($event)"
				url="{{element?.config?.uiStyles?.attributes?.url}}" 
				accept="{{element?.config?.uiStyles?.attributes?.type}}">
			</p-fileUpload>
	`
})

export class FileUploadComponent extends BaseElement implements ControlValueAccessor {

	@Input() element: Param;
	@Input() form: FormGroup;
	@Input('value') _value;
	selectedFiles: File[];
	multipleFiles: boolean = false;
	@ViewChild('pfu') pfu;
	sendEvent: boolean = true;

	constructor(private fileService: FileService, private _wcs: WebContentSvc, private logger: LoggerService, private counterMessageService: CounterMessageService) {
		super(_wcs);
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

	ngOnInit() {

		this.selectedFiles = [];
		this.fileService.metaData = this.element.config.uiStyles.attributes.metaData;
	
		this.fileService.errorEmitter$.subscribe(data => {

			this.pfu.files = [];
			
			this.element.message = [];
			this.element.message.push(Message.createMessage("DANGER", "TOAST", "File Upload Failed", 10000, ''));
			
        });
	}

	ngAfterViewInit() {
		if(this.form!= undefined && this.form.controls[this.element.config.code]!= null) {
            let frmCtrl = this.form.controls[this.element.config.code];
            frmCtrl.valueChanges.subscribe(($event) => 
            {
                if(frmCtrl.valid && this.sendEvent) {
                    this.counterMessageService.evalCounterMessage(true);
                    this.counterMessageService.evalFormParamMessages(this.element);
                    this.sendEvent = false;
                } else if(frmCtrl.invalid) {
                    this.counterMessageService.evalFormParamMessages(this.element);
                    this.counterMessageService.evalCounterMessage(true);
                    this.sendEvent = true;
                }
            });
        }
	}
	addFiles(event) {

		let files = event.originalEvent.dataTransfer ? event.originalEvent.dataTransfer.files : event.originalEvent.target.files;

		for (var p = 0; p < files.length; p++) {
			if (this.hasFile(files[p]) == -1) {

				if (!this.multipleFiles) {
					this.selectedFiles = [];
					this.value = this.selectedFiles;
				}
				if (this.pfu.isFileTypeValid(files[p])) {
					let file = files[p];					
					this.selectedFiles.push(file);
					this.value = this.selectedFiles;

					file['postUrl'] = this.element.config.uiStyles.attributes.url;

				}
			}

		}
	}

	removeFiles(event) {
		let index = this.hasFile(event.file);
		if (index > -1) {
			this.selectedFiles.splice(index, 1);
		}
		this.value = this.selectedFiles;

	}

	hasFile(file: File): number {
		let index = -1;
		for (var p=0; p<this.selectedFiles.length; p++) {
			let selFile = this.selectedFiles[p];
			if (selFile.name == file.name && selFile.size == file.size) {
				index = p;
			}
		}
		return index;
	}

}
