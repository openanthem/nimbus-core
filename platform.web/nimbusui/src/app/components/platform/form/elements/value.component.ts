/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

'use strict';
import { Component, Input, SimpleChanges } from '@angular/core';
import { Param } from '../../../../shared/app-config.interface';

@Component({
  selector: 'nm-value',
  template: `
      {{element.leafState}}
   `
})
export class Value {

    @Input() element: Param;

    ngOnInit() {
        //console.log(this.element.leafState);
    }

    ngOnChanges(changes: SimpleChanges) {
        if(changes['element']) {
            //console.log(this.element.leafState)
        }
    }
}
