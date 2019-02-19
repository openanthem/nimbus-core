
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
import { ModelEvent, Page, Result, ViewRoot } from '../shared/app-config.interface';
import { Param, Model, GridPage } from '../shared/param-state';
import { CustomHttpClient } from './httpclient.service';

import { Subject } from 'rxjs';
import { GenericDomain } from '../model/generic-domain.model';
import { RequestContainer } from '../shared/requestcontainer';
import { ExecuteException } from './../shared/app-config.interface';
import { ParamUtils } from './../shared/param-utils';
import { ParamAttribute } from './../shared/command.enum';
import { ViewConfig } from './../shared/param-annotations.enum';
import { LoggerService } from './logger.service';
import { SessionStoreService } from './session.store';
import { Location } from '@angular/common';
import { ViewComponent } from '../shared/param-annotations.enum';
import { GridData } from './../shared/param-state';
import { Message } from './../shared/message';
import { ComponentTypes } from './../shared/param-annotations.enum';
import { DataGroup } from '../components/platform/charts/chartdata';
import { NmMessageService } from './toastmessage.service';
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
        subdomainconfig$: EventEmitter<any>;
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

        postResponseProcessing = new Subject<string>();
        postResponseProcessing$ = this.postResponseProcessing.asObservable();

        private requestQueue :RequestContainer[] = [];

        private _entityId: number = 0;
        constructor(private http: CustomHttpClient, private loaderService: LoaderService, private configService: ConfigService, 
                    private logger: LoggerService, private sessionStore: SessionStoreService, private location: Location, private toastService: NmMessageService) {
                // initialize
                this.flowRootDomainId = {};
                // Create Observable Stream to output our data     
                this.config$ = new EventEmitter();
                this.subdomainconfig$ = new EventEmitter();
                this.layout$ = new EventEmitter();
        }

        ngOnInit() {
        }

        get entityId(): number {
                return this._entityId;
        }
        set entityId(id: number) {
                this._entityId = id;
        }

        logError(err) {
                this.logger.error('ERROR: Failure making server call : ' + JSON.stringify(err));
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
                return this.sessionStore.get(flowName);
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
                let flowRoodtId = this.sessionStore.get(flowName);
                let url = '';
                if(flowRoodtId != null) {
                        let rootId = flowRoodtId;
                        baseUrl += rootId == null ? '/' + flowName:  '/' + flowName+':'+rootId;
                        let action = rootId!= null ? Action._get.value: Action._new.value;
                        url = baseUrl + '/' + action + '?b=' + Behavior.execute.value;   
                        this.executeHttp(url, HttpMethod.GET.value, null);    
                } else {
                        baseUrl += '/' + flowName;
                        url = baseUrl + '/' + Action._new.value + '?b=' + Behavior.execute.value;  
                        this.executeHttp(url, HttpMethod.GET.value, null);
                }
        }

        /** Get Flow Config - TODO delete after routes refactor. the above method will be used instead of this one.*/
        loadFlowConfig(routeParms: any) {
                let baseUrl = ServiceConstants.PLATFORM_BASE_URL;
                baseUrl += '/' + routeParms.domain;
                let url = baseUrl + '/' + Action._new.value + '?b=' + Behavior.execute.value;
                this.executeHttp(url, HttpMethod.GET.value, null);
        }

        /** Page id - to know which page to load from the config */
        getPageToNavigateTo(direction: string, useFlowUrl: string) {
                let flowName = useFlowUrl.substring(useFlowUrl.indexOf('flow_'));
                if (flowName.indexOf('/') > 0) {
                        flowName = flowName.substring(0, flowName.indexOf('/'));
                }
                let url = useFlowUrl;
                url = url + '/_nav?a=' + direction + '&b=$execute';
                this.executeHttp(url, HttpMethod.GET.value, null);
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
        processResponse(response: any) {
                let index = 0;
                let rootParam: Param;
                while (response[index]) {
                        let subResponse: any = response[index];
                        if (subResponse.b === Behavior.execute.value) {
                                let flowConfig = new Result(this.configService).deserialize(subResponse.result);
                                if(flowConfig && flowConfig.inputCommandUri) {
                                        rootParam = this.findParamByCommandUri(flowConfig.inputCommandUri);
                                }
                                this.traverseOutput(flowConfig.outputs, rootParam);
                        } else {
                                this.logError('Unknown response for behavior - ' + subResponse.b);
                        }
                        index++;
                }
        }

        /**
         * 
         * @param inputCommandUri - takes the input as the input command uri from a result
         * Ex: /client/org/p/flowname:{id}/pageId/tile/section/_get
         * @returns Param - Param of the input command path i.e /flowname/pageId/tile/section
         */
        findParamByCommandUri(inputCommandUri: string): Param {
                let rootParam: Param;
                let truncatedBasePath: string;
                // truncate everything before /p/ including /p
                truncatedBasePath = inputCommandUri.substring(inputCommandUri.indexOf('/p/', 1) + 2, inputCommandUri.length);
                // truncate action at the end
                let action: string;
                if (truncatedBasePath.indexOf(Action._get.value) > 0 ) {
                        action = '/' + Action._get.value;
                } else if (truncatedBasePath.indexOf(Action._update.value) > 0 ) {
                        action = '/' + Action._update.value;
                } else if (truncatedBasePath.indexOf(Action._nav.value) > 0 ) {
                        action = '/' + Action._nav.value;
                } else if (truncatedBasePath.indexOf(Action._new.value) > 0 ) {
                        action = '/' + Action._new.value;
                }

                truncatedBasePath = truncatedBasePath.substring(0, truncatedBasePath.indexOf(action));
                // remove the rootDomainid
                let domainFlowPath = '';
                if (truncatedBasePath.indexOf(':') > 0) {
                        domainFlowPath = truncatedBasePath.substring(0, truncatedBasePath.indexOf(':'));
                } else {
                        domainFlowPath = truncatedBasePath.substring(0, truncatedBasePath.indexOf(ServiceConstants.PATH_SEPARATOR, 1));
                }

                // tslint:disable-next-line:max-line-length
                let pagePath = truncatedBasePath.substring(truncatedBasePath.indexOf(ServiceConstants.PATH_SEPARATOR, 1),truncatedBasePath.length);

                const paramPath = domainFlowPath + pagePath;

                // Now that we have the clean path, we can get the param for this path
                rootParam = this.findParamByAbsolutePath(paramPath);

                return rootParam;
        }
        /**
         * @param path - input parameter which takes the complete path of the param without any refId
         * @returns param - Returns the param corresponding to the path sent
         */
        findParamByAbsolutePath(path: string): Param {
                let rootParam: Param;
                let relativeParamPath: string;

                // Get the flow name based on the path
                const flowName = this.getFlowNameFromPath(path);
                // path always starts with flow name
                let nodes: string[] = path.split(ServiceConstants.PATH_SEPARATOR);
                // given the path is /flowname/pageId/...
                const ROOT_NODE = 1;
                const PAGE_NODE = ROOT_NODE + 1;
                const pageId = nodes[PAGE_NODE];
                const pageParam: Param = this.findMatchingPageConfigById(pageId, flowName);
                if (pageParam) {
                        // This will find the param using the relative path
                        relativeParamPath = nodes.slice(PAGE_NODE + 1).join(ServiceConstants.PATH_SEPARATOR);
                        rootParam = ParamUtils.findParamByPath(pageParam, relativeParamPath);
                }

                // making sure we indeed got the right param
                if (rootParam && rootParam.path === path) {
                        return rootParam;
                }
                return null;
        }

        getFlowNameFromOutput(paramPath: string): string {
                if(paramPath == undefined) {
                        return '';
                }
                let flow = this.getFlowNameFromPath(paramPath);
                if (flow && flow.indexOf(':') > 0) {
                        flow = flow.substr(0, flow.indexOf(':'));
                }
                return flow;
        }

        getDetaultPageForFlow(model: Model): Param {
                if (model != null && model.params != null) {
                        for (var p in model.params) {
                                let pageParam: Param = model.params[p];
                                if (pageParam != null && pageParam.config.uiStyles != null && pageParam.config.uiStyles.name ===  ViewConfig.page.toString()) {
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
         * @param rootParam - optional parameter. It is the Param of the attribute from where http call is initiated
         */
        traverseOutput(outputs: Result[], rootParam?: Param) {                
                /** Check for Nav Output. Execute Nav after processing all other outputs. */
                var navToDefault = true;
                let navOutput: Result = undefined;
                let navToBrowserBack = false;

                /** If rootParam from which the http call is made is configured with browserback navigation,
                 *  set navToDefault to false, and browserback navigation superceds other nav calls
                 */
                if (rootParam && rootParam.config && 
                        rootParam.config.uiStyles && 
                        rootParam.config.uiStyles.attributes && 
                        rootParam.config.uiStyles.attributes.browserBack === true) {

                        navToDefault = false;
                        navToBrowserBack = true;
                }
                outputs.forEach(otp => {
                        if (otp.action === Action._nav.value) {
                                navToDefault = false;
                                navOutput = otp;
                        }
                });
                outputs.forEach(output => {
                        if (output.value == null || ParamUtils.isEmpty(output.value)) {
                                // Since this is for inneroutput, there is no need to send the root param again
                                this.traverseOutput(output.outputs);
                        } else {
                                if (output.action === Action._new.value) {
                                        let flow = this.getFlowNameFromOutput(output.value.path);
                                        // Check if the _new output is for the Root Flow
                                        if (output.value.path == "/" + flow) {
                                                this.setViewRootAndNavigate(output,flow,navToDefault,true);
                                        } else {
                                                let eventModel: ModelEvent = new ModelEvent().deserialize(output);
                                                this.traverseFlowConfig(eventModel, flow);
                                        }

                                } else if (output.action === Action._get.value) {
                                        if (output.value.config && output.value.type && output.value.type.model) {
                                                let refresh = false;

                                                if(ParamUtils.isEmpty(this.configService.flowConfigs)) {
                                                        refresh = true;
                                                }
                                                let flow = this.getFlowNameFromOutput(output.value.path);
                                                this.setViewRootAndNavigate(output,flow,navToDefault,refresh);

                                        } else {
                                                this.logger.warn('Received a _get call without model or config ' + output.value.path);
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
                if (navToBrowserBack) {
                        this.logger.debug('Navigation using browser back location');
                        this.location.back();
                } else if (navOutput) {
                        let flow = this.getFlowNameFromOutput(navOutput.inputCommandUri);
                        let pageParam = this.findMatchingPageConfigById(navOutput.value, flow);
                        this.navigateToPage(pageParam, flow);
                }
        }

        setViewRootAndNavigate(output: Result, flow:string, navToDefault:boolean, refreshLayout:boolean) {
                let viewRoot: ViewRoot = new ViewRoot();
                                                
                viewRoot.model = output.value.type.model;
                this.configService.setLayoutToAppConfig(flow, viewRoot);

                if (output.rootDomainId !== 'null' ) {
                        this.flowRootDomainId[flow] = output.rootDomainId;
                        this.sessionStore.set(flow, output.rootDomainId);
                }
                // Check if there is a layout for this domain
                if (output.value.config.type.model.uiStyles) {
                        viewRoot.layout = output.value.config.type.model.uiStyles.attributes.layout;
                       // if(refreshLayout) {
                                this.layout$.emit(viewRoot.layout);
                       // }
                }
                if (navToDefault) {
                        this.navigateToDefaultPageForFlow(output.value.type.model, flow);
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
                // if(flow !== 'cmcaseview' && flow !== 'vrCSLandingPage' && flow !== 'home') {
                //         this.subdomainconfig$.next(page);
                // } else {
                //         this.config$.next(page);
                // }
                this.config$.next(page);
        }

        getPageConfigById(pageId: string, flowName: string): Promise<Param> {
                return Promise.resolve(this.findMatchingPageConfigById(pageId, flowName));
        }

        /** Get the page config matching the page ID and flow Name */
        findMatchingPageConfigById(pageId: string, flowName: string): Param {

                if (flowName && flowName.indexOf(':') !== -1) {
                        flowName = flowName.substr(0, flowName.indexOf(':'));
                }

                let page: Param;
                let viewRoot: ViewRoot = this.configService.getFlowConfig(flowName);
                if (viewRoot) {
                        let flowConfig: Model = viewRoot.model;
                        if (flowConfig != null && flowConfig.params != null) {
                                flowConfig.params.forEach(pageParam => {
                                        if (pageParam.config.uiStyles != null && pageParam.config.uiStyles.name === ViewConfig.page.toString()) {
                                                if (pageParam.config.code === pageId) {
                                                        page = pageParam;
                                                }
                                        }
                                });
                        }
                }
                if (!page) {
                        this.logger.debug('Page Configuration not found for Page ID: ' + pageId + ' in Flow: ' + flowName);
                }
                return page;
        }

        /** Process execute call - TODO revisit to make it more dynamic based on url completion */
        processEvent(processUrl: string, behavior: string, model: GenericDomain, method: string, queryParams?: string) {
                let path = processUrl;
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
                if (queryParams) {
                        url = url + queryParams;
                }

                let rootDomainId = this.getFlowRootDomainId(flowName);
                if (rootDomainId != null) {
                        let flowNameWithId = flowName.concat(':' + rootDomainId);
                        url = url.replace(flowName, flowNameWithId);
                }
                this.executeHttp(url, method, model, path);
        }

        executeHttp(url:string, method : string, model:any, paramPath?: string) {
                this.showLoader();
                this.logger.info('http call' + url + 'started');
                if (method !== '' && method.toUpperCase() === HttpMethod.GET.value) {
                        this.executeHttpGet(url, paramPath)
                 } else if (method !== '' && method.toUpperCase() === HttpMethod.POST.value) {
                         this.executeHttpPost(url, model, paramPath);
                 } else {
                         this.invokeFinally(url);
                         this.logger.error('http method not supported');
                 }
        }
        executeHttpGet(url, paramPath?: string) {
                this.http.get(url).subscribe(
                        data => { 
                                this.sessionStore.setSessionId(data.sessionId);
                                this.processResponse(data.result); 
                        },
                        err => { this.processError(err, paramPath); },
                        () => { this.invokeFinally(url, paramPath); }
                        );
        }

        executeHttpPost(url:string, model:GenericDomain, paramPath?: string) {
                this.http.post(url, JSON.stringify(model)).subscribe(
                        data => { 
                                this.sessionStore.setSessionId(data.sessionId);
                                this.processResponse(data.result);
                        },
                        err => { this.processError(err, paramPath); },
                        () => { this.invokeFinally(url, paramPath); }
                        );
        }

        processError(err:any, paramPath?: string) {
                if(paramPath) {
                        this.postResponseProcessing.next(paramPath);
                }
                this.logError(err);
                this.hideLoader();
        }

        invokeFinally(url:string, paramPath?: string) {
                if(paramPath){
                        this.postResponseProcessing.next(paramPath);
                }
                this.logger.info('http response for ' + url + ' processed successsfully');
                this.hideLoader();
        }

        postOnChange(path: string, payloadAttr: string, payloadValue: string) {
                path = path;
                let flowName = path.substring(1, path.indexOf('/', 2));
                let rootDomainId = this.getFlowRootDomainId(flowName);
                if (rootDomainId != null) {
                        let flowNameWithId = flowName.concat(':' + rootDomainId);
                        path = path.replace(flowName, flowNameWithId);
                }

                let modelEvnt = new ModelEvent();
                modelEvnt.type = Action._update.value; //use the server side actions like _new, _update
                modelEvnt.id = path;
                modelEvnt.value = payloadValue;
                modelEvnt.payload = payloadValue;
             
                var urlBase = ServiceConstants.PLATFORM_BASE_URL;
                var url = urlBase + '/event/notify';

                this.logger.info('Post update Event Notify call started for '+ url);
                return this.executeHttp(url, HttpMethod.POST.value, modelEvnt);
                
        }

        /**
         * Loop through all Configs to find the match to update the Model Event
         */
        traverseFlowConfig(eventModel: ModelEvent, flowName: string) {
                let viewRoot: ViewRoot = this.configService.getFlowConfig(flowName);
                if(viewRoot == undefined) {
                        this.logger.warn("Response cannot be processed for the path " + eventModel.value.path + " as there is no get/new done on the viewroot "+flowName);
                } else {
                        let flowConfig: Model = viewRoot.model;
                        if(eventModel.value.path == '/'+flowName && eventModel.value.message) {
                                this.toastService.emitMessageEvent(eventModel.value.message);
                        }
                        if (flowConfig) {
                                this.traverseConfig(flowConfig.params, eventModel);
                        }
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
                        if(rootParam.path === eventModel.value.path) {
                                this.processModelEvent(rootParam, eventModel);
                        }
                        // Loop through the Params in the Page
                        else if (rootParam.type.model && rootParam.type.model.params) {
                                rootParam.type.model.params.forEach(element => {
                                        // Check if param matches the updated param path
                                        if (element && element.config && element.config.code === paramTree[node]) {
                                                const test = ((element.config.type && element.config.type.collection));
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
                        } else {
                                this.logger.warn('event update with path '+ eventModel.value.path +   ' did not match with any param of current domain')
                        }
                }
        }

        /**
         * Loop through the Param State and build the Grid
         *
         */
        createGridData(gridElementParams: Param[], gridParam: Param): GridData {
                let gridData = [];
                let collectionParams = [];
                // contains state data for the grid (other than leafState (e.g. style, etc.))
                let gridStateMap = [];

                if (gridElementParams) {
                        gridElementParams.forEach(param => {
                                let p = new Param(this.configService).deserialize(param, gridParam.path);
                                if (p != null) {
                                        // build the gridList data
                                        if (p.leafState && p.leafState.nestedGridParam) {
                                                collectionParams = collectionParams.concat(p.leafState.nestedGridParam);
                                        }
                                        // let leafState = p.leafState;
                                        // delete leafState.nestedGridParam;
                                        // gridData.push(leafState);
                                        gridData.push(p.leafState);

                                        // build the gridRowConfig data (other stateful data)
                                        let rowStateData = {};
                                        if (p.leafState) {
                                                for (let cellParam of p.type.model.params) {
                                                        let cellStateData = this.configService.getViewConfigById(cellParam.configId);
                                                        rowStateData[cellStateData.code] = {
                                                                style: cellParam.style
                                                        };
                                                }
                                        }
                                        gridStateMap.push(rowStateData);
                                }       
                        });
                }
                gridParam['collectionParams'] = collectionParams;
                
                return {
                        leafState: gridData,
                        stateMap: gridStateMap
                } as GridData;
        }

        /**
         * Process the Event by updating the Param
         * param: Param to be updated
         * eventModel: Event received
         */
        processModelEvent(param: Param, eventModel: ModelEvent) {
                // Grid updates
                if (param.config.uiStyles != null && (param.config.uiStyles.attributes.alias === ViewComponent.grid.toString() || param.config.uiStyles.attributes.alias === ViewComponent.treeGrid.toString())) {
                        if (eventModel.value != null) {
                                // Check if the update is for the Current Collection or a Nested Collection
                                if (param.path == eventModel.value.path) {
                                        // Current Collection
                                        // Collection Element Check - update only the element
                                        if (eventModel.value.collectionElem) {
                                                if (param.gridData.leafState == null) {
                                                        param.gridData = this.createGridData(eventModel.value.type.model.params, param);
                                                } else {
                                                        param.gridData.leafState.push(this.createGridData(eventModel.value.type.model.params, param).leafState);
                                                }
                                                this.gridValueUpdate.next(param);
                                        }
                                        // Collection check - replace entire grid
                                        if (param.config.type.collection) {
                                                if (eventModel.value.page) {
                                                        let page: GridPage = new GridPage().deserialize(eventModel.value.page);
                                                        param.page = page;
                                                }
                                                param.gridData = this.createGridData(eventModel.value.type.model.params, param);
                                                this.gridValueUpdate.next(param);
                                        }
                                        //handle visible, enabled, activatevalidationgroups - the state above will always run if visible is true or false
                                        let responseGridParam: Param = new Param(this.configService).deserialize(eventModel.value, eventModel.value.path);
                                        let config = this.configService.getViewConfigById(responseGridParam.configId);
                                        if(config != null && config != undefined) {
                                                this.replaceSourceParamkeys(param,responseGridParam);
                                        }
                                } else { // Nested Collection. Need to traverse to right location
                                        let nestedPath = eventModel.value.path.substr(param.path.length + 1);
                                        let elemIndex = nestedPath.substr(0, nestedPath.indexOf('/'));
                                        if( param.gridData.leafState && eventModel.value.type && eventModel.value.type.model) {
                                                for (var p = 0; p < param.gridData.leafState.length; p++) {
                                                        if (param.gridData.leafState[p]['elemId'] == elemIndex) {
                                                                let nestedElement = this.getNestedElementParam(param.gridData.leafState[p]['nestedElement'], nestedPath, eventModel.value.path);
                                                                if (nestedElement && nestedElement.gridData) {
                                                                        nestedElement['gridData'] = this.createGridData(eventModel.value.type.model.params, nestedElement);
                                                                        this.gridValueUpdate.next(nestedElement);
                                                                }
                                                                // if nestedElement is not present, we do not need to handle this scenario.
                                                                break;
                                                        }
                                                }
                                        }
                                }
                        }
                } else if (param.config.uiStyles != null && param.config.uiStyles.attributes.alias === ViewComponent.cardDetailsGrid.toString()) {
                        if (param.config.type.collection === true) {
                                let payload: Param = new Param(this.configService).deserialize(eventModel.value, eventModel.value.path);
                                if(payload.type.model && payload.path == param.path) // TODO - need to handle updates for each collection item in a card detail grid
                                        param.type.model['params'] = payload.type.model.params;
                        } else {
                                this.traverseParam(param, eventModel);
                        }
                } else {
                        this.traverseParam(param, eventModel);       
                }
        }

        getNestedElementParam(nestedElement: Param, nestedPath: string, eventPath: string): Param {
                let paramTree = nestedPath.split('/');
                let startIndex = 1;

                if (this.matchNode(nestedElement, paramTree[startIndex])) {
                        return this.traverseNestedPath(nestedElement, startIndex + 1, paramTree, eventPath);
                } else {
                        return undefined;
                }
        }

        matchElementId (nestedElement:Param, elemId:string){
                if (nestedElement.elemId && nestedElement.elemId == elemId){
                        return true;
                }
                return false;
        }

        traverseNestedPath(nestedElement: Param, index: number, tree: string[], eventPath: string): Param {
                if (!nestedElement) {
                        return nestedElement;
                }
                for (let p = 0; p < nestedElement.type.model.params.length; p++) {
                        const element = nestedElement.type.model.params[p];

                        // find's and return the element based on the nestedelement.config.code and index
                        if (this.matchNode(element, tree[index])) {
                                let matchFoundOnGrid = false;
                                if (element.gridData && element.gridData.leafState && element.gridData.leafState.length >0){
                                        // if there is a gridlist, match the elemntId
                                        // and look into nested element of the gridlist.
                                        for(let  i = 0; i < element.gridData.leafState.length; i++){
                                                if (this.matchElementId(element.gridData.leafState[i], tree[index+1])){
                                                                matchFoundOnGrid = true;
                                                                index += 2; // skip the elementID in the path and the curentElements cnfig.code
                                                                return  this.traverseNestedPath(element.gridData.leafState[i].nestedElement, index + 1, tree, eventPath);
                                                }
                                        }
                                }
                                if (!matchFoundOnGrid){
                                        if (index === tree.length - 1 && element.path === eventPath) {
                                                return element;
                                        } else {
                                                return this.traverseNestedPath(element, index + 1, tree, eventPath);
                                        }
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
                let payload: Param = new Param(this.configService).deserialize(eventModel.value, eventModel.value.path);
                if(param.path === payload.path) {
                        this.updateParam(param, payload);
                }
        }

        /*
        * Updates the param with the values sent from server and emit the events so that controls that subscribed to these would process
        * accordingly. validation update will switch on/off certain validations and eventUpdate will do a set state on the form control
        */
        updateParam(sourceParam: Param, responseParam: Param) {
                //By this time the config for the parameter is set in the config map as it is deserelized
                let config = this.configService.getViewConfigById(responseParam.configId);
                if(config != null && config != undefined) {
                        //replace the source param with the response key updates 
                        this.replaceSourceParamkeys(sourceParam,responseParam);
                        this.eventUpdate.next(sourceParam); 
                        this.validationUpdate.next(sourceParam);
                        this.updateNestedParameters(sourceParam,responseParam);
                } else {
                        this.logger.debug('Could not process the update from the server for ' + responseParam.path + ' because config is undefined.');
                }
        }

        replaceSourceParamkeys(sourceParam: Param, responseParam: Param) {
                let responseParamKeys: any[] = Reflect.ownKeys(responseParam);
                responseParamKeys.forEach(respKey => { 
                        try { 
                                //config is static and cannot be replaced and nested parameters should be ignored as update is for state of the current param 
                                if(respKey !== ParamAttribute.config.toString() && respKey !== ParamAttribute.type.toString()) 
                                        Reflect.set(sourceParam, respKey, Reflect.get(responseParam, respKey)); 
                        } catch (e) { 
                                this.logger.error(JSON.stringify(e));
                        } 
                });
        }
        
        updateNestedParameters(sourceParam: Param,responseParam: Param) {
                if(sourceParam.type && sourceParam.type.model && responseParam.type && responseParam.type.model) {
                        try {
                                if(sourceParam.type.model.params) {
                                        if(sourceParam.type.model.params.length <  responseParam.type.model.params.length) {
                                                this.logger.warn('Detected new params for  '+ responseParam.path+ ' in the update when sourceParam does not have config');
                                        }
                                        for (var p in sourceParam.type.model.params) {
                                                this.updateParam(sourceParam.type.model.params[p], responseParam.type.model.params[p]);
                                        }
                                }
                        }catch (e) {
                                this.logError('Could not find source param to update the nested payload param path'+ responseParam.path);
                                this.logger.error(JSON.stringify(e));
                        }
                }
        }
        /*
        * show the loader icon the page
        */
        private showLoader(): void {
                this.loaderService.show();
        }

        /*
        * hide the loader icon the page
        */
        private hideLoader(): void {
                this.loaderService.hide();
        }

}