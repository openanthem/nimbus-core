'use strict';
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Component, ViewChild, forwardRef, Input } from '@angular/core';
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
    <label *ngIf="element.config?.uiStyles?.attributes?.hidden!=true" [attr.for]="element.config?.code" class="">{{label}} 
        <nm-tooltip *ngIf="element.config?.uiStyles?.attributes?.help!=''" [helpText]='element.config?.uiStyles?.attributes?.help'></nm-tooltip>
    </label>  
    <input 
        [(ngModel)] = "value"
        [id]="element.config?.code" 
        (focusout)="emitValueChangedEvent(this,value)"
        [value]="element.config?.uiStyles?.attributes?.type"
        class="form-control" *ngIf="element.config?.uiStyles?.attributes?.readOnly==false && element.config?.uiStyles?.attributes?.hidden!=true"/>
    
    <p style="margin-bottom:0rem;" *ngIf="element.config?.uiStyles?.attributes?.readOnly==true">{{element.leafState}}</p>
  
    <input [id]="element.config?.code" type="hidden" 
    [value]="element.leafState" *ngIf="element.config?.uiStyles?.attributes?.hidden==true"/>
   `
})
export class InputText extends BaseControl<String> {

   @ViewChild(NgModel) model: NgModel;

     element: Param;

    constructor(wcs: WebContentSvc, pageService: PageService) {
        super(pageService,wcs);
    }
}
