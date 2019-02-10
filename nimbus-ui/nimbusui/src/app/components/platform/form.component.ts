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
import { WebContentSvc } from './../../services/content-management.service';
import { Component, Input, OnInit, OnChanges, SimpleChanges, SimpleChange } from '@angular/core';
import { FormGroup, ValidatorFn } from '@angular/forms';
import { FormElementsService } from './form-builder.service';
import { PageService } from '../../services/page.service';
import { ValidationUtils } from './validators/ValidationUtils';
import { Param, Model } from '../../shared/param-state';
import { LoggerService } from '../../services/logger.service';
import { BaseElement } from './base-element.component';
import { FormModel, FormElementType } from '../../model/form.model';

var uniqueId = 0;

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-form',
    styles: [`
        .hide {
        display: none;
        },
        `
    ],
    templateUrl: './form.component.html',
    providers: [ FormElementsService, WebContentSvc]
})
export class Form extends BaseElement implements OnInit, OnChanges {
    @Input() element: Param;
    @Input() model: Model;
    id: string = 'nm-element'+ uniqueId++;
    formId: string = 'nm-form'+ uniqueId++;
    form: FormGroup;
    opened: Boolean = true;
    formModel: FormModel[];
    formElementType = FormElementType;
    buttonList: Param[] = [];
    elementCss: string;

    formElements: Param[] = [];

    // accordionGroups: Array<any> =[];
    // formGroupElements: Param[] = [];

    constructor(private service: FormElementsService, 
        private pageSvc: PageService, 
        private wcsv: WebContentSvc, 
        private logger: LoggerService) {
            super(wcsv);
    }

    toggle() {
        this.opened = !this.opened;
    }
    
    /**
     * Iterates through the Form definition to separte out the different Form Elements.
     * The recursion parameter is used to indicate if this method is called the first time or if it is called through recurssion.
     * The need for this - we want one recursion to get the entire list of paramters to build the reactive form validations. 
     * At the same time, we also also to capture the Form Elements to display. We do not want the nested form elements at thie form level. 
     * The nested elements will be accounted for in the sub components.
     * @param model 
     * @param recursion 
     */
    groupFormElements(model : Model, recursion: boolean){
        if (model && model.params) {
            model.params.forEach(element => {
                if (element.config && element.config.uiStyles) {
                    if(element.config.uiStyles != null && element.config.uiStyles.attributes.alias === 'Accordion') {
                        if (!recursion) {
                            let currElement = {} as FormModel;
                            currElement.elementType = FormElementType.ACCORDION;
                            currElement.param = element;
                            this.formModel.push(currElement);
                        }

                        if(element.type && element.type.model.params) {
                            element.type.model.params.forEach(accElem => {
                                this.groupFormElements(accElem.type.model, true)
                            });
                        }

                        // this.accordionGroups.push(element);
                    } else if (element.config.uiStyles != null && element.config.uiStyles.attributes.alias === 'FormElementGroup') {
                        if (!recursion) {
                            let currElement = {} as FormModel;
                            currElement.elementType = FormElementType.ELEMENTGROUP;
                            currElement.param = element;
                            this.formModel.push(currElement);
                        }

                        if(element.type){
                            this.groupFormElements(element.type.model, true)
                        }
                    } else {
                        if (!recursion) {
                            let currElement = {} as FormModel;
                            currElement.elementType = FormElementType.ELEMENT;
                            currElement.param = element;
                            this.formModel.push(currElement);
                        }

                        this.formElements.push(element);
                    }
                } else {
                    if(element.type){
                        this.groupFormElements(element.type.model, false);
                    }
                }
             });
        }
    }
    
    /** Handling model changes to Form **/
    ngOnChanges(changes: SimpleChanges) {
        const model: SimpleChange = changes.model;
        this.buildFormElements(model.currentValue);
    }

    /** Initialize the Form **/
    ngOnInit() {
        super.ngOnInit();
        this.logger.debug('Form-i ' + this.element.path);
        if(this.element.config.uiStyles.attributes.cssClass === 'inline') {
            this.elementCss = 'd-block d-md-inline-block mr-3';
        } else {
            this.elementCss = this.element.config.uiStyles.attributes.cssClass;
        }
        this.buildFormElements(this.model);
        this.updatePosition();
    }
    
    /** Loop through the config and build Form Elements **/
    buildFormElements(model: Model) {
        this.formModel = [];
        this.formElements = [];
        // this.formGroupElements = [];
        // this.accordionGroups = [];
        
        // Seperate out the accordions from the individual form elements
        this.groupFormElements(this.model, false);

        // the below two if conditions are for creating the input data for building the form for the reactiveforms module binding   
        // if(this.formElements.length > 0) {
        //      this.formGroupElements = this.formGroupElements.concat(this.formElements);
        // }

        // if(this.accordionGroups.length>0) {
        //     this.accordionGroups.forEach(element => {
        //         this.formGroupElements = this.formGroupElements.concat(element.type.model.params);
        //     });
        // }

        // Build the reactiveforms module binding with validations
        var checks: ValidatorFn[] = [];
        checks = ValidationUtils.buildStaticValidations(this.element);

        this.form = this.service.toFormGroup(this.formElements, checks);
        this.pageSvc.eventUpdate$.subscribe(event => {
            if(event.config && event.config.uiStyles != null && event.config.uiStyles.attributes.alias === 'Form' && event.path === this.element.path) {
                if(event.leafState != null && !this.hasNull(event.leafState))
                    this.form.patchValue(event.leafState);
                //form reset will be addressed at the each control level where the update would be sent by the server
            }
        });
    }

     hasNull(target) {
        for (var member in target) {
            if (target[member] == null)
                return true;
        }
        return false;
    }

    partialUpdate(obj:any) {
        this.form.patchValue(obj);
        //this.form.patchValue({firstName: 'Partial'});
    }
    
    getElementClass(parentCss: String, child: Param) {
        if (child.config.uiStyles) {
            if (child.config.uiStyles.attributes && child.config.uiStyles.attributes.cssClass && child.config.uiStyles.attributes.cssClass != '') {
                return child.config.uiStyles.attributes.cssClass;
            } else {
                if (parentCss) {
                    return parentCss;
                } else {
                    return '';
                }
            }
        } else {
            return '';
        }
    }
}
