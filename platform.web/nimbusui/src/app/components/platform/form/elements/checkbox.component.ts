/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

'use strict';
import { ViewChild, Component, forwardRef, ChangeDetectorRef } from '@angular/core';
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { BaseControl } from './base-control.component';
import { Param } from '../../../../shared/app-config.interface';

@Component({
  selector: 'nm-single-checkbox',
  template: ` <label class="custom-control custom-check">
                 <input type="checkbox" [disabled]="!element?.enabled?.currState" class="custom-control-input" (change)="emitValueChangedEvent(this,$event)" [(ngModel)] = "value" [attr.checked]="value">
                 <span class="custom-control-indicator"></span>
                 <span class="custom-control-description">{{label}}
                      <nm-tooltip *ngIf="element.config?.uiStyles?.attributes?.help!=''" [helpText]='element.config?.uiStyles?.attributes?.help'></nm-tooltip>
                 </span>
             </label>
            `,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CheckBox),
      multi: true
    }, WebContentSvc
  ]
})
export class CheckBox extends BaseControl<boolean> {

    @ViewChild(NgModel) model: NgModel;
    dis: boolean;
    element: Param;

    constructor(wcs: WebContentSvc, pageService: PageService, cd:ChangeDetectorRef) {
      super(pageService,wcs,cd);
  }

}
