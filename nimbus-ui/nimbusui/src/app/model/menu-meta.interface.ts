import { Route } from '@angular/router';
import { Param } from './../shared/app-config.interface';
import {MenuItem} from 'primeng/primeng';

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
    settings:Param;
    help:Param;
    logOut: Param;
    numOfNotifications:Param;
    linkNotifications:Param;
}

export interface TopBarConfig {
    branding: AppBranding;
    headerMenus: Param[];
    subHeaders: Param[];
}

//TODO: Determine the strategy for global nav

export interface GlobalNavConfig {
    organization: Param;
    menus: MenuItem[][];
}

export interface GlobalNavConfig {
       organization: Param;
       menuItems: Map<string, Param[]>;
       menuLinks: Param[];
}

export interface FooterConfig {
    disclaimer: Param;
    links: Param[];
    sslCert: Param;
}

export interface Layout {
    topBar: TopBarConfig;
    leftNavBar: LinkConfig[];
    footer: FooterConfig;
    subBar: GlobalNavConfig;
}
