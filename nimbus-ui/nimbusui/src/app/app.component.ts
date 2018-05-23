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
 * 
 */
'use strict';
import { Component, ViewEncapsulation, Inject } from '@angular/core';
import { HostListener } from '@angular/core';
import { WebContentSvc } from './services/content-management.service';
import { ServiceConstants } from './services/service.constants';
import { LoggerService } from './services/logger.service';
import { DOCUMENT } from '@angular/platform-browser';
import * as moment from 'moment';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'app-root',
    encapsulation: ViewEncapsulation.None,
    templateUrl: './app.component.html'
})

export class AppComponent {
    navIsFixed: boolean;

    constructor(@Inject(DOCUMENT) private document: any, private _logger: LoggerService) {
    }

    @HostListener("window:scroll", [])
    onWindowScroll() {
        if (window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop > 100) {
            this.navIsFixed = true;
        } else if (this.navIsFixed && window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop < 10) { this.navIsFixed = false; } } scrollToTop() { (function smoothscroll() { var currentScroll = document.documentElement.scrollTop || document.body.scrollTop; if (currentScroll > 0) {
                //window.requestAnimationFrame(smoothscroll);
                window.scrollTo(0, 0);
            }
        })();
    }

    ngOnInit() {
        this._logger.info('AppComponent-i');
        ServiceConstants.STOPGAP_APP_HOST = this.document.location.hostname;
        ServiceConstants.STOPGAP_APP_PORT = this.document.location.port;
        ServiceConstants.APP_CONTEXT = this.document.location.pathname.split('/').splice(1, 1);
        ServiceConstants.LOCALE_LANGUAGE = "en-US"; //TODO This locale should be read dynamically. Currently defaulting to en-US
        ServiceConstants.STOPGAP_APP_PROTOCOL = this.document.location.protocol;
        const docObj = {
            message: 'all values are updated from document',
            host: ServiceConstants.STOPGAP_APP_HOST,
            port: ServiceConstants.STOPGAP_APP_PORT,
            appContext: ServiceConstants.APP_CONTEXT,
            appProtocol: ServiceConstants.STOPGAP_APP_PROTOCOL
        };
        this._logger.debug('app component: ' + JSON.stringify(docObj));
    }

}
