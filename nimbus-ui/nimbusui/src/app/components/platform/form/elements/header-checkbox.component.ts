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
 * 
 * \@author Swetha Vemuri
 * 
 * \@whatItDoes 
 * 
 * Custom implementation for a select-all checkbox in a table header
 * which overrides prime-ng's default implementation of selecting all the records in a paginated dataset when checked. 
 * This component provides the capability to configure the behaviour of select-all checkbox in the table header
 * based on the boolean value of grid attribute - `headerCheckboxToggleAllPages`.
 * When the attribute is set to true the behavior is to select data in all pages in a dataset.
 * When set to false (default) the behavior is to select only records within the current page (when table is paginated)
 * and not to select all the records in a dataset.
 * Primeng provides <p-tableHeaderCheckbox> with the default behavior
 * of selecting all the records in a dataset when checked.
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
  /**
   * Method to compute and set the state of select-all checkbox whenever a page is changed in a paginated table.
   */
  pageChangeSubscription() {
    let firstEle = this.dt.first;
    this.dt.onPage.subscribe(val => {
      const filteredValues: any[] = this.dt.filteredValue != null ? this.dt.filteredValue : this.dt.value;
      if (!this.isHeaderCheckboxToggleAllPages()) {
        /** If the current page is same as the header check box selected page, retain the selection */
        if (this.currentSelection.length > 0 && this.currentSelection[0] === filteredValues[val.first]) {
          this.dt.selection = this.currentSelection;
          this.headerChckbxState = true;
          firstEle = val.first;
          return;
        }
        /** If the current page is different from the header check box selected page, uncheck the header checkbox,
         * also do not retain any individual row selections */
        if (firstEle !== val.first) {
          this.dt.selection = [];
          this.headerChckbxState = false;
          firstEle = val.first;
        }
      } else {
        if (this.currentSelection.length > 0 && this.currentSelection.length === filteredValues.length) {
          this.dt.selection = this.currentSelection;
          firstEle = val.first;
          return;
        }
      }
    });
  }
  /**
   * Enters the method whenever any selection changes on the table. It can be a single row or all rows in the page/table
   * 
   */
  selectionChangeSubscription() {
    this.dt.selectionChange.subscribe(val => {
      const pageSize = this.element.config.uiStyles.attributes.pageSize;
      let allMatch = true;
      const filteredValues: any[] = this.dt.filteredValue != null ? this.dt.filteredValue: this.dt.value;
      /* The outer for loop checks for all rows within the current page*/
      for (let i = this.dt.first; i < filteredValues.length && i < (this.dt.first + pageSize); i++) {
        let found = false;

        /* Inner loop is for the value that subscribed for selection change.
        It can be a single value or an array of values. 
        This checks if the selection is made for all rows within the page or only a few */
        for (let j = 0; j < val.length; j++) {
          if (filteredValues[i] === val[j]) {
            found = true;
          }
        }
        /** found = true if all values in page are selected. 
         *  found = false when only some values have been selected in the current page.
         */
        if (!found) {
          this.headerChckbxState = false;
          allMatch = false;
          this.currentSelection = [];
          break;
        }
      }
      /** If all the rows in the current page are selected, update table selection & 
       *  set headerChckbxState to true if `headerCheckboxToggleAllPages` is false
      */
      if (allMatch) {
        if (!this.isHeaderCheckboxToggleAllPages()) {
          this.headerChckbxState = true;
          if (this.currentSelection.length === 0) {
            this.updateDtSelection();
          }
        }
      }
    });
  }

  /**
   * Whenever any filter on the table is triggered, reset the selectAll checkbox on the header to unchecked state.
   */
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

  /** Invoke this method when selectAll checkbox id checked in header of the table.
   * When the checkbox is selected, `check` parameter is false.
   */
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

  /**
   * Update the DataTable selection (dt.selection) with the selected values.
   * Before 1.2.0 - Update dt.selection with the current page values only.
   * Since 1.2.0 - Based on the grid attribute `headerCheckboxToggleAllPages` - when set to true,
   * update the dt.selection with data from all pages in the dataset.
   */
  updateDtSelection() {
    this.dt.selection = [];
    const filteredValues: any[] = this.dt.filteredValue != null ? this.dt.filteredValue : this.dt.value;

    const startSelection = this.isHeaderCheckboxToggleAllPages() ? 0 : this.dt.first;
    const maxRowsAllowed = this.getMaxRowsAllowed(filteredValues.length);
    for (let i = startSelection ; i < filteredValues.length && i < maxRowsAllowed; i++) {
      /** If `headerCheckboxToggleAllPages`  is true (startSelection = 0) - add all filtered records in all pages to selection*/
      /** If `headerCheckboxToggleAllPages` is false (startSelection = this.dt.first) i.e first record of current page
       * - add filtered records in just the current page to the selection */
      this.dt.selection.push(filteredValues[i]);
    }
    this.currentSelection = this.dt.selection;
  }

  onClickSelectAll(event, check) {
    if (this.dt.value && this.dt.value.length > 0) {
      this.dt.toggleRowsWithCheckbox(event, !check);
    }
  }

  /**
   * Return the allowed size of data that can be selected based on the grid attribute `headerCheckboxToggleAllPages`
   * @param filteredValuesLength
   */
  getMaxRowsAllowed(filteredValuesLength: number): number {
    const pageSize = this.element.config.uiStyles.attributes.pageSize;
    if (!this.element.config.uiStyles.attributes.headerCheckboxToggleAllPages) {
      return this.dt.first + pageSize;
    }
      return filteredValuesLength;
  }

  isHeaderCheckboxToggleAllPages(): boolean {
    return this.element.config.uiStyles.attributes.headerCheckboxToggleAllPages;
  }
}
