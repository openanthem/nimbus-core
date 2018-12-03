/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';
import { Component, Input } from '@angular/core';
import { Table } from 'primeng/table';

import { Param } from '../../../../shared/param-state';

/**
 * \@author Purna
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

@Component({
  selector: 'nm-header-checkbox',
  template: `
      <div class='ui-chkbox ui-widget' (click)='selectingAll($event, headerCB.checked)'>
          <div class='ui-helper-hidden-accessible'>
              <input #headerCB type='checkbox' [checked]='headerChckbxState' [disabled]='!dt.value || dt.value.length === 0'>
          </div>
          <div class='ui-chkbox-box ui-widget ui-state-default' [ngClass]="{'ui-state-active': headerChckbxState}">
              <span class='ui-chkbox-icon ui-clickable' [ngClass]="{'fa fa-check': headerChckbxState}"></span>
          </div>
      </div>
            `
})
export class HeaderCheckBox {

  headerChckbxState: boolean = false;
  currentSelection: Array<any> = [];

  @Input() dt: Table;
  @Input() element: Param;

  constructor() {}

  ngOnInit() {
    this.pageChangeSubscription();
    this.selectionChangeSubscription();
    this.updateToggleRowsWithCheckbox();
    // with below subscription on filter, the select-all checkbox is updated to 'unchecked' since 
    // filter would change the rows that are being displayed from what was selected before
    this.filterChangeSubscription(); 

  }

  ngDoCheck(): void {
    if (this.dt.selection && this.dt.selection.length === 0) {
        this.headerChckbxState = false;
    }
  }

  pageChangeSubscription() {
    let firstEle = this.dt.first;
    this.dt.onPage.subscribe(val => {
      const filteredValues: any[] = this.dt.filteredValue != null ? this.dt.filteredValue:this.dt.value;

      if (this.currentSelection.length > 0 && this.currentSelection[0] === filteredValues[val.first]) {
        this.dt.selection = this.currentSelection;
        this.headerChckbxState = true;
        firstEle = val.first;
        return;
      } 
      if (firstEle !== val.first) {
        this.dt.selection = [];
        this.headerChckbxState = false;
        firstEle = val.first;
      }
    });
  }

  selectionChangeSubscription() {
    this.dt.selectionChange.subscribe(val => {
      const pageSize = this.element.config.uiStyles.attributes.pageSize;
      let allMatch = true;
      const filteredValues: any[] = this.dt.filteredValue != null ? this.dt.filteredValue: this.dt.value;
      for (let i = this.dt.first; i < filteredValues.length && i < (this.dt.first + pageSize); i++) {
        let found = false;
       
        for (let j = 0; j < val.length; j++) {
          if (filteredValues[i] === val[j]) {
            found = true;
          }
        }
        if (!found) {
          this.headerChckbxState = false;
          allMatch = false;
          this.currentSelection = [];
          break;
        }
      }
      if (allMatch) {
        this.headerChckbxState = true;
        if (this.currentSelection.length === 0) {
              this.updateDtSelection();
        }
      }
    });
  }

  filterChangeSubscription() {
    this.dt.onFilter.subscribe(val => {
        this.headerChckbxState = false;
    });
  }

  // following method will override the dt.toggleRowsWithCheckbox method as dt.selection is not modified by this method
  // dt.selection  will be updated by this component
  updateToggleRowsWithCheckbox() {
    this.dt.toggleRowsWithCheckbox = (event: Event, check: boolean) => {
      this.dt.preventSelectionSetterPropagation = true;
      this.dt.updateSelectionKeys();
      this.dt.selectionChange.emit(this.dt.selection);
      this.dt.tableService.onSelectionChange();
      this.dt.onHeaderCheckboxToggle.emit({
        originalEvent: event,
        checked: check
      });
    };
  }

  selectingAll(event, check) {
    if (check) {
      this.dt.selection = [];
      this.headerChckbxState = false;
    } else {
      this.headerChckbxState = true;
      this.updateDtSelection();
    }
    this.onClickSelectAll(event, !check);
  }

  updateDtSelection() {
    this.dt.selection = [];
    const pageSize = this.element.config.uiStyles.attributes.pageSize;
    const filteredValues: any[] = this.dt.filteredValue != null ? this.dt.filteredValue:this.dt.value;
    for (let i = this.dt.first; i < filteredValues.length && i < (this.dt.first + pageSize); i++) {
        this.dt.selection.push(filteredValues[i]);
    }
    this.currentSelection = this.dt.selection;
  }

  onClickSelectAll(event, check) {
    if (this.dt.value && this.dt.value.length > 0) {
      this.dt.toggleRowsWithCheckbox(event, !check);
    }
  }
}