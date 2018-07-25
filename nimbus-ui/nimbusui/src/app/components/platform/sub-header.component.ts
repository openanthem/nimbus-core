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
import { Component, Input, SimpleChanges, forwardRef } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { Param } from '../../shared/param-state';
import { WebContentSvc } from '../../services/content-management.service';
import { BaseElement } from './base-element.component';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
     provide: NG_VALUE_ACCESSOR,
     useExisting: forwardRef(() => SubHeaderCmp),
     multi: true
    };

@Component({
    selector: 'nm-subheader',
    providers: [WebContentSvc, CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
    template:`
        <ng-template [ngIf]="!element?.config?.type?.nested">
           <div class="{{element?.config?.uiStyles?.attributes?.cssClass}}">
                <ng-template [ngIf]="isDate(element.config.type.name)">
                    <span [hidden]="!element?.config?.uiStyles?.attributes?.showName">{{label}}</span>
                    <span>@{{value | dateTimeFormat: element.config?.uiStyles?.attributes?.datePattern : element.config.type.name }}@</span>
                </ng-template>
                <div *ngIf="!isDate(element.config.type.name)">
                    <span [hidden]="!element?.config?.uiStyles?.attributes?.showName">{{label}}</span>
                    <span>*{{value}}*</span>
                </div>
           </div>
        </ng-template>
    `
})

export class SubHeaderCmp extends BaseElement implements ControlValueAccessor {

 //   @Input() element: element;
    @Input('value') _value = '';

    constructor(private _wcs: WebContentSvc) {
        super(_wcs);
    }
    ngOnInit() {
        this.loadLabelConfig(this.element);
    }
    ngOnChanges(changes: SimpleChanges) {
        if(changes['element']) {
            console.log('changes on element suheader');
        }
    }

    onChange: any = () => { }; 
    onTouched: any = () => { }; 

    get value() {
        return this._value;
    }

    set value(val) {
        this._value = this.element.leafState;
        this.onChange(val); 
        this.onTouched();
    }

    registerOnChange(fn) { 
        this.onChange = fn;
    }  
    
    writeValue(value) { 
        if (value) { 
            this.value = value; 
        } 
    }  
    
    registerOnTouched(fn) { 
        this.onTouched = fn; 
    }
}
