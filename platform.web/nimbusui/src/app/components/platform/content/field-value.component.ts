'use strict';

import { Component, Input } from '@angular/core';
import { WebContentSvc } from '../../../services/content-management.service';
import { Param } from '../../../shared/app-config.interface';

@Component({
    selector: 'nm-field-value',
    providers: [WebContentSvc],
    template: `
    <div [hidden]="!element?.config?.visible?.currState" >
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

