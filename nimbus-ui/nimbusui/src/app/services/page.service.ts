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
import { LoaderService } from './loader.service';
import { ConfigService } from './config.service';
import { Action, HttpMethod, Behavior } from './../shared/command.enum';
import { Injectable, EventEmitter } from '@angular/core';
import { ServiceConstants } from './service.constants';
import { Validation, ViewConfig,
        Model,
        ModelEvent,
        Page,
        Param,
        ParamConfig,
        Type,
        Result, ViewRoot
} from '../shared/app-config.interface';
import { CustomHttpClient } from './httpclient.service';

import { Subject } from 'rxjs/Subject';
import { GenericDomain } from '../model/generic-domain.model';
import { RequestContainer } from '../shared/requestcontainer';
import { Observable } from 'rxjs/Observable';
import { ExecuteResponse } from './../shared/app-config.interface';
import { ParamUtils } from './../shared/param-utils';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Injectable()
export class PageService {
        config$: EventEmitter<any>;
        layout$: EventEmitter<any>;
        flowRootDomainId: Object;
        pageMap: Object;
        routeParams: any;

        eventUpdate = new Subject<Param>();
        eventUpdate$ = this.eventUpdate.asObservable();

        validationUpdate = new Subject<Param>();
        validationUpdate$ = this.validationUpdate.asObservable();

        gridValueUpdate = new Subject<Param>();
        gridValueUpdate$ = this.gridValueUpdate.asObservable();

    private requestQueue :RequestContainer[] = [];

        private _entityId: number = 0;
        constructor(private http: CustomHttpClient, private loaderService: LoaderService, private configService: ConfigService) {
                // initialize
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
                if (this.routeParams != null) {
                        client = this.routeParams.client;
                        /** If client is passed then use client code, else 'platform' */
                        if (client == null) {
                                baseURL = ServiceConstants.PLATFORM_BASE_URL;
                        } else {
                                baseURL = ServiceConstants.CLIENT_BASE_URL;
                                /** client code is required for a client base url.*/
                                if (this.routeParams.client == null) {
                                        this.logError('Missing client information for a CLIENT_BASE_URL.');
                                }
                                baseURL += this.routeParams.client;
                        }
                        /** App name for the flow - required */
                        if (this.routeParams != null && this.routeParams.app == null) {
                                this.logError('Missing app name.');
                        } else {
                                baseURL += '/' + this.routeParams.app + '/p';
                        }
                        /** Domain for the operation - required */
                        if (this.routeParams != null && this.routeParams.domain == null) {
                                this.logError('Missing domain entity name.');
                        }
                }
                return baseURL;
        }

        /** Build the page flow base URL for Server calls */
        buildFlowBaseURL() {
                let baseURL = this.buildBaseURL();
                baseURL += this.routeParams.domain;
                return baseURL;
        }

        /** Get the layout config name for flow */
        getFlowLayoutConfig(flowName: string, routeToDefaultPage: boolean): Promise<string> {
                let layoutName: string = undefined;
                let viewRoot: ViewRoot = this.configService.getFlowConfig(flowName);
                if (viewRoot) {
                        layoutName = viewRoot.layout;
                        if (routeToDefaultPage) {
                                this.navigateToDefaultPageForFlow(viewRoot.model, flowName);
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
                let url = baseUrl + '/' + Action._new.value + '?b=' + Behavior.execute.value;
                this.http.get(url)
                        .subscribe(data => {
                                // console.log('Creating a global page map');
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
                let url = baseUrl + '/' + Action._new.value + '?b=' + Behavior.execute.value;
                this.http.get(url)
                        .subscribe(data => {
                                console.log('Creating a global page map');
                                this.processResponse(data.result, baseUrl, routeParms.domain);
                        },
                        err => this.logError(err),
                        () => console.log('GetPageConfig query completed..')
                        );
        }

        /** Page id - to know which page to load from the config */
        getPageToNavigateTo(direction: string, useFlowUrl: string) {
                let flowName = useFlowUrl.substring(useFlowUrl.indexOf('flow_'));
                if (flowName.indexOf('/') > 0) {
                        flowName = flowName.substring(0, flowName.indexOf('/'));
                }
                let url = '';
                if (useFlowUrl === '') {
                        url = this.buildFlowBaseURL();
                } else {
                        url = useFlowUrl;
                }
                url = url + '/_nav?a=' + direction + '&b=$execute';
                this.http.get(url)
                        .subscribe(data => {
                                this.processResponse(data.result, '', flowName);
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
        processResponse(response: any, serverUrl: string, flowName: string) {
                let index = 0;
                while (response[index]) {
                        let subResponse: any = response[index];
                        if (subResponse.b === Behavior.execute.value) {
                                let flowConfig = new Result(this.configService).deserialize(subResponse.result);
                                this.traverseOutput(flowConfig.outputs);
                        } else {
                                this.logError('Unknown response for behavior - ' + subResponse.b);
                        }
                        index++;
                }
        }

        getFlowNameFromOutput(paramPath: string): string {
                let flow = this.getFlowNameFromPath(paramPath);
                if (flow.indexOf(':') > 0) {
                        flow = flow.substr(0, flow.indexOf(':'));
                }
                return flow;
        }

        getDetaultPageForFlow(model: Model): Param {
                if (model != null && model.params != null) {
                        for (var p in model.params) {
                                let pageParam: Param = model.params[p];
                                if (pageParam != null && pageParam.config.uiStyles != null && pageParam.config.uiStyles.name === 'ViewConfig.Page') {
                                        if (pageParam.config.uiStyles.attributes.defaultPage) {
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
        traverseOutput(outputs: Result[]) {
                /** Check for Nav Output. Execute Nav after processing all other outputs. */
                var navToDefault = true;
                let navOutput: Result = undefined;
                outputs.forEach(otp => {
                        if (otp.action === Action._nav.value) {
                                navToDefault = false;
                                navOutput = otp;
                        }
                });
                outputs.forEach(output => {
                        if (output.value == null || ParamUtils.isEmpty(output.value)) {
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
                                                this.configService.setLayoutToAppConfig(flow, viewRoot);

                                                if (output.rootDomainId !== 'null') {
                                                        this.flowRootDomainId[flow] = output.rootDomainId;
                                                }
                                                if (navToDefault) {
                                                        this.navigateToDefaultPageForFlow(output.value.type.model, flow);
                                                }
                                        } else {
                                                let eventModel: ModelEvent = new ModelEvent().deserialize(output);
                                                this.traverseFlowConfig(eventModel, flow);
                                        }
                                } else if (output.action === Action._get.value) {
                                        if (output.value.config && output.value.type && output.value.type.model) {
                                                let flow = this.getFlowNameFromOutput(output.value.path);
                                                let viewRoot: ViewRoot = new ViewRoot();
                                                // Add the flow config to memory.
                                                if (output.value.config.type.model && output.value.config.type.model.uiStyles) {
                                                        viewRoot.layout = output.value.config.type.model.uiStyles.attributes.layout;
                                                }
                                                viewRoot.model = output.value.type.model;
                                                this.configService.setLayoutToAppConfig(flow, viewRoot);
                                                
                                                if (output.rootDomainId !== 'null') {
                                                        this.flowRootDomainId[flow] = output.rootDomainId;
                                                }
        
                                                if (navToDefault) {
                                                        let toPage: Param = this.getDetaultPageForFlow(viewRoot.model);
                                                        this.navigateToPage(toPage, flow);
                                                }                                                                
                                        } else {
                                                this.logError('Received an _get call without model or config ' + output.value.path);
                                        }
                                } else if (output.action === Action._nav.value) {
                                        // Do Nothing. We will process _nav action in the end.
                                } else {
                                        if (output.value && output.value.path) {
                                                let flow = this.getFlowNameFromOutput(output.value.path);
                                                let eventModel: ModelEvent = new ModelEvent().deserialize(output);
                                                this.traverseFlowConfig(eventModel, flow);
                                        }
                                }
                        }
                });
                /** Now that all outputs are processed, lets process _nav */
                if (navOutput) {
                        let flow = this.getFlowNameFromOutput(navOutput.inputCommandUri);
                        let pageParam = this.findMatchingPageConfigById(navOutput.value, flow);
                        this.navigateToPage(pageParam, flow);
                }
        }

        /** When the config is already loaded, find the default page to load */
        loadDefaultPageForConfig(flowName: string) {
                let baseUrl = ServiceConstants.PLATFORM_BASE_URL;
                baseUrl += '/' + flowName;
                this.getPageToNavigateTo('next', baseUrl);
                let viewRoot: ViewRoot = this.configService.getFlowConfig(flowName);
                let model: Model = viewRoot.model;
                this.navigateToDefaultPageForFlow(model, flowName);
        }

        navigateToPage(pageParam: Param, flow: string) {
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

                if (flowName.indexOf(':') !== -1) {
                        flowName = flowName.substr(0, flowName.indexOf(':'));
                }

                let page: Param;
                let viewRoot: ViewRoot = this.configService.getFlowConfig(flowName);
                if (viewRoot) {
                        let flowConfig: Model = viewRoot.model;
                        if (flowConfig != null && flowConfig.params != null) {
                                flowConfig.params.forEach(pageParam => {
                                        if (pageParam.config.uiStyles != null && pageParam.config.uiStyles.name === 'ViewConfig.Page') {
                                                if (pageParam.config.code === pageId) {
                                                        page = pageParam;
                                                }
                                        }
                                });
                        }
                }
                if (!page) {
                        this.logError('Page Configuration not found for Page ID: ' + pageId + ' in Flow: ' + flowName);
                }
                return page;
        }

        /** Process execute call - TODO revisit to make it more dynamic based on url completion */
        processEvent(processUrl: string, behavior: string, model: GenericDomain, method: string) {
                // if (model!=null && model['id']) {
                //         this.entityId = model['id'];
                // }
                processUrl = processUrl + '/' + Action._get.value;
                let url = '';
                let serverUrl = '';
                let flowName = '';
                if (behavior == undefined) {
                        behavior = Behavior.execute.value;
                }
                flowName = processUrl.substring(1, processUrl.indexOf('/', 2)); //TODO
                url = ServiceConstants.PLATFORM_BASE_URL;
                serverUrl = url + processUrl.replace('{id}', this.entityId.toString());

                if (processUrl.indexOf('?') > 0) {
                        url = url + processUrl.replace('{id}', this.entityId.toString()) + '&b=' + behavior;
                } else {
                        url = url + processUrl.replace('{id}', this.entityId.toString()) + '?b=' + behavior;
                }
                //}

                let rootDomainId = this.getFlowRootDomainId(flowName);
                if (rootDomainId != null) {
                        let flowNameWithId = flowName.concat(':' + rootDomainId);
                        url = url.replace(flowName, flowNameWithId);
                }
                this.showLoader();
                if (method !== '' && method.toUpperCase() === HttpMethod.GET.value) {
                        // if(behavior =='$config'){
                        //         this.router.navigate(['Patientenrollment',{domain:"patientenrollment"}]);
                        // }else{

                        this.http.get(url)
                                .subscribe(data => {
                                        this.processResponse(data.result, serverUrl, flowName);
                                },
                                err => {
                                        this.logError(err);
                                        this.hideLoader();
                                        },
                                () => {
                                        console.log('Process Execution query completed..');
                                        this.hideLoader();
                                }
                                );
                        //}
                } else if (method !== '' && method.toUpperCase() === HttpMethod.POST.value) {

                        // console.log('payload - ' + json);
                        this.http.post(url, JSON.stringify(model))
                                .subscribe(data => {
                                        this.processResponse(data.result, serverUrl, flowName);
                                },
                                err => {this.logError(err);
                                        this.hideLoader();
                                        },
                                () => {
                                        console.log('Process Execution query completed..');
                                        this.hideLoader();
                                }
                                );
                } else {
                        throw 'http method not supported';
                }
        }

        /** Process execute call - TODO revisit to make it more dynamic based on url completion */
        processPost(processUrl: string, behavior: string, payload: any, method: string, navLink?: string) {
                let flowName = processUrl.substring(1, processUrl.indexOf('/', 2)); //TODO
                let url = ServiceConstants.PLATFORM_BASE_URL + processUrl + '/' + Action._get.value;

                if (method !== '' && method.toUpperCase() === HttpMethod.POST.value) {
                        
                        this.http.post(url, JSON.stringify(payload))
                                .subscribe(data => {
                                        this.processResponse(data.result, url, flowName);
                                        if (navLink != null && navLink !== '') {
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
                path = path;
                let flowName = path.substring(1, path.indexOf('/', 2));
                let rootDomainId = this.getFlowRootDomainId(flowName);
                if (rootDomainId != null) {
                        let flowNameWithId = flowName.concat(':' + rootDomainId);
                        path = path.replace(flowName, flowNameWithId);
                }
                // console.log('Post on change :: ' + path);
                let modelEvnt = new ModelEvent();
                modelEvnt.type = Action._update.value; //use the server side actions like _new, _update
                modelEvnt.id = path;
                modelEvnt.value = payloadValue;
                modelEvnt.payload = payloadValue;
                //  modelEvnt.value.path = path;
                // console.log(JSON.stringify(modelEvnt));
                var urlBase = ServiceConstants.PLATFORM_BASE_URL;
                var url = urlBase + '/event/notify';
                //if (action === Action._update.value) {
                console.log('Post update Event Notify call');
                return this.http.post(url, JSON.stringify(modelEvnt))
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
                let viewRoot: ViewRoot = this.configService.getFlowConfig(flowName);
                let flowConfig: Model = viewRoot.model;
                if (flowConfig) {
                        this.traverseConfig(flowConfig.params, eventModel);
                }
        }

        getUpdatedParamPath(eventModelPath: string): string {
                // Remove path before '/p/' which is the platform path
                //let updatedParamPath = eventModelPath.substring(eventModelPath.indexOf('/p/')+3);
                // Remove flow name which is the beginning of the event path
                let updatedParamPath = eventModelPath.substring(eventModelPath.indexOf('/', 1) + 1);
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
                                        if (element && element.config && element.config.code === paramTree[node]) {
                                                if ((element.config.type && element.config.type.collection) || node >= numNodes) {
                                                        // Matching param node OR Collection node. Collection nodes cannot be traversed further.
                                                        this.processModelEvent(element, eventModel);
                                                } else {
                                                        // Lets go futher into the matching param to find the exact match
                                                        this.traversePageConfig(element, eventModel, node);
                                                }

                                                if (node < numNodes) {
                                                } else {
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
        createRowData(param: Param, nestedParamIdx: number) {
                let rowData: any = param.leafState;

                // If nested data exists, set the data to nested grid
                if (nestedParamIdx && param.type.model.params) {
                        rowData['nestedGridParam'] = param.type.model.params[nestedParamIdx];
                }

                return rowData;
        }

        /** 
         * Loop through the Param State and build the Grid
         * 
         */
        createGridData(gridElementParams: Param[], gridParam: Param) {
                let nestedParams = gridParam.config.type.elementConfig.type.model.paramConfigs;
                let gridData = [];
                let paramState = [];
                // Look for inner lists (nested grid)
                let nestedParamIdx: number;
                if (nestedParams) {
                        for (let p in nestedParams) {
                                if (nestedParams[p].uiStyles && nestedParams[p].uiStyles.name == 'ViewConfig.GridRowBody') {
                                        nestedParamIdx = +p;
                                        break;
                                }
                        }
                }
                if (gridElementParams) {
                        gridElementParams.forEach(param => {
                                let p = new Param(this.configService).deserialize(param);
                                if(p != null) {
                                        paramState.push(p.type.model.params);
                                        gridData.push(this.createRowData(p, nestedParamIdx));
                                }
                        });        
                }
                gridParam['paramState'] = paramState;
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
                        if (eventModel.value.leafState != null) {
                                // Check if the update is for the Current Collection or a Nested Collection
                                if (param.path == eventModel.value.path) {
                                        // Current Collection
                                        // Collection Element Check - update only the element
                                        if (eventModel.value.collectionElem) {
                                                if (param.gridList == null) {
                                                        param.gridList = this.createGridData(eventModel.value.type.model.params, param);
                                                } else {
                                                        param.gridList.push(this.createGridData(eventModel.value.type.model.params, param));
                                                }
                                                this.gridValueUpdate.next(param);
                                        }
                                        // Collection check - replace entire grid
                                        if (eventModel.value.collection) {
                                                param.gridList = this.createGridData(eventModel.value.type.model.params, param);
                                                this.gridValueUpdate.next(param);
                                        }
                                } else { // Nested Collection. Need to traverse to right location
                                        let nestedPath = eventModel.value.path.substr(param.path.length + 1);
                                        let elemIndex = nestedPath.substr(0, nestedPath.indexOf('/'));
                                        for (var p = 0; p < param.gridList.length; p++) {
                                                if (param.gridList[p]['elemId'] == elemIndex) {
                                                        // console.log(param.config.gridList[p]['nestedElement']);
                                                        let nestedElement = this.getNestedElementParam(param.gridList[p]['nestedElement'], nestedPath);
                                                        if (nestedElement) {
                                                                nestedElement['gridList'] = this.createGridData(eventModel.value.type.model.params, nestedElement);
                                                                this.gridValueUpdate.next(nestedElement);
                                                        } else {
                                                                console.log('Nested Grid Element not found.');
                                                        }
                                                        break;
                                                }
                                        }

                                        // console.log('eventModel');
                                }
                        }
                } else if (param.config.uiStyles != null && param.config.uiStyles.attributes.alias === 'CardDetailsGrid') {
                        if (param.type.collection === true) {
                                let payload: Param = new Param(this.configService).deserialize(eventModel.value);
                                param.type.model['params'] = payload.type.model.params;
                        } else {
                                this.traverseParam(param, eventModel);
                        }
                } else {
                        this.traverseParam(param, eventModel);
                }
        }

        getNestedElementParam(nestedElement: Param, nestedPath: string): Param {
                let paramTree = nestedPath.split('/');
                let startIndex = 1;

                if (this.matchNode(nestedElement, paramTree[startIndex])) {
                        return this.traverseNestedPath(nestedElement, startIndex + 1, paramTree);
                } else {
                        return undefined;
                }
        }

        traverseNestedPath(nestedElement: Param, index: number, tree: string[]): Param {
                for (var p = 0; p < nestedElement.type.model.params.length; p++) {
                        let element = nestedElement.type.model.params[p];
                        if (this.matchNode(element, tree[index])) {
                                if (index == tree.length - 1) {
                                        return element;
                                } else {
                                        return this.traverseNestedPath(element, index + 1, tree);
                                }
                        }
                }
                return undefined;
        }

        matchNode(element: Param, node: string): boolean {
                if (element && element.config.code == node) {
                        return true;
                } else {
                        return false;
                }
        }

        /** Update param with value */
        traverseParam(param: Param, eventModel: ModelEvent) {
                /* Flow-Wrapper class also invokes methods that eventually call this behaviour. We need to make sure that the eventModel is deserialized by then */
                let payload: Param = new Param(this.configService).deserialize(eventModel.value);
                if (param.type.nested === true) {
                        this.updateParam(param, payload);
                        if (param.type.model && payload.type.model && payload.type.model.params) {
                                for (var p in param.type.model.params) {
                                                this.updateParam(param.type.model.params[p], payload.type.model.params[p]);
                                }
                        }
                        if (param.type.model === undefined && payload.type.model) {
                                param.type['model'] = payload.type.model;
                        }
                        if (param.message === undefined && payload.message) {
                                param['message'] = payload.message;
                        }
                } else {                        
                        this.updateParam(param, payload);
                }
        }

        updateParam(param: Param, payload: Param) {
                let result: any[] = Reflect.ownKeys(param);
                let updatedKeys: any[] = Reflect.ownKeys(payload);
                let config = this.configService.getViewConfigById(payload.configId);
                //By this time the config for the parameter is set in the config map as it is deserelized
                if(config != null && config != undefined) {
                        updatedKeys.forEach(updatedKey => {
                                result.forEach(currentKey => {
                                        if (currentKey === updatedKey) {
                                                try {
                                                        if (Reflect.ownKeys(Reflect.get(param, currentKey)) != null
                                                                && Reflect.ownKeys(Reflect.get(param, currentKey)).length > 0) {
                                                                if (currentKey === 'type') {
                                                                        //Object.assign(Reflect.get(param, currentKey), new Type().deserialize(Reflect.get(payload, updatedKey)));
                                                                } else if (currentKey === 'config') {
                                                                        Object.assign(Reflect.get(param, currentKey), new ParamConfig(this.configService).deserialize(Reflect.get(payload, updatedKey)));
                                                                        //TODO - refactor this whole method and the conditions. Revisit - order, count
                                                                } else if (currentKey === 'activeValidationGroups') {
                                                                        Reflect.set(param, currentKey, Reflect.get(payload, updatedKey));
                                                                        this.validationUpdate.next(param);
                                                                        //TODO - refactor this whole method and the conditions. Revisit - order, count
                                                                } else if (currentKey === 'leafState' || currentKey === 'message') {
                                                                        Reflect.set(param, currentKey, Reflect.get(payload, updatedKey));
                                                                        this.eventUpdate.next(param);
                                                                } else if (currentKey === 'enabled' || currentKey === 'visible') {
                                                                        Reflect.set(param, currentKey, Reflect.get(payload, updatedKey));
                                                                        this.validationUpdate.next(param);
                                                                } else if (currentKey === 'values') {
                                                                        if (param.values == null) {
                                                                                let values = [];
                                                                                values.push(Reflect.get(payload, updatedKey)) ;
                                                                                param.values = values;
                                                                                //this.eventUpdate.next(param);
                                                                        } else {
                                                                                param.values = Reflect.get(payload, updatedKey);
                                                                                //this.eventUpdate.next(param);
                                                                        }
                                                                }
                                                        } else if (Reflect.ownKeys(Reflect.get(param, currentKey)).length == 0 && currentKey === 'leafState') {
                                                                Reflect.set(param, currentKey, Reflect.get(payload, updatedKey));
                                                                this.eventUpdate.next(param);
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
                } else {
                        this.logError('Could not process the update from the server for ' + payload.path + ' because config is undefined.');
                }
        }

        /** Get flow configuration to create pages. */
        getPageConfig() {
                let url = this.buildFlowBaseURL();
                // TODO - temp to handle subflows
                if (this.routeParams.subflow === true) {
                        url = url + '/_landing/_process?b=$executeAnd$config';
                } else {
                        url = url + '/_new?b=$config';
                }

                this.http.get(url)
                        .subscribe(data => {
                                // console.log('Creating a global page map');
                                this.processResponse(data.result, '', '');
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
