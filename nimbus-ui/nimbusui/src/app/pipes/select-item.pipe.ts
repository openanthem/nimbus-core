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

import { Pipe, PipeTransform } from '@angular/core';
import { SelectItem } from 'primeng/components/common/selectitem';
import { Param } from '../shared/param-state';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Pipe({
  name: 'selectItemPipe'
})
export class SelectItemPipe implements PipeTransform {
  transform(value: any, element?: Param): SelectItem[] {
    if (value) {
      let items = [];
      if (
        element &&
        element.config &&
        element.config.uiStyles &&
        element.config.uiStyles.attributes &&
        element.config.uiStyles.attributes.defaultLabel != ''
      ) {
        items.push({
          label: element.config.uiStyles.attributes.defaultLabel,
          value: null
        });
      }
      return items.concat(
        value.map(function(item) {
          return {
            label: item['label'],
            value: item['code']
          };
        })
      );
    } else return [];
  }
}
