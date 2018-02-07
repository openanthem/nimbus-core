'use strict';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { Param } from '../../../shared/app-config.interface';
import { Component, Input, forwardRef } from '@angular/core';
import { WebContentSvc } from '../../../services/content-management.service';
import { BaseElement } from './../base-element.component';

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
        if (this.element.config.uiStyles.attributes.cols === '2') { // occupies 2 cols of 4
            this.fieldClass = 'col-sm-6';
        }

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

