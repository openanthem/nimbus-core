import { URLUtils } from './../shared/url-utils';
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
import { WebContentSvc } from './content-management.service';
import { Component, EventEmitter, Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import { Result, ViewRoot } from '../shared/app-config.interface';
import { UiAttribute } from '../shared/param-config';
import { Param, Model } from '../shared/param-state';
import { ServiceConstants } from './service.constants';
import { PageService } from './page.service';
import { ConfigService } from './config.service';
import { CustomHttpClient } from './httpclient.service';
import { AppBranding, Layout, TopBarConfig, FooterConfig } from '../model/menu-meta.interface';
import { GenericDomain } from '../model/generic-domain.model';
import { ViewComponent } from './../shared/param-annotations.enum';
import { LoggerService } from './logger.service';
import { MenuItem } from '../shared/menuitem';
import { Action, Behavior } from './../shared/command.enum';
import { SessionStoreService, CUSTOM_STORAGE } from './session.store';
/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-layout',
    //conflict jit-aot,  below line has to be commented out for gulp-build
    template: '',
    providers: [
        WebContentSvc
    ]
})
@Injectable()
export class LayoutService {

    layout$: EventEmitter<any>;

    constructor(public http: CustomHttpClient, 
        private wcs: WebContentSvc,
        private pageSvc: PageService,
        private configSvc: ConfigService,
        private logger: LoggerService,  
        private router: Router,
        @Inject(CUSTOM_STORAGE) private sessionstore: SessionStoreService,
        private httpClient: HttpClient) {
        this.layout$ = new EventEmitter<any>();
    }

    public getLayout(flowName: string) {
        let layoutConfig: ViewRoot = this.configSvc.getFlowConfig(flowName);
        if (layoutConfig) {
            this.parseLayoutConfig(layoutConfig.model);
        } else {
            let flowRoodtId = this.sessionstore.get(flowName);
            var urlBase = urlBase = ServiceConstants.PLATFORM_BASE_URL;
            var urlAction;

            if(flowRoodtId != null) {
                    let rootId = flowRoodtId;
                    urlBase += rootId == null ? '/' + flowName:  '/' + flowName+':'+rootId;
                    let action = rootId!= null ? Action._get.value: Action._new.value;
                    urlAction = urlBase + '/' + action + '?b=' + Behavior.execute.value;
                    //window.location.href = `${ServiceConstants.APP_REFRESH}`;
            } 
            else {
                urlAction = urlBase +  '/' + flowName + '/_new?b=$execute';
                
            }
            return this.http.get(urlAction)
                    .subscribe(data => {
                        let subResponse: any = data.result[0];
                        if (subResponse) {
                            let layoutConfig = new Result(this.configSvc).deserialize(subResponse.result);
                            let flowModel: Model = layoutConfig.outputs[0].value.type.model;
                            // Set layout config to appconfig map
                            this.configSvc.setLayoutToAppConfigByModel(flowName, flowModel);
                            this.sessionstore.set(flowName, layoutConfig.rootDomainId);
                            this.parseLayoutConfig(flowModel);
                        } else {
                            this.logger.error('ERROR: Unknown response for Layout config call - ' + subResponse.b);
                        }
                    },
                        err => {this.logger.error(err)},
                        () => {this.logger.info('Layout config call completed..');}
                    );
        }
    }

    public parseLayoutConfig(flowModel: Model) {
        let layout = {} as Layout;
        const pageParam: Param = flowModel.params.find (p => ( p.config &&
                                    p.config.uiStyles && p.config.uiStyles.attributes && 
                                    p.config.uiStyles.attributes.alias === ViewComponent.page.toString()));
        layout['fixLayout'] = pageParam.config.uiStyles.attributes.fixLayout;
        layout['menu'] = this.getMenu(pageParam.type.model);
        layout['topBar'] = this.getTopBar(pageParam.type.model);
        layout['footer'] = this.getFooterItems(pageParam.type.model);
        layout['modalList'] = this.getModalItems(pageParam.type.model);
        layout['actiontray'] = this.getActionTrayItems(pageParam.type.model);
        // Push the new menu into the Observable stream
        this.layout$.next(layout);
    }

    private getFooterItems(layoutConfig: Model) {
        let footerConfig = {} as FooterConfig;
        let elemetarry:any = [];
        layoutConfig.params.forEach(param => {
            if (param.config.uiStyles.attributes.alias === 'Footer') {
                // if (param.config.uiStyles.attributes.value === 'FOOTER') {
                    param.type.model.params.forEach(element => {

                        
                        element.config.uiNatures.forEach(nature => {
                            if (nature.name === 'ViewConfig.FooterProperty') {
                                if (nature.attributes.value === 'DISCLAIMER') {
                                    footerConfig['disclaimer'] = element;
                                }
                                 if(nature.attributes.value === 'LINK') {
                                //     footerConfig.links.push(element);
                                    elemetarry.push(element);
                                }
                                if (nature.attributes.value === 'SSLCERT') {
                                    footerConfig['sslCert'] = element;
                                }
                            }
                        });
                    });
                // }
            }
        });
        if(elemetarry.length !=0)
        footerConfig['links'] = elemetarry;
        return footerConfig;
    }

    private getTopBar(layoutConfig: Model) {
        let topBar = {} as TopBarConfig;
        let branding = {} as AppBranding;
        let headerMenus: Param[] = [];
        let accordions: Param[] = [];

        layoutConfig.params.forEach(param => {
            let attribute: UiAttribute = param.config.uiStyles.attributes;
            if (attribute.alias === 'Header' || attribute.alias === 'Global-Header' ||
                (attribute.alias === 'Section' && attribute.value === 'HEADER')) {
                this.parseTopBarConfig(param.type.model, branding, headerMenus, accordions);
                // if param has initialize, execute the config
                if (param.config && param.config.initializeComponent()) { 
                        this.pageSvc.processEvent(param.path, '$execute', new GenericDomain(), 'POST'); 
                }
            }     
        });

        topBar['branding'] = branding;
        topBar['headerMenus'] = headerMenus;
        topBar['accordions'] = accordions;
        return topBar;
    }

    private parseTopBarConfig(topBarConfig: Model, branding: AppBranding, headerMenus: Param[], accordions: Param[]) {
        topBarConfig.params.forEach(element => {
            if (element.config.type.nested === true && element.config.uiNatures.length == 0) {
                if (element.config.uiStyles && element.config.uiStyles.attributes.alias === 'Accordion') {
                    accordions.push(element);
                } else {
                    this.parseTopBarConfig(element.type.model, branding, headerMenus, accordions);
                }
            } else {
                element.config.uiNatures.forEach(nature => {
                    if (nature.name === 'ViewConfig.PageHeader') {
                        if (nature.attributes.value === 'LOGO') {
                            branding['logo'] = element;
                        }
                        if (nature.attributes.value === 'TITLE') {
                            branding['title'] = element;
                        }
                        if (nature.attributes.value === 'APPTITLE') {
                            branding['appTitle'] = element;
                        }
                        if (nature.attributes.value === 'SUBTITLE') {
                            branding['subTitle'] = element;
                        }
                        if (nature.attributes.value === 'USERNAME') {
                            branding['userName'] = element;
                        }
                        if (nature.attributes.value === 'USERROLE') {
                            branding['userRole'] = element;
                        }
                        if (nature.attributes.value === 'HELP') {
                            branding['help'] = element;
                        }
                        if (nature.attributes.value === 'LOGOUT') {
                            branding['logOut'] = element;
                        }
                        if (nature.attributes.value === 'NOTIFICATIONS') {
                            branding['linkNotifications'] = element;
                        }
                        if (nature.attributes.value === 'NUMBEROFNOTIFICATIONS') {
                            branding['numOfNotifications'] = element;
                        }
                        if (nature.attributes.value === 'MENU') {
                            headerMenus.push(element);
                        }
                    }
                });
            }
        });
    }

    private getMenu(layoutConfig: Model) {
        let menuItems : MenuItem[] = [];
        layoutConfig.params.forEach(param => {
            if(param.config.uiStyles.attributes.alias === ViewComponent.menupanel.toString()) {
                this.buildMenu(param, menuItems); 
            }
        });

        return menuItems;
    }

    private buildMenu(param: Param,  menuItems : MenuItem[]) {
        param.type.model.params.forEach(element => {
            let menuItem = this.createMenuItem(element);
            if (element.config.uiStyles.attributes.alias=== ViewComponent.menulink.toString()) {
                menuItem.routerLink =  this.createRouterLink(element);
                menuItem.command = (event: Event) => { this.processClick(event, menuItem)};
                menuItem.code = element.config.code;
               // menuItem.routerLinkActiveOptions = {'exact':true};
                menuItems.push(menuItem);
            } else if (element.config.uiStyles.attributes.alias ===ViewComponent.menupanel.toString()){
                this.buildSubMenu(element, menuItem);
                menuItems.push(menuItem);
            }
        });
    }

    private buildSubMenu(param: Param,  menuItem: MenuItem) {
        let subMenuItems: MenuItem[] = [];
        if (!param.type.model || !param.type.model.params) {
            return subMenuItems;
        }
        param.type.model.params.forEach(element => {
            let item = this.createMenuItem(element);
            if (element.config.uiStyles.attributes.alias === ViewComponent.menulink.toString()) {
                item.command = (event: Event) => { this.processClick(event, item)};
                item.routerLink =  this.createRouterLink(element, true);
                subMenuItems.push(item);
            } else if (element.config.uiStyles.attributes.alias ===ViewComponent.menupanel.toString()){
                this.buildSubMenu(element, item);
                subMenuItems.push(item);
            }
        });
        menuItem.items = subMenuItems;
    }

    createMenuItem(element: Param): MenuItem {
        let item = {} as MenuItem;        
        item.label = this.wcs.findLabelContent(element).text;
        item.path = element.path;
        item.page = element.config.uiStyles.attributes.page;
        item.icon = element.config.uiStyles.attributes.imgSrc;
        item.imgType = element.config.uiStyles.attributes.imgType;
        item.url = element.config.uiStyles.attributes.url;
        item.type = element.config.uiStyles.attributes.type;
        item.target = element.config.uiStyles.attributes.target;
        item.rel = element.config.uiStyles.attributes.rel;
        item.visible = element.visible;
        return item;
    }

    createRouterLink(element: Param, nested?: boolean): string {
        let uiAttribute = element.config.uiStyles.attributes;
        let routeLink = "";
        if(uiAttribute.url != null && uiAttribute.url != "") {
            if(nested)
                routeLink = uiAttribute.url;
            else
                routeLink = '/h/'+uiAttribute.url;
        }

        return routeLink;
    }

    processClick(event: Event, item: MenuItem) {
        if(item.routerLink === "" || item.routerLink === null || typeof item.routerLink === 'undefined') {
            if (item.page == URLUtils.getDomainPage(this.router.url)) {
                return;
            }
            this.pageSvc.processEvent(item.path, '$execute', new GenericDomain(), 'GET')
        }
    }

    private getActionTrayItems(layoutConfig: Model): Param {

        const actionTrayParam: Param = layoutConfig.params.find( param => ( param.config.uiStyles != null && 
                                                param.config.uiStyles.attributes.alias === ViewComponent.actiontray.toString()));

        return actionTrayParam;
    }


    private getModalItems(layoutConfig: Model): any {
        let modalList: Array<Param> =[];
        layoutConfig.params.forEach(((param) => {
            if(param.config.uiStyles && param.config.uiStyles.attributes.alias === ViewComponent.modal.toString()){
                modalList.push(param);
            }
        }))

        return modalList;
    }

}
