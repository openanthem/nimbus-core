/**
 * @license
 * Copyright 2016-2018 the original author or authors.
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

import { Component, Input } from '@angular/core';
import { Param } from '../../../shared/param-state';
import { LayoutService } from '../../../services/layout.service';
import {KeysPipe} from '../../../pipes/app.pipe';

/**
 * \@author Mayur.Mehta
 * \@whatItDoes 
 * 
 * This component creates a global navigation menu below the header within an application that will display the 
 * organization(s) that the user belongs to and global menu items
 * 
 */

//how to make sure that 1 value is preselected -- both seems f/w changes
@Component({
    selector: 'nm-nav-menu-global',
    template: `

    <nav class="globalNav">     
        <div *ngIf="organization" class="orgSelect">     
            <div *ngIf="organization.values.length < 2 ; else orgs">
                {{organization.values[0].label}}
            </div>
            <ng-template #orgs>
                <nm-comboBox [element]="organization" [(ngModel)]="organization.leafState"></nm-comboBox>
            </ng-template>
        </div>
        
        <ng-template ngFor let-key [ngForOf]="menuItems | keys">
            <div class="custom-dropdown" [ngClass]="{'open': isOpen}">
                <button class="dropdownTrigger" attr.aria-expanded="{{isOpen}}">
                    {{key.key}}
                </button> 
                <div class="dropdownContent" attr.aria-hidden="{{!isOpen}}">
                    <ng-template ngFor let-value [ngForOf]="key.values">
                        <nm-link [element]="value"></nm-link>
                     </ng-template>
                </div>
            </div>
        </ng-template>

        <ng-template *ngIf="menuLinks" ngFor let-menuLink [ngForOf]="menuLinks">
            <nm-link [element]="menuLink"></nm-link>
        </ng-template>
       

    </nav>
     
     `
 })
 
 export class NavMenuGlobal{

  @Input() organization: Param;
  @Input() menuItems: Map<string, Param[]>;
  @Input() menuLinks: Param[];

    constructor(){}

}
