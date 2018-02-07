'use strict';
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Component, ViewChild, forwardRef, ChangeDetectorRef } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/app-config.interface';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => TextArea),
  multi: true
};

@Component({
  selector: 'nm-input-textarea',
  providers: [ CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc ],
  template: `
    <!--<div class='textarea-holder' [hidden]="!element?.config?.visible?.currState || !element?.visible?.currState" *ngIf="element.config?.uiStyles?.attributes?.hidden==false">-->
    <div class='textarea-holder' [hidden]="!element?.visible?.currState" *ngIf="element.config?.uiStyles?.attributes?.hidden==false">
        <div class="number" *ngIf="element.config?.uiStyles?.attributes?.controlId!=''">{{element.config?.uiStyles?.attributes?.controlId}}</div>
        <label [attr.for]="element.config?.code" class="{{elementStyle}}">{{label}}
            <nm-tooltip *ngIf="helpText" [helpText]='helpText'></nm-tooltip>
        </label>
        <textarea [(ngModel)] = "value" 
        rows="element.config?.uiStyles?.attributes?.rows"  
            (focusout)="emitValueChangedEvent(this,value)"
            [id]="element.config?.code" class="form-control" *ngIf="element.config?.uiStyles?.attributes?.readOnly==false"></textarea>
        <p style="margin-bottom:0rem;" *ngIf="element.config?.uiStyles?.attributes?.readOnly==true">{{element.leafState}}</p>
    </div>
   `
})
export class TextArea extends BaseControl<String> {

    @ViewChild(NgModel) model: NgModel;

     element: Param;

    constructor(wcs: WebContentSvc, pageService: PageService,cd:ChangeDetectorRef) {
        super(pageService,wcs,cd);
    }
}
