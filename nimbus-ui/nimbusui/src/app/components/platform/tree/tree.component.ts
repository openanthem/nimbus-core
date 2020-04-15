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
import { TreeData, Values } from './../../../shared/param-state';
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
    
    tempList: String[];
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
        this.setState(this.element.leafState);     
    }
   

    setState(val) {
        if(val) {
            if(this.selectedFields == null) {
                this.selectedFields = [];
            }
            this.tempList = val;
            this.tempList.forEach(item => {
                let searchedItems: Array<any> = [];
                this.selectedFields.push(this.search(this.treedata, item, searchedItems));
            });
        } 
    }

    search(values: TreeNode[], key: String, searchedItems) {
        values.forEach(val => {
            if(val.data == key) {
                searchedItems.push(val);
            } else if(val.children) {
                if(val.children.length > 0 ) {
                    this.search(val.children, key, searchedItems);
                }
            }
        });
        return searchedItems[0];
      }

    ngAfterViewInit() {
        this.expandingTree.selectionChange.subscribe(e => {
            let selected = [];
            this.selectedFields.forEach(node => {
                selected.push(node.data);
            });
            this.element.leafState = selected;
            if(this.element.config.uiStyles.attributes.postEventOnChange) {
                this.postSelectedValues();
            }
        });

        this.subscribers.push(
            this.pageSvc.eventUpdate$.subscribe(event => {
              if (event.path === this.element.path) {
                this.setState(event.leafState);
              }
            })
          );
    }
    
    postSelectedValues() {
        this.pageSvc.postOnChange(
            this.element.path,
            'state',
            JSON.stringify(this.element.leafState)
        );
    }
}