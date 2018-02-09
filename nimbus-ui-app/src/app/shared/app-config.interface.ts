
export interface Serializable<T> {
    deserialize( inJson: Object ): T;
}

export class Values implements Serializable<Values> {
    code: string;
    label: string;
    desc: string;

    deserialize( inJson ) {
        this.code = inJson.code;
        this.label = inJson.label;
        this.desc = inJson.desc;
        return this;
    }
}

export class ElementConfig implements Serializable<ElementConfig> {
    type: Type;
    desc: Desc;

    deserialize( inJson ) {
        if ( inJson.type ) {
            this.type = new Type().deserialize( inJson.type );
        }
        if ( inJson.desc ) {
            this.desc = new Desc().deserialize( inJson.desc );
        }

        return this;
    }
}

export class Type implements Serializable<Type> {
    nested: boolean;
    name: string;
    model: Model;
    collection: boolean;
    elementConfig: ElementConfig;

    deserialize( inJson ) {
        this.nested = inJson.nested;
        this.name = inJson.name;
        this.collection = inJson.collection;

        if ( this.nested === true && inJson.model  != null) {
            this.model = new Model().deserialize( inJson.model );
        }
        if ( inJson.elementConfig != null) {
            this.elementConfig = new ElementConfig().deserialize( inJson.elementConfig );
        }

        return this;
    }
}

export class Desc implements Serializable<Desc> {
    label: string;
    hint: string;
    help: string;

    deserialize( inJson ) {
        this.help = inJson.help;
        this.hint = inJson.hint;
        this.label = inJson.label;
        return this;
    }
}

export class Attribute implements Serializable<Attribute> {
    message: string;
    pattern: string;
    regexp: string;
    min: any;
    max: any;

    deserialize( inJson ) {
        this.message = inJson.message;
        this.pattern = inJson.pattern;
        this.regexp = inJson.regexp;
        this.min = inJson.min;
        this.max = inJson.max;
        return this;
    }
}

export class Constraint implements Serializable<Constraint> {
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

export class Validation implements Serializable<Validation> {
    constraints: Constraint[];

    deserialize( inJson ) {
        this.constraints = [];
        for ( var p in inJson ) {
            this.constraints.push( new Constraint().deserialize( inJson[p] ) );
        }
        return this;
    }
}

export class Page implements Serializable<Page> {
    pageConfig: Param;
    flow: string;

    deserialize( inJson ) {
        return this;
    }
}

export class Message implements Serializable<Message> {
    type: string;
    text: string;
    
    deserialize( inJson ) {
        this.type = inJson.type;
        this.text = inJson.text;
        
        return this;
    }
}

export class RemnantState<T> {
    currState: T
    prevState: T

    deserialize(inJson) {
        this.currState = inJson.currState;
        this.prevState = inJson.prevState;
        return this;
    }
}

export class Param implements Serializable<Param> {
    config: ParamConfig;
    type: Type;
    leafState: any;
    path: string;
    collection: boolean;
    collectionElem: boolean;
    elemId: string;
    mapped: boolean;
    visible: RemnantState<boolean>;
    enabled: RemnantState<boolean>;
    message : Message;
    values : Values[];
    _alias: string;

    public get alias(): string {
        if (this.config.uiStyles && this.config.uiStyles.attributes) {
            return this.config.uiStyles.attributes.alias;
        }
        return undefined;
    }
    deserialize( inJson ) {
        this.mapped = inJson.mapped;
        if(inJson.config != null) {
            this.config = new ParamConfig().deserialize( inJson.config );
        }
        if(inJson.type != null) {
            this.type = new Type().deserialize( inJson.type );
        }
        this.collectionElem = inJson.collectionElem;
        this.collection = inJson.collection;
        if ( inJson.collectionElem ) {
            this.elemId = inJson.elemId;
        }
        if ( this.config != null && this.config.uiStyles && this.config.uiStyles.attributes.alias === 'CardDetailsGrid' ) {
            if(inJson.leafState != null) {
                this.leafState = new CardDetailsGrid().deserialize( inJson.leafState );
            }
        //} else if( this.config != null && this.config.uiStyles && this.config.uiStyles.attributes.alias === 'Grid' ){ 
        //  // TODO : Clean up. Duplicating this logic here to populate grid data when data is recieved as leafstate.
             if(inJson.leafState != null) {
                 this.config.gridList = this.createGridData(this.type.model.params, this.config.gridCols);
             }
        } else {
            this.leafState = inJson.leafState;
        }

        if(this.config != null && this.config.uiStyles && this.config.uiStyles.attributes.alias === 'Grid' ) {
            if(inJson.leafState != null) {
                 this.config.gridList = this.createGridData(this.type.model.params, this.config.gridCols);
            }
        }
        
        this.path = inJson.path;
        if (inJson.visible != null) {
            this.visible = new RemnantState<boolean>().deserialize(inJson.visible);
        }
        if (inJson.enabled != null) {
            this.enabled = new RemnantState<boolean>().deserialize(inJson.enabled);
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
        return this;
    }
    // TODO : Clean up. Duplicating this logic here to populate grid data when data is recieved as leafstate
    private createGridData(params: Param[], gridCols: ElementModelParam[]) {
        let gridData = [];
        // Look for inner lists (nested grid)
        let nestedGridParam: ElementModelParam;
        if (gridCols) {
            gridCols.forEach(col => {
            if (col.uiStyles && col.uiStyles.name == 'ViewConfig.Grid') {
                nestedGridParam = col;
            } 
            });
        }
        params.forEach(param => {
            let rowData : any = {};
            rowData = this.createRowData(param, nestedGridParam);
            gridData.push(rowData);
        });
        return gridData;
    } 
    // TODO : Clean up. Duplicating this logic here to populate grid data when data is recieved as leafstate
    private createRowData(param: Param, nestedGridParam: ElementModelParam) {
        let rowData: any = {};
        rowData = param.leafState;
        rowData['elemId'] = param.elemId;
    
        // If nested data exists, set the data to nested grid
        if (nestedGridParam) {
            nestedGridParam.gridList = rowData[nestedGridParam.code];
        }
    
    return rowData;
    }
}

export class Input implements Serializable<Input> {
    model: Model;
    deserialize( inJson ) {
        this.model = new Model().deserialize( inJson.model );
        return this;
    }
}

export class Result implements Serializable<Result> {
    action: string;
    inputCommandUri: string;
    outputs : Result[];
    value: any;
    rootDomainId:string;
    deserialize( inJson ) {
        this.action = inJson.action;
        this.inputCommandUri = inJson.inputCommandUri;
        this.rootDomainId = inJson.rootDomainId;
        if(inJson.value != null) {
            if(inJson.value.config != null) {
                this.value = new Param().deserialize( inJson.value );
            } else {
                this.value = inJson.value;
            }
        }
        this.outputs = [];
        if ( inJson.outputs != null && inJson.outputs.length > 0 ) {
            for ( var value in inJson.outputs ) {
                this.outputs.push( new Result().deserialize( inJson.outputs[value] ) );
            }
        }
        return this;
    }
}

// export class Outputs implements Serializable<Outputs> {
//     action: string;
//     inputCommandUri: string;
//     value: Param;
//     outputs : Result;
//     deserialize( inJson ) {
//         this.action = inJson.action;
//         this.inputCommandUri = inJson.inputCommandUri;
//         if(inJson.value!=null) {
//             this.value = new Param().deserialize( inJson.value );
//         }
//         if (inJson.outputs != null ) {
//             this.outputs = new Result().deserialize(inJson.outputs);
//         }
//         return this;
//     }
// }

export class ViewRoot {
    model: Model;
    layout: string;
}

export class Model implements Serializable<Model> {
    config: ParamConfig;
    uiStyles: UiStyle;
    params: Param[];
    deserialize( inJson ) {
        if(inJson.config != null) {
            this.config = new ParamConfig().deserialize( inJson.config );
        }
        if(inJson.uiStyles != null) {
            this.uiStyles = new UiStyle().deserialize( inJson.uiStyles );
        }
        this.params = [];
        for ( var p in inJson.params ) {
            this.params.push( new Param().deserialize( inJson.params[p] ) );
        }
        return this;
    }
}

export class Config implements Serializable<Config> {
    input: Input;
    output: Output;

    deserialize( inJson ) {
        this.input = new Input().deserialize( inJson.input );
        this.output = new Output().deserialize( inJson.output );
        return this;
    }
}

export class Pattern implements Serializable<Pattern> {
    regex: string;

    deserialize( inJson ) {
        let start = inJson.indexOf( '(', 0 ) + 1;
        let end = inJson.lastIndexOf( ')' );
        this.regex = inJson.substr( start, end - start );

        return this;
    }
}

export class Length implements Serializable<Length> {
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

export class Output implements Serializable<Input> {
    model: Model;
    deserialize( inJson ) {
        this.model = new Model().deserialize( inJson.model );
        return this;
    }
}

export class ModelEvent implements Serializable<ModelEvent> {
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

export class Detail implements Serializable<Detail> {
    label: string;
    value: string;

    deserialize( inJson ) {
        this.value = inJson.value;
        this.label = inJson.label;
        return this;
    }
}

export class Details implements Serializable<Details> {
    details: Detail[];

    deserialize( inJson ) {
        this.details = [];
        for ( var detail in inJson.details ) {
            this.details.push( new Detail().deserialize( inJson.details[detail] ) );
        }
        return this;
    }
}


export class ParamConfig implements Serializable<ParamConfig> {
    type: Type;
    code: string;
    desc: Desc;
    label: string;
    message: Message;
    validation: Validation;
    url: string;
    active: boolean;
    enabled: RemnantState<boolean>;
    required: boolean;
    visible: RemnantState<boolean>;
    uiNatures: UiNature[];
    uiStyles: UiStyle;
    postEvent: boolean;
    gridList: any[];
    //TODO Temporary for grid
    gridCols: ElementModelParam[];
    labelConfigs: LabelConfig[];

    deserialize( inJson ) {
     //   this.referredClass = inJson.referredClass;
        if(inJson.type != null) {
            this.type = new Type().deserialize( inJson.type );
        }
        this.code = inJson.code;
        if ( inJson.desc != null ) {
            this.desc = new Desc().deserialize( inJson.desc );
        }
        this.label = inJson.label;
        if ( inJson.message != null ) {
            this.message = new Message().deserialize( inJson.message );
        }
        if ( inJson.validations != null ) {
            this.validation = new Validation().deserialize( inJson.validations );
        }
        this.uiNatures = [];
        if ( inJson.uiNatures != null && inJson.uiNatures.length > 0 ) {
            for ( var uiNature in inJson.uiNatures ) {
                this.uiNatures.push( new UiNature().deserialize( inJson.uiNatures[uiNature] ) );
            }
        }
        if (inJson.visible != null) {
            this.visible = new RemnantState<boolean>().deserialize(inJson.visible);
        }
        if (inJson.enabled != null) {
            this.enabled = new RemnantState<boolean>().deserialize(inJson.enabled);
        }
        if ( inJson.message != null ) {
            this.message = new Message().deserialize( inJson.message );
        }
        this.url = inJson.url;
        this.active = inJson.active;
        this.required = inJson.required;
        if ( inJson.uiStyles != null ) {
            this.uiStyles = new UiStyle().deserialize( inJson.uiStyles );
        }

        //TODO Temporary for grid
        if ( inJson.type && inJson.type.elementModelParams ) {
            this.gridCols = [];
            for ( var p in inJson.type.elementModelParams ) {
                let colParam = new ElementModelParam();
                colParam = new ElementModelParam().deserialize( inJson.type.elementModelParams[p] );
                this.gridCols.push(colParam);
            }
        }
        this.labelConfigs = [];  
        if ( inJson.labelConfigs != null && inJson.labelConfigs.length > 0) { 
            for ( var p in inJson.labelConfigs ) {
                this.labelConfigs.push( new LabelConfig().deserialize(inJson.labelConfigs[p]) );
            }
        }   
        
        return this;
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

export class ElementModelParam extends ParamConfig implements Serializable<ElementModelParam> {

    code: string;
    label: string;
    uiStyles: UiStyle;
    nestedParams: ElementModelParam[];

    deserialize( inJson ) {
        this.code = inJson.code;
        this.labelConfigs = inJson.labelConfigs;
        this.label = inJson.label;
        if ( inJson.uiStyles != null ) {
            this.uiStyles = new UiStyle().deserialize( inJson.uiStyles );
        }
        if (inJson.type && inJson.type.model && inJson.type.model.params) {
            this.nestedParams = [];
            for (var p in inJson.type.model.params ) {
                this.nestedParams.push(new ElementModelParam().deserialize(inJson.type.model.params[p]));
            }
        }

        return this;
    }
}

export class UiNature implements Serializable<UiNature> {
    name: string;
    value: string;
    attributes: UiAttribute;

    deserialize( inJson ) {
        this.name = inJson.name;
        this.value = inJson.value;
        this.attributes = new UiAttribute().deserialize( inJson.attributes );

        return this;
    }
}

export class UiStyle implements Serializable<UiStyle> {
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

export class UiAttribute implements Serializable<UiAttribute> {
    value: string;
    url: string;
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
    submitButton: boolean = true;
    content: string; //-- TO BE DELETED (always pull with contentId)
    contentId: string;
    labelClass: string;
    showTime: boolean;
    timeOnly: boolean;
    hourFormat: string;
    header: string;
    help: string;
    title: string;
    closable: boolean;
    width: string;
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
    pagination: boolean;
    pageSize: string;
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
    deserialize( inJson ) {
        this.value = inJson.value;
        this.url = inJson.url;
        this.editUrl = inJson.editUrl;
        this.editable = inJson.editable;
        this.align = inJson.align;
        this.alias = inJson.alias;
        this.onLoad = inJson.onLoad;
        this.expandableRows = inJson.expandableRows;
        this.b = inJson.b;
        this.method = inJson.method;
        this.imgSrc = inJson.imgSrc;
        this.level = inJson.level;
        this.cssClass = inJson.cssClass;
        this.labelClass = inJson.labelClass;
        this.control = inJson.control;
        this.content = inJson.content;
        this.contentId = inJson.contentId;
        this.header = inJson.header;
        this.help = inJson.help;
        this.title = inJson.title;
        this.closable = inJson.closable;
        this.width = inJson.width;
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
        this.pageSize = inJson.pageSize;
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
        return this;
    }
}

export class LabelConfig implements Serializable<LabelConfig> {
    text: string;
    locale: string;
    helpText : string;

    deserialize( inJson ) {
        this.text = inJson.text;
        this.locale = inJson.locale;
        this.helpText = inJson.helpText;
        return this;
    }
}

export class ExecuteOutput implements Serializable<ExecuteOutput> {
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

export class CardDetailsGrid implements Serializable<CardDetailsGrid> {
    cards: Array<CardDetails>;
    deserialize( inJson ) {
        this.cards = [];
        //for ( var p in inJson ) {
            //this.cards.push(new CardDetails().deserialize(inJson[p]));
        //}
        return this;
    }
}

export class CardDetails implements Serializable<CardDetails> {
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

export class CardDetailsHeader implements Serializable<CardDetailsHeader> {
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

export class CardDetailsBody implements Serializable<CardDetailsBody> {
    fields: Array<FieldValue>;

    deserialize( inJson ) {
        this.fields = [];
        for ( var p in inJson.fields ) {
            this.fields.push( new FieldValue().deserialize( inJson.fields[p] ) );
        }
        return this;
    }
}

export class CardDetailsFooter implements Serializable<CardDetailsFooter> {
    fields: Array<FieldValue>;

    deserialize( inJson ) {
        this.fields = [];
        for ( var p in inJson.fields ) {
            this.fields.push( new FieldValue().deserialize( inJson.fields[p] ) );
        }
        return this;
    }
}

export class FieldValue implements Serializable<FieldValue> {
    field: string;
    value: any;

    deserialize( inJson ) {
        this.field = inJson.field;
        this.value = inJson.value;
        return this;
    }
}

export class DragDropData {
    dragData: any;
    mouseEvent: MouseEvent;
}

export class DragDropConfig {
    public onDragStartClass: string = 'dnd-drag-start';
    public onDragEnterClass: string = 'dnd-drag-enter';
    public onDragOverClass: string = 'dnd-drag-over';
    public onSortableDragClass: string = 'dnd-sortable-drag';

    public dragEffect: DataTransferEffect = DataTransferEffect.MOVE;
    public dropEffect: DataTransferEffect = DataTransferEffect.MOVE;
    public dragCursor: string = 'move';
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

