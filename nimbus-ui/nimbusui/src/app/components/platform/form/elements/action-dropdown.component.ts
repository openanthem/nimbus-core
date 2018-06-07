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

import { trigger,state,style,transition,animate,keyframes } from '@angular/animations';
import { Component, Input, ViewChild, Output, EventEmitter, ElementRef, HostListener } from '@angular/core';
import { LabelConfig } from './../../../../shared/param-config';
import { Behavior } from './../../../../shared/command.enum';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { ParamConfig } from '../../../../shared/param-config';
import { Param } from '../../../../shared/param-state';
import { GenericDomain } from './../../../../model/generic-domain.model';
import { HttpMethod } from '../../../../shared/command.enum';
import { BaseElement } from './../../base-element.component';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-action-dropdown',
    template: `
    <div class="action-dropdown" [hidden]="!element?.visible">
        <button class="dropdownTrigger" 
            aria-label="action menu" 
            attr.aria-expanded="{{isOpen}}" 
            (click)="toggleOpen($event)"
            [disabled]="(enabled !== undefined && !enabled) ? true : null">
        </button> 
        <div class="dropdownContent" 
            [ngClass]="{'displayNone': isHidden}" 
            [@dropdownAnimation]='state' 
            (@dropdownAnimation.start)="animationStart($event)" 
            (@dropdownAnimation.done)="animationDone($event)" 
            attr.aria-hidden="{{isHidden}}">
            <nm-action-link
                [elementPath]="elementPath" 
                [rowData]="rowData" 
                [param]="param"
                [element]="element?.type?.model?.params[i]"
                *ngFor="let param of params; index as i">
            </nm-action-link>
        </div>
    </div>
  `
  ,
  animations: [
    trigger('dropdownAnimation', [
        state('openPanel', style({
            maxHeight: '300px',
        })),
        state('closedPanel', style({
            maxHeight: '0',
        })),
        
        transition('closedPanel => openPanel', animate('300ms ease-in')),
        transition('openPanel => closedPanel', animate('300ms ease-out')),
     ]),
]
})
export class ActionDropdown {

    @Input() element: Param;
    @Input() params: ParamConfig[];
    @Input() elementPath: string;
    @Input() rowData: any;
    isOpen: boolean = false;
    isHidden: boolean = true;
    state: string = "closedPanel";
    selectedItem: boolean = false;

    @Output() dropDownClick: EventEmitter<any> = new EventEmitter();

    constructor(private _wcs: WebContentSvc, private pageSvc: PageService, private _elementRef: ElementRef) {
    }

    get elementRef(){
        return this._elementRef;
    }

    ngOnInit() {
        
    }
  
    toggleOpen( event: MouseEvent ): void {
        event.preventDefault();
        this.selectedItem = true;
        this.dropDownClick.emit(this);
    }
    processOnClick(linkCode: string) {
        let item: GenericDomain = new GenericDomain();
        this.pageSvc.processEvent(this.elementPath + '/' + linkCode, Behavior.execute.value, item, HttpMethod.GET.value);
    }
    animationStart($event) {
        this.isHidden = false;
    }
    animationDone($event) {
        if(this.isOpen == false){
            this.isHidden = true;
        }
    }
    
    get enabled(): boolean {
        if (this.element) {
            return this.element.enabled;
        }
        return undefined;
    }
}

@Component({
    selector: 'nm-action-link',
    providers: [
        WebContentSvc
    ],
    template: `
        <ng-template [ngIf]="param.uiStyles.attributes.value =='EXTERNAL'">
            <ng-template [ngIf]="enabled">
                <a href="{{url}}" class="{{param.uiStyles?.attributes?.cssClass}}" target="{{param.uiStyles?.attributes?.target}}" rel="{{param.uiStyles?.attributes?.rel}}">{{label}}</a>
            </ng-template>
            <ng-template [ngIf]="enabled !== undefined && !enabled">
                <a href="javascript:void(0)" class="{{param.uiStyles?.attributes?.cssClass}}" [class.disabled]="enabled !== undefined && !enabled" rel="{{param.uiStyles?.attributes?.rel}}">{{label}}</a>
            </ng-template>
        </ng-template>
        <ng-template [ngIf]="param.uiStyles.attributes.value !='EXTERNAL'">
            <a href="javascript:void(0)" [class.disabled]="enabled !== undefined && !enabled" (click)="processOnClick(this.param.code)">{{label}}</a>
        </ng-template>
    `
})
export class ActionLink extends BaseElement{
    
        @Input() param: ParamConfig;
        @Input() elementPath: string;
        @Input() rowData: any;
        protected url:string;
        
        constructor(private _wcs: WebContentSvc, private pageSvc: PageService) {
            super(_wcs);
        }
    
        ngOnInit() {
            this.loadLabelConfigByCode(this.param.code, this.param.labelConfigs);

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
        }

        getAllURLParams (url: string): string[] {
            var pattern = /{([\s\S]*?)}/g;
            return url.match(pattern);
        }
    
        processOnClick(linkCode: string) {
            if (undefined !== this.enabled && !this.enabled) {
                return;
            }

            let item: GenericDomain = new GenericDomain();
            this.pageSvc.processEvent(this.elementPath + '/' + linkCode, Behavior.execute.value, item, HttpMethod.GET.value);
        }

    }


