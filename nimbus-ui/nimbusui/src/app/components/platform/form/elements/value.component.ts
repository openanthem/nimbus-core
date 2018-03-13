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
