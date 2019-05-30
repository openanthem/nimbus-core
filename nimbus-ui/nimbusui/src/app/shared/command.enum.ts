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

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
export class Enum<T> {
  public constructor(public readonly value: T) {}
  public toString() {
    return this.value.toString();
  }
}

export class Action extends Enum<string> {
  public static readonly _get = new Enum('_get');
  public static readonly _update = new Enum('_update');
  public static readonly _replace = new Enum('_replace');
  public static readonly _new = new Enum('_new');
  public static readonly _search = new Enum('_search');
  public static readonly _nav = new Enum('_nav');
  public static readonly _add = new Enum('_add');
}

export class HttpMethod extends Enum<string> {
  public static readonly GET = new Enum('GET');
  public static readonly POST = new Enum('POST');
}

export class Behavior extends Enum<string> {
  public static readonly execute = new Enum('$execute');
  public static readonly nav = new Enum('$nav');
}

export class ParamAttribute extends Enum<string> {
  public static readonly leafState = new Enum('leafState');
  public static readonly enabled = new Enum('enabled');
  public static readonly activeValidationGroups = new Enum(
    'activeValidationGroups'
  );
  public static readonly config = new Enum('config');
  public static readonly type = new Enum('type');

  static attributeList(): String[] {
    const keys = Object.keys(ParamAttribute);
    return keys;
  }
}
