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
        <button class="btn btn-plain zoomTrigger" (click)="zoomCanvas()" *ngIf="zoomFactor==1"><i class="fa fa-plus-square" aria-hidden="true"></i>Zoom In</button>
        <button class="btn btn-plain zoomTrigger" (click)="shrinkCanvas()" *ngIf="zoomFactor==2"><i class="fa fa-minus-square" aria-hidden="true"></i>Zoom Out</button>
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
            </div>
            <img #img [src]="value != null ? value : defaultEmptyImage" (load)="onImgLoad()" style='display: none;' />
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
    
    captureEvents(canvasEl: HTMLCanvasElement) {
        Observable
            .fromEvent(canvasEl, 'mousedown')
            .switchMap((e) => {
                return Observable
                    .fromEvent(canvasEl, 'mousemove')
                    .takeUntil(Observable.fromEvent(canvasEl, 'mouseup'))
                    .pairwise()
            })
            .subscribe((res: [MouseEvent, MouseEvent]) => {
                const rect = canvasEl.getBoundingClientRect();
        
                const prevPos = {
                    x: (res[0].clientX - rect.left) / this.zoomFactor,
                    y: (res[0].clientY - rect.top) / this.zoomFactor
                };
                
                const currentPos = {
                    x: (res[1].clientX - rect.left) / this.zoomFactor,
                    y: (res[1].clientY - rect.top) / this.zoomFactor
                };
                
                this.drawOnCanvas(prevPos, currentPos);
            });
    }

    drawOnCanvas(prevPos: { x: number, y: number }, currentPos: { x: number, y: number }) {
        if (!this.cx) { return; }

        this.cx.beginPath();

        if (prevPos) {
            this.cx.moveTo(prevPos.x, prevPos.y); // from
            this.cx.lineTo(currentPos.x, currentPos.y);
            this.cx.stroke();
        }
    }

    onImgLoad() {
        if(this.element.enabled) {
            this.initCanvasElement();
            this.captureEvents(this.canvasEl);
        }
        this.cx.clearRect(0, 0, this.width, this.height);
        if(this.imgElement.src != this.defaultEmptyImage ){
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

}
