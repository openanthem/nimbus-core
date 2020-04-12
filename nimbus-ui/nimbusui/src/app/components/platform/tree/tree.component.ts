import { Value } from './../form/elements/value.component';
/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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
import { TreeData } from './../../../shared/param-state';
import { PageService } from './../../../services/page.service';
import { BaseTableElement } from './../base-table-element.component';
import { Component, ViewChild, ChangeDetectorRef, Input } from '@angular/core';
import {TreeNode} from 'primeng/api';
import { Tree } from 'primeng/tree';
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
    selector: 'nm-tree',
    templateUrl: './tree.component.html',
    providers: []
})
export class TreeDemo extends BaseTableElement {

    @ViewChild('nbtree')
    expandingTree: Tree;
    @Input() hideLabel: boolean;

    treedata: any;
    
    selectedFields: TreeNode[];
        
    loading: boolean;
    
    constructor(
        private pageSvc: PageService,
        protected cd: ChangeDetectorRef
      ) {
        super(cd);
      }

    ngOnInit() {
        this.loading = true;
        super.ngOnInit();
        this.treedata = [];
        for (var p in this.element.values) {
            this.treedata.push(new TreeData().deserialize(this.element.values[p]));
        }
        this.selectedFields=[];
        if(this.element.leafState) {
            this.element.leafState.split('/').forEach(e => {
                this.selectedFields.push(this.filterData(this.element.values, function(f) {
                    return e === f.code;
                }));

            });
        }
      
        // this.treedata = [
        //     {"label": "Accounting/Finance/Audting", "data": "Accounting", "children": []},
        //     {"label": "Acturial", "data": "Acturial",  "children": []},
        //     {"label": "Administrative support", "data": "Administrative support", "children": []},
        //     {"label": "Business Development & Planning", "data": "Business Development & Planning",  "children": []},
        //     {"label": "Claims", "data": "Claims",  "children": []},
        //     {"label": "Customer Service", "data": "Customer Service", "children": []},
        //     {"label": "Education & Training", "data": "Education & Training", "children": []},
        //     {"label": "Facilities", "data": "Facilities", "children": []},
        //     {"label": "Information Technology", "data": "Information Technology", 
        //       "children": [ {"label": "Java", "data": "Java"},
        //       {"label": ".NET", "data": ".NET"},
        //       {"label": "Python", "data": "Python"}]}
        // ]
      
    }
   

    filterData(data, predicate) {

        // if no data is sent in, return null, otherwise transform the data
         return !!!data ? null : data.reduce((list, entry) => {
            if (predicate(entry)) {
                // if the object matches the filter, clone it as it is
                return entry;
            } else if (entry.children != null) {
                // if the object has childrens, filter the list of children
                return this.filterData(entry.children, predicate);
            }
        }, [])

    }

    ngAfterViewInit() {
        this.expandingTree.selectionChange.subscribe(e => {
            let selected = "";
            this.selectedFields.forEach(node => {
                selected = selected=="" ? node.data : selected+","+node.data;
            });
            this.element.leafState = selected;
        });
    }
    
    postSelectedValues() {
        this.pageSvc.postOnChange(
            this.element.path,
            'state',
            JSON.stringify(this.element.leafState)
        );
    }
}