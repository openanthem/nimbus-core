/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */
import { Component, ElementRef, Input, OnInit, OnDestroy } from '@angular/core';
import { Param, Model } from '../../../shared/app-config.interface';
import { DialogModule } from 'primeng/primeng';
import { WebContentSvc } from './../../../services/content-management.service';
import { BaseElement } from '../base-element.component';

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
    templateUrl: './modal.component.html'
})

export class Modal extends BaseElement implements OnInit, OnDestroy {
    // width of modal window
    public _width: string;
    // closable to indicate whether modal window can be closed
    public _closable: boolean;
    // title of modal window
    public title: string;

    constructor(private wcsvc: WebContentSvc) {
        super(wcsvc);
    }

    ngOnInit(): void {
        //let modal = this;
        this.title = this.element.config.uiStyles.attributes.title;
    }

    ngOnDestroy() {
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
        return this.element.config.uiStyles.attributes.width;
    }

}
