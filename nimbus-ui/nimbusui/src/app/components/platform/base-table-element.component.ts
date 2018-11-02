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

import { ActionDropdown } from './form/elements/action-dropdown.component';
import { fromEvent as observableFromEvent, Subscription } from 'rxjs';
import { first, filter } from 'rxjs/operators';
import { Component, ViewChildren, QueryList, ChangeDetectorRef } from '@angular/core';
import { WebContentSvc } from './../../services/content-management.service';
import { BaseElement } from './base-element.component';

/**
 * \@author Tony Lopez
 * \@whatItDoes A base element for components that use an underling table structure
 */
@Component({
    selector: 'nm-base-table-element',
    providers: [ WebContentSvc ],
    template: ``
})
export class BaseTableElement extends BaseElement {

    @ViewChildren('dropDown') dropDowns: QueryList<any>;

    mouseEventSubscription: Subscription;

    constructor(
        protected _wcs: WebContentSvc, 
        protected cd: ChangeDetectorRef) {
            super(_wcs);
    }

    toggleOpen(e: any) {
        let selectedDropDownIsOpen = e.isOpen;
        let selectedDropDownState = e.state;

        if (this.dropDowns)
            this.dropDowns.toArray().forEach((item) => {
                if (!item.selectedItem) {
                    item.isOpen = false;
                    item.state = 'closedPanel';
                }
            });

        e.isOpen = !selectedDropDownIsOpen;

        if (selectedDropDownState == 'openPanel') {
            e.state = 'closedPanel';
            if (!this.mouseEventSubscription.closed)
                this.mouseEventSubscription.unsubscribe();
        }
        else {
            e.state = 'openPanel';
            if (this.dropDowns && (this.mouseEventSubscription == undefined || this.mouseEventSubscription.closed))
                this.mouseEventSubscription =
                    observableFromEvent(document, 'click').pipe(filter((event: any) =>
                        !this.isClickedOnDropDown(this.dropDowns.toArray(), event.target)),first()).subscribe(() => {
                            this.dropDowns.toArray().forEach((item) => {
                                item.isOpen = false;
                                item.state = 'closedPanel';
                            });
                            this.cd.detectChanges();
                        });
        }
        e.selectedItem = false;
        this.cd.detectChanges();
    }

    isClickedOnDropDown(dropDownArray: Array<ActionDropdown>, target: any) {
        for (var i = 0; i < dropDownArray.length; i++) {
            if (dropDownArray[i].elementRef.nativeElement.contains(target))
                return true;
        }
        return false;
    }

}