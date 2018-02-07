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

import { PageService } from '../services/page.service';
import { ChangeDetectorRef } from '@angular/core';
import { Directive, Input, Output, EventEmitter, ElementRef } from '@angular/core';
import { DragDropService, DragDropSortableService } from './../services/dragdrop.service';
import { DragDropConfig } from './../shared/app-config.interface';
import { AbstractDragAndDropComponent } from './../components/platform/abstract-dragndrop.component';
import { GenericDomain } from '../model/generic-domain.model';
//Ref : https://github.com/akserg/ng2-dnd

@Directive({ selector: '[nm-dnd-sortable-container]' })
export class SortableContainerDirective extends AbstractDragAndDropComponent {

    @Input('dragEnabled') set draggable(value: boolean) {
        this.dragEnabled = !!value;
    }

    private _sortableData: Array<any> = [];

    @Input() set sortableData(sortableData: Array<any>) {
        this._sortableData = sortableData;
        this.dropEnabled = !!this._sortableData;
    }
    get sortableData(): Array<any> {
        return this._sortableData;
    }

    @Input('dropZones') set dropzones(value: Array<string>) {
        this.dropZones = value;
    }

    constructor(elemRef: ElementRef, dragDropService: DragDropService, config: DragDropConfig, cdr: ChangeDetectorRef,
        private _sortableDataService: DragDropSortableService) {

        super(elemRef, dragDropService, config, cdr);
        this.dragEnabled = false;
    }

    _onDragEnterCallback(event: Event) {
        if (this._sortableDataService.isDragged) {
            let item: any = this._sortableDataService.sortableContainer._sortableData[this._sortableDataService.index];
            // Check does element exist in sortableData of this Container
            if (this._sortableData.indexOf(item) === -1) {
                // Remove item from previouse list
                this._sortableDataService.sortableContainer._sortableData.splice(this._sortableDataService.index, 1);
                if (this._sortableDataService.sortableContainer._sortableData.length === 0) {
                    this._sortableDataService.sortableContainer.dropEnabled = true;
                }
                // Add item to new list
                this._sortableData.unshift(item);
                this._sortableDataService.sortableContainer = this;
                this._sortableDataService.index = 0;
            }
            // Refresh changes in properties of container component
            this.detectChanges();
        }
    }
}

@Directive({ selector: '[nm-dnd-sortable]' })
export class SortableComponentDirective extends AbstractDragAndDropComponent {
    @Input() path: string;
    @Input('sortableIndex') index: number;

    @Input('dragEnabled') set draggable(value: boolean) {
        this.dragEnabled = !!value;
    }

    @Input('dropEnabled') set droppable(value: boolean) {
        this.dropEnabled = !!value;
    }

    /**
     * The data that has to be dragged. It can be any JS object
     */
    @Input() dragData: any;

    /**
     * Drag allowed effect
     */
    @Input('effectAllowed') set effectallowed(value: string) {
        this.effectAllowed = value;
    }

    /**
     * Drag effect cursor
     */
    @Input('effectCursor') set effectcursor(value: string) {
        this.effectCursor = value;
    }

    /**
     * Callback function called when the drag action ends with a valid drop action.
     * It is activated after the on-drop-success callback
     */
    @Output('onDragSuccess') onDragSuccessCallback: EventEmitter<any> = new EventEmitter<any>();

    @Output('onDragStart') onDragStartCallback: EventEmitter<any> = new EventEmitter<any>();
    @Output('onDragOver') onDragOverCallback: EventEmitter<any> = new EventEmitter<any>();
    @Output('onDragEnd') onDragEndCallback: EventEmitter<any> = new EventEmitter<any>();
    @Output('onDropSuccess') onDropSuccessCallback: EventEmitter<any> = new EventEmitter<any>();

    private _sourceIndex: number;
    private _targetIndex: number;

    constructor(elemRef: ElementRef, dragDropService: DragDropService, config: DragDropConfig,
        private _sortableContainer: SortableContainerDirective,
        private _sortableDataService: DragDropSortableService, private pageSvc: PageService,
        cdr: ChangeDetectorRef) {

        super(elemRef, dragDropService, config, cdr);

        this.dropZones = this._sortableContainer.dropZones;
        this.dragEnabled = true;
        this.dropEnabled = true;
    }

    _onDragStartCallback(event: Event) {
        // console.log('_onDragStartCallback. dragging elem with index ' + this.index);
        this._sortableDataService.isDragged = true;
        this._sortableDataService.sortableContainer = this._sortableContainer;
        //console.log('onDragStart!!!!! ' + this.index);
        this._sortableDataService.index = this.index;
        this._sortableDataService.markSortable(this._elem);
        // Add dragData
        this._dragDropService.isDragged = true;
        this._dragDropService.dragData = this.dragData;
        this._dragDropService.onDragSuccessCallback = this.onDragSuccessCallback;
        //
        this.onDragStartCallback.emit(this._dragDropService.dragData);
        this._sourceIndex = this.index;
        //console.log('Source index::::' + this._sourceIndex);
    }

    _onDragOverCallback(event: Event) {
        if (this._sortableDataService.isDragged && this._elem !== this._sortableDataService.elem) {
            // console.log('_onDragOverCallback. dragging elem with index ' + this.index);
            this._sortableDataService.sortableContainer = this._sortableContainer;
            this._sortableDataService.index = this.index;
            this._sortableDataService.markSortable(this._elem);
            this.onDragOverCallback.emit(this._dragDropService.dragData);
        }
    }

    _onDragEndCallback(event: Event) {
        //console.log('_onDragEndCallback. end dragging elem with index ' + this.index);
        this._sortableDataService.isDragged = false;
        this._sortableDataService.sortableContainer = null;
        this._sortableDataService.index = null;
        this._sortableDataService.markSortable(null);
        // Add dragGata
        this._dragDropService.isDragged = false;
        this._dragDropService.dragData = null;
        this._dragDropService.onDragSuccessCallback = null;
        //
        this.onDragEndCallback.emit(this._dragDropService.dragData);

    }

    _onDragEnterCallback(event: Event) {
        if (this._sortableDataService.isDragged) {
            this._sortableDataService.markSortable(this._elem);
            if ((this.index !== this._sortableDataService.index) ||
                (this._sortableDataService.sortableContainer.sortableData !== this._sortableContainer.sortableData)) {
                //console.log('Component._onDragEnterCallback. drag node [' + this.index + '] over node [' + this._sortableDataService.index + ']');
                // Get item
                let item: any = this._sortableDataService.sortableContainer.sortableData[this._sortableDataService.index];
                // Remove item from previouse list
                this._sortableDataService.sortableContainer.sortableData.splice(this._sortableDataService.index, 1);
                if (this._sortableDataService.sortableContainer.sortableData.length === 0) {
                    this._sortableDataService.sortableContainer.dropEnabled = true;
                }
                // Add item to new list
                this._sortableContainer.sortableData.splice(this.index, 0, item);
                if (this._sortableContainer.dropEnabled) {
                    this._sortableContainer.dropEnabled = false;
                }
                this._sortableDataService.sortableContainer = this._sortableContainer;
                this._sortableDataService.index = this.index;
            }
        }
    }

    _onDropCallback(event: Event) {
        this._targetIndex = this.index;
        //console.log('target Index::' + this._targetIndex);
        if (this._sortableDataService.isDragged) {
            // console.log('onDropCallback.onDropSuccessCallback.dragData', this._dragDropService.dragData);
            this.onDropSuccessCallback.emit(this._dragDropService.dragData);
            if (this._dragDropService.onDragSuccessCallback) {
                // console.log('onDropCallback.onDragSuccessCallback.dragData', this._dragDropService.dragData);
                this._dragDropService.onDragSuccessCallback.emit(this._dragDropService.dragData);
            }
            //console.log('Finally - Drag started from [' + this._sourceIndex + '] and ended at [' + this._targetIndex + ']');

            let itemList = [];
            //console.log(this._sortableContainer.sortableData);
            var count = 0;
            this._sortableContainer.sortableData.forEach(card => {
                let item: GenericDomain = new GenericDomain();
                item.addAttribute(this.path + count + '/#/order', (card.number - 1)); // Card number/priority = index +1;
                itemList.push(item);
                count++;
            });

            this._sortableContainer.detectChanges();
            this.pageSvc.postOnChange(this.path, 'state', JSON.stringify(itemList));
        }
    }
}
