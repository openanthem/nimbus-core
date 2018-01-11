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

import { BreadcrumbService } from './../breadcrumb/breadcrumb.service';
import { PageService } from '../../../services/page.service';
import { Param } from '../../../shared/app-config.interface';
import { Injectable } from '@angular/core';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class PageResolver implements Resolve<Param> {

    constructor(
        private _pageSvc: PageService, 
        private _router: Router,
        private _breadcrumbService: BreadcrumbService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<Param> {
        let pageId = route.params['pageId'];
        //let flow = route.parent.data['domain'];
        let flow = route.parent.url[0]['path'];
        return this._pageSvc.getPageConfigById(pageId, flow).then(page => {
            if (page) {

                // Push the home breadcrumb into memory under the domain name.
                this._breadcrumbService.push(page.config.code, page.config.code, page.path);
                
                return page;
            } else { // page not found
                this._router.navigate([`/h/${flow}`]);
                return null;
            }
        });
    }
}
