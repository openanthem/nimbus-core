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

import { Component, ElementRef, Input, OnInit, OnDestroy } from '@angular/core';
import { Param, Model } from '../../../shared/param-state';
import { DialogModule } from 'primeng/primeng';
import { WebContentSvc } from './../../../services/content-management.service';
import { PageService } from '../../../services/page.service';
import { Action, HttpMethod, Behavior} from './../../../shared/command.enum';
import { GenericDomain } from '../../../model/generic-domain.model';
import { BaseElement } from '../base-element.component';
import { ViewComponent, ComponentTypes } from '../../../shared/param-annotations.enum';

/**
 * \@author Sandeep.Mantha
 * \@author Dinakar.Meda
 * \@whatItDoes Modal is a container. Modal(s) go within a Tile. Modal windows can have only Section(s).
 * 
 * \@howToUse 
 * @Modal annotation from Configuration drives this. 
 *      Attribute "type = {dialog, slider}".
 *      Attribute "title"
 *      Attribute "cssClass"
 *      Attribute "width" // TODO add from config
 * <nm-modal [element]="element"></nm-modal>
 * 
 */
@Component({
    selector: 'nm-modal',
    templateUrl: './modal.component.html',
    providers: [
        WebContentSvc
    ]
})
export class Modal extends BaseElement implements OnInit, OnDestroy {
    // width of modal window
    public _width: string;
    // closable to indicate whether modal window can be closed
    public _closable: boolean;
    display: boolean = false;
    private _resizable: boolean;
    private elementCss: string;
    viewComponent = ViewComponent;
    componentTypes = ComponentTypes;
    
    constructor(private wcsvc: WebContentSvc, private pageSvc: PageService) {
        super(wcsvc);
    }

    ngOnDestroy() {
    }

    ngAfterViewInit() {
        this.pageSvc.eventUpdate$.subscribe(event => {
            if(event.path == this.element.path) {
                this.display = event.visible;
            }
        });    
    }
    /**
     * Closable attribute. Can the Modal window be closed?
     */
    public get closable(): boolean {
        return this.element.config.uiStyles.attributes.closable;
    }

    /**
     * Width attribute. Width of the Modal Window.
     * TODO drive it through cssClass instead of a separate width attribute.
     */
    public get width(): string {
        let myWidth = this.element.config.uiStyles.attributes.width;

        if(myWidth === 'small') {
            return '500';
        }else if(myWidth === 'medium') {
            return '700';
        }else if(myWidth === 'large') {
            return '900';
        }else {
            return myWidth;
        }
        
    }

    /**
     * Close diaglog function.
     */
    public closeDialog(event: any) {
        if (!this.display) {
            this.pageSvc.processEvent(this.element.path+'/closeModal', Behavior.execute.value, new GenericDomain(), HttpMethod.GET.value);
        }
    }

    /**
     * Resizable attribute to alter the size of the modal window
     */
    public get resizable(): boolean {
        return this.element.config.uiStyles.attributes.resizable;
    }
}
