/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

import { Component } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { BaseElement } from '../base-element.component';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes Equivalent to <P> tag in HTML. Content specified in this tag will be displayed within a <P>
 * tag on the HTML page.
 *
 * \@howToUse
 * @Paragraph annotation from Configuration drives this.
 * <nm-paragraph [element]="element"></nm-paragraph>
 *
 */

@Component({
  selector: 'nm-paragraph',
  template: `
    <p *ngIf="visible == true" [innerHTML]="htmlContent"></p>
  `
})
export class Paragraph extends BaseElement {
  private _htmlContent: string;

  constructor(private _sanitizer: DomSanitizer) {
    super();
  }

  public get htmlContent(): SafeHtml {
    return this._sanitizer.bypassSecurityTrustHtml(this.label);
  }
}
