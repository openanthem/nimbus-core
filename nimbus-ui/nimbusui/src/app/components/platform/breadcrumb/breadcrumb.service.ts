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

import { Injectable } from '@angular/core';
import { ActivatedRoute, Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { ServiceConstants } from './../../../services/service.constants';
import { PageService } from './../../../services/page.service';
import { Breadcrumb } from '../../../model/breadcrumb.model';

/**
 * \@author Tony.Lopez
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Injectable()
export class BreadcrumbService {

    private _breadcrumbs: any;
    private _homePageId: string = '';

    constructor(private _pageService: PageService) {
        
    }

    public addCrumb(pageId: string, label: string, path: string) : Breadcrumb {
        let crumb = {
            id: pageId,
            label: label,
            params: null,
            url: `${path}`
        } as Breadcrumb;
        return crumb;
    }

    public push(pageId: string, label: string, path: string): boolean {
        /** Push the first crumb as home crumb */
        if (this._breadcrumbs == undefined) {
            this._breadcrumbs = {};
            this._homePageId = pageId;
            this._breadcrumbs[pageId] = this.addCrumb(pageId, label, path);
        } else { // Place the crumb in the right place
            let tempBreadCrumbs = {};
            // Does the crumb already exist?
            if (this._breadcrumbs[pageId]) {
                // Add up to this crumb and remove rest
                for (let p in this._breadcrumbs) {
                    let crumb = this._breadcrumbs[p];
                    tempBreadCrumbs[crumb['id']] = crumb; 
                    if (crumb['id'] == pageId) {
                        break;
                    }
                }
                this._breadcrumbs = tempBreadCrumbs;
            } else {
                // Add this crumb to the end;
                this._breadcrumbs[pageId] = this.addCrumb(pageId, label, path);
            }
        }
        return false;
    }

    public getAll(): Breadcrumb[] {
        return this._breadcrumbs;
    }

    public getByPageId(pageId: string): Breadcrumb {
        return this._breadcrumbs[pageId];
    }

    /** Return home route url */
    public getHomeBreadcrumb(): Breadcrumb {
        if (this._breadcrumbs) {
            return this._breadcrumbs[this._homePageId];
        }
        return undefined;
    }

    /**
     * Returns true if the provided route is configured as the HOME route in @ViewRoot.
     * @param route the route to check
     */
    public isHomeRoute(route: ActivatedRoute) {
        // TODO - Obtain this value from configuration.
        return route.firstChild.snapshot.params['pageId'] === this._homePageId;
    }

}
