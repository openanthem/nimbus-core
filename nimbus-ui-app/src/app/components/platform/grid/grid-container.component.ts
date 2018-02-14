import { Component, Input } from '@angular/core';

import { Param } from '../../../shared/app-config.interface';

@Component({
    selector: 'nm-grid-container',
    templateUrl: './grid-container.component.html'
})
export class GridContainer {
    @Input() element: Param;

    constructor() {
    }

    ngOnInit() {
        console.log(this.element);
    }

}
