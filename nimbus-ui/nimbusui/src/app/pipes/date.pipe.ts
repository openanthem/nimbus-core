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

import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';
import { ParamUtils } from './../shared/param-utils';

@Pipe({ name: 'dateTimeFormat' })
export class DateTimeFormatPipe extends DatePipe implements PipeTransform {
  transform(value: any, format: string, typeClassMapping: string): any {
    // If client has provided a format, prefer that format over all others.
    if (format && format !== '') {
      return super.transform(value, format);
    }

    // If a type is given in the pipe, prefer to use the configuration for that type to determine
    // the format
    if (typeClassMapping) {
      let defaultFormat: string = ParamUtils.getDateFormatForType(
        typeClassMapping
      );
      if (defaultFormat) {
        return super.transform(value, defaultFormat);
      }
    }
    // If format is not given, and a typeClassMapping is not defined then use the system default.
    // This situation is not ideal, put possible.
    return super.transform(value, ParamUtils.DEFAULT_DATE_FORMAT);
  }
}
