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
import { NgModel, NG_VALUE_ACCESSOR, ControlValueAccessor, FormGroup } from '@angular/forms';
import { Component, ViewChild, ElementRef, forwardRef, Input, ChangeDetectorRef } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import 'rxjs/add/observable/fromEvent';
import 'rxjs/add/operator/takeUntil';
import 'rxjs/add/operator/pairwise';
import 'rxjs/add/operator/switchMap';

import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/param-state';
import { ControlSubscribers } from '../../../../services/control-subscribers.service';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => Signature),
  multi: true
};

/**
 * \@author Rakesh.Patel
 * \@author Tony Lopez
 * \@whatItDoes 
 *  Component to capture/display user's signature
 * \@howToUse 
 *  
 */
@Component({
  selector: 'nm-signature',
  providers: [ CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc, ControlSubscribers],
  template: `
    <div style="position:relative" class="{{zoomClass}}">
        <label *ngIf="hidden!=true"
            [ngClass]="{'required': requiredCss, '': !requiredCss}"
            [attr.for]="element.config?.code">{{label}} 
            <nm-tooltip *ngIf="helpText" 
                [helpText]='helpText'>
            </nm-tooltip>
        </label>
        <ng-template [ngIf]="!disabled">
            <canvas #canvas
                [id]="element.config?.code" 
                class="form-control" ngDefaultControl>
            </canvas>
            <div class="text-sm-center buttonGroup signatureCtrls" [style.width.px]="width">
                <ng-template [ngIf]="save">
                    <button (click)="acceptSignature()" type="button" class="btn btn-secondary post-btn">
                        {{element.config?.uiStyles?.attributes?.acceptLabel}}
                    </button>
                </ng-template>
                <button (click)="clearSignature()" type="button" class="btn btn-secondary post-btn">
                    {{element.config?.uiStyles?.attributes?.clearLabel}}
                </button>
                <button class="btn btn-plain" (click)="zoomCanvas()" *ngIf="zoomFactor==1"><i class="fa fa-fw fa-plus-square" aria-hidden="true"></i>Zoom In</button>
                <button class="btn btn-plain" (click)="shrinkCanvas()" *ngIf="zoomFactor==2"><i class="fa fa-fw fa-minus-square" aria-hidden="true"></i>Zoom Out</button>
            </div>
            <img #img [src]="value != null || value != '' ? value : defaultEmptyImage" (load)="onImgLoad()" style='display: none;' />
        </ng-template>
        <ng-template [ngIf]="disabled">
            <img #img src="{{value}}" />
        </ng-template>
    </div>
   `
})
export class Signature extends BaseControl<String> {
    @ViewChild(NgModel) model: NgModel;
    
    @ViewChild('canvas') canvas: ElementRef;
    canvasEl: HTMLCanvasElement;
    cx: CanvasRenderingContext2D;

    @ViewChild('img') img: ElementRef;
    imgElement: HTMLImageElement;
    
    @Input() element: Param;
    
    width: number;
    height: number;
    save: boolean = true;
    zoomClass: string = '';
    zoomFactor: number = 1;
    defaultEmptyImage: string = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAVkAAAA8CAYAAADMvMmGAAAB+klEQVR4Xu3UsQ0AAAjDMPr/01yRzRzQwULZOQIECBDIBJYtGyZAgACBE1lPQIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBB41fMAPZcifoIAAAAASUVORK5CYII=";
    
    isCapturing: boolean = false;
    
    constructor(wcs: WebContentSvc, controlService: ControlSubscribers, cd:ChangeDetectorRef) {
        super(controlService, wcs, cd);
    }

    ngOnInit() {
        super.ngOnInit();
        if(this.element.config !== undefined) {
            this.width = Number(this.element.config.uiStyles.attributes.width);
            this.height = Number(this.element.config.uiStyles.attributes.height);
        }
    }

    ngAfterViewInit() {
        super.ngAfterViewInit();
        if(this.img !== undefined) {
            this.imgElement = this.img.nativeElement;
        }
        if(this.element.enabled) {
            this.initCanvasElement();
            this.captureEvents(this.canvasEl);
        }
    }

    initCanvasElement() {
        this.canvasEl = this.canvas.nativeElement;
        this.cx = this.canvasEl.getContext('2d');

        this.canvasEl.width = this.width;
        this.canvasEl.height = this.height;

        this.cx.lineWidth = 3;
        this.cx.lineCap = 'round';
        this.cx.strokeStyle = '#000';
     }
    
     /**
      * Provided <tt>canvasEl</tt>, performs subscriptions that should occur related to "capturing"
      * user input on an HTML canvas element, using the usual observer/subscriber pattern.
      * 
      * @param canvasEl the HTML Canvas element to register subscribers with
      */
    captureEvents(canvasEl: HTMLCanvasElement) {
        switch (this.captureType) {
            case 'DEFAULT': {
                this.registerDefaultCapture(canvasEl);
                break;
            }
            case 'ON_CLICK': {
                this.registerOnClickCapture(canvasEl);
                break;
            }
        }
    }

    onImgLoad() {
        if(this.element.enabled) {
            this.initCanvasElement();
        }
        this.cx.clearRect(0, 0, this.width, this.height);
        if(this.img.nativeElement.src != this.defaultEmptyImage ){
            this.cx.drawImage(this.imgElement, 0, 0, this.width, this.height);
            this.toggleSave(false);
        }
    }

    clearSignature() {
        this.cx.clearRect(0, 0, this.width, this.height);
        this.value = '';
        this.imgElement.src = "";
        super.emitValueChangedEvent(this, '');
        this.toggleSave(true);
    }

    acceptSignature() {
        var imageData: string = this.canvasEl.toDataURL();
        if(imageData == this.defaultEmptyImage) {
            this.clearSignature();
        }
        else {
            this.value = imageData;
            this.imgElement.src = imageData;
            super.emitValueChangedEvent(this, this.value);
            this.toggleSave(false);
        }
        this.shrinkCanvas();
    }

    toggleSave(mode: boolean) {
        this.save = mode;
    }

    zoomCanvas() {
        this.zoomFactor = 2;
        this.zoomClass = 'zoom';
    }

    shrinkCanvas() {
        this.zoomFactor = 1;
        this.zoomClass = '';
    }

    get captureType() {
        return this.element.config.uiStyles.attributes.captureType;
    }

    /**
     * Uses the subscriber pattern to capture user input on an HTML canvas element.
     * 
     * Applies a capture strategy of <tt>ON_CLICK</tt>, where user input is captured when the user clicks on <tt>canvasEl</tt>, and continues to 
     * capture until the user clicks on <tt>canvasEl</tt> again.
     *  
     * @param canvasEl the HTML Canvas element to register subscribers with
     */
    private registerOnClickCapture(canvasEl: HTMLCanvasElement) {
        const canvasElClick = Observable.fromEvent(canvasEl, 'click');
        
        canvasElClick.subscribe((e) => this.isCapturing = !this.isCapturing);
        
        canvasElClick
            .switchMap((e) => {
                return Observable
                    .fromEvent(canvasEl, 'mousemove')
                    .takeUntil(Observable
                        .fromEvent(canvasEl, 'click')
                    )
                    .pairwise()
            })
            .subscribe((res: [MouseEvent, MouseEvent]) => {
                if (this.isCapturing) {
                    this.drawOnCanvas(canvasEl, res[0], res[1]);
                }
            }
        );
    }

    /**
     * Uses the subscriber pattern to capture user input on an HTML canvas element.
     * 
     * Applies a capture strategy of <tt>DEFAULT</tt>, where user input is captured when the user clicks within <tt>canvasEl</tt> 
     * and holds the left mouse button down, and continues to capture until the left mouse button is released within <tt>canvasEl</tt>.
     *  
     * @param canvasEl the HTML Canvas element to register subscribers with
     */
    private registerDefaultCapture(canvasEl: HTMLCanvasElement) {
        Observable
            .fromEvent(canvasEl, 'mousedown')
            .switchMap((e) => {
                return Observable
                    .fromEvent(canvasEl, 'mousemove')
                    .takeUntil(Observable.fromEvent(canvasEl, 'mouseup'))
                    .pairwise()
            })
            .subscribe((res: [MouseEvent, MouseEvent]) => {
                this.drawOnCanvas(canvasEl, res[0], res[1]);
            }
        );
    }

    /**
     * Given a previous and current mouse event containing position information, draws a stroke between those two mouse
     * points to render the pattern the user is moving with the mouse.
     * 
     * @param canvasEl the HTML canvas element
     * @param prevEvent the previous mouse event, containing position information
     * @param currentEvent the current mouse event, containing position information
     */
    private drawOnCanvas(canvasEl: HTMLCanvasElement, prevEvent: MouseEvent, currentEvent: MouseEvent) {
        const rect = canvasEl.getBoundingClientRect();
        
        const prevPos = {
            x: (prevEvent.clientX - rect.left) / this.zoomFactor,
            y: (prevEvent.clientY - rect.top) / this.zoomFactor
        };
        
        const currentPos = {
            x: (currentEvent.clientX - rect.left) / this.zoomFactor,
            y: (currentEvent.clientY - rect.top) / this.zoomFactor
        };
        
        if (!this.cx) {
            return;
        }
        this.cx.beginPath();

        if (prevPos) {
            this.cx.moveTo(prevPos.x, prevPos.y); // from
            this.cx.lineTo(currentPos.x, currentPos.y);
            this.cx.stroke();
        }
    }
}
