import { Component, Input, forwardRef } from '@angular/core';
import { FormGroup, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/app-config.interface';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseElement } from './../../base-element.component';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => MultiselectCard),
  multi: true
};

@Component({
    selector: 'nm-multiselect-card',
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR,WebContentSvc ],
    template: `
    <div class="col-lg-12">
        <div *ngFor="let value of element.values" class="col-sm-6 pb-2"> 
            <a class="checkBoxCard" [ngClass]="{checked: this.selectedOptions.indexOf(value.code) > -1}"  (click)="selectOption(value.code, this)"> 
                <div class="upper">
                    <span class="align-middle">{{value.code}}</span>
                </div> 
                <div class="lower">
                    <span class="align-middle">{{value.label}}</span>
                </div> 
                <div class="box"> <div class="check">✓</div> 
                </div> 
            </a> 
        </div>
    </div>
   `
})
export class MultiselectCard  extends BaseElement implements ControlValueAccessor {
    @Input() element: Param;
    @Input() form: FormGroup;
    @Input('value') _value;

    private selectedOptions: string[] = [];

    constructor(private _wcs: WebContentSvc, private pageService: PageService) {
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
        if (value) {
        this.value = value;
        }
    }

    registerOnTouched(fn) {
        this.onTouched = fn;
    }

    toggleChecked(code: string) {
        if (this.selectedOptions.indexOf(code) > -1) {
            return true;
        } else {
            return false;
        }
    }

    selectOption(code: string, elem: any) {
        var array = this.selectedOptions;
        var index = array.indexOf(code);
        if (index > -1) {
            array.splice(index, 1);
        } else {
            array.push(code);
        }
        this.value = this.selectedOptions;
    }

    setState(event:any) {
            this.element.leafState = event;
            if (this.element.config.uiStyles.attributes.postEventOnChange) {
                this.pageService.postOnChange(this.element.path, 'state', JSON.stringify(this.element.leafState));
            }
    }

    ngOnInit() {
        super.ngOnInit();
        //to set the state of the control with db value on page load. TODO - remove the selectedoptions variable and work with element.leafState
        if(this.element.leafState !=null) {
            this.selectedOptions = this.element.leafState;
        }
        if(this.form!=null && this.form.controls[this.element.config.code]!= null) {
            this.form.controls[this.element.config.code].valueChanges.subscribe(($event) => this.setState($event));
            
            this.pageService.eventUpdate$.subscribe(event => {
                let frmCtrl = this.form.controls[event.config.code];
                if(frmCtrl!=null && event.path.startsWith(this.element.path)) {
                    frmCtrl.setValue(event.leafState);
                }
            });
        }
        //this.form.controls[this.element.config.code].statusChanges.subscribe(($event) => this.setStatus($event));
    }

}
