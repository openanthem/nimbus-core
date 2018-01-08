'use strict';
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Component, ViewChild, forwardRef, ChangeDetectorRef } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/app-config.interface';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => ComboBox),
  multi: true
};

@Component({
  selector: 'nm-comboBox',
  providers: [ CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc ],
  template: `
    <label [attr.for]="element.config?.code" class="" *ngIf="this.showLabel">{{label}}
        <nm-tooltip *ngIf="element.config?.uiStyles?.attributes?.help!=''" [helpText]='element.config?.uiStyles?.attributes?.help'></nm-tooltip>
    </label>
    <select
        [(ngModel)] = "value"
        [id]="element.config?.code" 
        [disabled]="!element?.enabled?.currState"
        (change)="emitValueChangedEvent(this,$event)"
        class="form-control">
        <option value=""></option>
        <option value="{{value.code}}" *ngFor="let value of element.values"> {{value.label}}</option>
    </select>
   `
})

export class ComboBox extends BaseControl<String> {

    @ViewChild(NgModel) model: NgModel;

    element: Param;

    constructor(wcs: WebContentSvc, pageService: PageService, cd:ChangeDetectorRef) {
        super(pageService,wcs,cd);
    }

}
