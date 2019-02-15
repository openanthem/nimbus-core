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

import { ParamConfig } from './../shared/param-config';
import { TestBed, async } from '@angular/core/testing';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JL } from 'jsnlog';
import { SESSION_STORAGE, StorageServiceModule } from 'angular-webstorage-service';
import { Location } from '@angular/common';

import { PageService } from './page.service';
import { CustomHttpClient } from './httpclient.service';
import { LoaderService } from './loader.service';
import { ConfigService } from './config.service';
import { LoggerService } from './logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from './session.store';
import { ParamUtils } from './../shared/param-utils';
import { Param, Type, Model, GridPage } from '../shared/param-state';
import { ExecuteException, ModelEvent, ViewRoot, Page } from '../shared/app-config.interface';
import { Message } from './../shared/message';
import { ServiceConstants } from './../services/service.constants';
import { GenericDomain } from '../model/generic-domain.model';
import { pageServiceProcessResponse, pageServiceOutputs, pageServiceRootParam, pageServiceRootParam1, configServiceFlowConfigs, 
    pageServiceModel, pageServiceSetViewRootAndNavigateOutput, pageServiceTraverseParam, pageServiceTraverseParamEventModel, 
    pageServiceTraverseParamPayload, pageServiceCreateGridDataGridElementParams, pageServiceCreateGridDataParam, 
    configServiceParamConfigs, pageServiceCreateGridDataResult, pageServiceTraverseNestedPathResult } from 'mockdata'
import { NmMessageService } from './toastmessage.service';

let http, backend, service, location, loggerService, sessionStoreService, loaderService, configService, configService_actual;

class MockLocation {
  back() { }
}

class MockLoggerService {
  error(a) {}
  info(a) {}
  debug(a) {}
  warn(a) {}
}

class MockSessionStoreService {
  setSessionId(a) {}
  get(a) {
    if (a === 'test1') {
      return null;
    }
    return 'test';
  }
  set(a, b) {}
}

class MockConfigService {

  flowConfigs: any = configServiceFlowConfigs;
  paramConfigs: any = configServiceParamConfigs;

  getFlowConfig(flowName) {    
    if (this.flowConfigs[flowName]) {
            return this.flowConfigs[flowName];
    } else {
            return undefined;
    }
}

  getViewConfigById() {
    return {
      uiStyles: {
        attributes: {}
      }
    }
  };
  setLayoutToAppConfig(a, b) {}
  setViewConfigToParamConfigMap(a,b) {}

}

class MockLoaderService {
  show() {}
  hide() {}
}

describe('PageService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: 'JSNLOG', useValue: JL },
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        { provide: LoggerService, useClass: MockLoggerService },
        { provide: SessionStoreService, useClass: MockSessionStoreService },
        { provide: ConfigService, useClass: MockConfigService },
        { provide: LoaderService, useClass: MockLoaderService },
        { provide: Location, useClass: MockLocation},
        NmMessageService,
        PageService,
        CustomHttpClient
      ],
      imports: [HttpClientTestingModule, HttpModule, StorageServiceModule, HttpClientTestingModule]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(PageService);
    loggerService = TestBed.get(LoggerService);
    sessionStoreService = TestBed.get(SessionStoreService);
    loaderService = TestBed.get(LoaderService);
    configService = TestBed.get(ConfigService);
    location = TestBed.get(Location);
  });

    it('should be created', async(() => {
        expect(service).toBeTruthy();
    }));

    it('entityId property should be updated', async(() => {
        service.entityId = 123;
        expect(service.entityId).toEqual(123);
    }));

    it('logError() should call loggerService.error()', async(() => {
        spyOn(loggerService, 'error').and.callThrough();
        service.logError('test');
        expect(loggerService.error).toHaveBeenCalled();
    }));

    it('getFlowRootDomainId() should return the domainId', async(() => {
        expect(service.getFlowRootDomainId('')).toEqual('test');
    }));

    it('getLayoutConfigForFlow() should call the loadDomainFlowConfig()', async(() => {
        spyOn(service, 'loadDomainFlowConfig').and.callThrough();
        service.getLayoutConfigForFlow('testingFlowName');
        expect(service.loadDomainFlowConfig).toHaveBeenCalledWith('testingFlowName');
    }));

    it('navigateToDefaultPageForFlow() should call getDetaultPageForFlow() and navigateToPage()', async(() => {
        spyOn(service, 'navigateToPage').and.callThrough();
        spyOn(service, 'getDetaultPageForFlow').and.callThrough();
        service.navigateToDefaultPageForFlow(pageServiceModel, 'ownerlandingview');
        expect(service.navigateToPage).toHaveBeenCalledWith(pageServiceModel.params[0], 'ownerlandingview');
        expect(service.getDetaultPageForFlow).toHaveBeenCalledWith(pageServiceModel);
    }));

    it('navigateToDefaultPageForFlow() should not call navigateToPage()', async(() => {
        service.getDetaultPageForFlow = () => {
            return false;
        };
        service.navigateToPage = () => { };
        spyOn(service, 'navigateToPage').and.callThrough();
        service.navigateToDefaultPageForFlow({}, '');
        expect(service.navigateToPage).not.toHaveBeenCalled();
    }));

    it('loadDefaultPageForConfig() should call getPageToNavigateTo() and navigateToDefaultPageForFlow()', async(() => {
        spyOn(service, 'getPageToNavigateTo').and.returnValue('');
        spyOn(service, 'navigateToDefaultPageForFlow').and.returnValue('');
        spyOn(configService, 'getFlowConfig').and.returnValue({ 'model': 'testingModel' });
        service.loadDefaultPageForConfig('t');
        expect(service.getPageToNavigateTo).toHaveBeenCalledWith('next', 'undefined//undefined:undefined/undefinedundefined/p/t');
        expect(service.navigateToDefaultPageForFlow).toHaveBeenCalledWith('testingModel', 't');
        expect(configService.getFlowConfig).toHaveBeenCalledWith('t');
    }));

    it('navigateToPage() should update the config$', async(() => {
        const page: Page = new Page();
        page.pageConfig = {} as Param;
        page.flow = 'testFlow';
        spyOn(service.config$, 'next').and.callThrough();
        service.navigateToPage({}, 'testFlow');
        expect(service.config$.next).toHaveBeenCalledWith(page);
    }));

    it('getPageConfigById() should return true', async(() => {
        spyOn(service, 'findMatchingPageConfigById').and.returnValue(123);
        const result = service.getPageConfigById('pageId', 'flowName');
        result.then(data => {
            expect(data).toEqual(123);
            expect(service.findMatchingPageConfigById).toHaveBeenCalledWith('pageId', 'flowName');
        });
    }));

    it('processError() should call logError() and hideLoader()', async(() => {
        spyOn(service, 'logError').and.callThrough();
        spyOn(service, 'hideLoader').and.callThrough();
        service.processError('testingError');
        expect(service.logError).toHaveBeenCalledWith('testingError');
        expect(service.hideLoader).toHaveBeenCalled();
    }));

    it('processError() should postResponseProcessing subject with given paramPath', async(() => {
        spyOn(service.postResponseProcessing, 'next').and.callThrough();
        service.processError('testingError', 'testingParamPath');
        expect(service.postResponseProcessing.next).toHaveBeenCalledWith('testingParamPath');
    }));

    it('invokeFinally() should call loggerService.info() and hideLoader()', async(() => {
        spyOn(loggerService, 'info').and.callThrough();
        spyOn(service, 'hideLoader').and.callThrough();
        service.invokeFinally('');
        expect(loggerService.info).toHaveBeenCalledWith('http response for  processed successsfully');
        expect(service.hideLoader).toHaveBeenCalled();
    }));

    it('invokeFinally() should call loggerService.info(), hideLoader() and update the postResponseProcessing subject', async(() => {
        spyOn(loggerService, 'info').and.callThrough();
        spyOn(service, 'hideLoader').and.callThrough();
        spyOn(service.postResponseProcessing, 'next').and.callThrough();
        service.invokeFinally('', 'test');
        expect(loggerService.info).toHaveBeenCalledWith('http response for  processed successsfully');
        expect(service.hideLoader).toHaveBeenCalled();
        expect(service.postResponseProcessing.next).toHaveBeenCalledWith('test');
    }));

    it('showLoader() should call loaderService.show()', async(() => {
        spyOn(loaderService, 'show').and.callThrough();
        service.showLoader();
        expect(loaderService.show).toHaveBeenCalled();
    }));

    it('hideLoader() should call loaderService.hide()', async(() => {
        spyOn(loaderService, 'hide').and.callThrough();
        service.hideLoader();
        expect(loaderService.hide).toHaveBeenCalled();
    }));

    it('matchNode() should return true', async(() => {
        expect(service.matchNode(pageServiceRootParam, 'owners')).toBeTruthy();
    }));

    it('matchNode() should return false', async(() => {
        expect(service.matchNode(pageServiceRootParam, 'owners1')).toBeFalsy();
    }));

    it('traverseParam() should call updateParam()', async(() => {
        spyOn(service, 'updateParam').and.callThrough();
        service.traverseParam(pageServiceTraverseParam, pageServiceTraverseParamEventModel);
        expect(service.updateParam).toHaveBeenCalled();
    }));

    it('getUpdatedParamPath() should return updated path', async(() => {
        const eveModel = 'test/123/#';
        expect(service.getUpdatedParamPath(eveModel)).toEqual('123');
    }));

    it('getUpdatedParamPath() should update the path without # and return it', async(() => {
        const eveModel = 'test/123/';
        expect(service.getUpdatedParamPath(eveModel)).toEqual('123/');
    }));

    // it('traverseFlowConfig() should call traverseConfig()', async(() => {
    //     const eveModel = new ModelEvent();
    //     eveModel.value = { 'path': '/ownerlandingview', 'message': 'testMessage' };
    //     spyOn(service, 'traverseConfig').and.returnValue('');
    //     spyOn(service.messageEvent, 'next').and.callThrough();
    //     service.traverseFlowConfig(eveModel, 'ownerlandingview');
    //     expect(service.traverseConfig).toHaveBeenCalledWith(configServiceFlowConfigs.ownerlandingview.model.params, eveModel);
    //     expect(service.messageEvent.next).toHaveBeenCalledWith('testMessage');
    // }));

    it('traverseFlowConfig() should throw an warning', async(() => {
        const eveModel = new ModelEvent();
        eveModel.value = { 'path': '/ownerlandingview1' };
        spyOn(service, 'traverseConfig').and.returnValue('');
        spyOn(loggerService, 'warn').and.callThrough();
        service.traverseFlowConfig(eveModel, '');
        expect(loggerService.warn).toHaveBeenCalledWith('Response cannot be processed for the path /ownerlandingview1 as there is no get/new done on the viewroot ');
    }));

    it('getFlowNameFromPath() should updated flow name', async(() => {
        expect(service.getFlowNameFromPath('test/p/t')).toEqual('t');
    }));

    it('getFlowNameFromPath() should updated flow name based on /p/ index', async(() => {
        expect(service.getFlowNameFromPath('test/p/t123')).toEqual('t123');
    }));

    it('getFlowNameFromPath() should updated flow name p', async(() => {
        expect(service.getFlowNameFromPath('/p/t123')).toEqual('p');
    }));

    it('getPageToNavigateTo() should call executeHttp() with flow_/_nav?a=right&b=$execute url', async(() => {
        spyOn(service, 'executeHttp').and.callThrough();
        service.getPageToNavigateTo('right', 'flow_');
        expect(service.executeHttp).toHaveBeenCalledWith('flow_/_nav?a=right&b=$execute', 'GET', null);
    }));

    it('getPageToNavigateTo() should call buildFlowBaseURL() and executeHttp() with /_nav?a=right&b=$execute url', async(() => {
        spyOn(service, 'executeHttp').and.callThrough();
        service.getPageToNavigateTo('right', '');
        expect(service.executeHttp).toHaveBeenCalledWith('/_nav?a=right&b=$execute', 'GET', null);
    }));

    it('getPageToNavigateTo() should call executeHttp() with flow_/ad/_nav?a=right&b=$execute url', async(() => {
        spyOn(service, 'executeHttp').and.callThrough();
        service.getPageToNavigateTo('right', 'flow_/ad');
        expect(service.executeHttp).toHaveBeenCalledWith('flow_/ad/_nav?a=right&b=$execute', 'GET', null);
    }));

    it('getPageToNavigateTo() should call executeHttp() with /1flow_/_nav?a=right&b=$execute url', async(() => {
        service.executeHttp = () => { };
        service.routeParams = { domain: '' };
        spyOn(service, 'executeHttp').and.returnValue('');
        service.getPageToNavigateTo('right', '/1flow_');
        expect(service.executeHttp).toHaveBeenCalledWith('/1flow_/_nav?a=right&b=$execute', 'GET', null);
    }));

    it('getFlowNameFromOutput() should return empty string', async(() => {
        expect(service.getFlowNameFromOutput(undefined)).toEqual('');
    }));

    it('getFlowNameFromOutput() should return updated flowname from getFlowNameFromPath()', async(() => {
        spyOn(service, 'getFlowNameFromPath').and.returnValue('test:123');
        expect(service.getFlowNameFromOutput('')).toEqual('test');
    }));

    it('getFlowNameFromOutput() should return updated flow', async(() => {
        expect(service.getFlowNameFromOutput(pageServiceProcessResponse['0'].result.outputs[0].value.path)).toEqual('ownerlandingview');
    }));

    it('getNestedElementParam() should call matchNode() and traverseNestedPath()', async(() => {
        const param = Object.assign({}, pageServiceRootParam);
        spyOn(service, 'matchNode').and.returnValue(true);
        spyOn(service, 'traverseNestedPath').and.returnValue('test');
        const res = service.getNestedElementParam(param, 'test/123', 'eventPath');
        expect(service.matchNode).toHaveBeenCalledWith(param, '123');
        expect(service.traverseNestedPath).toHaveBeenCalledWith(param, 2, ['test', '123'], 'eventPath');
        expect(res).toEqual('test');
    }));

    it('getNestedElementParam() should not call traverseNestedPath', async(() => {
        const param = Object.assign({}, pageServiceRootParam);
        spyOn(service, 'matchNode').and.returnValue(false);
        spyOn(service, 'traverseNestedPath').and.returnValue('test');
        const res = service.getNestedElementParam(param, 'test/123');
        expect(service.matchNode).toHaveBeenCalledWith(param, '123');
        expect(service.traverseNestedPath).not.toHaveBeenCalled();
        expect(res).toEqual(undefined);
    }));

    it('loadFlowConfig() should call the executeHttp()', async(() => {
      const arg = { domain: 'test' };
      service.executeHttp = () => {};
      spyOn(service, 'executeHttp').and.callThrough();
      service.loadFlowConfig(arg);
      expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefinedundefined/p/test/_new?b=$execute', 'GET', null);
    }));

    it('matchElementId() should return true', async(() => {
        const param = Object.assign({}, pageServiceRootParam);
        param.elemId = 'test';
        expect(service.matchElementId(param, 'test')).toBeTruthy();
    }));

    it('matchElementId() should return true', async(() => {
        const param = Object.assign({}, pageServiceRootParam);
        param.elemId = 'test';
        expect(service.matchElementId(param, 'test1')).toBeFalsy();
    }));

    it('traverseConfig() should call traversePageConfig()', async(() => {
        const param = Object.assign({}, pageServiceRootParam);
        const params = [param];
        const eveModel = new ModelEvent();
        eveModel.value = { path: '/t' };
        spyOn(service, 'getUpdatedParamPath').and.returnValue('owners/123');
        spyOn(service, 'traversePageConfig').and.returnValue('');
        service.traverseConfig(params, eveModel);
        expect(service.traversePageConfig).toHaveBeenCalledWith(param, eveModel, 0);
    }));

    it('traverseConfig() should not call traversePageConfig()', async(() => {
        const param = Object.assign({}, pageServiceRootParam);
        param.config.code = 'owners1'
        const params = [param];
        const eveModel = new ModelEvent();
        eveModel.value = { path: '/t' };
        spyOn(service, 'getUpdatedParamPath').and.returnValue('owners/123');
        spyOn(service, 'traversePageConfig').and.returnValue('');
        service.traverseConfig(params, eveModel);
        expect(service.traversePageConfig).not.toHaveBeenCalled();
    }));

    it('loadDomainFlowConfig() should call executeHttp()', async(() => {
        spyOn(service, 'executeHttp').and.callThrough();
        service.loadDomainFlowConfig('test');
        expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefinedundefined/p/test:test/_get?b=$execute', 'GET', null);
    }));

    it('loadDomainFlowConfig() should call executeHttp() with null session id', async(() => {
        spyOn(service, 'executeHttp').and.callThrough();
        service.loadDomainFlowConfig('test1');
        expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefinedundefined/p/test1/_new?b=$execute', 'GET', null);
    }));

    it('getDetaultPageForFlow() should return model.params[0] value', async(() => {
        expect(service.getDetaultPageForFlow(pageServiceModel)).toEqual(pageServiceModel.params[0]);
    }));

    it('getDetaultPageForFlow(null) should return undefined', async(() => {
        expect(service.getDetaultPageForFlow({ params: null })).toEqual(undefined);
    }));

    it('setViewRootAndNavigate() should call the navigateToDefaultPageForFlow()', async(() => {
        spyOn(service, 'navigateToDefaultPageForFlow').and.callThrough();
        service.setViewRootAndNavigate(pageServiceSetViewRootAndNavigateOutput, 'test', true, true);
        expect(service.navigateToDefaultPageForFlow).toHaveBeenCalledWith(pageServiceSetViewRootAndNavigateOutput.value.type.model, 'test');
    }));

    it('setViewRootAndNavigate() should call the configservice.setLayoutToAppConfig()', async(() => {
        spyOn(configService, 'setLayoutToAppConfig').and.callThrough();
        service.setViewRootAndNavigate(pageServiceSetViewRootAndNavigateOutput, 'test', true, true);
        const viewRoot: ViewRoot = new ViewRoot();
        viewRoot.model = pageServiceSetViewRootAndNavigateOutput.value.type.model;
        viewRoot.layout = '';
        expect(configService.setLayoutToAppConfig).toHaveBeenCalledWith('test', viewRoot);
    }));

    it('setViewRootAndNavigate() should update the layout$ event', async(() => {
        spyOn(service.layout$, 'emit').and.callThrough();
        pageServiceSetViewRootAndNavigateOutput.value.config.type.model.uiStyles = { attributes: { layout: 'testting layout' } };
        service.setViewRootAndNavigate(pageServiceSetViewRootAndNavigateOutput, 'test', true, true);
        expect(service.layout$.emit).toHaveBeenCalledWith(pageServiceSetViewRootAndNavigateOutput.value.config.type.model.uiStyles.attributes.layout);
    }));

    it('setViewRootAndNavigate() should update the sessionStore with rootDomainId provided', async(() => {
        spyOn(sessionStoreService, 'set').and.callThrough();
        pageServiceSetViewRootAndNavigateOutput.r0otDomainId = 'testingRootDomainId'
        service.setViewRootAndNavigate(pageServiceSetViewRootAndNavigateOutput, 'test', true, true);
        expect(sessionStoreService.set).toHaveBeenCalledWith('test', pageServiceSetViewRootAndNavigateOutput.rootDomainId);
    }));

    it('setViewRootAndNavigate() should not call the navigateToDefaultPageForFlow()', async(() => {
        spyOn(service, 'navigateToDefaultPageForFlow').and.callThrough();
        service.setViewRootAndNavigate(pageServiceSetViewRootAndNavigateOutput, '', false, true);
        expect(service.navigateToDefaultPageForFlow).not.toHaveBeenCalled();
    }));


    it('traverseOutput() should call traverseFlowConfig()', async(() => {
        const outputs = pageServiceOutputs.slice(0, 1);
        spyOn(service, 'traverseFlowConfig').and.callThrough();
        service.traverseOutput(outputs);
        const resOuptuts = new ModelEvent()
        resOuptuts.type = undefined;
        resOuptuts.value = pageServiceOutputs[0].value;
        resOuptuts.id = undefined;
        expect(service.traverseFlowConfig).toHaveBeenCalledWith(resOuptuts, 'ownerlandingview');
    }));

    it('traverseOutput() should call navigateToPage()', async(() => {
        const outputs = pageServiceOutputs.slice(0, 1);
        outputs[0].action = '_nav';
        spyOn(service, 'navigateToPage').and.returnValue('');
        service.traverseOutput(outputs);
        expect(service.navigateToPage).toHaveBeenCalledWith(undefined, 'ownerlandingview');
    }));

    it('traverseOutput() should call location back() and logger.debug()', async(() => {
        const outputs = pageServiceOutputs.slice(0, 1);
        outputs[0].action = '_get';
        pageServiceRootParam.config.uiStyles.attributes.browserBack = true;
        spyOn(location, 'back').and.callThrough();
        spyOn(loggerService, 'debug').and.callThrough();
        service.traverseOutput(outputs, pageServiceRootParam);
        expect(location.back).toHaveBeenCalled();
        expect(loggerService.debug).toHaveBeenCalled();
    }));

    it('traverseOutput() should call location back() only once', async(() => {
        const outputs = pageServiceOutputs.slice(0, 1);
        outputs[0].action = '_get';
        pageServiceRootParam.config.uiStyles.attributes.browserBack = true;
        spyOn(location, 'back').and.callThrough();
        service.traverseOutput(outputs, pageServiceRootParam);
        expect(location.back).toHaveBeenCalledTimes(1);
    }));

    it('traverseOutput() should not call location back()', async(() => {
        const outputs = pageServiceOutputs.slice(0, 1);
        outputs[0].action = '_get';
        pageServiceRootParam.config.uiStyles.attributes.browserBack = false;
        spyOn(location, 'back').and.callThrough();
        service.traverseOutput(outputs, pageServiceRootParam);
        expect(location.back).not.toHaveBeenCalled();
    }));

    it('traverseOutput() should not call navigateToPage() when browserBack is true', async(() => {
        const outputs = pageServiceOutputs.slice(0, 1);
        outputs[0].action = '_get';
        pageServiceRootParam.config.uiStyles.attributes.browserBack = true;
        spyOn(service, 'navigateToPage').and.returnValue('');
        service.traverseOutput(outputs, pageServiceRootParam);
        expect(service.navigateToPage).not.toHaveBeenCalled();
    }));

    it('traverseOutput() should call navigateToPage() when browserBack is false', async(() => {
        const outputs = pageServiceOutputs.slice(0, 1);
        outputs[0].action = '_nav';
        pageServiceRootParam.config.uiStyles.attributes.browserBack = false;
        spyOn(service, 'navigateToPage').and.returnValue('');
        service.traverseOutput(outputs, pageServiceRootParam);
        expect(service.navigateToPage).toHaveBeenCalledWith(undefined, 'ownerlandingview');
    }));

    it('traverseOutput() should call the traverseFlowConfig() on _new action', async(() => {
        const outputs = pageServiceOutputs.slice(0, 1);
        outputs[0].action = '_new';
        pageServiceRootParam.config.uiStyles.attributes.browserBack = false;
        spyOn(service, 'traverseFlowConfig').and.returnValue('');
        spyOn(service, 'setViewRootAndNavigate').and.returnValue('');
        service.traverseOutput(outputs);
        const resOuptuts = new ModelEvent()
        resOuptuts.type = undefined;
        resOuptuts.value = pageServiceOutputs[0].value;
        resOuptuts.id = undefined;
        expect(service.traverseFlowConfig).toHaveBeenCalledWith(resOuptuts, 'ownerlandingview');
    }));

    it('traverseOutput() should call setViewRootAndNavigate() on _new action', async(() => {
        const outputs = pageServiceOutputs.slice(0, 1);
        outputs[0].action = '_new';
        outputs[0].value.path = '/ownerlandingview';
        spyOn(service, 'setViewRootAndNavigate').and.returnValue('');
        service.traverseOutput(outputs);
        expect(service.setViewRootAndNavigate).toHaveBeenCalledWith(outputs[0], 'ownerlandingview', true, true);
    }));

    it('traverseOutput() should call setViewRootAndNavigate() on _get action', async(() => {
        const outputs = pageServiceOutputs.slice(0, 1);
        outputs[0].action = '_get';
        outputs[0].value = { config: {}, type: { model: 'test' } }
        pageServiceRootParam.config.uiStyles.attributes.browserBack = true;
        spyOn(service, 'setViewRootAndNavigate').and.returnValue('');
        service.traverseOutput(outputs);
        expect(service.setViewRootAndNavigate).toHaveBeenCalledWith(outputs[0], '', true, false);
    }));



    it('findMatchingPageConfigById() should return param object based on the pageId and flowName', async(() => {
        expect(service.findMatchingPageConfigById('vpOwners', 'ownerlandingview')).toEqual(configServiceFlowConfigs.ownerlandingview.model.params[0]);
    }));

    it('findMatchingPageConfigById() should return param object based on the pageId and flowName with colon in flowname', async(() => {
        expect(service.findMatchingPageConfigById('vpOwners', 'ownerlandingview:1')).toEqual(configServiceFlowConfigs.ownerlandingview.model.params[0]);
    }));

    it('findMatchingPageConfigById() should call logError()', async(() => {
        spyOn(loggerService, 'debug').and.callThrough();
        service.findMatchingPageConfigById('test', 'test1');
        expect(loggerService.debug).toHaveBeenCalled();
    }));

    it('processEvent() should call executeHttp() with undefined//undefined:undefined/undefinedundefined/p/test:test/123/_get?b=t', async(() => {
        const processUrl = '/ownerlandingview/vpOwners/vtOwners/vsOwners/owners';
        const behavior = '$execute';
        const model: GenericDomain = new GenericDomain();
        const method = 'GET';
        const queryParams = '&pageSize=5&page=0';
        spyOn(service, 'executeHttp').and.callThrough();
        service.processEvent(processUrl, behavior, model, method, queryParams);
        expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefinedundefined/p/ownerlandingview:test/vpOwners/vtOwners/vsOwners/owners/_get?b=$execute&pageSize=5&page=0', 'GET', model, '/ownerlandingview/vpOwners/vtOwners/vsOwners/owners');
    }));

    it('processEvent() should call executeHttp() with undefined//undefined:undefined/undefinedundefined/p/ownerlandingview:test/vpOwners/?vtOwners/vsOwners/owners/_get&b=$execute&pageSize=5&page=0', async(() => {
        const processUrl = '/ownerlandingview/vpOwners/?vtOwners/vsOwners/owners';
        const model: GenericDomain = new GenericDomain();
        const method = 'GET';
        const queryParams = '&pageSize=5&page=0';
        spyOn(service, 'executeHttp').and.callThrough();
        service.processEvent(processUrl, undefined, model, method, queryParams);
        expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefinedundefined/p/ownerlandingview:test/vpOwners/?vtOwners/vsOwners/owners/_get&b=$execute&pageSize=5&page=0', 'GET', model, '/ownerlandingview/vpOwners/?vtOwners/vsOwners/owners');
    }));


    it('executeHttp() should call executeHttpGet(), showLoader() and logger.info', async(() => {
        spyOn(service, 'executeHttpGet').and.callThrough();
        spyOn(service, 'showLoader').and.callThrough();
        spyOn(loggerService, 'info').and.callThrough();
        service.executeHttp('/test', 'GET', {}, 'testingParamPath');
        expect(service.executeHttpGet).toHaveBeenCalledWith('/test', 'testingParamPath');
        expect(service.showLoader).toHaveBeenCalled();
        expect(loggerService.info).toHaveBeenCalledWith('http call/teststarted');
    }));

    it('executeHttp() should call executeHttpPost()', async(() => {
        spyOn(service, 'executeHttpPost').and.callThrough();
        service.executeHttp('/test', 'POST', {}, 'paramPath');
        expect(service.executeHttpPost).toHaveBeenCalledWith('/test', {}, 'paramPath');
    }));

    it('executeHttp() should throw an error', async(() => {
        spyOn(loggerService, 'error').and.callThrough();
        spyOn(service, 'invokeFinally').and.callThrough();
        service.executeHttp('/test', 'DELETE', {});
        expect(loggerService.error).toHaveBeenCalledWith('http method not supported');
        expect(service.invokeFinally).toHaveBeenCalledWith('/test');
    }));

    it('postOnChange() should call executeHttp()', async(() => {
        const resOuptuts = new ModelEvent();
        resOuptuts.type = '_update';
        resOuptuts.id = 'test:test/123';
        resOuptuts.value = 'a';
        resOuptuts.payload = 'a';
        spyOn(service, 'executeHttp').and.callThrough();
        spyOn(loggerService, 'info').and.callThrough();
        service.postOnChange('test/123', 't', 'a');
        expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefinedundefined/p/event/notify', 'POST', resOuptuts);
        expect(loggerService.info).toHaveBeenCalledWith('Post update Event Notify call started for undefined//undefined:undefined/undefinedundefined/p/event/notify');
    }));

    it('postOnChange() should call executeHttp() even if flow is not found', async(() => {
        const resOuptuts = new ModelEvent();
        resOuptuts.type = '_update';
        resOuptuts.id = 'flow/flow';
        resOuptuts.value = 'a';
        resOuptuts.payload = 'a';
        spyOn(service, 'executeHttp').and.callThrough();
        spyOn(loggerService, 'info').and.callThrough();
        spyOn(service, 'getFlowRootDomainId').and.returnValue(null);
        service.postOnChange('flow/flow', 't', 'a');
        expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefinedundefined/p/event/notify', 'POST', resOuptuts);
        expect(loggerService.info).toHaveBeenCalledWith('Post update Event Notify call started for undefined//undefined:undefined/undefinedundefined/p/event/notify');
    }));


    it('traversePageConfig() should call traversePageConfig()', async(() => {
        const rootParam = Object.assign({}, pageServiceTraverseParamPayload);
        rootParam.type.model.params[0].config.type.collection = false;
        const eventModel = new ModelEvent();
        eventModel.value = { 'path': '', 'collectionElem': false };
        spyOn(service, 'getUpdatedParamPath').and.returnValue('/vfvgOwnerContact/address/address/a/b/a');
        spyOn(service, 'traversePageConfig').and.callThrough();
        service.traversePageConfig(rootParam, eventModel, 1);
        expect(service.traversePageConfig).toHaveBeenCalledWith(rootParam.type.model.params[0], eventModel, 2);
        expect(service.traversePageConfig).toHaveBeenCalledTimes(2);
    }));

    it('traversePageConfig() should call logger.warn()', async(() => {
        const rootParam = Object.assign({}, pageServiceTraverseParamPayload);
        rootParam.type.model.params[0].config.type.collection = true;
        const eventModel = new ModelEvent();
        eventModel.value = { 'path': '', 'collectionElem': false };
        spyOn(service, 'getUpdatedParamPath').and.returnValue('/vfvgOwnerContact/address/address/a/b/a');
        spyOn(loggerService, 'warn').and.callThrough();
        service.traversePageConfig(rootParam.type.model.params[0], eventModel, 2);
        expect(loggerService.warn).toHaveBeenCalledWith('event update with path  did not match with any param of current domain');
    }));

    it('traversePageConfig() should call processModelEvent()', async(() => {
        const rootParam = Object.assign({}, pageServiceTraverseParamPayload);
        rootParam.type.model.params[0].config.type.collection = true;
        const eventModel = new ModelEvent();
        eventModel.value = { 'path': '', 'collectionElem': false };
        spyOn(service, 'getUpdatedParamPath').and.returnValue('/vfvgOwnerContact/address/address/a/b/a');
        spyOn(service, 'traversePageConfig').and.callThrough();
        spyOn(service, 'processModelEvent').and.callThrough();
        service.traversePageConfig(rootParam, eventModel, 1);
        expect(service.processModelEvent).toHaveBeenCalledWith(rootParam.type.model.params[0], eventModel);
    }));

    it('traversePageConfig() should call processModelEvent()', async(() => {
        const rootParam = Object.assign({}, pageServiceTraverseParamPayload);
        rootParam.path = '';
        const eventModel = new ModelEvent();
        eventModel.value = { 'path': '', 'collectionElem': true };
        spyOn(service, 'getUpdatedParamPath').and.returnValue('/vfvgOwnerContact');
        spyOn(service, 'processModelEvent').and.returnValue('');
        service.traversePageConfig(rootParam, eventModel, 1);
        expect(service.processModelEvent).toHaveBeenCalledWith(rootParam, eventModel);
    }));


    it('processModelEvent() should call createGridData(), replaceSourceParamkeys() and update gridValueUpdate subject', async(() => {
        const rootParam = Object.assign({}, pageServiceRootParam1);
        const eventModel = new ModelEvent()
        eventModel.value = { 'path': '/ownerlandingview/vpOwners/vtOwners/vsOwners/owners', 'collectionElem': true, 'type': { 'model': { 'params': [] } } }
        spyOn(service.gridValueUpdate, 'next').and.callThrough();
        spyOn(service, 'createGridData').and.callThrough();
        spyOn(service, 'replaceSourceParamkeys').and.callThrough();
        service.processModelEvent(rootParam, eventModel);
        expect(service.createGridData).toHaveBeenCalledWith(eventModel.value.type.model.params, rootParam);
        expect(service.replaceSourceParamkeys).toHaveBeenCalled();
        expect(service.gridValueUpdate.next).toHaveBeenCalledWith(rootParam);
        expect(rootParam.gridData.leafState).toEqual([]);
    }));

    it('traverseNestedPath() should call traverseNestedPath()', async(() => {
        spyOn(service, 'matchElementId').and.returnValue(true);
        spyOn(service, 'traverseNestedPath').and.callThrough();
        const rootParam = Object.assign({}, pageServiceRootParam1);
        rootParam.type.model.params.push(configServiceFlowConfigs.ownerlandingview.model.params[0].type.model.params[0]);
        rootParam.type.model.params[0].type.model.params[0].gridData = rootParam.gridData;
        rootParam.type.model.params[0].gridData = rootParam.gridData;
        service.traverseNestedPath(rootParam, 0, ['vtOwners', "vsSearchOwnerCriteria"], 'eventPath');
        expect(service.traverseNestedPath).toHaveBeenCalledTimes(2);
        expect(service.traverseNestedPath).toHaveBeenCalledWith(pageServiceTraverseNestedPathResult, 0, ['vtOwners', "vsSearchOwnerCriteria"], 'eventPath');
        expect(service.traverseNestedPath).toHaveBeenCalledWith(undefined, 3, ['vtOwners', "vsSearchOwnerCriteria"], 'eventPath');
    }));

    it('processModelEvent() should call createGridData() update gridValueUpdate subject with nestedElement', async(() => {
        const rootParam = Object.assign({}, pageServiceRootParam1);
        rootParam.gridData.leafState[0]['elemId'] = '23';
        rootParam.gridData.leafState[0]['nestedElement'] = 'nestedElement';
        const eventModel = new ModelEvent()
        eventModel.value = { 'path': '/ownerlandingview/vpOwners/vtOwners/vsOwners/owners123/a/b/c/', 'collectionElem': true, 'type': { 'model': { 'params': [] } } }
        spyOn(service.gridValueUpdate, 'next').and.callThrough();
        spyOn(service, 'getNestedElementParam').and.returnValue(pageServiceRootParam1);
        spyOn(service, 'createGridData').and.returnValue({ 'leafState': 'testingleafstate' });
        service.processModelEvent(rootParam, eventModel);
        expect(service.getNestedElementParam).toHaveBeenCalledWith(rootParam.gridData.leafState[0]['nestedElement'], "23/a/b/c/", eventModel.value.path);
        expect(service.createGridData).toHaveBeenCalledWith(eventModel.value.type.model.params, pageServiceRootParam1);
        expect(service.gridValueUpdate.next).toHaveBeenCalledWith(pageServiceRootParam1);
    }));

    it('traverseNestedPath() should call traverseNestedPath() with element which config.code is not found', async(() => {
        spyOn(service, 'matchElementId').and.returnValue(true);
        spyOn(service, 'traverseNestedPath').and.callThrough();
        const rootParam = Object.assign({}, pageServiceRootParam1);
        rootParam.type.model.params.push(configServiceFlowConfigs.ownerlandingview.model.params[0].type.model.params[0]);
        rootParam.type.model.params[0].type.model.params[0].gridData = rootParam.gridData;
        rootParam.type.model.params[0].gridData = rootParam.gridData;
        rootParam.type.model.params[0].gridData.leafState = [];
        service.traverseNestedPath(rootParam, 0, ['vtOwners', "vsSearchOwnerCriteria"], 'eventPath');
        expect(service.traverseNestedPath).toHaveBeenCalledTimes(3);
        expect(service.traverseNestedPath).toHaveBeenCalledWith(rootParam.type.model.params[0].type.model.params[0], 2, ['vtOwners', "vsSearchOwnerCriteria"], 'eventPath');
    }));

    it('traverseNestedPath() should return element', async(() => {
        spyOn(service, 'matchElementId').and.returnValue(true);
        spyOn(service, 'traverseNestedPath').and.callThrough();
        const rootParam = Object.assign({}, pageServiceRootParam1);
        rootParam.type.model.params.push(configServiceFlowConfigs.ownerlandingview.model.params[0].type.model.params[0]);
        rootParam.type.model.params[0].type.model.params[0].gridData = rootParam.gridData;
        rootParam.type.model.params[0].gridData = rootParam.gridData;
        rootParam.type.model.params[0].gridData.leafState = [];
        const res = service.traverseNestedPath(rootParam, 0, ['vtOwners'], '/ownerlandingview/vpOwners/vtOwners');
        expect(res).toEqual(rootParam.type.model.params[0]);
    }));

    it('processModelEvent() should call createGridData() and update gridValueUpdate subject, rootParam, rootParam.page', async(() => {
        const rootParam = Object.assign({}, pageServiceRootParam1);
        rootParam.gridData.leafState = null;
        const eventModel = new ModelEvent()
        const gridPage = new GridPage();
        gridPage.first = true;
        gridPage.last = true;
        gridPage.numberOfElements = 1;
        gridPage.size = 0;
        gridPage.totalElements = 1;
        gridPage.totalPages = 1;
        eventModel.value = { 'path': '/ownerlandingview/vpOwners/vtOwners/vsOwners/owners', 'collectionElem': true, 'type': { 'model': { 'params': [] } }, 'page': gridPage };
        spyOn(service.gridValueUpdate, 'next').and.callThrough();
        spyOn(service, 'createGridData').and.returnValue({ 'leafState': 'testingleafstate' });
        service.processModelEvent(rootParam, eventModel);
        expect(service.createGridData).toHaveBeenCalledWith(eventModel.value.type.model.params, rootParam);
        expect(service.gridValueUpdate.next).toHaveBeenCalledWith(rootParam);
        expect(rootParam.gridData.leafState).toEqual('testingleafstate');
        expect(rootParam.page).toEqual(eventModel.value.page);
    }));

    it('processModelEvent() should call createGridData() and update gridValueUpdate subject, rootParam', async(() => {
        const rootParam = Object.assign({}, pageServiceRootParam1);
        rootParam.gridData.leafState = null;
        const eventModel = new ModelEvent()
        eventModel.value = { 'path': '/ownerlandingview/vpOwners/vtOwners/vsOwners/owners', 'collectionElem': true, 'type': { 'model': { 'params': [] } } }
        spyOn(service.gridValueUpdate, 'next').and.callThrough();
        spyOn(service, 'createGridData').and.returnValue({ 'leafState': 'testingleafstate' });
        service.processModelEvent(rootParam, eventModel);
        expect(service.createGridData).toHaveBeenCalledWith(eventModel.value.type.model.params, rootParam);
        expect(service.gridValueUpdate.next).toHaveBeenCalledWith(rootParam);
        expect(rootParam.gridData.leafState).toEqual('testingleafstate');
    }));

    it('processModelEvent() should update rootParam.type.model.params for CardDetailsGrid', async(() => {
        const rootParam = Object.assign({}, pageServiceRootParam1);
        rootParam.config.uiStyles.attributes.alias = 'CardDetailsGrid';
        const eventModel = new ModelEvent()
        eventModel.value = { 'path': '/ownerlandingview/vpOwners/vtOwners/vsOwners/owners', 'collectionElem': true, 'type': { 'model': { 'params': [] } } }
        service.processModelEvent(rootParam, eventModel);
        expect(rootParam.type.model['params']).toEqual([]);
    }));

    it('processModelEvent() should call traverseParam() for CardDetailsGrid', async(() => {
        const rootParam = Object.assign({}, pageServiceRootParam1);
        rootParam.config.uiStyles.attributes.alias = 'CardDetailsGrid';
        rootParam.config.type.collection = false;
        const eventModel = new ModelEvent()
        eventModel.value = { 'path': '/ownerlandingview/vpOwners/vtOwners/vsOwners/owners', 'collectionElem': true, 'type': { 'model': { 'params': [] } } }
        spyOn(service, 'traverseParam').and.callThrough();
        service.processModelEvent(rootParam, eventModel);
        expect(service.traverseParam).toHaveBeenCalledWith(rootParam, eventModel);
    }));

    it('processModelEvent() should call traverseParam() for CardDetails', async(() => {
        const rootParam = Object.assign({}, pageServiceRootParam1);
        rootParam.config.uiStyles.attributes.alias = 'CardDetails';
        const eventModel = new ModelEvent()
        eventModel.value = { 'path': '/ownerlandingview/vpOwners/vtOwners/vsOwners/owners', 'collectionElem': true, 'type': { 'model': { 'params': [] } } }
        spyOn(service, 'traverseParam').and.callThrough();
        service.processModelEvent(rootParam, eventModel);
        expect(service.traverseParam).toHaveBeenCalledWith(rootParam, eventModel);
    }));

    it('traverseNestedPath() should return empty string', async(() => {
        expect(service.traverseNestedPath('', 1, ['a'])).toEqual('');
    }));

    it('updateParam() should call updateNestedParameters()', async(() => {
        spyOn(configService, 'getViewConfigById').and.returnValue('a');
        spyOn(service, 'updateNestedParameters').and.returnValue('');
        spyOn(service, 'replaceSourceParamkeys').and.returnValue('');
        spyOn(service.eventUpdate, 'next').and.returnValue('');
        spyOn(service.validationUpdate, 'next').and.returnValue('');
        service.updateParam(pageServiceCreateGridDataParam, pageServiceRootParam);
        expect(service.updateNestedParameters).toHaveBeenCalledWith(pageServiceCreateGridDataParam, pageServiceRootParam);
        expect(service.replaceSourceParamkeys).toHaveBeenCalledWith(pageServiceCreateGridDataParam, pageServiceRootParam);
        expect(service.eventUpdate.next).toHaveBeenCalledWith(pageServiceCreateGridDataParam);
        expect(service.validationUpdate.next).toHaveBeenCalledWith(pageServiceCreateGridDataParam);
    }));

    it('updateParam() should call logger.debug()', async(() => {
        spyOn(configService, 'getViewConfigById').and.returnValue(null);
        spyOn(loggerService, 'debug').and.callThrough();
        service.updateParam(pageServiceCreateGridDataParam, pageServiceRootParam);
        expect(loggerService.debug).toHaveBeenCalledWith('Could not process the update from the server for /ownerlandingview/vpOwners/vtOwners/vsOwners/owners because config is undefined.');
    }));

    it('replaceSourceParamkeys() should call reflect.set()', async(() => {
        spyOn(Reflect, 'get').and.returnValue('test');
        spyOn(Reflect, 'set').and.returnValue('');
        service.replaceSourceParamkeys(pageServiceCreateGridDataParam, pageServiceRootParam);
        expect(Reflect.set).toHaveBeenCalledWith(pageServiceCreateGridDataParam, 'enabled', 'test');
        expect(Reflect.get).toHaveBeenCalledWith(pageServiceRootParam, 'enabled');
    }));

    it('replaceSourceParamkeys() should call logger.error()', async(() => {
        Reflect.set = () => {
            throw new Error('testing error scenerio');
        };
        spyOn(loggerService, 'error').and.callThrough();
        service.replaceSourceParamkeys(pageServiceCreateGridDataParam, pageServiceRootParam);
        expect(loggerService.error).toHaveBeenCalledWith('{}');
    }));

    it('updateNestedParameters() should call updateParam()', async(() => {
        spyOn(service, 'updateParam').and.returnValue('');
        spyOn(loggerService, 'warn').and.callThrough();
        service.updateNestedParameters(pageServiceModel.params[0], pageServiceTraverseParam);
        expect(service.updateParam).toHaveBeenCalledWith(pageServiceModel.params[0].type.model.params[0], pageServiceTraverseParam.type.model.params[0]);
        expect(loggerService.warn).toHaveBeenCalledWith('Detected new params for  /petlayout/vp/vsHeader/vaBanner/vatBannerTab/vcdPetDetails/vcdbPetDetails/vfvgOwnerContact in the update when sourceParam does not have config');
    }));

    it('updateNestedParameters() should call loggerService.error()', async(() => {
        service.updateParam = () => {
            throw new Error('testing error scenerio');
        };
        spyOn(loggerService, 'error').and.callThrough();
        service.updateNestedParameters(pageServiceModel.params[0], pageServiceTraverseParam);
        expect(loggerService.error).toHaveBeenCalledWith('ERROR: Failure making server call : "Could not find source param to update the nested payload param path/petlayout/vp/vsHeader/vaBanner/vatBannerTab/vcdPetDetails/vcdbPetDetails/vfvgOwnerContact"');
    }));

    it('executeHttpGet() should make a get call and call invokeFinally(), processResponse()', async(() => {
        spyOn(service, 'invokeFinally').and.callThrough();
        spyOn(service, 'processResponse').and.callThrough();
        service.executeHttpGet('/test', 'testParamPath');
        const req = backend.expectOne('/test');
        expect(req.request.method).toEqual('GET');
        req.flush({ sessionId: 123, result: 'testResult' });
        expect(service.invokeFinally).toHaveBeenCalledWith('/test', 'testParamPath');
        expect(service.processResponse).toHaveBeenCalledWith('testResult');
        backend.verify();
    }));

    it('executeHttpPost() should make a post call', async(() => {
        spyOn(service, 'invokeFinally').and.callThrough();
        spyOn(service, 'processResponse').and.callThrough();
        service.executeHttpPost('/test', {}, 'testParamPath');
        const req = backend.expectOne('/test');
        expect(req.request.method).toEqual('POST');
        req.flush({ sessionId: 123, result: 'testResult' });
        expect(service.invokeFinally).toHaveBeenCalledWith('/test', 'testParamPath');
        expect(service.processResponse).toHaveBeenCalledWith('testResult');
        backend.verify();
    }));

    it('executeHttpPost() should call processError()', async(() => {
        const processError = (error, path) => {
            expect(error.error).toEqual('deliberate 404 error');
            expect(error.message).toEqual('Http failure response for /test: 404 Not Found');
            expect(error.name).toEqual('HttpErrorResponse');
            expect(error.ok).toEqual(false);
            expect(error.status).toEqual(404);
            expect(error.statusText).toEqual('Not Found');
            expect(error.url).toEqual('/test');
            expect(path).toEqual('paramPath');
        };
        const httpClient: HttpClient = TestBed.get(HttpClient);
        const httpTestingController: HttpTestingController = TestBed.get(HttpTestingController);
        const emsg = 'deliberate 404 error';
        const testUrl = '/test'
        spyOn(service, 'processError').and.callFake(processError);
        service.executeHttpPost(testUrl, {}, 'paramPath');
        const req = httpTestingController.expectOne(testUrl);
        req.flush(emsg, { status: 404, statusText: 'Not Found' });
        expect(service.processError).toHaveBeenCalled();
    }));

    it('executeHttpGet() should call processError()', async(() => {
        const processError = (error, path) => {
            expect(error.error).toEqual({ 'errorMessage': 'Uh oh!' });
            expect(error.message).toEqual('Http failure response for /test: 500 Server Error');
            expect(error.name).toEqual('HttpErrorResponse');
            expect(error.ok).toEqual(false);
            expect(error.status).toEqual(500);
            expect(error.statusText).toEqual('Server Error');
            expect(error.url).toEqual('/test');
            expect(path).toEqual('paramPath');
        };
        spyOn(service, 'processError').and.callFake(processError);
        service.executeHttpGet('/test', 'paramPath');
        const req = backend.expectOne('/test');
        expect(req.request.method).toEqual('GET');
        req.flush({ errorMessage: 'Uh oh!' }, { status: 500, statusText: 'Server Error' });
        backend.verify();
        expect(service.processError).toHaveBeenCalled();
    }));

    it('getFlowLayoutConfig() should return layout from configservice.getFlowConfig()', async(() => {
        spyOn(service, 'navigateToDefaultPageForFlow').and.returnValue('');
        service.getFlowLayoutConfig('ownerlandingview', true).then(data => {
            expect(data).toEqual('ownerlandingview layout');
        });
    }));

    it('findParamByCommandUri() should call findParamByAbsolutePath with paramPath (with rootCommandURI on _get)', async(() => {
        spyOn(service, 'findParamByAbsolutePath').and.callThrough();
        service.findParamByCommandUri(pageServiceProcessResponse['0'].result.inputCommandUri);
        expect(service.findParamByAbsolutePath).toHaveBeenCalledWith('/ownerlandingview/vpOwners/vtOwners/vsOwners/owners');
    }));

    it('findParamByCommandUri() should call findParamByAbsolutePath with paramPath (without rootCommandURI on _get)', async(() => {
        const response = Object.assign({}, pageServiceProcessResponse);
        response['0'].result.inputCommandUri = '/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/_get?b=$execute';
        spyOn(service, 'findParamByAbsolutePath').and.callThrough();
        service.findParamByCommandUri(response['0'].result.inputCommandUri);
        expect(service.findParamByAbsolutePath).toHaveBeenCalledWith('/ownerlandingview/vpOwners/vtOwners/vsOwners/owners');
    }));

    it('findParamByCommandUri() should call findParamByAbsolutePath with empty paramPath (without rootCommandURI _set)', async(() => {
        const response = Object.assign({}, pageServiceProcessResponse);
        response['0'].result.inputCommandUri = '/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/';
        spyOn(service, 'findParamByAbsolutePath').and.callThrough();
        service.findParamByCommandUri(response['0'].result.inputCommandUri);
        expect(service.findParamByAbsolutePath).toHaveBeenCalledWith('');
    }));

    it('findParamByCommandUri() should call findParamByAbsolutePath with paramPath (without rootCommandURI with _nav)', async(() => {
        const response = Object.assign({}, pageServiceProcessResponse);
        response['0'].result.inputCommandUri = '/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/_nav?b=$execute';
        spyOn(service, 'findParamByAbsolutePath').and.callThrough();
        service.findParamByCommandUri(response['0'].result.inputCommandUri);
        expect(service.findParamByAbsolutePath).toHaveBeenCalledWith('/ownerlandingview/vpOwners/vtOwners/vsOwners/owners');
    }));

    it('findParamByCommandUri() should call findParamByAbsolutePath with paramPath (without rootCommandURI with _new)', async(() => {
        const response = Object.assign({}, pageServiceProcessResponse);
        response['0'].result.inputCommandUri = '/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/_new?b=$execute';
        spyOn(service, 'findParamByAbsolutePath').and.callThrough();
        service.findParamByCommandUri(response['0'].result.inputCommandUri);
        expect(service.findParamByAbsolutePath).toHaveBeenCalledWith('/ownerlandingview/vpOwners/vtOwners/vsOwners/owners');
    }));

    it('findParamByCommandUri() should call findParamByAbsolutePath with paramPath (without rootCommandURI with _update)', async(() => {
        const response = Object.assign({}, pageServiceProcessResponse);
        response['0'].result.inputCommandUri = '/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/_update?b=$execute';
        spyOn(service, 'findParamByAbsolutePath').and.callThrough();
        service.findParamByCommandUri(response['0'].result.inputCommandUri);
        expect(service.findParamByAbsolutePath).toHaveBeenCalledWith('/ownerlandingview/vpOwners/vtOwners/vsOwners/owners');
    }));

    it('findParamByCommandUri() should call findParamByAbsolutePath with paramPath (without rootCommandURI with _update) and domainFlowPath with :', async(() => {
        const response = Object.assign({}, pageServiceProcessResponse);
        response['0'].result.inputCommandUri = '/client/org/app/p/ownerlandingview/test:vpOwners/vtOwners/vsOwners/owners/_update?b=$execute';
        spyOn(service, 'findParamByAbsolutePath').and.callThrough();
        service.findParamByCommandUri(response['0'].result.inputCommandUri);
        expect(service.findParamByAbsolutePath).toHaveBeenCalledWith('/ownerlandingview/test/test:vpOwners/vtOwners/vsOwners/owners');
    }));

    it('findParamByAbsolutePath() should return root param', async(() => {
        expect(service.findParamByAbsolutePath(configServiceFlowConfigs.ownerlandingview.model.params[0].type.model.params[0].path)).toEqual(configServiceFlowConfigs.ownerlandingview.model.params[0].type.model.params[0]);
    }));

    it('findParamByAbsolutePath() should return null due to incorrect path', async(() => {
        expect(service.findParamByAbsolutePath(pageServiceProcessResponse['0'].result.inputCommandUri)).toEqual(null);
    }));

    it('processResponse() should call traverseOutput()', async(() => {
        spyOn(service, 'traverseOutput').and.callThrough();
        service.processResponse(pageServiceProcessResponse);
        expect(service.traverseOutput).toHaveBeenCalled();
    }));

    it('processResponse() should call traverseOutput() with null rootparam', async(() => {
        const response = Object.assign({}, pageServiceProcessResponse);
        response[0].result = { inputCommandUri: '/test', outputs: [] };
        spyOn(service, 'traverseOutput').and.callThrough();
        service.processResponse(response);
        expect(service.traverseOutput).toHaveBeenCalledWith([], null);
    }));

    it('processResponse() should call only logError()', async(() => {
        const response = Object.assign({}, pageServiceProcessResponse);
        response[0].b = ''
        spyOn(service, 'traverseOutput').and.callThrough();
        spyOn(service, 'logError').and.callThrough();
        service.processResponse(response);
        expect(service.traverseOutput).not.toHaveBeenCalled();
        expect(service.logError).toHaveBeenCalled();
    }));


    it('createGridData() should return empty gridData', async(() => {
        const rootParam = Object.assign({}, pageServiceTraverseParamPayload);
        const param = Object.assign({}, pageServiceCreateGridDataParam);
        const res = service.createGridData(pageServiceCreateGridDataGridElementParams, param);
        expect(param.collectionParams.length).toEqual(2);
        expect(res.leafState[0].nestedGridParam.length).toEqual(pageServiceCreateGridDataResult.leafState[0].nestedGridParam.length);
    }));

    it('traverseParam() should call updateParam()', async(() => {
        spyOn(service, 'updateParam').and.callThrough();
        service.traverseParam(pageServiceTraverseParam, pageServiceTraverseParamEventModel);
        expect(service.updateParam).toHaveBeenCalled();
    }));

}); 
