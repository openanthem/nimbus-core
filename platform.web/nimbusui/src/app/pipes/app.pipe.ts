/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import {Pipe, PipeTransform} from '@angular/core';

 @Pipe({name: 'keys'})
 export class KeysPipe implements PipeTransform {
 transform(value) {
   let keys:any = [];
   for (let key in value) {
      keys.push( {key: key, value: value[key]} );
    }
     return keys;
  }
}

