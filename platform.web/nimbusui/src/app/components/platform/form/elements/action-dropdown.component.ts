/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { ElementModelParam } from './../../../../shared/app-config.interface';
import { Behavior } from './../../../../shared/command.enum';
import { Component, Input } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/app-config.interface';
import { GenericDomain } from './../../../../model/generic-domain.model';
import { HttpMethod } from '../../../../shared/command.enum';

@Component({
    selector: 'nm-action-dropdown',
    template: `
    <div class="custom-dropdown {{widgetPosition}}" [ngClass]="{'open': isOpen}">
        <button class="dropdownTrigger" attr.aria-expanded="{{isOpen}}" (click)="toggleOpen($event)"></button> 
        <div class="dropdownContent" attr.aria-hidden="{{!isOpen}}">
            <nm-action-link [elementPath]="elementPath" [rowData]="rowData" [param]="param" *ngFor="let param of params"></nm-action-link>
        </div>
    </div>
  `
})
export class ActionDropdown {

    @Input() params: ElementModelParam[];
    @Input() elementPath: string;
    @Input() rowData: any;
    isOpen: boolean = false;
    widgetPosition: string;
    
    constructor(private _wcs: WebContentSvc, private pageSvc: PageService) {
    }

    ngOnInit() {
        // console.log(this.params);
        // console.log(this.elementPath + '/');
    }

    toggleOpen(event: MouseEvent): void {
        event.preventDefault();
        if(window.innerWidth - event.clientX <= 400){
            this.widgetPosition = "west"
        }
        else{this.widgetPosition = "east"}
        //console.log("x =",event.screenX,"y =",event.screenY, "broswer inner width", window.innerWidth,"broswer outer width", window.outerWidth);
        this.isOpen = !this.isOpen;
        //console.log("widgetPosition",this.widgetPosition, window.innerWidth,"-",event.clientX,"=",window.innerWidth-event.clientX  );
    }

    processOnClick(linkCode: string) {
        let item: GenericDomain = new GenericDomain();
        this.pageSvc.processEvent(this.elementPath + '/' + linkCode, Behavior.execute.value, item, HttpMethod.GET.value);
    }
}

@Component({
    selector: 'nm-action-link',
    providers: [
        WebContentSvc
    ],
    template: `
        <ng-template [ngIf]="param.uiStyles.attributes.value =='EXTERNAL'">
            <a href="{{url}}" class="{{param.uiStyles?.attributes?.cssClass}}" target="{{param.uiStyles?.attributes?.target}}" rel="{{param.uiStyles?.attributes?.rel}}">{{label}}</a>
        </ng-template>
        <ng-template [ngIf]="param.uiStyles.attributes.value !='EXTERNAL'">
            <a href="javascript:void(0)" (click)="processOnClick(this.param.code)">{{label}}</a>
        </ng-template>
    `
})
export class ActionLink {
    
        @Input() param: ElementModelParam;
        @Input() elementPath: string;
        @Input() rowData: any;
        protected label: string;
        protected url:string;
        
        constructor(private wcs: WebContentSvc, private pageSvc: PageService) {
            wcs.content$.subscribe(result => {
                if (this.param && result.id === this.param.code) {
                    this.label = result.label;
                }
            });
        }
    
        ngOnInit() {
            if (this.param && this.param.code) {
                this.wcs.getContent(this.param.code);
            }
            // replace parameters in url enclosed within {}
            if (this.param.uiStyles && this.param.uiStyles.attributes && this.param.uiStyles.attributes.url) {
                this.url = this.param.uiStyles.attributes.url;
                let urlParams: string[] = this.getAllURLParams(this.param.uiStyles.attributes.url);
                if (urlParams && urlParams.length > 0) {
                    if(urlParams!=null) {
                        for (let urlParam of urlParams) {
                            let p = urlParam.substring(1, urlParam.length-1);
                            if (this.rowData[p]) {
                                this.url = this.url.replace(new RegExp(urlParam, 'g'), this.rowData[p]);
                            }
                        }
                    }
                }
            }
            console.log(this.url);
        }

        getAllURLParams (url: string): string[] {
            var pattern = /{([\s\S]*?)}/g;
            return url.match(pattern);
        }
    
        processOnClick(linkCode: string) {
            let item: GenericDomain = new GenericDomain();
            this.pageSvc.processEvent(this.elementPath + '/' + linkCode, Behavior.execute.value, item, HttpMethod.GET.value);
        }
    }


