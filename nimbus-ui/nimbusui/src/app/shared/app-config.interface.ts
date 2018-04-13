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
import { SortAs } from "../components/platform/grid/sortas.interface";
import { PageService } from '../services/page.service';
import { GridService } from '../services/grid.service';
import { ParamUtils } from './param-utils';
import { Converter } from './object.conversion';
import { Param } from './Param';
/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
export interface Serializable<T, R> {
    deserialize( inJson: Object, path?: string ): T;
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

export class Type implements Serializable<Type,string> {
   
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

export class Attribute implements Serializable<Attribute,string> {
    message: string;
    pattern: string;
    regexp: string;
    min: any;
    max: any;
    groups: String[];

    deserialize( inJson ) {
        let obj = this;
        obj = Converter.convert(inJson,obj);
        obj['groups'] = inJson.groups;
        return obj;
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

export class Page implements Serializable<Page, string> {
    pageConfig: Param;
    flow: string;

    deserialize( inJson ) {
        return this;
    }
}

export class Message implements Serializable<Message, string> {
    type: string;
    text: string;
    context: string;
    
    deserialize( inJson ) {
        let obj = this;
        obj = Converter.convert(inJson,obj);
        return obj;
    }
}

export class Input implements Serializable<Input,string> {
    model: Model;

    constructor(private configSvc: ConfigService) {}

    deserialize( inJson, path ) {
        this.model = new Model(this.configSvc).deserialize( inJson.model , path);
        return this;
    }
}

export class Result implements Serializable<Result,string> {
    inputCommandUri: string;
    action: string;
    outputs : Result[];
    value: any;
    rootDomainId:string;
    constructor(private configSvc: ConfigService) {}
    deserialize( inJson ) {
        let obj = this;
        obj = Converter.convert(inJson, obj);
        if(inJson.value != null) {
            if(inJson.value.config != null) {
                obj['value'] = new Param(this.configSvc).deserialize( inJson.value, inJson.code );
            } else {
                obj['value'] = inJson.value;
            }
        }
        var outputs = [];
        if ( inJson.outputs != null && inJson.outputs.length > 0 ) {
            for ( var value in inJson.outputs ) {
                outputs.push( new Result(this.configSvc).deserialize( inJson.outputs[value] ) );
            }
        }
        obj['outputs'] = outputs;
        return obj;
    }
}

export class ViewRoot {
    model: Model;
    layout: string;
}

export class ViewConfig {
    configId: string;
    config: ParamConfig;
}

export class Model implements Serializable<Model,string> {
    uiStyles: UiStyle;
    params: Param[];

    constructor(private configSvc: ConfigService) {}

    deserialize( inJson, path ) {
        if(inJson.uiStyles != null) {
            this.uiStyles = new UiStyle().deserialize( inJson.uiStyles );
        }
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

export class Config implements Serializable<Config,string> {
    input: Input;
    output: Output;

    constructor(private configSvc: ConfigService) {}

    deserialize( inJson , path) {
        this.input = new Input(this.configSvc).deserialize( inJson.input , path);
        this.output = new Output(this.configSvc).deserialize( inJson.output, path );
        return this;
    }
}

export class Pattern implements Serializable<Pattern,string> {
    regex: string;

    deserialize( inJson ) {
        let start = inJson.indexOf( '(', 0 ) + 1;
        let end = inJson.lastIndexOf( ')' );
        this.regex = inJson.substr( start, end - start );

        return this;
    }
}

export class Length implements Serializable<Length, string> {
    regexp: string;

    deserialize( inJson ) {
        // Build a regex pattern for min, max (pattern=".{min,max}")
        this.regexp = '.{';
        if ( inJson.min ) {
            this.regexp += inJson.min;
        }
        this.regexp += ',';
        if ( inJson.min ) {
            this.regexp += inJson.max;
        }
        this.regexp += '}';

        return this;
    }
}

export class Output implements Serializable<Output, string> {
    model: Model;

    constructor(private configSvc: ConfigService) {}

    deserialize( inJson, path ) {
        this.model = new Model(this.configSvc).deserialize( inJson.model, path );
        return this;
    }
}

export class ModelEvent implements Serializable<ModelEvent,string> {
    type: string; //Revisit - server sending as type instead of action
    value: any;
    payload: any; //temp fix - value doesnt work. Remove value since server side ModelEvent interface has payload.
    id: string;

    deserialize( inJson ) {
        this.type = inJson.type;
        this.value = inJson.value;
        this.id = inJson.id;

        return this;
    }
}

export class Detail implements Serializable<Detail,string> {
    label: string;
    value: string;

    deserialize( inJson ) {
        var obj = this;
        obj = Converter.convert(inJson, obj)
        return obj;
    }
}

export class Details implements Serializable<Details,string> {
    details: Detail[];

    deserialize( inJson ) {
        this.details = [];
        for ( var detail in inJson.details ) {
            this.details.push( new Detail().deserialize( inJson.details[detail] ) );
        }
        return this;
    }
}


export class ParamConfig implements Serializable<ParamConfig,string> {
    uiStyles: UiStyle;
    id: string;
    code: string;
    type: ConfigType;   
    labelConfigs: LabelConfig[];
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
        let labelConfigs = [];  
        if ( inJson.labelConfigs != null && inJson.labelConfigs.length > 0) { 
            for ( var p in inJson.labelConfigs ) {
                labelConfigs.push( new LabelConfig().deserialize(inJson.labelConfigs[p]) );
            }
            obj['labelConfigs'] = labelConfigs;
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
        if ( this.name === 'ViewConfig.Link' || this.name === 'ViewConfig.LinkMenu') {
            this.isLink = true;
        }
        if ( this.name === 'ViewConfig.Hidden' ) {
            this.isHidden = true;
        }
        this.value = inJson.value;
        this.attributes = new UiAttribute().deserialize( inJson.attributes );

        return this;
    }
}

export class UiAttribute implements Serializable<UiAttribute,string> {
    value: string;
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
    pageSize: number;
    postButton: boolean;
    postButtonUrl: string;
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
    resizable:boolean;
    placeholder: string;
    clearAllFilters: boolean;
    export: boolean;
    clearLabel: string;
    acceptLabel: string;
    deserialize( inJson ) {
        this.value = inJson.value;
        this.url = inJson.url;
        this.editUrl = inJson.editUrl;
        this.editable = inJson.editable;
        this.align = inJson.align;
        this.alias = inJson.alias;
        this.onLoad = inJson.onLoad;
        this.expandableRows = inJson.expandableRows;
        this.asynchronous = inJson.asynchronous;
        this.b = inJson.b;
        this.method = inJson.method;
        if (inJson.imgSrc && inJson.imgSrc != '') {
            this.imgSrc = inJson.imgSrc;
        }
        this.level = inJson.level;
        this.cssClass = inJson.cssClass;
        this.multiple = inJson.multiple;
        this.showExpandAll = inJson.showExpandAll;
        this.selected = inJson.selected;
        this.activeIndex = inJson.activeIndex;
        this.labelClass = inJson.labelClass;
        this.control = inJson.control;
        this.content = inJson.content;
        this.contentId = inJson.contentId;
        this.datePattern = inJson.datePattern;
        this.header = inJson.header;
        this.help = inJson.help;
        this.title = inJson.title;
        this.closable = inJson.closable;
        this.width = inJson.width;
        this.height = inJson.height;
        this.type = inJson.type;
        this.style = inJson.style;
        this.size = inJson.size;
        this.submitUrl = inJson.submitUrl;
        this.navLink = inJson.navLink;
        this.browserBack = inJson.browserBack;
        this.postEventOnChange = inJson.postEventOnChange;
        this.controlId = inJson.controlId;
        this.submitButton = inJson.submitButton;
        this.draggable = inJson.draggable;
        this.sourceHeader = inJson.sourceHeader;
        this.targetHeader = inJson.targetHeader;
        this.rowSelection = inJson.rowSelection;
        this.pagination = inJson.pagination;
        this.showHeader = inJson.showHeader;
        this.pageSize = +inJson.pageSize;
        this.postButton = inJson.postButton;
        this.rows = inJson.rows;
        this.postButtonUrl = inJson.postButtonUrl;
        this.postButtonTargetPath = inJson.postButtonTargetPath;
        this.postButtonAlias = inJson.postButtonAlias;
        this.postButtonLabel = inJson.postButtonLabel;
        this.filterMode=inJson.filterMode;
        this.filterValue=inJson.filterValue;
        this.payload = inJson.payload;
        this.showName = inJson.showName;
        this.iconField = inJson.iconField;
        this.inplaceEdit = inJson.inplaceEdit;
        this.defaultPage = inJson.defaultPage;
        this.formReset = inJson.formReset;
        this.target = inJson.target;
        this.rel = inJson.rel;
        this.hourFormat = inJson.hourFormat;
        this.sortAs = inJson.sortAs;
        this.placeholder = inJson.placeholder;
        this.clearAllFilters = inJson.clearAllFilters;
        this.clearLabel = inJson.clearLabel;
        this.acceptLabel = inJson.acceptLabel;
        if ( inJson.export != null ) {
            this.export = inJson.export;
        }
        if ( inJson.controlType != null ) {
            this.controlType = inJson.controlType;
        }
        if (inJson.showTime) {
            this.showTime = inJson.showTime;
        }
        if (inJson.timeOnly) {
            this.timeOnly = inJson.timeOnly;
        }
        if (inJson.defaultFlow) {
            this.defaultFlow = inJson.defaultFlow;
        }
        if ( inJson.inplaceEdit ) {
            this.inplaceEditType = inJson.inplaceEditType;
        }
        if ( inJson.modelPath ) {
            this.modelPath = inJson.modelPath;
        }
        if ( inJson.cols ) {
            this.cols = inJson.cols;
        }
        if ( inJson.hidden ) {
            this.hidden = inJson.hidden;
        }
        if ( inJson.readOnly ) {
            this.readOnly = inJson.readOnly;
        } else {
            this.readOnly = false;
        }
        if ( inJson.route ) {
            this.route = inJson.route;
        }
        if ( inJson.layout ) {
            this.layout = inJson.layout;
        }
        if ( inJson.formReset ) {
            this.formReset = inJson.formReset;
        }
        if (inJson.filter) {
            this.filter=inJson.filter;
        }
        if(inJson.sortable !== undefined) {
            this.sortable = inJson.sortable;
        }
        if(inJson.resizable) {
            this.resizable = inJson.resizable;
        }
        return this;
    }
}

export class LabelConfig implements Serializable<LabelConfig,string> {
    text: string;
    locale: string;
    helpText : string;

    deserialize( inJson ) {
        let obj = this;
        obj = Converter.convert(inJson,obj);
        return obj;
    }
}

export class ExecuteOutput implements Serializable<ExecuteOutput,string> {
    result: ModelEvent;
    validationResult: any;
    executeException: any;

    deserialize( inJson ) {
        if ( inJson.result != null ) {
            this.result = new ModelEvent().deserialize( inJson.result );
        }
        this.validationResult = inJson.validationResult;
        this.executeException = inJson.executeException;

        return this;
    }
}

export class ExecuteResponse implements Serializable<ExecuteResponse,string> {
    result: MultiOutput[];
    constructor(private configSvc: ConfigService) {}
    deserialize( inJson ) {
        this.result = [];
        if ( inJson.result != null && inJson.result.length > 0) {
            // tslint:disable-next-line:forin
            for ( const p in inJson.result ) {
                this.result.push( new MultiOutput(this.configSvc).deserialize(inJson.result[p]) );
            }
        }
        return this;
    }
}

export class MultiOutput implements Serializable<MultiOutput,string> {
    behavior: string;
    result: Result;
    constructor(private configSvc: ConfigService) {}
    deserialize( inJson ) {
        this.behavior = inJson.b;
        if ( inJson.result != null ) {
            this.result = new Result(this.configSvc).deserialize( inJson.result );
        }
        return this;
    }
}

export class CardDetailsGrid implements Serializable<CardDetailsGrid,string> {
    cards: Array<CardDetails>;
    deserialize( inJson ) {
        this.cards = [];
        //for ( var p in inJson ) {
            //this.cards.push(new CardDetails().deserialize(inJson[p]));
        //}
        return this;
    }
}

export class CardDetails implements Serializable<CardDetails,string> {
    id: string;
    number: number;
    header: CardDetailsHeader;
    body: CardDetailsBody;
    footer: CardDetailsFooter;

    deserialize( inJson ) {
        this.id = inJson.id;
        this.number = inJson.number;
        this.header = new CardDetailsHeader().deserialize( inJson.header );
        this.body = new CardDetailsBody().deserialize( inJson.body );
        this.footer = new CardDetailsFooter().deserialize( inJson.footer );
        return this;
    }
}

export class CardDetailsHeader implements Serializable<CardDetailsHeader,string> {
    title: FieldValue;
    date: FieldValue;
    status: FieldValue;

    deserialize( inJson ) {
        this.title = new FieldValue().deserialize( inJson.title );
        this.date = new FieldValue().deserialize( inJson.date );
        this.status = new FieldValue().deserialize( inJson.status );
        return this;
    }
}

export class CardDetailsBody implements Serializable<CardDetailsBody,string> {
    fields: Array<FieldValue>;

    deserialize( inJson ) {
        this.fields = [];
        for ( var p in inJson.fields ) {
            this.fields.push( new FieldValue().deserialize( inJson.fields[p] ) );
        }
        return this;
    }
}

export class CardDetailsFooter implements Serializable<CardDetailsFooter,string> {
    fields: Array<FieldValue>;

    deserialize( inJson ) {
        this.fields = [];
        for ( var p in inJson.fields ) {
            this.fields.push( new FieldValue().deserialize( inJson.fields[p] ) );
        }
        return this;
    }
}

export class FieldValue implements Serializable<FieldValue,string> {
    field: string;
    value: any;

    deserialize( inJson ) {
        this.field = inJson.field;
        this.value = inJson.value;
        return this;
    }
}

export class DataTransferEffect {

    static COPY = new DataTransferEffect( 'copy' );
    static LINK = new DataTransferEffect( 'link' );
    static MOVE = new DataTransferEffect( 'move' );
    static NONE = new DataTransferEffect( 'none' );

    constructor( public name: string ) { }
}

/**
* Check and return true if an object is type of string
*/
export function isString( obj: any ) {
    return typeof obj === 'string';
}

/**
* Check and return true if an object not undefined or null
*/
export function isPresent( obj: any ) {
    return obj !== undefined && obj !== null;
}

/**
* Check and return true if an object is type of Function
*/
export function isFunction( obj: any ) {
    return typeof obj === 'function';
}

/**
* Create Image element with specified url string
*/
export function createImage( src: string ) {
    let img: HTMLImageElement = new HTMLImageElement();
    img.src = src;
    return img;
}

/**
* Call the function
*/
export function callFun( fun: Function ) {
    return fun();
}

