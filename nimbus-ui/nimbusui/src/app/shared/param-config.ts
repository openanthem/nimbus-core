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

export class ParamConfig implements Serializable<ParamConfig,string> {
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

    deserialize( inJson ) {
        var obj = this;
        obj = Converter.convert(inJson, obj);
        if ( inJson.uiStyles != null ) {
            obj['uiStyles'] = new UiStyle().deserialize( inJson.uiStyles );
        }
        if(inJson.type != null) {
            obj['type'] = new ConfigType(this.configSvc).deserialize( inJson.type );
        } 
        if ( inJson.validations != null ) {
            obj['validation'] = new Validation().deserialize( inJson.validations );
        }
        let uiNatures = [];
        if ( inJson.uiNatures != null && inJson.uiNatures.length > 0 ) {
            for ( var uiNature in inJson.uiNatures ) {
                uiNatures.push( new UiNature().deserialize( inJson.uiNatures[uiNature] ) );
            }
        }
        obj['uiNatures'] = uiNatures;        
        return obj;
    }

    initializeComponent() : boolean {
        for (var p in this.uiNatures ) {
            if (this.uiNatures[p].attributes.alias === 'initialize') {
                return true;
            }
        }
        return false;
    }
}

export class UiNature implements Serializable<UiNature,string> {
    name: string;
    value: string;
    attributes: UiAttribute;

    deserialize( inJson ) {
        let obj = this;
        obj = Converter.convert(inJson, obj);
        obj['attributes'] = new UiAttribute().deserialize( inJson.attributes );

        return obj;
    }
}

export class UiStyle implements Serializable<UiStyle,string> {
    name: string;
    value: string;
    isLink: boolean = false;
    isHidden: boolean = false;
    attributes: UiAttribute;

    deserialize( inJson ) {
        this.name = inJson.name;
        this.value = inJson.value;
        this.attributes = new UiAttribute().deserialize( inJson.attributes );
        if ( this.name === ViewConfig.link.toString() || this.name === ViewConfig.linkmenu.toString()
            || this.attributes.showAsLink === true ) {
            this.isLink = true;
        }
        if ( this.name === ViewConfig.hidden.toString() ) {
            this.isHidden = true;
        }
        return this;
    }
}

export class UiAttribute implements Serializable<UiAttribute,string> {
    value: any;
    url: string;
    asynchronous: boolean;
    controlType: string;
    editUrl: string;
    editable: boolean;
    align: string;
    alias: string;
    control: string;
    onLoad: boolean;
    expandableRows: boolean;
    b: string;
    method: string;
    imgSrc: string;
    imgType: string;
    hidden: boolean = false;
    readOnly: boolean = false;
    level: string;
    cssClass: string;
    multiple: boolean;
    showExpandAll: boolean;
    selected: boolean;
    activeIndex: string;
    submitButton: boolean = true;
    content: string; //-- TO BE DELETED (always pull with contentId)
    contentId: string;
    datePattern: string;
    labelClass: string;
    showTime: boolean;
    timeOnly: boolean;
    hourFormat: string;
    header: string;
    help: string;
    title: string;
    closable: boolean;
    width: string;
    height: string;
    type: string;
    style: string;
    layout: string;
    route: string;
    size: string;
    submitUrl: string;
    rows: string;
    cols: string;
    showName: boolean = true;
    iconField: string;
    navLink: string; //TODO temp fix for launching a new flow from current flow on form submit
    controlId: string;
    postEventOnChange: boolean;
    draggable: boolean;
    rowSelection: boolean;
    showHeader: boolean;
    pagination: boolean;
    pageSize: number = 25; //server side has a default but defaulting here so that coverter can cast to number
    postButton: boolean;
    postButtonUrl: string;
    postButtonUri: string;
    postButtonTargetPath: string;
    postButtonAlias : string;
    postButtonLabel: string;
    filter: boolean;
    filterMode : string;
    filterValue : string;
    modelPath: string;
    inplaceEdit: boolean;
    inplaceEditType: string;
    //below 2 attributes are for OrderablePickList
    sourceHeader: string;
    targetHeader: string;
    payload: string;
    defaultPage: boolean;
    defaultFlow: string;
    formReset: boolean;
    browserBack: boolean=false;
    target: string;
    rel: string;
    sortAs: string;
    sortable: boolean;
    lazyLoad: boolean;
    resizable:boolean;
    placeholder: string;
    clearAllFilters: boolean;
    export: boolean;
    clearLabel: string;
    acceptLabel: string;
    rowExpander: boolean;
    readonlyInput: boolean;
	  monthNavigator: boolean;
	  yearNavigator: boolean;
    yearRange: string;
    metaData: any;
    captureType: string;
    showAsLink: boolean = false;
    info: string;
    page: string;
    applyValueStyles: boolean;
    expandable: boolean;
    border: boolean;
    showSourceControls: boolean;
    showTargetControls: boolean;
    scriptName: string;
    orientation: string;
    showMessages: boolean;
    stylesheet: string;
    delay: number;
    useDelay: boolean;
    closeAfterPrint: boolean;
    printPath: string;
    autoPrint: boolean;
    dataEntryField: boolean;
    mask: string;
    slotChar: string;
    charRegex: string;
    maskPlaceHolder: string;
    fixLayout: boolean;
    headerCheckboxToggleAllPages: boolean;
    xAxisLabel: string;
    yAxisLabel: string;
    stepSize: string;
    inlineStyle: string;
    formats: string;
    toolbarFeatures: string[];

    deserialize( inJson ) {
        let obj = this;
        obj = Converter.convert(inJson, obj, { includeArrays: true });
        if(inJson['metaData'] || inJson['metaData'] === ""){
            obj['metaData'] = inJson['metaData'] !== "" ? inJson['metaData'].split(",") : [];
        }
        return obj;
    }
}

export class ModelConfig implements Serializable<ModelConfig,string> {
    paramConfigIds: string[];
    uiStyles: UiStyle;
    id: string;
    _paramConfigs: ParamConfig[];
    
    constructor(private configSvc: ConfigService) {}

    public get paramConfigs(): ParamConfig[] {
        let paramConfigs: ParamConfig[] = [];
        for ( var p in this.paramConfigIds ) {
            paramConfigs.push(this.configSvc.getViewConfigById(this.paramConfigIds[p]));
        }
        return paramConfigs;
    }
    
    deserialize( inJson ) {
        if(inJson.uiStyles != null) {
            this.uiStyles = new UiStyle().deserialize( inJson.uiStyles );
        }
        this.paramConfigIds = [];
        for ( var p in inJson.paramConfigs ) {
            let config: ParamConfig = new ParamConfig(this.configSvc).deserialize(inJson.paramConfigs[p]);
            this.paramConfigIds.push(config.id);
            this.configSvc.setViewConfigToParamConfigMap(config.id, config);
        }
        return this;
    }
}

export class ConfigType implements Serializable<ConfigType,string> {
   
    name: string;
    model: ModelConfig;
    collection: boolean = false;
    elementConfig: ElementConfig;
    nested: boolean = false;

    constructor(private configSvc: ConfigService) {}
    
    deserialize( inJson ) {
    
        let obj = this;
        obj = Converter.convert(inJson,obj);
        
        if (inJson.modelConfig != null) {
            obj['model'] = new ModelConfig(this.configSvc).deserialize( inJson.modelConfig );
        }
       
        if ( inJson.elementConfig != null) {
           obj['elementConfig'] = new ElementConfig(this.configSvc).deserialize( inJson.elementConfig );
        }

        return obj;
    }
}

export class LabelConfig implements Serializable<LabelConfig,string> {
    text: string;
    locale: string;
    helpText : string;
    cssClass: string;

    deserialize( inJson ) {
        let obj = this;
        obj = Converter.convert(inJson,obj);
        return obj;
    }
}

export class Validation implements Serializable<Validation,string> {
    constraints: Constraint[];

    deserialize( inJson ) {
        this.constraints = [];
        for ( var p in inJson ) {
            this.constraints.push( new Constraint().deserialize( inJson[p] ) );
        }
        return this;
    }
}

export class Constraint implements Serializable<Constraint,string> {
    name: string;
    value: string;
    attribute: Attribute;

    deserialize( inJson ) {
        this.name = inJson.name;
        this.value = inJson.value;
        this.attribute = new Attribute().deserialize( inJson.attributes );
        return this;
    }
}

export class ElementConfig implements Serializable<ElementConfig, string> {
    id: string;
    type: ConfigType;

    constructor(private configSvc: ConfigService) {}

    deserialize( inJson ) {
        this.id = inJson.id;
        if ( inJson.type ) {
            this.type = new ConfigType(this.configSvc).deserialize( inJson.type );
        }

        return this;
    }
}

export class Attribute implements Serializable<Attribute,string> {
    message: string;
    pattern: string;
    regexp: string;
    min: any;
    max: any;
    groups: String[];
    value: any;

    deserialize( inJson ) {
        let obj = this;
        obj = Converter.convert(inJson,obj);
        obj['groups'] = inJson.groups;
        return obj;
    }
}

