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
import { BaseElement } from './base-element.component';
import { Component, Input } from '@angular/core';
import { Param } from '../../shared/param-state';
import { WebContentSvc } from '../../services/content-management.service';
import { PageService } from '../../services/page.service';
import { GenericDomain } from './../../model/generic-domain.model';
import { ServiceConstants } from './../../services/service.constants';
import { ComponentTypes } from '../../shared/param-annotations.enum';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-link',
    providers: [
        WebContentSvc
    ],
    template: `
        <ng-template [ngIf]="imgSrc && element?.visible == true">
            <a href="{{url}}" class='{{cssClass}}'>
                <img src="{{imagesPath}}{{imgSrc}}" class=" logo" alt="{{this.label}}" />
            </a>
        </ng-template>
        <ng-template [ngIf]="!imgSrc && element?.visible == true">

            <!-- External Links -->
            <ng-template [ngIf]="value==componentTypes.external.toString()">
                <a href="{{url}}" class="{{cssClass}}" target="{{target}}" rel="{{rel}}">{{label}}</a>
            </ng-template>

            <ng-template [ngIf]="value==componentTypes.menu.toString()">
                <ng-template [ngIf]="element.mapped==false">
                    <a href="javascript:void(0)" class=" mr-2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                       
                        <svg class="">
                            <use xmlns:xlink="http://www.w3.org/1999/xlink" attr.xlink:href="{{imagesPath}}{{imgSrc}}#Layer_1"></use> 
                        </svg>
                    </a>
                    
                </ng-template>
                <ng-template [ngIf]="element.mapped==true">
                    <a href="javascript:void(0)" class=" mr-2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <ng-template ngFor let-menuParam [ngForOf]="element?.type?.model?.params">
                            <ng-template [ngIf]="menuParam.mapped == true">
                                <div class='sizeTarget'>{{menuParam.leafState}}</div>
                            </ng-template>
                        </ng-template>
                    </a>
                </ng-template>
                <div class="dropdown-menu dropdown-menu-right">
                    <ng-template ngFor let-menuParam [ngForOf]="element?.type?.model?.params">
                        <ng-template [ngIf]="menuParam.config?.uiStyles?.attributes?.alias == componentTypes.menu.toString()">
                            <ng-template ngFor let-linkParam [ngForOf]="menuParam?.type?.model?.params">
                                <nm-link [element]="linkParam" inClass="dropdown-item"></nm-link>
                            </ng-template>
                        </ng-template>
                    </ng-template>
                </div>
            </ng-template>
            <ng-template [ngIf]="value==componentTypes.default.toString()">
                <a class="{{cssClass}}" href="javascript:void(0)" (click)="processOnClick(element.path, method, b)">
                    {{this.label}}
                </a>
            </ng-template>
            <ng-template [ngIf]="value==componentTypes.inline.toString()">
                <a class="{{cssClass}}" href="javascript:void(0)" title="{{this.element.leafState}}" (click)="processOnClick(element.path, 'GET', '$executeAnd$nav')">
                    {{this.element.leafState}}
                </a>
            </ng-template>
        </ng-template>
    `
})

export class Link extends BaseElement {

    @Input() element: Param;
    @Input() root: Param;
    @Input() inClass: string;
    @Input() renderAsLink: boolean;
    @Input() rowData?: any;
    private linkClass: string = 'basicView';
    private imagesPath: string;
    private url: string;
    componentTypes = ComponentTypes;


    constructor(private _wcs: WebContentSvc, private pageSvc: PageService) {
        super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();
        this.imagesPath = ServiceConstants.IMAGES_URL;
        if (this.inClass) {
            this.linkClass = this.inClass;
        }
        if (this.element.config &&
            this.element.config.uiStyles.attributes && this.element.config.uiStyles.attributes.url) {
            if (this.rowData) {
                this.url = this.element.config.uiStyles.attributes.url;
                let urlParams: string[] = this.getAllURLParams(this.element.config.uiStyles.attributes.url);
                if (urlParams && urlParams.length > 0) {
                    if (urlParams != null) {
                        for (let urlParam of urlParams) {
                            let p = urlParam.substring(1, urlParam.length - 1);
                            if (this.rowData[p]) {
                                this.url = this.url.replace(new RegExp(urlParam, 'g'), this.rowData[p]);
                            }
                        }
                    }
                }
            } else {
                this.url = this.resolveUrl();
            }

        }
        this.pageSvc.eventUpdate$.subscribe(event => {
            if (event.path == this.element.path && event.leafState && !this.rowData) {
                this.url = this.resolveUrl();
            }
        });
    }

    resolveUrl() {
        let resolvedUrl = this.element.config.uiStyles.attributes.url;

        if (this.element.leafState) {
            return resolvedUrl.replace(new RegExp("{" + this.element.config.code + "}.*", "g"), this.element.leafState);
        }
        else {
            return resolvedUrl;
        }
    }

    getAllURLParams(url: string): string[] {
        var pattern = /{([\s\S]*?)}/g;
        return url.match(pattern);
    }

    processOnClick(uri: string, httpMethod: string, behaviour: string) {
        let item: GenericDomain = new GenericDomain();
        if (this.root) {
            this.root.type.model.params.forEach(ele => {
                if (ele.path === this.element.path.slice(0, this.element.path.lastIndexOf('/')) + '/id') {
                    item.addAttribute('id', ele.leafState);
                }
            });
        }

        this.pageSvc.processEvent(uri, behaviour, item, httpMethod);
    }

    /**
     * The value attribute for this Param.
     */
    get value(): string {
        const value = this.element.config.uiStyles.attributes.value;
        if (value) {
            return value;
        } else {
            return 'INLINE';
        }
    }

    /**
     * The method attribute for this Param.
     */
    get method(): string {
        return this.element.config.uiStyles.attributes.method;
    }

    /**
     * The b attribute for this Param.
     */
    get b(): string {
        return this.element.config.uiStyles.attributes.b;
    }

    /**
     * The target attribute for this Param.
     */
    get target(): string {
        return this.element.config.uiStyles.attributes.target;
    }

    /**
     * The rel attribute for this Param.
     */
    get rel(): string {
        return this.element.config.uiStyles.attributes.rel;
    }
}
