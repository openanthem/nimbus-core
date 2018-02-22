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
import { BaseElement } from './../base-element.component';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Component, Input, Output, forwardRef, ViewChild, EventEmitter, ViewEncapsulation, ChangeDetectorRef } from '@angular/core';

import { GenericDomain } from '../../../model/generic-domain.model';
import { Param, ParamConfig } from '../../../shared/app-config.interface';
import { PageService } from '../../../services/page.service';
import { GridService } from '../../../services/grid.service';
import { WebContentSvc } from '../../../services/content-management.service';
import { DataTable, OverlayPanel, Paginator } from 'primeng/primeng';
import { ServiceConstants } from './../../../services/service.constants';
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { Calendar } from 'primeng/components/calendar/calendar';
import * as moment from 'moment';
import { SortAs, GridColumnDataType } from './sortas.interface';


export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InfiniteScrollGrid),
    multi: true
};

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'infinite-scroll-grid',
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc],
    encapsulation: ViewEncapsulation.None,
    templateUrl: './grid.component.html'
})
export class InfiniteScrollGrid extends BaseElement implements ControlValueAccessor {
    @Input() data: any[];
    @Output() onScrollEvent: EventEmitter<any> = new EventEmitter();
    @Input() params: ParamConfig[];
    @Input() form: FormGroup;
    @Input('value') _value = [];
    filterValue: Date;
    totalRecords: number = 0;

    //    references DataTable named 'flex' in the view
    @ViewChild('flex') flex: DataTable;
    @ViewChild('dt') dt: DataTable;
    @ViewChild('op') overlayPanel: OverlayPanel;

    summaryData: any;
    rowHover: boolean;
    selectedRows: any[];
    filterState: boolean = false;
    rowStart = 0;
    rowEnd = 0;

    public onChange: any = (_) => { /*Empty*/ }
    public onTouched: any = () => { /*Empty*/ }

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
        private _wcs: WebContentSvc,
        private gridService: GridService,
        private cd: ChangeDetectorRef) {

        super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();

        // Set the column headers
        if (this.params) {
            this.params.forEach(element => {
                element.label = this._wcs.findLabelContentFromConfig(element.code, element.labelConfigs).text;
            });
        }

        if (this.element.config.gridList != null && this.element.config.gridList.length > 0) {
            this.value = this.element.config.gridList;
        }
    }

    ngAfterViewInit() {
        this.imagesPath = ServiceConstants.IMAGES_URL;
        if (this.params != null) {
            this.params.forEach(element => {
                if (element != null) {
                    if (element.uiStyles && element.uiStyles.attributes &&
                        element.uiStyles.attributes.filterValue && element.uiStyles.attributes.filterValue !== '') {
                        let filterValue = element.uiStyles.attributes.filterValue;
                        this.flex.filter(filterValue, element.code, element.uiStyles.attributes.filterMode);
                    }
                }
            });
        }

        if (this.element.config.uiStyles.attributes.onLoad === true) {
            this.pageSvc.processEvent(this.element.path, '$execute', new GenericDomain(), 'GET');
        }

        this.rowHover = true;
        this.gridService.eventUpdate$.subscribe(data => {
            this.summaryData = data;
        });

        this.pageSvc.gridValueUpdate$.subscribe(event => {
            if (event.path == this.element.path) {
                this.value = event.config.gridList;
                this.totalRecords = this.value.length;
                this.updatePageDetailsState();
                this.cd.markForCheck();
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
        //console.log(event);
        this.filterState = !this.filterState;
    }

    postGridData(obj) {
        let item: GenericDomain = new GenericDomain();
        let elemIds = [];
        this.selectedRows.forEach(element => {
            elemIds.push(element.elemId);
        });

        item.addAttribute(this.element.config.uiStyles.attributes.postButtonTargetPath, elemIds);
        this.pageSvc.processEvent(this.element.config.uiStyles.attributes.postButtonUrl, null, item, 'POST');
    }

    onRowSelect(event) {
    }

    onRowUnselect(event) {
    }

    onRowClick(event: any) {
    }

    onRowUnSelect(event) {
    }

    postOnChange(col: ParamConfig, item: any) {
        let uri = this.element.path + '/' + item.elemId + '/' + col.code;
        this.pageSvc.postOnChange(uri, 'state', JSON.stringify(event.target['checked']));
    }

    handleRowChange(val) {
    }

    getAddtionalData(event: any) {
        let elemPath;
        for (var p in this.params) {
            let param = this.params[p];
            if (param.type.nested) {
                if (param.uiStyles && param.uiStyles.attributes.alias == 'GridRowBody') {
                    // // Check if data has to be extracted async'ly
                    // if (param.uiStyles.attributes.asynchronous) {
                    //     elemPath = this.element.path + '/' + event.data.elemId + '/' + param.code;
                    // } else {
                        event.data['nestedElement'] = event.data.params[p];
                    // }
                }
            }
        }
        if (elemPath) {
            this.pageSvc.processEvent(elemPath, '$execute', new GenericDomain(), 'GET');
        }
    }

    resetMultiSelection() {
        this.selectedRows = [];
    }

    sortBy(e: any, fieldType: string, sortAs: string) {
        if (this.isSortAsNumber(fieldType, sortAs)) {
            this.sortInternal(fieldValue => Number(fieldValue), e);
        }
        else if (this.isSortAsDate(fieldType, sortAs)) {
            this.sortInternal(fieldValue => new Date(fieldValue), e);
        }
        else {
            // all else are sorted as string using localeCompare
            this.value.sort((item1: any, item2: any) => {
                return item1[e.field].localeCompare(item2[e.field]) * e.order;
            });
        }
        this.value = [...this.value];
    }

    protected sortInternal(itemCallback: Function, event: any): Array<any> {
        return this.value.sort((item1: any, item2: any) => {
            let value1 = itemCallback(item1[event.field]);
            let value2 = itemCallback(item2[event.field]);

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
        return ((sortAs !== null && sortAs === SortAs.date.value) || fieldTypeToMatch === GridColumnDataType.date.value
            || fieldTypeToMatch === GridColumnDataType.localdatetime.value || fieldType === GridColumnDataType.zoneddatetime.value);

    }

    dateFilter(e: any, dt: DataTable, field: string, filterMatchMode: string, datePattern?: string, dateType?: string) {
        datePattern = (datePattern == "") ? "MM/DD/YYYY" : datePattern;

        if (e.target.value.length == '0') {
            dt.filter(e.target.value, field, "startsWith");
        }
        else {
            if (moment(e.target.value, datePattern.toUpperCase(), true).isValid()) {
                let formatedDate = moment(e.target.value, datePattern.toUpperCase()).format('MM/DD/YYYY');
                dt.filter(formatedDate, field, "startsWith");
            }
        }

        this.totalRecords = dt.dataToRender.length;
        this.updatePageDetailsState();
    }

    inputFilter(e: any, dt: DataTable, field: string, filterMatchMode: string) {
        dt.filter(e.target.value, field, "startsWith");
    }

    isDate(dataType: string): boolean {
        if (dataType === 'date' || dataType === 'Date' || dataType === 'LocalDateTime' || dataType === 'ZonedDateTime') return true;
        if (dataType !== 'date' && dataType !== 'Date' && dataType !== 'LocalDateTime' && dataType !== 'ZonedDateTime') return false;
    }

    paginate(e: any) {
        if (this.totalRecords != 0) {
            this.rowEnd = ((this.totalRecords / (e.first + (+e.rows)) >= 1) ? (e.first + (+e.rows)) : e.first + (this.totalRecords - e.first));
            this.rowStart = e.first + 1;
        }
        else {
            this.rowStart = 0; this.rowEnd = 0;
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
    }
}
