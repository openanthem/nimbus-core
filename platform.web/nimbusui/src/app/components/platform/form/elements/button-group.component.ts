/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { Component, Input, ViewEncapsulation } from '@angular/core';
import { Param } from '../../../../shared/app-config.interface';
import { FormGroup } from '@angular/forms';
@Component({
    selector: 'nm-button-group',
    template:`
        <div class="{{cssClass}} mt-1 mb-1">
            <ng-template ngFor let-element [ngForOf]="buttonList">
                <ng-template [ngIf]="element.config?.uiStyles?.attributes?.alias=='Button'">
                    <nm-button [form]="form" [element]="element"></nm-button>
                </ng-template>
                <ng-template [ngIf]="element.config?.uiStyles?.attributes?.alias=='FilterButton'">
                    <nm-filter-button [form]="form" [filterButton]="element"></nm-filter-button>
                </ng-template>
            </ng-template>
        </div>
    `
})

export class ButtonGroup {
   @Input() buttonList: Param[];
   @Input() form: FormGroup;
   @Input() cssClass: string;

   constructor() { }

   ngOnInit() {
       //console.log("css class " + this.cssClass)
   }
}
