/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { ActivatedRoute, Router } from '@angular/router';
import { LayoutService } from '../../services/layout.service';
import { Component } from '@angular/core';
import { Layout, LinkConfig } from '../../model/menu-meta.interface';
import { Param } from '../../shared/app-config.interface';
import { WebContentSvc } from '../../services/content-management.service';


@Component({
    templateUrl: './domain-layout.component.html',
    providers: [ LayoutService,WebContentSvc ]
})

export class DomainLayoutCmp {
    public hasLayout: boolean = true;
    public leftMenuItems: LinkConfig[];
    public topMenuItems: Param[];
    public subHeaders: Param[];

    constructor(private layoutSvc: LayoutService, private _route: ActivatedRoute, private _router: Router, private wcs: WebContentSvc) {
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
            if(this.leftMenuItems !== null && this.leftMenuItems !== undefined) {
                this.leftMenuItems.forEach(item => {
                    if(item != null && item.title === result.id) {
                        item.title = result.label;
                    }
                });
            }
        });
        if(this._route.data['value']['layout']) {
            this.layoutSvc.getLayout(this._route.data['value']['layout']);
        }
    }
}
