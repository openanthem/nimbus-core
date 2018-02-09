/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { Component, Input, OnInit } from '@angular/core';
import { FooterConfig } from '../../../model/menu-meta.interface'

/**
 * \@author Vinay.Kurva
 * Content specified in this tag will display in global footer
 *
 * 
 */


@Component({
    selector: 'nm-footer-global',
    templateUrl: './footer-global.component.html'
})

export class FooterGlobal implements OnInit {

    @Input() footerConfig = <FooterConfig>{};
    constructor() {
    }

    ngOnInit() {
    }

}