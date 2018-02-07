import { Component, Input, SimpleChanges } from '@angular/core';
import { Param } from '../../shared/app-config.interface';
import { WebContentSvc } from '../../services/content-management.service';
import { BaseElement } from './base-element.component';

@Component({
    selector: 'nm-subheader',
    providers: [WebContentSvc],
    template:`           
        <ng-template [ngIf]="!param?.type?.nested">
           <div class="col-sm-12 col-md-6 col-lg-4 {{param?.config?.uiStyles?.attributes?.cssClass}}">
                <span [hidden]="!param?.config?.uiStyles?.attributes?.showName">{{label}}</span>
                <span>{{param.leafState}}</span>
           </div>
        </ng-template>
    `
})
export class SubHeaderCmp extends BaseElement{

    @Input() param: Param;
    constructor(private _wcs: WebContentSvc) {
        super(_wcs);
    }
    ngOnInit() {
        this.loadLabelConfig(this.param);
    }
    ngOnChanges(changes: SimpleChanges) {
        if(changes['element']) {
            //console.log(this.param.leafState)
        }
    }
}



