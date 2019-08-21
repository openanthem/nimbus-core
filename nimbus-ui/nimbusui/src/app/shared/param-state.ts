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

'use strict';

import { DataGroup } from './../components/platform/charts/chartdata';
import { TableComponentConstants } from './../components/platform/grid/table.component.constants';
import { ConfigService } from './../services/config.service';
import { Message } from './message';
import { Converter } from './object.conversion';
import { ViewComponent } from './param-annotations.enum';
import { LabelConfig, ParamConfig } from './param-config';
import { ParamUtils } from './param-utils';
import { Serializable } from './serializable';

export interface NestedParams {
  [id: string]: Param;
}
export interface CollectionParams {
  [id: number]: NestedParams;
}

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
export class Param implements Serializable<Param, string> {
  configId: string;
  type: Type;
  leafState: any;
  path: string;
  collection: boolean;
  collectionElem: boolean;
  enabled: boolean = true;
  message: Message[];
  paramState: Param[];
  values: Values[];
  visible: boolean = true;
  activeValidationGroups: String[] = [];
  page: GridPage;
  _alias: string;
  _config: ParamConfig;
  labels: LabelConfig[];
  elemLabels: Map<string, LabelConfig[]>;
  tableBasedData: TableBasedData;
  style: StyleState;

  elemId: string;

  constructor(private configSvc: ConfigService) {}

  public get config(): ParamConfig {
    return this.configSvc.getViewConfigById(this.configId);
  }

  public get alias(): string {
    if (this.config.uiStyles && this.config.uiStyles.attributes) {
      return this.config.uiStyles.attributes.alias;
    }
    return undefined;
  }

  private constructPath(path, inJson) {
    let paramPath;
    if (path == inJson.path) {
      paramPath = path;
    } else if (path == undefined && this.config) {
      paramPath = '/' + this.config.code;
    } else if (path && this.config) {
      paramPath = path + '/' + this.config.code;
    } else if (path && this.config && inJson.elemId) {
      paramPath = path + '/' + inJson.elemId + '/' + this.config.code;
    } else if (path && inJson.elemId) {
      paramPath = path + '/' + inJson.elemId;
    } else {
      paramPath = path;
    }

    return paramPath;
  }
  deserialize(inJson, path) {
    this.configId = inJson.configId;
    // Set Config in ParamConfig Map
    if (inJson.config != null) {
      let config: ParamConfig = new ParamConfig(this.configSvc).deserialize(
        inJson.config
      );
      this.configSvc.setViewConfigToParamConfigMap(config.id, config);
    }

    if (typeof inJson.collectionElem !== 'undefined') {
      this.collectionElem = inJson.collectionElem;
    }

    if (typeof inJson.collection !== 'undefined') {
      this.collection = inJson.collection;
    }
    if (inJson.collectionElem) {
      this.elemId = inJson.elemId;
    }

    this.path = this.constructPath(path, inJson);
    if (inJson.type != null) {
      this.type = new Type(this.configSvc).deserialize(inJson.type, this.path);
    } else {
      this.type = new Type(this.configSvc);
      this.type['nested'] = false;
      this.type['name'] = 'string';
      this.type['collection'] = false;
    }
    if (ParamUtils.isTableBasedComponent(this)) {
      // deserialize Page
      if (inJson.page) {
        this.page = new GridPage().deserialize(inJson.page);
      }
      this.tableBasedData = TableBasedData.fromParam(this.configSvc, this);
    } else if (
      this.config != null &&
      this.config.uiStyles &&
      this.config.uiStyles.attributes.alias === ViewComponent.chart.toString()
    ) {
      let data: DataGroup[] = [];
      if (inJson.leafState != null) {
        this.leafState = new DataGroup().deserialize(inJson.leafState);
      } else if (inJson.type && inJson.type.model && inJson.type.model.params) {
        for (var p in inJson.type.model.params) {
          let eventModelParam = new Param(this.configSvc).deserialize(
            inJson.type.model.params[p],
            this.path
          );
          if (eventModelParam.leafState != null) {
            data.push(new DataGroup().deserialize(eventModelParam.leafState));
          }
        }
        this.leafState = data;
      }
    } else if (
      this.config &&
      this.config.type &&
      ParamUtils.isKnownDateType(this.config.type.name)
    ) {
      this.leafState = ParamUtils.convertServerDateStringToDate(
        inJson.leafState,
        this.config.type.name
      );
    } else {
      this.leafState = inJson.leafState;
      // Handle any transformations that need to be applied to the leaf state
      if (
        typeof inJson.leafState === 'object' &&
        (this.type.model && this.type.model.params)
      ) {
        this.leafState = ParamUtils.applyLeafStateTransformations(
          inJson.leafState,
          this
        );
      }
    }
    if (inJson.style) {
      this.style = new StyleState().deserialize(inJson.style);
    }
    if (typeof inJson.visible !== 'undefined') {
      this.visible = inJson.visible;
    }
    if (inJson.enabled != null) {
      this.enabled = inJson.enabled;
    }
    this.message = [];
    if (inJson.message != null && inJson.message.length > 0) {
      for (const msg in inJson.message) {
        if (inJson.message[msg]) {
          this.message.push(new Message().deserialize(inJson.message[msg]));
        }
      }
    }
    this.values = [];
    if (inJson.values != null && inJson.values.length > 0) {
      for (var value in inJson.values) {
        this.values.push(new Values().deserialize(inJson.values[value]));
      }
    }

    if (typeof inJson.activeValidationGroups === 'object') {
      this.activeValidationGroups = inJson.activeValidationGroups;
    }

    this.labels = [];
    if (inJson.labels != null && inJson.labels.length > 0) {
      for (var p in inJson.labels) {
        this.labels.push(new LabelConfig().deserialize(inJson.labels[p]));
      }
    }

    this.elemLabels = new Map<string, LabelConfig[]>();
    if (inJson.elemLabels) {
      const configIds: string[] = Object.keys(inJson.elemLabels);
      configIds.forEach(configId => {
        const labels = [];
        if (
          inJson.elemLabels[configId] != null &&
          inJson.elemLabels[configId].length > 0
        ) {
          for (var p in inJson.elemLabels[configId]) {
            labels.push(
              new LabelConfig().deserialize(inJson.elemLabels[configId][p])
            );
          }
        }
        this.elemLabels.set(configId, labels);
      });
    }
    return this;
  }
}

export class Values implements Serializable<Values, string> {
  code: string;
  label: string;
  desc: string;

  deserialize(inJson) {
    var obj = this;
    obj = Converter.convert(inJson, obj);
    return obj;
  }

  static of(code: string, label: string): Values {
    return new Values().deserialize({
      code: code,
      label: label
    });
  }
}

export class Model implements Serializable<Model, string> {
  params: Param[];

  constructor(private configSvc: ConfigService) {}

  deserialize(inJson, path) {
    this.params = [];
    for (var p in inJson.params) {
      if (!ParamUtils.isEmpty(inJson.params[p])) {
        //param when null means that there is an @Ignore(event = websocket) on the parameter
        this.params.push(
          new Param(this.configSvc).deserialize(inJson.params[p], path)
        );
      }
    }
    return this;
  }
}

export class Type implements Serializable<Type, string> {
  model: Model;

  constructor(private configSvc: ConfigService) {}

  deserialize(inJson, path) {
    if (inJson.model != null) {
      this.model = new Model(this.configSvc).deserialize(inJson.model, path);
    } else if (inJson.modelConfig != null) {
      this.model = new Model(this.configSvc).deserialize(
        inJson.modelConfig,
        path
      );
    }

    return this;
  }
}

export class GridPage implements Serializable<GridPage, string> {
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  pageNumber: number;
  first: boolean;
  numberOfElements: number;

  deserialize(inJson) {
    var obj = this;
    obj = Converter.convert(inJson, obj);
    return obj;
  }
}

export class TreeGridDeserializer {
  setTreeChildren(node, nestedArray) {
    node.children = [];
    nestedArray.forEach(nestedArrayItem =>
      node.children.push(this.deserialize(nestedArrayItem, nestedArrayItem))
    );
  }

  deserialize(objectItem, data?, children?) {
    try {
      let node: any = {};
      node.data = {};

      if (Array.isArray(objectItem) && objectItem) {
        objectItem.forEach(childItem => {
          Object.keys(childItem).map(value => {
            if (Array.isArray(childItem[value]) && childItem[value]) {
              this.setTreeChildren(node, childItem[value]);
            } else {
              node.data[value] = childItem[value];
            }
          });
        });
        return node;
      } else {
        Object.keys(objectItem).map(value => {
          if (
            Array.isArray(objectItem[value]) &&
            objectItem[value] &&
            value !== 'nestedGridParam'
          ) {
            this.setTreeChildren(node, objectItem[value]);
          } else {
            if (
              Object.prototype.toString.call(objectItem[value]) ===
              '[object Date]'
            ) {
              // more robust than instanceof. Tested in safari, FF and chrome.
              node.data[value] = objectItem[value];
            } else if (
              !(
                objectItem[value] &&
                typeof objectItem[value] === 'object' &&
                Object.keys(objectItem[value]).length === 0
              )
            )
              node.data[value] = objectItem[value];
          }
        });
        return node;
      }
    } catch (e) {}
  }
}

export class StyleState implements Serializable<StyleState, string> {
  cssClass: string;

  deserialize(inJson) {
    let obj = this;
    obj = Converter.convert(inJson, obj);
    return obj;
  }
}

/**
 * \@author Tony Lopez
 */
export abstract class TableBasedData {
  collectionParams: CollectionParams = {};
  values: any[] = undefined;
  stateMap: any[] = [];

  constructor(protected configSvc: ConfigService) {}

  /**
   * Factory method for building TableBasedData from a given param.
   * @param configSvc the config service to gather context information from
   * @param param the table based param, provides context information and/or the "source" of data to transform
   * @param colElemParamsJSON the "source" of data to transform
   */
  static fromParam(
    configSvc: ConfigService,
    param: Param
  ): TableBasedData {
    switch (param.config.uiStyles.attributes.alias) {
      case ViewComponent.grid.toString(): {
        return new TableData(configSvc).from(param);
      }
      case ViewComponent.treeGrid.toString(): {
        return new TreegridData(configSvc).from(param);
      }
      default: {
        throw new Error(
          `Unsupported alias: ${param.config.uiStyles.attributes.alias}`
        );
      }
    }
  }

  /**
   * Factory method for building TableBasedData from a given param and collection element
   * JSON.
   * @param configSvc the config service to gather context information from
   * @param param the table based param, provides context information and/or the "source" of data to transform
   * @param colElemParamsJSON the "source" of data to transform
   */
  static fromJson(
    configSvc: ConfigService,
    param: Param,
    colElemParamsJSON: Param[]
  ): TableBasedData {
    switch (param.config.uiStyles.attributes.alias) {
      case ViewComponent.grid.toString(): {
        return new TableData(configSvc).from(param, true, colElemParamsJSON);
      }
      case ViewComponent.treeGrid.toString(): {
        return new TreegridData(configSvc).from(param, true, colElemParamsJSON);
      }
      default: {
        throw new Error(
          `Unsupported alias: ${param.config.uiStyles.attributes.alias}`
        );
      }
    }
  }

  /**
   * Create table data from the given parameters. The method attempts to find a "source" of data
   * to transform into TableBasedData. If colElemParamsJSON is given then the resulting table data will
   * be built from it, otherwise it will be be built from params contained within baseParam.
   * @param baseParam the table based param, provides context information and/or the "source" of data to transform
   * @param useJson denotes to use json as the table data building strategy
   * @param colElemParamsJSON the "source" of data to transform
   */
  from(baseParam: Param, useJson?: boolean, colElemParamsJSON?: Param[]): TableBasedData {
    if (!baseParam) {
      return this;
    }

    // determine the "source" of data to collect TableBasedData information from
    let colElemParams: Param[] = [];
    if (useJson === true) {
      if (!colElemParamsJSON) {
        return this;
      } else {
        // if JSON is given, deserialize the JSON into useable params
        for (let colElemParamJSON of colElemParamsJSON) {
          colElemParams.push(colElemParamJSON);
        }
      }
    } else {
      if (
        !baseParam.type ||
        !baseParam.type.model ||
        !baseParam.type.model.params
      ) {
        return this;
      } else {
        colElemParams = baseParam.type.model.params;
      }
    }

    // build the TableBasedData object
    this.values = [];
    for (let colElemParam of colElemParams) {
      if (!ParamUtils.isEmpty(colElemParam)) {
        const rowData = this.buildRowData(colElemParam, baseParam);
        this.collectionParams[colElemParam.elemId] = rowData.nestedParams;
        if (rowData.values) {
          this.values = this.values.concat(rowData.values);
        }
        this.stateMap = this.stateMap.concat(rowData.stateMap);
      }
    }
    return this;
  }

  abstract buildRowData(colElemParam: Param, baseParam: Param): RowData;
}

/**
 * \@author Tony Lopez
 */
export abstract class RowData {
  values: any = undefined;
  nestedParams: CollectionParams = {};
  stateMap: any[] = [];

  constructor(protected configSvc: ConfigService) {}

  /**
   * Create row data from the given collection element param.
   * @param colElemParam the collection element param which provides the "source" of data to transform
   */
  from(colElemParam: Param, baseParam: Param): RowData {
    if (!colElemParam) {
      return this;
    }
    this.values = colElemParam.leafState;
    if (
      !colElemParam.type ||
      !colElemParam.type.model ||
      !colElemParam.type.model.params
    ) {
      return this;
    }

    let stateData = {};
    for (let cellParam of colElemParam.type.model.params) {
      // collect all nested params for the collection element row
      this.collectNestedParams(
        colElemParam,
        cellParam,
        this.nestedParams,
        baseParam
      );

      // collect all state data for the collection element row
      let cellParamCode = this.configSvc.getViewConfigById(cellParam.configId)
        .code;
      stateData[cellParamCode] = {
        style: cellParam.style
      };
    }
    if (Object.keys(stateData).length > 0) {
      this.stateMap.push(stateData);
    }

    return this;
  }

  /**
   * Recursively retrieve all nested params within a collection element param (relative to argument "param") and
   * store them into nestedParams.
   * @param colElemParam the collection element param to start from
   * @param param the base param (used recursively)
   * @param nestedParams an array containing the added nested params
   */
  protected collectNestedParams(
    colElemParam: Param,
    param: Param,
    nestedParams: CollectionParams,
    baseParam: Param
  ): void {
    if (!param || !param.config || !this.shouldCollect(param, baseParam)) {
      return;
    }

    this.nestedParams[param.config.code] = param;

    // if nested, recursively perform the collection
    if (param.type && param.type.model && param.type.model.params) {
      for (let p of param.type.model.params) {
        this.collectNestedParams(colElemParam, p, nestedParams, baseParam);
      }
    }
  }

  abstract shouldCollect(param: Param, baseParam: Param): boolean;
}

/**
 * \@author Tony Lopez
 */
export class TableData extends TableBasedData {
  buildRowData(colElemParam: Param, baseParam: Param): RowData {
    return new TableRowData(this.configSvc).from(colElemParam, baseParam);
  }
}

/**
 * \@author Tony Lopez
 */
export class TableRowData extends RowData {
  shouldCollect(param: Param, baseParam: Param): boolean {
    return (
      param.config.uiStyles &&
      (TableComponentConstants.allowedColumnStylesAlias.includes(
        param.config.uiStyles.attributes.alias
      ) ||
        param.config.uiStyles.attributes.showAsLink ||
        baseParam.config.uiStyles.attributes.editRow)
    );
  }
}

/**
 * \@author Tony Lopez
 */
export class TreegridData extends TableBasedData {
  buildRowData(colElemParam: Param): RowData {
    return new TreegridRowData(this.configSvc).from(colElemParam, undefined);
  }
}

/**
 * \@author Tony Lopez
 */
export class TreegridRowData extends RowData {
  static readonly allowedNestedAliases = [
    ViewComponent.button.toString(),
    ViewComponent.link.toString(),
    ViewComponent.linkMenu.toString(),
    ViewComponent.treeGridChild.toString()
  ];

  shouldCollect(param: Param, baseParam: Param): boolean {
    if (!param.config) {
      // TODO investigate why param config is not loaded.
      // until this is available, this is a workaround to include @TreeGridChild nested components
      return param.collectionElem;
    }
    return (
      param.config &&
      param.config.uiStyles &&
      (TreegridRowData.allowedNestedAliases.includes(
        param.config.uiStyles.attributes.alias
      ) ||
        param.config.uiStyles.attributes.showAsLink)
    );
  }
}
