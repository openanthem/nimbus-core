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
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { Component, ChangeDetectorRef } from '@angular/core';
import { LocationStrategy } from '@angular/common';
import { ExecuteException } from '../../../shared/app-config.interface';
import { Param } from '../../../shared/param-state';
import { BaseElement } from '../base-element.component';
import { WebContentSvc } from './../../../services/content-management.service';
import { PageService } from '../../../services/page.service';
import { LoggerService } from '../../../services/logger.service';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-page-content',
    templateUrl: './page-content.component.html',
    providers: [
        WebContentSvc
    ],
})

export class PageContent extends BaseElement{
    pageId: string;
    tilesList: any[];
    isPopState: boolean = false;
    labelSize: string;
    position: number = 0;

    constructor(private router: Router, private route: ActivatedRoute, 
        private _wcs : WebContentSvc, 
        private pageSvc: PageService,
        private cd: ChangeDetectorRef,
        private ls: LocationStrategy,
        private _logger: LoggerService
    ) {
        super(_wcs);
        this.ls.onPopState(() => {
            this.isPopState = true;
        });
        this.router.events.subscribe(event => {
            this.pageId = this.route.snapshot.url[0].path;
            // Scroll to the TOP of the page
            if (event instanceof NavigationEnd && !this.isPopState) {
                window.scrollTo(0, 0);
                this.isPopState = false;
            }
            if (event instanceof NavigationEnd) {
                this.isPopState = false;
            }
        });
    }

    ngOnInit() {
        this._logger.debug('PageContent - i ' + this.pageId);
        this.route.data.subscribe((data: { page: Param }) => {
            let page : Param = data.page;
            this.element = page;
            this.loadLabelConfig(this.element);
            this.tilesList = [];
            if(page.type.model != null) {
                page.type.model.params.forEach(element => {
                    if(element.config.uiStyles.attributes.alias === 'Tile') {
                        this.tilesList.push(element);
                    }
                });
            }
        });
        this.updateIntialPosition();        
    }

    updateIntialPosition() {
        if (this.label && this.element.labels) {
            this.position = 1;
            this.labelSize = this.getHeaderSize(this.position);
        }
    }

}
