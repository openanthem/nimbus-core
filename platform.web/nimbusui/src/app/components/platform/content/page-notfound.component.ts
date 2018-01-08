/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

'use strict';

import { Component } from '@angular/core';

@Component({
    selector: 'nm-pg-notfound',
    template: `
        <div class="p-0 col-sm-10" style="text-align: center; min-height:500px; ">
            <h1>Page Not Found</h1>
        </div>
    `
})

export class PageNotfoundComponent {

    constructor() {
    }

    ngOnInit() {
    }

}

