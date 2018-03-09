import { Component } from '@angular/core';
import { AppBranding, Layout, FooterConfig } from '../../model/menu-meta.interface';
import { Param } from '../../shared/app-config.interface';
import { LayoutService } from '../../services/layout.service';

@Component({
    templateUrl: './login-layout.component.html',
    providers: [ LayoutService ]
})

export class LoginLayoutCmp {
    private static LAYOUT: string = 'loginlayout';
    public topMenuItems: Param[];
    public branding: AppBranding;
    public footer: FooterConfig;

    constructor(private layoutSvc: LayoutService) {
        // initialize
        this.branding = {} as AppBranding;

        this.layoutSvc.layout$.subscribe(
            data => {
                let layout: Layout = data;
                this.branding = layout.topBar.branding;
                this.footer = layout.footer;
                this.topMenuItems = layout.topBar.headerMenus;
            }
        );
        this.layoutSvc.getLayout(LoginLayoutCmp.LAYOUT);
    }
}
