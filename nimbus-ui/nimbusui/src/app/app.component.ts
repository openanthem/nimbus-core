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
import { HostListener } from '@angular/core/src/metadata/directives';
import { WebContentSvc } from './services/content-management.service';
import { ServiceConstants } from './services/service.constants';
import { Router, ActivatedRoute } from '@angular/router';
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
    domain: string;
    port : string;
    protocol: string;
    appcontext: string;
    locale : string;
    label: string;
    private collapse: boolean = false;

    constructor(private router: Router,
            private activatedRoute: ActivatedRoute,
            @Inject(DOCUMENT) private document: any) {
    }

   ngOnInit() {

        this.domain = this.document.location.hostname;
        this.port=this.document.location.port;
        this.protocol=this.document.location.protocol;
        this.appcontext=this.document.location.pathname.split('/').splice(1, 1);
        this.locale = "en-US"; //TODO This locale should be read dynamically. Currently defaulting to en-US
        ServiceConstants.STOPGAP_APP_HOST = this.domain;
        ServiceConstants.STOPGAP_APP_PORT = this.port;
        ServiceConstants.APP_CONTEXT = this.appcontext;
        ServiceConstants.LOCALE_LANGUAGE = this.locale;
        ServiceConstants.STOPGAP_APP_PROTOCOL = this.protocol;
    }

}
