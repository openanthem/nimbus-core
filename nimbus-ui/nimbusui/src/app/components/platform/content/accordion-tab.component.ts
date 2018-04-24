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
import { WebContentSvc } from '../../../services/content-management.service';
import { BaseElement } from '../base-element.component';
import { PageService } from '../../../services/page.service';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component( {
    selector: 'nm-accordion-tab',
    providers: [WebContentSvc],
    template: `
        <p-accordionTab [header]="label" [selected]="element?.config?.uiStyles?.attributes?.selected">
            <div class="accordionBtn" *ngIf="element?.config?.uiStyles?.attributes?.editable">
                <button  (click)="processOnClick(this)" type="button" class="btn btn-plain">
                    <i class="fa fa-fw fa-pencil" aria-hidden="true"></i>Edit
                </button>
            </div>
            <ng-template ngFor let-element [ngForOf]="element?.type?.model?.params">
                <!-- Card Content -->
                <ng-template [ngIf]="element.alias == 'CardDetail'">
                    <nm-card-details [element]="element"></nm-card-details>
                </ng-template>
            </ng-template>
        </p-accordionTab>
    `
})

export class AccordionTab  extends BaseElement {

    protected _selected: boolean;

    constructor(private wcsvc: WebContentSvc, private pageSvc: PageService) {
        super(wcsvc);
    }

    ngOnInit() {
        super.ngOnInit();
    }

    /**
     * Tab Selected?
     */
    public get selected(): boolean {
        return this.element.config.uiStyles.attributes.selected;
    }

    /**
     * Process configs on the click event
     */
    processOnClick() {
        this.pageSvc.processEvent(this.element.path, '$execute', null, 'POST');
    }
}
