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
import { ComponentTypes } from '../../../../shared/param-annotations.enum';

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
        <button  *ngIf="element?.visible == true" class="{{element.config?.uiStyles?.attributes?.cssClass}}" aria-label="action menu"  attr.aria-expanded="{{isOpen}}"  (click)="toggleOpen($event)" type="button" [disabled]="(enabled !== undefined && !enabled) ? true : null">
            <nm-image *ngIf="element.config?.uiStyles?.attributes?.imgSrc" [name]="element.config?.uiStyles?.attributes?.imgSrc" [type]="element.config?.uiStyles?.attributes?.imgType" [cssClass]=""></nm-image>
        </button>
        <div class="dropdownContent" 
            [ngClass]="{'displayNone': isHidden}" 
            [@dropdownAnimation]='state' 
            (@dropdownAnimation.start)="animationStart($event)" 
            (@dropdownAnimation.done)="animationDone($event)" 
            attr.aria-hidden="{{isHidden}}">
            <ng-template [ngIf]="rowData">
                <nm-action-link
                    [elementPath]="elementPath" 
                    [rowData]="rowData"
                    [element]="element?.type?.model?.params[i]"
                    *ngFor="let param of params; index as i">
                </nm-action-link>
            </ng-template>
            <ng-template [ngIf]="!rowData">
                <ng-template ngFor let-ele [ngForOf]="element?.type?.model?.params">
                    <ng-template [ngIf]="ele.alias == 'Link'">
                        <nm-link [element] = "ele"> </nm-link>
                    </ng-template>
                 </ng-template>
           </ng-template>
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
        <ng-template [ngIf]="visible">
            <ng-template [ngIf]="value == componentTypes.external.toString()">
                <!-- External Link: Enabled -->
                <ng-template [ngIf]="enabled">
                    <a 
                        [href]="url" 
                        [class]="cssClass" 
                        [target]="target" 
                        [rel]="rel">
                            {{label}}
                    </a>
                </ng-template>
                <!-- External Link: Disabled -->
                <ng-template [ngIf]="enabled !== undefined && !enabled">
                    <a 
                        [class]="cssClass" 
                        [class.disabled]="true"
                        [rel]="rel">
                            {{label}}
                    </a>
                </ng-template>
            </ng-template>
            <ng-template [ngIf]="value != componentTypes.external.toString()">
                <!-- General Link -->
                <a 
                    [class]="cssClass" 
                    [class.disabled]="enabled !== undefined && !enabled" 
                    (click)="processOnClick(code)">
                        {{label}}
                </a>
            </ng-template>
        </ng-template>
    `
})
export class ActionLink extends BaseElement{
    
        @Input() elementPath: string;
        @Input() rowData: any;
        protected url:string;
        componentTypes = ComponentTypes;

        constructor(private _wcs: WebContentSvc, private pageSvc: PageService) {
            super(_wcs);
        }
    
        ngOnInit() {
            this.loadLabelConfigFromConfigs(this.element.labels, this.element.config.code);

            // replace parameters in url enclosed within {}
            if (this.element.config && this.element.config.uiStyles && this.element.config.uiStyles.attributes && this.element.config.uiStyles.attributes.url) {
                this.url = this.element.config.uiStyles.attributes.url;
                let urlParams: string[] = this.getAllURLParams(this.url);
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

        get rel(): string {
            return this.element.config.uiStyles.attributes.rel;
        }

        get target(): string {
            return this.element.config.uiStyles.attributes.target;
        }

        get value(): string {
            return this.element.config.uiStyles.attributes.value;
        }
    }


