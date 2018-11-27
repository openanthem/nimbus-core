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
import { Component, Input, ViewChildren, QueryList, ChangeDetectorRef } from '@angular/core';
import { LayoutService } from '../../../services/layout.service';
import { Param } from '../../../shared/param-state';
import { AppBranding, LinkConfig } from '../../../model/menu-meta.interface';
import { ServiceConstants } from './../../../services/service.constants';
import { BreadcrumbService } from './../breadcrumb/breadcrumb.service';
import { Breadcrumb } from '../../../model/breadcrumb.model';
import { ActionDropdown } from '../form/elements/action-dropdown.component';
import { fromEvent as observableFromEvent,  Subscription, Observable } from 'rxjs';
import { first, filter } from 'rxjs/operators';

/**
 * \@author Mayur.Mehta
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-header-global',
    templateUrl: './header-global.component.html'
})

export class HeaderGlobal {
    @Input() branding : AppBranding;
    @Input() topMenuItems: Param[];
    @Input() leftMenuItems: LinkConfig[];
    @Input() element: Param;
    private imagesPath: string;
    private _homeRoute: string;
    @ViewChildren('dropDown') dropDowns: QueryList<any>;
    mouseEventSubscription: Subscription;

    private messageCount : number = 3;

    constructor(private _breadcrumbService: BreadcrumbService, private cd: ChangeDetectorRef) {
        this.imagesPath = ServiceConstants.IMAGES_URL;
    }

    public get homeRoute(): string {
        let crumb: Breadcrumb = this._breadcrumbService.getHomeBreadcrumb();
        if (crumb) {
            return crumb['url'];
        }
        return '';
    }

    ngAfterViewInit() {
        console.log(this.branding);
    }

    ngOnDestroy() {
        if (this.mouseEventSubscription)
            this.mouseEventSubscription.unsubscribe();
        this.cd.detach();
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

    get headerImageURL() {
        if (!this.branding || !this.branding.logo.config || !this.branding.logo.config.uiStyles) {
            return undefined;
        }
        let imgSrc = this.branding.logo.config.uiStyles.attributes.imgSrc;
        if (!imgSrc) {
            return undefined;
        }
        return this.imagesPath + this.branding.logo.config.uiStyles.attributes.imgSrc;
    }
}

