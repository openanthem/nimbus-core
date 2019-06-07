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
  forwardRef,
  ViewChild
} from '@angular/core';
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Subject } from 'rxjs';
import { ControlSubscribers } from '../../../../services/control-subscribers.service';
import { AutoCompleteService } from './../../../../services/autocomplete.service';
import { CounterMessageService } from './../../../../services/counter-message.service';
import { LoggerService } from './../../../../services/logger.service';
import { PageService } from './../../../../services/page.service';
import { ParamUtils } from './../../../../shared/param-utils';
import { BaseControl } from './base-control.component';

export const AUTOCOMPLETE_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => NmAutocomplete),
  multi: true
};

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-autocomplete',
  providers: [AUTOCOMPLETE_CONTROL_VALUE_ACCESSOR, ControlSubscribers],
  template: `
    <ng-template [ngIf]="element?.visible == true">
      <nm-input-label
        *ngIf="!isLabelEmpty && this.showLabel"
        [element]="element"
        [for]="element.config?.code"
        [required]="requiredCss"
      >
      </nm-input-label>

      <p-autoComplete
        [disabled]="!element?.enabled"
        [(ngModel)]="value"
        [minLength]="element?.config?.uiStyles?.attributes?.minLength"
        (onSelect)="sync($event)"
        [suggestions]="results"
        (completeMethod)="search($event)"
      >
      </p-autoComplete>
    </ng-template>
  `
})
export class NmAutocomplete extends BaseControl<any> {
  @ViewChild(NgModel) model: NgModel;
  results: any[];
  searchTerm$ = new Subject<string>();
  suggestionObj: any[];

  constructor(
    controlService: ControlSubscribers,
    cd: ChangeDetectorRef,
    private searchService: AutoCompleteService,
    private pageSvc: PageService,
    private logger: LoggerService,
    counterMessageService: CounterMessageService
  ) {
    super(controlService, cd, counterMessageService);
  }

  ngOnInit() {
    super.ngOnInit();
    this.searchService.search(this.searchTerm$, this.element.path).subscribe(
      data => {
        this.results = [];
        this.suggestionObj = data;
        data.forEach(obj => {
          if (obj[this.element.config.uiStyles.attributes.display]) {
            this.results.push(
              obj[this.element.config.uiStyles.attributes.display]
            );
          }
        });
      },
      error => {
        this.logger.error(
          'ERROR: Failure making server call : ' + JSON.stringify(error)
        );
      }
    );

    this.pageSvc.validationUpdate$.subscribe(event => {
      ParamUtils.validate(event, this);
    });
  }

  search($event) {
    this.searchTerm$.next($event.query);
  }

  sync(value) {
    let obj = this.suggestionObj.find(
      obj => obj[this.element.config.uiStyles.attributes.display] == value
    );
    let postObject: any;
    if (this.form === undefined) {
      postObject = obj;
    } else if (
      this.form &&
      (this.form.controls[this.element.config.code] != null &&
        this.form.controls[this.element.config.code].valid)
    ) {
      postObject = obj[this.element.config.uiStyles.attributes.display];
    }

    if (this.element.config.uiStyles.attributes.postEventOnChange) {
      this.pageSvc.postOnChange(
        this.element.path,
        'state',
        JSON.stringify(postObject)
      );
    }
  }
}
