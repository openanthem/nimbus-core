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

import {
    Component, Input, Output, forwardRef, ViewChild, EventEmitter,
    ViewEncapsulation, ChangeDetectorRef, QueryList, ViewChildren, ViewRef
} from '@angular/core';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { OverlayPanel } from 'primeng/primeng';
import { Table } from 'primeng/table';
import * as moment from 'moment';
import { Subscription } from 'rxjs';
import { ParamUtils } from './../../../shared/param-utils';
import { WebContentSvc } from '../../../services/content-management.service';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { GenericDomain } from '../../../model/generic-domain.model';
import { ParamConfig } from '../../../shared/param-config';
import { PageService } from '../../../services/page.service';
import { GridService } from '../../../services/grid.service';
import { ServiceConstants } from './../../../services/service.constants';
import { SortAs, GridColumnDataType } from './sortas.interface';
import { Param, StyleState } from '../../../shared/param-state';
import { HttpMethod } from './../../../shared/command.enum';
import { TableComponentConstants } from './table.component.constants';
import { ViewComponent, ComponentTypes } from '../../../shared/param-annotations.enum';
import { BaseTableElement } from './../base-table-element.component';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => DataTable),
    multi: true
};

/**
* \@author Dinakar.Meda
* \@whatItDoes
 *
 * \@howToUse
 *
 */

var counter = 0;

@Component({
    selector: 'nm-table',
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc, DateTimeFormatPipe],
    encapsulation: ViewEncapsulation.None,
    templateUrl: './table.component.html'
})
export class DataTable extends BaseTableElement implements ControlValueAccessor {

    @Output() onScrollEvent: EventEmitter<any> = new EventEmitter();
    @Input() params: ParamConfig[];
    @Input() form: FormGroup;
    @Input('value') _value = [];
    @ViewChild('dt') dt: Table;
    @ViewChild('op') overlayPanel: OverlayPanel;
    @ViewChildren('dropDown') dropDowns: QueryList<any>;
    componentTypes = ComponentTypes;
    viewComponent = ViewComponent;
    
    public onChange: any = (_) => { /*Empty*/ }
    public onTouched: any = () => { /*Empty*/ }
    
    filterValue: Date;
    totalRecords: number = 0;
    mouseEventSubscription: Subscription;
    filterState: any[] = [];
    columnsToShow: number = 0;
    summaryData: any;
    rowHover: boolean;
    selectedRows: any[];
    showFilters: boolean = false;
    hasFilters: boolean = false;
    filterTimeout: any;
    rowStart = 0;
    rowEnd = 0;
    rowExpanderKey = '';
    defaultPattern: RegExp = /^[ A-Za-z0-9_@./#&+-,()!%_{};:?.<>-]*$/;
    numPattern: RegExp = /[\d\-\.]/;
    id: String = 'grid-control' + counter++;
    gridRowConfig: any[];

    get value() {
        return this._value;
    }

    set value(val) {
        this._value = val;
        this.onChange(val);
        this.onTouched();
    }

    public writeValue(obj: any): void {
        if (obj !== undefined) {
        }
        this.cd.markForCheck();
    }

    public registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    public registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    fg = new FormGroup({}); // TODO this is for the filter controls that need to be embedded in the grid
    private imagesPath: string;

    constructor(
        private pageSvc: PageService,
        protected _wcs: WebContentSvc,
        private gridService: GridService,
        private dtFormat: DateTimeFormatPipe,
        protected cd: ChangeDetectorRef) {

        super(_wcs, cd);
    }

    ngOnInit() {
        super.ngOnInit();
        // non-hidden columns
        this.columnsToShow = 0;
        // Set the column headers
        if (this.params) {
            this.params.forEach(column => {
                if(column.uiStyles) {
                    if (column.uiStyles.attributes.filter) {
                        this.hasFilters = true;
                    }
                    if (column.uiStyles.attributes.rowExpander) {
                        this.rowExpanderKey = column.code;
                    }
                    if (!column.uiStyles.attributes.hidden) {
                        this.columnsToShow ++;
                    }
                }
            });
        }
        // include row selection checkbox to column count
        if (this.element.config.uiStyles && this.element.config.uiStyles.attributes.rowSelection) {
            this.columnsToShow ++;
        }

        if (this.element.gridData.leafState != null && this.element.gridData.leafState.length > 0) {
            this.value = this.element.gridData.leafState;
            this.totalRecords = this.value.length;
            this.updatePageDetailsState();
        }

        if (this.dt !== undefined) {

            const customFilterConstraints = this.dt.filterConstraints;
            customFilterConstraints['between'] = this.between;
            this.dt.filterConstraints = customFilterConstraints;
        }
        this.updatePosition();
    }

    ngAfterViewInit() {
        this.imagesPath = ServiceConstants.IMAGES_URL;
        if (this.params != null) {
            this.params.forEach(element => {
                if (element != null) {
                    if (element.uiStyles && element.uiStyles.attributes &&
                        element.uiStyles.attributes.filterValue && element.uiStyles.attributes.filterValue !== '') {
                        let filterValue = element.uiStyles.attributes.filterValue;
                        this.dt.filter(filterValue, element.code, element.uiStyles.attributes.filterMode);
                    }
                }
            });
        }

        if (this.element.config.uiStyles && this.element.config.uiStyles.attributes.onLoad === true) {
            // If table is set to lazyload, the loadDataLazy(event) method will handle the initialization
            if (!this.element.config.uiStyles.attributes.lazyLoad) {
                let queryString: string = this.getQueryString(0, undefined);
                this.pageSvc.processEvent(this.element.path, '$execute', new GenericDomain(), 'GET', queryString);
            }
        }

        this.rowHover = true;
        this.gridService.eventUpdate$.subscribe(data => {
            this.summaryData = data;
        });

        this.pageSvc.gridValueUpdate$.subscribe(event => {            
            if (event.path == this.element.path) {
                this.value = event.gridData.leafState;
                
                // iterate over currently expanded rows and refresh the data
                Object.keys(this.dt.expandedRowKeys).forEach(key => {
                    this.value.find((lineItem, index) => {
                        if (lineItem[this.element.elemId] == key) {
                            this._putNestedElement(event.collectionParams, index, lineItem);
                            return true;
                        }
                    });
                });

                let gridListSize = this.value ? this.value.length : 0;
                // Check for Server Pagination Vs Client Pagination
                if (this.element.config.uiStyles && this.element.config.uiStyles.attributes.lazyLoad) {
                    // Server Pagination
                    this.totalRecords = event.page.totalElements;
                    if (event.page.first) {
                        this.updatePageDetailsState();
                    }
                } else {
                    // Client Pagination
                    this.totalRecords = this.value ? this.value.length : 0;
                    this.updatePageDetailsState();
                    this.dt.first = 0;
                }
                if(!(<ViewRef>this.cd).destroyed)
                    this.cd.detectChanges();
                this.resetMultiSelection();
            }
        });

        if (this.form != undefined && this.form.controls[this.element.config.code] != null) {
            this.pageSvc.validationUpdate$.subscribe(event => {
                let frmCtrl = this.form.controls[event.config.code];
                if (frmCtrl != null && event.path == this.element.path) {
                    if (event.enabled)
                        frmCtrl.enable();
                    else
                        frmCtrl.disable();
                }
            });
        }

    }
    isRowExpanderHidden(rowData: any): boolean {
        if(this.rowExpanderKey == '')
            return true;
        let val = rowData[this.rowExpanderKey];
        if(val)
            return true;
        else
            return false;
    }

    getCellDisplayValue(rowData: any, col: ParamConfig) {
        let cellData = rowData[col.code];
        if (cellData) {
            if (super.isDate(col.type.name)) {
                return this.dtFormat.transform(cellData, col.uiStyles.attributes.datePattern, col.type.name);
            } else {
                return cellData;
            }
        } else {
            return col.uiStyles.attributes.placeholder;
        }
    }

    showColumn(col: ParamConfig) {
        if (col.uiStyles && col.uiStyles.attributes.hidden === false &&
             col.uiStyles.attributes.alias !== ViewComponent.gridRowBody.toString()) {
            return true;
        } 
        return false;
    }

    showHeader(col: ParamConfig) {
        if (col.uiStyles && col.uiStyles.attributes.hidden === false &&
            col.uiStyles.attributes.alias === ViewComponent.gridcolumn.toString()) {
            return true;
        } 
        return false;
    }
    /* 
    * Show value in the grid row. Below are a few examples for various configs on a grid column:
    * @GridColumn(showAsLink=true,sortable=true) - false . Value will be shown in this case using the link component
    * @Link - false
    * @GridColumn(showAsLink=false,sortable=true) - true
    */
    showValue(col: ParamConfig) {
        let showValue = false;
        if (col.uiStyles && col.uiStyles.attributes ) {
            if (!TableComponentConstants.allowedColumnStylesAlias.includes(col.uiStyles.attributes.alias)) {
                if (col.uiStyles.attributes.alias === ViewComponent.gridcolumn.toString()) {
                    if (col.uiStyles.attributes.showAsLink !== true) {
                        showValue = true;
                    }
                }
            }
        }
            return showValue;
    }

    showUiStyleInColumn(col: ParamConfig) {
        if (col.uiStyles && TableComponentConstants.allowedColumnStylesAlias.includes(col.uiStyles.attributes.alias)) {
            return true;
        }
        return false;
    }
    
    showLinkMenu(col: ParamConfig) {
        if (col.uiStyles && col.uiStyles.attributes.alias === 'LinkMenu') {
            return true;
        }
        return false;
    } 
    /**
     * return the css class for table column style.
     * This will determine the column width and other css properties
     * @param col 
     */
    getColumnStyle(col: ParamConfig): string {
        if (col.uiStyles && col.uiStyles.attributes.alias === 'LinkMenu') {
            return 'dropdown';
        } else if (col.uiStyles && col.uiStyles.attributes.alias === 'Button') {
            return 'imageColumn';
        } else {
            if (col.uiStyles && col.uiStyles.attributes.cssClass && col.uiStyles.attributes.cssClass !== "") {
                return col.uiStyles.attributes.cssClass;
            } 
        } 
    }

    isActive(index) {
        if (this.filterState[index] != '' && this.filterState[index] != undefined) return true;
        else return false;
    }

    getViewParam(col: ParamConfig, rowIndex: number): Param {
        return this.element.collectionParams.find(ele => ele.path == this.element.path + '/'+rowIndex+'/' + col.code);
    }

    getRowPath(col: ParamConfig, item: any) {
        return this.element.path + '/' + item.elemId + '/' + col.code;
    }

    processOnClick(col: ParamConfig, item: any) {
        let uri = this.element.path + '/' + item.elemId + '/' + col.code;

        let uriParams = this.getAllURLParams(uri);
        if (uriParams != null) {
            for (let uriParam of uriParams) {
                let p = uriParam.substring(1, uriParam.length - 1);
                if (item[p]) {
                    uri = uri.replace(new RegExp(uriParam, 'g'), item[p]);
                }
            }
        }
        this.pageSvc.processEvent(uri, col.uiStyles.attributes.b, item, col.uiStyles.attributes.method);
    }

    /* look for parameters in URI {} */
    getAllURLParams(uri: string): string[] {
        var pattern = /{([\s\S]*?)}/g;
        return uri.match(pattern);
    }

    toggleFilter(event: any) {
        this.showFilters = !this.showFilters;
    }

    postGridData(obj) {
        let item: GenericDomain = new GenericDomain();
        let elemIds = [];
        if (this.selectedRows && this.selectedRows.length > 0) {
            this.selectedRows.forEach(element => {
                elemIds.push(element.elemId);
            });
        }
        item.addAttribute(this.element.config.uiStyles.attributes.postButtonTargetPath, elemIds);

        // postButtonUrl is deprecated in @Grid Config from 1.1.11
        // TODO - remove this when the annotation attribute is removed completely in ViewConfig.
        if (this.element.config.uiStyles.attributes.postButtonUrl) {
            console.warn('Use of postButtonUrl attribute in @Grid is deprecated. Consider using postButtonUri');
            this.pageSvc.processEvent(this.element.config.uiStyles.attributes.postButtonUrl, null, item, 'POST');
            return;
        }
        const postButtonUri = this.element.config.uiStyles.attributes.postButtonUri;
        if (postButtonUri) {
            const resolvedPostButtonUri = ParamUtils.resolveParamUri(this.element.path, postButtonUri);
            // resolve the postbutton url relative to the current element path.
            // Ex: (../, <!#this#!> , ./ , /p, .d, absolute path with current domain)
            this.pageSvc.processEvent(resolvedPostButtonUri, null, item, 'POST');
        }
    }

    onRowSelect(event) {
    }

    onRowUnselect(event) {
    }

    onRowClick(event: any) {
    }

    postOnChange(col: ParamConfig, item: any) {
        let uri = this.element.path + '/' + item.elemId + '/' + col.code;
        this.pageSvc.postOnChange(uri, 'state', JSON.stringify(event.target['checked']));
    }

    handleRowChange(val) {
    }

    onRowExpand(event: any) {
        this._putNestedElement(this.element.collectionParams, event.data.elemId, event.data)
    }

    resetMultiSelection() {
        this.selectedRows = [];
    }

    customSort(event: any) {
        let fieldType: string = event.field.type.name;
        let sortAs: string = event.field.uiStyles.attributes.sortAs;
        if (this.isSortAsNumber(fieldType, sortAs)) {
            this.sortInternal(fieldValue => {
                if (fieldValue != null && fieldValue !== undefined)
                    return Number(fieldValue);
                else
                    return null;
            }, event);
        }
        else if (this.isSortAsDate(fieldType, sortAs)) {
            this.sortInternal(fieldValue => {
                if (fieldValue != null && fieldValue !== undefined)
                    return new Date(fieldValue);
                else
                    return null;
            }, event);
        }
        else {
            // all else are sorted as string using localeCompare
            this.value.sort((item1: any, item2: any) => {
                let value1 = item1[event.field.code] !== undefined ? item1[event.field.code] : null;
                let value2 = item2[event.field.code] !== undefined ? item2[event.field.code] : null;

                if (value1 == null && value2 == null)
                    return 0;
                if (value1 == null && value2 != null)
                    return -1 * event.order;
                else if (value1 != null && value2 == null)
                    return 1 * event.order;

                return value1.localeCompare(value2) * event.order;
            });
        }
    }

    protected sortInternal(itemCallback: Function, event: any): Array<any> {
        return this.value.sort((item1: any, item2: any) => {
            let value1 = itemCallback(item1[event.field.code]);
            let value2 = itemCallback(item2[event.field.code]);

            if (value1 == null && value2 == null)
                return 0;
            if (value1 == null && value2 != null)
                return -1 * event.order;
            if (value1 != null && value2 == null)
                return 1 * event.order;

            if (value1 > value2) {
                return 1 * event.order;
            }
            if (value1 < value2) {
                return -1 * event.order;
            }
            return 0;
        });
    }

    protected isSortAsNumber(fieldType: string, sortAs: string): boolean {
        let fieldTypeToMatch = fieldType.toLowerCase();
        return ((sortAs !== null && sortAs === SortAs.number.value) || fieldTypeToMatch === GridColumnDataType.int.value || fieldTypeToMatch === GridColumnDataType.integer.value
            || fieldTypeToMatch === GridColumnDataType.long.value || fieldTypeToMatch === GridColumnDataType.double.value);

    }

    protected isSortAsDate(fieldType: string, sortAs: string): boolean {
        let fieldTypeToMatch = fieldType.toLowerCase();
        return ((sortAs !== null && sortAs === SortAs.date.value) || fieldTypeToMatch === GridColumnDataType.date.value || fieldTypeToMatch === GridColumnDataType.localdate.value
            || fieldTypeToMatch === GridColumnDataType.localdatetime.value || fieldTypeToMatch === GridColumnDataType.zoneddatetime.value);

    }

    between(value: any, filter: any) {
        return moment(filter).isSame(value, 'day');
    }

    dateFilter(e: any, dt: Table, field: string, datePattern?: string, dateType?: string) {

        let dtPattern = datePattern? datePattern : ParamUtils.getDateFormatForType(dateType);
       
        if (moment(e.toLocaleDateString(), dtPattern.toUpperCase(), false).isValid()) {
            dt.filter(moment(e.toLocaleDateString(), dtPattern.toUpperCase()).toDate(), field, "between");
        }
       
        this.updatePageDetailsState();
    }

    inputFilter(e: any, dt: Table, field: string, filterMatchMode: string) {
        // Wait for 500 ms before triggering the filter. This is to give time for the user to enter the criteria 
        if (this.filterTimeout) {
            clearTimeout(this.filterTimeout);
        }

        this.filterTimeout = setTimeout(() => {
            dt.filter(e.target.value, field, filterMatchMode);
        }, 500);
    }

    clearFilter(txt: any, dt: Table, field: string, index) {
        txt.value = '';
        dt.filter(txt.value, field, "");
    }

    clearAll() {
        this.filterState = [];
        this.dt.reset();
    }

    paginate(e: any) {
        let first: number = parseInt(e.first);
        let rows: number = parseInt(e.rows);
        this.rowStart = first + 1;
        if (first + rows < this.totalRecords) {
            this.rowEnd = first + rows;
        } else  {
            this.rowEnd = this.totalRecords;
        }
    }

    updatePageDetailsState() {
        if (this.totalRecords != 0) {
            this.rowStart = 1;
            this.rowEnd = this.totalRecords < +this.element.config.uiStyles.attributes.pageSize ? this.totalRecords : +this.element.config.uiStyles.attributes.pageSize;
        }
        else {
            this.rowStart = 0; this.rowEnd = 0;
        }
    }

    filterCallBack(e: any) {
        this.totalRecords = e.filteredValue.length;
        this.updatePageDetailsState();
        this.selectedRows.length = 0
        this.dt.toggleRowsWithCheckbox(e, true);
    }

    export() {
        let exportDt = this.dt;
        let dtCols = this.params.filter(col => (col.type != null && ParamUtils.isKnownDateType(col.type.name) != null))
        if (dtCols != null && dtCols.length > 0) {
            let tblData: any[] = exportDt.filteredValue || exportDt.value;
            tblData.forEach(row => {
                for (var key in row) {
                    if (row.hasOwnProperty(key)) {
                        if (row[key] instanceof Date) {
                            let col = dtCols.filter(cd => cd.code == key)
                            if (col != null && col.length > 0) {
                                row[key] = this.dtFormat.transform(row[key], col[0].uiStyles.attributes.datePattern, col[0].type.name);
                            }
                        }
                    }
                }
            });
            exportDt.filteredValue = tblData;
        }
        exportDt.exportCSV();
    }

    ngOnDestroy() {
        if (this.mouseEventSubscription)
            this.mouseEventSubscription.unsubscribe();
        this.cd.detach();
    }

    loadDataLazy(event:any) {
        let pageSize: number = this.element.config.uiStyles.attributes.pageSize;
        let pageIdx: number = 0;        
        let first: number = event.first;
        if (first != 0 ) {
            pageIdx = first/pageSize;
        } else {
            pageIdx = 0;
        }

        // Sort Logic
        let sortBy: string = undefined;
        if (event.sortField) {
            let order: number = event.sortOrder;
            let sortField: string = event.sortField.code;
            let sortOrder: string = 'ASC';
            if (order != 1) {
                sortOrder = 'DESC';
            }
            sortBy = sortField + ',' + sortOrder;    
        }

        // Filter Logic
        let filterCriteria: any[] = [];
        let filterKeys: string[] = [];
        if (event.filters) {
            filterKeys = Object.keys(event.filters);
        }
        filterKeys.forEach(key => {
            let filter: any = {};
            filter['code'] = key;
            filter['value'] = event.filters[key].value;
            filterCriteria.push(filter);
        })
        let payload: GenericDomain = new GenericDomain();
        if (filterCriteria.length > 0) {
            payload.addAttribute('filters', filterCriteria);
        }
        // query params - &pageSize=5&page=0&sortBy=attr_String,DESC
        // request body - filterCriteria
        let queryString: string = this.getQueryString(pageIdx, sortBy);
        this.pageSvc.processEvent(this.element.path, '$execute', payload, HttpMethod.POST.value, queryString);

    }

    getQueryString(pageIdx: number, sortBy: string): string {
        let queryString: string = '';
        let pageSize: number = this.element.config.uiStyles.attributes.pageSize;
        if (sortBy) {
            queryString = queryString + '&sortBy=' + sortBy;
        }
        if (pageIdx !== undefined) {
            queryString = queryString + '&pageSize=' + pageSize + '&page=' + pageIdx;
        }
        return queryString;
    }

    getPattern(dataType: string): any {
        if(this.isSortAsNumber(dataType, null)) {
            return this.numPattern;
        } else {
            return this.defaultPattern;
        }
    }

    getCellStyle(rowIndex, code): string {
        let elemStateMap = this.element.gridData.stateMap[rowIndex];
        if (!elemStateMap) {
            return '';
        }
        let style: StyleState = elemStateMap[code].style;
        return style ? style.cssClass : '';
    }

    /**
     * This method will identify and set the "nested element" used to render the expanded row content within the 
     * table component.
     * 
     * Given an <tt>index</tt> which represents the row number of the line item within a grid, this method first 
     * identifies the corresponding <tt>Param</tt> within <tt>collectionParams</tt> that is annotated with 
     * <tt>@GridRowBody</tt> that should be set as a "nested element". The identified param contains the latest data
     * retrieved from the server.
     * 
     * Next, <tt>targetLineItem</tt> (the view representation in the grid) will be updated to contain the identified 
     * param for rendering.
     * 
     * @param collectionParams the array of params that are eligible for being a "nested element"
     * @param index the row index of the line item to set
     * @param targetLineItem the object value of the table
     * @returns true if targetLineItem's nestedElement was set, false otherwise.
     */
    private _putNestedElement(collectionParams: Param[], index: number, targetLineItem: any): boolean {
        const identifiedParam = collectionParams.find(p => {
            return p.alias == ViewComponent.gridRowBody.toString() &&
                p.path == `${this.element.path}/${index}/${p.config.code}`;
        });

        if (identifiedParam) {
            targetLineItem['nestedElement'] = identifiedParam;
            return true;
        }

        return false;
    }
}