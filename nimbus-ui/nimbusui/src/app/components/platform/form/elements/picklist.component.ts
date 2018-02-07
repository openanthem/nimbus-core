import { Values } from './../../../../shared/app-config.interface';
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { Param } from '../../../../shared/app-config.interface';
import { Component, forwardRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';

import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { PickList } from 'primeng/primeng';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => OrderablePickList),
  multi: true
};

@Component({
    selector: 'nm-pickList',
    providers: [
        CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR,WebContentSvc
    ],
    template: `
        <!--<div [hidden]="!element?.config?.visible?.currState || !element?.visible?.currState" *ngIf="element.config?.uiStyles?.attributes?.hidden==false">-->
        <div [hidden]="!element?.visible?.currState" *ngIf="element.config?.uiStyles?.attributes?.hidden==false">
            <fieldset [disabled]="!element.config?.enabled?.currState">
                <p-pickList #picklist [source]="element.values" 
                    [sourceHeader] = "element.config?.uiStyles?.attributes.sourceHeader" 
                    [targetHeader]="element.config?.uiStyles?.attributes.targetHeader" 
                    [disabled]="!element?.enabled?.currState"
                    [target]="targetList" pDroppable="dd" [responsive]="true" 
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

export class OrderablePickList implements OnInit, ControlValueAccessor {

    @Input() element: Param;
    @Input() form: FormGroup;
    @Input('value') _value ;
    @ViewChild('picklist') pickListControl: PickList;
    private targetList: any[];
    private draggedItm: any;
    private selectedOptions: string[] = [];
    private _disabled: boolean;
    public onChange: any = (_) => { /*Empty*/ }
    public onTouched: any = () => { /*Empty*/ }

    @Input()
    get disabled(): boolean { return this._disabled; }

    set disabled(value) { this._disabled = value; }

    constructor(wcs: WebContentSvc, private pageService: PageService) {
    }

    ngOnInit() {
        //set the default target list when the page loads to the config state
        if(this.element.leafState !=null) {
            this.targetList = this.element.leafState;
        } else {
            this.targetList = [];
        }

        if( this.form!= null && this.form.controls[this.element.config.code]!= null) {
            this.form.controls[this.element.config.code].valueChanges.subscribe(
                ($event) => { this.setState($event,this); });

            this.pageService.eventUpdate$.subscribe(event => {
                let frmCtrl = this.form.controls[event.config.code];
                if(frmCtrl!=null && event.path.startsWith(this.element.path)) {
                    frmCtrl.setValue(event.leafState);
                }
            });
        }
    }

    setState(event:any, frmInp:any) {
        frmInp.element.leafState = event;
        //console.log(frmInp.element.leafState);
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
        if(this.element.config.enabled.currState) {
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
            //console.log(this.targetList);
            if(this.targetList.length === 0) {
                this.value = null;
            } else {
                this.selectedOptions = [];
                this.targetList.forEach(element => {
                    this.selectedOptions.push(element.code);
                });
                this.value = this.selectedOptions;
                //this.value = this.targetList;
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
