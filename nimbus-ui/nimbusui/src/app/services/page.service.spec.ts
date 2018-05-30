import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JL } from 'jsnlog';
import { SESSION_STORAGE, StorageServiceModule } from 'angular-webstorage-service';

import { PageService } from './page.service';
import { CustomHttpClient } from './httpclient.service';
import { LoaderService } from './loader.service';
import { ConfigService } from './config.service';
import { LoggerService } from './logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from './session.store';
import { ParamUtils } from './../shared/param-utils';

let http, backend, service, loggerService, sessionStoreService, loaderService, configService;

class MockLoggerService {
  error(a) {}
  info(a) {}
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
  getFlowConfig() {
    return {
      model: {
        params: [
          {
            config: {
              code: '',
              uiStyles: {
                name: 'ViewConfig.Page'
              }
            }
          }
        ]
      }
    };
  }

  getViewConfigById() {}
  setLayoutToAppConfig(a, b) {}
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
        PageService,
        CustomHttpClient
      ],
      imports: [HttpClientTestingModule, HttpModule, StorageServiceModule]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(PageService);
    loggerService = TestBed.get(LoggerService);
    sessionStoreService = TestBed.get(SessionStoreService);
    loaderService = TestBed.get(LoaderService);
    configService = TestBed.get(ConfigService);
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

    it('notifyErrorEvent() should update the errorMessageUpdate subject', async(() => {
      service.errorMessageUpdate = { next: () => {} };
      spyOn(service.errorMessageUpdate, 'next').and.callThrough();
      service.notifyErrorEvent({});
      expect(service.errorMessageUpdate.next).toHaveBeenCalled();
    }));

    it('buildFlowBaseURL() should return the baseUrl', async(() => {
      service.buildBaseURL = () => {
        return '123';
      };
      service.routeParams = { domain: 'test' };
      service.buildFlowBaseURL();
      expect(service.buildFlowBaseURL()).toEqual('123test');
    }));

    it('getFlowRootDomainId() should return the domainId', async(() => {
      expect(service.getFlowRootDomainId('')).toEqual('test');
    }));

    it('getLayoutConfigForFlow() should call the loadDomainFlowConfig()', async(() => {
      service.loadDomainFlowConfig = a => {};
      spyOn(service, 'loadDomainFlowConfig').and.callThrough();
      service.getLayoutConfigForFlow();
      expect(service.loadDomainFlowConfig).toHaveBeenCalled();
    }));

    it('loadFlowConfig() should call the executeHttp()', async(() => {
      const arg = { domain: 'test' };
      service.executeHttp = () => {};
      spyOn(service, 'executeHttp').and.callThrough();
      service.loadFlowConfig(arg);
      expect(service.executeHttp).toHaveBeenCalled();
    }));

    it('navigateToDefaultPageForFlow() should call the navigateToPage()', async(() => {
      service.getDetaultPageForFlow = () => {
        return 'test';
      };
      service.navigateToPage = () => {};
      spyOn(service, 'navigateToPage').and.callThrough();
      service.navigateToDefaultPageForFlow({}, '');
      expect(service.navigateToPage).toHaveBeenCalled();
    }));

    it('navigateToDefaultPageForFlow() should not call navigateToPage()', async(() => {
      service.getDetaultPageForFlow = () => {
        return false;
      };
      service.navigateToPage = () => {};
      spyOn(service, 'navigateToPage').and.callThrough();
      service.navigateToDefaultPageForFlow({}, '');
      expect(service.navigateToPage).not.toHaveBeenCalled();
    }));

    it('loadDefaultPageForConfig() should call getPageToNavigateTo() and navigateToDefaultPageForFlow()', async(() => {
      spyOn(service, 'getPageToNavigateTo').and.returnValue('');
      spyOn(service, 'navigateToDefaultPageForFlow').and.returnValue('');
      service.loadDefaultPageForConfig('t');
      expect(service.getPageToNavigateTo).toHaveBeenCalled();
      expect(service.navigateToDefaultPageForFlow).toHaveBeenCalled();
    }));

    it('navigateToPage() should update the config$', async(() => {
      spyOn(service.config$, 'next').and.callThrough();
      service.navigateToPage({}, 't');
      expect(service.config$.next).toHaveBeenCalled();
    }));

    it('getPageConfigById() should return true', async(() => {
      expect(service.getPageConfigById('', '')).toBeTruthy();
    }));

    it('processError() should call logError() and hideLoader()', async(() => {
      spyOn(service, 'logError').and.returnValue('');
      spyOn(service, 'hideLoader').and.returnValue('');
      service.processError('');
      expect(service.logError).toHaveBeenCalled();
      expect(service.hideLoader).toHaveBeenCalled();
    }));

    it('invokeFinally() should call loggerService.info() and hideLoader()', async(() => {
      spyOn(loggerService, 'info').and.returnValue('');
      spyOn(service, 'hideLoader').and.returnValue('');
      service.invokeFinally('');
      expect(loggerService.info).toHaveBeenCalled();
      expect(service.hideLoader).toHaveBeenCalled();
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
      const element = { config: { code: 'test' } };
      expect(service.matchNode(element, 'test')).toBeTruthy();
    }));

    it('matchNode() should return false', async(() => {
      const element = { config: { code: 'test1' } };
      expect(service.matchNode(element, 'test')).toBeFalsy();
    }));

    it('traverseParam() should call updateParam()', async(() => {
      const eve = { value: { path: 'tPath' } };
      spyOn(service, 'updateParam').and.returnValue('');
      service.traverseParam({}, eve);
      expect(service.updateParam).toHaveBeenCalled();
    }));

    it('createRowData() should return the updated row data', async(() => {
      const resObj = { type: { model: { params: true } } };
      const param = { leafState: { nestedGridParam: '' }, type: { model: { params: [resObj] } } };
      const result = service.createRowData(param);
      expect(result.nestedGridParam).toEqual([resObj]);
    }));

    it('createRowData() should return empty string', async(() => {
      const resObj = { type: { model: { params: false } } };
      const param = { leafState: { nestedGridParam: '' }, type: { model: { params: false } } };
      const result = service.createRowData(param);
      expect(result.nestedGridParam).toEqual('');
    }));

    it('getUpdatedParamPath() should return updated path', async(() => {
      const eveModel = 'test/123/#';
      expect(service.getUpdatedParamPath(eveModel)).toEqual('123');
    }));

    it('getUpdatedParamPath() should update the path without # and return it', async(() => {
      const eveModel = 'test/123/';
      expect(service.getUpdatedParamPath(eveModel)).toEqual('123/');
    }));

    it('traverseFlowConfig() should call traverseConfig()', async(() => {
      const evemodel = { value: { path: '' } };
      spyOn(service, 'traverseConfig').and.returnValue('');
      service.traverseFlowConfig(evemodel, '');
      expect(service.traverseConfig).toHaveBeenCalled();
    }));

    it('traverseFlowConfig() should throw an error', async(() => {
      const evemodel = { value: { path: '' } };
      spyOn(service, 'traverseConfig').and.returnValue('');
      spyOn(configService, 'getFlowConfig').and.returnValue(undefined);
      expect(() => {
        service.traverseFlowConfig(evemodel, '');
      }).toThrow();
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
      expect(service.executeHttp).toHaveBeenCalled();
      expect(service.executeHttp).toHaveBeenCalledWith('flow_/_nav?a=right&b=$execute', 'GET', null);
    }));

    it('getPageToNavigateTo() should call executeHttp() with flow_/ad/_nav?a=right&b=$execute url', async(() => {
      spyOn(service, 'executeHttp').and.callThrough();
      service.getPageToNavigateTo('right', 'flow_/ad');
      expect(service.executeHttp).toHaveBeenCalledWith('flow_/ad/_nav?a=right&b=$execute', 'GET', null);
    }));

    it('getPageToNavigateTo() should call executeHttp() with undefined//undefined:undefined/undefined/org/p/_nav?a=right&b=$execute url', async(() => {
      service.executeHttp = () => {};
      service.routeParams = { domain: '' };
      spyOn(service, 'executeHttp').and.returnValue('');
      service.getPageToNavigateTo('right', '');
      expect(service.executeHttp).toHaveBeenCalled();
      expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefined/org/p/_nav?a=right&b=$execute', 'GET', null);
    }));

    it('getFlowNameFromOutput() should return empty string', async(() => {
      expect(service.getFlowNameFromOutput(undefined)).toEqual('');
    }));

    it('getFlowNameFromOutput() should return updated flowname from getFlowNameFromPath()', async(() => {
      spyOn(service, 'getFlowNameFromPath').and.returnValue('test:123');
      expect(service.getFlowNameFromOutput('')).toEqual('test');
    }));

    it('getFlowNameFromOutput() should return updated flowname', async(() => {
      spyOn(service, 'getFlowNameFromPath').and.returnValue('test123');
      expect(service.getFlowNameFromOutput('')).toEqual('test123');
    }));

    it('getNestedElementParam() should call matchNode() and traverseNestedPath()', async(() => {
      spyOn(service, 'matchNode').and.returnValue(true);
      spyOn(service, 'traverseNestedPath').and.returnValue('test');
      const res = service.getNestedElementParam({}, 'test/123');
      expect(service.matchNode).toHaveBeenCalled();
      expect(service.traverseNestedPath).toHaveBeenCalled();
      expect(res).toEqual('test');
    }));

    it('getNestedElementParam() should only call matchNode()', async(() => {
      spyOn(service, 'matchNode').and.returnValue(false);
      spyOn(service, 'traverseNestedPath').and.returnValue('test');
      const res = service.getNestedElementParam({}, 'test/123');
      expect(service.matchNode).toHaveBeenCalled();
      expect(service.traverseNestedPath).not.toHaveBeenCalled();
      expect(res).toEqual(undefined);
    }));

    it('traverseConfig() should call traversePageConfig()', async(() => {
      spyOn(service, 'getUpdatedParamPath').and.returnValue('test/123');
      spyOn(service, 'traversePageConfig').and.returnValue('');
      const params = [{ config: { code: 'test' } }];
      service.traverseConfig(params, { value: { path: '/t' } });
      expect(service.traversePageConfig).toHaveBeenCalled();
    }));

    it('traverseConfig() should not call traversePageConfig()', async(() => {
      spyOn(service, 'getUpdatedParamPath').and.returnValue('test/123');
      spyOn(service, 'traversePageConfig').and.returnValue('');
      const params = [{ config: { code: 'test1' } }];
      service.traverseConfig(params, { value: { path: '/t' } });
      expect(service.traversePageConfig).not.toHaveBeenCalled();
    }));

    it('buildBaseURL() should return updated url from routeParams', async(() => {
      service.routeParams = { client: 123, app: 'test' };
      const res = service.buildBaseURL();
      expect(res.includes('123')).toBeTruthy();
    }));

    it('buildBaseURL() should return updated url including org string', async(() => {
      service.routeParams = null;
      const res = service.buildBaseURL();
      expect(res.includes('org')).toBeTruthy();
    }));

    it('loadDomainFlowConfig() should call executeHttp()', async(() => {
      spyOn(service, 'executeHttp').and.callThrough();
      service.loadDomainFlowConfig('test');
      expect(service.executeHttp).toHaveBeenCalled();
    }));

    it('loadDomainFlowConfig() should call executeHttp() with null session id', async(() => {
      spyOn(service, 'executeHttp').and.callThrough();
      service.loadDomainFlowConfig('test1');
      expect(service.executeHttp).toHaveBeenCalled();
    }));

    it('processResponse() should call traverseOutput()', async(() => {
      const response = [{ b: '$execute', result: '' }];
      spyOn(service, 'traverseOutput').and.returnValue('');
      service.processResponse(response);
      expect(service.traverseOutput).toHaveBeenCalled();
    }));

    it('processResponse() should call only logError()', async(() => {
      const response = [{ b: '' }];
      spyOn(service, 'traverseOutput').and.returnValue('');
      spyOn(service, 'logError').and.callThrough();
      service.processResponse(response);
      expect(service.traverseOutput).not.toHaveBeenCalled();
      expect(service.logError).toHaveBeenCalled();
    }));

    it('getDetaultPageForFlow() should return model.params.a value', async(() => {
      const pageParam = { config: { uiStyles: { attributes: { defaultPage: true }, name: 'ViewConfig.Page' } } };
      const model = { params: { a: pageParam } };
      const res = service.getDetaultPageForFlow(model);
      expect(res).toEqual(pageParam);
    }));

    it('getDetaultPageForFlow(null) should return undefined', async(() => {
      expect(service.getDetaultPageForFlow(null)).toEqual(undefined);
    }));

    it('setViewRootAndNavigate() should call the navigateToDefaultPageForFlow()', async(() => {
      const output = { rootDomainId: 'tRoot', value: { config: { type: { model: { uiStyles: { attributes: { layout: 'tLayout' } } } } }, type: { model: 'tModel' } } };
      spyOn(service, 'navigateToDefaultPageForFlow').and.callThrough();
      service.setViewRootAndNavigate(output, true, true);
      expect(service.navigateToDefaultPageForFlow).toHaveBeenCalled();
    }));

    it('traverseOutput() should call traverseFlowConfig()', async(() => {
      const outputs = [{ action: '', value: { path: '/test', type: { model: '' } } }];
      spyOn(service, 'traverseFlowConfig').and.returnValue('');
      service.traverseOutput(outputs);
      expect(service.traverseFlowConfig).toHaveBeenCalled();
    }));

    it('traverseOutput() should call navigateToPage()', async(() => {
      const outputs = [{ action: '_nav', value: { path: '/test', type: { model: '' } } }];
      spyOn(service, 'traverseFlowConfig').and.returnValue('');
      spyOn(service, 'navigateToPage').and.returnValue('');
      service.traverseOutput(outputs);
      expect(service.navigateToPage).toHaveBeenCalled();
    }));

    it('traverseOutput() should call setViewRootAndNavigate()', async(() => {
      const outputs = [{ action: '', value: null, outputs: [{ action: '_new', value: { path: '/ ' } }] }];
      spyOn(service, 'traverseFlowConfig').and.returnValue('');
      spyOn(service, 'setViewRootAndNavigate').and.returnValue('');
      service.traverseOutput(outputs);
      expect(service.setViewRootAndNavigate).toHaveBeenCalled();
    }));

    it('traverseOutput() should call the traverseFlowConfig() on _new action', async(() => {
      const outputs = [{ action: '', value: null, outputs: [{ action: '_new', value: { path: 'test' } }] }];
      spyOn(service, 'traverseFlowConfig').and.returnValue('');
      spyOn(service, 'setViewRootAndNavigate').and.returnValue('');
      service.traverseOutput(outputs);
      expect(service.traverseFlowConfig).toHaveBeenCalled();
    }));

    it('traverseOutput() should call setViewRootAndNavigate() on _get action', async(() => {
      const outputs = [{ action: '_get', value: { path: 'test', config: 'c', type: { model: 'm' } } }];
      spyOn(service, 'traverseFlowConfig').and.returnValue('');
      spyOn(service, 'setViewRootAndNavigate').and.returnValue('');
      service.traverseOutput(outputs);
      expect(service.setViewRootAndNavigate).toHaveBeenCalled();
    }));

    it('traverseOutput() should be call logError()', async(() => {
      const outputs = [{ action: '_get', value: { path: 'test', config: 'c', type: {} } }];
      spyOn(service, 'traverseFlowConfig').and.returnValue('');
      spyOn(service, 'setViewRootAndNavigate').and.returnValue('');
      spyOn(service, 'logError').and.callThrough();
      service.traverseOutput(outputs);
      expect(service.logError).toHaveBeenCalled();
    }));

    it('findMatchingPageConfigById() should call logError()', async(() => {
      spyOn(service, 'logError').and.callThrough();
      service.findMatchingPageConfigById('test', 'test:test');
      expect(service.logError).toHaveBeenCalled();
    }));

    it('processEvent() should call executeHttp() with undefined//undefined:undefined/undefined/org/p/test:test/123/_get?b=t url', async(() => {
      spyOn(service, 'executeHttp').and.callThrough();
      service.processEvent('/test/123', '', {}, 'GET', 't');
      expect(service.executeHttp).toHaveBeenCalled();
      expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefined/org/p/test:test/123/_get?b=t', 'GET', {});
    }));

    it('processEvent() should call executeHttp() with undefined//undefined:undefined/undefined/org/p/test:test/?123/_get&b=$executet url', async(() => {
      spyOn(service, 'executeHttp').and.callThrough();
      service.processEvent('/test/?123', undefined, {}, 'GET', 't');
      expect(service.executeHttp).toHaveBeenCalled();
      expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefined/org/p/test:test/?123/_get&b=$executet', 'GET', {});
    }));

    it('executeHttp() should call executeHttpGet()', async(() => {
      spyOn(service, 'executeHttpGet').and.callThrough();
      service.executeHttp('/test', 'GET', {});
      expect(service.executeHttpGet).toHaveBeenCalled();
      expect(service.executeHttpGet).toHaveBeenCalledWith('/test');
    }));

    it('executeHttp() should call executeHttpPost()', async(() => {
      spyOn(service, 'executeHttpPost').and.callThrough();
      service.executeHttp('/test', 'POST', {});
      expect(service.executeHttpPost).toHaveBeenCalled();
      expect(service.executeHttpPost).toHaveBeenCalledWith('/test', {});
    }));

    it('executeHttp() should throw an error', async(() => {
      expect(() => {
        service.executeHttp('/test', 'DELETE', {});
      }).toThrow();
    }));

    it('postOnChange() should call executeHttp()', async(() => {
      spyOn(service, 'executeHttp').and.callThrough();
      service.postOnChange('test/123', 't', 'a');
      expect(service.executeHttp).toHaveBeenCalled();
    }));

    it('traversePageConfig() should call processModelEvent()', async(() => {
      const rootParam = { config: { code: '' }, type: { model: { params: [{ config: { code: undefined, type: { collection: 'c' } } }] } } };
      const eventModel = { value: { path: '', collectionElem: true } };
      spyOn(service, 'processModelEvent').and.callThrough();
      service.traversePageConfig(rootParam, eventModel, 0);
      expect(service.processModelEvent).toHaveBeenCalled();
    }));

    it('traversePageConfig() should call traversePageConfig()', async(() => {
      const rootParam = { config: { code: undefined }, type: { model: { params: [{ config: { code: undefined, type: { collection: true } } }] } } };
      const eventModel = { value: { path: '', collectionElem: false } };
      spyOn(service, 'traversePageConfig').and.callThrough();
      service.traversePageConfig(rootParam, eventModel, -2);
      expect(service.traversePageConfig).toHaveBeenCalled();
    }));

    it('traversePageConfig() should call traversePageConfig() based on updated param path', async(() => {
      const rootParam = { config: { code: '123' }, type: { model: { params: [{ config: { code: '123' }, type: { collection: false, model: { params: [] } } }] } } };
      const eventModel = { value: { path: 'test/123/a', collectionElem: false } };
      spyOn(service, 'traversePageConfig').and.callThrough();
      spyOn(service, 'getUpdatedParamPath').and.returnValue('123/123/123/a');
      service.traversePageConfig(rootParam, eventModel, 0);
      expect(service.traversePageConfig).toHaveBeenCalled();
    }));

    it('createGridData() should return [{}]', async(() => {
      const gridElementParams = [''];
      const gridParam = { path: '', collectionParams: '' };
      spyOn(service, 'createRowData').and.returnValue({
        nestedGridParam: true
      });
      const res = service.createGridData(gridElementParams, gridParam);
      expect(res).toEqual([{}]);
    }));

    it('processModelEvent() should update the param.gridlist', async(() => {
      const eventModel = { value: { page: '', type: { model: { params: '' } }, collectionElem: true, path: '/test' } };
      const param = { gridList: [], path: '/test', config: { type: { collection: '' }, uiStyles: { attributes: { alias: 'Grid' } } } };
      spyOn(service, 'traverseParam').and.callThrough();
      service.processModelEvent(param, eventModel);
      expect(param.gridList).toEqual([[]]);
    }));

    it('processModelEvent() should update param.gridlist and call createGridData()', async(() => {
      const eventModel = { value: { page: '', type: { model: { params: '' } }, collectionElem: true, path: '/test' } };
      const param = { gridList: null, path: '/test', config: { type: { collection: '' }, uiStyles: { attributes: { alias: 'Grid' } } } };
      spyOn(service, 'traverseParam').and.callThrough();
      spyOn(service, 'createGridData').and.callThrough();
      service.processModelEvent(param, eventModel);
      expect(param.gridList).toEqual([]);
      expect(service.createGridData).toHaveBeenCalled();
    }));

    it('processModelEvent() should not update the param.page', async(() => {
      const eventModel = { value: { page: true, type: { model: { params: '' } }, collectionElem: true, path: '/test' } };
      const param = { page: '', gridList: null, path: '/test', config: { type: { collection: true }, uiStyles: { attributes: { alias: 'Grid' } } } };
      spyOn(service, 'traverseParam').and.callThrough();
      service.processModelEvent(param, eventModel);
      expect(param.page).not.toEqual('');
    }));

    it('processModelEvent() should call loggerService.error()', async(() => {
      const eventModel = { value: { page: true, type: { model: { params: '' } }, collectionElem: true, path: '/test1' } };
      const param = { gridList: [{ elemId: '' }], path: '/test', config: { type: { collection: true }, uiStyles: { attributes: { alias: 'Grid' } } } };
      spyOn(service, 'traverseParam').and.callThrough();
      spyOn(loggerService, 'error').and.callThrough();
      service.processModelEvent(param, eventModel);
      expect(loggerService.error).toHaveBeenCalled();
    }));

    it('processModelEvent() should update gridValueUpdate subject', async(() => {
      const eventModel = { value: { page: true, type: { model: { params: '' } }, collectionElem: true, path: '/test1' } };
      const param = { gridList: [{ elemId: '' }], path: '/test', config: { type: { collection: true }, uiStyles: { attributes: { alias: 'Grid' } } } };
      spyOn(service, 'traverseParam').and.callThrough();
      spyOn(loggerService, 'error').and.callThrough();
      spyOn(service, 'getNestedElementParam').and.returnValue({
        collectionParams: ''
      });
      spyOn(service.gridValueUpdate, 'next').and.callThrough();
      service.processModelEvent(param, eventModel);
      expect(service.gridValueUpdate.next).toHaveBeenCalled();
    }));

    it('processModelEvent() should update the param.type.model.params', async(() => {
      const eventModel = { value: { page: '', type: { model: { params: '' } }, collectionElem: true, path: '/test' } };
      const param = { type: { model: { params: '' } }, gridList: { push: () => {} }, path: '/test', config: { type: { collection: true }, uiStyles: { attributes: { alias: 'CardDetailsGrid' } } } };
      spyOn(service, 'traverseParam').and.callThrough();
      service.processModelEvent(param, eventModel);
      expect(param.type.model['params']).toEqual([]);
    }));

    it('processModelEvent() should call traverseParam()', async(() => {
      const eventModel = { value: { page: '', type: { model: { params: '' } }, collectionElem: true, path: '/test' } };
      const param = { type: { model: { params: '' } }, gridList: { push: () => {} }, path: '/test', config: { type: { collection: false }, uiStyles: { attributes: { alias: 'CardDetailsGrid' } } } };
      spyOn(service, 'traverseParam').and.callThrough();
      service.processModelEvent(param, eventModel);
      expect(service.traverseParam).toHaveBeenCalled();
    }));

    it('traverseNestedPath() should return undefined', async(() => {
      const nestedEle = { type: { model: { params: [{}] } } };
      spyOn(service, 'matchNode').and.returnValue(false);
      const res = service.traverseNestedPath(nestedEle, 1, 'a');
      expect(res).toEqual(undefined);
    }));

    it('traverseNestedPath() should return res woth params as empty array', async(() => {
      const nestedEle = { type: { model: { params: [{ type: { model: { params: [] } } }] } } };
      spyOn(service, 'matchNode').and.returnValue(true);
      const res = service.traverseNestedPath(nestedEle, 0, 'a');
      expect(res.type.model.params).toEqual([]);
    }));

    it('traverseNestedPath() should call traverseNestedPath() 2 times', async(() => {
      const nestedEle = { type: { model: { params: [{ type: { model: { params: [] } } }] } } };
      spyOn(service, 'matchNode').and.returnValue(true);
      spyOn(service, 'traverseNestedPath').and.callThrough();
      const res = service.traverseNestedPath(nestedEle, 1, 'a');
      expect(service.traverseNestedPath).toHaveBeenCalledTimes(2);
    }));

    it('updateParam() should call updateNestedParameters()', async(() => {
      const rParam = { configId: '', path: '' };
      spyOn(configService, 'getViewConfigById').and.returnValue('a');
      spyOn(service, 'updateNestedParameters').and.callThrough();
      service.updateParam({}, rParam);
      expect(service.updateNestedParameters).toHaveBeenCalled();
    }));

    it('updateNestedParameters() should call updateParam()', async(() => {
      const sParam = { type: { model: { params: { a: 'b' } } } };
      const rParam = { type: { model: { params: { b: 'a', a: 'c' } } } };
      spyOn(service, 'updateParam').and.returnValue('');
      service.updateNestedParameters(sParam, rParam);
      expect(service.updateParam).toHaveBeenCalled();
    }));

    it('executeHttpGet() should make a get call', async(() => {
      service.executeHttpGet('/test');
      const req = backend.expectOne('/test');
      expect(req.request.method).toEqual('GET');
      req.flush({ sessionId: 123, result: '' });
      backend.verify();
    }));

    it('executeHttpPost() should make a post call', async(() => {
      service.executeHttpPost('/test', {});
      const req = backend.expectOne('/test');
      expect(req.request.method).toEqual('POST');
      req.flush({ sessionId: 123, result: '' });
      backend.verify();
    }));

    it('executeHttpPost() should call processError()', async(() => {
      spyOn(service, 'processError').and.callThrough();
      service.executeHttpPost('/test', {});
      const req = backend.expectOne('/test');
      expect(req.request.method).toEqual('POST');
      req.flush({ errorMessage: 'Uh oh!' }, { status: 500, statusText: 'Server Error' });
      backend.verify();
      expect(service.processError).toHaveBeenCalled();
    }));

    it('executeHttpGet() should call processError()', async(() => {
      spyOn(service, 'processError').and.callThrough();
      service.executeHttpGet('/test');
      const req = backend.expectOne('/test');
      expect(req.request.method).toEqual('GET');
      req.flush({ errorMessage: 'Uh oh!' }, { status: 500, statusText: 'Server Error' });
      backend.verify();
      expect(service.processError).toHaveBeenCalled();
    }));

    it('getFlowLayoutConfig() should return layout from configservice.getFlowConfig()', async(() => {
      spyOn(configService, 'getFlowConfig').and.returnValue({
        layout: 'rlayout',
        model: ''
      });
      spyOn(service, 'navigateToDefaultPageForFlow').and.returnValue('');
      service.getFlowLayoutConfig('test', true).then(data => {
        expect(data).toEqual('rlayout');
      });
    }));

});