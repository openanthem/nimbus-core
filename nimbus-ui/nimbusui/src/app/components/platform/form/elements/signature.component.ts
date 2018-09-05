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
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Component, ViewChild, ElementRef, forwardRef, Input, ChangeDetectorRef } from '@angular/core';
import { fromEvent as observableFromEvent } from 'rxjs';
import { takeUntil, switchMap, pairwise } from 'rxjs/operators';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { Param } from '../../../../shared/param-state';
import { ControlSubscribers } from '../../../../services/control-subscribers.service';
import { LoggerService } from './../../../../services/logger.service';

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
        <nm-input-label *ngIf="!isLabelEmpty && hidden != true"
            [element]="element" 
            [for]="element.config?.code" 
            [required]="requiredCss">

        </nm-input-label>
        <canvas #canvas 
            [id]="element.config?.code" 
            class="form-control" ngDefaultControl>
        </canvas>
        <ng-template [ngIf]="!disabled">
            <div class="text-sm-left buttonGroup signatureCtrls" [style.width.px]="width">
                <ng-template [ngIf]="!isSaved">
                    <button (click)="save()" type="button" class="btn btn-secondary post-btn">
                        {{element.config?.uiStyles?.attributes?.acceptLabel}}
                    </button>
                </ng-template>
                <button (click)="clear()" type="button" class="btn btn-secondary post-btn">
                    {{element.config?.uiStyles?.attributes?.clearLabel}}
                </button>
                <button class="btn btn-plain" (click)="zoomCanvas()" *ngIf="zoomFactor==1"><i class="fa fa-fw fa-plus-square" aria-hidden="true"></i>Zoom In</button>
                <button class="btn btn-plain" (click)="shrinkCanvas()" *ngIf="zoomFactor==2"><i class="fa fa-fw fa-minus-square" aria-hidden="true"></i>Zoom Out</button>
                <button *ngIf="element.config?.uiStyles?.attributes?.scriptName" (click)="getUpdatedSignature()" type="button" class="btn btn-plain post-btn">
                    <i class="fa fa-fw fa-plus-circle"></i>
                    Get Updated Signature
                </button>
            </div>
        </ng-template>
    </div>
   `
})
export class Signature extends BaseControl<string> {
    
    public static readonly EVENT_NAMES = {
        CLICK: 'click',
        MOUSE_DOWN: 'mousedown',
        MOUSE_MOVE: 'mousemove',
        MOUSE_UP: 'mouseup',
    }

    @ViewChild('canvas') canvas: ElementRef;
    @Input() element: Param;
    @ViewChild(NgModel) model: NgModel;

    canvasEl: HTMLCanvasElement;
    defaultEmptyImage: string = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAVkAAAA8CAYAAADMvMmGAAAB+klEQVR4Xu3UsQ0AAAjDMPr/01yRzRzQwULZOQIECBDIBJYtGyZAgACBE1lPQIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBAQWT9AgACBUEBkQ1zTBAgQEFk/QIAAgVBAZENc0wQIEBBZP0CAAIFQQGRDXNMECBB41fMAPZcifoIAAAAASUVORK5CYII=";
    height: number;
    isCapturing: boolean = false;
    isSaved: boolean = false;
    width: number;
    zoomClass: string = '';
    zoomFactor: number = 1;
    
    constructor(
        wcs: WebContentSvc, 
        controlService: ControlSubscribers, 
        cd: ChangeDetectorRef, 
        private logger: LoggerService) {
        
            super(controlService, wcs, cd);
    }

    public ngOnInit() {
        super.ngOnInit();
        if(this.element.config !== undefined) {
            this.width = Number(this.element.config.uiStyles.attributes.width);
            this.height = Number(this.element.config.uiStyles.attributes.height);
        }        
    }

    public ngAfterViewInit() {
        super.ngAfterViewInit();
        this.initCanvasElement();

        // If the disabled property changes, ensure the canvas size is restored.
        this.controlService.onEnabledUpdateSubscriber(this, undefined, () => {
            this.shrinkCanvas();
        });
    }

    /**
     * Initialize the canvas element including applying any default rules and subscription registrations
     * that should occur.
     */
    private initCanvasElement() {
        this.canvasEl = this.canvas.nativeElement;
        this.canvasEl.width = this.width;
        this.canvasEl.height = this.height;

        this.applyContextRules();
        this.renderExistingSignature();
        this.registerCaptureEvents();
     }
    
     /**
      * Render the existing signature (if signature data exists)
      */
     private renderExistingSignature() {
        if(this.value) {
            this.logger.debug(`Found exsiting signature data to render: ${this.value}`);
            var img = new Image();
            var self = this;
            img.onload = () => self.canvasEl.getContext('2d').drawImage(img, 0, 0, self.width, self.height);
            img.src = this.value;
        }
     }

     /**
      * Apply styling rules to the canvas, such as brush size, line width, etc.
      * @param cx the canvas context object
      */
     private applyContextRules(): CanvasRenderingContext2D {
        let cx = this.canvasEl.getContext('2d');
        cx.lineWidth = 3;
        cx.lineCap = 'round';
        cx.strokeStyle = '#000';
        return cx;
     }

     /**
      * Register subscriptions that should occur related to "capturing"
      * user input on an HTML canvas element, using the usual observer/subscriber pattern.
      * The capturing event type registered will be selected based on the server-side configuration 
      * of captureType.
      */
    private registerCaptureEvents() {
        switch (this.captureType) {
            case 'DEFAULT': {
                this.registerCaptureOnEvent(Signature.EVENT_NAMES.MOUSE_DOWN, Signature.EVENT_NAMES.MOUSE_UP);
                break;
            }
            case 'ON_CLICK': {
                this.registerCaptureOnEvent(Signature.EVENT_NAMES.CLICK, Signature.EVENT_NAMES.CLICK);
                break;
            }
        }
    }

    /**
     * Clear the signature canvas data and essentially reset the component to its initial state.
     */
    public clear() {
        this.canvasEl.getContext('2d').clearRect(0, 0, this.width, this.height);
        this.value = '';
        super.emitValueChangedEvent(this, '');
        this.isSaved = false;
    }

    /**
     * Save the signature canvas data into this instances value property as a data URL string.
     */
    public save() {
        var imageData: string = this.canvasEl.toDataURL();
        if(imageData == this.defaultEmptyImage) {
            this.clear();
        } else {
            this.value = imageData;
            super.emitValueChangedEvent(this, this.value);
            this.isSaved = true;
        }
        this.shrinkCanvas();
    }

    /**
     * Zoom in the canvas to a zoomed in size.
     */
    public zoomCanvas() {
        this.zoomFactor = 2;
        this.zoomClass = 'zoom';
    }

    /**
     * Zoom out the canvas to regular size.
     */
    public shrinkCanvas() {
        this.zoomFactor = 1;
        this.zoomClass = '';
    }

    /**
     * Return the capture type as defined by the server.
     */
    public get captureType() {
        return this.element.config.uiStyles.attributes.captureType;
    }

    /**
     * Register the signature capture to start after the event related to startEventName is triggered and
     * end when the event related to endEventName is triggered.
     * @param startEventName the name of the event to start capturing
     * @param endEventName the name of the event to end capturing
     */
    private registerCaptureOnEvent(startEventName: string, endEventName: string) {
        const $startEvent = observableFromEvent(this.canvasEl, startEventName);
        const $endEvent = observableFromEvent(this.canvasEl, endEventName)

        if (startEventName === endEventName) {
            $startEvent.subscribe((e) => this.setIsCapturing(!this.isCapturing));
        } else {
            $startEvent.subscribe((e) => this.setIsCapturing(true));
            $endEvent.subscribe((e) => this.setIsCapturing(false));
        }

        $startEvent.pipe(
            switchMap((e) => {
                return observableFromEvent(this.canvasEl, Signature.EVENT_NAMES.MOUSE_MOVE).pipe(
                    takeUntil($endEvent),
                    pairwise())
            })).subscribe((res: [MouseEvent, MouseEvent]) => {
                if (this.isEditable() && this.isCapturing) {
                    this.drawOnCanvas(res[0], res[1]);
                }
            });
    }

    /**
     * Set the value of isCapturing if this signature component is not disabled or in a saved status.
     * @param isCapturing the value to set to isCapturing
     */
    private setIsCapturing(isCapturing: boolean) {
        if (this.isEditable()) {
            this.isCapturing = isCapturing;
            this.logger.debug(`Signature component is currently ${this.isCapturing ? '' : 'not '}capturing.`);
        }
    }

    private isEditable(): boolean {
        return !this.disabled && !this.isSaved;
    }

    /**
     * Draw a stroke between two mouse points to render the pattern the user is moving with the mouse.
     * @param prevEvent the previous mouse event, containing position information
     * @param currentEvent the current mouse event, containing position information
     */
    private drawOnCanvas(prevEvent: MouseEvent, currentEvent: MouseEvent) {
        const rect = this.canvasEl.getBoundingClientRect();
        
        const prevPos = {
            x: (prevEvent.clientX - rect.left) / this.zoomFactor,
            y: (prevEvent.clientY - rect.top) / this.zoomFactor
        };
        
        const currentPos = {
            x: (currentEvent.clientX - rect.left) / this.zoomFactor,
            y: (currentEvent.clientY - rect.top) / this.zoomFactor
        };
        
        let cx = this.canvasEl.getContext('2d');
        if (!cx) {
            return;
        }
        cx.beginPath();

        if (prevPos) {
            cx.moveTo(prevPos.x, prevPos.y);
            cx.lineTo(currentPos.x, currentPos.y);
            cx.stroke();
        }
    }

    getUpdatedDataUrl (updatedDataUrl){
        console.log('updatedDataUrl', updatedDataUrl);
    }

    getUpdatedSignature() {
         const callExternalFn = eval(this.element.config.uiStyles.attributes.scriptName);
         callExternalFn(this.canvasEl.toDataURL(), this.getUpdatedDataUrl);
    }
}