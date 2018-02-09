'use strict';

import { Component, Input } from '@angular/core';
import { WebContentSvc } from '../../../services/content-management.service';
import { Param } from '../../../shared/app-config.interface';
import { BaseElement } from '../base-element.component';

@Component({
    selector: 'nm-field-value',
    providers: [WebContentSvc],
    template: `
    <!--<div [hidden]="!element?.config?.visible?.currState" >-->
    <div [hidden]="!element?.visible?.currState" >
        <label>{{label}}</label>
        <p style="margin-bottom:0rem;">{{element.leafState}}</p>
    </div>
   `
})

export class FieldValue extends BaseElement{
    @Input() element: Param;
    constructor(private _wcs: WebContentSvc) {
        super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();
    }

}

