/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

'use strict';
import { ActivatedRoute, Router } from '@angular/router';
import { Component } from '@angular/core';

import { Param } from '../../../shared/app-config.interface';

@Component({
    selector: 'nm-page-content',
    templateUrl: './page-content.component.html'
})

export class PageContent {
    pageId: string;
    tilesList: any[];
    public pageParam: Param;
    constructor(private router: Router, private route: ActivatedRoute) {
        this.router.events.subscribe(path => {
            this.pageId = this.route.snapshot.url[0].path;
        });
    }

    ngOnInit() {
        this.route.data.subscribe((data: { page: Param }) => {
            let page : Param = data.page;
            this.pageParam = page;
            this.tilesList = [];
            if(page.type.model != null) {
                page.type.model.params.forEach(element => {
                    if(element.config.uiStyles.attributes.alias === 'Tile') {
                        this.tilesList.push(element);
                    }
                });
            }
        });
    }

}
