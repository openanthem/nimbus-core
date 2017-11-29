import { GenericDomain } from './../../../../model/generic-domain.model';
import { Component, Input, Output, EventEmitter, ViewEncapsulation } from '@angular/core';
import { Param } from '../../../../shared/app-config.interface';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { FormGroup } from '@angular/forms';
import { Location } from '@angular/common';
import { ServiceConstants } from './../../../../services/service.constants';

@Component( {
    selector: 'nm-button',
    providers: [WebContentSvc],
    template: `
        <ng-template [ngIf]="element.config?.uiStyles?.attributes?.imgSrc == ''">
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style=='PRIMARY' && element?.visible?.currState == true">
                <button class="btn btn-action" (click)="onSubmit()" type="{{element.config?.uiStyles?.attributes?.type}}" [disabled]="!form.valid">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style=='SECONDARY' && element?.visible?.currState == true">
                <button class="btn btn-secondary" (click)="emitEvent(this)" type="{{element.config?.uiStyles?.attributes?.type}}">{{label}}</button>
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

export class Button {

    @Input() element: Param;
    @Input() payload: string;
    @Input() form: FormGroup;
    @Output() buttonClickEvent = new EventEmitter();
    private label: string;
    private imagesPath: string;

    constructor( private pageService: PageService, private wcs: WebContentSvc, private location: Location ) {
        wcs.content$.subscribe( result => {
            this.label = result.label;
        } );
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
        this.imagesPath = ServiceConstants.IMAGES_URL;
        this.wcs.getContent( this.element.config.code );
        this.payload = this.element.config.uiStyles.attributes.payload;
        this.buttonClickEvent.subscribe(( $event ) => {
            //console.log( $event );
            //let payload = JSON.parse(JSON.stringify(this.payload));

            this.pageService.processEvent( $event.element.path, $event.element.config.uiStyles.attributes.b,
                null, $event.element.config.uiStyles.attributes.method );
        } );
    }

    onSubmit() {
        let item: GenericDomain = new GenericDomain();
        item = this.form.value;
        let navLink = this.element.config.uiStyles.attributes.navLink;
        let domainParams;
        if ( navLink != null ) {
            domainParams = this.getAllURLParams( navLink );
        }
        if ( domainParams != null ) {
            for ( let domainParam of domainParams ) {
                let p = domainParam.substring( 1, domainParam.length - 1 );
                if ( item[p] ) {
                    navLink = navLink.replace( new RegExp( domainParam, 'g' ), item[p] );
                } else {
                    navLink = '';
                    break;
                }
            }
        }
        //this.pageSvc.processEvent(this.element.config.uiStyles.attributes.submitUrl, this.element.config.uiStyles.attributes.b, item,'POST',navLink);
        this.pageService.processEvent( this.element.path, this.element.config.uiStyles.attributes.b, item, 'POST', navLink );
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
