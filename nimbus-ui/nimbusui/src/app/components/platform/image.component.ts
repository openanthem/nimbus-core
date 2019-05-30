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

import { Component, Input } from '@angular/core';
import { ServiceConstants } from '../../services/service.constants';
import { ImageTypes } from '../../shared/imageTypes.enum';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@author Purnachander.Mashetty
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-image',
  template: `
    <span
      *ngIf="type === imageTypes.svg.toString()"
      class="{{ cssClass }}"
      title="{{ title }}"
    >
      <nm-svg [name]="name"></nm-svg>
    </span>
    <span *ngIf="type === imageTypes.fa.toString()" class="{{ cssClass }}">
      <i class="fa {{ name }}" title="{{ title }}" *ngIf="title"></i>
      <i class="fa {{ name }}" *ngIf="!title"></i>
    </span>
  `
})
export class Image {
  @Input() name: string;
  @Input() type: string;
  @Input() title?: string;
  @Input() cssClass: string;
  public imagesPath: string;
  public imageTypes = ImageTypes;

  constructor() {}

  ngOnInit() {
    this.imagesPath = ServiceConstants.IMAGES_URL;
  }
}
