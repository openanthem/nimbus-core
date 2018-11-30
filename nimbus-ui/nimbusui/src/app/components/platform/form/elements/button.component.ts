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
import { Component, Input, Output, EventEmitter, KeyValueDiffer, KeyValueDiffers } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Location } from '@angular/common';
import { GenericDomain } from './../../../../model/generic-domain.model';
import { Param } from '../../../../shared/param-state';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { ServiceConstants } from './../../../../services/service.constants';
import { BaseElement } from '../../base-element.component';
import { FileService } from '../../../../services/file.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoggerService } from '../../../../services/logger.service';
import { ComponentTypes } from '../../../../shared/param-annotations.enum';
import { PrintService } from './../../../../services/print.service';
import { ViewConfig } from './../../../../shared/param-annotations.enum';
import { ParamUtils } from './../../../../shared/param-utils';
import { PrintConfig } from './../../../../shared/print-event';

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
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style==componentTypes.primary.toString() && element?.visible == true">
                <button class="btn btn-primary"  eventpropagation (clickEvnt)="onSubmit()" [path]="element.path" type="{{element.config?.uiStyles?.attributes?.type}}" [disabled]="!form.valid">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style==componentTypes.secondary.toString() && element?.visible == true">
                <button class="btn btn-secondary" [disabled]="disabled" eventpropagation (clickEvnt)="emitEvent($event)" [path]="element.path" type="{{element.config?.uiStyles?.attributes?.type}}">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style==componentTypes.plain.toString() && element?.visible == true">
                <button class="btn btn-plain {{cssClass}}" eventpropagation (clickEvnt)="emitEvent($event)" [path]="element.path" [disabled]="disabled" type="{{element.config?.uiStyles?.attributes?.type}}">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style==componentTypes.destructive.toString() && element?.visible == true">
                <button class="btn btn-delete" eventpropagation (clickEvnt)="emitEvent($event)" [path]="element.path" [disabled]="disabled" type="{{element.config?.uiStyles?.attributes?.type}}">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style==componentTypes.validation.toString() && element?.visible == true">
                <button class="btn btn-primary" eventpropagation (clickEvnt)="emitEvent($event)" [path]="element.path" [disabled]="form.valid">{{label}}</button>
            </ng-template>
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.style==componentTypes.print.toString() && element?.visible == true">
                <button class="btn btn-secondary" eventpropagation (clickEvnt)="emitEvent($event)" [path]="element.path" [disabled]="disabled">{{label}}</button>
            </ng-template>
        </ng-template>
        
        <ng-template [ngIf]="element.config?.uiStyles?.attributes?.imgSrc && element?.visible == true">
            <button eventpropagation (clickEvnt)="emitEvent($event)" [path]="element.path" [disabled]="disabled" title="{{element.config?.uiStyles?.attributes?.title}}" type="button" class="{{btnClass}}">
                <nm-image [name]="element.config?.uiStyles?.attributes?.imgSrc" [type]="element.config?.uiStyles?.attributes?.imgType" [cssClass]=""></nm-image>
                {{label}}
            </button>    
        </ng-template>
    `
} )

export class Button extends BaseElement {

    @Input() element: Param;
    @Input() payload: string;
    @Input() form: FormGroup;
    @Input() actionTray?: boolean;

    @Output() buttonClickEvent = new EventEmitter();

    @Output() elementChange = new EventEmitter();
    private imagesPath: string;
    private btnClass: string;
    private disabled: boolean;
    files: any;
    differ: KeyValueDiffer<any, any>;
    componentTypes = ComponentTypes;

    constructor( private pageService: PageService, private _wcs: WebContentSvc, 
        private location: Location, private fileService: FileService, private http: CustomHttpClient, private logger: LoggerService, private differs: KeyValueDiffers,
        private printService: PrintService) {
        super(_wcs);
    }

    emitEvent( $event: any ) {
        //browserback to go back to previous page
        if(this.element.config.uiStyles != null && this.element.config.uiStyles.attributes.browserBack) {
            this.location.back();
        } else if(this.element.config.uiStyles != null && this.element.config.uiStyles.attributes.style === this.componentTypes.validation.toString()){
            this.validate(this.form);
        } else if(this.element.config.uiStyles != null && this.element.config.uiStyles.attributes.style === this.componentTypes.print.toString()) {
            this.handlePrint($event);
        } else {
            this.pageService.processEvent( this.element.path, this.element.config.uiStyles.attributes.b,
                null, this.element.config.uiStyles.attributes.method );
        }
    }

    ngOnInit() {        
        super.ngOnInit();

        this.disabled = !this.element.enabled;
        this.imagesPath = ServiceConstants.IMAGES_URL;
        this.payload = this.element.config.uiStyles.attributes.payload;
        this.differ = this.differs.find(this.element).create();

        if (this.label && this.label.trim() !== '') {
            this.btnClass = 'btn btn-plain ' + this.cssClass;
        } else {
            this.btnClass = 'btn btn-icon icon ' + this.cssClass;
        }

        this.pageService.validationUpdate$.subscribe(event => {
            if(event.path == this.element.path) {
                this.disabled = !event.enabled;
            }
        });
    }

    /*  This method will be run for every change detection cycle.  This is used to detect changes within 
        element Object. We could create a custom subject in page service and emit there if this causes any issues.
    */
    ngDoCheck(): void {
        if(this.actionTray && this.differ){
            const elementChanges = this.differ.diff(this.element);
            if(elementChanges){
                this.elementChange.emit();
            }
        }
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

    handlePrint($event: any) {
        let printConfig = PrintConfig.fromNature(ParamUtils.getUiNature(this.element, ViewConfig.printConfig.toString()));
        let printPath = this.element.config.uiStyles.attributes.printPath;
        // If printPath is not provided, default is to use the page
        if (!printPath) {
            printPath = ParamUtils.getDomainPageFromPath(this.element.path);
        }
        this.printService.emitPrintEvent(printPath, $event, printConfig);
    }

    onSubmit() {
        let item: GenericDomain = new GenericDomain();
        item = this.form.value;
        // Check for File upload parameters ('fileControl').
        if (item['fileControl']) {
            let files: File[] = item['fileControl'];
            delete item['fileControl'];
            for(let p=0; p<files.length; p++){
                this.fileService.uploadFile(files[p], this.form)
                    .subscribe(data => {
                        item['fileId'] = data;  
                        item['name'] = files[p]['name'];
                        this.pageService.processEvent( this.element.path, this.element.config.uiStyles.attributes.b, item, 'POST' );              
                    },
                    error => this.logger.error(error),
                    () => this.logger.info('File uploaded ..')
                );
                 
            }
            
        } else {
            this.pageService.processEvent( this.element.path, this.element.config.uiStyles.attributes.b, item, 'POST' );
        }
        // Form reset after submit
        this.reset();
        
    }

    validate(formGroup: FormGroup) {
        Object.keys(formGroup.controls).forEach(field => {
             let ctrl = formGroup.controls[field];
             if(ctrl instanceof FormControl) {
                 ctrl.markAsDirty({onlySelf:true})
             }
             else if (ctrl instanceof FormGroup) {
                 this.validate(ctrl);
             }
        });
             
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

