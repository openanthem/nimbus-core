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

import { Input } from '@angular/core';
import { Param } from './../../shared/param-state';
import { ParamUtils } from './../../shared/param-utils';

/**
 * \@author Tony Lopez
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
export abstract class BaseLabel {
  @Input() element: Param;
  @Input() labelClass: Param;

  constructor() {}

  /**
   * Get the tooltip help text for this element.
   */
  public get helpText(): string {
    return ParamUtils.getHelpText(this.element);
  }

  /**
   * Get the label text for this element.
   */
  public get label(): string {
    return ParamUtils.getLabelText(this.element);
  }

  /**
   * Get the css classes to apply for this element.
   */
  public get cssClass(): string {
    let cssClass = ParamUtils.getLabelCss(this.element);
    if (this.labelClass) {
      cssClass += ' ' + this.labelClass;
    }
    return cssClass ? cssClass : '';
  }
}
