/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { ActivatedRoute, Router } from '@angular/router';
import { LayoutService } from '../../services/layout.service';
import { Component } from '@angular/core';
import { AppBranding, Layout, LinkConfig, FooterConfig, BodyConfig } from '../../model/menu-meta.interface';
import { Param } from '../../shared/app-config.interface';
import { AuthenticationService } from '../../services/authentication.service';
import { ServiceConstants } from '../../services/service.constants';


@Component({
    templateUrl: './main-layout.component.html',
    providers: [ LayoutService ]
})

export class MainLayoutCmp {
    public leftMenuItems: LinkConfig[];
    public topMenuItems: Param[];
    public branding: AppBranding;
    public footer: FooterConfig;
    public body: BodyConfig;
    public collapse: boolean = false;
    public themes: any[] = [];

    private _activeTheme = '';
    constructor(
        private _authenticationService: AuthenticationService,
        private layoutSvc: LayoutService,
        private _route: ActivatedRoute,
        private _router: Router) {

        // initialize
        this.themes.push({link:'styles/vendor/anthem.blue.theme.css',label:'Blue Theme'});
        this.themes.push({link:'styles/vendor/anthem.black.theme.css',label:'Black Theme'});

        this.layoutSvc.layout$.subscribe(
            data => {
                let layout: Layout = data;
                if(layout != null ) {
                    if(layout.topBar != null && layout.topBar.branding != null) {
                        this.branding = {} as AppBranding;
                        this.branding = layout.topBar.branding;
                        this.topMenuItems = layout.topBar.headerMenus;
                    }
                    this.leftMenuItems = layout.leftNavBar;
                    this.footer = layout.footer;
                    this.body = layout.body;
                }
                //this._router.navigate([this.body['defaultFlow']], { relativeTo: this._route });
            }
        );
        if(this._route.data['value']['layout'] != null) {
            this.layoutSvc.getLayout(this._route.data['value']['layout']);
        }
    }

    logout() {
        this._authenticationService.logout().subscribe(success => {
            window.location.href=`${ServiceConstants.CLIENT_BASE_URL}logout`;
        });
    }

    // Side navigation bar toggle effect
    toggelSideNav() {
        this.collapse = !this.collapse;
    }

    get activeTheme(): string {
        return this._activeTheme;
    }

    set activeTheme(value: string) {
        if (this._activeTheme !== value) {
            this._activeTheme = value;
            let themeLink = <HTMLLinkElement>document.getElementById('activeThemeLink');
            if (themeLink) {
                themeLink.href = value;
            }
        }
    }

}
