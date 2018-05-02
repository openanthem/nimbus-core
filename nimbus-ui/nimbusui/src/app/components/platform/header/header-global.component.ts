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
import { LayoutService } from '../../../services/layout.service';
import { Param } from '../../../shared/param-state';
import { AppBranding, LinkConfig } from '../../../model/menu-meta.interface';
import { ServiceConstants } from './../../../services/service.constants';
import { BreadcrumbService } from './../breadcrumb/breadcrumb.service';
import { Breadcrumb } from '../../../model/breadcrumb.model';

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

    private messageCount : number = 3;

    constructor(private _breadcrumbService: BreadcrumbService) {
        this.imagesPath = ServiceConstants.IMAGES_URL;
    }

    public get homeRoute(): string {
        let crumb: Breadcrumb = this._breadcrumbService.getHomeBreadcrumb();
        if (crumb) {
            return crumb['url'];
        }
        return '';
    }
}

