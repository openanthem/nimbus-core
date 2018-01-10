/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
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
