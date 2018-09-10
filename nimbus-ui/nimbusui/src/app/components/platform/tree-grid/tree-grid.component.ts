import { Component, Input } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { BaseElement } from './../base-element.component';
import { WebContentSvc } from '../../../services/content-management.service';
import { ParamConfig } from '../../../shared/param-config';
import { PageService } from '../../../services/page.service';
import { GenericDomain } from '../../../model/generic-domain.model';


@Component({
    selector: 'nm-treegrid',
    templateUrl: './tree-grid.component.html'
})
export class TreeGrid extends BaseElement  implements ControlValueAccessor {

    @Input() params: ParamConfig[];
    @Input() form: FormGroup;

    treeData: any;

    public onChange: any = (_) => { /*Empty*/ }
    public onTouched: any = () => { /*Empty*/ }

    public writeValue(obj: any): void {
        if (obj !== undefined) {
        }
    }

    public registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    public registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    constructor(private _wcs: WebContentSvc, private pageSvc: PageService, ) {
        super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();

        this.pageSvc.processEvent(this.element.path, '$execute', new GenericDomain(), 'GET', undefined);
        this.pageSvc.treeListUpdate$.subscribe((treeList: {data: {}}) => {
            console.log("treeList in ts file", treeList.data);
            this.treeData = treeList.data;
        });

    }

    getCellDisplayValue(rowData: any, col: ParamConfig) {
        let cellData = rowData[col.code];
        return cellData
    }

    showColumn(col: ParamConfig) {
        if (col.uiStyles && col.uiStyles.attributes.hidden === false) {
            return true;
        } 
        return false;
    }

    showHeader(col: ParamConfig) {
        if (col.uiStyles && col.uiStyles.attributes.hidden === false &&
            col.uiStyles.attributes.alias === 'GridColumn') {
            return true;
        } 
        return false;
    }

    getLabelValue(col: ParamConfig){

        if(col.labelConfigs){
            return col.labelConfigs[0].text;
        }
    }
    
}
