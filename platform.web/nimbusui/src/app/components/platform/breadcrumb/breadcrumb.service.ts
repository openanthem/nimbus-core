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

import { Injectable } from '@angular/core';
import { ActivatedRoute, Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { ServiceConstants } from './../../../services/service.constants';
import { PageService } from './../../../services/page.service';

import { IBreadcrumb } from './ibreadcrumb.d';

@Injectable()
export class BreadcrumbService {

    private _breadcrumbs: any;

    constructor(private _pageService: PageService) {

        this._breadcrumbs = {};
    }

    public push(pageId: string, label: string, path: string): boolean {
        if (!this._breadcrumbs.hasOwnProperty(pageId)) {
            this._breadcrumbs[pageId] = {
                id: pageId,
                label: label,
                params: null,
                url: `${ServiceConstants.HOME_ROUTE}${path}`
            };
            return true;
        }
        return false;
    }

    public getAll(): IBreadcrumb[] {
        return this._breadcrumbs;
    }

    public getByPageId(pageId: string): IBreadcrumb {
        return this._breadcrumbs[pageId];
    }

    // TODO - Need to add this to configuration.
    public getHomeBreadcrumb(): IBreadcrumb {
        return {
            id: ServiceConstants.HOME_DOMAIN,
            label: 'Home',
            params: null,
            url: `${ServiceConstants.HOME_ROUTE}/${ServiceConstants.HOME_DOMAIN}/${ServiceConstants.HOME_PAGE}`
        }
    }

    /**
     * Returns true if the provided route is configured as the HOME route in @ViewRoot.
     * @param route the route to check
     */
    public isHomeRoute(route: ActivatedRoute) {
        // TODO - Obtain this value from configuration.
        return route.firstChild.snapshot.params['pageId'] === ServiceConstants.HOME_PAGE;
    }
}
