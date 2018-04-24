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
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { BreadcrumbService } from './../breadcrumb/breadcrumb.service'
import { WebContentSvc } from './../../../services/content-management.service';
import { PageService } from '../../../services/page.service';
import { Param } from '../../../shared/param-state';
import { LabelConfig } from './../../../shared/param-config';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Injectable()
export class PageResolver implements Resolve<Param> {

    constructor(
        private _pageSvc: PageService, 
        private _router: Router,
        private _breadcrumbService: BreadcrumbService,
        private _wcs: WebContentSvc
    ) {}

    resolve(route: ActivatedRouteSnapshot, rustate: RouterStateSnapshot): Promise<Param> {
        let pageId = route.params['pageId'];
        //let flow = route.parent.data['domain'];
        let flow = route.parent.url[0]['path'];
        return this._pageSvc.getPageConfigById(pageId, flow).then(page => {
            if (page) {
                let labelConfig: LabelConfig = this._wcs.findLabelContent(page);
                let labelText = page.config.code;
                if (labelConfig.text && labelConfig.text.trim().length > 0) {
                    labelText = labelConfig.text;
                }
                // Push the home breadcrumb into memory under the domain name.
                this._breadcrumbService.push(page.config.code, labelText, route['_routerState'].url);
                
                return page;
            } else { // page not found
                this._router.navigate([`/h/${flow}`]);
                return null;
            }
        });
    }
}
