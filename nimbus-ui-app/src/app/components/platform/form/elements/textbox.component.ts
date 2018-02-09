'use strict';
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Component, ViewChild, forwardRef, Input, ChangeDetectorRef } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/app-config.interface';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => InputText),
  multi: true
};


@Component({
  selector: 'nm-input',
  providers: [ CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc ],
  template: `
    <label *ngIf="hidden!=true"
        [attr.for]="element.config?.code" class="{{elementStyle}}">{{label}} 
        <nm-tooltip *ngIf="helpText" 
            [helpText]='helpText'>
        </nm-tooltip>
    </label>

    <input *ngIf="hidden!=true"
        [(ngModel)] = "value"
        [id]="element.config?.code" 
        (focusout)="emitValueChangedEvent(this,value)"
        [value]="type"
        class="form-control" 
        [readonly]="readOnly" />

    <!--
    <p style="margin-bottom:0rem;" *ngIf="readOnly">{{element.leafState}}</p>
    -->
    
    <input *ngIf="hidden==true"
        [id]="element.config?.code" 
        type="hidden" 
        [value]="element.leafState" />
   `
})
export class InputText extends BaseControl<String> {

   @ViewChild(NgModel) model: NgModel;

     element: Param;

    constructor(wcs: WebContentSvc, pageService: PageService,cd:ChangeDetectorRef) {
        super(pageService,wcs,cd);
    }
}
