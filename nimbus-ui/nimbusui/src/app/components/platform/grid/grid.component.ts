import { BaseElement } from './../base-element.component';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Component, Input, Output, forwardRef, ViewChild, EventEmitter, ViewEncapsulation, ChangeDetectorRef } from '@angular/core';

import { GenericDomain } from '../../../model/generic-domain.model';
import { Param, ParamConfig } from '../../../shared/app-config.interface';
import { PageService } from '../../../services/page.service';
import { GridService } from '../../../services/grid.service';
import { WebContentSvc } from '../../../services/content-management.service';
import { DataTable, OverlayPanel, Paginator } from 'primeng/primeng';
import { ElementModelParam } from './../../../shared/app-config.interface';
import { ServiceConstants } from './../../../services/service.constants';
import { ControlValueAccessor } from '@angular/forms/src/directives';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InfiniteScrollGrid),
    multi: true
  };

@Component({
    selector: 'infinite-scroll-grid',
    providers: [ CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc ],
    encapsulation: ViewEncapsulation.None,
    templateUrl:'./grid.component.html'
})
export class InfiniteScrollGrid extends BaseElement implements ControlValueAccessor{
    @Input() data: any[];
    @Output() onScrollEvent: EventEmitter<any> = new EventEmitter();
    @Input() params: ElementModelParam[];
    @Input() form: FormGroup;
    @Input('value') _value = [];

//    references DataTable named 'flex' in the view
    @ViewChild('flex') flex: DataTable;
    @ViewChild('dt') dt: DataTable;
    @ViewChild ('op') overlayPanel: OverlayPanel;
    
    summaryData: any;
    rowHover:boolean;
    selectedRows: any[];
    filterState: boolean = false;

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

    fg= new FormGroup({}); // TODO this is for the filter controls that need to be embedded in the grid 
    private imagesPath: string;
    
    constructor(
        private pageSvc : PageService, 
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

        if(this.element.config.gridList!=null && this.element.config.gridList.length > 0) {
            this.value = this.element.config.gridList;
        }
    }

    ngAfterViewInit() {
        this.imagesPath = ServiceConstants.IMAGES_URL;
        if (this.params != null) {
            this.params.forEach(element => {
                if(element != null) {
                    if(element.uiStyles && element.uiStyles.attributes && 
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

        this.rowHover=true;
        this.gridService.eventUpdate$.subscribe(data => {
            this.summaryData = data;
        });

        this.pageSvc.gridValueUpdate$.subscribe(event => {
            if(event.path.startsWith(this.element.path)) {
                this.value = event.config.gridList;
                this.cd.markForCheck();
                this.resetMultiSelection();
            }
        });

        if(this.form!= undefined && this.form.controls[this.element.config.code]!= null) {
            this.pageSvc.validationUpdate$.subscribe(event => {
                let frmCtrl = this.form.controls[event.config.code];
                if(frmCtrl!=null && event.path.startsWith(this.element.path)) {
                    if(event.enabled.currState)
                        frmCtrl.enable();
                    else
                        frmCtrl.disable();
                }
            });
        }
    }

    getRowPath(col:ElementModelParam, item: any) {
        return this.element.path + '/' + item.elemId;// + '/' + col.code;
    }

    processOnClick(col: ElementModelParam, item: any) {
        let uri=this.element.path + '/' + item.elemId + '/' + col.code;

        let uriParams = this.getAllURLParams(uri);
        if(uriParams!=null) {
            for (let uriParam of uriParams) {
                let p = uriParam.substring(1, uriParam.length-1);
                if (item[p]) {
                    uri = uri.replace(new RegExp(uriParam, 'g'), item[p]);
                }
            }
        }
        this.pageSvc.processEvent(uri, col.uiStyles.attributes.b, item, col.uiStyles.attributes.method);
    }

    /* look for parameters in URI {} */
    getAllURLParams (uri: string): string[] {
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
        
        item.addAttribute(this.element.config.uiStyles.attributes.postButtonTargetPath,elemIds);
        this.pageSvc.processEvent(this.element.config.uiStyles.attributes.postButtonUrl, null, item, 'POST');
    }

    onRowSelect(event) {
        //console.log(event);
        //this.pageService.postOnChange($event.path, '_update', 'state', JSON.stringify(true));
    }

    onRowUnselect(event) {
        //console.log(event);
        //this.pageService.postOnChange($event.path, '_update', 'state', JSON.stringify(true));
    }
    onRowClick(event: any) {
        //console.log(event);
        //this.pageService.postOnChange($event.path, '_update', 'state', JSON.stringify(true));
    }
    onRowUnSelect(event) {
        //console.log(event);
        //this.pageService.postOnChange($event.path, '_update', 'state', JSON.stringify(false));
    }

    postOnChange(col: ElementModelParam, item: any) {
        let uri=this.element.path + '/' + item.elemId + '/' + col.code;
        //console.log(event);
        this.pageSvc.postOnChange(uri, 'state', JSON.stringify(event.target['checked']));
    }

    handleRowChange(val) {
        //this.cd.markForCheck();
        // console.log('onRowUpdate');
        // console.log(val);
    }

    getAddtionalData(event: any) {
        let elemPath = '';
        this.params.forEach(param => {
            if (param.uiStyles && param.uiStyles.attributes.alias == 'Grid') {
                elemPath = this.element.path + '/' + event.data.elemId + '/' + param.code;
            }
        });
        
        this.pageSvc.processEvent(elemPath, '$execute', new GenericDomain(), 'GET' );
    }

    resetMultiSelection() {
        this.selectedRows = [];
    }
}
