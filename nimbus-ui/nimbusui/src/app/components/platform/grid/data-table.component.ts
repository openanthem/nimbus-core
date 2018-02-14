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
import { BaseElement } from './../base-element.component';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Component, Input, Output, forwardRef, 
    ViewChild, EventEmitter, ViewEncapsulation, ChangeDetectorRef } from '@angular/core';
import { GenericDomain } from '../../../model/generic-domain.model';
import { Param, ParamConfig } from '../../../shared/app-config.interface';
import { PageService } from '../../../services/page.service';
import { GridService } from '../../../services/grid.service';
import { WebContentSvc } from '../../../services/content-management.service';
import { DataTable, OverlayPanel, Paginator } from 'primeng/primeng';
import { ElementModelParam } from './../../../shared/app-config.interface';
import { ServiceConstants } from './../../../services/service.constants';
import { ControlValueAccessor } from '@angular/forms/src/directives';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => NMDataTable),
    multi: true
  };

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-data-table',
    providers: [ CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc ],
    encapsulation: ViewEncapsulation.None,
    templateUrl: './data-table.component.html'
})
export class NMDataTable extends BaseElement implements ControlValueAccessor {
    @Input() data: any[];
    @Input() params: ElementModelParam[];
    @Input('value') _value = [];

    constructor(
        private pageService : PageService, 
        private webContentSvc: WebContentSvc, 
        private gridService: GridService, 
        private changeDetectorRef: ChangeDetectorRef) {
            super(webContentSvc);
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

    public writeValue(obj: any): void {
        if (obj !== undefined) {
        }
        this.changeDetectorRef.markForCheck();
    }

    public registerOnChange(fn: any): void {
       this.onChange = fn;
    }

    public registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    ngOnInit() {
        super.ngOnInit();
    }

}
