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
import { Route } from '@angular/router';
import { Param } from './../shared/param-state';
import { MenuItem } from 'primeng/primeng';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
export enum MenuType {
    DEFAULT,
    TOP,
    LEFT,
    RIGHT
}

export interface MenuConfig extends Route {
    title?: string;
    image?: string;
    menuType: MenuType;
    children?: MenuConfig[];
}
export interface LinkConfig extends Route {
    path:string;
    title?: string;
    image?: string;
    enabled?: boolean;
    visible?: boolean;
    children?: LinkConfig[];
}

export interface AppBranding {
    logo: Param;
    title: Param;
    appTitle: Param;
    subTitle: Param;
    userName: Param;
    userRole: Param;
    settings:Param;
    help:Param;
    logOut: Param;
    numOfNotifications:Param;
    linkNotifications:Param;
}

export interface TopBarConfig {
    branding: AppBranding;
    headerMenus: Param[];
    accordions: Param[];
}

export interface FooterConfig {
    disclaimer: Param;
    links: Param[];
    sslCert: Param;
}

export interface Layout {
    fixLayout: boolean;
    topBar: TopBarConfig;
    menu: MenuItem[];
    footer: FooterConfig;
    actiontray: Param;
    modalList: Param[];
}
