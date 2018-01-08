/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { Component, Input } from '@angular/core';

import { Param } from '../../../shared/app-config.interface';

@Component({
    selector: 'nm-grid-container',
    templateUrl: './grid-container.component.html'
})
export class GridContainer {
    @Input() element: Param;

    constructor() {
    }

    ngOnInit() {
        console.log(this.element);
    }

}
