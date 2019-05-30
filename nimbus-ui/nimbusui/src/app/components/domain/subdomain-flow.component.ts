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

import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { LinkConfig } from '../../model/menu-meta.interface';
import { LayoutService } from '../../services/layout.service';
import { LoggerService } from '../../services/logger.service';
import { PageService } from '../../services/page.service';
import { Param } from '../../shared/param-state';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  templateUrl: './sub-domain-flow.component.html',
  providers: [LayoutService]
})
export class SubDomainFlowCmp {
  public hasLayout: boolean = true;
  public infoClass: string = '';
  public leftMenuItems: LinkConfig[];
  public topMenuItems: Param[];
  public subHeaders: Param[];
  public actionTray: Param;
  public _showActionTray: boolean;
  items: MenuItem[];
  routeParams: any;

  constructor(
    private _pageSvc: PageService,
    private layoutSvc: LayoutService,
    private _route: ActivatedRoute,
    private _router: Router,
    private _logger: LoggerService
  ) {
    //TODO - enable the below code and test if dynamic subdomains can be added and the config emitted is processed correctly
    // this._pageSvc.subdomainconfig$.subscribe(result => {
    //     let page: Page = result;
    //     this._logger.debug('domain flow component received page from config$ subject');
    //     if (page && page.pageConfig && page.pageConfig.config) {
    //         // Navigate to page with pageId
    //         let toPage = './' +  page.flow + '/' + page.pageConfig.config.code;
    //         this._logger.debug('sub domain flow component will be navigated to ' + toPage + ' route');
    //         this._router.navigate([toPage], { relativeTo: this._route });
    //     }
    // });
  }

  ngOnInit() {
    this._logger.debug('SubDomainDomainFlowCmp-i ');
  }
}
