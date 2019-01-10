import { ViewRoot } from './../../shared/app-config.interface';
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
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { first, map } from 'rxjs/operators';
import { PageService } from '../../services/page.service';
import { ConfigService } from '../../services/config.service';
import { LoggerService } from '../../services/logger.service';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Injectable()
export class LayoutResolver implements Resolve<ViewRoot> {

    constructor(
        private _pageSvc: PageService,
        private _configSvc: ConfigService,
        private _logger: LoggerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ViewRoot> | Promise<ViewRoot> {
        let flowName = route.params['domain'];
        let flowConfig = this._configSvc.getFlowConfig(flowName);
        if (flowConfig && flowConfig.model) {

            let routeToDefaultPage: boolean = true;
            if (route.firstChild.params['pageId'] || route.firstChild.params['subdomain']) {
                routeToDefaultPage = false;
            }
            return this._pageSvc.getFlowLayoutConfig(flowName, routeToDefaultPage).then(layout => {
                this._logger.debug('layout resolver service flowName can be navigated' + flowName);
                return this._configSvc.getFlowConfig(flowName);
            });
        } else {
            this._pageSvc.getLayoutConfigForFlow(flowName);
            return this._pageSvc.layout$.pipe(map(layout => {
                this._logger.debug('layout resolver service flowName can be navigated' + flowName);
                return this._configSvc.getFlowConfig(flowName);
            }),first());
        }
    }

}
