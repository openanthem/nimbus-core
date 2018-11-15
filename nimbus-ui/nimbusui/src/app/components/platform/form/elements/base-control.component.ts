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

import { LabelConfig, Constraint } from './../../../../shared/param-config';
import { BaseControlValueAccessor } from './control-value-accessor.component';
import { Input, ChangeDetectorRef } from '@angular/core';
import { FormGroup, NgModel } from '@angular/forms';
import { Param } from '../../../../shared/param-state';
import { WebContentSvc } from '../../../../services/content-management.service';
import { ValidationUtils } from '../../validators/ValidationUtils';
import { ValidationConstraint } from './../../../../shared/validationconstraints.enum';
import { Subscription } from 'rxjs';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';
import { ParamUtils } from './../../../../shared/param-utils';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
export abstract class BaseControl<T> extends BaseControlValueAccessor<T> {
    @Input() element: Param;
    @Input() form: FormGroup;
    
    protected abstract model: NgModel;
    protected _elementStyle: string;
    inPlaceEditContext: any;
    showLabel: boolean = true;
    disabled: boolean;
    requiredCss:boolean = false;
    labelConfig: LabelConfig;

    stateChangeSubscriber: Subscription;
    validationChangeSubscriber: Subscription;
    onChangeSubscriber: Subscription;

    constructor(protected controlService: ControlSubscribers, private wcs: WebContentSvc, private cd: ChangeDetectorRef) {
        super();
    }

    setState(val:any,frmInp:any) {
        frmInp.element.leafState = val;
        this.cd.markForCheck();
        if(val == null) {  //if the val is null - the form is set for the first time or it is being reset or user clearing the value (date component)
            this.controlService.resetPreviousLeafState(val);
        }
    }

    emitValueChangedEvent(formControl:any,$event:any) {
        if (this.inPlaceEditContext) {
            this.inPlaceEditContext.value = formControl.value;
        }
        if(this.form == null || (this.form.controls[this.element.config.code]!= null && this.form.controls[this.element.config.code].valid)) {
            this.controlService.controlValueChanged.emit(formControl.element);
        }
            
    }

    ngOnInit() {
        
        this.value = this.element.leafState;
        this.disabled = !this.element.enabled;
        this.loadLabelConfig(this.element);
        this.requiredCss = ValidationUtils.applyelementStyle(this.element);
        if (this.form) {
            let frmCtrl = this.form.controls[this.element.config.code];
            //rebind the validations as there are dynamic validations along with the static validations
            if(frmCtrl!=null && this.element.activeValidationGroups != null && this.element.activeValidationGroups.length > 0) {
                this.requiredCss = ValidationUtils.rebindValidations(frmCtrl,this.element.activeValidationGroups,this.element);
            } 
        }
    }

    /**	
     * Retrieve the label config from the provided param and set it into this instance's labelConfig.
     * @param param The param for which to load label content for.	
     */	
    protected loadLabelConfig(param: Param): void {	
        this.labelConfig = this.wcs.findLabelContent(param);	
    }

    ngAfterViewInit(){
        if(this.form!= undefined && this.form.controls[this.element.config.code]!= null) {
            this.form.controls[this.element.config.code].valueChanges.subscribe(($event) => this.setState($event,this));
            this.controlService.stateUpdateSubscriber(this);
            this.controlService.validationUpdateSubscriber(this);
        }
        this.controlService.onChangeEventSubscriber(this);
    }

   
    /** invoked from InPlaceEdit control */
    setInPlaceEditContext(context: any) {
        this.showLabel = false;
        this.inPlaceEditContext = context;
    }
    /**
     * The hidden attribute for this param
     */
    public get hidden(): boolean {
        return this.element.config.uiStyles.attributes.hidden;
    }

    /**
     * The help attribute for this param
     */
    public get help(): string {
        return this.element.config.uiStyles.attributes.help;
    }

    /**
     * The help readOnly for this param
     */
    public get readOnly(): boolean {
        return this.element.config.uiStyles.attributes.readOnly;
    }

    /**
     * The type attribute for this param
     */
    public get type(): string {
        return this.element.config.uiStyles.attributes.type;
    }

    /**
     * Get the tooltip help text for this element.
     */
    public get helpText(): string {
        return ParamUtils.getHelpText(this.labelConfig);
    }

    /**
     * Get the label text for this element.
     */
    public get label(): string {
        return ParamUtils.getLabelText(this.labelConfig);
    }

    /**
     * Determine if the label for this element is empty or not.
     */
    public get isLabelEmpty(): boolean {
        if (this.label) {
            return this.label.trim().length === 0;
        }
        return true;
    }
    
    /**
     * Return constraint matches param attribute
     * @param name 
     */
    public getConstraint(name: string):Constraint {
        if (this.element.config.validation) {
            if ( !this.element.config.validation.constraints) {
                return;
            }
            let constraints = this.element.config.validation.constraints.filter( constraint => constraint.name === name);
            if (constraints.length >= 2) {
                throw new Error('Constraint array list should not have more than one attribute '+name);
            } else {
                return constraints[0];
            }
        } else {
            return;
        }
    }
    /**
     * Get Max length of the attribute
     */
    public getMaxLength():number {
        let constraint = this.getConstraint(ValidationConstraint._max.value);
        if (constraint) {
             return constraint.attribute.value;
        } else {
            return;
        }

    }
}
