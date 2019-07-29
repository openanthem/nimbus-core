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

import { ChangeDetectorRef, Component, Input } from '@angular/core';
import {
  AbstractControlDirective,
  FormGroup,
  NgModel,
  ValidationErrors
} from '@angular/forms';
import { AbstractControl } from '@angular/forms/src/model';
import { Message } from '../../shared/message';
import {
  ComponentTypes,
  ViewComponent
} from '../../shared/param-annotations.enum';
import { CounterMessageService } from './../../services/counter-message.service';
import { Constraint } from './../../shared/param-config';
import { ConstraintMapping } from './../../shared/validationconstraints.enum';
import { BaseElement } from './base-element.component';
import { ParamUtils } from '../../shared/param-utils';

var counter = 0;

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-element',
  templateUrl: './form-element.component.html'
})
export class FormElement extends BaseElement {
  @Input() form: FormGroup;
  elemMessages: Message[];
  id: String = 'form-control' + counter++;
  componentStyle: string;
  componentTypes = ComponentTypes;
  viewComponent = ViewComponent;
  componentClass: string[] = ['form-group'];
  errorStyles = 'alert alert-danger';

  get isValid() {
    if (this.form.controls[this.element.config.code] != null) {
      return this.form.controls[this.element.config.code].valid;
    } else {
      return true;
    }
  }

  get isPristine() {
    return this.getPristine();
  }
  getPristine() {
    if (this.element.config.code.startsWith('{')) {
      return true;
    }
    if (this.form.controls[this.element.config.code] != null) {
      return this.form.controls[this.element.config.code].pristine;
    } else {
      return true;
    }
  }

  getMessages() {
    this.elemMessages = [];
    if (this.element.message != null && this.element.message.length > 0) {
      this.elemMessages.push.apply(this.elemMessages, this.element.message);
    }
    if (!this.getPristine()) {
      this.updateErrorMessages();
    }
    return this.elemMessages;
  }

  get showMessages() {
    return this.elemMessages != null && this.elemMessages.length > 0;
  }

  constructor(
    private cms: CounterMessageService,
    private cd: ChangeDetectorRef
  ) {
    super();
  }

  getErrorStyles() {
    if (this.showErrors) {
      return this.errorStyles;
    } else {
      return '';
    }
  }

  get showErrors() {
    return !this.isPristine && !this.isValid;
  }

  ngOnInit() {
    super.ngOnInit();
  }

  ngAfterViewInit() {
    this.getMessages();
    this.cd.detectChanges();
    this.subscribers.push(
      this.cms.formErrorMessages$.subscribe(eventParam => {
        if (
          eventParam.path == this.element.path ||
          (eventParam.config.uiStyles != null &&
            eventParam.config.uiStyles.attributes.style ===
              this.componentTypes.validation.toString())
        ) {
          this.getMessages();
        }
      })
    );
  }

  getElementStyle() {
    if (
      this.element.config.uiStyles != null &&
      this.element.config.uiStyles.attributes.alias === 'MultiSelectCard'
    ) {
      return 'col-lg-12 col-md-6';
    } else {
      return '';
    }
  }

  /**
   * <p>Update all form controls belonging to this form instance that have validation errors present.
   * <p>Error messages are first attempted to be set by setting the message that is defined in the server-side
   * Constraint annotation's message attribute. If a message is not provided, the default message as defined
   * by ValidationUtils will be used.
   */
  updateErrorMessages() {
    var control: AbstractControl = this.form.controls[this.element.config.code];
    // TODO needs to be removed when refactoring @PickList
    // start REMOVE_ME
    if (control instanceof FormGroup) {
      control = control.controls[Object.keys(control.controls)[0]]
    }
    // end REMOVE_ME
    if (control.invalid) {
      let errs: ValidationErrors = control.errors;
      for (var key in errs) {
        let constraintName = ConstraintMapping.getConstraintValue(key);
        // TODO needs to be removed/refactored when refactoring @PickList
        // start REFACTOR_ME
        let constraintParam = !ParamUtils.isNested(this.element) ? this.element : this.element.type.model.params[0];
        let constraint: Constraint = constraintParam.config.validation.constraints.find(v => v.name == constraintName);
        // end REFACTOR_ME
        this.addErrorMessages(constraint.attribute.message);
        const index = this.componentClass.indexOf(this.errorStyles, 0);
        if (index < 0) {
          this.componentClass.push(this.getErrorStyles());
        }
      }
    } else {
      const index = this.componentClass.indexOf(this.errorStyles, 0);
      if (index >= 0) {
        this.componentClass.splice(index);
      }
    }
  }

  addErrorMessages(errorText: string) {
    let errorMessage: Message, summary: string;
    errorMessage = new Message();
    errorMessage.context = 'INLINE';
    // errorMessage.life = 10000;
    errorMessage.messageArray.push({
      severity: 'error',
      summary: summary,
      detail: errorText,
      life: 10000
    });
    this.elemMessages.push(errorMessage);
  }

  ngModelState(ngm: AbstractControlDirective): string {
    let ret = ngm instanceof NgModel ? 'name: ${ngm.name};  ' : '';
    return (
      ret +
      'touched: ${ngm.touched};  pristine: ${ngm.pristine};  valid: ${ngm.valid};  errors: ${JSON.stringify(ngm.errors)};  value: ${JSON.stringify(ngm.value)}'
    );
  }
}
