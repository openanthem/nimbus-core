/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Component, Input, OnInit } from '@angular/core';
import { LayoutService } from '../../../services/layout.service';
import { Param, Desc, ParamConfig, UiStyle, UiAttribute, RemnantState } from '../../../shared/app-config.interface';
import { AppBranding, LinkConfig, TopBarConfig } from '../../../model/menu-meta.interface';

@Component({
    selector: 'nm-header-global',
    templateUrl: './header-global.component.html'
})

export class HeaderGlobal implements OnInit {
    // @Input() topBar : TopBarConfig;
    @Input() branding : AppBranding;
    @Input() topMenuItems: Param[];
    @Input() leftMenuItems: LinkConfig[];
    @Input() element: Param;


    private label               : string;
    private messageCount        : number = 3;
    // private hasMessages         : boolean;
    private organizationList    : Array<string>;
    //private id                  : string;

    constructor(){
        
    }

    ngOnInit(){
// console.log(this.branding.userName.leafState);
      
    }

}
