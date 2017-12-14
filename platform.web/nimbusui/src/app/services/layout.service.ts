import { WebContentSvc } from './content-management.service';
import { Component, EventEmitter, Injectable } from '@angular/core';

import { Model, Param, Result } from '../shared/app-config.interface';
import { ServiceConstants } from './service.constants';
import { PageService } from './page.service';
import { HttpClient } from './httpclient.service';
import { AppBranding, Layout, LinkConfig, TopBarConfig, FooterConfig, BodyConfig } from '../model/menu-meta.interface';

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

    constructor(public http: HttpClient, private wcs: WebContentSvc, private pageSvc: PageService) {
        this.layout$ = new EventEmitter<any>();
    }

    public getLayout(flowName: string) {
        let layoutConfig: Model = this.pageSvc.getFlowConfig(flowName);
        if (layoutConfig) {
            this.parseLayoutConfig(layoutConfig);
        } else {
            var urlBase = ServiceConstants.PLATFORM_BASE_URL;
            var urlAction = urlBase +  '/' + flowName + '/_new?b=$execute';
            return this.http.get(urlAction)
                .map(res => res.json())
                .subscribe(data => {
                    let subResponse: any = data.result[0];
                    if (subResponse) {
                        let layoutConfig = new Result().deserialize(subResponse.result);
                        let flowModel: Model = layoutConfig.outputs[0].value.type.model;
                        // Set layout config to appconfig map
                        this.pageSvc.setLayoutToAppConfig(flowName, flowModel);

                        this.parseLayoutConfig(flowModel);
                        //console.log(flowModel);
                    } else {
                        console.log('ERROR: Unknown response for Layout config call - ' + subResponse.b);
                    }
                },
                    err => console.log(err),
                    () => console.log('Layout config call completed..')
                );
        }
    }

    private parseLayoutConfig(flowModel: Model) {
        let layout = {} as Layout;
        layout['leftNavBar'] = this.getLeftMenu(flowModel.params[0].type.model);
        layout['topBar'] = this.getTopBar(flowModel.params[0].type.model);
        layout['footer'] = this.getFooterItems(flowModel.params[0].type.model);
        layout['body'] = this.getBodyItems(flowModel.params[0].type.model);
        // Push the new menu into the Observable stream
        this.layout$.next(layout);
    }

    private getBodyItems(layoutConfig: Model) {
        let bodyConfig = {} as BodyConfig;
        layoutConfig.params.forEach(param => {
            if (param.config.uiStyles.attributes.alias === 'Section') {
                if (param.config.uiStyles.attributes.value === 'BODY') {
                    if (param.config.uiStyles.attributes.defaultFlow) {
                        bodyConfig['defaultFlow'] = param.config.uiStyles.attributes.defaultFlow;
                    } else {
                        bodyConfig['defaultFlow'] = 'pageNotFound'; // TODO implement pagenotfound (404)
                    }
                }
            }
        });
        return bodyConfig;
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
        let subHeaders: Param[] = [];

        layoutConfig.params.forEach(param => {
            if (param.config.uiStyles.attributes.alias === 'Header') {
                // if (param.config.uiStyles.attributes.value === 'HEADER') {
                    this.parseTopBarConfig(param.type.model, branding, headerMenus, subHeaders);
                // }
            }
        });

        topBar['branding'] = branding;
        topBar['headerMenus'] = headerMenus;
        topBar['subHeaders'] = subHeaders;
        return topBar;
    }

    private parseTopBarConfig(topBarConfig: Model, branding: AppBranding, headerMenus: Param[], subHeaders: Param[]) {
        topBarConfig.params.forEach(element => {
            if (element.type.nested === true) {
                if (element.config.uiStyles !== undefined && element.config.uiStyles.attributes.alias === 'SubHeader') {
                    subHeaders.push(element);
                } else {
                    this.parseTopBarConfig(element.type.model, branding, headerMenus, subHeaders);
                }
            } else {
                element.config.uiNatures.forEach(nature => {
                    if (nature.name === 'ViewConfig.PageHeader') {
                        if (nature.attributes.value === 'LOGO') {
                            branding['logo'] = element;
                        }
                        if (nature.attributes.value === 'APPTITLE') {
                            branding['title'] = element;
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

    private getLeftMenu(layoutConfig: Model) {
        let leftMenu : LinkConfig[] = [];
        layoutConfig.params.forEach(param => {
            if (param.config.uiStyles.attributes.alias === 'Section') {
                if (param.config.uiStyles.attributes.value ==='LEFTBAR') {
                    param.type.model.params.forEach(element => { // look for links and add to left menu
                        if (element.config.uiStyles.name === 'ViewConfig.Link') {
                            let navItem = {} as LinkConfig;
                            navItem['path'] = element.config.uiStyles.attributes.url;
                            navItem['title'] = element.config.code;
                            navItem['image'] = element.config.uiStyles.attributes.imgSrc;
                            leftMenu.push(navItem);
                        }
                    });
                }
            }
        });

        return leftMenu;
    }

}
