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
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { Param } from '../../../shared/param-state';
import { Component, Input, forwardRef } from '@angular/core';
import { WebContentSvc } from '../../../services/content-management.service';
import { BaseElement } from './../base-element.component';
import {DateTimeFormatPipe} from '../../../pipes/date.pipe';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => CardDetailsFieldComponent),
  multi: true
};

/**
 * \@author Dinakar.Meda
 * @author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-card-details-field',
    providers: [
        WebContentSvc, CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR
    ],
    templateUrl: './card-details-field.component.html'
})

export class CardDetailsFieldComponent  extends BaseElement implements ControlValueAccessor {
    @Input() element: Param;
    @Input('value') _value='';
    private iconClass: string = ''; // default to 'not an icon'.
    private fieldClass: string = 'col-sm-3'; // occupies 1 col of 4

    constructor(private _wcs: WebContentSvc) {
        super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();

        // field style
        if (this.element.config.uiStyles.attributes.cols === '6') { // occupies 1 cols of 6
            this.fieldClass = 'col-sm-2';
        } else if (this.element.config.uiStyles.attributes.cols === '4') { // occupies 1 cols of 4
            this.fieldClass = 'col-sm-3';
        } else if (this.element.config.uiStyles.attributes.cols === '3') { // occupies 1 cols of 3
            this.fieldClass = 'col-sm-4';
        } else if (this.element.config.uiStyles.attributes.cols === '2') { // occupies 1 cols of 2
            this.fieldClass = 'col-sm-6';
        } else if (this.element.config.uiStyles.attributes.cols === '1') { // occupies 1 col of 1
            this.fieldClass = 'col-sm-12';
        } else {
            this.fieldClass = 'col-sm-3';
        }
        // icon class
        this.setIconClass();

    }
//icon not used but this will pass custom class to value field
    setIconClass() { 
        if (this.element.config.uiStyles.attributes.iconField !== '') {
            this.iconClass = this.element.config.uiStyles.attributes.iconField;
        }
    }

    onChange: any = () => { };
    onTouched: any = () => { };
    get value() {
        return this._value;
    }

    set value(val) {
        if (this.element.values.length > 0) {
            let desc: string = undefined;
            let indexVal: any = undefined;
            for (let i = 0; i < this.element.values.length; i++) {
                indexVal = this.element.values[i];
                if (indexVal.code === this.element.leafState) {
                    desc = indexVal.label;
                    break;
                }
            }
            if (desc) {
                this._value = desc;
            } else  {
                this._value = this.element.leafState;
            }
        } else {
            this._value = this.element.leafState;
        }
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

