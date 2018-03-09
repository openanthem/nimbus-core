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
