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
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
import { ConfigService } from './../services/config.service';
import { SortAs } from "../components/platform/grid/sortas.interface";
import { PageService } from '../services/page.service';
import { GridService } from '../services/grid.service';
import { ParamUtils } from './param-utils';
import { Converter } from './object.conversion';
import { Serializable } from './serializable';
import { ParamConfig } from './param-config';
import { Message } from './message';
import { CardDetailsGrid } from './card-details';
import { ViewConfig } from './param-annotations.enum';

export class Param implements Serializable<Param, string> {
    configId: string;
    type: Type;
    leafState: any;
    path: string;
    collection: boolean;
    collectionElem: boolean;
    elemId: string;
    enabled: boolean = true;
    gridList: any[];
    message : Message;
    paramState: Param[];
    values : Values[];
    visible: boolean = true;
    activeValidationGroups: String[] = [];
    collectionParams: Param[] = [];
    page: GridPage;
    _alias: string;
    _config: ParamConfig;

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
        if(rowData instanceof Object)
            rowData['elemId'] = param.elemId;
        if(param.type.model) {
            rowData['nestedGridParam'] = [];
            for(let p of param.type.model.params) {
                if(p != null) {
                    let config = this.configSvc.paramConfigs[p.configId];
                    //let path = paramPath + "/" + config.code;
                    // handle nested grid data
                    if (config.uiStyles && (config.uiStyles.name == ViewConfig.gridrowbody.toString()|| config.uiStyles.name == ViewConfig.linkmenu.toString())) {
                        let isDeserialized = false;
                        if(p instanceof Param){
                            isDeserialized = true;
                        }
                        if(param.collectionElem)
                            rowData['nestedGridParam'].push(isDeserialized ? p : new Param(this.configSvc).deserialize(p,this.path + '/' + param.elemId));
                        else
                            rowData['nestedGridParam'].push(isDeserialized ? p : new Param(this.configSvc).deserialize(p,this.path));
                    }
    
                    // handle dates
                    if (config && ParamUtils.isKnownDateType(config.type.name)) {
                        rowData[config.code] = ParamUtils.convertServerDateStringToDate(rowData[config.code], config.type.name);
                    }
                }
            }
        }

        return rowData;
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

        this.path = this.constructPath(path, inJson);
        if (inJson.type != null) {
            this.type = new Type(this.configSvc).deserialize( inJson.type, this.path );
        } else {
            this.type =  new Type (this.configSvc);
            this.type['nested'] = false;
            this.type['name'] = 'string';
            this.type['collection'] = false;
        } 
       // this.path = inJson.path;
        if ( this.config != null && this.config.uiStyles && this.config.uiStyles.attributes.alias === 'CardDetailsGrid' ) {
            if(inJson.leafState != null) {
                this.leafState = new CardDetailsGrid().deserialize( inJson.leafState );
            }
        } else if (this.config != null && this.config.uiStyles && this.config.uiStyles.attributes.alias === 'Grid') {
            // deserialize Page
            if (inJson.page) {
                this.page = new GridPage().deserialize(inJson.page);
            }
            if (inJson.type && inJson.type.model && inJson.type.model.params) {
                this.gridList = [];
                //this.paramState = [];
                if(this.path && typeof inJson.path == undefined)
                    inJson.path = this.path;
                for ( var p in inJson.type.model.params ) {
                    if (!ParamUtils.isEmpty(inJson.type.model.params[p])) {
                        //this.paramState.push(inJson.type.model.params[p].type.model.params); 
                        //this.gridList.push(this.createRowData(inJson.type.model.params[p])); 
                        let lineItem = this.createRowData(inJson.type.model.params[p]);
                        if(lineItem.nestedGridParam)
                          this.collectionParams = this.collectionParams.concat(lineItem.nestedGridParam); 
                        delete lineItem.nestedGridParam; 
                        this.gridList.push(lineItem);  
                    }
                }
            }
        } else if (this.config && this.config.type && ParamUtils.isKnownDateType(this.config.type.name)) {
            this.leafState = ParamUtils.convertServerDateStringToDate(inJson.leafState, this.config.type.name);
        } else {
            this.leafState = inJson.leafState;

            // Handle any transformations that need to be applied to the leaf state
            if (typeof inJson.leafState === 'object' && (this.type.model && this.type.model.params)) {

                this.leafState = ParamUtils.applyLeafStateTransformations(inJson.leafState, this);
            }
        }

        if ( inJson.collectionElem  && this.leafState) {
            this.elemId = inJson.elemId;
            this.leafState = this.createRowData(this);
        }
        if (typeof inJson.visible !== 'undefined') {
            this.visible = inJson.visible;
        }
        if (inJson.enabled != null) {
            this.enabled = inJson.enabled;
        }
        if ( inJson.message != null ) {
            this.message = new Message().deserialize( inJson.message );
        }
        this.values = [];
        if ( inJson.values != null && inJson.values.length > 0 ) {
            for ( var value in inJson.values ) {
                this.values.push( new Values().deserialize( inJson.values[value] ) );
            }
        }
       
        if (typeof inJson.activeValidationGroups ! == 'undefined') {
            this.activeValidationGroups = inJson.activeValidationGroups;
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

    deserialize( inJson) {   
        var obj = this;
        obj = Converter.convert(inJson, obj);
        return obj;
    }
}
