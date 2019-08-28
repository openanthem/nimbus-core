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

import { ConfigService } from './../services/config.service';
import { Converter } from './object.conversion';
import { Serializable } from './serializable';
import { ViewConfig } from './param-annotations.enum';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */

export class ParamConfig implements Serializable<ParamConfig, string> {
  uiStyles: UiStyle;
  id: string;
  code: string;
  type: ConfigType;
  validation: Validation;
  uiNatures: UiNature[];
  label: string;
  url: string;
  active: boolean = false;
  required: boolean = false;

  constructor(private configSvc: ConfigService) {}

  deserialize(inJson) {
    var obj = this;
    obj = Converter.convert(inJson, obj);
    if (inJson.uiStyles != null) {
      obj['uiStyles'] = new UiStyle().deserialize(inJson.uiStyles);
    }
    if (inJson.type != null) {
      obj['type'] = new ConfigType(this.configSvc).deserialize(inJson.type);
    }
    if (inJson.validations != null) {
      obj['validation'] = new Validation().deserialize(inJson.validations);
    }
    let uiNatures = [];
    if (inJson.uiNatures != null && inJson.uiNatures.length > 0) {
      for (var uiNature in inJson.uiNatures) {
        uiNatures.push(new UiNature().deserialize(inJson.uiNatures[uiNature]));
      }
    }
    obj['uiNatures'] = uiNatures;
    return obj;
  }

  initializeComponent(): boolean {
    for (var p in this.uiNatures) {
      if (this.uiNatures[p].attributes.alias === 'initialize') {
        return true;
      }
    }
    return false;
  }
}

export class UiNature implements Serializable<UiNature, string> {
  name: string;
  value: string;
  attributes: UiAttribute;

  deserialize(inJson) {
    let obj = this;
    obj = Converter.convert(inJson, obj);
    obj['attributes'] = new UiAttribute().deserialize(inJson.attributes);

    return obj;
  }
}

export class UiStyle implements Serializable<UiStyle, string> {
  name: string;
  value: string;
  isLink: boolean = false;
  isHidden: boolean = false;
  attributes: UiAttribute;

  deserialize(inJson) {
    this.name = inJson.name;
    this.value = inJson.value;
    this.attributes = new UiAttribute().deserialize(inJson.attributes);
    if (
      this.name === ViewConfig.link.toString() ||
      this.name === ViewConfig.linkmenu.toString() ||
      this.attributes.showAsLink === true
    ) {
      this.isLink = true;
    }
    if (this.name === ViewConfig.hidden.toString()) {
      this.isHidden = true;
    }
    return this;
  }
}

export class UiAttribute implements Serializable<UiAttribute, string> {
  acceptLabel: string;
  activeIndex: string;
  addRow: boolean;
  alias: string;
  align: string;
  applyValueStyles: boolean;
  asynchronous: boolean;
  autoPrint: boolean;
  b: string;
  border: boolean;
  breadcrumbLabel: string;
  browserBack: boolean = false;
  captureType: string;
  charRegex: string;
  clearAllFilters: boolean;
  clearLabel: string;
  closable: boolean;
  closeAfterPrint: boolean;
  cols: string;
  content: string; //-- TO BE DELETED (always pull with contentId)
  contentId: string;
  control: string;
  controlId: string;
  controlType: string;
  cssClass: string;
  dataEntryField: boolean;
  datePattern: string;
  defaultFlow: string;
  defaultLabel: string;
  defaultPage: boolean;
  delay: number;
  display: string;
  draggable: boolean;
  editable: boolean;
  editRow: boolean;
  editUrl: string;
  escape: boolean;
  expandable: boolean;
  expandableRows: boolean;
  export: boolean;
  filter: boolean;
  filterMode: string;
  filterValue: string;
  fixLayout: boolean;
  flow: string;
  formats: string;
  formReset: boolean;
  header: string;
  headerCheckboxToggleAllPages: boolean;
  height: string;
  help: string;
  hidden: boolean = false;
  hourFormat: string;
  iconField: string;
  imgSrc: string;
  imgType: string;
  info: string;
  inlineStyle: string;
  inplaceEdit: boolean;
  inplaceEditType: string;
  labelClass: string;
  layout: string;
  lazyLoad: boolean;
  level: string;
  mask: string;
  maskPlaceHolder: string;
  metaData: any;
  method: string;
  minLength: number;
  modelPath: string;
  monthNavigator: boolean;
  multiple: boolean;
  navLink: string; //TODO temp fix for launching a new flow from current flow on form submit
  onAdd: string;
  onEdit: string;
  onLoad: boolean;
  orientation: string;
  page: string;
  pageSize: number = 25; //server side has a default but defaulting here so that coverter can cast to number
  pagination: boolean;
  payload: string;
  placeholder: string;
  postButton: boolean;
  postButtonAlias: string;
  postButtonLabel: string;
  postButtonTargetPath: string;
  postButtonUri: string;
  postButtonUrl: string;
  postEventOnChange: boolean;
  printPath: string;
  readOnly: boolean = false;
  readonlyInput: boolean;
  rel: string;
  resizable: boolean;
  route: string;
  rowExpander: boolean;
  rows: string;
  rowSelection: boolean;
  scriptName: string;
  selected: boolean;
  showAsLink: boolean = false;
  showExpandAll: boolean;
  showHeader: boolean;
  showMessages: boolean;
  showName: boolean = true;
  showSourceControls: boolean;
  showTargetControls: boolean;
  showTime: boolean;
  size: string;
  slotChar: string;
  sortable: boolean;
  sortAs: string;
  sourceHeader: string;
  stepSize: string;
  style: string;
  stylesheet: string;
  submitButton: boolean = true;
  submitUrl: string;
  target: string;
  targetHeader: string;
  timeOnly: boolean;
  title: string;
  toolbarFeatures: string[];
  toolTipPosition: string;
  tooltipStyleClass: string;
  toolTipText: string;
  type: string;
  url: string;
  useDelay: boolean;
  value: any;
  width: string;
  xAxisLabel: string;
  yAxisLabel: string;
  yearNavigator: boolean;
  yearRange: string;
  loaderIcon: string;
  autofill: boolean;

  deserialize(inJson) {
    let obj = this;
    obj = Converter.convert(inJson, obj, { includeArrays: true });
    if (inJson['metaData'] || inJson['metaData'] === '') {
      obj['metaData'] =
        inJson['metaData'] !== '' ? inJson['metaData'].split(',') : [];
    }
    return obj;
  }
}

export class ModelConfig implements Serializable<ModelConfig, string> {
  paramConfigIds: string[];
  uiStyles: UiStyle;
  id: string;
  _paramConfigs: ParamConfig[];

  constructor(private configSvc: ConfigService) {}

  public get paramConfigs(): ParamConfig[] {
    let paramConfigs: ParamConfig[] = [];
    for (var p in this.paramConfigIds) {
      paramConfigs.push(
        this.configSvc.getViewConfigById(this.paramConfigIds[p])
      );
    }
    return paramConfigs;
  }

  deserialize(inJson) {
    if (inJson.uiStyles != null) {
      this.uiStyles = new UiStyle().deserialize(inJson.uiStyles);
    }
    this.paramConfigIds = [];
    for (var p in inJson.paramConfigs) {
      let config: ParamConfig = new ParamConfig(this.configSvc).deserialize(
        inJson.paramConfigs[p]
      );
      this.paramConfigIds.push(config.id);
      this.configSvc.setViewConfigToParamConfigMap(config.id, config);
    }
    return this;
  }
}

export class ConfigType implements Serializable<ConfigType, string> {
  name: string;
  model: ModelConfig;
  collection: boolean = false;
  elementConfig: ElementConfig;
  nested: boolean = false;

  constructor(private configSvc: ConfigService) {}

  deserialize(inJson) {
    let obj = this;
    obj = Converter.convert(inJson, obj);

    if (inJson.modelConfig != null) {
      obj['model'] = new ModelConfig(this.configSvc).deserialize(
        inJson.modelConfig
      );
    }

    if (inJson.elementConfig != null) {
      obj['elementConfig'] = new ElementConfig(this.configSvc).deserialize(
        inJson.elementConfig
      );
    }

    return obj;
  }
}

export class LabelConfig implements Serializable<LabelConfig, string> {
  text: string;
  locale: string;
  helpText: string;
  cssClass: string;

  deserialize(inJson) {
    let obj = this;
    obj = Converter.convert(inJson, obj);
    return obj;
  }
}

export class Validation implements Serializable<Validation, string> {
  constraints: Constraint[];

  deserialize(inJson) {
    this.constraints = [];
    for (var p in inJson) {
      this.constraints.push(new Constraint().deserialize(inJson[p]));
    }
    return this;
  }
}

export class Constraint implements Serializable<Constraint, string> {
  name: string;
  value: string;
  attribute: Attribute;

  deserialize(inJson) {
    this.name = inJson.name;
    this.value = inJson.value;
    this.attribute = new Attribute().deserialize(inJson.attributes);
    return this;
  }
}

export class ElementConfig implements Serializable<ElementConfig, string> {
  id: string;
  type: ConfigType;

  constructor(private configSvc: ConfigService) {}

  deserialize(inJson) {
    this.id = inJson.id;
    if (inJson.type) {
      this.type = new ConfigType(this.configSvc).deserialize(inJson.type);
    }

    return this;
  }
}

export class Attribute implements Serializable<Attribute, string> {
  message: string;
  pattern: string;
  regexp: string;
  min: any;
  max: any;
  groups: String[];
  value: any;
  ruleset: RuleSet[];

  deserialize(inJson) {
    let obj = this;
    obj = Converter.convert(inJson, obj);
    obj['groups'] = inJson.groups;
    obj['ruleset'] = [];
    for (var p in inJson.ruleset) {
      obj['ruleset'].push(new RuleSet().deserialize(inJson.ruleset[p]));
    }

    return obj;
  }
}

export class RuleSet implements Serializable<RuleSet, string> {
  message: string;
  rule: string;

  deserialize(inJson) {
    let obj = this;
    obj = Converter.convert(inJson, obj);
    return obj;
  }
}