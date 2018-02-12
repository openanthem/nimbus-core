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
import { CardDetails, Param } from '../../../shared/app-config.interface';
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
        // let uri = this.element.config.uiStyles.attributes.editUrl;

        // // Collection check - append colletion path and index
        // if (this.collectionElem) {
        //     uri = uri + '?path=' + this.element.config.uiStyles.attributes.modelPath;
        //     uri = uri + '/' + this.elemId;
        // } 

        // // Replace url parameters with values 
        // let uriParams = this.getAllURLParams(uri);
        // if(uriParams!=null) {
        //     for (let uriParam of uriParams) {
        //         let p = uriParam.substring(1, uriParam.length-1);
        //         this.element.type.model.params.forEach(param => {
        //             param.type.model.params.forEach(field => {
        //                 if(field.config.code == p) {
        //                     uri = uri.replace(new RegExp(uriParam, 'g'), field.leafState);
        //                 }
        //             });
        //         });
        //     }
        // }
        // console.log(uri);
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

