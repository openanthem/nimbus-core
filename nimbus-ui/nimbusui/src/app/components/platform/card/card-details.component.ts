/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

import { WebContentSvc } from './../../../services/content-management.service';
import { BaseElement } from './../base-element.component';
import { CardDetails } from '../../../shared/card-details';
import { Param } from '../../../shared/param-state';
import { Component, Input } from '@angular/core';
import { PageService } from '../../../services/page.service';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-card-details',
     styles: [`
        .hide {
        display: none;
        },
        `
    ],
    templateUrl: './card-details.component.html',
    providers: [
        WebContentSvc
    ]
})
export class CardDetailsComponent extends BaseElement {
    @Input() list : CardDetails;
    @Input() collectionElem: boolean = false;
    @Input() elemId: string = undefined;
    @Input() element: Param;
    @Input() editUrl: string;
    opened: boolean = false;

    constructor(private pageSvc : PageService, private _wcs: WebContentSvc) {
        super(_wcs);
    }

    processOnClick() {
        this.pageSvc.processEvent(this.element.path, '$execute', null, 'POST');
    }

    /* look for parameters in URI {} */
    getAllURLParams (uri: string): string[] {
        var pattern = /{([\s\S]*?)}/g;
        return uri.match(pattern);
    }

    toggle() {
        this.opened = !this.opened;
    }

}

