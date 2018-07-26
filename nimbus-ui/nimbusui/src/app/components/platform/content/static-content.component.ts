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
import { Component, Input } from '@angular/core';
import { Param } from '../../../shared/param-state';
import { WebContentSvc } from '../../../services/content-management.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { BaseElement } from '../base-element.component';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
  selector: 'nm-static-text',
  providers: [WebContentSvc],
  template: `
    <div class="col-sm-12" [innerHTML]="label"></div>
   `
})
export class StaticText extends BaseElement{
    @Input() element: Param;
    private _htmlContent: string;

    constructor(private _wcs: WebContentSvc, private _sanitizer: DomSanitizer) {
         super(_wcs);
    }

    public get htmlContent() : SafeHtml {
       return this._sanitizer.bypassSecurityTrustHtml(this._htmlContent);
    }

    ngOnInit() {
        super.ngOnInit();
    }
}
