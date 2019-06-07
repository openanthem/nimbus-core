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
  Router,
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot,
  ActivatedRoute
} from '@angular/router';
import { BreadcrumbService } from './../breadcrumb/breadcrumb.service';
import { PageService } from '../../../services/page.service';
import { Param } from '../../../shared/param-state';
import { ParamUtils } from './../../../shared/param-utils';
import { LoggerService } from '../../../services/logger.service';

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
    private route: ActivatedRoute,
    private _logger: LoggerService
  ) {}

  resolve(
    route: ActivatedRouteSnapshot,
    rustate: RouterStateSnapshot
  ): Promise<Param> {
    let pageId = route.params['pageId'];
    //let flow = route.parent.data['domain'];
    let flow = route.parent.url[0]['path'];
    return this._pageSvc.getPageConfigById(pageId, flow).then(page => {
      if (page) {
        let labelText = ParamUtils.getLabelText(page);
        if (
          page.config &&
          page.config.uiStyles &&
          page.config.uiStyles.attributes &&
          page.config.uiStyles.attributes.breadcrumbLabel
        ) {
          labelText = page.config.uiStyles.attributes.breadcrumbLabel;
        } else if (!labelText || labelText.length === 0) {
          labelText = page.config.code;
        }
        // Push the home breadcrumb into memory under the domain name.
        this._breadcrumbService.push(
          page.config.code,
          labelText,
          route['_routerState'].url
        );
        this._logger.debug(
          'page resolver service: resolve() is returning the page '
        );
        return page;
      } else {
        // page not found
        this._logger.debug(
          'page resolver service: resolve() is navigating to ' + flow
        );
        this._router.navigate([`/h/${flow}`]);
        return null;
      }
    });
  }
}
