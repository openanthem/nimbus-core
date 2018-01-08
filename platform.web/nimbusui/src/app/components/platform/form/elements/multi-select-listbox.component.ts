/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { Param } from '../../../../shared/app-config.interface';
import { Component, forwardRef, Input,Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { FormGroup, NgModel} from '@angular/forms';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import {SelectItem} from 'primeng/primeng';
import { GenericDomain } from '../../../../model/generic-domain.model';

@Component({
    selector: 'nm-multi-select-listbox',
    providers: [
        WebContentSvc
    ],
    template: `
        <div [formGroup]="form"  [hidden]="!element?.visible?.currState">
            <label class="">{{label}} 
                <nm-tooltip *ngIf="element.config?.uiStyles?.attributes?.help!=''" [helpText]='element.config?.uiStyles?.attributes?.help'></nm-tooltip>
            </label>
            <p-listbox [options]="optionsList" formControlName="{{element.config.code}}" multiple="multiple" 
            (onChange)="emitValueChangedEvent(this,value)" checkbox="checkbox" filter="filter" [style]="{'width':'190px','max-height':'250px'}"></p-listbox>
        </div>
   `
})

export class MultiSelectListBox {

    @Input() element: Param;
    @Input() form: FormGroup;
    @Output() controlValueChanged =new EventEmitter();
    value = [];
    public label: string;
    optionsList: SelectItem[];
    private targetList: any[];

    constructor(private wcs: WebContentSvc, private pageService: PageService,private cd: ChangeDetectorRef) {
        wcs.content$.subscribe(result => {
            this.label = result.label;
        });
    }

    ngOnInit() {
        this.optionsList = [];
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
        this.wcs.getContent(this.element.config.code);
        if( this.form.controls[this.element.config.code]!= null) {
            this.form.controls[this.element.config.code].valueChanges.subscribe(
                ($event) => { console.log($event);this.setState($event,this); });
        }
        this.controlValueChanged.subscribe(($event) => {
            //console.log($event);
            if ($event.config.uiStyles.attributes.postEventOnChange) {
               this.pageService.postOnChange($event.path, 'state', JSON.stringify($event.leafState));
            } else if($event.config.uiStyles.attributes.postButtonUrl) {
               let item: GenericDomain = new GenericDomain();
               //item.addAttribute($event.config.code,$event.leafState);
               this.pageService.processPost(this.element.config.uiStyles.attributes.postButtonUrl, null, $event.leafState, 'POST');
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
            if(frmCtrl!=null && event.path.startsWith(this.element.path)) {
                if(event.enabled.currState)
                    frmCtrl.enable();
                else
                    frmCtrl.disable();
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
