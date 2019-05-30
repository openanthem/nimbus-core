/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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
import {
  ActivatedRoute,
  ActivatedRouteSnapshot,
  Resolve,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import { ConfigService } from '../../../services/config.service';
import { PageService } from '../../../services/page.service';
import { Page } from '../../../shared/app-config.interface';
import { Param } from '../../../shared/param-state';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Injectable()
export class FlowResolver implements Resolve<Param> {
  page: Page;

  constructor(
    private _pageSvc: PageService,
    private _configSvc: ConfigService,
    private _route: ActivatedRoute,
    private _router: Router
  ) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<Param> {
    let flowName = route.params['flow'];

    this._pageSvc.config$.subscribe(result => {
      this.page = result;
      // Navigate to page with pageId
      let toPage = this.page.pageConfig.config.code;
      let parentRoute = this.page.pageConfig.config.uiStyles.attributes.route;
      if (parentRoute) {
        toPage = parentRoute + '/' + toPage;
      }
      //this._router.navigate([toPage], { queryParams: { flow: this.page.flow }, relativeTo: this._route.parent.parent });
      this._router.navigate([toPage], { relativeTo: this._route });
    });

    // Check if this is a new flow to be loaded.
    if (this._configSvc.getFlowConfig(flowName) === undefined) {
      this._pageSvc.loadFlowConfig(flowName);
    } else {
      // load page from pre loaded config
      this._pageSvc.loadDefaultPageForConfig(flowName);
    }

    return null;
  }
}
