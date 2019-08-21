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

import { EventEmitter, Injectable, Output } from '@angular/core';
import { AbstractControl } from '@angular/forms/src/model';
import { Subscription } from 'rxjs';
import { BaseControl } from '../components/platform/form/elements/base-control.component';
import { HttpMethod } from '../shared/command.enum';
import { GenericDomain } from './../model/generic-domain.model';
import { Param } from './../shared/param-state';
import { ParamUtils } from './../shared/param-utils';
import { PageService } from './page.service';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Injectable()
export class ControlSubscribers {
  @Output() controlValueChanged = new EventEmitter();

  private previousLeafState: any;

  private subscribers: Subscription[] = [];

  constructor(private pageService: PageService) {}

  ngOnDestroy() {
    if (this.subscribers && this.subscribers.length > 0) {
      this.subscribers.forEach(s => s.unsubscribe());
    }
  }

  /**
   * Register a subscriber that executes consumer whenever:
   * 1. An event is emitted (param) from pageService.eventUpdate$
   * 2. The emitted event is matching the control element within control
   * @param control the control element to match against
   * @param consumer the callback method to execute if event contains an update for control
   */
  private onEventUpdateSubscriber(
    control: BaseControl<any>,
    consumer: (event: Param, frmCtrl: AbstractControl) => void
  ): Subscription {
    return this.pageService.eventUpdate$.subscribe(event => {
      let frmCtrl = control.form.controls[event.config.code];
      if (frmCtrl != null && event.path == control.element.path) {
        if (consumer) {
          consumer(event, frmCtrl);
        }
      }
    });
  }

  /**
   * Register a subscriber that:
   * 1. Executes onEnabled whenever an event is emitted that matches control and event.enabled is true.
   * 2. Executes onDisabled whenever an event is emitted that matches control and event.enabled is false.
   * @param control the control element to match against
   * @param onEnabled the callback method to execute if event contains an update for control and event.enabled is true
   * @param onDisabled the callback method to execute if event contains an update for control and event.enabled is false
   */
  public onEnabledUpdateSubscriber(
    control: BaseControl<any>,
    onEnabled: (event: Param, frmCtrl: AbstractControl) => void,
    onDisabled: (event: Param, frmCtrl: AbstractControl) => void
  ) {
    this.subscribers.push(
      this.onEventUpdateSubscriber(control, (event, frmCtrl) => {
        if (onEnabled && event.enabled) {
          return onEnabled(event, frmCtrl);
        } else if (onDisabled && !event.enabled) {
          return onDisabled(event, frmCtrl);
        }
      })
    );
  }

  /**
   * Register a subscriber that sets the value of the form control when the control matches the updated event. If the value
   * of the updated event is provided, it will be set, otherwise the form control will be reset.
   * @param control the control element to match against
   */
  public stateUpdateSubscriber(control: BaseControl<any>) {
    this.subscribers.push(
      this.onEventUpdateSubscriber(control, (event, frmCtrl) => {
        if (event.leafState != null) {
          this.previousLeafState = event.leafState;
          frmCtrl.setValue(event.leafState);
        } else {
          frmCtrl.reset();
        }
      })
    );
  }

  public validationUpdateSubscriber(control: BaseControl<any>) {
    this.subscribers.push(
      this.pageService.validationUpdate$.subscribe(event => {
        ParamUtils.validate(event, control);
      })
    );
  }

  public resetPreviousLeafState(val: any) {
    this.previousLeafState = val;
  }

  public onChangeEventSubscriber(control: BaseControl<any>) {
    this.subscribers.push(
      this.controlValueChanged.subscribe($event => {
        //old leafState is used to post data to the server only when the user has changed the value
        if (
          $event.config.uiStyles.attributes.postEventOnChange &&
          ($event.leafState == null ||
            this.previousLeafState !== $event.leafState)
        ) {
          let leafState = $event.leafState;
          if (ParamUtils.isKnownDateType($event.config.type.name)) {
            leafState = ParamUtils.convertDateToServerDate(leafState, $event.config.type.name);
          } 
          this.pageService.postOnChange(
            $event.path,
            'state',
            JSON.stringify(leafState)
          );
          
        } else if ($event.config.uiStyles.attributes.postButtonUrl) {
          let item: GenericDomain = new GenericDomain();
          this.pageService.processEvent(
            control.element.config.uiStyles.attributes.postButtonUrl,
            null,
            $event.leafState,
            HttpMethod.POST.value
          );
        }
        this.previousLeafState = $event.leafState;
      })
    );
  }
}
