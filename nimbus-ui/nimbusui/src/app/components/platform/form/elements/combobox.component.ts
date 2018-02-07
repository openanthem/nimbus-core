'use strict';
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Component, ViewChild, forwardRef, ChangeDetectorRef } from '@angular/core';
import { SelectItemPipe } from './../../../../pipes/select-item.pipe';
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
    <label [attr.for]="element.config?.code" class="{{elementStyle}}" *ngIf="this.showLabel">{{label}}
        <nm-tooltip *ngIf="helpText" [helpText]='helpText'></nm-tooltip>
    </label>
    <p-dropdown 
        [options]="element.values | selectItemPipe" 
        [(ngModel)] = "value"
        [disabled]="!element?.enabled?.currState"
        (onChange)="emitValueChangedEvent(this,$event)"
        class="form-control" 
        placeholder="Please Select...">
    </p-dropdown>
   `
})

export class ComboBox extends BaseControl<String> {

    @ViewChild(NgModel) model: NgModel;

    constructor(wcs: WebContentSvc, pageService: PageService, cd:ChangeDetectorRef) {
        super(pageService,wcs,cd);
    }

}
