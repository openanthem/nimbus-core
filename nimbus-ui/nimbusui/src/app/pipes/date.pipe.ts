import { ParamUtils } from './../shared/param-utils';
import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'dateTimeFormat' })
  export class DateTimeFormatPipe extends DatePipe implements PipeTransform {
    transform(value: any, format: string, typeClassMapping: string): any {
      // If client has provided a format, prefer that format over all others.
      if(format && format !== '') {
        return super.transform(value, format);
      }
      
      // If a type is given in the pipe, prefer to use the configuration for that type to determine
      // the format
      if (typeClassMapping) {
        let defaultFormat: string = ParamUtils.getDateFormatForType(typeClassMapping);
        if (defaultFormat) {
          return super.transform(value, defaultFormat);
        }
      }
      // If format is not given, and a typeClassMapping is not defined then use the system default.
      // This situation is not ideal, put possible.
      return super.transform(value, ParamUtils.DEFAULT_DATE_FORMAT);
    }
  }
