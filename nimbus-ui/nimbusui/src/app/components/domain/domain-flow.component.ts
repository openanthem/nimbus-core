
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
import { Component, HostListener } from '@angular/core';
import { LayoutService } from '../../services/layout.service';
import { PageService } from '../../services/page.service';
import { Layout, LinkConfig } from '../../model/menu-meta.interface';
import { Page } from '../../shared/app-config.interface';
import { Param } from '../../shared/param-state';
import { LoggerService } from '../../services/logger.service';
import { MenuItem } from '../../shared/menuitem';
import { Message } from './../../shared/message';
import { ViewRoot } from './../../shared/app-config.interface';
import { NmMessageService } from './../../services/toastmessage.service';
/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    templateUrl: './domain-flow.component.html',
    providers: [ LayoutService ]
})

export class DomainFlowCmp {
    public hasLayout: boolean = true;
    public fixLayout: boolean = false;
    public infoClass: string = '';
    public leftMenuItems: LinkConfig[];
    public topMenuItems: Param[];
    public accordions: Param[];
    public actionTray: Param;
    public modalItems: Param[];
    public _showActionTray: boolean;
    public messages: Message[];
    items: MenuItem[];
    routeParams: any;

    constructor(private _pageSvc: PageService, private layoutSvc: LayoutService,
            private _route: ActivatedRoute, private _router: Router, private _logger: LoggerService, private _messageservice: NmMessageService) {

        this.layoutSvc.layout$.subscribe(
            data => {
                let layout: Layout = data;
                this.fixLayout = layout['fixLayout'];
                this.accordions = layout.topBar.accordions;
                this.items = layout.menu;
                this.topMenuItems = layout.topBar.headerMenus;
                this.actionTray = layout.actiontray;
                this.modalItems = layout.modalList;
               
                this._logger.debug('domain flow component received layout from layout$ subject');
                if(this.hasLayout && this.accordions != null && this.accordions !== undefined) {
                    this.getDocument().getElementById('main-content').classList.add('withInfoBar');
                }

                this.setLayoutScroll();
            }
        );

        this._pageSvc.config$.subscribe(result => {
            let page: Page = result;
            this._logger.debug('domain flow component received page from config$ subject');
            if (page && page.pageConfig && page.pageConfig.config) {
                // Navigate to page with pageId
                let toPage = '';
                // if(page.flow == "notesview") {
                //     toPage = './' + page.flow + '/' + page.pageConfig.config.code;
                // } else {
                    toPage = '/h/' + page.flow + '/' + page.pageConfig.config.code;
                //}
                this._logger.debug('domain flow component will be navigated to ' + toPage + ' route');
                this._router.navigate([toPage], { relativeTo: this._route });
            }
        });

        this._pageSvc.subdomainconfig$.subscribe(result => {
            let page: Page = result;
            this._logger.debug('domain flow component received page from config$ subject');
            if (page && page.pageConfig && page.pageConfig.config) {
                // Navigate to page with pageId
                let toPage = './' +  page.flow + '/' + page.pageConfig.config.code;
                this._logger.debug('sub domain flow component will be navigated to ' + toPage + ' route');
                this._router.navigate([toPage], { relativeTo: this._route });
            }
        });
    }

    getDocument() {
        return document;
    }

    /** Set the layout to fixed or scrollable based on the config param - fixLayout */
    setLayoutScroll() {
        if (this.fixLayout) {
            this.getDocument().body.classList.remove("browserScroll");
            this.getDocument().body.classList.add("browserFixed");
            this.resetInfoCardScrollHeight();
        } else {
            this.getDocument().body.classList.remove("browserFixed");
            this.getDocument().body.classList.add("browserScroll");
            this.getDocument().getElementById('page-content').style.height = 'auto';
        }
    }

    /** Calculate the scroll height everytime the view changes */
    resetInfoCardScrollHeight() {
        if (this.fixLayout) {
            var target = this.getDocument().getElementById('page-content');
            var h = target.getBoundingClientRect().top;
            h = window.innerHeight - h - 50; // 50 is padding below viewport
            target.style.height = h + 'px';    
        }
    }

    ngOnInit() {
        this._logger.debug('DomainFlowCmp-i ');
        this._route.data.subscribe((data: { layout: ViewRoot }) => {
            let viewRoot: ViewRoot = data.layout;
            if (viewRoot && viewRoot.layout) {
                this.hasLayout = true;
                this.infoClass = 'info-card page-content';
                this.layoutSvc.getLayout(viewRoot.layout);
            } else {
                this.infoClass = 'page-content';
                this.hasLayout = false;
                this.fixLayout = false;
                this.setLayoutScroll();
                this.getDocument().getElementById('main-content').classList.remove('withInfoBar');
            }
        });
    }

    /** 
     * Recalculate the scroll height everytime the view changes. 
     * This can happen when elements are hidden/visible based on rules.
     */
    ngAfterViewChecked() {
        this.resetInfoCardScrollHeight();
    }

    /** 
     * Recalculate the scroll height everytime the window is resized.
     */
    @HostListener('window:resize', ['$event'])
    onResize(event) {
        this.resetInfoCardScrollHeight();
    }

    @HostListener("scroll", ['$event'])
    onPageContentScroll(event) {
        if (this.getDocument().getElementById('page-content').scrollTop >= 10) {
            this.getDocument().getElementById('scroll-div-to-top').setAttribute("style", "opacity:1; bottom:50px;")
        } else if (this.getDocument().getElementById('page-content').scrollTop < 10) {
            this.getDocument().getElementById('scroll-div-to-top').setAttribute("style", "opacity:0; bottom:-50px;")
        }
    }
}