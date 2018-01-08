/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

'use strict';

import { Component, Input } from '@angular/core';
import { WebContentSvc } from '../../../services/content-management.service';
import { Param } from '../../../shared/app-config.interface';

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

export class FieldValue {
    @Input() element: Param;
    public label: string;

    constructor(private wcs: WebContentSvc) {
        wcs.content$.subscribe(result => {
            this.label = result.label;
        });
    }

    ngOnInit() {
        this.wcs.getContent(this.element.config.code);
    }

}

