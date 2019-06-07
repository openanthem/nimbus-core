/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  forwardRef,
  Input,
  Output
} from '@angular/core';
import { AbstractControl, FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { ValidatorFn } from '@angular/forms/src/directives/validators';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/param-state';
import { ValidationUtils } from '../../validators/ValidationUtils';
import { CounterMessageService } from './../../../../services/counter-message.service';
import { BaseElement } from './../../base-element.component';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => CheckBoxGroup),
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
  selector: 'nm-input-checkbox',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
  template: `
    <fieldset>
      <div class="fieldsetFlex">
        <nm-input-legend
          [element]="element"
          [required]="requiredCss"
        ></nm-input-legend>
        <div class="checkboxHolder" [formGroup]="form">
          <div
            class="form-checkrow"
            *ngFor="let val of element?.values; let i = index"
          >
            <p-checkbox
              name="{{ element?.config?.code }}"
              [formControl]="form.controls[(element?.config?.code)]"
              [value]="val.code"
              [label]="val.label"
              (onChange)="emitValueChangedEvent(this,$event)"
            ></p-checkbox>
          </div>
        </div>
      </div>
    </fieldset>
  `
})
export class CheckBoxGroup extends BaseElement implements ControlValueAccessor {
  @Input() element: Param;
  @Input() form: FormGroup;
  @Input('value') _value;
  @Output() controlValueChanged = new EventEmitter();
  sendEvent: boolean = true;

  constructor(
    private pageService: PageService,
    private cd: ChangeDetectorRef,
    private counterMessageService: CounterMessageService
  ) {
    super();
  }

  public onChange: any = _ => {
    /*Empty*/
  };
  public onTouched: any = () => {
    /*Empty*/
  };

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
    }
  }

  registerOnTouched(fn) {
    this.onTouched = fn;
  }

  setState(event: any, frmInp: any) {
    frmInp.element.leafState = event;
    this.cd.markForCheck();
  }

  emitValueChangedEvent(formControl: any, $event: any) {
    let frmCtrl: AbstractControl;
    if (this.form) {
      frmCtrl = this.form.controls[this.element.config.code];
      if (frmCtrl.valid && this.sendEvent) {
        this.counterMessageService.evalCounterMessage(true);
        this.counterMessageService.evalFormParamMessages(this.element);
        this.sendEvent = false;
      } else if (frmCtrl.invalid && !frmCtrl.pristine) {
        this.counterMessageService.evalFormParamMessages(this.element);
        this.sendEvent = true;
        this.counterMessageService.evalCounterMessage(true);
      }
    }
    if (
      this.form == null ||
      (this.form.controls[this.element.config.code] != null &&
        this.form.controls[this.element.config.code].valid)
    ) {
      this.controlValueChanged.emit(formControl.element);
    }
  }

  ngOnInit() {
    super.ngOnInit();
    let frmCtrl = this.form.controls[this.element.config.code];
    //rebind the validations as there are dynamic validations along with the static validations
    if (
      frmCtrl != null &&
      this.element.activeValidationGroups != null &&
      this.element.activeValidationGroups.length > 0
    ) {
      this.requiredCss = ValidationUtils.rebindValidations(
        frmCtrl,
        this.element.activeValidationGroups,
        this.element
      );
    }
    if (this.element.leafState != null && this.element.leafState.length > 0) {
      this.value = this.element.leafState;
    }
    if (this.form.controls[this.element.config.code] != null) {
      this.subscribers.push(
        this.form.controls[this.element.config.code].valueChanges.subscribe(
          $event => this.setState($event, this)
        )
      );

      this.subscribers.push(
        this.pageService.eventUpdate$.subscribe(event => {
          let frmCtrl = this.form.controls[event.config.code];
          if (frmCtrl != null && event.path.startsWith(this.element.path)) {
            if (event.leafState != null) frmCtrl.setValue(event.leafState);
            else frmCtrl.reset();
          }
        })
      );
      this.subscribers.push(
        this.pageService.validationUpdate$.subscribe(event => {
          let frmCtrl = this.form.controls[event.config.code];
          if (frmCtrl != null) {
            if (event.path === this.element.path) {
              //bind dynamic validations on a param as a result of a state change of another param
              if (
                event.activeValidationGroups != null &&
                event.activeValidationGroups.length > 0
              ) {
                this.requiredCss = ValidationUtils.rebindValidations(
                  frmCtrl,
                  event.activeValidationGroups,
                  this.element
                );
              } else {
                this.requiredCss = ValidationUtils.applyelementStyle(
                  this.element
                );
                var staticChecks: ValidatorFn[] = [];
                staticChecks = ValidationUtils.buildStaticValidations(
                  this.element
                );
                frmCtrl.setValidators(staticChecks);
              }
              ValidationUtils.assessControlValidation(event, frmCtrl);
            }
          }
        })
      );
    }
    this.subscribers.push(
      this.controlValueChanged.subscribe($event => {
        if ($event.config.uiStyles.attributes.postEventOnChange) {
          this.pageService.postOnChange(
            $event.path,
            'state',
            JSON.stringify($event.leafState)
          );
        }
      })
    );
  }
}
