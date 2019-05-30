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

import { Enum } from '../../../shared/command.enum';

/**
 * \@author Rakesh.Patel
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
export class SortAs extends Enum<string> {
  public static readonly default = new Enum('DEFAULT');
  public static readonly date = new Enum('DATE');
  public static readonly number = new Enum('NUMBER');
  public static readonly text = new Enum('TEXT');
}

export class GridColumnDataType extends Enum<string> {
  public static readonly int = new Enum('int');
  public static readonly integer = new Enum('integer');
  public static readonly long = new Enum('long');
  public static readonly double = new Enum('double');

  public static readonly date = new Enum('date');
  public static readonly localdate = new Enum('localdate');
  public static readonly localdatetime = new Enum('localdatetime');
  public static readonly zoneddatetime = new Enum('zoneddatetime');
}
