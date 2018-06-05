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

import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd, Params, PRIMARY_OUTLET } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { DomainFlowCmp } from './../../domain/domain-flow.component';
import { BreadcrumbService } from './breadcrumb.service';
import { Breadcrumb } from './../../../model/breadcrumb.model';
/**
 * \@author Tony.Lopez
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-breadcrumb',
    template: `
        <div class="breadcrumb-bar"> 
            <ol class="breadcrumb">
                <li *ngFor="let breadcrumb of breadcrumbs" class="breadcrumb-item">
                    <a [routerLink]="[breadcrumb.url]">{{breadcrumb.label}}</a>
                </li>
            </ol>
        </div>
    `
})
export class BreadcrumbComponent implements OnInit {

    public breadcrumbs: Breadcrumb[];

    constructor (private _activatedRoute: ActivatedRoute, 
        private _router: Router,
        private _breadcrumbService: BreadcrumbService) {
        
        // initialize breadcrumbs as empty
        this.breadcrumbs = [];

        // retrieve labels
        this.breadcrumbs.forEach(b => {
                b.label = b.id;
        });

        //subscribe to the NavigationEnd event and load the breadcrumbs.
        this._router.events.filter(event => event instanceof NavigationEnd).subscribe(event => {
            this._loadBreadcrumbs();
        });
    }

    ngOnInit() {
    }

    /**
     * Returns array of IBreadcrumb objects that represent the breadcrumb.
     *
     * @method getBreadcrumbs
     * @param {ActivateRoute} route
     * @param {string} url
     * @param {IBreadcrumb[]} breadcrumbs
     */
    private getBreadcrumbs(route: ActivatedRoute, breadcrumbs: Breadcrumb[]=[]): Observable<Breadcrumb[]> {

        //get the child routes, return if there are no more children
        let children: ActivatedRoute[] = route.children;
        if (children.length === 0) {
            return Observable.of(breadcrumbs);
        }

        //iterate over each children
        for (let child of children) {

            //verify primary route
            if (child.outlet !== PRIMARY_OUTLET) {
                continue;
            }

            // Check for param[0] in the route object.
            // If there is a param object in the route, we will use it to identify the Param
            // we are explicitely ignoring the domain flow
            let params = child.snapshot.params;
            if (child.component === DomainFlowCmp || !params || !params[Object.keys(params)[0]]) {
                return this.getBreadcrumbs(child, breadcrumbs);
            }

            //get the pageId from the route
            let pageId: string = params[Object.keys(params)[0]];

            //add breadcrumb
            let breadcrumb = this._breadcrumbService.getByPageId(pageId);
            if (breadcrumb) {
                breadcrumbs.push(breadcrumb);
            }

            //recursive until exit condition (no remaining route children)
            return this.getBreadcrumbs(child, breadcrumbs);
        }
    }

    private _loadBreadcrumbs(): void {
        // if activatedRoute is the "home" route, show no breadcrumbs
        if (!this._breadcrumbService.isHomeRoute(this._activatedRoute)) {

            let root: ActivatedRoute = this._activatedRoute.root;
            this.getBreadcrumbs(root).subscribe(breadcrumbs => {
                
                this.breadcrumbs = [];

                // add home breadcrumb data
                this.breadcrumbs.push(this._breadcrumbService.getHomeBreadcrumb());

                // Push the result from the service into this.breadcrumbs
                this.breadcrumbs.push.apply(this.breadcrumbs, breadcrumbs);

                // update labels
                // this.breadcrumbs.forEach(breadcrumb => this._wcs.getContent(breadcrumb.id));
            });
        } else {

            // if it is the "home" route, reset the breadcrumbs
            this.breadcrumbs = [];
        }
    }
}
