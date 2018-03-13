import {Pipe, PipeTransform} from '@angular/core';
import { SelectItem } from 'primeng/components/common/selectitem';

@Pipe({
  name: 'selectItemPipe'
})
export class SelectItemPipe implements PipeTransform {

  transform(value: any): SelectItem[] {
    if (value)
      return value.map(function (item) {
        return {
          label: item['label'],
          value: item['code']
        }
      });
    else return [];
  }

}