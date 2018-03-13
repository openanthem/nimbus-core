/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */
'use strict';
import { Component, Input } from '@angular/core';
import { Param } from '../../../shared/app-config.interface';
import { WebContentSvc } from '../../../services/content-management.service';
import { BaseElement } from '../base-element.component';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes Equivalent to Header tag in HTML. 
 * Content specified in this tag will be displayed within a <Hn> (n is the header size) tag on the HTML page.
 * 
 * \@howToUse 
 * @Header annotation from Configuration drives this. 
 *      Attribute "size = {H1, H2, H3, H4, H5, H6}" drives the size of the Header.
 * <nm-paragraph [element]="element"></nm-paragraph>
 * 
 */
@Component({
    selector: 'nm-header',
    providers: [WebContentSvc],
    template:`
        <ng-template [ngIf]="visible == true">
            <H1 *ngIf="size=='H1'">{{label}}</H1>
            <H2 *ngIf="size=='H2'">{{label}}</H2>
            <H3 *ngIf="size=='H3'">{{label}}</H3>
            <H4 *ngIf="size=='H4'">{{label}}</H4>
            <H5 *ngIf="size=='H5'">{{label}}</H5>
            <H6 *ngIf="size=='H6'">{{label}}</H6>
        </ng-template>
    `
})

export class Header extends BaseElement {

    private size: string;

    constructor(private wcsvc: WebContentSvc) {
        super(wcsvc);
    }

    ngOnInit() {
        super.ngOnInit();
        this.size = this.element.config.uiStyles.attributes.size;
    }
}

