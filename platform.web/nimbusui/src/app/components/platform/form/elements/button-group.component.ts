/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
