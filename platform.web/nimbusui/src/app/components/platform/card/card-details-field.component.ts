/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { Param } from '../../../shared/app-config.interface';
import { Component, Input, forwardRef } from '@angular/core';
import { WebContentSvc } from '../../../services/content-management.service';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => CardDetailsFieldComponent),
  multi: true
};

@Component({
    selector: 'nm-card-details-field',
    providers: [
        WebContentSvc, CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR
    ],
    templateUrl: './card-details-field.component.html'
})

export class CardDetailsFieldComponent  implements ControlValueAccessor {
    @Input() element: Param;
    @Input('value') _value='';
    private label: string;
    private iconClass: string = ''; // default to 'not an icon'.
    private fieldClass: string = 'col-sm-3'; // occupies 1 col of 4

    constructor(private wcs: WebContentSvc) {
        wcs.content$.subscribe(result => {
            this.label = result.label;
        });
    }

    ngOnInit() {
        // field style
        if (this.element.config.uiStyles.attributes.cols === '2') { // occupies 2 cols of 4
            this.fieldClass = 'col-sm-6';
        }
        // label content
        this.wcs.getContent(this.element.config.code);

        // icon class
        this.setIconClass();

    }

    setIconClass() {
        if (this.element.config.uiStyles.attributes.iconField !== '') {
            this.iconClass = 'iconField ' + this.element.config.uiStyles.attributes.iconField;
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
        //console.log(value);
        if (value) {
        this.value = value;
        }
    }

    registerOnTouched(fn) {
        this.onTouched = fn;
    }

}

