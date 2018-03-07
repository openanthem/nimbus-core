/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
