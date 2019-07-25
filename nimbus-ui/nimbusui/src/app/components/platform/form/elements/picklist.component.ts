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
  Component,
  EventEmitter,
  forwardRef,
  Input,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import { FormGroup, NG_VALUE_ACCESSOR, ValidatorFn } from '@angular/forms';
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { PickList } from 'primeng/primeng';
import { GenericDomain } from '../../../../model/generic-domain.model';
import { PageService } from '../../../../services/page.service';
import { Param, Values } from '../../../../shared/param-state';
import { BaseElement } from '../../base-element.component';
import { CounterMessageService } from './../../../../services/counter-message.service';
import { ValidationUtils } from './../../validators/ValidationUtils';

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
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
  template: `
    <nm-input-label
      [element]="element"
      [for]="parent.config.code"
      [required]="requiredCss"
    >
    </nm-input-label>
    <div style="padding:2px;">
      <fieldset [disabled]="!parent?.enabled">
        <p-pickList
          #picklist
          [source]="sourceList"
          filterBy="label"
          [sourceHeader]="parent?.config?.uiStyles?.attributes.sourceHeader"
          [targetHeader]="parent?.config?.uiStyles?.attributes.targetHeader"
          [disabled]="!parent.enabled"
          [target]="targetList"
          pDroppable="dd"
          [responsive]="true"
          [showSourceControls]="false"
          [showTargetControls]="false"
          (onMoveToTarget)="updateParamState($event)"
          (onMoveToSource)="updateParamState($event)"
        >
          <ng-template let-itm pTemplate="item">
            <div class="ui-helper-clearfix">
              <div pDraggable="dd">
                {{ itm.label ? itm.label : getDesc(itm) }}
              </div>
            </div>
          </ng-template>
        </p-pickList>
      </fieldset>
    </div>
  `
})
export class OrderablePickList extends BaseElement
  implements OnInit, ControlValueAccessor {
  @Input() parent: Param;
  sourceList: any[];
  @Input() selectedvalues: Values[];
  @Input() form: FormGroup;
  @ViewChild('picklist') pickListControl: PickList;
  targetList: any[];
  private draggedItm: any;
  private _disabled: boolean;
  public onChange: any = _ => {
    /*Empty*/
  };
  public onTouched: any = () => {
    /*Empty*/
  };
  sendEvent: boolean = true;
  @Output() controlValueChanged = new EventEmitter();

  @Input()
  get disabled(): boolean {
    return this._disabled;
  }

  set disabled(value) {
    this._disabled = value;
  }

  constructor(
    private pageService: PageService,
    private counterMessageService: CounterMessageService
  ) {
    super();
  }

  ngOnInit() {
    this.requiredCss = ValidationUtils.applyelementStyle(this.parent);
    this.refreshSourceList();
    if (this.form != null) {
      const parentCtrl = this.form.controls[this.parent.config.code];
      const frmCtrl = this.form.controls[this.element.config.code];
      if (frmCtrl != null) {
        //rebind the validations as there are dynamic validations along with the static validations
        if (
          this.element.activeValidationGroups != null &&
          this.element.activeValidationGroups.length > 0
        ) {
          this.requiredCss = ValidationUtils.rebindValidations(
            frmCtrl,
            this.element.activeValidationGroups,
            this.element
          );
        }
        frmCtrl.valueChanges.subscribe($event => {
          this.setState($event, this);
          // setting parent Picklist value manually since
          parentCtrl.setValue(this.updateParentValue($event));
          let frmCtrl = this.form.controls[$event.config.code];
          if (frmCtrl) {
            if (frmCtrl.valid && this.sendEvent) {
              this.counterMessageService.evalCounterMessage(true);
              this.counterMessageService.evalFormParamMessages(this.element);
              this.sendEvent = false;
            } else if (frmCtrl.invalid && !frmCtrl.pristine) {
              this.counterMessageService.evalFormParamMessages(this.element);
              this.counterMessageService.evalCounterMessage(true);
              this.sendEvent = true;
            }
          }
        });

        this.subscribers.push(
          this.pageService.eventUpdate$.subscribe(event => {
            const frmCtrl = this.form.controls[this.element.config.code];
            if (frmCtrl != null && event.path.startsWith(this.element.path)) {
              if (event.leafState != null) {
                frmCtrl.setValue(event.leafState);
              } else {
                frmCtrl.reset();
              }
            }
          })
        );
        this.pageService.validationUpdate$.subscribe(event => {
          const frmCtrl = this.form.controls[this.element.config.code];
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
        });
        this.controlValueChanged.subscribe($event => {
          if ($event.config.uiStyles.attributes.postEventOnChange) {
            this.pageService.postOnChange(
              $event.path,
              'state',
              JSON.stringify($event.leafState)
            );
          }
        });
      }
    }

    // Refresh the source list on param update
    this.subscribers.push(
      this.pageService.eventUpdate$.subscribe(event => {
        if (event.path == this.parent.path) {
          this.refreshSourceList();
        }
      })
    );
  }

  emitValueChangedEvent() {
    if (
      this.form == null ||
      (this.form.controls[this.element.config.code] != null &&
        this.form.controls[this.element.config.code].valid)
    ) {
      this.controlValueChanged.emit(this.element);
    }
  }

  setState(event: any, frmInp: any) {
    frmInp.element.leafState = event;
  }

  updateParamState(event: any) {
    let newState = null;
    if (this.targetList.length > 0) {
      newState = [];
      // targetList stores Values object, but leafState should be sent as string[]
      // the following converts Values to string[], using "code" as the value
      this.targetList.forEach(element => {
        newState.push(element.code);
      });
    }
    this.element.leafState = newState;
    this.emitValueChangedEvent();
  }

  findIndexInList(item: Values, list: Values[]): number {
    let index: number = -1;
    if (list) {
      for (let i = 0; i < list.length; i++) {
        if (list[i].code === item.code) {
          index = i;
          break;
        }
      }
    }
    return index;
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
    const values = this.selectedvalues;
    let val = values.find(value => (value && value.code === itm));
    return val ? val.label : itm;
 }

  /**
   * This method validates if there are duplicate values in both source and target.
   * If yes, remove them from source and refresh the source list.
   */
  private refreshSourceList() {
    // Build the target (RHS) of the picklist
    this.targetList = [];
    if (this.element.leafState != null && this.element.leafState.length > 0) {
      let leafState = this.element.leafState as [];
      if (!this.element.values) {
        // if NO values are given, then default all labels to be the code
        this.targetList = leafState.map(code => Values.of(code, code));
      } else {
        this.targetList = leafState.map(code => {
          let value = this.element.values.find(x => x.code == code);
          // if values is not given, then default the label to be the code
          return value ? value : Values.of(code, code);
        });
      }
    }

    // Build the source (LHS) of the picklist
    if (this.targetList.length > 0) {
      if (this.parent.values != null && this.parent.values.length > 0) {
        this.sourceList = this.parent.values.filter(
          value => this.targetList.indexOf(value) < 0
        );
      }
    } else {
      this.sourceList = [];
      if (this.parent.values) {
        this.parent.values.forEach(value => {
          this.sourceList.push(value);
        });
      }
    }
  }

  /**
   * This is a temp hack to resolve the complex type form submit issues
   */
  private updateParentValue(event: any): GenericDomain {
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
