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
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Pipe({
  name: 'mask'
})
export class MaskPipe implements PipeTransform {
  transform(value: any, showMask: boolean, element?: Param): any {
    if (value!=null) {
      if(element.config.uiStyles.attributes.maskcount && element.config.uiStyles.attributes.maskcount >= 0 && showMask) {
        let maskedSection = value;
        let visibleSection = "";        
        if (element.config.uiStyles.attributes.maskcount !== 0){
          maskedSection = value.slice(0, -element.config.uiStyles.attributes.maskcount);
          visibleSection = value.slice(-element.config.uiStyles.attributes.maskcount);
        }       
        return maskedSection.replace(/./g, element.config.uiStyles.attributes.maskchar) + visibleSection;
      } else {
        return value;
      }
    }
  }
}