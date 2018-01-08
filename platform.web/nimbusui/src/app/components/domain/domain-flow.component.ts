/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { ActivatedRoute, Router } from '@angular/router';
import { Component } from '@angular/core';
import { LayoutService } from '../../services/layout.service';
import { PageService } from '../../services/page.service';
import { Layout, LinkConfig } from '../../model/menu-meta.interface';
import { Param, Page } from '../../shared/app-config.interface';
import { WebContentSvc } from '../../services/content-management.service';

@Component({
    templateUrl: './domain-flow.component.html',
    providers: [ LayoutService, WebContentSvc ]
})

export class DomainFlowCmp {
    public hasLayout: boolean = true;
    public infoClass: string = '';
    public leftMenuItems: LinkConfig[];
    public topMenuItems: Param[];
    public subHeaders: Param[];
    routeParams: any;

    constructor(private _pageSvc: PageService, private layoutSvc: LayoutService,
            private _route: ActivatedRoute, private _router: Router, private wcs: WebContentSvc) {

        this.layoutSvc.layout$.subscribe(
            data => {
                let layout: Layout = data;
                this.leftMenuItems = layout.leftNavBar;
                this.subHeaders = layout.topBar.subHeaders;
                this.topMenuItems = layout.topBar.headerMenus;


                if(this.leftMenuItems != null && this.leftMenuItems !== undefined) {
                    this.leftMenuItems.forEach(item => {
                            if(item != null) {
                                this.wcs.getContent(item.title);
                            }
                        });
                }
                if(this.hasLayout && this.subHeaders != null && this.subHeaders !== undefined) {
                    document.getElementById('main-content').classList.add('withInfoBar');
                }

            }
        );

        wcs.content$.subscribe(result => {
            if (this.leftMenuItems) {
                this.leftMenuItems.forEach(item => {
                    if(item != null && item.title === result.id) {
                        item.title = result.label;
                    }
                });
            }
        });

        this._pageSvc.config$.subscribe(result => {
            let page: Page = result;
            // Navigate to page with pageId
            let toPage = '/h/' + page.flow + '/' + page.pageConfig.config.code;
            this._router.navigate([toPage], { relativeTo: this._route });
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
