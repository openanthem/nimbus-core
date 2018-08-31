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
import { Param } from '../../../../shared/param-state';
import { Component, forwardRef, Input,Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { FormGroup, NgModel} from '@angular/forms';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import {SelectItem} from 'primeng/primeng';
import { GenericDomain } from '../../../../model/generic-domain.model';
import { BaseElement } from './../../base-element.component';
import { ValidatorFn } from '@angular/forms/src/directives/validators';
import { ValidationUtils } from '../../validators/ValidationUtils';
import { HttpMethod } from './../../../../shared/command.enum';
/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-multi-select-listbox',
    providers: [
        WebContentSvc
    ],
    template: `
        <div [formGroup]="form"  [hidden]="!element?.visible">
            <nm-input-label
                [element]="element" 
                [for]="element.config.code"
                [required]="requiredCss">

            </nm-input-label>
            <p-listbox [options]="optionsList" formControlName="{{element.config.code}}" multiple="multiple" 
            (onChange)="emitValueChangedEvent(this,value)" checkbox="checkbox" filter="filter" [style]="{'width':'190px','max-height':'250px'}"></p-listbox>
        </div>
   `
})

export class MultiSelectListBox extends BaseElement{

    @Input() element: Param;
    @Input() form: FormGroup;
    @Output() controlValueChanged =new EventEmitter();
    value = [];
    public label: string;
    public helpText : string;
    optionsList: SelectItem[];
    private targetList: any[];

    constructor(private _wcs: WebContentSvc, private pageService: PageService, private cd: ChangeDetectorRef) {
       super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();
        this.optionsList = [];
        let frmCtrl = this.form.controls[this.element.config.code];
        //rebind the validations as there are dynamic validations along with the static validations
        if(frmCtrl!=null && this.element.activeValidationGroups != null && this.element.activeValidationGroups.length > 0) {
            this.requiredCss = ValidationUtils.rebindValidations(frmCtrl,this.element.activeValidationGroups,this.element);
        } 
        this.element.values.forEach(element => {
            this.optionsList.push({label: element.label, value: element.code});
        });
        //set the default target list when the page loads to the config state
        if(this.element.leafState !=null) {
            this.targetList = [];
            this.targetList = this.element.leafState;
            this.form.controls[this.element.config.code].setValue(this.targetList);
        } else {
            this.targetList = [];
            this.form.controls[this.element.config.code].setValue(this.targetList);
        }
        if( this.form.controls[this.element.config.code]!= null) {
            this.form.controls[this.element.config.code].valueChanges.subscribe(
                ($event) => { 
                    this.setState($event,this); 
                });
        }
        this.controlValueChanged.subscribe(($event) => {
            if ($event.config.uiStyles.attributes.postEventOnChange) {
               this.pageService.postOnChange($event.path, 'state', JSON.stringify($event.leafState));
            } else if($event.config.uiStyles.attributes.postButtonUrl) {
               let item: GenericDomain = new GenericDomain();
               this.pageService.processEvent(this.element.config.uiStyles.attributes.postButtonUrl, null, $event.leafState, HttpMethod.POST.value);
            }
        });

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

    setState(event:any, frmInp:any) {
        frmInp.element.leafState = event;
        this.cd.markForCheck();
    }

    emitValueChangedEvent(formControl:any,$event:any) {
        this.controlValueChanged.emit(formControl.element);
    }

}
