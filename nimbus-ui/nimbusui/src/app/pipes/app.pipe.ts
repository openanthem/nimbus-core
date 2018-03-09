import { Pipe, PipeTransform } from '@angular/core';
import { Param } from '../shared/app-config.interface';

@Pipe({ name: 'keys' })
export class KeysPipe implements PipeTransform {
  transform(value) {
    if (value instanceof Map) {
      let keys: any[] = [];
      let mapValue: Map<string, Param[]> = value;
      for (let [key, values] of Array.from(mapValue.entries())) {
        keys.push({ key: key, values: values });
      }
      return keys;
    }
  }
}