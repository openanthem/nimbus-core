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


import { Component, Input } from '@angular/core';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { BaseElement } from './../base-element.component';
import { WebContentSvc } from '../../../services/content-management.service';
import { ParamConfig } from '../../../shared/param-config';
import { PageService } from '../../../services/page.service';
import { GenericDomain } from '../../../model/generic-domain.model';
import { Param, TreeGridDeserializer } from '../../../shared/param-state';
import { HttpMethod } from './../../../shared/command.enum';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { ViewComponent} from '../../../shared/param-annotations.enum';


/**
 * \@author Vivek Kamineni
 * \@author Tony Lopez
 * \@whatItDoes A control to be used to display closely related data in a Tree structure.
 */


@Component({
    selector: 'nm-treegrid',
    providers: [WebContentSvc, DateTimeFormatPipe],
    templateUrl: './tree-grid.component.html'
})
export class TreeGrid extends BaseElement  implements ControlValueAccessor {

    @Input() params: ParamConfig[];
    @Input() form: FormGroup;
    firstColumn: ParamConfig;
    viewComponent = ViewComponent;
    treeData: any;

    public onChange: any = (_) => { /*Empty*/ }
    public onTouched: any = () => { /*Empty*/ }

    private collectionAlias: string;

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

    constructor(private _wcs: WebContentSvc, private pageSvc: PageService,  private dtFormat: DateTimeFormatPipe) {
        super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();

        this.pageSvc.processEvent(this.element.path, '$execute', new GenericDomain(), HttpMethod.GET.value, undefined);
        this.pageSvc.gridValueUpdate$.subscribe((treeList: Param) => {
            if(this.element.path === treeList.path){
                this.treeData = this.getTreeStructure(treeList.gridList);
            }
        });

        // For convenience
        this.collectionAlias = this.element.config.code;

        if (this.params) {
            this.params.forEach(column => {
                column.label = this._wcs.findLabelContentFromConfig(this.element.elemLabels.get(column.id), column.code).text;
                column['field'] = column.code;
                column['header'] = column.label;                  
            });

            this.firstColumn = this.params.find((param) => param.uiStyles && param.uiStyles.attributes.hidden === false);
        }
        
    }

    getTreeStructure(gridList: any[]) {
        let data: any[] = [];
        gridList.forEach(row => {                    
            new TreeGridDeserializer().deserialize(row)            
            data.push(new TreeGridDeserializer().deserialize(row));        
        });

        return data;
    }

    getCellDisplayValue(rowData: any, col: ParamConfig) {
        let cellData = rowData[col.code];
        if (cellData) {
            if (super.isDate(col.type.name)) {
                return this.dtFormat.transform(cellData, col.uiStyles.attributes.datePattern, col.type.name);
            } else {
                return cellData;
            }
        }
        else {
            return col.uiStyles.attributes.placeholder;
        }
    }

    isDisplayValueColumn(col: ParamConfig, elemId: number) {
        if (!col || !col.uiStyles) {
            return false;
        }

        return !this.isRenderableComponent(col, elemId) && !col.uiStyles.attributes.hidden;
    }

    isRenderableComponent(paramConfig: ParamConfig, elemId: number) {
        if (!paramConfig || !paramConfig.uiStyles) {
            return false;
        }
        
        if (this.treeData[elemId].data && this.treeData[elemId].data.nestedGridParam) {

            // Look through the nested grid params and see if paramConfig
            // is contained within it. If it is, it is a "renderable" component.
            // See the building of nestedGridParam for more details...
            let params = this.treeData[elemId].data.nestedGridParam as Param[];
            for(var p of params) {
                if (paramConfig.id === p.configId) {
                    return true;
                }
            }
        }
    }

    buildNestedCollectionPath(rowNode: any): string {
        // If we're at the top level, exit and return the collection element id (e.g. '0')
        if (rowNode.level <= 0) {
            return rowNode.node.data.elemId;
        }

        // Build the parent rowNode
        let parentRowNode = {
            level: rowNode.level - 1,
            node: rowNode.parent,
            parent: rowNode.parent.parent,
            visible: true
        };

        // Recursive build the param path until an exit condition is met 
        // (e.g. 0/owners/4.../owners/2)
        return `${this.buildNestedCollectionPath(parentRowNode)}/${this.collectionAlias}/${rowNode.node.data.elemId}`;
    }

    getViewParam(col: ParamConfig, rowNode: any): Param {
        let nestedCollectionPath = `${this.element.path}/${this.buildNestedCollectionPath(rowNode)}/${col.code}`;
        return this.element.collectionParams.find(p => p.path === nestedCollectionPath);
    }

    showHeader(col: ParamConfig) {
        if (col.uiStyles && col.uiStyles.attributes.hidden === false &&
            col.uiStyles.attributes.alias === 'GridColumn') {
            return true;
        } 
        return false;
    }

    getNestedCollectionParamConfigs(paramConfigs: ParamConfig[], level: number) {
        // TODO REMOVE: This if statement should be removed when the nestedCollection.type.model.paramConfigs
        // issue below is resolved. For now it is a testing fix.
        if (paramConfigs.length === 0) {
            return paramConfigs;
        }

        if (level <= 0) {
            return paramConfigs;
        }

        // Retrieve the nested collection param config.
        let nestedCollection = paramConfigs.find(pc => pc.code === this.collectionAlias);

        // TODO nestedCollection.type.model.paramConfigs is coming back as an empty array
        // This needs to be fixed during the deserialization process for tree grid to get
        // the necessary param configs.
        // Until this resolved, this will throw an error when the tree grid level > 2.

        // Recursively retrieve the param configs for the nested collection until an
        // exit condition is satisfied.
        return this.getNestedCollectionParamConfigs(nestedCollection.type.model.paramConfigs, level - 1);
    }

}
