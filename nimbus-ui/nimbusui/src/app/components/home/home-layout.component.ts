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
import { Observable } from 'rxjs';
import { Message } from 'stompjs';
import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { LayoutService } from '../../services/layout.service';
import { AppBranding, Layout, LinkConfig, FooterConfig } from '../../model/menu-meta.interface';
import { ExecuteOutput, ModelEvent } from '../../shared/app-config.interface';
import { Param } from '../../shared/param-state';
import { AuthenticationService } from '../../services/authentication.service';
// import { STOMPService } from '../../services/stomp.service';
import { PageService } from '../../services/page.service';
import { ServiceConstants } from '../../services/service.constants';
import { MenuItem } from 'primeng/primeng';
import { LoggerService } from '../../services/logger.service';
import { WebContentSvc } from '../../services/content-management.service';
import { LabelConfig } from './../../shared/param-config';
/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    templateUrl: './home-layout.component.html'
    // providers: [ STOMPService ],
})

export class HomeLayoutCmp {
    public leftMenuItems: LinkConfig[];
    public topMenuItems: Param[];
    public branding: AppBranding;
    public footer: FooterConfig;
    public userName: any;
    items: MenuItem[];
    

    public organizations: Param[];
    public collapse: boolean = false;
    public themes: any[] = [];
    // Stream of messages
    public messages: Observable<Message>;
    public organization: Param;
    private _activeTheme = '';

    constructor(
        private _authenticationService: AuthenticationService,
        private layoutSvc: LayoutService,
        // private _stompService: STOMPService,
        private _pageSvc: PageService,
        private _route: ActivatedRoute,
        private _router: Router,
        private _logger: LoggerService,
        private wcs: WebContentSvc,
        private titleService: Title) {
        
    }

    logout() {
        this._logger.info('home layout component: logout() method is called');
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

    /** Initialize the WebSocket */
    // initWebSocket() {
    //     this._stompService.configure();
    //     this._stompService.try_connect().then(this.on_connect).catch((err) => {
    //         const errObj = {
    //             message: 'error in intializing the webSocket',
    //             error: err
    //         };
    //         this._logger.error(JSON.stringify(errObj));
    //       });
    // }

    /** Cleanup on Destroy of Component */
    // ngOnDestroy() {
    //     this._stompService.disconnect();
    // }

    /** Callback on_connect to queue */
    // public on_connect = () => {

    //     // Store local reference to Observable
    //     // for use with template ( | async )
    //     this.messages = this._stompService.messages;
    //     // Subscribe a function to be run on_next message
    //     this.messages.subscribe(this.on_next);
    // }

    /** Consume a message from the _stompService */
    public on_next = (message: Message) => {
        let executeOutput : ExecuteOutput = JSON.parse(message.body);
        let outputModel : any = executeOutput.result;
        let count = 0;
        let _p = this;
        //let foundMatchInCurrentPage: boolean = false;
        //let ROOTNODE = 0;
        // loop thru all the results and process 1 by 1.
        while(outputModel[count]) {
            let eventModel : ModelEvent = outputModel[count].result;
            // Find flow name from eventmodel result path
            let flowName = eventModel.value.path.split('/')[1];
            _p._pageSvc.traverseFlowConfig(eventModel, flowName);

            count ++;
        }
    }

    ngOnInit() {
        this._logger.debug('HomeLayoutCmp-i');
        // initialize
        this.themes.push({link:'styles/vendor/anthem.blue.theme.css',label:'Blue Theme'});
        this.themes.push({link:'styles/vendor/anthem.black.theme.css',label:'Black Theme'});

        this.layoutSvc.layout$.subscribe(
            data => {
                let layout: Layout = data;
                this._logger.debug('home layout component received layout from layout$ subject');
                if(layout != null ) {
                    

                    if(layout.topBar != null && layout.topBar.branding != null) {
                        this.branding = layout.topBar.branding;
                        if (this.branding.title) {
                            let titleLabel: LabelConfig = this.wcs.findLabelContent(this.branding.title);
                            this.titleService.setTitle(titleLabel.text);    
                        }
                        this.topMenuItems = layout.topBar.headerMenus;
                    }
                    this.items = layout.menu;
                    this.footer = layout.footer;
                }
                //this._router.navigate([this.body['defaultFlow']], { relativeTo: this._route });
            }
        );
        if(this._route.data['value']['layout'] != null) {
            this.layoutSvc.getLayout(this._route.data['value']['layout']);
        }

        //        this.initWebSocket();
    }

}
