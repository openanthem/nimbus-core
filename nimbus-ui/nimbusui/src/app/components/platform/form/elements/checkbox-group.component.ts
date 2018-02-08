'use strict';
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { Component, Input, Output, EventEmitter,forwardRef, ChangeDetectorRef } from '@angular/core';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Param } from '../../../../shared/app-config.interface';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { ServiceConstants } from '../../../../services/service.constants';
import { BaseElement } from './../../base-element.component';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => CheckBoxGroup),
  multi: true
};

@Component({
  selector: 'nm-input-checkbox',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR,WebContentSvc ],
  template: `
      <fieldset>
          <legend class="{{elementStyle}}">{{label}}
                <nm-tooltip *ngIf="helpText" [helpText]='helpText'></nm-tooltip>
           </legend>
          <div class="checkboxHolder" [formGroup]="form" >
            <div class="form-checkrow" *ngFor="let val of element?.values; let i = index">
                <p-checkbox name="{{element?.config?.code}}" [formControlName]="element.config?.code" [value]="val.code" [label]="val.label" (onChange)="emitValueChangedEvent(this,$event)"></p-checkbox>
            </div>
          </div>
    </fieldset>
   `
})

export class CheckBoxGroup extends BaseElement implements ControlValueAccessor {

    @Input() element: Param;
    @Input() form: FormGroup;
    @Input('value') _value;
    @Output() antmControlValueChanged =new EventEmitter();
    
    // TODO replace selected options with element.leafState once it supports collection
    //private selectedOptions: string[] = [];

    constructor(private pageService: PageService, private _wcs: WebContentSvc, private cd: ChangeDetectorRef) {
        super(_wcs);    
    }

    public onChange: any = (_) => { /*Empty*/ }
    public onTouched: any = () => { /*Empty*/ }

    get value() {
        return this._value;
    }

    set value(val) {
        this._value = val;
        this.onChange(val);
        this.onTouched();
    }

    registerOnChange(fn) {
        this.onChange = fn;
    }

    writeValue(value) {
        if(value) {
        }
    }

    registerOnTouched(fn) {
        this.onTouched = fn;
    }

    setState(event:any,frmInp:any) {
        frmInp.element.leafState = event;
        this.cd.markForCheck();
        //added to overide the state with ws update.
        //this.selectedOptions = [];
        //this.selectedOptions.push(event);
        //console.log(frmInp.element.leafState);
    }

    emitValueChangedEvent(formControl:any,$event:any) {
        this.antmControlValueChanged.emit(formControl.element);
    }

    // selectOption(code: string, elem: any) {
    //     var array = this.value;
    //     var index = array.indexOf(code);
    //     if (index > -1) {
    //         array.splice(index, 1);
    //     } else {
    //         array.push(code);
    //     }
    //     this.value = this.value;
    // }

    // checkedState(val:any) {
    //     var array = this.value;
    //     var index = array.indexOf(val);
    //     if(index> -1) {
    //         return true;
    //     } else {
    //         return false;
    //     }
    // }
    ngOnInit() {
        super.ngOnInit();
        if(this.element.leafState !=null && this.element.leafState.length > 0) {
            this.value = this.element.leafState;
        }
        if( this.form.controls[this.element.config.code]!= null) {
            this.form.controls[this.element.config.code].valueChanges.subscribe(($event) => this.setState($event,this));
            
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
        this.antmControlValueChanged.subscribe(($event) => {
             //console.log($event);
             if ($event.config.uiStyles.attributes.postEventOnChange) {
                this.pageService.postOnChange($event.path, 'state', JSON.stringify($event.leafState));
             }
         });
    }
}