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

import { Component, ElementRef, Input, OnInit, OnDestroy, ViewChild, NgZone} from '@angular/core';
import { Param, Model } from '../../../shared/param-state';
import { DialogModule } from 'primeng/primeng';
import { WebContentSvc } from './../../../services/content-management.service';
import { PageService } from '../../../services/page.service';
import { HttpMethod, Behavior} from './../../../shared/command.enum';
import { GenericDomain } from '../../../model/generic-domain.model';
import { BaseElement } from '../base-element.component';
import { ViewComponent, ComponentTypes } from '../../../shared/param-annotations.enum';
import { Dialog } from 'primeng/primeng';
import { DomHandler } from 'primeng/components/dom/domhandler';
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
        WebContentSvc, DomHandler
    ]
})
export class Modal extends BaseElement implements OnInit, OnDestroy {
    
    @ViewChild('modal') modal: Dialog;
    
    // width of modal window
    public _width: string;
    // closable to indicate whether modal window can be closed
    public _closable: boolean;
    display: boolean = false;
    private _resizable: boolean;
    private elementCss: string;
    viewComponent = ViewComponent;
    componentTypes = ComponentTypes;
    currentHeight: number;

    readonly modalSize: { [id: string]: IModalSize, } = {
        SMALL: { width: '500' },
        MEDIUM: { width: '700' },
        LARGE: { width: '900' }
    };

    constructor(private wcsvc: WebContentSvc, public domHandler: DomHandler, private pageSvc: PageService, private zone: NgZone) {
        super(wcsvc);
    }

    ngOnDestroy() {
    }

    ngAfterViewInit() {
        //override the primeng definition as the modal does not have scrollbar and also does not position correctly with grid data (pagesize = 50)
        this.modal.positionOverlay = () => {
            let viewport = this.domHandler.getViewport();
            if (this.domHandler.getOuterHeight(this.modal.container) > viewport.height) {
                 this.modal.contentViewChild.nativeElement.style.height = (viewport.height * .75) + 'px';
            }
            
            if (this.modal.positionLeft >= 0 && this.modal.positionTop >= 0) {
                this.modal.container.style.left = this.modal.positionLeft + 'px';
                this.modal.container.style.top = this.modal.positionTop + 'px';
            }
            else if (this.modal.positionTop >= 0) {
                this.modal.center();
                this.modal.container.style.top = this.modal.positionTop + 'px';
            }
            else{
                this.modal.center();
            }
        }

        this.pageSvc.eventUpdate$.subscribe(event => {
            if(event.path == this.element.path) {
                this.display = event.visible;
            }
        });
    }

    ngAfterViewChecked() {
        this.zone.runOutsideAngular(() => {
            setTimeout(() => {
                if(this.modal.visible && this.modal.container) {
                    let height = this.domHandler.getOuterHeight(this.modal.container);

                    if(height !== this.currentHeight) {
                        this.currentHeight = height;
                        this.modal.positionOverlay();
                    }
                }
            }, 50);
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
        if (!myWidth) {
            return undefined;
        }
        let modalSize = this.modalSize[myWidth.toUpperCase()];
        return modalSize ? modalSize.width : myWidth;
    }

    /**
     * Close diaglog function.
     */
    public closeDialog(open: any) {
        if(!open) {
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

export interface IModalSize {
    width: string;
};