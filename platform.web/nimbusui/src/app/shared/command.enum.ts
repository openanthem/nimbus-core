/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

export class Enum<T> {
  public constructor(public readonly value: T) {}
  public toString() {
    return this.value.toString();
  }
}

export class Action extends Enum<string> {
  public static readonly _get = new Enum('_get');
  public static readonly _update= new Enum('_update');
  public static readonly _replace= new Enum('_replace');
  public static readonly _new = new Enum('_new');
  public static readonly _search = new Enum('_search');
  public static readonly _nav = new Enum('_nav');
}

export class HttpMethod extends Enum<string> {
  public static readonly GET = new Enum('GET');
  public static readonly POST = new Enum('POST');
}

export class Behavior extends Enum<string> {
  public static readonly execute = new Enum('$execute');
  public static readonly nav = new Enum('$nav');
}
