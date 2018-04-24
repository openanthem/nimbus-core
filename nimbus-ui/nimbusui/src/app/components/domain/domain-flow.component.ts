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
import { ActivatedRoute, Router } from '@angular/router';
import { Component } from '@angular/core';
import { LayoutService } from '../../services/layout.service';
import { PageService } from '../../services/page.service';
import { Layout, LinkConfig } from '../../model/menu-meta.interface';
import { Page } from '../../shared/app-config.interface';
import { Param } from '../../shared/param-state';
/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    templateUrl: './domain-flow.component.html',
    providers: [ LayoutService ]
})

export class DomainFlowCmp {
    public hasLayout: boolean = true;
    public infoClass: string = '';
    public leftMenuItems: LinkConfig[];
    public topMenuItems: Param[];
    public subHeaders: Param[];
    routeParams: any;

    constructor(private _pageSvc: PageService, private layoutSvc: LayoutService,
            private _route: ActivatedRoute, private _router: Router) {

        this.layoutSvc.layout$.subscribe(
            data => {
                let layout: Layout = data;
                this.leftMenuItems = layout.leftNavBar;
                this.subHeaders = layout.topBar.subHeaders;
                this.topMenuItems = layout.topBar.headerMenus;

                if(this.hasLayout && this.subHeaders != null && this.subHeaders !== undefined) {
                    document.getElementById('main-content').classList.add('withInfoBar');
                }

            }
        );

        this._pageSvc.config$.subscribe(result => {
            let page: Page = result;
            if (page && page.pageConfig && page.pageConfig.config) {
                // Navigate to page with pageId
                let toPage = '/h/' + page.flow + '/' + page.pageConfig.config.code;
                this._router.navigate([toPage], { relativeTo: this._route });
            }
        });

    }

    ngOnInit() {
        this._route.data.subscribe((data: { layout: string }) => {
            let layout: string = data.layout;
            if (layout) {
                this.hasLayout = true;
                this.infoClass = 'info-card';
                this.layoutSvc.getLayout(layout);
            } else {
                this.infoClass = '';
                this.hasLayout = false;
                document.getElementById('main-content').classList.remove('withInfoBar');
            }
        });
    }
}