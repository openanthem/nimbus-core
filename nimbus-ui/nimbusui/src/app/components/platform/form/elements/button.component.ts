import { Length } from './../../../../shared/app-config.interface';
import { Component, Input, Output, EventEmitter, ViewEncapsulation } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Location } from '@angular/common';
import { GenericDomain } from './../../../../model/generic-domain.model';
import { Param } from '../../../../shared/app-config.interface';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { ServiceConstants } from './../../../../services/service.constants';
import { BaseElement } from '../../base-element.component';
import { FileService } from '../../../../services/file.service';

@Component( {
    selector: 'nm-button',
    providers: [WebContentSvc],
    template: `
        <ng-template [ngIf]="element.config?.uiStyles?.attributes?.imgSrc == ''">
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style=='PRIMARY' && element?.visible?.currState == true">
                <button class="btn btn-action" (click)="onSubmit()" type="{{element.config?.uiStyles?.attributes?.type}}" [disabled]="!form.valid">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style=='SECONDARY' && element?.visible?.currState == true">
                <button class="btn btn-secondary" [disabled]="disabled" (click)="emitEvent(this)" type="{{element.config?.uiStyles?.attributes?.type}}">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style=='PLAIN' && element?.visible?.currState == true">
                <button class="btn btn-plain" (click)="emitEvent(this)" type="{{element.config?.uiStyles?.attributes?.type}}">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style=='DESTRUCTIVE' && element?.visible?.currState == true">
                <button class="btn btn-delete" (click)="emitEvent(this)" type="{{element.config?.uiStyles?.attributes?.type}}">{{label}}</button>
            </ng-template>
        </ng-template>
        <ng-template [ngIf]="element.config?.uiStyles?.attributes?.imgSrc != ''">
           <button (click)="emitEvent(this)" type="button" class="{{element.config?.uiStyles?.attributes?.cssClass}} ">
                <svg> 
                    <use xmlns:xlink="http://www.w3.org/1999/xlink" attr.xlink:href="resources/icons/{{element.config?.uiStyles?.attributes?.imgSrc}}#Layer_1"></use> 
                </svg>
            </button>
        </ng-template>
    `
} )

export class Button extends BaseElement {

    @Input() element: Param;
    @Input() payload: string;
    @Input() form: FormGroup;
    @Output() buttonClickEvent = new EventEmitter();
    private imagesPath: string;
    private disabled: boolean;
    files: any;

    constructor( private pageService: PageService, private _wcs: WebContentSvc, 
        private location: Location, private fileService: FileService ) {
        super(_wcs);
    }

    emitEvent( $event: any ) {
        //browserback to go back to previous page
        if(this.element.config.uiStyles != null && this.element.config.uiStyles.attributes.browserBack) {
            this.location.back();
        } else {
            this.buttonClickEvent.emit( $event );
        }
    }

    ngOnInit() {
        super.ngOnInit();
        this.disabled = !this.element.enabled.currState;
        this.imagesPath = ServiceConstants.IMAGES_URL;
        this.payload = this.element.config.uiStyles.attributes.payload;
        this.buttonClickEvent.subscribe(( $event ) => {
            //console.log( $event );
            //let payload = JSON.parse(JSON.stringify(this.payload));

            this.pageService.processEvent( $event.element.path, $event.element.config.uiStyles.attributes.b,
                null, $event.element.config.uiStyles.attributes.method );
        } );

        this.pageService.validationUpdate$.subscribe(event => {
            if(event.path == this.element.path) {
                this.disabled = !event.enabled.currState;
            }
        });
    }

    checkObjectType(type: string, obj: object) {
        var clas = Object.prototype.toString.call(obj).slice(8, -1);
        return obj !== undefined && obj !== null && clas === type;
    }

    getFileParameter(item: GenericDomain): string { // make it recursive for nested model in future
        let hasFile = undefined;
        for (var key in item) {
            if (!item.hasOwnProperty(key)) continue;
            let obj = item[key];
            if (this.checkObjectType('Array', obj)) {
                if(obj.length > 0) {
                    if (this.checkObjectType('File', obj[0])) {
                        hasFile = key;
                    }
                }
            }
        }
        return hasFile;
    }

    onSubmit() {
        let item: GenericDomain = new GenericDomain();
        item = this.form.value;
        // Check for File upload parameters ('fileControl').
        if (item['fileControl']) {
            let files: File[] = item['fileControl'];
            delete item['fileControl'];
            for(let p=0; p<files.length; p++){
                item['fileId'] = files[p]['fileId'];
                item['name'] = files[p]['name'];
                this.pageService.processEvent( this.element.path, this.element.config.uiStyles.attributes.b, item, 'POST' );
               
            }
            
        } else {
            this.pageService.processEvent( this.element.path, this.element.config.uiStyles.attributes.b, item, 'POST' );
        }

        // Form reset after submit
        this.reset();
    }

    reset() {
        if(this.form !== undefined) {
            if(this.element.config.uiStyles.attributes.formReset) {
                this.form.reset();
            }
        }
    }

    /* look for parameters in URI {} */
    getAllURLParams( uri: string ): string[] {
        var pattern = /{([\s\S]*?)}/g;
        return uri.match( pattern );
    }
}
