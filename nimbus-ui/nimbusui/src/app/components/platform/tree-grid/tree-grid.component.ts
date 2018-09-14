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

    public static readonly RENDERABLE_COMPONENT_ALIASES = [
        ViewComponent.button.toString()
    ]

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

    isDisplayValueColumn(col: ParamConfig, elemId: number): boolean {
        if (!col || !col.uiStyles) {
            return false;
        }

        return !this.isRenderableComponent(col) && !col.uiStyles.attributes.hidden;
    }

    isRenderableComponent(paramConfig: ParamConfig): boolean {
        if (!paramConfig || !paramConfig.uiStyles) {
            return false;
        }
        
        if (TreeGrid.RENDERABLE_COMPONENT_ALIASES.find(s => s === paramConfig.uiStyles.attributes.alias)) {
            return true;
        }
    }

    buildNestedCollectionPath(rowNode: any): string {
        // If we're at the top level, exit and return the collection element id (e.g. '0')
        if (rowNode.level <= 0) {
            return rowNode.node.data.elemId;
        }

        // Build the parent rowNode
        let parentRowNode = this.createParentRowNode(rowNode);

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

    getBaseRowNode(rowNode: any) {
        if (!rowNode.parent) {
            return rowNode;
        }
        rowNode.parent.child = rowNode;
        return this.getBaseRowNode(rowNode.parent);
    }

    getRowNodeParamConfigs(rowNode: any): ParamConfig[] {
        return this.getNestedCollectionParamConfigs(this.element.collectionParams, this.element, this.getBaseRowNode(rowNode));
    }

    getNestedCollectionParamConfigs(collectionParams: Param[], param: Param, rowNode: any): ParamConfig[] {
        if (!rowNode.child) {
            return param.config.type.elementConfig.type.model.paramConfigs;
        }
        
        // Retrieve the nested collection param.
        let nestedCollectionParam: Param = collectionParams.find(p => this.element.config.code === p.config.code);

        // Recursively retrieve the param configs for the nested collection until an
        // exit condition is satisfied.
        return this.getNestedCollectionParamConfigs(collectionParams, nestedCollectionParam, rowNode.child);
    }

    private createParentRowNode(rowNode: any): any {
        return this.createRowNode(rowNode.level - 1, rowNode.parent, rowNode.parent.parent, rowNode, true);
    }

    private createRowNode(level: number, node: any, parent: any, child: any, visible: boolean): any {
        return {
            level: level,
            node: node,
            parent: parent,
            child: child,
            visible: visible
        };
    }

}
