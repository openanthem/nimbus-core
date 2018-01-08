/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { Route } from '@angular/router';
import { Param } from './../shared/app-config.interface';

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
    title?: string;
    image?: string;
    children?: LinkConfig[];
}

export interface AppBranding {
    logo: Param;
    title: Param;
    subTitle: Param;
    userName: Param;
    userRole: Param;
    help:Param;
    logOut: Param;
    numOfNotifications:Param;
    linkNotifications:Param;
}

export interface TopBarConfig {
    branding: AppBranding;
    headerMenus: Param[];
    subHeaders: Param[];
    organizations: Param[];
}

export interface FooterConfig {
    disclaimer: Param;
    links: Param[];
    sslCert: Param;
}

export interface BodyConfig {
    defaultFlow: String;
}

export interface Layout {
    topBar: TopBarConfig;
    leftNavBar: LinkConfig[];
    footer: FooterConfig;
    body: BodyConfig;
}
