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

import { Component, Input, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { BaseTableElement } from './../base-table-element.component';
import { WebContentSvc } from '../../../services/content-management.service';
import { ParamConfig } from '../../../shared/param-config';
import { PageService } from '../../../services/page.service';
import { GenericDomain } from '../../../model/generic-domain.model';
import { Param, TreeGridDeserializer } from '../../../shared/param-state';
import { HttpMethod } from './../../../shared/command.enum';
import { ViewComponent } from '../../../shared/param-annotations.enum';
import { GridUtils } from './../../../shared/grid-utils';
import { ChangeDetectorRef } from '@angular/core';
import { TreeTable } from 'primeng/primeng';


/**
 * \@author Vivek Kamineni
 * \@author Tony Lopez
 * \@whatItDoes A control to be used to display closely related data in a Tree structure.
 */
@Component({
    selector: 'nm-treegrid',
    providers: [WebContentSvc],
    templateUrl: './tree-grid.component.html'
})
export class TreeGrid extends BaseTableElement implements ControlValueAccessor {

    @Input() params: ParamConfig[];
    @Input() form: FormGroup;
    firstColumn: ParamConfig;
    viewComponent = ViewComponent;
    treeData: any;

    @ViewChild('tt') tt: TreeTable;

    public onChange: any = (_) => { /*Empty*/ }
    public onTouched: any = () => { /*Empty*/ }

    private collectionAlias: string;

    public static readonly RENDERABLE_COMPONENT_ALIASES = [
        ViewComponent.button.toString(),
        ViewComponent.linkMenu.toString()
    ]

    public writeValue(obj: any): void { }

    public registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    public registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    constructor(
        protected _wcs: WebContentSvc,
        private pageSvc: PageService,
        private gridUtils: GridUtils,
        protected cd: ChangeDetectorRef) {
        super(_wcs, cd);
    }

    ngOnInit() {
        super.ngOnInit();
        this.pageSvc.processEvent(this.element.path, '$execute', new GenericDomain(), HttpMethod.GET.value, undefined);
        this.pageSvc.gridValueUpdate$.subscribe((treeList: Param) => {
            if (this.element.path === treeList.path) {
                this.tt.first = 0;
                this.treeData = this.getTreeStructure(treeList.gridData.leafState);
                
            }
        });

        // For convenience        
        this.collectionAlias = this.element.config.type.elementConfig.type.model.paramConfigs.find((config) =>
            config.uiStyles.attributes.alias === this.viewComponent.treeGridChild.toString()).code;

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
            data.push(new TreeGridDeserializer().deserialize(row));
        });

        return data;
    }

    isDisplayValueColumn(col: ParamConfig): boolean {
        if (!col || !col.uiStyles || (col && col.type && col.type.collection)) {
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
        let parentRowNode = RowNodeUtils.createParentRowNode(rowNode);

        // Recursive build the param path until an exit condition is met 
        // (e.g. 0/owners/4.../owners/2)
        return `${this.buildNestedCollectionPath(parentRowNode)}/${this.collectionAlias}/${rowNode.node.data.elemId}`;
    }

    getViewParam(col: ParamConfig, rowNode: any): Param {
        let nestedCollectionPath = this.getRowPath(col, rowNode);
        return this.element.collectionParams.find(p => p.path === nestedCollectionPath);
    }

    getRowNodeParamConfigs(rowNode: any): ParamConfig[] {
        return this.getNestedCollectionParamConfigs(this.element.collectionParams, this.element, RowNodeUtils.getBaseRowNode(rowNode));
    }

    getNestedCollectionParamConfigs(collectionParams: Param[], param: Param, rowNode: any): ParamConfig[] {
        if (!rowNode.activeChild) {
            return param.config.type.elementConfig.type.model.paramConfigs;
        }

        // Find the first collection param, who is annotated with @TreeGridChild.
        // Continue this manner recursively until the last child is found.
        let treegridChild: Param = collectionParams.find(p => this.viewComponent.treeGridChild.toString() === p.config.uiStyles.attributes.alias);
        if (!treegridChild) {
            return param.config.type.elementConfig.type.model.paramConfigs;
        }
        return this.getNestedCollectionParamConfigs(collectionParams, treegridChild, rowNode.activeChild);
    }

    getRowPath(col: ParamConfig, rowNode: any) {
        return `${this.element.path}/${this.buildNestedCollectionPath(rowNode)}/${col.code}`;
    }

}

/**
 * \@author Tony Lopez
 * \@whatItDoes A utility class for working with the PrimeNG RowNode attribute within TreeGrid.
 */
export class RowNodeUtils {

    /**
     * Create a parent rowNode from the given rowNode argument.
     * @param rowNode the starting rowNode object
     */
    static createParentRowNode(rowNode: any): any {
        return this.createRowNode(rowNode.level - 1, rowNode.parent, rowNode.parent.parent, rowNode, true);
    }

    /**
     * Create a TreeGrid rowNode object following the domain specification as listed by PrimeNG. This
     * method is a simple utility method for creating row nodes.
     * @param level the depth of the tree
     * @param node contains the expected data model for this row
     * @param parent the parent node of this row
     * @param activeChild the child node of this row (if applicable)
     * @param visible whether or not this row is visible
     */
    static createRowNode(level: number, node: any, parent: any, activeChild: any, visible: boolean): any {
        return {
            level: level,
            node: node,
            parent: parent,
            activeChild: activeChild,
            visible: visible
        };
    }

    /**
     * Retrieve the highest rowNode object from the given rowNode by recursively traversing it's parents
     * until a parent is unable to be found in the tree. It then logically follows that the last element found
     * is the base rowNode.
     * This method also sets activeChild into rowNode along the traversal process to specify the path the 
     * tree traversal followed. Traversing the resulting rowNode's activeChildren performs the reverse process
     * of this method.
     * @param rowNode the starting rowNode object
     */
    static getBaseRowNode(rowNode: any) {
        if (!rowNode.parent) {
            return rowNode;
        }
        rowNode.parent.activeChild = rowNode;
        return this.getBaseRowNode(rowNode.parent);
    }

}