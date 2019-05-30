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
import { ParamConfig } from '../../../shared/param-config';
import { Param } from '../../../shared/param-state';
import { ParamUtils } from './../../../shared/param-utils';
import { TableComponentConstants } from './table.component.constants';

/**
 * \@author Sandeep Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-th',
  template: `
    {{ label }}
  `
})
export class TableHeader {
  @Input() element: Param;
  @Input() paramConfig: ParamConfig;

  ngOnInit() {
    this.paramConfig['field'] = this.paramConfig.code;
    let colElemLabelConfig = ParamUtils.getLabelConfig(
      this.element.elemLabels.get(this.paramConfig.id)
    );
    if (colElemLabelConfig) {
      this.paramConfig.label = colElemLabelConfig.text;
    }
    this.paramConfig['header'] = this.paramConfig.label;
    if (this.paramConfig.uiStyles.attributes.hidden) {
      this.paramConfig['exportable'] = false;
    } else {
      if (
        TableComponentConstants.allowedColumnStylesAlias.includes(
          this.paramConfig.uiStyles.attributes.alias
        ) ||
        this.paramConfig.type.nested === true
      ) {
        this.paramConfig['exportable'] = false;
      } else {
        this.paramConfig['exportable'] = true;
      }
    }
  }
  public get label(): string {
    return this.paramConfig.label;
  }
}
