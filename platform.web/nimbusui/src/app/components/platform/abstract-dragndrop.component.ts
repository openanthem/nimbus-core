/*
The MIT License (MIT)

Copyright (c) 2016 Sergey Akopkokhyants

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
https://github.com/akserg/ng2-dnd
*/

import { Injectable, ChangeDetectorRef } from '@angular/core';
import { ElementRef } from '@angular/core';
import { DragDropConfig } from './../../shared/app-config.interface';
import { DragDropService } from './../../services/dragdrop.service';
import { isPresent, isString, createImage, callFun, isFunction } from './../../shared/app-config.interface';

@Injectable()
export abstract class AbstractDragAndDropComponent {
    _elem: HTMLElement;
    _dragHelper: HTMLElement;
    _defaultCursor: string;
    /**
     * Allows drop on this element
     */
    dropEnabled: boolean = false;
    /**
     * Drag effect
     */
    effectAllowed: string;
    /**
     * Drag cursor
     */
    effectCursor: string;

    allowDrop: (dropData: any) => boolean;
    dropZones: string[] = [];

    // Use HTML - DragImage if use case requires an image for drag icon
    dragImage: string | Function;

    cloneItem: boolean = false;
  /**
     * Whether the object is draggable. Default is true.
     */
    private _dragEnabled: boolean = false;
    set dragEnabled(enabled: boolean) {
        this._dragEnabled = !!enabled;
       this._elem.draggable = this._dragEnabled;
    }
    get dragEnabled(): boolean {
        return this._dragEnabled;
    }

    constructor(elemRef: ElementRef, public _dragDropService: DragDropService, public _config: DragDropConfig,
        private _cdr: ChangeDetectorRef) {

        this._elem = elemRef.nativeElement;
            this._elem.ondragenter = (event: Event) => {
                this._onDragEnter(event);
            };
            this._elem.ondragover = (event: DragEvent) => {
                this._onDragOver(event);
                //
                if (event.dataTransfer != null) {
                    event.dataTransfer.dropEffect = this._config.dropEffect.name;
                }

                return false;
            };
            // this._elem.ondragleave = (event: Event) => {
            //     this._onDragLeave(event);
            // };
            this._elem.ondrop = (event: Event) => {
                this._onDrop(event);
            };

            // Drag events
            this._elem.ondragstart = (event: DragEvent) => {
                this._onDragStart(event);
                if (event.dataTransfer != null) {
                    event.dataTransfer.setData('text', '');
                    // Change drag effect
                    event.dataTransfer.effectAllowed = this.effectAllowed || this._config.dragEffect.name;
                    // Change drag image
                    if (isPresent(this.dragImage)) {
                        if (isString(this.dragImage)) {
                            (<any>event.dataTransfer).setDragImage(createImage(<string>this.dragImage));
                        } else if (isFunction(this.dragImage)) {
                            (<any>event.dataTransfer).setDragImage(callFun(<Function>this.dragImage));
                        }
                    } else if (this.cloneItem) {
                        this._dragHelper = <HTMLElement>this._elem.cloneNode(true);
                        this._dragHelper.classList.add('dnd-drag-item');
                        this._dragHelper.style.position = 'absolute';
                        this._dragHelper.style.top = '0px';
                        this._dragHelper.style.left = '-1000px';
                        this._elem.parentElement.appendChild(this._dragHelper);
                        (<any>event.dataTransfer).setDragImage(this._dragHelper, event.offsetX, event.offsetY);
                    }
                    // Change drag cursor
                    if (this._dragEnabled) {
                        this._elem.style.cursor = this.effectCursor ? this.effectCursor : this._config.dragCursor;
                    } else {
                        this._elem.style.cursor = this._defaultCursor;
                    }
                }
            };
            this._elem.ondragend = (event: Event) => {
                if (this._elem.parentElement && this._dragHelper) {
                    this._elem.parentElement.removeChild(this._dragHelper);
                }
                this._onDragEnd(event);
                // Restore style of dragged element
                this._elem.style.cursor = this._defaultCursor;
            };

    }

    /******* Change detection ******/

    detectChanges() {
        // Programmatically run change detection to fix issue in Safari
        setTimeout(() => {
            this._cdr.detectChanges();
        }, 250);
    }

     //**** Drop Callbacks ****//
    _onDragEnterCallback(event: Event) { }
    _onDragOverCallback(event: Event) { }
    // _onDragLeaveCallback(event: Event) { }
    _onDropCallback(event: Event) { }

    //**** Drag Callbacks ****//
    _onDragStartCallback(event: Event) { }
    _onDragEndCallback(event: Event) { }
    //****** Droppable *******//
    private _onDragEnter(event: Event): void {
        if (this._isDropAllowed) {
            this._onDragEnterCallback(event);
        }
    }

    private _onDragOver(event: Event) {
        if (this._isDropAllowed) {
            // The element is over the same source element - do nothing
            if (event.preventDefault) {
                // Necessary. Allows us to drop.
                event.preventDefault();
            }

            this._onDragOverCallback(event);
        }
    }

    private _onDrop(event: Event): void {
        if (this._isDropAllowed) {
            if (event.preventDefault) {
                // Necessary. Allows us to drop.
                event.preventDefault();
            }

            if (event.stopPropagation) {
                event.stopPropagation();
            }

            this._onDropCallback(event);

            this.detectChanges();
        }
    }

    private get _isDropAllowed(): boolean {
        if (this._dragDropService.isDragged && this.dropEnabled) {
            // First, if `allowDrop` is set, call it to determine whether the
            // dragged element can be dropped here.
            if (this.allowDrop) {
                return this.allowDrop(this._dragDropService.dragData);
            }

            // Otherwise, use dropZones if they are set.
            if (this.dropZones.length === 0 && this._dragDropService.allowedDropZones.length === 0) {
                return true;
            }
            for (let i: number = 0; i < this._dragDropService.allowedDropZones.length; i++) {
                let dragZone: string = this._dragDropService.allowedDropZones[i];
                if (this.dropZones.indexOf(dragZone) !== -1) {
                    return true;
                }
            }
        }
        return false;
    }
    //*********** Draggable **********//
    private _onDragStart(event: Event): void {
        if (this._dragEnabled) {
            this._dragDropService.allowedDropZones = this.dropZones;
            this._onDragStartCallback(event);
        }
    }
    private _onDragEnd(event: Event): void {
        this._dragDropService.allowedDropZones = [];
        this._onDragEndCallback(event);
    }
}
