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
import { AppBranding, LinkConfig } from '../../../model/menu-meta.interface';

var _uniqueId = 0;

@Component({
    selector: 'nm-header-global',
    templateUrl: './header-global.component.html'
})

export class HeaderGlobal implements OnInit {
    @Input() branding = <AppBranding>null;
    @Input() topMenuItems: Param[];
    @Input() leftMenuItems: LinkConfig[];
    @Input() element: Param;

    private label               : string;
    private messageCount        : number = 3;
    // private hasMessages         : boolean;
    private organizationList    : Array<string>;
    //private id                  : string;

    constructor(private layoutSvc: LayoutService){
        
    }

    ngOnInit(){

        this.element = new Param();
        this.element.config = new ParamConfig();
        this.element.config.visible = new RemnantState<boolean>();
        this.element.config.visible.currState = true;
        this.element.config.visible.prevState = true;
        this.element.config.code = "Organizations";
        this.element.config.uiStyles = new UiStyle();
        this.element.config.uiStyles.attributes = new UiAttribute();
        this.element.config.uiStyles.attributes.alias = "ComboBox";
        this.element.visible = new RemnantState<boolean>();
        this.element.visible.currState = true;
        this.element.visible.prevState = true;
        this.organizationList = ['organization 1', 'organization 2', 'organization 3', 'organization 4'];
        // this.hasMessages = this.messageCount > 0; 
        this.label = "Organizations";
        //this.id = "id-combobox-test";

        
        //this.elements.config.code = "Organizations";
        this.element.config.label = "Organizations";
    }

}
