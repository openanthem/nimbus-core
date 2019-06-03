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

import { Component, OnInit } from '@angular/core';
import { LoggerService } from '../../services/logger.service';
import { PageService } from '../../services/page.service';
import {
  ComponentTypes,
  ViewComponent
} from '../../shared/param-annotations.enum';
import { GenericDomain } from './../../model/generic-domain.model';
import { BaseElement } from './base-element.component';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes Section is a container. Section(s) go within a Tile. Section can have Sub-Sections
 *
 * \@howToUse
 * @Section annotation from Configuration drives this.
 *      Attribute "type".
 *      Attribute "cssClass" - custom css for the section.
 * <nm-section [element]="element"></nm-section>
 *
 */
@Component({
  selector: 'nm-section',
  templateUrl: './section.component.html'
})
export class Section extends BaseElement implements OnInit {
  viewComponent = ViewComponent;
  componentTypes = ComponentTypes;

  constructor(
    private pageService: PageService,
    private _logger: LoggerService
  ) {
    super();
  }

  ngOnInit() {
    super.ngOnInit();
    this._logger.debug('Section-i ' + this.element.path);
    // Check for initialization
    if (this.element.config && this.element.config.initializeComponent()) {
      this.pageService.processEvent(
        this.element.path,
        '$execute',
        new GenericDomain(),
        'POST'
      );
    }
    this.updatePosition();
  }
}
