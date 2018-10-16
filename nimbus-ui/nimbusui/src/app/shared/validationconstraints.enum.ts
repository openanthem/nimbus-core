/**
 * @license
 * Copyright 2016-2018 the original author or authors.
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

export class ValidationConstraint extends Enum<string> {
  public static readonly _notNull = new Enum('NotNull');
  public static readonly _pattern= new Enum('Pattern');
  public static readonly _size= new Enum('Size');
  public static readonly _number = new Enum('isNumber');
  public static readonly _zip = new Enum('isZip');
  public static readonly _max = new Enum('Max');
  public static readonly _past = new Enum('Past');
  public static readonly _future = new Enum('Future');
}

export class ConstraintMapping extends Enum<string> {
  public static readonly required = new Enum(ValidationConstraint._notNull.value);
  public static readonly pattern = new Enum(ValidationConstraint._pattern.value);
  public static readonly minMaxSelection = new Enum(ValidationConstraint._size.value);
  public static readonly isNumber = new Enum(ValidationConstraint._number.value);
  public static readonly isPast = new Enum(ValidationConstraint._past.value);
  public static readonly isFuture = new Enum(ValidationConstraint._future.value);

  static getConstraintValue(contraintName: string): string {
    for (var key in ConstraintMapping) {
      if(key == contraintName) {
        return ConstraintMapping[key].value;
      }
    } 
  }
}