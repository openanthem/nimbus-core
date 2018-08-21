import { ValidationUtils } from './../../validators/ValidationUtils';
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
import { Param, Values } from '../../../../shared/param-state';
import { Component, forwardRef, Input, OnInit, ViewChild, Output, EventEmitter } from '@angular/core';
import { FormGroup, NG_VALUE_ACCESSOR, ValidatorFn } from '@angular/forms';

import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { PickList } from 'primeng/primeng';
import { BaseElement } from '../../base-element.component';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => OrderablePickList),
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
    selector: 'nm-pickList',
    providers: [
        CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR,WebContentSvc
    ],
    template: `
        <!--<div [hidden]="!element?.visible" *ngIf="element.config?.uiStyles?.attributes?.hidden==false">-->
        <label [attr.for]="element.config?.code"  [ngClass]="{'required': requiredCss, '': !requiredCss}">{{label}}
            <nm-tooltip *ngIf="helpText" [helpText]='helpText'></nm-tooltip>
        </label>
        <div>
            <fieldset [disabled]="!element?.enabled">
                <p-pickList #picklist [source]="element.values" 
                    filterBy="label"
                    [sourceHeader] = "element.config?.uiStyles?.attributes.sourceHeader" 
                    [targetHeader]="element.config?.uiStyles?.attributes.targetHeader" 
                    [disabled]="!element?.enabled"
                    [target]="targetList" pDroppable="dd" [responsive]="true" 
                    [showSourceControls]="element.config?.uiStyles?.attributes?.showSourceControls"
                    [showTargetControls]="element.config?.uiStyles?.attributes?.showTargetControls"
                    (onMoveToTarget)="updateListValues($event)" (onMoveToSource)="updateListValues($event)">
                    <ng-template let-itm pTemplate="item">
                        <div class="ui-helper-clearfix">
                            <div style="font-size:14px;float:right;margin:15px 5px 0 0" pDraggable="dd"  
                                (onDragStart)="dragStart($event, itm)" (onDragEnd)="dragEnd($event)">{{itm.label}} </div>
                        </div>
                    </ng-template>
                </p-pickList> 
             </fieldset>   
        </div>
   `
})

export class OrderablePickList extends BaseElement implements OnInit, ControlValueAccessor {

    @Input() element: Param;
    @Input() form: FormGroup;
    @Input('value') _value ;
    @ViewChild('picklist') pickListControl: PickList;
    targetList: any[];
    private draggedItm: any;
    private selectedOptions: string[] = [];
    private _disabled: boolean;
    public onChange: any = (_) => { /*Empty*/ }
    public onTouched: any = () => { /*Empty*/ }
    @Output() controlValueChanged =new EventEmitter();

    @Input()
    get disabled(): boolean { return this._disabled; }

    set disabled(value) { this._disabled = value; }

    constructor(wcs: WebContentSvc, private pageService: PageService) {
        super(wcs);
    }

    ngOnInit() {
        this.loadLabelConfigByCode(this.element.config.code, this.element.config.labelConfigs);
        this.requiredCss = ValidationUtils.applyelementStyle(this.element);
        //set the default target list when the page loads to the config state
        if(this.element.leafState !=null) {
            this.targetList = this.element.leafState;
        } else {
            this.targetList = [];
        }

        if( this.form!= null) {
            let frmCtrl = this.form.controls[this.element.config.code]
            if(frmCtrl != null) {
                //rebind the validations as there are dynamic validations along with the static validations
                if(this.element.activeValidationGroups != null && this.element.activeValidationGroups.length > 0) {
                    this.requiredCss = ValidationUtils.rebindValidations(frmCtrl,this.element.activeValidationGroups,this.element);
                } 
                frmCtrl.valueChanges.subscribe(
                    ($event) => { this.setState($event,this); });

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
                this.controlValueChanged.subscribe(($event) => {
                    if ($event.config.uiStyles.attributes.postEventOnChange) {
                       this.pageService.postOnChange($event.path, 'state', JSON.stringify($event.leafState));
                    }
                });
            }
            
        }
    }

    emitValueChangedEvent() {
        if(this.form == null || (this.form.controls[this.element.config.code]!= null && this.form.controls[this.element.config.code].valid)) {
            this.controlValueChanged.emit(this.element);
        }
        
    }

    setState(event:any, frmInp:any) {
        frmInp.element.leafState = event;
    }

    updateListValues(event: any) {
        if(this.targetList.length === 0) {
            this.value = null;
        } else {
            this.selectedOptions = [];
            this.targetList.forEach(element => {
                this.selectedOptions.push(element.code);
            });
            this.value = this.selectedOptions;
        }
        this.emitValueChangedEvent();
    }

    get value() {
        return this._value;
    }

    set value(val) {
        this._value = val;
        this.onChange(val);
        this.onTouched();
    }

    dragStart(event, itm: any) {
        if(this.element.enabled) {
            this.draggedItm = itm;
        }
    }


    findIndexInList(item: Values, list: Values[]): number {
        let index: number = -1;
        if(list) {
            for(let i = 0; i < list.length; i++) {
                if(list[i].code === item.code) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    dragEnd(event) {
        if (this.draggedItm) {
            let index = this.findIndexInList(this.draggedItm,this.pickListControl.source);
            if(index >=0) {
                this.pickListControl.source.splice(index, 1);
                this.pickListControl.target.push(this.draggedItm);
            } else {
                index = this.findIndexInList(this.draggedItm,this.pickListControl.target);
                if(index >=0) {
                    this.pickListControl.target.splice(index, 1);
                    this.pickListControl.source.push(this.draggedItm);
                }
            }
            this.draggedItm = null;
            //updating the internal data model
            if(this.targetList.length === 0) {
                this.value = null;
            } else {
                this.selectedOptions = [];
                this.targetList.forEach(element => {
                    this.selectedOptions.push(element.code);
                });
                this.value = this.selectedOptions;
            }
        }
    }

    public writeValue(obj: any): void {
        if (obj !== undefined) {
        }
    }

    public registerOnChange(fn: any): void {
       this.onChange = fn;
    }

    public registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    public setDisabledState(isDisabled: boolean) {
        this.disabled = isDisabled;
  }

}
