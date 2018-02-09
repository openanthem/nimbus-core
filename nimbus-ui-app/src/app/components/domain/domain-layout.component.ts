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

                if(this.hasLayout && this.subHeaders != null && this.subHeaders !== undefined) {
                    document.getElementById('main-content').classList.add('withInfoBar');
                }
            }
        );

        if(this._route.data['value']['layout']) {
            this.layoutSvc.getLayout(this._route.data['value']['layout']);
        }
    }
}
