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

import { ConfigService } from './../services/config.service';
import { ParamUtils } from './param-utils';
import { Converter } from './object.conversion';
import { Serializable } from './serializable';
import { ParamConfig, LabelConfig } from './param-config';
import { Message } from './message';
import { ViewComponent } from './param-annotations.enum';
import { TableComponentConstants } from '../components/platform/grid/table.component.constants';

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
    previousLeafState: any;
    path: string;
    collection: boolean;
    collectionElem: boolean;
    enabled: boolean = true;
    message : Message[];
    paramState: Param[];
    values : Values[];
    visible: boolean = true;
    activeValidationGroups: String[] = [];
    page: GridPage;
    _alias: string;
    _config: ParamConfig;
    labels: LabelConfig[];
    elemLabels: Map<string, LabelConfig[]>;
    gridData: GridData;
    style: StyleState;

    // Move to Grid Data
    elemId: string;
    collectionParams: Param[] = [];

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

    private createRowData(param: Param) {
        let rowData: any = {};
        rowData = param.leafState;
        if(param.type.model) {
            let isTreeGrid = this.config != null && this.config.uiStyles && this.config.uiStyles.attributes.alias == ViewComponent.treeGrid.toString();
            rowData['nestedGridParam'] = [];
            for(let p of param.type.model.params) {
                if(p != null) {
                    let config = this.configSvc.paramConfigs[p.configId];

                    this.handleNestedGridParams(rowData, param, p, config);
                    if (isTreeGrid && config.uiStyles && config.uiStyles.attributes.alias === ViewComponent.treeGridChild.toString()) {
                        this.putNestedGridParam(rowData, param, p);
                        this.handleRecursiveNestedGridParams(rowData, param, p);
                    }
    
                    // handle dates
                    if (ParamUtils.isKnownDateType(config.type.name)) {
                        rowData[config.code] = ParamUtils.convertServerDateStringToDate(rowData[config.code], config.type.name);
                    }
                }
            }
        }

        return rowData;
    }

    handleRecursiveNestedGridParams(rowData: any, baseParam: Param, param: Param) {
        if (!param.type.model) {
            return;
        }
        for(let collectionElements of param.type.model.params) {
            if (!collectionElements.type.model) {
                continue;
            }
            for(let p of collectionElements.type.model.params) {
                if(p != null) {
                    let config = this.configSvc.paramConfigs[p.configId];
                    this.handleNestedGridParams(rowData, baseParam, p, config);
                    if (config.uiStyles && config.uiStyles.attributes.alias === ViewComponent.treeGridChild.toString()) {
                        this.putNestedGridParam(rowData, baseParam, p);
                        this.handleRecursiveNestedGridParams(rowData, baseParam, p);
                    }
                }
            }
        }
    }

    handleNestedGridParams(rowData: any, param: Param, p: any, config: ParamConfig) {
        if (config.uiStyles && (TableComponentConstants.allowedColumnStylesAlias.includes(config.uiStyles.attributes.alias)
            || config.uiStyles.attributes.showAsLink === true)) {
    
            this.putNestedGridParam(rowData, param, p);
        }
    }

    putNestedGridParam(rowData: any, param: Param, p: Param) {
        let isDeserialized = false;
        if (p instanceof Param) {
            isDeserialized = true;
        }
        if(param.collectionElem) {
            rowData['nestedGridParam'].push(isDeserialized ? p : new Param(this.configSvc).deserialize(p,this.path + '/' + param.elemId));
        } else {
            rowData['nestedGridParam'].push(isDeserialized ? p : new Param(this.configSvc).deserialize(p,this.path));
        }
    }

    private constructPath(path, inJson) {
        let paramPath;
        if(path == inJson.path){
            paramPath = path;
        } else if(path == undefined && this.config) {
            paramPath =  "/" + this.config.code;
        } else if(path && this.config){
            paramPath = path + "/" + this.config.code;
        } else if(path && this.config && inJson.elemId){
            paramPath = path + "/" + inJson.elemId + "/" + this.config.code;
        } else if (path && inJson.elemId) {
            paramPath = path + '/' + inJson.elemId;
        }  
        else {
            paramPath = path;
        }
        
 
        return paramPath;
    }     
    
    deserialize( inJson, path ) {
        this.configId = inJson.configId;
        // Set Config in ParamConfig Map
        if (inJson.config != null) {
            let config: ParamConfig = new ParamConfig(this.configSvc).deserialize(inJson.config);
            this.configSvc.setViewConfigToParamConfigMap(config.id, config);
        }

        if (typeof inJson.collectionElem !== 'undefined'){
            this.collectionElem = inJson.collectionElem;
        }  
        
        if (typeof inJson.collection !== 'undefined'){
            this.collection = inJson.collection;
        }
        if ( inJson.collectionElem) {
            this.elemId = inJson.elemId;
        }

        this.path = this.constructPath(path, inJson);
        if (inJson.type != null) {
            this.type = new Type(this.configSvc).deserialize( inJson.type, this.path );
        } else {
            this.type =  new Type (this.configSvc);
            this.type['nested'] = false;
            this.type['name'] = 'string';
            this.type['collection'] = false;
        } 
       if (this.config != null && this.config.uiStyles && 
            (this.config.uiStyles.attributes.alias === ViewComponent.grid.toString() || this.config.uiStyles.attributes.alias == ViewComponent.treeGrid.toString())) {
            // deserialize Page
            if (inJson.page) {
                this.page = new GridPage().deserialize(inJson.page);
            }
            this.gridData = new GridData();
            if (inJson.type && inJson.type.model && inJson.type.model.params) {
                this.gridData.leafState = [];
                this.gridData.stateMap = [];
                //this.paramState = [];
                if(this.path && typeof inJson.path == undefined)
                    inJson.path = this.path;
                for ( var p in inJson.type.model.params ) {
                    if (!ParamUtils.isEmpty(inJson.type.model.params[p])) {
                        //this.paramState.push(inJson.type.model.params[p].type.model.params); 
                        //this.gridList.push(this.createRowData(inJson.type.model.params[p])); 
                        let colElemParam = new Param(this.configSvc).deserialize(inJson.type.model.params[p], this.path)
                        
                        // TODO Refactor the following transformations for efficiency by 
                        // combining this.createRowData and gridData.stateMap implementation
                        // into single loop.
                        
                        // Update the gridData.leafState
                        let lineItem = this.createRowData(colElemParam);
                        if(lineItem.nestedGridParam)
                          this.collectionParams = this.collectionParams.concat(lineItem.nestedGridParam); 
                        delete lineItem.nestedGridParam; 
                        this.gridData.leafState.push(lineItem);

                        // Update the gridData.stateMap
                        let rowStateData = {};
                        for(var cellParam of colElemParam.type.model.params) {
                            let cellParamCode = this.configSvc.getViewConfigById(cellParam.configId).code;
                            rowStateData[cellParamCode] = {
                                style: cellParam.style
                            };
                        }
                        this.gridData.stateMap.push(rowStateData);
                    }
                }
            }
        } else if (this.config && this.config.type && ParamUtils.isKnownDateType(this.config.type.name)) {
            this.leafState = ParamUtils.convertServerDateStringToDate(inJson.leafState, this.config.type.name);
        } else {
            this.leafState = inJson.leafState;
            this.previousLeafState = inJson.leafState;
            // Handle any transformations that need to be applied to the leaf state
            if (typeof inJson.leafState === 'object' && (this.type.model && this.type.model.params)) {

                this.leafState = ParamUtils.applyLeafStateTransformations(inJson.leafState, this);
            }
        }
        if ( inJson.style ) {
            this.style = new StyleState().deserialize(inJson.style);
        }
        if ( inJson.collectionElem && this.leafState) {
            this.leafState = this.createRowData(this);
        }
        if (typeof inJson.visible !== 'undefined') {
            this.visible = inJson.visible;
        }
        if (inJson.enabled != null) {
            this.enabled = inJson.enabled;
        }
        this.message = [];
        if ( inJson.message != null && inJson.message.length > 0) {
            for ( const msg in inJson.message ) {
                if (inJson.message[msg]) {
                    this.message.push( new Message().deserialize( inJson.message[msg] ) );
                }
            }
        }
        this.values = [];
        if ( inJson.values != null && inJson.values.length > 0 ) {
            for ( var value in inJson.values ) {
                this.values.push( new Values().deserialize( inJson.values[value] ) );
            }
        }
       
        if (typeof inJson.activeValidationGroups === 'object') {
            this.activeValidationGroups = inJson.activeValidationGroups;
        }

        this.labels = [];
        if ( inJson.labels != null && inJson.labels.length > 0) { 
            for ( var p in inJson.labels ) {
                this.labels.push( new LabelConfig().deserialize(inJson.labels[p]) );
            }
        }

        this.elemLabels = new Map<string, LabelConfig[]>();
        if (inJson.elemLabels) {
            const configIds: string[] = Object.keys(inJson.elemLabels);
            configIds.forEach(configId => {
                const labels = [];
                if (inJson.elemLabels[configId] != null && inJson.elemLabels[configId].length > 0) {
                    for ( var p in inJson.elemLabels[configId]) {
                        labels.push( new LabelConfig().deserialize(inJson.elemLabels[configId][p]))
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

    deserialize( inJson ) {
       var obj = this;
       obj = Converter.convert(inJson,obj);
       return obj;
    }
}

export class Model implements Serializable<Model, string> {
    params: Param[];

    constructor(private configSvc: ConfigService) {}

    deserialize( inJson, path ) {
        this.params = [];
        for ( var p in inJson.params ) {
            if(!ParamUtils.isEmpty(inJson.params[p])) {
                //param when null means that there is an @Ignore(event = websocket) on the parameter
                this.params.push( new Param(this.configSvc).deserialize( inJson.params[p], path ) );
            }
        }
        return this;
    }
}

export class Type implements Serializable<Type, string> {
    model: Model;

    constructor(private configSvc: ConfigService) {}

    deserialize( inJson, path ) {        
 
            if (inJson.model  != null) {
                this.model = new Model(this.configSvc).deserialize( inJson.model, path );
            } else if (inJson.modelConfig != null) {
                this.model = new Model(this.configSvc).deserialize( inJson.modelConfig, path);
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

    setTreeChildren(node, nestedArray){
        node.children = [];
        nestedArray.forEach((nestedArrayItem) =>
            node.children.push(this.deserialize(nestedArrayItem, nestedArrayItem))
        );

    }


    deserialize(objectItem, data?, children?) {

        try {

            let node: any = {};
            node.data = {};

            if (Array.isArray(objectItem) && objectItem) {
                objectItem.forEach((childItem) => {
                    Object.keys(childItem).map(value => {
                        if (Array.isArray(childItem[value]) && childItem[value]) {
                            this.setTreeChildren(node, childItem[value]);

                        }
                        else {
                            node.data[value] = childItem[value];
                        }
                    });

                });
                return node;
            }

            else {
              
                Object.keys(objectItem).map(value => {
                    if (Array.isArray(objectItem[value]) && objectItem[value] && value !== 'nestedGridParam') {
                        this.setTreeChildren(node, objectItem[value]);
                    }
                    else {
                        if(Object.prototype.toString.call(objectItem[value]) === "[object Date]"){    // more robust than instanceof. Tested in safari, FF and chrome.
                            node.data[value] = objectItem[value];
                        }
                        else if(!((objectItem[value] && typeof objectItem[value] === 'object') && Object.keys(objectItem[value]).length === 0))
                        node.data[value] = objectItem[value];
                    }
                });
                return node;
            }           

        }
        catch (e) {
            //  console.log("Error in tree deserializer", e);

        }
    }

}

export class StyleState implements Serializable<StyleState, string> {

    cssClass: string;

    deserialize( inJson ) {
        let obj = this;
        obj = Converter.convert(inJson, obj);
        return obj;
    }
}

export class GridData {

    elemId: string;
    collectionParams: Param[] = [];
    leafState: any[];
    stateMap: any[];
}
