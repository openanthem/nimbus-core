/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';
import { Length } from './../../../../shared/app-config.interface';
import { Component, Input, Output, EventEmitter, ViewEncapsulation } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Location } from '@angular/common';
import { GenericDomain } from './../../../../model/generic-domain.model';
import { Param } from '../../../../shared/param-state';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { ServiceConstants } from './../../../../services/service.constants';
import { BaseElement } from '../../base-element.component';
import { FileService } from '../../../../services/file.service';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component( {
    selector: 'nm-button',
    providers: [WebContentSvc],
    template: `
        <ng-template [ngIf]="!element.config?.uiStyles?.attributes?.imgSrc">
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style=='PRIMARY' && element?.visible == true">
                <button class="btn btn-primary" (click)="onSubmit()" type="{{element.config?.uiStyles?.attributes?.type}}" [disabled]="!form.valid">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style=='SECONDARY' && element?.visible == true">
                <button class="btn btn-secondary" [disabled]="disabled" (click)="emitEvent(this)" type="{{element.config?.uiStyles?.attributes?.type}}">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style=='PLAIN' && element?.visible == true">
                <button class="btn btn-plain" (click)="emitEvent(this)" [disabled]="disabled" type="{{element.config?.uiStyles?.attributes?.type}}">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style=='DESTRUCTIVE' && element?.visible == true">
                <button class="btn btn-delete" (click)="emitEvent(this)" [disabled]="disabled" type="{{element.config?.uiStyles?.attributes?.type}}">{{label}}</button>
            </ng-template>
        </ng-template>
        <ng-template [ngIf]="element.config?.uiStyles?.attributes?.imgSrc">
           <button (click)="emitEvent(this)" [disabled]="disabled" type="button" class="{{element.config?.uiStyles?.attributes?.cssClass}} ">
                    <i class="fa fa-fw {{element.config?.uiStyles?.attributes?.imgSrc}}" aria-hidden="true"></i>{{label}}</button>
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
        this.disabled = !this.element.enabled;
        this.imagesPath = ServiceConstants.IMAGES_URL;
        this.payload = this.element.config.uiStyles.attributes.payload;
        this.buttonClickEvent.subscribe(( $event ) => {

            this.pageService.processEvent( $event.element.path, $event.element.config.uiStyles.attributes.b,
                null, $event.element.config.uiStyles.attributes.method );
        } );

        this.pageService.validationUpdate$.subscribe(event => {
            if(event.path == this.element.path) {
                this.disabled = !event.enabled;
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
