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

import { Component, Input, SimpleChanges } from '@angular/core';
import { Param } from '../../shared/app-config.interface';
import { WebContentSvc } from '../../services/content-management.service';

@Component({
    selector: 'nm-subheader',
    providers: [WebContentSvc],
    template:`           
        <ng-template [ngIf]="!param?.type?.nested">
           <div class="col-sm-12 col-md-6 col-lg-4 {{param?.config?.uiStyles?.attributes?.cssClass}}">
                <span [hidden]="!param?.config?.uiStyles?.attributes?.showName">{{this.label}}</span>
                <span>{{param.leafState}}</span>
           </div>
        </ng-template>
    `
})
export class SubHeaderCmp {

    @Input() param: Param;
    private label : string;
    constructor(private wcs: WebContentSvc) {
         wcs.content$.subscribe( result => {
            this.label = result.label;
        } );
    }
    ngOnInit() {
        this.wcs.getContent(this.param.config.code);
    }
    ngOnChanges(changes: SimpleChanges) {
        if(changes['element']) {
            //console.log(this.param.leafState)
        }
    }
}



