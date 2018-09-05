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
import { Component, Input, Output, forwardRef, ViewChild, EventEmitter, ViewEncapsulation, ChangeDetectorRef, QueryList, ViewChildren } from '@angular/core';

import { GenericDomain } from '../../../model/generic-domain.model';
import { ParamConfig } from '../../../shared/param-config';
import { Param } from '../../../shared/param-state';
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
import { ActionDropdown } from './../form/elements/action-dropdown.component';
import { fromEvent as observableFromEvent,  Observable ,  Subscription } from 'rxjs';
import { first, filter } from 'rxjs/operators';
import { ParamUtils } from './../../../shared/param-utils';
import { ViewComponent } from '../../../shared/param-annotations.enum';

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
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc, DateTimeFormatPipe],
    encapsulation: ViewEncapsulation.None,
    templateUrl: './grid.component.html'
})
export class InfiniteScrollGrid extends BaseElement implements ControlValueAccessor {
    @Input() data: any[];
    @Output() onScrollEvent: EventEmitter<any> = new EventEmitter();
    @Input() params: ParamConfig[];
    @Input() form: FormGroup;
    @Input('value') _value = [];
    //paramState: Param[];
    filterValue: Date;
    totalRecords: number = 0;
    mouseEventSubscription: Subscription;
    filterState: any[]=[];
   
    

    //    references DataTable named 'flex' in the view
    @ViewChild('flex') flex: DataTable;
    @ViewChild('dt') dt: DataTable;
    @ViewChild('op') overlayPanel: OverlayPanel;
    @ViewChildren('dropDown') dropDowns: QueryList<any>;


    summaryData: any;
    rowHover: boolean;
    selectedRows: any[];
    showFilters: boolean = false;
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
        private dtFormat: DateTimeFormatPipe,
       // private requestProcessor: RequestProcessorService,
        private cd: ChangeDetectorRef) {

        super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();

        // Set the column headers
        if (this.params) {
            this.params.forEach(element => {
                element.label = this._wcs.findLabelContentFromConfig(this.element.elemLabels.get(element.id), element.code).text;
            });
        }

        if (this.element.gridList != null && this.element.gridList.length > 0) {
            this.value = this.element.gridList;
            this.totalRecords = this.value.length;
            this.updatePageDetailsState();
        }

        if (this.dt !== undefined) {

            const customFilterConstraints = this.dt.filterConstraints;
            customFilterConstraints['between'] = this.between;
            this.dt.filterConstraints = customFilterConstraints;
        }
        
        //this.paramState = this.element.paramState;
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
                this.value = event.gridList;
                //this.paramState = event.paramState;
                //this.element.collectionParams = event.collectionParams;
                this.totalRecords = this.value ? this.value.length : 0;
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

    isClickedOnDropDown(dropDownArray: Array<ActionDropdown>, target: any) {

        for (var i = 0; i < dropDownArray.length; i++) {
            if (dropDownArray[i].elementRef.nativeElement.contains(target))
                return true;
        }
        return false;

    }

    getLinkMenuParam(col,rowIndex): Param {
        return this.element.collectionParams.find(ele => ele.path == this.element.path +'/'+rowIndex+'/'+ ele.config.code && ele.alias == ViewComponent.linkMenu.toString());
    }

    isActive(index){
        if (this.filterState[index]!='' && this.filterState[index]!= undefined) return true;
        else return false;
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
        event.data['nestedElement']= this.element.collectionParams.find(ele => ele.path == this.element.path +'/'+event.data.elemId+'/'+ ele.config.code && ele.alias == 'GridRowBody');
    }

    resetMultiSelection() {
        this.selectedRows = [];
    }

    sortBy(e: any, fieldType: string, sortAs: string) {
        if (this.isSortAsNumber(fieldType, sortAs)) {
            this.sortInternal(fieldValue =>  {
                if(fieldValue != null && fieldValue !== undefined)
                    return Number(fieldValue);
                else
                    return null;
            }, e);
        }
        else if (this.isSortAsDate(fieldType, sortAs)) {
            this.sortInternal(fieldValue => {
                if(fieldValue != null && fieldValue !== undefined)
                    return new Date(fieldValue);
                else
                    return null;
            }, e);
        }
        else {
            // all else are sorted as string using localeCompare
            this.value.sort((item1: any, item2: any) => {
                let value1 = item1[e.field] !== undefined ? item1[e.field]: null;
                let value2 = item2[e.field] !== undefined ? item2[e.field]: null;

                if(value1 == null && value2 == null)
                    return 0;
                if (value1 == null && value2 != null)
                    return -1 * e.order;
                else if (value1 != null && value2 == null)
                    return 1 * e.order;

                return value1.localeCompare(value2) * e.order;
            });
        }
        this.value = [...this.value];
    }

    protected sortInternal(itemCallback: Function, event: any): Array<any> {
        return this.value.sort((item1: any, item2: any) => {
            let value1 = itemCallback(item1[event.field]);
            let value2 = itemCallback(item2[event.field]);

            if(value1 == null && value2 == null)
                return 0;
            if(value1 == null && value2 != null) 
                return -1 * event.order;
            if(value1 != null && value2 == null)
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

    // between(value: any, filter: any[]){

    between(value: any, filter: any){    

        return moment(filter).isSame(value, 'day'); 
        // if(value){
        // var valueDate1 = new Date(value.toDateString());
        // var valueDate2 = new Date (filter.toDateString());
        // return valueDate1.valueOf() == valueDate2.valueOf();}
     
        // return (value >= filter[0] && value<=filter[1])
    }

    dateFilter(e: any, dt: DataTable, field: string, filterMatchMode: string, datePattern?: string, dateType?: string) {
      
    if(dateType == 'LocalDate' || dateType == 'date' || dateType == 'Date'){

        datePattern = (!datePattern || datePattern == "") ? "MM/DD/YYYY" : datePattern;

        if (e.target.value.length == '0') {
            dt.filter(e.target.value, field, "startsWith");
        }
        else {
            // let filter: any[] = [];

            if (moment(e.target.value, datePattern.toUpperCase(), true).isValid()) {
                // let formatedDate = moment(e.target.value, datePattern.toUpperCase()).format('MM/DD/YYYY');
            //    var localStartDate= moment.utc(e.target.value, datePattern.toUpperCase()).toDate();
            //    var localEndDate=moment.utc(e.target.value,  datePattern.toUpperCase()).endOf('day').toDate();
            //    filter[0]=localStartDate; filter[1]=localEndDate;
            //    dt.filter(filter, field, "between");
                dt.filter( moment(e.target.value, datePattern.toUpperCase()).toDate(), field, "between");
            }
        }
    }

    else if(dateType == 'LocalDateTime' || dateType == 'ZonedDateTime'){
        
        if (e.target.value.length == '0') {
            dt.filter(e.target.value, field, "startsWith");
        }
        else {

            if (moment(e.target.value, "MM/DD/YYYY", true).isValid()) {

                dt.filter( moment(e.target.value, "MM/DD/YYYY").toDate(), field, "between");
            }
        }
    }

        this.totalRecords = dt.dataToRender.length;
        this.updatePageDetailsState();
    }

    inputFilter(e: any, dt: DataTable, field: string, filterMatchMode: string) {
        dt.filter(e.target.value, field, filterMatchMode);
    }


    clearFilter(txt: any, dt: DataTable, field: string, index) {
        txt.value = '';
        dt.filter(txt.value, field, "");
    }

    clearAll() {
        this.filterState=[];
        this.dt.reset();
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

    toggleOpen(e: any) {

        let selectedDropDownIsOpen = e.isOpen;
        let selectedDropDownState = e.state;

        if(this.dropDowns)
        this.dropDowns.toArray().forEach((item) => {
            if (!item.selectedItem) {
                item.isOpen = false;
                item.state = 'closedPanel';
            }
        });

        e.isOpen = !selectedDropDownIsOpen;

        if (selectedDropDownState == 'openPanel') {
            e.state = 'closedPanel';
            if(!this.mouseEventSubscription.closed)
            this.mouseEventSubscription.unsubscribe();
        }
        else {
            e.state = 'openPanel';
            if(this.dropDowns && (this.mouseEventSubscription == undefined || this.mouseEventSubscription.closed))
            this.mouseEventSubscription =
                observableFromEvent(document, 'click').pipe(filter((event: any) => 
                !this.isClickedOnDropDown(this.dropDowns.toArray(), event.target)),first()).subscribe(() => 
                {
                    this.dropDowns.toArray().forEach((item) => {
                        item.isOpen = false;
                        item.state = 'closedPanel';
                    });
                    this.cd.detectChanges();
                });
        }
        e.selectedItem = false;
        this.cd.detectChanges();
    }

    export() {
        let exportDt = this.dt;
        let dtCols = this.params.filter(col => (col.type != null && ParamUtils.isKnownDateType(col.type.name)!= null))
        if(dtCols != null && dtCols.length > 0) {
            exportDt.dataToRender.forEach(row => {
                for (var key in row) {
                    if (row.hasOwnProperty(key)) {
                        if(row[key] instanceof Date) {
                            let col = dtCols.filter(cd => cd.code == key)
                            if(col != null && col.length > 0){
                                row[key] = this.dtFormat.transform(row[key], col[0].uiStyles.attributes.datePattern,col[0].type.name);
                            }
                        }
                    }
                } 
            });
        }
       
        exportDt.exportCSV();
    }

    ngOnDestroy() {
        if(this.mouseEventSubscription)   
        this.mouseEventSubscription.unsubscribe();
        this.cd.detach();
    }
}








