import { WebContentSvc } from './../../../services/content-management.service';
import { BaseElement } from './../base-element.component';
import { CardDetails, Param } from '../../../shared/app-config.interface';
import { Component, Input } from '@angular/core';
import { PageService } from '../../../services/page.service';

@Component({
    selector: 'nm-card-details',
     styles: [`
        .hide {
        display: none;
        },
        `
    ],
    templateUrl: './card-details.component.html',
    providers: [
        WebContentSvc
    ]
})
export class CardDetailsComponent extends BaseElement {
    @Input() list : CardDetails;
    @Input() collectionElem: boolean = false;
    @Input() elemId: string = undefined;
    @Input() element: Param;
    @Input() editUrl: string;
    opened: boolean = false;

    constructor(private pageSvc : PageService, private _wcs: WebContentSvc) {
        super(_wcs);
    }

    processOnClick() {
        // let uri = this.element.config.uiStyles.attributes.editUrl;

        // // Collection check - append colletion path and index
        // if (this.collectionElem) {
        //     uri = uri + '?path=' + this.element.config.uiStyles.attributes.modelPath;
        //     uri = uri + '/' + this.elemId;
        // } 

        // // Replace url parameters with values 
        // let uriParams = this.getAllURLParams(uri);
        // if(uriParams!=null) {
        //     for (let uriParam of uriParams) {
        //         let p = uriParam.substring(1, uriParam.length-1);
        //         this.element.type.model.params.forEach(param => {
        //             param.type.model.params.forEach(field => {
        //                 if(field.config.code == p) {
        //                     uri = uri.replace(new RegExp(uriParam, 'g'), field.leafState);
        //                 }
        //             });
        //         });
        //     }
        // }
        // console.log(uri);
        this.pageSvc.processEvent(this.element.path, '$execute', null, 'POST');
    }

    /* look for parameters in URI {} */
    getAllURLParams (uri: string): string[] {
        var pattern = /{([\s\S]*?)}/g;
        return uri.match(pattern);
    }

    toggle() {
        this.opened = !this.opened;
    }

}

