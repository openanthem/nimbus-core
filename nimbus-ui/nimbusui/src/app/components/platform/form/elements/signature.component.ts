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
import { Param } from '../../../../shared/app-config.interface';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => Signature),
  multi: true
};

/**
 * \@author Rakesh.Patel
 * \@whatItDoes 
 *  Component to capture user's signature
 * \@howToUse 
 *  
 */
@Component({
  selector: 'nm-signature',
  providers: [ CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc ],
  template: `
    <label *ngIf="hidden!=true"
        [ngClass]="{'required': requiredCss, '': !requiredCss}"
        [attr.for]="element.config?.code">{{label}} 
        <nm-tooltip *ngIf="helpText" 
            [helpText]='helpText'>
        </nm-tooltip>
    </label>
    <canvas #canvas
        [id]="element.config?.code" 
        (mouseout)="acceptSignature()"
        class="form-control" ngDefaultControl>
    </canvas>
    <img #img *ngIf="element.enabled != true" src="{{value}}" 
            (load)="afterLoading()" style='display: none;' />
    
   `
})
export class Signature extends BaseControl<String> implements ControlValueAccessor {
    @ViewChild(NgModel) model: NgModel;
    @ViewChild('canvas') public canvas: ElementRef;
    @ViewChild('img') img: ElementRef;

    @Input() element: Param;
    @Input() form: FormGroup;
    @Input('value') _value ;

    canvasEl: HTMLCanvasElement;
    imgElement: HTMLImageElement;
    
    @Input() public width = 400;
    @Input() public height = 400;
    
    constructor(wcs: WebContentSvc, pageService: PageService,cd:ChangeDetectorRef) {
        super(pageService,wcs,cd);
    }

    public onChange: any = (_) => { console.log("signature changed .... ") }
    public onTouched: any = () => { /*Empty*/ }
    
    get value() {
        return this._value;
    }

    set value(val) {
        this._value = val;
        this.onChange(val);
        this.onTouched();
    }

    registerOnChange(fn) {
        this.onChange = fn;
    }

    writeValue(value) {
        if (value) {
            this.value = value;
        }
    }

    registerOnTouched(fn) {
        this.onTouched = fn;
    }

    acceptSignature() {
        var imageData: String = this.canvasEl.toDataURL();
        this.value = imageData;
        super.emitValueChangedEvent(this, this.value);
    }

    private cx: CanvasRenderingContext2D;

    public ngAfterViewInit() {
        super.ngAfterViewInit();

        this.canvasEl = this.canvas.nativeElement;
        this.cx = this.canvasEl.getContext('2d');

        if(this.img !== undefined) {
            this.imgElement = this.img.nativeElement;
        }

        this.canvasEl.width = this.width;
        this.canvasEl.height = this.height;

        this.cx.lineWidth = 3;
        this.cx.lineCap = 'round';
        this.cx.strokeStyle = '#000';

        if(this.element.enabled) {
            this.captureEvents(this.canvasEl);
        }
    }
    
    afterLoading() {
        this.cx.clearRect(0, 0, this.width, this.height);
        console.log('drawImage');
        // this prints an image element with src I gave
        console.log(this.imgElement);
        this.cx.drawImage(this.imgElement, 0, 0, this.width, this.height);
    }

    // clearImage() {
    //     this.cx.clearRect(0,0,this.width, this.height);
    // }

    private captureEvents(canvasEl: HTMLCanvasElement) {
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
            x: res[0].clientX - rect.left,
            y: res[0].clientY - rect.top
            };
    
            const currentPos = {
            x: res[1].clientX - rect.left,
            y: res[1].clientY - rect.top
            };
    
            this.drawOnCanvas(prevPos, currentPos);
        });
    }

    private drawOnCanvas(prevPos: { x: number, y: number }, currentPos: { x: number, y: number }) {
        if (!this.cx) { return; }

        this.cx.beginPath();

        if (prevPos) {
        this.cx.moveTo(prevPos.x, prevPos.y); // from
        this.cx.lineTo(currentPos.x, currentPos.y);
        this.cx.stroke();
        }
    }

}
