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
 * 
 */
'use strict';
import { Component, Input } from '@angular/core';
import { FormGroup, AbstractControlDirective, NgModel, ValidationErrors } from '@angular/forms';
import { Param } from '../../shared/param-state';
import { Message } from '../../shared/message';
import { ComponentTypes, ViewComponent } from '../../shared/param-annotations.enum';
import { BaseElement } from './base-element.component';
import { WebContentSvc } from './../../services/content-management.service';
import { ValidationUtils } from './validators/ValidationUtils';
import { AbstractControl } from '@angular/forms/src/model';
import { ConstraintMapping } from './../../shared/validationconstraints.enum';
import { Constraint } from './../../shared/param-config';
import { CounterMessageService } from './../../services/counter-message.service';

var counter = 0;

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-element',
    templateUrl: './form-element.component.html'
})

export class FormElement extends BaseElement {

    @Input() element: Param;
    @Input() form: FormGroup;
    @Input() elementCss: string;
    elemMessages: Message[];
    id: String = 'form-control' + counter++;
    componentStyle: string;
    componentTypes = ComponentTypes;
    viewComponent = ViewComponent;
    componentClass: string[] = ['form-group'];
    errorStyles = 'alert alert-danger';
    fieldDirty: boolean = false;

    get isValid() {
        if (this.form.controls[this.element.config.code] != null) {
            return this.form.controls[this.element.config.code].valid;
        } else {
            return true;
        }
    }

    get isPristine() {
        return this.getPristine();
    }
    getPristine() {
        if (this.element.config.code.startsWith('{')) {
            return true;
        }
        if (this.form.controls[this.element.config.code] != null) {
            return this.form.controls[this.element.config.code].pristine;
        } else {
            return true;
        }
    }
    get elementMessages() {
        return this.getMessages();
    }
    getMessages() {
        this.elemMessages = [];
            if (this.element.message != null && this.element.message.length > 0) {
                this.elemMessages.push.apply(this.elemMessages, this.element.message);
            }
            if (!this.getPristine()) {
                this.updateErrorMessages();
            }
        return this.elemMessages;
    }

    get showMessages() {
        return (this.elemMessages != null && this.elemMessages.length > 0);
    }

    constructor(private wcsv: WebContentSvc, private counterMsgService: CounterMessageService) { 
        super(wcsv);
    }

    getErrorStyles() {
        if (this.showErrors) {
            return this.errorStyles;
        } else {
            return '';
        }
    }

    get showErrors() {
        return (!this.isPristine && !this.isValid);
    }

    getComponentClass() {
        let overrideClass: string = '';
        if (this.element.config.uiStyles && this.element.config.uiStyles.attributes &&
            this.element.config.uiStyles.attributes.cssClass && this.element.config.uiStyles.attributes.cssClass !== '') {
                overrideClass = this.element.config.uiStyles.attributes.cssClass;
        }  
        if (this.element.config.uiStyles && this.element.config.uiStyles.attributes && 
            this.element.config.uiStyles.attributes.cols && this.element.config.uiStyles.attributes.cols !== '') {
            // Convert cols to equivalent css styles
            if (this.element.config.uiStyles.attributes.cols === '6') { // occupies 1 cols of 6
                overrideClass += ' col-sm-2';
            } else if (this.element.config.uiStyles.attributes.cols === '4') { // occupies 1 cols of 4
                overrideClass += ' col-sm-3';
            } else if (this.element.config.uiStyles.attributes.cols === '3') { // occupies 1 cols of 3
                overrideClass += ' col-sm-4';
            } else if (this.element.config.uiStyles.attributes.cols === '2') { // occupies 1 cols of 2
                overrideClass += ' col-sm-6';
            } else if (this.element.config.uiStyles.attributes.cols === '1') { // occupies 1 col of 1
                overrideClass += ' col-sm-12';
            } else {
                overrideClass += ' col-sm-12';
            }
        } 
        if (overrideClass != '') {
            this.componentClass.push(overrideClass);
        } else if(this.elementCss){
            this.componentClass.push(this.elementCss);
        }
        
        return this.componentClass;
    }

    ngOnInit() {
        super.ngOnInit();
        this.updatePositionWithNoLabel();
        this.getComponentClass();
    }

    getElementStyle() {
        if (this.element.config.uiStyles != null && this.element.config.uiStyles.attributes.alias === 'MultiSelectCard') {
            return 'col-lg-12 col-md-6';
        } else {
            return '';
        }
    }

    /**
     * <p>Update all form controls belonging to this form instance that have validation errors present.
     * <p>Error messages are first attempted to be set by setting the message that is defined in the server-side
     * Constraint annotation's message attribute. If a message is not provided, the default message as defined 
     * by ValidationUtils will be used.
     */
    updateErrorMessages() {
        var control: AbstractControl = this.form.controls[this.element.config.code];
        if(control.dirty) {
            if(!this.fieldDirty) {
                this.counterMsgService.evalCounterMessage(true);
                this.fieldDirty = control.dirty;
            }
        }
        let constraintNames = ValidationUtils.getAllValidationNames();
        if (control.invalid) {
            let errs: ValidationErrors = control.errors;
            for (var key in errs) {
                let constraintName = ConstraintMapping.getConstraintValue(key);
                    let constraint: Constraint = this.element.config.validation.constraints.find(v => v.name == constraintName);
                    this.addErrorMessages(constraint.attribute.message ? constraint.attribute.message : ValidationUtils.getDefaultErrorMessage(key));
                    const index = this.componentClass.indexOf(this.errorStyles,0)
                    this.counterMsgService.evalCounterMessage(true);
                    if(index < 0) {
                        this.componentClass.push(this.getErrorStyles());
                    }
            }   
        } else {
            const index = this.componentClass.indexOf(this.errorStyles,0)
            if(index >=0) {
                this.counterMsgService.evalCounterMessage(true);
                this.componentClass.splice(index);
            }
        }
        //if(this.previousvalid !== this.isValid) {
            // this.counterMsgService.evalCounterMessage(control);
        //}
        ///this.previousvalid = this.isValid;
    }

    addErrorMessages(errorText: string) {
        let errorMessage: Message, summary: string;
        errorMessage = new Message();
        errorMessage.context = 'INLINE';
        // errorMessage.life = 10000;
        errorMessage.messageArray.push({ severity: 'error', summary: summary, detail: errorText, life: 10000  });
        this.elemMessages.push(errorMessage);
    }

    ngModelState(ngm: AbstractControlDirective): string {
        let ret = ngm instanceof NgModel ? 'name: ${ngm.name};  ' : '';
        return ret + 'touched: ${ngm.touched};  pristine: ${ngm.pristine};  valid: ${ngm.valid};  errors: ${JSON.stringify(ngm.errors)};  value: ${JSON.stringify(ngm.value)}';
    }
}
