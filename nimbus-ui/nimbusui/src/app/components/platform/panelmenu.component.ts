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
*
* The MIT License (MIT)
* Copyright (c) 2016-2017 PrimeTek
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
'use strict';

import { Component, Input } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { MenuItem } from '../../shared/menuitem';
import { ComponentTypes } from './../../shared/param-annotations.enum';

/**
* \@author Sandeep.Mantha
* Modified the panel menu component of primeng
* \@whatItDoes
*
* \@howToUse
*
*/
export class NmBasePanelMenuItem {

    componentTypes = ComponentTypes;

    handleClick(event, item) {
        if(item.disabled) {
            event.preventDefault();
            return;
        }
        
        item.expanded = !item.expanded;
        
        if(!item.url) {
            event.preventDefault();
        }
                  
        if(item.command) {
            item.command({
                originalEvent: event,
                item: item
            });
        }
    }

}

@Component({
    selector: 'nm-panelMenuSub',
    template: `
        <ul class="ui-submenu-list" [@submenu]="expanded ? 'visible' : 'hidden'">
            <ng-template ngFor let-child [ngForOf]="item.items">
                <li *ngIf="child.separator" class="ui-menu-separator ui-widget-content">
                <li *ngIf="!child.separator" class="ui-menuitem ui-corner-all" [ngClass]="child.styleClass" [class.ui-helper-hidden]="child.visible === false" [ngStyle]="child.style">    
                    
                    <a *ngIf="child.type == componentTypes.internal.toString()"
                        class="ui-menuitem-link ui-corner-all internal" 
                        nmrouterLink="{{child.routerLink}}"
                        nmrouterLinkActive="ui-state-active" 
                        [item]="child" 
                        [ngClass]="{'ui-state-disabled':child.disabled}" 
                        [queryParams]="child.queryParams" 
                        [attr.id]="child.id"
                        [attr.tabindex]="item.expanded ? null : '-1'" 
                        [attr.target]="child.target" 
                        [attr.title]="child.title"
                        (toggleParent)="handleParentToggle(item)"
                        (click)="handleClick($event,child)">

                            <span *ngIf="child.items"
                                class="ui-panelmenu-icon fa fa-fw" 
                                [ngClass]="{'fa-caret-right':!child.expanded,'fa-caret-down':child.expanded}">
                            </span>
                            <nm-image class='nm-panelmenu-headerimage' *ngIf="child.icon" [name]="child.icon" [type]="child.imgType" [title]="" [cssClass]=""></nm-image>
                            <span class="ui-menuitem-text">{{child.label}}</span>
                    </a>

                    <a *ngIf="child.type == componentTypes.external.toString()" 
                        class = "ui-menuitem-link ui-corner-all external"  
                        href="{{child.url}}" 
                        target="{{child.target}}" 
                        rel="{{child.rel}}">
                            {{child.label}}
                    </a>

                    <nm-panelMenuSub [item]="child" [expanded]="child.expanded" *ngIf="child.items"></nm-panelMenuSub>
                </li>
            </ng-template>
        </ul>
    `,
    animations: [
        trigger('submenu', [
            state('hidden', style({
                height: '0px'
            })),
            state('visible', style({
                height: '*'
            })),
            transition('visible => hidden', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)')),
            transition('hidden => visible', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
        ])
    ]
})
export class NmPanelMenuSub extends NmBasePanelMenuItem {

    @Input() item: MenuItem;
    
    @Input() expanded: boolean;

    handleParentToggle(it: MenuItem) {}
}

@Component({
    selector: 'nm-panelMenu',
    template: `
    <div [class]="styleClass" [ngStyle]="style" [ngClass]="'ui-panelmenu ui-widget'">
        <ng-container *ngFor="let item of model;let f=first;let l=last;">
            <div class="ui-panelmenu-panel" [ngClass]="{'ui-helper-hidden': item.visible === false}">
                <div [ngClass]="{'hassubmenu': item.items&&item.items.length > 0, 'ui-widget ui-panelmenu-header ':true,'ui-corner-top':f,'ui-corner-bottom':l&&!item.expanded,
                'ui-state-disabled':item.disabled, 'ui-state-active':item.expanded}" [class]="item.styleClass" [ngStyle]="item.style">    
                    
                    <!-- INTERNAL Menu Link -->
                    <a *ngIf="item.type == componentTypes.internal.toString()"
                        id="Panelmenu-{{item.code}}"
                        class="ui-panelmenu-header-link internal"
                        nmrouterLink="{{item.routerLink}}"
                        nmrouterLinkActive="ui-state-active"
                        [queryParams]="item.queryParams"
                        [item]="item"
                        [attr.target]="item.target" 
                        [attr.title]="item.title"
                        (toggleParent)="handleParentToggle(item)"
                        (click)="handleClick($event,item)">
                            
                            <span *ngIf="item.items" 
                                class="ui-panelmenu-icon fa fa-fw" 
                                [ngClass]="{'fa-caret-right':!item.expanded,'fa-caret-down':item.expanded}">
                            </span>
                            <span class="ui-menuitem-text">{{item.label}}</span>
                            <nm-image class='nm-panelmenu-headerimage' *ngIf="item.icon" [name]="item.icon" [type]="item.imgType" [title]="" [cssClass]=""></nm-image>
                    </a>

                    <!-- EXTERNAL Panel Menu -->
                    <a *ngIf="item.type == componentTypes.external.toString()"
                        id="Panelmenu-{{item.code}}"
                        class="ui-panelmenu-header-link external"
                        href="{{item.url}}" 
                        target="{{item.target}}" 
                        rel="{{item.rel}}">
                            {{item.label}}
                    </a>

                </div>
                <div *ngIf="item.items" class="ui-panelmenu-content-wrapper" [@rootItem]="item.expanded ? 'visible' : 'hidden'"  (@rootItem.done)="onToggleDone()"
                    [ngClass]="{'ui-panelmenu-content-wrapper-overflown': !item.expanded||animating}">
                    <div class="ui-panelmenu-content ui-widget-content">
                        <nm-panelMenuSub [item]="item" [expanded]="true" class="ui-panelmenu-root-submenu"></nm-panelMenuSub>
                    </div>
                </div>
            </div>
        </ng-container>
    </div>
    `,
    animations: [
        trigger('rootItem', [
            state('hidden', style({
                height: '0px'
            })),
            state('visible', style({
                height: '*'
            })),
            transition('visible => hidden', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)')),
            transition('hidden => visible', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
        ])
    ]
})
export class NmPanelMenu extends NmBasePanelMenuItem {
    
    @Input() model: MenuItem[];

    @Input() style: any;

    @Input() styleClass: string;

    @Input() multiple: boolean = true;  
    
    public animating: boolean;

    collapseAll() {
        for(let item of this.model) {
            if(item.expanded) {
                item.expanded = false;
            }
        }
    }

    handleClick(event, item) {
        if(!this.multiple) {
            for(let modelItem of this.model) {
                if(item !== modelItem && modelItem.expanded) {
                    modelItem.expanded = false;
                }
            }
        }
        
        this.animating = true;
        super.handleClick(event, item);
    }
    
    onToggleDone() {
        this.animating = false;
    }

    handleParentToggle(it: MenuItem) {
        if(!this.multiple) {
            for(let modelItem of this.model) {
                if(it !== modelItem) {
                    modelItem.expanded = false;
                }
            }
        }
    }

}