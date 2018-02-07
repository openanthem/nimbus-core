'use strict';
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { FormGroup, NG_VALUE_ACCESSOR, NgModel } from '@angular/forms';
import { Component, Input, Output, EventEmitter, forwardRef, ViewChild, ChangeDetectorRef } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/app-config.interface';
import { BaseElement } from './../../base-element.component';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => RadioButton),
  multi: true
};

@Component({
  selector: 'nm-input-radio',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR,WebContentSvc],
  template: `
    <fieldset>
        <legend class="{{elementStyle}}">{{label}}
        <nm-tooltip *ngIf="helpText" [helpText]='helpText'></nm-tooltip>
        </legend>
        <div class="checkboxHolder" >
        <div class="form-checkrow" *ngFor="let val of element?.values; let i = index">
        <p-radioButton name="{{element?.config?.code}}" [(ngModel)]="value" [value]="val.code" [label]="val.label" (ngModelChange)="emitValueChangedEvent(this,$event)"></p-radioButton>
    </div>
        </div>
    </fieldset>
   `
})

export class RadioButton extends BaseControl<String> {

    @ViewChild(NgModel) model: NgModel;

    element: Param;
    

    constructor(wcs: WebContentSvc, pageService: PageService, cd:ChangeDetectorRef) {
        super(pageService, wcs, cd);
    }

}
