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

import { ConfigService } from './../services/config.service';
import { Converter } from './object.conversion';
import { Model, Param } from './param-state';
import { Serializable } from './serializable';
/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
export class Page implements Serializable<Page, string> {
  pageConfig: Param;
  flow: string;

  deserialize(inJson) {
    return this;
  }
}

export class Input implements Serializable<Input, string> {
  model: Model;

  constructor(private configSvc: ConfigService) {}

  deserialize(inJson, path) {
    this.model = new Model(this.configSvc).deserialize(inJson.model, path);
    return this;
  }
}

export class Result implements Serializable<Result, string> {
  inputCommandUri: string;
  action: string;
  outputs: Result[];
  value: any;
  rootDomainId: string;
  constructor(private configSvc: ConfigService) {}
  deserialize(inJson) {
    let obj = this;
    obj = Converter.convert(inJson, obj);
    if (inJson.value != null) {
      if (inJson.value.config != null) {
        obj['value'] = new Param(this.configSvc).deserialize(
          inJson.value,
          inJson.code
        );
      } else {
        obj['value'] = inJson.value;
      }
    }
    var outputs = [];
    if (inJson.outputs != null && inJson.outputs.length > 0) {
      for (var value in inJson.outputs) {
        outputs.push(
          new Result(this.configSvc).deserialize(inJson.outputs[value])
        );
      }
    }
    obj['outputs'] = outputs;
    return obj;
  }
}

export class ViewRoot {
  model: Model;
  layout: string;
}

export class Config implements Serializable<Config, string> {
  input: Input;
  output: Output;

  constructor(private configSvc: ConfigService) {}

  deserialize(inJson, path) {
    this.input = new Input(this.configSvc).deserialize(inJson.input, path);
    this.output = new Output(this.configSvc).deserialize(inJson.output, path);
    return this;
  }
}

export class Pattern implements Serializable<Pattern, string> {
  regex: string;

  deserialize(inJson) {
    let start = inJson.indexOf('(', 0) + 1;
    let end = inJson.lastIndexOf(')');
    this.regex = inJson.substr(start, end - start);

    return this;
  }
}

export class Length implements Serializable<Length, string> {
  regexp: string;

  deserialize(inJson) {
    // Build a regex pattern for min, max (pattern=".{min,max}")
    this.regexp = '.{';
    if (inJson.min) {
      this.regexp += inJson.min;
    }
    this.regexp += ',';
    if (inJson.min) {
      this.regexp += inJson.max;
    }
    this.regexp += '}';

    return this;
  }
}

export class Output implements Serializable<Output, string> {
  model: Model;

  constructor(private configSvc: ConfigService) {}

  deserialize(inJson, path) {
    this.model = new Model(this.configSvc).deserialize(inJson.model, path);
    return this;
  }
}

export class ModelEvent implements Serializable<ModelEvent, string> {
  type: string; //Revisit - server sending as type instead of action
  value: any;
  payload: any; //temp fix - value doesnt work. Remove value since server side ModelEvent interface has payload.
  id: string;

  deserialize(inJson) {
    this.type = inJson.type;
    this.value = inJson.value;
    this.id = inJson.id;

    return this;
  }
}

export class Detail implements Serializable<Detail, string> {
  label: string;
  value: string;

  deserialize(inJson) {
    var obj = this;
    obj = Converter.convert(inJson, obj);
    return obj;
  }
}

export class Details implements Serializable<Details, string> {
  details: Detail[];

  deserialize(inJson) {
    this.details = [];
    for (var detail in inJson.details) {
      this.details.push(new Detail().deserialize(inJson.details[detail]));
    }
    return this;
  }
}

export class ExecuteOutput implements Serializable<ExecuteOutput, string> {
  result: ModelEvent;
  validationResult: any;
  executeException: any;

  deserialize(inJson) {
    if (inJson.result != null) {
      this.result = new ModelEvent().deserialize(inJson.result);
    }
    this.validationResult = inJson.validationResult;
    this.executeException = inJson.executeException;

    return this;
  }
}

export class ExecuteResponse implements Serializable<ExecuteResponse, string> {
  result: MultiOutput[];
  sessionId: string;
  constructor(private configSvc: ConfigService) {}
  deserialize(inJson) {
    this.result = [];
    if (inJson.result != null) {
      // tslint:disable-next-line:forin
      for (const p in inJson.result) {
        this.result.push(
          new MultiOutput(this.configSvc).deserialize(inJson.result[p])
        );
      }
    }
    if (inJson.sessionId != null) {
      this.sessionId = inJson.sessionId;
    }
    return this;
  }
}

export class MultiOutput implements Serializable<MultiOutput, string> {
  behavior: string;
  result: Result;
  executeException: ExecuteException;
  constructor(private configSvc: ConfigService) {}
  deserialize(inJson) {
    this.behavior = inJson.b;
    if (inJson.result != null) {
      this.result = new Result(this.configSvc).deserialize(inJson.result);
    }
    if (inJson.executeException != null) {
      this.executeException = new ExecuteException().deserialize(
        inJson.executeException
      );
    }
    return this;
  }
}

export class ExecuteException
  implements Serializable<ExecuteException, string> {
  code: string;
  message: string;
  uniqueId: string;
  deserialize(inJson) {
    this.code = inJson.code;
    this.message = inJson.message;
    this.uniqueId = inJson.uniqueId;
    return this;
  }
}

export class DataTransferEffect {
  static COPY = new DataTransferEffect('copy');
  static LINK = new DataTransferEffect('link');
  static MOVE = new DataTransferEffect('move');
  static NONE = new DataTransferEffect('none');

  constructor(public name: string) {}
}

/**
 * Check and return true if an object is type of string
 */
export function isString(obj: any) {
  return typeof obj === 'string';
}

/**
 * Check and return true if an object not undefined or null
 */
export function isPresent(obj: any) {
  return obj !== undefined && obj !== null;
}

/**
 * Check and return true if an object is type of Function
 */
export function isFunction(obj: any) {
  return typeof obj === 'function';
}

/**
 * Create Image element with specified url string
 */
export function createImage(src: string) {
  let img: HTMLImageElement = new HTMLImageElement();
  img.src = src;
  return img;
}

/**
 * Call the function
 */
export function callFun(fun: Function) {
  return fun();
}
