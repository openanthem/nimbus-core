import { LoaderService } from './loader.service';
'use strict';
import { Action, HttpMethod, Behavior} from './../shared/command.enum';
import { Injectable, EventEmitter } from '@angular/core';
import { ServiceConstants } from './service.constants';
import { ElementModelParam, RemnantState, Validation } from './../shared/app-config.interface';
import {
    Model,
    ModelEvent,
    Page,
    Param,
    ParamConfig,
    Type,
    Result, ViewRoot
} from '../shared/app-config.interface';
import { CustomHttpClient} from './httpclient.service';

import { Subject } from 'rxjs/Subject';
import { GenericDomain } from '../model/generic-domain.model';

declare var trackJs: any;

@Injectable()
export class PageService {
        config$: EventEmitter<any>;
        layout$: EventEmitter<any>;
        appConfigs: Object;
        flowRootDomainId: Object;
        pageMap: Object;
        routeParams: any;

        eventUpdate = new Subject<Param>();
        eventUpdate$ = this.eventUpdate.asObservable();

        validationUpdate = new Subject<Param>();
        validationUpdate$ = this.validationUpdate.asObservable();

        gridValueUpdate = new Subject<Param>();
        gridValueUpdate$ = this.gridValueUpdate.asObservable();

        private _entityId: number = 0;
        constructor(private http: CustomHttpClient, private loaderService: LoaderService) {
                // initialize
                this.appConfigs = {};
                this.flowRootDomainId = {};
                // Create Observable Stream to output our data     
                this.config$ = new EventEmitter();
                this.layout$ = new EventEmitter();
        }

        ngOnInit() {
                //console.log('on init of page service.');
        }

        get entityId(): number {
                return this._entityId;
        }
        set entityId(id: number) {
                this._entityId = id;
        }

        logError(err) {
            console.error('ERROR: Failure making server call : ' + JSON.stringify(err));
        }

        /** Build the base URL for Server calls */
        buildBaseURL() {
                let baseURL = ServiceConstants.PLATFORM_BASE_URL;
                let client;
                if(this.routeParams != null) {
                        client = this.routeParams.client;
                        /** If client is passed then use client code, else 'platform' */
                        if (client == null) {
                                baseURL = ServiceConstants.PLATFORM_BASE_URL;
                        } else {
                                baseURL = ServiceConstants.CLIENT_BASE_URL;
                                /** client code is required for a client base url.*/
                                if (this.routeParams.client == null) {
                                        console.log('ERROR: Missing client information for a CLIENT_BASE_URL.');
                                }
                                baseURL += this.routeParams.client;
                        }
                        /** App name for the flow - required */
                        if (this.routeParams!=null && this.routeParams.app == null) {
                                console.log('ERROR: Missing app name.');
                        } else {
                                baseURL += '/' + this.routeParams.app + '/p';
                        }
                        /** Domain for the operation - required */
                        if (this.routeParams!= null && this.routeParams.domain == null) {
                                console.log('ERROR: Missing domain entity name.');
                        }
                }
                return baseURL;
        }

        /** Build the page flow base URL for Server calls */
        buildFlowBaseURL() {
                let baseURL = this.buildBaseURL();
                baseURL +=  this.routeParams.domain;
                return baseURL;
        }

        /** Get the flow config if it is already loaded */
        getFlowConfig(flowName: string): Model {
            if (this.appConfigs[flowName]) {
                return this.appConfigs[flowName].model;
            } else {
                return undefined;
            }
        }

        /** Get the layout config name for flow */
        getFlowLayoutConfig(flowName: string, routeToDefaultPage: boolean): Promise<string> {
            let layoutName: string = undefined;
            if (this.appConfigs[flowName]) {
                layoutName = this.appConfigs[flowName].layout;
                if (routeToDefaultPage) {
                    this.navigateToDefaultPageForFlow(this.appConfigs[flowName].model, flowName);
                }
            }
            return Promise.resolve(layoutName);
        }

        /** Get the rootDomainId config if it is already loaded */
        getFlowRootDomainId(flowName: string) {
                return this.flowRootDomainId[flowName];
        }

        /**
         * Load the Layout for give Flow Domain
         * If Domain does not exist, load the domain first and then the layout.
         * @param flowName
         */
        getLayoutConfigForFlow(flowName: string) {
            this.loadDomainFlowConfig(flowName);
        }

        /** Get Flow Config */
        loadDomainFlowConfig(flowName: string) {
            let baseUrl = ServiceConstants.PLATFORM_BASE_URL;
            baseUrl += '/' + flowName;
            let url = baseUrl + '/'+Action._new.value+'?b='+Behavior.execute.value;

            this.http.get(url)
                    .map(res => res.json())
                    .subscribe(data => {
                            console.log('Creating a global page map');
                            this.processResponse(data.result, baseUrl, flowName);
                    },
                    err => this.logError(err),
                    () => console.log('GetPageConfig query completed..')
                    );
        }

        /** Get Flow Config - TODO delete after routes refactor. the above method will be used instead of this one.*/
        loadFlowConfig(routeParms: any) {
                let baseUrl = ServiceConstants.PLATFORM_BASE_URL;
                baseUrl += '/' + routeParms.domain;
                let url = baseUrl + '/'+Action._new.value+'?b='+Behavior.execute.value;

                this.http.get(url)
                        .map(res => res.json())
                        .subscribe(data => {
                                console.log('Creating a global page map');
                                this.processResponse(data.result, baseUrl, routeParms.domain);
                        },
                        err => this.logError(err),
                        () => console.log('GetPageConfig query completed..')
                        );
        }

        /** Page id - to know which page to load from the config */
        getPageToNavigateTo(direction: string, useFlowUrl:string) {
                let flowName = useFlowUrl.substring(useFlowUrl.indexOf('flow_'));
                if (flowName.indexOf('/') > 0) {
                        flowName = flowName.substring(0,flowName.indexOf('/'));
                }
                let url = '';
                if(useFlowUrl === '') {
                        url = this.buildFlowBaseURL();
                } else {
                        url = useFlowUrl;
                }
                url = url + '/_nav?a=' + direction + '&b=$execute';
                this.http.get(url)
                .map(res => res.json())
                .subscribe(data => {
                        this.processResponse(data.result,'', flowName);
                },
                        err => this.logError(err),
                        () => console.log('getPageToNavigateTo query completed..')
                );
        }

        /** Parse flow name from payload path */
        getFlowNameFromPath(path: string): string {
                let flowName = '';
                let truncatedBasePath = '';
                // path pattern is - '/flowname/.../...'.
                // If it is complete path, truncate everything before /p/
                if (path.indexOf('/p/') > 0) {
                    truncatedBasePath = path.substring(path.indexOf('/p/', 1) + 2, path.length);
                } else {
                    truncatedBasePath = path;
                }
                flowName = truncatedBasePath.split('/')[1];

                // Remove everything before '/p/', including /p/ which is the platform root.
                //flowName = path.substring(path.indexOf('/p/')+3);
                // The start now is flow name. Remove everything from the first '/' after flow name
                //flowName = flowName.substring(0, flowName.indexOf('/'));
                return flowName;
        }

        /** Process response - array or responses based on request chaining */
        processResponse(response: any, serverUrl:string, flowName: string) {
                let index = 0;
                while (response[index]) {
                        let subResponse: any = response[index];
                        if (subResponse.b === Behavior.execute.value) {
                            let flowConfig = new Result().deserialize(subResponse.result);
                            this.traverseOutput(flowConfig.outputs);
                        } else {
                                console.log('ERROR: Unknown response for behavior - ' + subResponse.b);
                        }
                        index ++;
                }
        }

       getFlowNameFromOutput(paramPath:string): string {
                let flow = this.getFlowNameFromPath(paramPath);
                if(flow.indexOf(':')>0) {
                        flow = flow.substr(0,flow.indexOf(':'));
                }
                return flow;
        }

       setLayoutToAppConfig(flowName: string, model: Model) {
           let viewRoot: ViewRoot = new ViewRoot();
           viewRoot.model = model;
           this.appConfigs[flowName] = viewRoot;
       }

       getDetaultPageForFlow(model: Model): Param {
           if (model!=null && model.params != null) {
               for ( var p in model.params ) {
                   let pageParam: Param = model.params[p];
                   if (pageParam.config.uiStyles!= null && pageParam.config.uiStyles.name === 'ViewConfig.Page') {
                       if(pageParam.config.uiStyles.attributes.defaultPage) {
                           return pageParam;
                       }
                   }
               }
           }
           return undefined;
       }

       navigateToDefaultPageForFlow(model: Model, flow: string) {
           let pageParam: Param = this.getDetaultPageForFlow(model);
           if (pageParam) {
               this.navigateToPage(pageParam, flow);
           }
       }

       /**
        * Process the Outputs from http call response.
        * @param outputs
        */
       traverseOutput(outputs:Result[]) {
           var navToDefault = true;
           outputs.forEach(otp => {
                   if(otp.action === Action._nav.value ) {
                        navToDefault = false;
                   }
           });
           outputs.forEach(output => {
               if (output.value == null) {
                   this.traverseOutput(output.outputs);
               } else { 
                   if (output.action === Action._new.value) {
                       let flow = this.getFlowNameFromOutput(output.value.path);
                       // Check if the _new output is for the Root Flow
                       if (output.value.path == '/' + flow) {
                           let viewRoot: ViewRoot = new ViewRoot();
                           // Check if there is a layout for this domain
                           if (output.value.config.type.model.uiStyles) {
                               viewRoot.layout = output.value.config.type.model.uiStyles.attributes.layout;
                               // emit the layout name
                               this.layout$.emit(viewRoot.layout);
                           }
                           viewRoot.model = output.value.type.model;
                           this.appConfigs[flow] = viewRoot;

                           if(output.rootDomainId !== 'null') {
                                   this.flowRootDomainId[flow] = output.rootDomainId;
                           }
                           if(navToDefault) {
                                    this.navigateToDefaultPageForFlow(output.value.type.model, flow);
                           }
                       } else {
                           let eventModel : ModelEvent = new ModelEvent().deserialize(output);
                           this.traverseFlowConfig(eventModel, flow);
                       }                          
                   } else if (output.action === Action._get.value) {
                       let flow = this.getFlowNameFromOutput(output.value.path);
                       let viewRoot: ViewRoot = new ViewRoot();
                       // Add the flow config to memory.
                       if (output.value.config.type.model && output.value.config.type.model.uiStyles) {
                           viewRoot.layout = output.value.config.type.model.uiStyles.attributes.layout;
                       }
                       viewRoot.model = output.value.type.model;
                       this.appConfigs[flow] = viewRoot;
                       if (output.rootDomainId !== 'null') {
                           this.flowRootDomainId[flow]=output.rootDomainId;
                       }

                       if(navToDefault) {
                           let toPage: Param = this.getDetaultPageForFlow(viewRoot.model);
                           this.navigateToPage(toPage, flow);
                       }
                   } else if (output.action === Action._nav.value) {
                       let flow = this.getFlowNameFromOutput(output.inputCommandUri);
                       let pageParam = this.findMatchingPageConfigById(output.value, flow);
                       this.navigateToPage(pageParam, flow);
                   } else {
                       if (output.value && output.value.path) {
                           let flow = this.getFlowNameFromOutput(output.value.path);
                           let eventModel : ModelEvent = new ModelEvent().deserialize(output);
                           this.traverseFlowConfig(eventModel, flow);
                       }
                   }
               }
           });
       }

       /** When the config is already loaded, find the default page to load */
       loadDefaultPageForConfig(flowName: string) {
           let baseUrl = ServiceConstants.PLATFORM_BASE_URL;
           baseUrl += '/' + flowName;
           this.getPageToNavigateTo('next', baseUrl);
           let model: Model = this.getFlowConfig(flowName);
           this.navigateToDefaultPageForFlow(model, flowName);
       }

       navigateToPage(pageParam:Param, flow:string) {
                let page: Page = new Page();
                page.pageConfig = pageParam;
                page.flow = flow;
                this.config$.emit(page);
        }

        getPageConfigById(pageId: string, flowName: string): Promise<Param> {
               return Promise.resolve(this.findMatchingPageConfigById(pageId, flowName));
        }

        /** Get the page config matching the page ID and flow Name */
        findMatchingPageConfigById(pageId: string, flowName: string): Param {

                if(flowName.indexOf(':') !== -1) {
                        flowName = flowName.substr(0, flowName.indexOf(':'));
                }

                let page: Param;
                let flowConfig = this.getFlowConfig(flowName);
                if (flowConfig != null && flowConfig.params != null) {
                        flowConfig.params.forEach(pageParam => {
                                if (pageParam.config.uiStyles!= null && pageParam.config.uiStyles.name === 'ViewConfig.Page') {
                                        if(pageParam.config.code === pageId) {
                                                page = pageParam;
                                        }
                                }
                        });
                }
                if (!page) {
                        console.log('ERROR: Page Configuration not found for Page ID: ' + pageId + ' in Flow: ' + flowName);
                }
                return page;
        }

        /** Process execute call - TODO revisit to make it more dynamic based on url completion */
        processEvent(processUrl: string, behavior: string, model: GenericDomain, method: string ) {
                // if (model!=null && model['id']) {
                //         this.entityId = model['id'];
                // }
                processUrl = processUrl+'/' + Action._get.value;
                let url = '';
                let serverUrl = '';
                let flowName = '';
                if(behavior==undefined) {
                         behavior = Behavior.execute.value;
                }
                flowName = processUrl.substring(1, processUrl.indexOf('/', 2)); //TODO
                url = ServiceConstants.PLATFORM_BASE_URL;
                serverUrl = url + processUrl.replace('{id}',this.entityId.toString());

                if (processUrl.indexOf('?') > 0) {
                        url = url + processUrl.replace('{id}',this.entityId.toString())+'&b='+behavior;
                } else {
                        url = url + processUrl.replace('{id}',this.entityId.toString())+'?b='+behavior;
                }
                //}

                let rootDomainId = this.getFlowRootDomainId(flowName);
                if(rootDomainId!=null) {
                        let flowNameWithId = flowName.concat(':'+rootDomainId);
                        url = url.replace(flowName,flowNameWithId);
                }
                this.showLoader();
                if (method !== '' && method.toUpperCase() === HttpMethod.GET.value) {
                        // if(behavior =='$config'){
                        //         this.router.navigate(['Patientenrollment',{domain:"patientenrollment"}]);
                        // }else{

                                this.http.get(url)
                                .map(res => res.json())
                                .subscribe(data => {
                                        this.processResponse(data.result,serverUrl, flowName);
                                },
                                        err => this.logError(err),
                                        () => {console.log('Process Execution query completed..');
                                        this.hideLoader();
                                                }
                                );
                        //}
                } else if(method !== '' && method.toUpperCase() === HttpMethod.POST.value) {
                        console.log('payload - ' + JSON.stringify(model));
                        this.http.post(url, JSON.stringify(model))
                        .map(res => res.json())
                        .subscribe(data => {
                                this.processResponse(data.result,serverUrl, flowName);
                        },
                                err => this.logError(err),
                                () => {console.log('Process Execution query completed..');
                                        this.hideLoader();
                                                }
                        );
                } else {
                        throw 'http method not supported';
                }
        }

        /** Process execute call - TODO revisit to make it more dynamic based on url completion */
        processPost(processUrl: string, behavior: string, payload: any, method: string, navLink?: string ) {
                let flowName = processUrl.substring(1, processUrl.indexOf('/', 2)); //TODO
                let url = ServiceConstants.PLATFORM_BASE_URL + processUrl+'/' + Action._get.value;
                
                if(method !== '' && method.toUpperCase() === HttpMethod.POST.value) {
                        console.log('payload - ' + JSON.stringify(payload));
                        this.http.post(url, JSON.stringify(payload))
                        .map(res => res.json())
                        .subscribe(data => {
                                this.processResponse(data.result, url, flowName);
                                if(navLink != null && navLink !== '') {
                                        this.processEvent(navLink, '$executeAnd$config', null, 'POST');
                                }
                        },
                                err => this.logError(err),
                                () => console.log('Process Execution query completed..')
                        );
                } else {
                        throw 'http method not supported';
                }
        }

        postOnChange(path: string, payloadAttr: string, payloadValue: string) {
                path = path + '/' + Action._replace.value;
                let flowName = path.substring(1, path.indexOf('/', 2));
                let rootDomainId = this.getFlowRootDomainId(flowName);
                if(rootDomainId!=null) {
                        let flowNameWithId = flowName.concat(':'+rootDomainId);
                        path = path.replace(flowName,flowNameWithId);
                }
                console.log('Post on change :: ' + path);
                let modelEvnt = new ModelEvent();
                modelEvnt.type = Action._replace.value; //use the server side actions like _new, _update
                modelEvnt.id = path;
                modelEvnt.value = payloadValue;
                modelEvnt.payload = payloadValue;
              //  modelEvnt.value.path = path;
                console.log(JSON.stringify(modelEvnt));
                var urlBase = ServiceConstants.PLATFORM_BASE_URL;
                var url = urlBase + '/event/notify';
                //if (action === Action._update.value) {
                        console.log('Post update Event Notify call');
                        return this.http.post(url, JSON.stringify(modelEvnt))
                                .map(res => res.json())
                                .subscribe(data => {
                                        //console.log('Event notify data::' + JSON.stringify(data));
                                        this.processResponse(data.result, '', '');
                                },
                                err => this.logError(err),
                                () => console.log('Post completed..')
                                );
               // }
        }

        /**
         * Loop through all Configs to find the match to update the Model Event
         */
        traverseFlowConfig(eventModel: ModelEvent, flowName: string) {
            if (this.getFlowConfig(flowName)) {
                this.traverseConfig(this.getFlowConfig(flowName).params, eventModel);
            }
        }

        getUpdatedParamPath(eventModelPath: string): string {
                // Remove path before '/p/' which is the platform path
                //let updatedParamPath = eventModelPath.substring(eventModelPath.indexOf('/p/')+3);
                // Remove flow name which is the beginning of the event path
                let updatedParamPath = eventModelPath.substring(eventModelPath.indexOf('/', 1)+1);
                //Remove the context model attributes from path
                if (updatedParamPath.indexOf('/#') !== -1) {
                        updatedParamPath = updatedParamPath.substring(0, updatedParamPath.indexOf('/#'));
                        // TODO REMOVE , ONLY FOR TESTING SINCE SERVER SIDE FRAMEWORK IS NOT SENDING THE WEBSOCKET UPDATE UPTO THE LAST ELEMENT LEVEL
                        if (updatedParamPath === 'caseOverview/caseInfoCard/caseInfoSection/cardDetailCase/headerCase') {
                                updatedParamPath = 'caseOverview/caseInfoCard/caseInfoSection/cardDetailCase/headerCase/caseStatus';
                                //cmPrograms/cmProgramCard/cmProgramSection/cmProgramForm/caseStatus/#/values
                        }
                        if (updatedParamPath === 'caseOverview/caseInfoCard/caseInfoSection/cardDetailCase/headerCase2') {
                                updatedParamPath = 'caseOverview/caseInfoCard/caseInfoSection/cardDetailCase/headerCase2/statusReason';
                                //cmPrograms/cmProgramCard/cmProgramSection/cmProgramForm/caseStatus/#/values
                        }
                }
                return updatedParamPath;
        }
        
        /**
         * Traverse the complete config to find the matching page to update the event
         * params: list of pages
         * eventModel: websocket update
         */
        traverseConfig(params: Param[], eventModel: ModelEvent) {
                let updatedParamPath = this.getUpdatedParamPath(eventModel.value.path);
                // Hierarchy of the updated param
                let paramTree = updatedParamPath.split('/');
                // Page node
                let ROOTNODE = 0;
                params.forEach(element => {
                        // Check if param matches the updated param path
                        if (element.config.code === paramTree[ROOTNODE]) {
                                // Lets go futher into the page param to find the exact match
                                this.traversePageConfig(element, eventModel, ROOTNODE);
                        }
                });
        }

        /** 
         * Traverse the Page tree to find the updated object 
         * param: page
         * eventModel: websocket update
        */
        traversePageConfig(rootParam: Param, eventModel: ModelEvent, node: number) {
                let updatedParamPath = this.getUpdatedParamPath(eventModel.value.path);
                // Hierarchy of the updated param
                let paramTree = updatedParamPath.split('/');
                // Size - # tree nodes
                let numNodes = paramTree.length - 1;
                // Check for Collection
                if (eventModel.value.collectionElem) {
                        // reduce the size by 1 to account for collection element index
                        numNodes--;
                }

                // Check if root param matches the eventModel update node
                if (rootParam.config.code === paramTree[node]) {
                    node++;
                    // Loop through the Params in the Page
                    if (rootParam.type.model && rootParam.type.model.params) {
                        rootParam.type.model.params.forEach(element => {
                                // Check if param matches the updated param path
                                if (element.config.code === paramTree[node]) {
                                        if (node < numNodes) {
                                                // Lets go futher into the matching param to find the exact match
                                                this.traversePageConfig(element, eventModel, node);
                                        } else {
                                                // Matching param node
                                                this.processModelEvent(element, eventModel);
                                        }
                                }
                        });
                    }
                }
        }
        
        /** 
         * Create the Grid Row Data from Param Leaf State
         * 
         */
        createRowData(param: Param, nestedGridParam: ElementModelParam) {
            let rowData: any = {};
            rowData = param.leafState;
            rowData['elemId'] = param.elemId;
            
            // If nested data exists, set the data to nested grid
            if (nestedGridParam) {
                nestedGridParam.gridList = rowData[nestedGridParam.code];
            }
            
            return rowData;
        }
        
        /** 
         * Loop through the Param State and build the Grid
         * 
         */
        createGridData(params: Param[], gridCols: ElementModelParam[]) {
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
                gridData.push(this.createRowData(param, nestedGridParam));
            });
            return gridData;
        }

        /**
         * Process the Event by updating the Param
         * param: Param to be updated
         * eventModel: Event received
         */
        processModelEvent(param: Param, eventModel: ModelEvent) {
            // Grid updates
            if (param.config.uiStyles != null && param.config.uiStyles.attributes.alias === 'Grid') {
                if(eventModel.value.leafState != null) {
                    // Collection Element Check - update only the element
                    if (eventModel.value.collectionElem) {
                        if (param.config.gridList == null) {
                            param.config.gridList = this.createGridData(eventModel.value.type.model.params, param.config.gridCols);
                        } else {
                            param.config.gridList.push(this.createGridData(eventModel.value.type.model.params, param.config.gridCols));
                        }
                        this.gridValueUpdate.next(param);
                    }
                    // Collection check - update entire collection
                    if (eventModel.value.collection) {
                            param.config.gridList = this.createGridData(eventModel.value.type.model.params, param.config.gridCols);
                            this.gridValueUpdate.next(param);
                    }
                }
            } else if (param.config.uiStyles != null && param.config.uiStyles.attributes.alias === 'CardDetailsGrid') {
                if (param.type.collection === true) {
                    let payload: Param = new Param().deserialize(eventModel.value);
                    param.type.model['params'] = payload.type.model.params;
                } else {
                    this.traverseParam(param, eventModel);
                }
            } else {
                this.traverseParam(param, eventModel);
            }
        }

        /** Update param with value */
        traverseParam(param: Param, eventModel: ModelEvent) {
                /* Flow-Wrapper class also invokes methods that eventually call this behaviour. We need to make sure that the eventModel is deserialized by then */
                let payload = eventModel.value;
                if (param.type.nested === true) {
                    this.updateParam(param, payload);
                    if (param.type.model && payload.type.model && payload.type.model.params) {
                        for ( var p in param.type.model.params ) {
                            this.updateParam(param.type.model.params[p], payload.type.model.params[p]);
                        }
                    }
                    if (param.type.model === undefined && payload.type.model) {
                        param.type['model'] = payload.type.model;
                    }
                } else {
                    this.updateParam(param, payload);
                }

                if (trackJs) {
                        trackJs.track('test the manual error log');
                }
        }

        updateParam(param: Param, payload: Param) {
                let result: any[] = Reflect.ownKeys(param);
                let updatedKeys: any[] = Reflect.ownKeys(payload);
                updatedKeys.forEach(updatedKey => {
                        result.forEach(currentKey => {
                                if (currentKey === updatedKey) {
                                        try {
                                                if (Reflect.ownKeys(Reflect.get(param, currentKey)) != null
                                                        && Reflect.ownKeys(Reflect.get(param, currentKey)).length > 0) {
                                                        if (currentKey === 'type') {
                                                                //Object.assign(Reflect.get(param, currentKey), new Type().deserialize(Reflect.get(payload, updatedKey)));
                                                        } else if (currentKey === 'config') {
                                                                Object.assign(Reflect.get(param, currentKey), new ParamConfig().deserialize(Reflect.get(payload, updatedKey)));
                                                                //TODO - refactor this whole method and the conditions. Revisit - order, count
                                                        } else if (currentKey === 'validations') {
                                                                Object.assign(Reflect.get(param, currentKey), new Validation().deserialize(Reflect.get(payload, updatedKey)));
                                                                //TODO - refactor this whole method and the conditions. Revisit - order, count
                                                        } else if (currentKey === 'leafState' || currentKey === 'message') {
                                                                Reflect.set(param, currentKey, Reflect.get(payload, updatedKey));
                                                                this.eventUpdate.next(param);
                                                        } else if (currentKey === 'enabled' || currentKey === 'visible') {
                                                                let newRState = new RemnantState().deserialize(Reflect.get(payload, updatedKey));
                                                                if (newRState['currState'] !== param[currentKey]['currState']) {
                                                                        Object.assign(Reflect.get(param, currentKey), newRState);
                                                                        this.validationUpdate.next(param);
                                                                }
                                                        } else if (currentKey === 'values') {
                                                                if (param.values == null) {
                                                                        let values = [];
                                                                        values.push(Reflect.get(payload, updatedKey)) ;
                                                                        param.values = values;
                                                                        this.eventUpdate.next(param);
                                                                } else {
                                                                        param.values = Reflect.get(payload, updatedKey);
                                                                        this.eventUpdate.next(param);
                                                                }
                                                        }
                                                }
                                        } catch (e) {
                                                if (e instanceof TypeError) {
                                                        Reflect.set(param, currentKey, Reflect.get(payload, updatedKey));
                                                        if (currentKey === 'leafState') {
                                                                this.eventUpdate.next(param);
                                                        }
                                                } else {
                                                        throw e;
                                                }
                                        }
                                }
                        });
                });

        }

        /** Get flow configuration to create pages. */
        getPageConfig() {
                let url = this.buildFlowBaseURL();
                // TODO - temp to handle subflows
                if (this.routeParams.subflow === true) {
                        url = url + '/_landing/_process?b=$executeAnd$config';
                } else  {
                        url = url + '/_new?b=$config';
                }

                this.http.get(url)
                        .map(res => res.json())
                        .subscribe(data => {
                                console.log('Creating a global page map');
                                this.processResponse(data.result,'', '');
                        },
                        err => this.logError(err),
                        () => console.log('GetPageConfig query completed..')
                        );
        }

        private showLoader(): void {
                this.loaderService.show();
            }
        
            private hideLoader(): void {
                this.loaderService.hide();
            }
}
