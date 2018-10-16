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
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { Component, Input, Output, EventEmitter,forwardRef, ChangeDetectorRef } from '@angular/core';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Param } from '../../../../shared/param-state';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { ServiceConstants } from '../../../../services/service.constants';
import { BaseElement } from './../../base-element.component';
import { ValidatorFn } from '@angular/forms/src/directives/validators';
import { ValidationUtils } from '../../validators/ValidationUtils';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => CheckBoxGroup),
  multi: true
};

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
  selector: 'nm-input-checkbox',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR,WebContentSvc ],
  template: `
      <fieldset>
           <nm-input-legend [element]="element" [required]="requiredCss"></nm-input-legend>
          <div class="checkboxHolder" [formGroup]="form" >
            <div class="form-checkrow" *ngFor="let val of element?.values; let i = index">
            <p-checkbox name="{{element?.config?.code}}" [formControl]="form.controls[element?.config?.code]" [value]="val.code" [label]="val.label" (onChange)="emitValueChangedEvent(this,$event)"></p-checkbox>
            </div>
          </div>
    </fieldset>
   `
})

export class CheckBoxGroup extends BaseElement implements ControlValueAccessor {

    @Input() element: Param;
    @Input() form: FormGroup;
    @Input('value') _value;
    @Output() controlValueChanged =new EventEmitter();
    
    constructor(private pageService: PageService, private _wcs: WebContentSvc, private cd: ChangeDetectorRef) {
        super(_wcs);    
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

    registerOnChange(fn) {
        this.onChange = fn;
    }

    writeValue(value) {
        if(value) {
        }
    }

    registerOnTouched(fn) {
        this.onTouched = fn;
    }

    setState(event:any,frmInp:any) {
        frmInp.element.leafState = event;
        this.cd.markForCheck();
    }

    emitValueChangedEvent(formControl:any,$event:any) {
        if(this.form == null || (this.form.controls[this.element.config.code]!= null && this.form.controls[this.element.config.code].valid)) {
            this.controlValueChanged.emit(formControl.element);
        }
        
    }

    ngOnInit() {
        super.ngOnInit();
        let frmCtrl = this.form.controls[this.element.config.code];
        //rebind the validations as there are dynamic validations along with the static validations
        if(frmCtrl!=null && this.element.activeValidationGroups != null && this.element.activeValidationGroups.length > 0) {
            this.requiredCss = ValidationUtils.rebindValidations(frmCtrl,this.element.activeValidationGroups,this.element);
        } 
        if(this.element.leafState !=null && this.element.leafState.length > 0) {
            this.value = this.element.leafState;
        }
        if( this.form.controls[this.element.config.code]!= null) {
            this.form.controls[this.element.config.code].valueChanges.subscribe(($event) => this.setState($event,this));
            
            this.pageService.eventUpdate$.subscribe(event => {
                let frmCtrl = this.form.controls[event.config.code];
                if(frmCtrl!=null && event.path.startsWith(this.element.path)) {
                    if(event.leafState!=null)
                        frmCtrl.setValue(event.leafState);
                    else
                        frmCtrl.reset();
                }
            });
            this.pageService.validationUpdate$.subscribe(event => {
                let frmCtrl = this.form.controls[event.config.code];
                if(frmCtrl!=null) {
                    if(event.path === this.element.path) {
                       //bind dynamic validations on a param as a result of a state change of another param
                        if(event.activeValidationGroups != null && event.activeValidationGroups.length > 0) {
                            this.requiredCss = ValidationUtils.rebindValidations(frmCtrl,event.activeValidationGroups,this.element);
                        } else {
                            this.requiredCss = ValidationUtils.applyelementStyle(this.element);
                            var staticChecks: ValidatorFn[] = [];
                            staticChecks = ValidationUtils.buildStaticValidations(this.element);
                            frmCtrl.setValidators(staticChecks);
                        }
                        ValidationUtils.assessControlValidation(event,frmCtrl);
                    }
                }
            });
        }
        this.controlValueChanged.subscribe(($event) => {
             if ($event.config.uiStyles.attributes.postEventOnChange) {
                this.pageService.postOnChange($event.path, 'state', JSON.stringify($event.leafState));
             }
         });
    }
}