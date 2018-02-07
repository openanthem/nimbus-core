import { Component, Input } from '@angular/core';
import { LayoutService } from '../../../services/layout.service';
import { Param, Desc, ParamConfig, UiStyle, UiAttribute, RemnantState } from '../../../shared/app-config.interface';
import { AppBranding, LinkConfig } from '../../../model/menu-meta.interface';

@Component({
    selector: 'nm-header-global',
    templateUrl: './header-global.component.html'
})

export class HeaderGlobal{
    @Input() branding : AppBranding;
    @Input() topMenuItems: Param[];
    @Input() leftMenuItems: LinkConfig[];
    @Input() element: Param;

    private messageCount        : number = 3;

    constructor(){
        
    }

}

