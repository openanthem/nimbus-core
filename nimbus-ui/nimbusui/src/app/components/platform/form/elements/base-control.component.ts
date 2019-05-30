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

import { ChangeDetectorRef, Input } from '@angular/core';
import { FormGroup, NgModel } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Param } from '../../../../shared/param-state';
import { ValidationUtils } from '../../validators/ValidationUtils';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';
import { CounterMessageService } from './../../../../services/counter-message.service';
import { Constraint } from './../../../../shared/param-config';
import { ParamUtils } from './../../../../shared/param-utils';
import { BaseControlValueAccessor } from './control-value-accessor.component';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
export abstract class BaseControl<T> extends BaseControlValueAccessor<T> {
  @Input() element: Param;
  @Input() form: FormGroup;
  @Input() hideLabel: boolean;

  protected abstract model: NgModel;
  protected _elementStyle: string;
  inPlaceEditContext: any;
  showLabel: boolean = true;
  disabled: boolean;
  requiredCss: boolean = false;

  stateChangeSubscriber: Subscription;
  validationChangeSubscriber: Subscription;
  onChangeSubscriber: Subscription;

  private subscribers: Subscription[] = [];

  sendEvent = true;

  constructor(
    protected controlService: ControlSubscribers,
    private cd: ChangeDetectorRef,
    private counterMessageService: CounterMessageService
  ) {
    super();
  }

  setState(val: any, frmInp: any) {
    frmInp.element.leafState = val;
    this.cd.markForCheck();
    if (val == null) {
      //if the val is null - the form is set for the first time or it is being reset or user clearing the value (date component)
      this.controlService.resetPreviousLeafState(val);
    }
  }

  emitValueChangedEvent(formControl: any, $event: any) {
    if (this.inPlaceEditContext) {
      this.inPlaceEditContext.value = formControl.value;
    }
    if (
      this.form == null ||
      (this.form.controls[this.element.config.code] != null &&
        this.form.controls[this.element.config.code].valid)
    ) {
      this.controlService.controlValueChanged.emit(formControl.element);
    }
  }

  ngOnInit() {
    this.value = this.element.leafState;
    this.disabled = !this.element.enabled;
    this.requiredCss = ValidationUtils.applyelementStyle(this.element);
    if (this.form) {
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
    }
  }

  ngOnDestroy() {
    if (this.subscribers && this.subscribers.length > 0) {
      this.subscribers.forEach(s => s.unsubscribe());
    }
    delete this.element;
  }

  ngAfterViewInit() {
    if (
      this.form != undefined &&
      this.form.controls[this.element.config.code] != null
    ) {
      let frmCtrl = this.form.controls[this.element.config.code];
      this.subscribers.push(
        frmCtrl.valueChanges.subscribe($event => {
          this.setState($event, this);
          if (frmCtrl.valid && this.sendEvent) {
            this.counterMessageService.evalCounterMessage(true);
            this.counterMessageService.evalFormParamMessages(this.element);
            this.sendEvent = false;
          } else if (frmCtrl.invalid && !frmCtrl.pristine) {
            this.counterMessageService.evalFormParamMessages(this.element);
            this.counterMessageService.evalCounterMessage(true);
            this.sendEvent = true;
          }
        })
      );
      this.controlService.stateUpdateSubscriber(this);
      this.controlService.validationUpdateSubscriber(this);
    }
    this.controlService.onChangeEventSubscriber(this);
  }

  ngOndestroy() {
    if (this.subscribers && this.subscribers.length > 0) {
      this.subscribers.forEach(s => s.unsubscribe());
    }
  }
  /** invoked from InPlaceEdit control */
  setInPlaceEditContext(context: any) {
    this.showLabel = false;
    this.inPlaceEditContext = context;
  }
  /**
   * The hidden attribute for this param
   */
  public get hidden(): boolean {
    return this.element.config.uiStyles.attributes.hidden;
  }

  /**
   * The help attribute for this param
   */
  public get help(): string {
    return this.element.config.uiStyles.attributes.help;
  }

  /**
   * The help readOnly for this param
   */
  public get readOnly(): boolean {
    return this.element.config.uiStyles.attributes.readOnly;
  }

  /**
   * The type attribute for this param
   */
  public get type(): string {
    return this.element.config.uiStyles.attributes.type;
  }

  /**
   * Get the tooltip help text for this element.
   */
  public get helpText(): string {
    return ParamUtils.getHelpText(this.element);
  }

  /**
   * Get the label text for this element.
   */
  public get label(): string {
    return ParamUtils.getLabelText(this.element);
  }

  /**
   * Determine if the label for this element is empty or not.
   */
  public get isLabelEmpty(): boolean {
    if (this.label) {
      return this.label.trim().length === 0;
    }
    return true;
  }

  /**
   * Return constraint matches param attribute
   * @param name
   */
  public getConstraint(name: string): Constraint {
    if (this.element.config.validation) {
      if (!this.element.config.validation.constraints) {
        return;
      }
      let constraints = this.element.config.validation.constraints.filter(
        constraint => constraint.name === name
      );
      if (constraints.length >= 2) {
        throw new Error(
          'Constraint array list should not have more than one attribute ' +
            name
        );
      } else {
        return constraints[0];
      }
    } else {
      return;
    }
  }
}
