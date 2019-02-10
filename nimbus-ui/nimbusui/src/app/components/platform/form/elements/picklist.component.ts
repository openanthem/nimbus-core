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
import { MAX_LENGTH_VALIDATOR } from '@angular/forms/src/directives/validators';
import { BaseControl } from './base-control.component';
import { GenericDomain } from '../../../../model/generic-domain.model';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => OrderablePickList),
  multi: true
};

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@author Swetha.Vemuri
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
        <nm-input-label
            [element]="element" 
            [for]="parent.config.code"
            [required]="requiredCss">
        </nm-input-label>
        <div style="padding:2px;"> 
        <fieldset [disabled]="!parent?.enabled">
            <p-pickList #picklist 
                [source]="sourcelist" 
                filterBy="label"
                [sourceHeader] = "parent?.config?.uiStyles?.attributes.sourceHeader" 
                [targetHeader]="parent?.config?.uiStyles?.attributes.targetHeader" 
                [disabled]="!parent.enabled"
                [target]="targetList" 
                pDroppable="dd" 
                [responsive]="true" 
                [showSourceControls]="false"
                [showTargetControls]="false"
                (onMoveToTarget)="updateListValues($event)" 
                (onMoveToSource)="updateListValues($event)">
                <ng-template let-itm pTemplate="item">
                    <div class="ui-helper-clearfix">
                        <div pDraggable="dd"  
                            (onDragStart)="dragStart($event, itm)" (onDragEnd)="dragEnd($event)">
                            {{itm.label ? itm.label : getDesc(itm)}}
                        </div>
                    </div>
                </ng-template>
            </p-pickList>   
            </fieldset>   
        </div>
   `
})

export class OrderablePickList extends BaseElement implements OnInit, ControlValueAccessor {
    @Input() parent: Param;
    sourcelist: any[]; 
    @Input() selectedvalues : Values[];
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

    constructor(private _wcs: WebContentSvc, private pageService: PageService) {
        super(_wcs);
    }

    ngOnInit() {
        this.loadLabelConfigFromConfigs(this.parent.labels, this.parent.config.code);
        this.requiredCss = ValidationUtils.applyelementStyle(this.parent);
        this.sourcelist = [];
        if (this.parent.values) {
            this.parent.values.forEach(value => {
                this.sourcelist.push(value);
            });
        }
        
        // First check if the picklist has any values that are selected onload
        if (this.element.leafState != null && this.element.leafState.length > 0) {
            this.targetList = this.element.leafState;
        } else {
            this.targetList = [];
        }
        this.refreshSourceList();
        if (this.form != null) {
            const parentCtrl = this.form.controls[this.parent.config.code];
            const frmCtrl = this.form.controls[this.element.config.code];
            if (frmCtrl != null) {
                //rebind the validations as there are dynamic validations along with the static validations
                if (this.element.activeValidationGroups != null && this.element.activeValidationGroups.length > 0) {
                    this.requiredCss = ValidationUtils.rebindValidations(frmCtrl,this.element.activeValidationGroups, this.element);
                } 
                frmCtrl.valueChanges.subscribe(
                    ($event) => { 
                        this.setState($event, this);
                        // setting parent Picklist value manually since 
                        parentCtrl.setValue(this.updateParentValue($event));
                    });

                this.pageService.eventUpdate$.subscribe(event => {
                const frmCtrl = this.form.controls[this.element.config.code];
                    if(frmCtrl != null && event.path.startsWith(this.element.path)) {
                        if(event.leafState != null) {
                            frmCtrl.setValue(event.leafState);
                        } else {
                            frmCtrl.reset();
                        }
                    }
                });
                this.pageService.validationUpdate$.subscribe(event => {
                const frmCtrl = this.form.controls[this.element.config.code];
                    if(frmCtrl != null) {
                        if (event.path === this.element.path) {
                           //bind dynamic validations on a param as a result of a state change of another param
                            if (event.activeValidationGroups != null && event.activeValidationGroups.length > 0) {
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
                        this.pageService.postOnChange($event.path , 'state', JSON.stringify($event.leafState));
                     }
                 });
            }
        }

        this.pageService.eventUpdate$.subscribe(event => {
            if(event.path == this.parent.path) {
                this.refreshSourceList();
            }
        });
    }

    emitValueChangedEvent() {        
        if (this.form == null || (this.form.controls[this.element.config.code]!= null 
            && this.form.controls[this.element.config.code].valid)) {
            this.controlValueChanged.emit(this.element);
        }
    }

    setState(event:any, frmInp:any) {
        frmInp.element.leafState = event;
    }

    updateListValues(event: any) {
        this.updateData();
        /* comes into this loop when leafState is not null and new values are added
         prime-ng adds data of type Values{code, label} to leafState since the [source] is of type {element.values} 
         ex: leafState : ["Mon","Tue",{'code' : "Wed", 'label' : "Wednesday"}]
         The below logic, transforms the data into just codes i.e leafState : ["Mon","Tue","Wed"] 
        */
        if (this.element.leafState) {
            this.element.leafState.forEach((state, i) => {
                if (state && state.code) {
                    const code = state.code;
                    this.element.leafState.splice(i, 1, code);
                    this.targetList = this.element.leafState;
                }
            });
        } else {
            if (this.value) {
                this.element.leafState = this.value;
            }
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
            this.updateData();
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

    /** 
    * @param - itm (represents a code of @Values) 
    * The method maps the code to a label based on the super set of @Values 
    * passed as @Input("selectedValues") to the component.
    * This particularly comes into picture when the component is rendered in edit mode with pre loaded values
    */
    public getDesc(itm : any) : string {
        let displayVal: string;
        const values = this.selectedvalues;
        displayVal = values.find(value => (value && value.code === itm)).label;
        if (displayVal === undefined) {
            displayVal = itm;
        }
        return displayVal;
    }

    /**
     * This method validates if there are duplicate values in both source and target. 
     * If yes, remove them from source and refresh the source list.
     */
    private refreshSourceList() {
        // make sure targetlist and leafstate are in sync
        if (this.element.leafState !== undefined &&
            this.element.leafState !== null && this.element.leafState.length > 0) {

            this.targetList = this.element.leafState;
        } else {
            this.targetList = [];
        }

        if (this.targetList && this.targetList.length > 0) {
            if (this.parent.values != null && this.parent.values.length > 0) {
                this.sourcelist = this.parent.values.filter(value => 
                    this.targetList.indexOf(value.code) < 0
                );
            }
        } else {
            this.sourcelist = [];
            this.parent.values.forEach(value => {
                this.sourcelist.push(value);
            });
        }
    }

    /**
     * Update the internal model.
     */
    private updateData() {
        if(this.targetList.length === 0) {
            this.value = null;
        } else {
            this.selectedOptions = [];
            this.targetList.forEach(element => {
                if (element.code) {
                    this.selectedOptions.push(element.code);
                } else {
                    this.selectedOptions.push(element);
                }
            });
            this.value = this.selectedOptions;
        }
    }

    /**
     * This is a temp hack to resolve the complex type form submit issues
     */
    private updateParentValue(event : any) : GenericDomain{
        let item: GenericDomain = new GenericDomain();
        let selectedOptions = [];
            this.targetList.forEach(element => {
                if (element.code) {
                    selectedOptions.push(element.code);
                } else {
                    selectedOptions.push(element);
                }
            });
        item.addAttribute(this.element.config.code, selectedOptions);
        return item;
    }
}
