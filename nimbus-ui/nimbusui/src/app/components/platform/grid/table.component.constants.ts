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

import {
  ComponentTypes,
  ViewComponent
} from '../../../shared/param-annotations.enum';

/**
 * \@author Swetha Vemuri
 * \@whatItDoes
 *
 * \@howToUse
 *
 */

export class TableComponentConstants {
  public static readonly tableBasedComponents = [
    ViewComponent.grid.toString(),
    ViewComponent.treeGrid.toString()
  ];

  public static readonly allowedColumnStylesAlias: string[] = [
    ViewComponent.button.toString(),
    ViewComponent.link.toString(),
    ViewComponent.linkMenu.toString(),
    ViewComponent.gridRowBody.toString()
  ];

  public static readonly allowedInlineEditColumnStylesAlias: string[] = [
    ViewComponent.gridcolumn.toString(),
    ComponentTypes.textBox.toString(),
    ComponentTypes.comboBox.toString(),
    'Calendar' // TODO replace with component types
  ];

  public static readonly componentsEditableAsText: string[] = [
    ViewComponent.gridcolumn.toString(),
    ComponentTypes.textBox.toString()
  ];
}
