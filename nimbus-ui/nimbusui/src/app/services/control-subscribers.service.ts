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
import { CustomHttpClient } from './httpclient.service';
import { Injectable, EventEmitter, Output } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { HttpHeaders } from '@angular/common/http';
import { RequestOptions, Request, RequestMethod } from '@angular/http';
import { PageService } from './page.service';
import { FormGroup } from '@angular/forms/src/model';
import { ValidationUtils } from '../components/platform/validators/ValidationUtils';
import { BaseControl } from '../components/platform/form/elements/base-control.component';
import { ValidatorFn } from '@angular/forms/src/directives/validators';
import { GenericDomain } from './../model/generic-domain.model';
import { Param } from './../shared/param-state';
import { HttpMethod } from '../shared/command.enum';
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Injectable()
export class ControlSubscribers {
    
    @Output() controlValueChanged =new EventEmitter();

    constructor(private pageService: PageService) {
        
    }

    public stateUpdateSubscriber(control:BaseControl<any>) {
        this.pageService.eventUpdate$.subscribe(event => {
            let frmCtrl = control.form.controls[event.config.code];
            if(frmCtrl!=null && event.path == control.element.path) {
                if(event.leafState!=null){
                    frmCtrl.setValue(event.leafState);
                } else
                    frmCtrl.reset();
            }
        });
    }

    public validationUpdateSubscriber(control:BaseControl<any>) {
        this.pageService.validationUpdate$.subscribe(event => {
            let frmCtrl = control.form.controls[event.config.code];
            if(frmCtrl!=null) {
                if(event.path === control.element.path) {
                    //bind dynamic validations on a param as a result of a state change of another param
                    if(event.activeValidationGroups != null && event.activeValidationGroups.length > 0) {
                        control.requiredCss = ValidationUtils.rebindValidations(frmCtrl,event.activeValidationGroups,control.element);
                    } else {
                        control.requiredCss = ValidationUtils.applyelementStyle(control.element);
                        var staticChecks: ValidatorFn[] = [];
                        staticChecks = ValidationUtils.buildStaticValidations(control.element);
                        frmCtrl.setValidators(staticChecks);
                    }
                    ValidationUtils.assessControlValidation(event,frmCtrl);
                    control.disabled = !event.enabled;   
                }

            }
        });
    }

    public onChangeEventSubscriber(control:BaseControl<any>) {
        this.controlValueChanged.subscribe(($event) => {
            if ($event.config.uiStyles.attributes.postEventOnChange) {
                this.pageService.postOnChange($event.path, 'state', JSON.stringify($event.leafState));
            } else if($event.config.uiStyles.attributes.postButtonUrl) {
                let item: GenericDomain = new GenericDomain();
                this.pageService.processEvent(control.element.config.uiStyles.attributes.postButtonUrl, null, $event.leafState, HttpMethod.POST.value);
            }
        });
    }

    private unSubscribeAll() {
        this.controlValueChanged.unsubscribe();
        this.pageService.validationUpdate.unsubscribe();
        this.pageService.eventUpdate.unsubscribe();
    }

}
