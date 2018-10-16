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
import { Component, Input, SimpleChanges } from '@angular/core';
import { Param } from '../../shared/param-state';
import { WebContentSvc } from '../../services/content-management.service';
import { BaseElement } from './base-element.component';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-subheader',
    providers: [WebContentSvc],
    template:`           
        <ng-template [ngIf]="!param?.config?.type?.nested">
           <div class="{{param?.config?.uiStyles?.attributes?.cssClass}}">
                <ng-template [ngIf]="isDate(param.config.type.name)">
                    <span [hidden]="!param?.config?.uiStyles?.attributes?.showName">{{label}}</span>
                    <span>{{param.leafState | dateTimeFormat: param.config?.uiStyles?.attributes?.datePattern : param.config.type.name }}</span>
                </ng-template>
                <div *ngIf="!isDate(param.config.type.name)">
                    <span [hidden]="!param?.config?.uiStyles?.attributes?.showName">{{label}}</span>
                    <span>{{param.leafState}}</span>
                </div>
           </div>
        </ng-template>
    `
})
export class SubHeaderCmp extends BaseElement{

    @Input() param: Param;
    
    constructor(private _wcs: WebContentSvc) {
        super(_wcs);
    }

    ngOnInit() {
        this.loadLabelConfig(this.param);
    }
    
}
