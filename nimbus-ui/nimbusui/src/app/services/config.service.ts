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

import { Injectable } from '@angular/core';
import { ViewRoot } from '../shared/app-config.interface';
import { ParamConfig } from '../shared/param-config';
import { Model } from '../shared/param-state';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Injectable()
export class ConfigService {
  flowConfigs: Object;
  paramConfigs: Object;

  constructor() {
    // initialize
    this.flowConfigs = {};
    this.paramConfigs = {};
  }

  /** Add the flow config to the lookup map */
  setLayoutToAppConfig(flowName: string, viewRoot: ViewRoot) {
    this.flowConfigs[flowName] = viewRoot;
  }

  /** Add the flow config to the lookup map */
  setLayoutToAppConfigByModel(flowName: string, model: Model) {
    let viewRoot: ViewRoot = new ViewRoot();
    viewRoot.model = model;
    this.flowConfigs[flowName] = viewRoot;
  }

  /** Get the flow config if it is already loaded */
  getFlowConfig(flowName: string): ViewRoot {
    if (this.flowConfigs[flowName]) {
      return this.flowConfigs[flowName];
    } else {
      return undefined;
    }
  }

  /** Add the view config to the lookup map */
  setViewConfigToParamConfigMap(configId: string, paramConfig: ParamConfig) {
    let viewConfig: ViewConfig = new ViewConfig();
    this.paramConfigs[configId] = paramConfig;
  }

  /** Get the view config by id */
  getViewConfigById(configId: string) {
    return this.paramConfigs[configId];
  }
}

export class ViewConfig {
  configId: string;
  config: ParamConfig;
}
