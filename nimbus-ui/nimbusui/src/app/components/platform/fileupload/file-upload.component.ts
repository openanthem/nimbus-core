import {
	Component, Input, ViewChild, ViewEncapsulation,
	ElementRef, ViewChildren, QueryList, forwardRef, ChangeDetectorRef
} from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { FormGroup, FormControl, FormArray, FormBuilder, Validators } from '@angular/forms';
import { FormArrayName } from '@angular/forms/src/directives/reactive_directives/form_group_name';
import { Param } from '../../../shared/app-config.interface';
import { FileService } from './../../../services/file.service';
import { BaseElement } from '../base-element.component';
import { WebContentSvc } from './../../../services/content-management.service';


export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
	provide: NG_VALUE_ACCESSOR,
	useExisting: forwardRef(() => FileUploadComponent),
	multi: true
};

@Component({
	selector: 'nm-upload',
	providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc ],
	styles: [
	],
	encapsulation: ViewEncapsulation.None,
	template: `
			<p-fileUpload name="pfileupload" 
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

	constructor(private fileService: FileService, private _wcs: WebContentSvc) {
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
		if(value) {
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
		// if( this.form!= null && this.form.controls[this.element.config.code]!= null) {
		// 	this.form.controls[this.element.config.code].valueChanges.subscribe(
		// 		($event) => { 
		// 			console.log($event); 
		// 		});
		// }

		this.fileService.addFile$.subscribe(file => {
			if (!this.multipleFiles) {
				this.selectedFiles = [];	
			}
			this.selectedFiles.push(file);
			this.value = this.selectedFiles;
			
		});
	}

	addFiles(event) {
		let files = event.originalEvent.dataTransfer ? event.originalEvent.dataTransfer.files : event.originalEvent.target.files;
		for ( var p=0; p< files.length; p++ ) {
			if (this.hasFile(files[p]) == -1) {
				let file = files[p];
				file['postUrl'] = this.element.config.uiStyles.attributes.url;
				// upload the file to get file Id
				this.fileService.uploadFile(file);
			}
		}
		//this.value = this.selectedFiles;
	}

	removeFiles(event) {
		let index = this.hasFile(event.file);
		if ( index > -1) {
			this.selectedFiles.splice(index, 1);

			// this.fileService.removeFile(file);
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
