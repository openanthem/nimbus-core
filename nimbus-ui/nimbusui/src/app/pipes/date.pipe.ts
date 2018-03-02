import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'dateTimeFormat' })
  export class DateTimeFormatPipe extends DatePipe implements PipeTransform {
    transform(value: any, args: string): any {
        if(args === undefined || args === '' || args === null){
            args="MM/dd/yyyy hh:mm a";
        }
        return super.transform(value, args, 'UTC');
    }
  }
