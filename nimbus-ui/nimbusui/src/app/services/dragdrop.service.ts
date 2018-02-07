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
import { Injectable, EventEmitter } from '@angular/core';
import { SortableContainerDirective } from './../directives/sortable-dragdrop.directive';
import { DragDropConfig, DragDropData } from './../shared/app-config.interface';
import { isPresent }  from './../shared/app-config.interface';

@Injectable()
export class DragDropService {
    allowedDropZones: Array<string> = [];
    onDragSuccessCallback: EventEmitter<DragDropData>;
    dragData: any;
    isDragged: boolean;
}

@Injectable()
export class DragDropSortableService {
    index: number;
    sortableContainer: SortableContainerDirective;
    isDragged: boolean;
    isDragAllowed : boolean;
    private _elem: HTMLElement;
    public get elem(): HTMLElement {
        return this._elem;
    }

    constructor(private _config:DragDropConfig) {}

    markSortable(elem: HTMLElement) {
        if (isPresent(this._elem)) {
            this._elem.classList.remove(this._config.onSortableDragClass);
        }
        if (isPresent(elem)) {
            this._elem = elem;
            this._elem.classList.add(this._config.onSortableDragClass);
        }
    }
}



