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
import { BaseElement } from '../base-element.component';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes Equivalent to Header tag in HTML.
 * Content specified in this tag will be displayed within a <Hn> (n is the header size) tag on the HTML page.
 *
 * \@howToUse
 * @Header annotation from Configuration drives this.
 *      Attribute "size = {H1, H2, H3, H4, H5, H6}" drives the size of the Header.
 * <nm-paragraph [element]="element"></nm-paragraph>
 *
 */
@Component({
  selector: 'nm-header',
  providers: [],
  template: `
    <ng-template [ngIf]="visible == true">
      <H1 *ngIf="size == 'H1'">{{ label }}</H1>
      <H2 *ngIf="size == 'H2'">{{ label }}</H2>
      <H3 *ngIf="size == 'H3'">{{ label }}</H3>
      <H4 *ngIf="size == 'H4'">{{ label }}</H4>
      <H5 *ngIf="size == 'H5'">{{ label }}</H5>
      <H6 *ngIf="size == 'H6'">{{ label }}</H6>
    </ng-template>
  `
})
export class Header extends BaseElement {
  private size: string;

  ngOnInit() {
    super.ngOnInit();
    this.size = this.element.config.uiStyles.attributes.size;
  }
}
