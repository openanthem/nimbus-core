/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { Component, Input, Output, EventEmitter} from '@angular/core';
import { Param } from '../../../shared/app-config.interface';

@Component({
    selector: '',
    template:`
        <div [hidden] = "!this.param?.config?.visible?.currState"  [innerHTML]="param?.config?.uiStyles?.attributes?.content | convertToLinks">
        </div>
    `
})
export class ContentContainer {

    @Input() param: Param;
    @Output() antmControlValueChanged =new EventEmitter();
    constructor() {
    }
}
