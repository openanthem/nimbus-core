import { ActivatedRoute, Router } from '@angular/router';
import { Component } from '@angular/core';
import { LayoutService } from '../../services/layout.service';
import { PageService } from '../../services/page.service';
import { Layout, LinkConfig } from '../../model/menu-meta.interface';
import { Param, Page } from '../../shared/app-config.interface';

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
