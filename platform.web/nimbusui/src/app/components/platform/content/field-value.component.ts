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

