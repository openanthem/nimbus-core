/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { Component, Input } from '@angular/core';
import { Param } from '../../../shared/app-config.interface';
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
