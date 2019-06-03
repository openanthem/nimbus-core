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
export const displayValueDirectiveConfig: any = {
  active: false,
  required: false,
  id: '632',
  code: 'lastName',
  validations: null,
  uiNatures: [],
  uiStyles: {
    isLink: false,
    isHidden: false,
    name: 'ViewConfig.GridColumn',
    attributes: {
      hidden: false,
      readOnly: false,
      submitButton: true,
      showName: true,
      pageSize: 25,
      browserBack: false,
      showAsLink: false,
      rowExpander: false,
      expandable: true,
      applyValueStyles: true,
      sortable: true,
      filter: false,
      filterMode: 'equals',
      cssClass: '',
      filterValue: '',
      datePattern: '',
      alias: 'GridColumn',
      placeholder: '',
      sortAs: 'DEFAULT'
    }
  },
  type: {
    collection: false,
    nested: false,
    name: 'string'
  },
  field: 'lastName',
  label: 'Last Name',
  header: 'Last Name',
  exportable: true
};

export const displayValueDirectiveChanges: any = {
  displayValue: {
    currentValue: 'test',
    firstChange: true
  },
  config: {
    currentValue: {
      active: false,
      required: false,
      id: '631',
      code: 'firstName',
      validations: null,
      uiNatures: [],
      uiStyles: {
        isLink: false,
        isHidden: false,
        name: 'ViewConfig.GridColumn',
        attributes: {
          hidden: false,
          readOnly: false,
          submitButton: true,
          showName: true,
          pageSize: 25,
          browserBack: false,
          showAsLink: false,
          rowExpander: false,
          expandable: true,
          applyValueStyles: false,
          sortable: true,
          filter: false,
          filterMode: 'equals',
          cssClass: '',
          filterValue: '',
          datePattern: '',
          alias: 'GridColumn',
          placeholder: '',
          sortAs: 'DEFAULT'
        }
      },
      type: {
        collection: false,
        nested: false,
        name: 'string'
      },
      field: 'firstName',
      label: 'First Name',
      header: 'First Name',
      exportable: true
    },
    firstChange: true
  }
};
