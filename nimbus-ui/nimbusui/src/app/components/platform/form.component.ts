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
import { FormGroup, Validators, ValidatorFn } from '@angular/forms';
import { FormElementsService } from './form-builder.service';
import { PageService } from '../../services/page.service';
import { ValidationUtils } from './validators/ValidationUtils';
import { Param, Model } from '../../shared/param-state';
import { LoggerService } from '../../services/logger.service';

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
export class Form implements OnInit, OnChanges {
    @Input() element: Param;
    @Input() model: Model;
    id: string = 'nm-element'+ uniqueId++;
    formId: string = 'nm-form'+ uniqueId++;
    form: FormGroup;
    opened: Boolean = true;
    groups: Array<any> =[];
    formElements: Param[] = [];
    formGroupElements: Param[] = [];
    buttonList: Param[] = [];
    elementCss: string;

    constructor(private service: FormElementsService, private pageSvc: PageService, private wcs: WebContentSvc, private logger: LoggerService) {

    }

    toggle() {
        this.opened = !this.opened;
    }
    
    groupFormElements(model : Model){
        if (model && model.params) {
            model.params.forEach(element => {
                if (element.config && element.config.uiStyles) {
                    if(element.config.uiStyles!= null && element.config.uiStyles.attributes.alias === 'Accordion') {
                        this.groups.push(element);
                    } else {
                        this.formElements.push(element);
                    }
                } else {
                    if(element.type){
                        this.groupFormElements(element.type.model)
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
        this.logger.debug('Form-i ' + this.element.path);
        if(this.element.config.uiStyles.attributes.cssClass === 'sixColumn') {
            this.elementCss = 'col-lg-2 col-md-4 col-sm-12';
        } else if(this.element.config.uiStyles.attributes.cssClass === 'fourColumn') {
            this.elementCss = 'col-lg-3 col-md-6 col-sm-12';
        } else if(this.element.config.uiStyles.attributes.cssClass === 'threeColumn') {
            this.elementCss = 'col-lg-4 col-md-6 col-sm-12';
        } else if(this.element.config.uiStyles.attributes.cssClass === 'twoColumn') {
            this.elementCss = 'col-sm-12 col-md-6';        
        } else if(this.element.config.uiStyles.attributes.cssClass === 'oneColumn') {
            this.elementCss = 'col-sm-12';        
        } else if(this.element.config.uiStyles.attributes.cssClass === 'inline') {
            this.elementCss = 'd-inline-block mr-3';
        } else if(this.element.config.uiStyles.attributes.cssClass === 'questionGroup') {
            this.elementCss = ' questionGroup';
        } else {
            this.elementCss = this.element.config.uiStyles.attributes.cssClass;
        }

        this.buildFormElements(this.model);
    }
    
    /** Loop through the config and build Form Elements **/
    buildFormElements(model: Model) {
        this.formElements = [];
        this.formGroupElements = [];
        this.groups = [];
        
        // Seperate out the accordions from the individual form elements
        this.groupFormElements(this.model);

        // the below to if conditions are for creating the input data for building the form for the reactiveforms module binding   
        if(this.formElements.length > 0) {
             this.formGroupElements = this.formGroupElements.concat(this.formElements);
        }

        if(this.groups.length>0) {
            this.groups.forEach(element => {
                this.formGroupElements = this.formGroupElements.concat(element.type.model.params);
            });
        }
        var checks: ValidatorFn[] = [];
        checks = ValidationUtils.buildStaticValidations(this.element);

        this.form = this.service.toFormGroup(this.formGroupElements,checks);
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
}
