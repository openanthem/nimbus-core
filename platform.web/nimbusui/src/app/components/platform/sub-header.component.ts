import { Component, Input, SimpleChanges } from '@angular/core';
import { Param } from '../../shared/app-config.interface';
import { WebContentSvc } from '../../services/content-management.service';

@Component({
    selector: 'nm-subheader',
    providers: [WebContentSvc],
    template:`           
        <ng-template [ngIf]="!param?.type?.nested">
           <div class="col-sm-12 col-md-6 col-lg-4 {{param?.config?.uiStyles?.attributes?.cssClass}}">
                <span [hidden]="!param?.config?.uiStyles?.attributes?.showName">{{this.label}}</span>
                <span>{{param.leafState}}</span>
           </div>
        </ng-template>
    `
})
export class SubHeaderCmp {

    @Input() param: Param;
    private label : string;
    constructor(private wcs: WebContentSvc) {
         wcs.content$.subscribe( result => {
            this.label = result.label;
        } );
    }
    ngOnInit() {
        this.wcs.getContent(this.param.config.code);
    }
    ngOnChanges(changes: SimpleChanges) {
        if(changes['element']) {
            //console.log(this.param.leafState)
        }
    }
}



