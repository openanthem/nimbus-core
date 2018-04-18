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
import { LayoutService } from '../../services/layout.service';
import { Component } from '@angular/core';
import { AppBranding, Layout, LinkConfig, FooterConfig } from '../../model/menu-meta.interface';
import { Param } from '../../shared/Param';
import { AuthenticationService } from '../../services/authentication.service';
import { ServiceConstants } from '../../services/service.constants';
import { WindowRefService } from '../../services/window-ref.service';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    templateUrl: './main-layout.component.html',
    providers: [ LayoutService ]
})

export class MainLayoutCmp {
    public leftMenuItems: LinkConfig[];
    public topMenuItems: Param[];
    public branding: AppBranding;
    public footer: FooterConfig;
    public collapse: boolean = false;
    public themes: any[] = [];

    private _activeTheme = '';
    constructor(
        private _authenticationService: AuthenticationService,
        private layoutSvc: LayoutService,
        private _route: ActivatedRoute,
        private _router: Router,
        private windowRef: WindowRefService
    ) {
    }

    logout() {
        this._authenticationService.logout().subscribe(success => {
            this.windowRef.window.location.href=`${ServiceConstants.CLIENT_BASE_URL}logout`;
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