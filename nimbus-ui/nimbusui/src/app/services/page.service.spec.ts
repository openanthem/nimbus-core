import { ParamConfig } from './../shared/param-config';
import { TestBed, async } from '@angular/core/testing';
import { HttpClient } from '@angular/common/http';
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
import { Param, Type, Model } from '../shared/param-state';
import { ExecuteException } from '../shared/app-config.interface';
import { Message } from './../shared/message';

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

  getFlowConfig(flowName) {    
    if (this.flowConfigs[flowName]) {
            return this.flowConfigs[flowName];
    } else {
            return undefined;
    }
}
  // getFlowConfig() {
  //   return {
  //     model: {
  //       params: [
  //         {
  //           config: {
  //             code: '',
  //             uiStyles: {
  //               name: 'ViewConfig.Page'
  //             }
  //           }
  //         }
  //       ]
  //     }
  //   };
  // }

  getViewConfigById() {
    return {
      uiStyles: {
        attributes: {}
      }
    }
  };
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
        { provide: Location, useClass: MockLocation},
        PageService,
        CustomHttpClient
        // ConfigService
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

    it('notifyErrorEvent() should update the messageEvent subject', async(() => {
      service.messageEvent = { next: () => {} };
      spyOn(service.messageEvent, 'next').and.callThrough();
      let execExp: ExecuteException = new ExecuteException();
      execExp.message = "test exception"; 
      execExp.code = "404";
      const message = new Message();
      message.messageArray = [ ({ severity: 'error', summary: 'Error Message', detail: 'test exception', life: 10000 }) ];
      message.context = 'TOAST';
      service.notifyErrorEvent(execExp);
      expect(service.messageEvent.next).toHaveBeenCalledWith([message]);
    }));

  //   it('buildFlowBaseURL() should return the baseUrl', async(() => {
  //     service.buildBaseURL = () => {
  //       return '123';
  //     };
  //     service.routeParams = { domain: 'test' };
  //     service.buildFlowBaseURL();
  //     expect(service.buildFlowBaseURL()).toEqual('123test');
  //   }));

    it('getFlowRootDomainId() should return the domainId', async(() => {
      expect(service.getFlowRootDomainId('')).toEqual('test');
    }));

    it('getLayoutConfigForFlow() should call the loadDomainFlowConfig()', async(() => {
      spyOn(service, 'loadDomainFlowConfig').and.callThrough();
      service.getLayoutConfigForFlow('testingFlowName');
      expect(service.loadDomainFlowConfig).toHaveBeenCalledWith('testingFlowName');
    }));

  //   it('loadFlowConfig() should call the executeHttp()', async(() => {
  //     const arg = { domain: 'test' };
  //     service.executeHttp = () => {};
  //     spyOn(service, 'executeHttp').and.callThrough();
  //     service.loadFlowConfig(arg);
  //     expect(service.executeHttp).toHaveBeenCalled();
  //   }));

    it('navigateToDefaultPageForFlow() should call the navigateToPage()', async(() => {
      service.getDetaultPageForFlow = () => {
        return 'test';
      };
      service.navigateToPage = () => {};
      spyOn(service, 'navigateToPage').and.callThrough();
      spyOn(service, 'getDetaultPageForFlow').and.callThrough();
      service.navigateToDefaultPageForFlow({'a': 'b'}, '');
      expect(service.navigateToPage).toHaveBeenCalledWith('test');
      expect(service.getDetaultPageForFlow).toHaveBeenCalledWith({'a': 'b'});

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

  //   it('loadDefaultPageForConfig() should call getPageToNavigateTo() and navigateToDefaultPageForFlow()', async(() => {
  //     spyOn(service, 'getPageToNavigateTo').and.returnValue('');
  //     spyOn(service, 'navigateToDefaultPageForFlow').and.returnValue('');
  //     service.loadDefaultPageForConfig('t');
  //     expect(service.getPageToNavigateTo).toHaveBeenCalled();
  //     expect(service.navigateToDefaultPageForFlow).toHaveBeenCalled();
  //   }));

  //   it('navigateToPage() should update the config$', async(() => {
  //     spyOn(service.config$, 'next').and.callThrough();
  //     service.navigateToPage({}, 't');
  //     expect(service.config$.next).toHaveBeenCalled();
  //   }));

  //   it('getPageConfigById() should return true', async(() => {
  //     expect(service.getPageConfigById('', '')).toBeTruthy();
  //   }));

  //   it('processError() should call logError() and hideLoader()', async(() => {
  //     spyOn(service, 'logError').and.returnValue('');
  //     spyOn(service, 'hideLoader').and.returnValue('');
  //     service.processError('');
  //     expect(service.logError).toHaveBeenCalled();
  //     expect(service.hideLoader).toHaveBeenCalled();
  //   }));

  //   it('invokeFinally() should call loggerService.info() and hideLoader()', async(() => {
  //     spyOn(loggerService, 'info').and.returnValue('');
  //     spyOn(service, 'hideLoader').and.returnValue('');
  //     service.invokeFinally('');
  //     expect(loggerService.info).toHaveBeenCalled();
  //     expect(service.hideLoader).toHaveBeenCalled();
  //   }));

  //   it('showLoader() should call loaderService.show()', async(() => {
  //     spyOn(loaderService, 'show').and.callThrough();
  //     service.showLoader();
  //     expect(loaderService.show).toHaveBeenCalled();
  //   }));

  //   it('hideLoader() should call loaderService.hide()', async(() => {
  //     spyOn(loaderService, 'hide').and.callThrough();
  //     service.hideLoader();
  //     expect(loaderService.hide).toHaveBeenCalled();
  //   }));

  //   it('matchNode() should return true', async(() => {
  //     const element = { config: { code: 'test' } };
  //     expect(service.matchNode(element, 'test')).toBeTruthy();
  //   }));

  //   it('matchNode() should return false', async(() => {
  //     const element = { config: { code: 'test1' } };
  //     expect(service.matchNode(element, 'test')).toBeFalsy();
  //   }));

  //   it('traverseParam() should call updateParam()', async(() => {
  //     const eve = { value: { path: 'tPath' } };
  //     spyOn(service, 'updateParam').and.returnValue('');
  //     service.traverseParam({}, eve);
  //     expect(service.updateParam).toHaveBeenCalled();
  //   }));

  // /*  it('createRowData() should return the updated row data', async(() => {
  //     const resObj = { type: { model: { params: true } } };
  //     const param = { leafState: { nestedGridParam: '' }, type: { model: { params: [resObj] } } };
  //     const result = service.createRowData(param);
  //     expect(result.nestedGridParam).toEqual('');
  //   })); 

  //   it('createRowData() should return the updated row data even when nestedGridParam is not a collection', async(() => {
  //     const resObj = { type: { model: { params: true } } };
  //     const nestedGridParams = [ {alias: 'Button', configId: 1 }, {alias: 'Link', configId: 2 } ];
  //     // tslint:disable-next-line:max-line-length
  //     const nestedParams = [{ alias: 'name', leafState: 'John' } , { alias: 'Button', configId: 1 , type : { collection : false} } , { alias: 'Link', configId: 2 , type : { collection : false} } ];
  //     // tslint:disable-next-line:max-line-length
  //     const inputparam = { leafState: { nestedGridParam: [nestedGridParams] }, type: { model: { params: [nestedParams] } } };
  //     const result = service.createRowData(inputparam);
  //     expect(result.nestedGridParam).toEqual([nestedGridParams]);
  //   })); */

  //   it('getUpdatedParamPath() should return updated path', async(() => {
  //     const eveModel = 'test/123/#';
  //     expect(service.getUpdatedParamPath(eveModel)).toEqual('123');
  //   }));

  //   it('getUpdatedParamPath() should update the path without # and return it', async(() => {
  //     const eveModel = 'test/123/';
  //     expect(service.getUpdatedParamPath(eveModel)).toEqual('123/');
  //   }));

  //   it('traverseFlowConfig() should call traverseConfig()', async(() => {
  //     const evemodel = { value: { path: '' } };
  //     spyOn(service, 'traverseConfig').and.returnValue('');
  //     service.traverseFlowConfig(evemodel, '');
  //     expect(service.traverseConfig).toHaveBeenCalled();
  //   }));

  //   it('traverseFlowConfig() should throw an error', async(() => {
  //     const evemodel = { value: { path: '' } };
  //     spyOn(service, 'traverseConfig').and.returnValue('');
  //     spyOn(configService, 'getFlowConfig').and.returnValue(undefined);
  //     spyOn(loggerService, 'warn').and.callThrough();
  //     service.traverseFlowConfig(evemodel, '');
  //     expect(loggerService.warn).toHaveBeenCalled();
  //   }));

  //   it('getFlowNameFromPath() should updated flow name', async(() => {
  //     expect(service.getFlowNameFromPath('test/p/t')).toEqual('t');
  //   }));

  //   it('getFlowNameFromPath() should updated flow name based on /p/ index', async(() => {
  //     expect(service.getFlowNameFromPath('test/p/t123')).toEqual('t123');
  //   }));

  //   it('getFlowNameFromPath() should updated flow name p', async(() => {
  //     expect(service.getFlowNameFromPath('/p/t123')).toEqual('p');
  //   }));

  //   it('getPageToNavigateTo() should call executeHttp() with flow_/_nav?a=right&b=$execute url', async(() => {
  //     spyOn(service, 'executeHttp').and.callThrough();
  //     service.getPageToNavigateTo('right', 'flow_');
  //     expect(service.executeHttp).toHaveBeenCalled();
  //     expect(service.executeHttp).toHaveBeenCalledWith('flow_/_nav?a=right&b=$execute', 'GET', null);
  //   }));

  //   it('getPageToNavigateTo() should call executeHttp() with flow_/ad/_nav?a=right&b=$execute url', async(() => {
  //     spyOn(service, 'executeHttp').and.callThrough();
  //     service.getPageToNavigateTo('right', 'flow_/ad');
  //     expect(service.executeHttp).toHaveBeenCalledWith('flow_/ad/_nav?a=right&b=$execute', 'GET', null);
  //   }));

  //   it('getPageToNavigateTo() should call executeHttp() with /1flow_/_nav?a=right&b=$execute url', async(() => {
  //     service.executeHttp = () => {};
  //     service.routeParams = { domain: '' };
  //     spyOn(service, 'executeHttp').and.returnValue('');
  //     service.getPageToNavigateTo('right', '/1flow_');
  //     expect(service.executeHttp).toHaveBeenCalled();
  //     expect(service.executeHttp).toHaveBeenCalledWith('/1flow_/_nav?a=right&b=$execute', 'GET', null);
  //   }));

  //   it('getFlowNameFromOutput() should return empty string', async(() => {
  //     expect(service.getFlowNameFromOutput(undefined)).toEqual('');
  //   }));

  //   it('getFlowNameFromOutput() should return updated flowname from getFlowNameFromPath()', async(() => {
  //     spyOn(service, 'getFlowNameFromPath').and.returnValue('test:123');
  //     expect(service.getFlowNameFromOutput('')).toEqual('test');
  //   }));

  //   it('getFlowNameFromOutput() should return updated flowname', async(() => {
  //     spyOn(service, 'getFlowNameFromPath').and.returnValue('test123');
  //     expect(service.getFlowNameFromOutput('')).toEqual('test123');
  //   }));

  //   it('getNestedElementParam() should call matchNode() and traverseNestedPath()', async(() => {
  //     spyOn(service, 'matchNode').and.returnValue(true);
  //     spyOn(service, 'traverseNestedPath').and.returnValue('test');
  //     const res = service.getNestedElementParam({}, 'test/123');
  //     expect(service.matchNode).toHaveBeenCalled();
  //     expect(service.traverseNestedPath).toHaveBeenCalled();
  //     expect(res).toEqual('test');
  //   }));

  //   it('getNestedElementParam() should only call matchNode()', async(() => {
  //     spyOn(service, 'matchNode').and.returnValue(false);
  //     spyOn(service, 'traverseNestedPath').and.returnValue('test');
  //     const res = service.getNestedElementParam({}, 'test/123');
  //     expect(service.matchNode).toHaveBeenCalled();
  //     expect(service.traverseNestedPath).not.toHaveBeenCalled();
  //     expect(res).toEqual(undefined);
  //   }));

  //   it('traverseConfig() should call traversePageConfig()', async(() => {
  //     spyOn(service, 'getUpdatedParamPath').and.returnValue('test/123');
  //     spyOn(service, 'traversePageConfig').and.returnValue('');
  //     const params: any = [{ config: { code: 'test' } }];
  //     service.traverseConfig(params, { value: { path: '/t' } });
  //     expect(service.traversePageConfig).toHaveBeenCalled();
  //   }));

  //   it('traverseConfig() should not call traversePageConfig()', async(() => {
  //     spyOn(service, 'getUpdatedParamPath').and.returnValue('test/123');
  //     spyOn(service, 'traversePageConfig').and.returnValue('');
  //     const params = [{ config: { code: 'test1' } }];
  //     service.traverseConfig(params, { value: { path: '/t' } });
  //     expect(service.traversePageConfig).not.toHaveBeenCalled();
  //   }));

  //   it('buildBaseURL() should return updated url from routeParams', async(() => {
  //     service.routeParams = { client: 123, app: 'test' };
  //     const res = service.buildBaseURL();
  //     expect(res.includes('123')).toBeTruthy();
  //   }));

  //   it('buildBaseURL() should return updated url including org string', async(() => {
  //     service.routeParams = null;
  //     const res = service.buildBaseURL();      
  //     expect(res.includes('undefined/')).toBeTruthy();
  //   }));

  //   it('loadDomainFlowConfig() should call executeHttp()', async(() => {
  //     spyOn(service, 'executeHttp').and.callThrough();
  //     service.loadDomainFlowConfig('test');
  //     expect(service.executeHttp).toHaveBeenCalled();
  //   }));

  //   it('loadDomainFlowConfig() should call executeHttp() with null session id', async(() => {
  //     spyOn(service, 'executeHttp').and.callThrough();
  //     service.loadDomainFlowConfig('test1');
  //     expect(service.executeHttp).toHaveBeenCalled();
  //   }));

//     it('getDetaultPageForFlow() should return model.params.a value', async(() => {
//       const pageParam = { config: { uiStyles: { attributes: { defaultPage: true }, name: 'ViewConfig.Page' } } };
//       const model = { params: { a: pageParam } };
//       const res = service.getDetaultPageForFlow(model);
//       expect(res).toEqual(pageParam);
//     }));

//     it('getDetaultPageForFlow(null) should return undefined', async(() => {
//       expect(service.getDetaultPageForFlow(null)).toEqual(undefined);
//     }));

//     it('setViewRootAndNavigate() should call the navigateToDefaultPageForFlow()', async(() => {
//       const output = { rootDomainId: 'tRoot', value: { config: { type: { model: { uiStyles: { attributes: { layout: 'tLayout' } } } } }, type: { model: 'tModel' } } };
//       spyOn(service, 'navigateToDefaultPageForFlow').and.callThrough();
//       service.setViewRootAndNavigate(output, true, true);
//       expect(service.navigateToDefaultPageForFlow).toHaveBeenCalled();
//     }));

//     it('traverseOutput() should call traverseFlowConfig()', async(() => {
//       const outputs = [{ action: '', value: { path: '/test', type: { model: '' } } }];
//       spyOn(service, 'traverseFlowConfig').and.returnValue('');
//       service.traverseOutput(outputs);
//       expect(service.traverseFlowConfig).toHaveBeenCalled();
//     }));

//     it('traverseOutput() should call navigateToPage()', async(() => {
//       const outputs = [{ action: '_nav', value: { path: '/test', type: { model: '' } } }];
//       spyOn(service, 'traverseFlowConfig').and.returnValue('');
//       spyOn(service, 'navigateToPage').and.returnValue('');
//       service.traverseOutput(outputs);
//       expect(service.navigateToPage).toHaveBeenCalled();
//     }));

//     it('traverseOutput() should call setViewRootAndNavigate()', async(() => {
//       const outputs = [{ action: '', value: null, outputs: [{ action: '_new', value: { path: '/ ' } }] }];
//       spyOn(service, 'traverseFlowConfig').and.returnValue('');
//       spyOn(service, 'setViewRootAndNavigate').and.returnValue('');
//       service.traverseOutput(outputs);
//       expect(service.setViewRootAndNavigate).toHaveBeenCalled();
//     }));

//     /* TODO - expect(location.back).toHaveBeenCalled(); is not working
//     it('traverseOutput() should call location back()', async(() => {
//       const outputs = [{ action: '_get', value: { path: '/test', type: { model: '' } } }];
//       const rootparam = {config: { uiStyles : { attributes : { alias : 'Button', browserBack : true}}}};
//       service.traverseOutput(outputs, rootparam);
//       spyOn(location, 'back').and.callThrough();
//       expect(location.back).toHaveBeenCalled();
//     }));

//     it('traverseOutput() should call location back() only once', async(() => {
//       const outputs = [{ action: '_get' , value: true , outputs : []}];
//       const rootparam = {config: { uiStyles : { attributes : { alias : 'Button', browserBack : true}}}};
//       service.traverseOutput(outputs, rootparam);
//       spyOn(location, 'back').and.callThrough();
//       expect(location.back).toHaveBeenCalledTimes(1);
//     }));
//     */
//     it('traverseOutput() should not call location back()', async(() => {
//       const outputs = [{ action: '_get', value: { path: '/test', type: { model: '' } } }];
//       const rootparam = {config: { uiStyles : { attributes : { alias : 'Button', browserBack : false}}}};
//       spyOn(service, 'traverseFlowConfig').and.returnValue('');
//       service.traverseOutput(outputs, rootparam);
//       spyOn(location, 'back').and.callThrough();
//       expect(location.back).not.toHaveBeenCalled();
//     }));

//     it('traverseOutput() should not call navigateToPage() when browserBack is true', async(() => {
//       const outputs = [{ action: '_nav', value: { path: '/test', type: { model: '' } } }];
//       const rootparam = {config: { uiStyles : { attributes : { alias : 'Button', browserBack : true}}}};
//       spyOn(service, 'traverseFlowConfig').and.returnValue('');
//       spyOn(service, 'navigateToPage').and.returnValue('');
//       service.traverseOutput(outputs, rootparam);
//       expect(service.navigateToPage).not.toHaveBeenCalled();
//     }));

//     it('traverseOutput() should call navigateToPage() when browserBack is false', async(() => {
//       const outputs = [{ action: '_nav', value: { path: '/test', type: { model: '' } } }];
//       const rootparam = {config: { uiStyles : { attributes : { alias : 'Button', browserBack : false}}}};
//       spyOn(service, 'traverseFlowConfig').and.returnValue('');
//       spyOn(service, 'navigateToPage').and.returnValue('');
//       service.traverseOutput(outputs, rootparam);
//       expect(service.navigateToPage).toHaveBeenCalled();
//     }));

//     it('traverseOutput() should call the traverseFlowConfig() on _new action', async(() => {
//       const outputs = [{ action: '', value: null, outputs: [{ action: '_new', value: { path: 'test' } }] }];
//       spyOn(service, 'traverseFlowConfig').and.returnValue('');
//       spyOn(service, 'setViewRootAndNavigate').and.returnValue('');
//       service.traverseOutput(outputs);
//       expect(service.traverseFlowConfig).toHaveBeenCalled();
//     }));

//     it('traverseOutput() should call setViewRootAndNavigate() on _get action', async(() => {
//       const outputs = [{ action: '_get', value: { path: 'test', config: 'c', type: { model: 'm' } } }];
//       spyOn(service, 'traverseFlowConfig').and.returnValue('');
//       spyOn(service, 'setViewRootAndNavigate').and.returnValue('');
//       service.traverseOutput(outputs);
//       expect(service.setViewRootAndNavigate).toHaveBeenCalled();
//     }));



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

    //     it('processEvent() should call executeHttp() with undefined//undefined:undefined/undefinedundefined/p/test:test/123/_get?b=t', async(() => {
//       spyOn(service, 'executeHttp').and.callThrough();
//       service.processEvent('/test/123', '', {}, 'GET', 't');
//       expect(service.executeHttp).toHaveBeenCalled();
//       expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefinedundefined/p/test:test/123/_get?b=t', 'GET', {}, '/test/123');
//     }));

//     it('processEvent() should call executeHttp() with undefined//undefined:undefined/undefinedundefined/p/test:test/?123/_get&b=$executet url', async(() => {
//       spyOn(service, 'executeHttp').and.callThrough();
//       service.processEvent('/test/?123', undefined, {}, 'GET', 't');
//       expect(service.executeHttp).toHaveBeenCalled();
//       expect(service.executeHttp).toHaveBeenCalledWith('undefined//undefined:undefined/undefinedundefined/p/test:test/?123/_get&b=$executet', 'GET', {}, '/test/?123');
//     }));

//     it('executeHttp() should call executeHttpGet()', async(() => {
//       spyOn(service, 'executeHttpGet').and.callThrough();
//       service.executeHttp('/test', 'GET', {});
//       expect(service.executeHttpGet).toHaveBeenCalled();
//       expect(service.executeHttpGet).toHaveBeenCalledWith('/test', undefined);
//     }));

//     it('executeHttp() should call executeHttpPost()', async(() => {
//       spyOn(service, 'executeHttpPost').and.callThrough();
//       service.executeHttp('/test', 'POST', {});
//       expect(service.executeHttpPost).toHaveBeenCalled();
//       expect(service.executeHttpPost).toHaveBeenCalledWith('/test', {}, undefined);
//     }));

//     it('executeHttp() should throw an error', async(() => {
//       spyOn(loggerService, 'error').and.callThrough();
//         service.executeHttp('/test', 'DELETE', {});
//       expect(loggerService.error).toHaveBeenCalled();
//     }));

//     it('postOnChange() should call executeHttp()', async(() => {
//       spyOn(service, 'executeHttp').and.callThrough();
//       service.postOnChange('test/123', 't', 'a');
//       expect(service.executeHttp).toHaveBeenCalled();
//     }));

//     it('traversePageConfig() should call processModelEvent()', async(() => {
//       const rootParam = { config: { code: '' }, type: { model: { params: [{ config: { code: undefined, type: { collection: 'c' } } }] } } };
//       const eventModel = { value: { path: '', collectionElem: true } };
//       spyOn(service, 'processModelEvent').and.returnValue('');
//       service.traversePageConfig(rootParam, eventModel, 0);
//       expect(service.processModelEvent).toHaveBeenCalled();
//     }));

//     it('traversePageConfig() should call traversePageConfig()', async(() => {
//       const rootParam = { config: { code: undefined }, type: { model: { params: [{ config: { code: undefined, type: { collection: true } } }] } } };
//       const eventModel = { value: { path: '', collectionElem: false } };
//       spyOn(service, 'traversePageConfig').and.returnValue('');
//       service.traversePageConfig(rootParam, eventModel, -2);
//       expect(service.traversePageConfig).toHaveBeenCalled();
//     }));

//     it('traversePageConfig() should call traversePageConfig() based on updated param path', async(() => {
//       const rootParam = { config: { code: '123' }, type: { model: { params: [{ config: { code: '123' }, type: { collection: false, model: { params: [] } } }] } } };
//       const eventModel = { value: { path: 'test/123/a', collectionElem: false } };
//       spyOn(service, 'traversePageConfig').and.callThrough();
//       spyOn(service, 'getUpdatedParamPath').and.returnValue('123/123/123/a');
//       service.traversePageConfig(rootParam, eventModel, 0);
//       expect(service.traversePageConfig).toHaveBeenCalled();
//     }));

//     // it('processModelEvent() should call createGridData()', async(() => {
//     //   const eventModel = { value: { page: '', type: { model: { params: '' } }, collectionElem: true, path: '/test' } };
//     //   const param = { type: {model: {params: [{ type: {model: {}}, gridData: { leafState: null}, gridList: null, path: '/test', config: { type: { collection: '' } , model:{}, uiStyles: { attributes: { alias: 'Grid' } } } }]}, gridData: { leafState: null}, gridList: null, path: '/test', config: { type: { collection: '' } , model:{params : [{}]}, uiStyles: { attributes: { alias: 'Grid' } } } }};
//     //   spyOn(service, 'traverseParam').and.callThrough();
//     //   spyOn(service, 'createGridData').and.callThrough();
//     //   service.processModelEvent(param, eventModel);
//     //   expect(service.createGridData).toHaveBeenCalled();
//     // }));

//     it('processModelEvent() should not update the param.page', async(() => {
//       const eventModel = { value: { page: true, type: { model: { params: '' } }, collectionElem: true, path: '/test' } };
//       const param = { gridData: { leafState: null}, page: '', gridList: null, path: '/test', config: { type: { collection: true }, uiStyles: { attributes: { alias: 'Grid' } } } };
//       spyOn(service, 'traverseParam').and.callThrough();
//       service.processModelEvent(param, eventModel);
//       expect(param.page).not.toEqual('');
//     }));

//     // it('processModelEvent() should update gridValueUpdate subject', async(() => {
//     //   const eventModel = { value: { page: true, collectionElem: true, type: { model: { params: '' } }, path: '/test1' } };
//     //   const param = { gridData: { leafState: [{ elemId: '' }]}, gridList: [{ elemId: '' }], path: '/test', config: { type: { collection: true }, uiStyles: { attributes: { alias: 'Grid' } } } };
//     //   spyOn(service, 'traverseParam').and.callThrough();
//     //   spyOn(loggerService, 'error').and.callThrough();
//     //   spyOn(service, 'getNestedElementParam').and.returnValue({
//     //     collectionParams: ''
//     //   });
//     //   spyOn(service.gridValueUpdate, 'next').and.callThrough();
//     //   service.processModelEvent(param, eventModel);
//     //   expect(service.gridValueUpdate.next).toHaveBeenCalled();
//     // }));

//     it('processModelEvent() should update the param.type.model.params', async(() => {
//       const eventModel = { value: { page: '', type: { model: { params: '' } }, collectionElem: true, path: '/test' } };
//       const param = { type: { model: { params: '' } }, gridList: { push: () => {} }, path: '/test', config: { type: { collection: true }, uiStyles: { attributes: { alias: 'CardDetailsGrid' } } } };
//       spyOn(service, 'traverseParam').and.callThrough();
//       service.processModelEvent(param, eventModel);
//       expect(param.type.model['params']).toEqual([]);
//     }));

//     it('processModelEvent() should call traverseParam()', async(() => {
//       const eventModel = { value: { page: '', type: { model: { params: '' } }, collectionElem: true, path: '/test' } };
//       const param = { type: { model: { params: '' } }, gridList: { push: () => {} }, path: '/test', config: { type: { collection: false }, uiStyles: { attributes: { alias: 'CardDetailsGrid' } } } };
//       spyOn(service, 'traverseParam').and.callThrough();
//       service.processModelEvent(param, eventModel);
//       expect(service.traverseParam).toHaveBeenCalled();
//     }));

//     it('traverseNestedPath() should return undefined', async(() => {
//       const nestedEle = { type: { model: { params: [{}] } } };
//       spyOn(service, 'matchNode').and.returnValue(false);
//       const res = service.traverseNestedPath(nestedEle, 1, 'a');
//       expect(res).toEqual(undefined);
//     }));

//     it('traverseNestedPath() should return res with params as empty array', async(() => {
//       const nestedEle = { type: { model: { params: [{ gridData: {leafState: [{}]}, type: { model: { params: [] } } }] } } };
//       spyOn(service, 'matchNode').and.returnValue(true);
//       const res = service.traverseNestedPath(nestedEle, 0, 'a');
//       expect(res.type.model.params).toEqual([]);
//     }));

//     it('traverseNestedPath() should call traverseNestedPath() 2 times', async(() => {
//       const nestedEle = {gridData: {leafState: [{}]}, type: { model: { params: [{gridData: {leafState: [{}]}, type: { model: { params: [] } } }] } } };
//       spyOn(service, 'matchNode').and.returnValue(true);
//       spyOn(service, 'traverseNestedPath').and.callThrough();
//       const res = service.traverseNestedPath(nestedEle, 1, 'a');
//       expect(service.traverseNestedPath).toHaveBeenCalledTimes(2);
//     }));

//     it('updateParam() should call updateNestedParameters()', async(() => {
//       const rParam = { configId: '', path: '' };
//       spyOn(configService, 'getViewConfigById').and.returnValue('a');
//       spyOn(service, 'updateNestedParameters').and.returnValue('');
//       service.updateParam({}, rParam);
//       expect(service.updateNestedParameters).toHaveBeenCalled();
//     }));

//     it('updateNestedParameters() should call updateParam()', async(() => {
//       const sParam = { type: { model: { params: { a: 'b' } } } };
//       const rParam = { type: { model: { params: { b: 'a', a: 'c' } } } };
//       spyOn(service, 'updateParam').and.returnValue('');
//       service.updateNestedParameters(sParam, rParam);
//       expect(service.updateParam).toHaveBeenCalled();
//     }));

//     it('executeHttpGet() should make a get call', async(() => {
//       service.executeHttpGet('/test');
//       const req = backend.expectOne('/test');
//       expect(req.request.method).toEqual('GET');
//       req.flush({ sessionId: 123, result: '' });
//       backend.verify();
//     }));

//     it('executeHttpPost() should make a post call', async(() => {
//       service.executeHttpPost('/test', {});
//       const req = backend.expectOne('/test');
//       expect(req.request.method).toEqual('POST');
//       req.flush({ sessionId: 123, result: '' });
//       backend.verify();
//     }));

//     it('executeHttpPost() should call processError()', async(() => {
//       spyOn(service, 'processError').and.callThrough();
//       service.executeHttpPost('/test', {});
//       const req = backend.expectOne('/test');
//       expect(req.request.method).toEqual('POST');
//       req.flush({ errorMessage: 'Uh oh!' }, { status: 500, statusText: 'Server Error' });
//       backend.verify();
//       expect(service.processError).toHaveBeenCalled();
//     }));

//     it('executeHttpGet() should call processError()', async(() => {
//       spyOn(service, 'processError').and.callThrough();
//       service.executeHttpGet('/test');
//       const req = backend.expectOne('/test');
//       expect(req.request.method).toEqual('GET');
//       req.flush({ errorMessage: 'Uh oh!' }, { status: 500, statusText: 'Server Error' });
//       backend.verify();
//       expect(service.processError).toHaveBeenCalled();
//     }));

//     it('getFlowLayoutConfig() should return layout from configservice.getFlowConfig()', async(() => {
//       spyOn(configService, 'getFlowConfig').and.returnValue({
//         layout: 'rlayout',
//         model: ''
//       });
//       spyOn(service, 'navigateToDefaultPageForFlow').and.returnValue('');
//       service.getFlowLayoutConfig('test', true).then(data => {
//         expect(data).toEqual('rlayout');
//       });
//     }));


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
  // unable to write toHaveBeenCalledWith as config service is too lengthy in param and nested param objects
  // expect(service.traverseOutput).toHaveBeenCalledWith(pageServiceOutputs, pageServiceRootParam);
}));

it('processResponse() should call traverseOutput() with null rootparam', async(() => {
  const response = Object.assign({}, pageServiceProcessResponse);
  response[0].result = {  inputCommandUri: '/test', outputs : []};
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

}); 

describe('PageService_mock', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: 'JSNLOG', useValue: JL },
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        { provide: LoggerService, useClass: MockLoggerService },
        { provide: SessionStoreService, useClass: MockSessionStoreService },
        { provide: LoaderService, useClass: MockLoaderService },
        { provide: Location, useClass: MockLocation},
        ConfigService,
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
    configService_actual = TestBed.get(ConfigService);
    location = TestBed.get(Location);
  }); 

  // it('createGridData() should return empty gridData', async(() => {
  //   const gridElementParam = new Param(configService_actual);
  //   gridElementParam.leafState = {nestedGridParam : {type: {model: {nested: false}}}};
  //   gridElementParam.type = new Type(configService);
  //   gridElementParam.type.model = new Model(configService);
  //   gridElementParam.type.model.params = [];
  //   const gridParam = new Param(configService_actual);
  //   const config = new ParamConfig(configService_actual);
  //   spyOn(configService_actual, 'getViewConfigById').and.returnValue(config);
  //   const res = service.createGridData([gridElementParam], gridParam);
  //   expect(res.stateMap).toEqual([]);
  //   expect(res.leafState).toEqual([]);
  // }));

});



const pageServiceProcessResponse: any = {
  "0": {
      "b": "$execute",
      "result": {
          "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/_get",
          "action": "_get",
          "behaviors": [
              "$execute"
          ],
          "outputs": [
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/nickname",
                      "configId": "578",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Nickname"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/vlmCaseItemLinks/edit",
                      "configId": "585",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Edit"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/expandedRowContent/pets",
                      "configId": "593",
                      "page": {
                          "last": true,
                          "totalElements": 0,
                          "totalPages": 1,
                          "size": 0,
                          "number": 0,
                          "first": true,
                          "numberOfElements": 0
                      },
                      "elemLabels": {
                          "599": [
                              {
                                  "locale": "en-US",
                                  "text": "Pet's name"
                              }
                          ]
                      },
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Pets"
                          }
                      ],
                      "type": {
                          "model": {}
                      }
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/lastName",
                      "configId": "577",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Last Name"
                          }
                      ],
                      "leafState": "123"
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/telephone",
                      "configId": "582",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Telephone"
                          }
                      ],
                      "leafState": "1231231231"
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/vlmCaseItemLinks/ownerInfo",
                      "configId": "586",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Owner Info"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/lastName",
                      "configId": "577",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Last Name"
                          }
                      ],
                      "leafState": "1"
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/nickname",
                      "configId": "578",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Nickname"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/telephone",
                      "configId": "582",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Telephone"
                          }
                      ],
                      "leafState": "1231231231"
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/ownerCity",
                      "configId": "581",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Owner City"
                          }
                      ],
                      "leafState": ""
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/firstName",
                      "configId": "576",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "First Name"
                          }
                      ],
                      "leafState": "test"
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/status",
                      "configId": "580",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Status"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/expandedRowContent/pets",
                      "configId": "593",
                      "page": {
                          "last": true,
                          "totalElements": 0,
                          "totalPages": 1,
                          "size": 0,
                          "number": 0,
                          "first": true,
                          "numberOfElements": 0
                      },
                      "elemLabels": {
                          "599": [
                              {
                                  "locale": "en-US",
                                  "text": "Pet's name"
                              }
                          ]
                      },
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Pets"
                          }
                      ],
                      "type": {
                          "model": {}
                      }
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/status",
                      "configId": "580",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Status"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/edit",
                      "configId": "585",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Edit"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_new",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners",
                      "configId": "570",
                      "page": {
                          "last": true,
                          "totalElements": 3,
                          "totalPages": 1,
                          "size": 0,
                          "number": 0,
                          "first": true,
                          "numberOfElements": 3
                      },
                      "elemLabels": {
                          "576": [
                              {
                                  "locale": "en-US",
                                  "text": "First Name"
                              }
                          ],
                          "577": [
                              {
                                  "locale": "en-US",
                                  "text": "Last Name"
                              }
                          ],
                          "578": [
                              {
                                  "locale": "en-US",
                                  "text": "Nickname"
                              }
                          ],
                          "580": [
                              {
                                  "locale": "en-US",
                                  "text": "Status"
                              }
                          ],
                          "581": [
                              {
                                  "locale": "en-US",
                                  "text": "Owner City"
                              }
                          ],
                          "582": [
                              {
                                  "locale": "en-US",
                                  "text": "Telephone"
                              }
                          ]
                      },
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Owners"
                          }
                      ],
                      "type": {
                          "model": {
                              "params": [
                                  {
                                      "elemId": "0",
                                      "configId": "573",
                                      "collectionElem": true,
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "configId": "575",
                                                      "type": {},
                                                      "leafState": 52
                                                  },
                                                  {
                                                      "configId": "576",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "First Name"
                                                          }
                                                      ],
                                                      "leafState": "test"
                                                  },
                                                  {
                                                      "configId": "577",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Last Name"
                                                          }
                                                      ],
                                                      "leafState": "1"
                                                  },
                                                  {
                                                      "configId": "578",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Nickname"
                                                          }
                                                      ]
                                                  },
                                                  {
                                                      "configId": "579",
                                                      "type": {},
                                                      "leafState": false
                                                  },
                                                  {
                                                      "configId": "580",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Status"
                                                          }
                                                      ]
                                                  },
                                                  {
                                                      "configId": "581",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Owner City"
                                                          }
                                                      ],
                                                      "leafState": ""
                                                  },
                                                  {
                                                      "configId": "582",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Telephone"
                                                          }
                                                      ],
                                                      "leafState": "1231231231"
                                                  },
                                                  {
                                                      "configId": "583",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "configId": "585",
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "Edit"
                                                                          }
                                                                      ]
                                                                  },
                                                                  {
                                                                      "configId": "586",
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "Owner Info"
                                                                          }
                                                                      ]
                                                                  }
                                                              ]
                                                          }
                                                      }
                                                  },
                                                  {
                                                      "configId": "587",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "configId": "593",
                                                                      "page": {
                                                                          "last": true,
                                                                          "totalElements": 0,
                                                                          "totalPages": 1,
                                                                          "size": 0,
                                                                          "number": 0,
                                                                          "first": true,
                                                                          "numberOfElements": 0
                                                                      },
                                                                      "elemLabels": {
                                                                          "599": [
                                                                              {
                                                                                  "locale": "en-US",
                                                                                  "text": "Pet's name"
                                                                              }
                                                                          ]
                                                                      },
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "Pets"
                                                                          }
                                                                      ],
                                                                      "type": {
                                                                          "model": {}
                                                                      }
                                                                  }
                                                              ]
                                                          }
                                                      }
                                                  }
                                              ]
                                          }
                                      },
                                      "leafState": {
                                          "id": 52,
                                          "firstName": "test",
                                          "lastName": "1",
                                          "shouldUseNickname": false,
                                          "ownerCity": "",
                                          "telephone": "1231231231",
                                          "vlmCaseItemLinks": {},
                                          "expandedRowContent": {
                                              "pets": []
                                          }
                                      }
                                  },
                                  {
                                      "elemId": "1",
                                      "configId": "573",
                                      "collectionElem": true,
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "configId": "575",
                                                      "type": {},
                                                      "leafState": 51
                                                  },
                                                  {
                                                      "configId": "576",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "First Name"
                                                          }
                                                      ],
                                                      "leafState": "test"
                                                  },
                                                  {
                                                      "configId": "577",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Last Name"
                                                          }
                                                      ],
                                                      "leafState": "1"
                                                  },
                                                  {
                                                      "configId": "578",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Nickname"
                                                          }
                                                      ]
                                                  },
                                                  {
                                                      "configId": "579",
                                                      "type": {},
                                                      "leafState": false
                                                  },
                                                  {
                                                      "configId": "580",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Status"
                                                          }
                                                      ]
                                                  },
                                                  {
                                                      "configId": "581",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Owner City"
                                                          }
                                                      ],
                                                      "leafState": ""
                                                  },
                                                  {
                                                      "configId": "582",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Telephone"
                                                          }
                                                      ],
                                                      "leafState": "1231231231"
                                                  },
                                                  {
                                                      "configId": "583",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "configId": "585",
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "Edit"
                                                                          }
                                                                      ]
                                                                  },
                                                                  {
                                                                      "configId": "586",
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "Owner Info"
                                                                          }
                                                                      ]
                                                                  }
                                                              ]
                                                          }
                                                      }
                                                  },
                                                  {
                                                      "configId": "587",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "configId": "593",
                                                                      "page": {
                                                                          "last": true,
                                                                          "totalElements": 0,
                                                                          "totalPages": 1,
                                                                          "size": 0,
                                                                          "number": 0,
                                                                          "first": true,
                                                                          "numberOfElements": 0
                                                                      },
                                                                      "elemLabels": {
                                                                          "599": [
                                                                              {
                                                                                  "locale": "en-US",
                                                                                  "text": "Pet's name"
                                                                              }
                                                                          ]
                                                                      },
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "Pets"
                                                                          }
                                                                      ],
                                                                      "type": {
                                                                          "model": {}
                                                                      }
                                                                  }
                                                              ]
                                                          }
                                                      }
                                                  }
                                              ]
                                          }
                                      },
                                      "leafState": {
                                          "id": 51,
                                          "firstName": "test",
                                          "lastName": "1",
                                          "shouldUseNickname": false,
                                          "ownerCity": "",
                                          "telephone": "1231231231",
                                          "vlmCaseItemLinks": {},
                                          "expandedRowContent": {
                                              "pets": []
                                          }
                                      }
                                  },
                                  {
                                      "elemId": "2",
                                      "configId": "573",
                                      "collectionElem": true,
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "configId": "575",
                                                      "type": {},
                                                      "leafState": 53
                                                  },
                                                  {
                                                      "configId": "576",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "First Name"
                                                          }
                                                      ],
                                                      "leafState": "test"
                                                  },
                                                  {
                                                      "configId": "577",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Last Name"
                                                          }
                                                      ],
                                                      "leafState": "123"
                                                  },
                                                  {
                                                      "configId": "578",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Nickname"
                                                          }
                                                      ]
                                                  },
                                                  {
                                                      "configId": "579",
                                                      "type": {},
                                                      "leafState": false
                                                  },
                                                  {
                                                      "configId": "580",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Status"
                                                          }
                                                      ]
                                                  },
                                                  {
                                                      "configId": "581",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Owner City"
                                                          }
                                                      ],
                                                      "leafState": ""
                                                  },
                                                  {
                                                      "configId": "582",
                                                      "labels": [
                                                          {
                                                              "locale": "en-US",
                                                              "text": "Telephone"
                                                          }
                                                      ],
                                                      "leafState": "1231231231"
                                                  },
                                                  {
                                                      "configId": "583",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "configId": "585",
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "Edit"
                                                                          }
                                                                      ]
                                                                  },
                                                                  {
                                                                      "configId": "586",
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "Owner Info"
                                                                          }
                                                                      ]
                                                                  }
                                                              ]
                                                          }
                                                      }
                                                  },
                                                  {
                                                      "configId": "587",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "configId": "593",
                                                                      "page": {
                                                                          "last": true,
                                                                          "totalElements": 0,
                                                                          "totalPages": 1,
                                                                          "size": 0,
                                                                          "number": 0,
                                                                          "first": true,
                                                                          "numberOfElements": 0
                                                                      },
                                                                      "elemLabels": {
                                                                          "599": [
                                                                              {
                                                                                  "locale": "en-US",
                                                                                  "text": "Pet's name"
                                                                              }
                                                                          ]
                                                                      },
                                                                      "labels": [
                                                                          {
                                                                              "locale": "en-US",
                                                                              "text": "Pets"
                                                                          }
                                                                      ],
                                                                      "type": {
                                                                          "model": {}
                                                                      }
                                                                  }
                                                              ]
                                                          }
                                                      }
                                                  }
                                              ]
                                          }
                                      },
                                      "leafState": {
                                          "id": 53,
                                          "firstName": "test",
                                          "lastName": "123",
                                          "shouldUseNickname": false,
                                          "ownerCity": "",
                                          "telephone": "1231231231",
                                          "vlmCaseItemLinks": {},
                                          "expandedRowContent": {
                                              "pets": []
                                          }
                                      }
                                  }
                              ]
                          }
                      }
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/status",
                      "configId": "580",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Status"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/telephone",
                      "configId": "582",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Telephone"
                          }
                      ],
                      "leafState": "1231231231"
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/vlmCaseItemLinks/ownerInfo",
                      "configId": "586",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Owner Info"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/firstName",
                      "configId": "576",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "First Name"
                          }
                      ],
                      "leafState": "test"
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/ownerCity",
                      "configId": "581",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Owner City"
                          }
                      ],
                      "leafState": ""
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/nickname",
                      "configId": "578",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Nickname"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/vlmCaseItemLinks/edit",
                      "configId": "585",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Edit"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/ownerInfo",
                      "configId": "586",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Owner Info"
                          }
                      ]
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/firstName",
                      "configId": "576",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "First Name"
                          }
                      ],
                      "leafState": "test"
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/lastName",
                      "configId": "577",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Last Name"
                          }
                      ],
                      "leafState": "1"
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/expandedRowContent/pets",
                      "configId": "593",
                      "page": {
                          "last": true,
                          "totalElements": 0,
                          "totalPages": 1,
                          "size": 0,
                          "number": 0,
                          "first": true,
                          "numberOfElements": 0
                      },
                      "elemLabels": {
                          "599": [
                              {
                                  "locale": "en-US",
                                  "text": "Pet's name"
                              }
                          ]
                      },
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Pets"
                          }
                      ],
                      "type": {
                          "model": {}
                      }
                  }
              },
              {
                  "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
                  "action": "_update",
                  "behaviors": [
                      "$execute"
                  ],
                  "value": {
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/ownerCity",
                      "configId": "581",
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Owner City"
                          }
                      ],
                      "leafState": ""
                  }
              }
          ]
      }
  }
};

const pageServiceOutputs: any = [
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/nickname",
          "configId": "578",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Nickname"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/vlmCaseItemLinks/edit",
          "configId": "585",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Edit"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/expandedRowContent/pets",
          "configId": "593",
          "page": {
              "last": true,
              "totalElements": 0,
              "totalPages": 1,
              "size": 0,
              "number": 0,
              "first": true,
              "numberOfElements": 0
          },
          "elemLabels": {
              "599": [
                  {
                      "locale": "en-US",
                      "text": "Pet's name"
                  }
              ]
          },
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Pets"
              }
          ],
          "type": {
              "model": {}
          }
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/lastName",
          "configId": "577",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Last Name"
              }
          ],
          "leafState": "123"
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/telephone",
          "configId": "582",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Telephone"
              }
          ],
          "leafState": "1231231231"
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/vlmCaseItemLinks/ownerInfo",
          "configId": "586",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Owner Info"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/lastName",
          "configId": "577",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Last Name"
              }
          ],
          "leafState": "1"
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/nickname",
          "configId": "578",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Nickname"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/telephone",
          "configId": "582",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Telephone"
              }
          ],
          "leafState": "1231231231"
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/ownerCity",
          "configId": "581",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Owner City"
              }
          ],
          "leafState": ""
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/firstName",
          "configId": "576",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "First Name"
              }
          ],
          "leafState": "test"
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/status",
          "configId": "580",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Status"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/expandedRowContent/pets",
          "configId": "593",
          "page": {
              "last": true,
              "totalElements": 0,
              "totalPages": 1,
              "size": 0,
              "number": 0,
              "first": true,
              "numberOfElements": 0
          },
          "elemLabels": {
              "599": [
                  {
                      "locale": "en-US",
                      "text": "Pet's name"
                  }
              ]
          },
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Pets"
              }
          ],
          "type": {
              "model": {}
          }
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/status",
          "configId": "580",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Status"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/edit",
          "configId": "585",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Edit"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_new",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners",
          "configId": "570",
          "page": {
              "last": true,
              "totalElements": 3,
              "totalPages": 1,
              "size": 0,
              "number": 0,
              "first": true,
              "numberOfElements": 3
          },
          "elemLabels": {
              "576": [
                  {
                      "locale": "en-US",
                      "text": "First Name"
                  }
              ],
              "577": [
                  {
                      "locale": "en-US",
                      "text": "Last Name"
                  }
              ],
              "578": [
                  {
                      "locale": "en-US",
                      "text": "Nickname"
                  }
              ],
              "580": [
                  {
                      "locale": "en-US",
                      "text": "Status"
                  }
              ],
              "581": [
                  {
                      "locale": "en-US",
                      "text": "Owner City"
                  }
              ],
              "582": [
                  {
                      "locale": "en-US",
                      "text": "Telephone"
                  }
              ]
          },
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Owners"
              }
          ],
          "type": {
              "model": {
                  "params": [
                      {
                          "elemId": "0",
                          "configId": "573",
                          "collectionElem": true,
                          "type": {
                              "model": {
                                  "params": [
                                      {
                                          "configId": "575",
                                          "type": {},
                                          "leafState": 52
                                      },
                                      {
                                          "configId": "576",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "First Name"
                                              }
                                          ],
                                          "leafState": "test"
                                      },
                                      {
                                          "configId": "577",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Last Name"
                                              }
                                          ],
                                          "leafState": "1"
                                      },
                                      {
                                          "configId": "578",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Nickname"
                                              }
                                          ]
                                      },
                                      {
                                          "configId": "579",
                                          "type": {},
                                          "leafState": false
                                      },
                                      {
                                          "configId": "580",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Status"
                                              }
                                          ]
                                      },
                                      {
                                          "configId": "581",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Owner City"
                                              }
                                          ],
                                          "leafState": ""
                                      },
                                      {
                                          "configId": "582",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Telephone"
                                              }
                                          ],
                                          "leafState": "1231231231"
                                      },
                                      {
                                          "configId": "583",
                                          "type": {
                                              "model": {
                                                  "params": [
                                                      {
                                                          "configId": "585",
                                                          "labels": [
                                                              {
                                                                  "locale": "en-US",
                                                                  "text": "Edit"
                                                              }
                                                          ]
                                                      },
                                                      {
                                                          "configId": "586",
                                                          "labels": [
                                                              {
                                                                  "locale": "en-US",
                                                                  "text": "Owner Info"
                                                              }
                                                          ]
                                                      }
                                                  ]
                                              }
                                          }
                                      },
                                      {
                                          "configId": "587",
                                          "type": {
                                              "model": {
                                                  "params": [
                                                      {
                                                          "configId": "593",
                                                          "page": {
                                                              "last": true,
                                                              "totalElements": 0,
                                                              "totalPages": 1,
                                                              "size": 0,
                                                              "number": 0,
                                                              "first": true,
                                                              "numberOfElements": 0
                                                          },
                                                          "elemLabels": {
                                                              "599": [
                                                                  {
                                                                      "locale": "en-US",
                                                                      "text": "Pet's name"
                                                                  }
                                                              ]
                                                          },
                                                          "labels": [
                                                              {
                                                                  "locale": "en-US",
                                                                  "text": "Pets"
                                                              }
                                                          ],
                                                          "type": {
                                                              "model": {}
                                                          }
                                                      }
                                                  ]
                                              }
                                          }
                                      }
                                  ]
                              }
                          },
                          "leafState": {
                              "id": 52,
                              "firstName": "test",
                              "lastName": "1",
                              "shouldUseNickname": false,
                              "ownerCity": "",
                              "telephone": "1231231231",
                              "vlmCaseItemLinks": {},
                              "expandedRowContent": {
                                  "pets": []
                              }
                          }
                      },
                      {
                          "elemId": "1",
                          "configId": "573",
                          "collectionElem": true,
                          "type": {
                              "model": {
                                  "params": [
                                      {
                                          "configId": "575",
                                          "type": {},
                                          "leafState": 51
                                      },
                                      {
                                          "configId": "576",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "First Name"
                                              }
                                          ],
                                          "leafState": "test"
                                      },
                                      {
                                          "configId": "577",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Last Name"
                                              }
                                          ],
                                          "leafState": "1"
                                      },
                                      {
                                          "configId": "578",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Nickname"
                                              }
                                          ]
                                      },
                                      {
                                          "configId": "579",
                                          "type": {},
                                          "leafState": false
                                      },
                                      {
                                          "configId": "580",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Status"
                                              }
                                          ]
                                      },
                                      {
                                          "configId": "581",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Owner City"
                                              }
                                          ],
                                          "leafState": ""
                                      },
                                      {
                                          "configId": "582",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Telephone"
                                              }
                                          ],
                                          "leafState": "1231231231"
                                      },
                                      {
                                          "configId": "583",
                                          "type": {
                                              "model": {
                                                  "params": [
                                                      {
                                                          "configId": "585",
                                                          "labels": [
                                                              {
                                                                  "locale": "en-US",
                                                                  "text": "Edit"
                                                              }
                                                          ]
                                                      },
                                                      {
                                                          "configId": "586",
                                                          "labels": [
                                                              {
                                                                  "locale": "en-US",
                                                                  "text": "Owner Info"
                                                              }
                                                          ]
                                                      }
                                                  ]
                                              }
                                          }
                                      },
                                      {
                                          "configId": "587",
                                          "type": {
                                              "model": {
                                                  "params": [
                                                      {
                                                          "configId": "593",
                                                          "page": {
                                                              "last": true,
                                                              "totalElements": 0,
                                                              "totalPages": 1,
                                                              "size": 0,
                                                              "number": 0,
                                                              "first": true,
                                                              "numberOfElements": 0
                                                          },
                                                          "elemLabels": {
                                                              "599": [
                                                                  {
                                                                      "locale": "en-US",
                                                                      "text": "Pet's name"
                                                                  }
                                                              ]
                                                          },
                                                          "labels": [
                                                              {
                                                                  "locale": "en-US",
                                                                  "text": "Pets"
                                                              }
                                                          ],
                                                          "type": {
                                                              "model": {}
                                                          }
                                                      }
                                                  ]
                                              }
                                          }
                                      }
                                  ]
                              }
                          },
                          "leafState": {
                              "id": 51,
                              "firstName": "test",
                              "lastName": "1",
                              "shouldUseNickname": false,
                              "ownerCity": "",
                              "telephone": "1231231231",
                              "vlmCaseItemLinks": {},
                              "expandedRowContent": {
                                  "pets": []
                              }
                          }
                      },
                      {
                          "elemId": "2",
                          "configId": "573",
                          "collectionElem": true,
                          "type": {
                              "model": {
                                  "params": [
                                      {
                                          "configId": "575",
                                          "type": {},
                                          "leafState": 53
                                      },
                                      {
                                          "configId": "576",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "First Name"
                                              }
                                          ],
                                          "leafState": "test"
                                      },
                                      {
                                          "configId": "577",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Last Name"
                                              }
                                          ],
                                          "leafState": "123"
                                      },
                                      {
                                          "configId": "578",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Nickname"
                                              }
                                          ]
                                      },
                                      {
                                          "configId": "579",
                                          "type": {},
                                          "leafState": false
                                      },
                                      {
                                          "configId": "580",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Status"
                                              }
                                          ]
                                      },
                                      {
                                          "configId": "581",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Owner City"
                                              }
                                          ],
                                          "leafState": ""
                                      },
                                      {
                                          "configId": "582",
                                          "labels": [
                                              {
                                                  "locale": "en-US",
                                                  "text": "Telephone"
                                              }
                                          ],
                                          "leafState": "1231231231"
                                      },
                                      {
                                          "configId": "583",
                                          "type": {
                                              "model": {
                                                  "params": [
                                                      {
                                                          "configId": "585",
                                                          "labels": [
                                                              {
                                                                  "locale": "en-US",
                                                                  "text": "Edit"
                                                              }
                                                          ]
                                                      },
                                                      {
                                                          "configId": "586",
                                                          "labels": [
                                                              {
                                                                  "locale": "en-US",
                                                                  "text": "Owner Info"
                                                              }
                                                          ]
                                                      }
                                                  ]
                                              }
                                          }
                                      },
                                      {
                                          "configId": "587",
                                          "type": {
                                              "model": {
                                                  "params": [
                                                      {
                                                          "configId": "593",
                                                          "page": {
                                                              "last": true,
                                                              "totalElements": 0,
                                                              "totalPages": 1,
                                                              "size": 0,
                                                              "number": 0,
                                                              "first": true,
                                                              "numberOfElements": 0
                                                          },
                                                          "elemLabels": {
                                                              "599": [
                                                                  {
                                                                      "locale": "en-US",
                                                                      "text": "Pet's name"
                                                                  }
                                                              ]
                                                          },
                                                          "labels": [
                                                              {
                                                                  "locale": "en-US",
                                                                  "text": "Pets"
                                                              }
                                                          ],
                                                          "type": {
                                                              "model": {}
                                                          }
                                                      }
                                                  ]
                                              }
                                          }
                                      }
                                  ]
                              }
                          },
                          "leafState": {
                              "id": 53,
                              "firstName": "test",
                              "lastName": "123",
                              "shouldUseNickname": false,
                              "ownerCity": "",
                              "telephone": "1231231231",
                              "vlmCaseItemLinks": {},
                              "expandedRowContent": {
                                  "pets": []
                              }
                          }
                      }
                  ]
              }
          }
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/status",
          "configId": "580",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Status"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/telephone",
          "configId": "582",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Telephone"
              }
          ],
          "leafState": "1231231231"
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/vlmCaseItemLinks/ownerInfo",
          "configId": "586",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Owner Info"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/firstName",
          "configId": "576",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "First Name"
              }
          ],
          "leafState": "test"
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/ownerCity",
          "configId": "581",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Owner City"
              }
          ],
          "leafState": ""
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/nickname",
          "configId": "578",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Nickname"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/vlmCaseItemLinks/edit",
          "configId": "585",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Edit"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/ownerInfo",
          "configId": "586",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Owner Info"
              }
          ]
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/firstName",
          "configId": "576",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "First Name"
              }
          ],
          "leafState": "test"
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/lastName",
          "configId": "577",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Last Name"
              }
          ],
          "leafState": "1"
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/expandedRowContent/pets",
          "configId": "593",
          "page": {
              "last": true,
              "totalElements": 0,
              "totalPages": 1,
              "size": 0,
              "number": 0,
              "first": true,
              "numberOfElements": 0
          },
          "elemLabels": {
              "599": [
                  {
                      "locale": "en-US",
                      "text": "Pet's name"
                  }
              ]
          },
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Pets"
              }
          ],
          "type": {
              "model": {}
          }
      },
      "outputs": []
  },
  {
      "inputCommandUri": "/client/org/app/p/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/.m/_process?fn=_set&url=/p/owner/_search?fn=example",
      "action": "_update",
      "value": {
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/ownerCity",
          "configId": "581",
          "labels": [
              {
                  "locale": "en-US",
                  "text": "Owner City"
              }
          ],
          "leafState": ""
      },
      "outputs": []
  }
];


const pageServiceRootParam: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "570",
      "code": "owners",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.Grid",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 5,
              "browserBack": false,
              "showAsLink": false,
              "headerCheckboxToggleAllPages": false,
              "rowSelection": false,
              "postButtonUrl": "",
              "pagination": true,
              "dataEntryField": true,
              "postButtonTargetPath": "",
              "postButtonUri": "",
              "expandableRows": true,
              "showHeader": true,
              "postEventOnChange": false,
              "lazyLoad": false,
              "url": "",
              "dataKey": "id",
              "cssClass": "",
              "clearAllFilters": false,
              "postButtonLabel": "",
              "alias": "Grid",
              "onLoad": true,
              "postButtonAlias": "",
              "isTransient": false,
              "postButton": false,
              "export": false
          }
      },
      "type": {
          "collection": true,
          "nested": true,
          "name": "ArrayList",
          "collectionType": "list",
          "model": {
              "paramConfigIds": []
          },
          "elementConfig": {
              "id": "573",
              "type": {
                  "collection": false,
                  "nested": true,
                  "name": "OwnerLineItem",
                  "model": {
                      "paramConfigIds": [
                          "575",
                          "576",
                          "577",
                          "578",
                          "579",
                          "580",
                          "581",
                          "582",
                          "583",
                          "587"
                      ]
                  }
              }
          }
      }
  },
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [
    {
      "config": {
          "active": false,
          "required": false,
          "id": "583",
          "code": "vlmCaseItemLinks",
          "validations": null,
          "uiNatures": [],
          "uiStyles": {
              "isLink": true,
              "isHidden": false,
              "name": "ViewConfig.LinkMenu",
              "attributes": {
                  "hidden": false,
                  "readOnly": false,
                  "submitButton": true,
                  "showName": true,
                  "pageSize": 25,
                  "browserBack": false,
                  "showAsLink": false,
                  "cssClass": "dropdownTrigger",
                  "alias": "LinkMenu",
                  "imgSrc": "",
                  "imgType": "FA"
              }
          },
          "type": {
              "collection": false,
              "nested": true,
              "name": "OwnerLineItem.VLMCaseItemLinks",
              "model": {
                  "paramConfigIds": [
                      "585",
                      "586"
                  ]
              }
          }
      },
      "enabled": true,
      "visible": true,
      "activeValidationGroups": [],
      "collectionParams": [],
      "configId": "583",
      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks",
      "type": {
          "model": {
              "params": [
                  {
                      "enabled": true,
                      "visible": true,
                      "activeValidationGroups": [],
                      "collectionParams": [],
                      "configId": "585",
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/edit",
                      "type": {
                          "nested": false,
                          "name": "string",
                          "collection": false
                      },
                      "message": [],
                      "values": [],
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Edit"
                          }
                      ],
                      "elemLabels": {}
                  },
                  {
                      "enabled": true,
                      "visible": true,
                      "activeValidationGroups": [],
                      "collectionParams": [],
                      "configId": "586",
                      "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/ownerInfo",
                      "type": {
                          "nested": false,
                          "name": "string",
                          "collection": false
                      },
                      "message": [],
                      "values": [],
                      "labels": [
                          {
                              "locale": "en-US",
                              "text": "Owner Info"
                          }
                      ],
                      "elemLabels": {}
                  }
              ]
          }
      },
      "message": [],
      "values": [],
      "labels": [],
      "elemLabels": {}
    },
      {
          "config": {
            "active": false,
            "required": false,
            "id": "587",
            "code": "expandedRowContent",
            "validations": null,
            "uiNatures": [],
            "uiStyles": {
                "isLink": false,
                "isHidden": false,
                "name": "ViewConfig.GridRowBody",
                "attributes": {
                    "hidden": false,
                    "readOnly": false,
                    "submitButton": true,
                    "showName": true,
                    "pageSize": 25,
                    "browserBack": false,
                    "showAsLink": false,
                    "cssClass": "",
                    "asynchronous": false,
                    "alias": "GridRowBody"
                }
            },
            "type": {
                "collection": false,
                "nested": true,
                "name": "OwnerLineItem.ExpandedRowContent",
                "model": {
                    "paramConfigIds": [
                        "593"
                    ]
                }
            }
        },
          "enabled": true,
          "visible": true,
          "activeValidationGroups": [],
          "collectionParams": [],
          "configId": "587",
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/expandedRowContent",
          "type": {
              "model": {
                  "params": [
                      {
                          "enabled": true,
                          "visible": true,
                          "activeValidationGroups": [],
                          "collectionParams": [],
                          "configId": "593",
                          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/expandedRowContent/pets",
                          "type": {
                              "model": {
                                  "params": []
                              }
                          },
                          "page": {
                              "last": true,
                              "totalElements": 0,
                              "totalPages": 1,
                              "size": 0,
                              "number": 0,
                              "first": true,
                              "numberOfElements": 0
                          },
                          "gridData": {
                              "collectionParams": []
                          },
                          "message": [],
                          "values": [],
                          "labels": [
                              {
                                  "locale": "en-US",
                                  "text": "Pets"
                              }
                          ],
                          "elemLabels": {}
                      }
                  ]
              }
          },
          "message": [],
          "values": [],
          "labels": [],
          "elemLabels": {}
      },
      {
          "config": {
            "active": false,
            "required": false,
            "id": "583",
            "code": "vlmCaseItemLinks",
            "validations": null,
            "uiNatures": [],
            "uiStyles": {
                "isLink": true,
                "isHidden": false,
                "name": "ViewConfig.LinkMenu",
                "attributes": {
                    "hidden": false,
                    "readOnly": false,
                    "submitButton": true,
                    "showName": true,
                    "pageSize": 25,
                    "browserBack": false,
                    "showAsLink": false,
                    "cssClass": "dropdownTrigger",
                    "alias": "LinkMenu",
                    "imgSrc": "",
                    "imgType": "FA"
                }
            },
            "type": {
                "collection": false,
                "nested": true,
                "name": "OwnerLineItem.VLMCaseItemLinks",
                "model": {
                    "paramConfigIds": [
                        "585",
                        "586"
                    ]
                }
            }
        },
          "enabled": true,
          "visible": true,
          "activeValidationGroups": [],
          "collectionParams": [],
          "configId": "583",
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/vlmCaseItemLinks",
          "type": {
              "model": {
                  "params": [
                      {
                          "enabled": true,
                          "visible": true,
                          "activeValidationGroups": [],
                          "collectionParams": [],
                          "configId": "585",
                          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/vlmCaseItemLinks/edit",
                          "type": {
                              "nested": false,
                              "name": "string",
                              "collection": false
                          },
                          "message": [],
                          "values": [],
                          "labels": [
                              {
                                  "locale": "en-US",
                                  "text": "Edit"
                              }
                          ],
                          "elemLabels": {}
                      },
                      {
                          "enabled": true,
                          "visible": true,
                          "activeValidationGroups": [],
                          "collectionParams": [],
                          "configId": "586",
                          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/vlmCaseItemLinks/ownerInfo",
                          "type": {
                              "nested": false,
                              "name": "string",
                              "collection": false
                          },
                          "message": [],
                          "values": [],
                          "labels": [
                              {
                                  "locale": "en-US",
                                  "text": "Owner Info"
                              }
                          ],
                          "elemLabels": {}
                      }
                  ]
              }
          },
          "message": [],
          "values": [],
          "labels": [],
          "elemLabels": {}
      },
      {
          "config":{
            "active": false,
            "required": false,
            "id": "587",
            "code": "expandedRowContent",
            "validations": null,
            "uiNatures": [],
            "uiStyles": {
                "isLink": false,
                "isHidden": false,
                "name": "ViewConfig.GridRowBody",
                "attributes": {
                    "hidden": false,
                    "readOnly": false,
                    "submitButton": true,
                    "showName": true,
                    "pageSize": 25,
                    "browserBack": false,
                    "showAsLink": false,
                    "cssClass": "",
                    "asynchronous": false,
                    "alias": "GridRowBody"
                }
            },
            "type": {
                "collection": false,
                "nested": true,
                "name": "OwnerLineItem.ExpandedRowContent",
                "model": {
                    "paramConfigIds": [
                        "593"
                    ]
                }
            }
        },
          "enabled": true,
          "visible": true,
          "activeValidationGroups": [],
          "collectionParams": [],
          "configId": "587",
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/expandedRowContent",
          "type": {
              "model": {
                  "params": [
                      {
                          "enabled": true,
                          "visible": true,
                          "activeValidationGroups": [],
                          "collectionParams": [],
                          "configId": "593",
                          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/1/expandedRowContent/pets",
                          "type": {
                              "model": {
                                  "params": []
                              }
                          },
                          "page": {
                              "last": true,
                              "totalElements": 0,
                              "totalPages": 1,
                              "size": 0,
                              "number": 0,
                              "first": true,
                              "numberOfElements": 0
                          },
                          "gridData": {
                              "collectionParams": []
                          },
                          "message": [],
                          "values": [],
                          "labels": [
                              {
                                  "locale": "en-US",
                                  "text": "Pets"
                              }
                          ],
                          "elemLabels": {}
                      }
                  ]
              }
          },
          "message": [],
          "values": [],
          "labels": [],
          "elemLabels": {}
      },
      {
          "config": {
            "active": false,
            "required": false,
            "id": "583",
            "code": "vlmCaseItemLinks",
            "validations": null,
            "uiNatures": [],
            "uiStyles": {
                "isLink": true,
                "isHidden": false,
                "name": "ViewConfig.LinkMenu",
                "attributes": {
                    "hidden": false,
                    "readOnly": false,
                    "submitButton": true,
                    "showName": true,
                    "pageSize": 25,
                    "browserBack": false,
                    "showAsLink": false,
                    "cssClass": "dropdownTrigger",
                    "alias": "LinkMenu",
                    "imgSrc": "",
                    "imgType": "FA"
                }
            },
            "type": {
                "collection": false,
                "nested": true,
                "name": "OwnerLineItem.VLMCaseItemLinks",
                "model": {
                    "paramConfigIds": [
                        "585",
                        "586"
                    ]
                }
            }
        },
          "enabled": true,
          "visible": true,
          "activeValidationGroups": [],
          "collectionParams": [],
          "configId": "583",
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/vlmCaseItemLinks",
          "type": {
              "model": {
                  "params": [
                      {
                          "enabled": true,
                          "visible": true,
                          "activeValidationGroups": [],
                          "collectionParams": [],
                          "configId": "585",
                          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/vlmCaseItemLinks/edit",
                          "type": {
                              "nested": false,
                              "name": "string",
                              "collection": false
                          },
                          "message": [],
                          "values": [],
                          "labels": [
                              {
                                  "locale": "en-US",
                                  "text": "Edit"
                              }
                          ],
                          "elemLabels": {}
                      },
                      {
                          "enabled": true,
                          "visible": true,
                          "activeValidationGroups": [],
                          "collectionParams": [],
                          "configId": "586",
                          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/vlmCaseItemLinks/ownerInfo",
                          "type": {
                              "nested": false,
                              "name": "string",
                              "collection": false
                          },
                          "message": [],
                          "values": [],
                          "labels": [
                              {
                                  "locale": "en-US",
                                  "text": "Owner Info"
                              }
                          ],
                          "elemLabels": {}
                      }
                  ]
              }
          },
          "message": [],
          "values": [],
          "labels": [],
          "elemLabels": {}
      },
      {
          "config": {
            "active": false,
            "required": false,
            "id": "587",
            "code": "expandedRowContent",
            "validations": null,
            "uiNatures": [],
            "uiStyles": {
                "isLink": false,
                "isHidden": false,
                "name": "ViewConfig.GridRowBody",
                "attributes": {
                    "hidden": false,
                    "readOnly": false,
                    "submitButton": true,
                    "showName": true,
                    "pageSize": 25,
                    "browserBack": false,
                    "showAsLink": false,
                    "cssClass": "",
                    "asynchronous": false,
                    "alias": "GridRowBody"
                }
            },
            "type": {
                "collection": false,
                "nested": true,
                "name": "OwnerLineItem.ExpandedRowContent",
                "model": {
                    "paramConfigIds": [
                        "593"
                    ]
                }
            }
        },
          "enabled": true,
          "visible": true,
          "activeValidationGroups": [],
          "collectionParams": [],
          "configId": "587",
          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/expandedRowContent",
          "type": {
              "model": {
                  "params": [
                      {
                          "enabled": true,
                          "visible": true,
                          "activeValidationGroups": [],
                          "collectionParams": [],
                          "configId": "593",
                          "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/2/expandedRowContent/pets",
                          "type": {
                              "model": {
                                  "params": []
                              }
                          },
                          "page": {
                              "last": true,
                              "totalElements": 0,
                              "totalPages": 1,
                              "size": 0,
                              "number": 0,
                              "first": true,
                              "numberOfElements": 0
                          },
                          "gridData": {
                              "collectionParams": []
                          },
                          "message": [],
                          "values": [],
                          "labels": [
                              {
                                  "locale": "en-US",
                                  "text": "Pets"
                              }
                          ],
                          "elemLabels": {}
                      }
                  ]
              }
          },
          "message": [],
          "values": [],
          "labels": [],
          "elemLabels": {}
      }
  ],
  "configId": "570",
  "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners",
  "type": {
      "model": {
          "params": []
      }
  },
  "page": {
      "last": true,
      "totalElements": 3,
      "totalPages": 1,
      "size": 0,
      "number": 0,
      "first": true,
      "numberOfElements": 3
  },
  "gridData": {
      "collectionParams": [],
      "leafState": [
          {
              "id": 52,
              "firstName": "test",
              "lastName": "1",
              "shouldUseNickname": false,
              "ownerCity": "",
              "telephone": "1231231231",
              "vlmCaseItemLinks": {},
              "expandedRowContent": {
                  "pets": []
              },
              "elemId": "0"
          },
          {
              "id": 51,
              "firstName": "test",
              "lastName": "1",
              "shouldUseNickname": false,
              "ownerCity": "",
              "telephone": "1231231231",
              "vlmCaseItemLinks": {},
              "expandedRowContent": {
                  "pets": []
              },
              "elemId": "1"
          },
          {
              "id": 53,
              "firstName": "test",
              "lastName": "123",
              "shouldUseNickname": false,
              "ownerCity": "",
              "telephone": "1231231231",
              "vlmCaseItemLinks": {},
              "expandedRowContent": {
                  "pets": []
              },
              "elemId": "2"
          }
      ],
      "stateMap": [
          {
              "id": {},
              "firstName": {},
              "lastName": {},
              "nickname": {},
              "shouldUseNickname": {},
              "status": {},
              "ownerCity": {},
              "telephone": {},
              "vlmCaseItemLinks": {},
              "expandedRowContent": {}
          },
          {
              "id": {},
              "firstName": {},
              "lastName": {},
              "nickname": {},
              "shouldUseNickname": {},
              "status": {},
              "ownerCity": {},
              "telephone": {},
              "vlmCaseItemLinks": {},
              "expandedRowContent": {}
          },
          {
              "id": {},
              "firstName": {},
              "lastName": {},
              "nickname": {},
              "shouldUseNickname": {},
              "status": {},
              "ownerCity": {},
              "telephone": {},
              "vlmCaseItemLinks": {},
              "expandedRowContent": {}
          }
      ]
  },
  "message": [],
  "values": [],
  "labels": [
      {
          "locale": "en-US",
          "text": "Owners"
      }
  ],
  "elemLabels": {}
};

const configServiceFlowConfigs: any = {
  "ownerlandingview": {
      "model": {
          "params": [
              {
                  "config": {
                      "active": false,
                      "required": false,
                      "id": "548",
                      "code": "vpOwners",
                      "validations": null,
                      "uiNatures": [],
                      "uiStyles": {
                          "isLink": false,
                          "isHidden": false,
                          "name": "ViewConfig.Page",
                          "attributes": {
                              "hidden": false,
                              "readOnly": false,
                              "submitButton": true,
                              "showName": true,
                              "pageSize": 25,
                              "browserBack": false,
                              "showAsLink": false,
                              "defaultPage": true,
                              "route": "",
                              "cssClass": "",
                              "fixLayout": false,
                              "alias": "Page",
                              "imgSrc": ""
                          }
                      },
                      "type": {
                          "collection": false,
                          "nested": true,
                          "name": "VROwnerLanding.VPOwners",
                          "model": {
                              "paramConfigIds": [
                                  "550"
                              ]
                          }
                      }
                  },
                  "enabled": true,
                  "visible": true,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "548",
                  "path": "/ownerlandingview/vpOwners",
                  "type": {
                      "model": {
                          "params": [
                              {
                                  "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "550",
                                      "code": "vtOwners",
                                      "validations": null,
                                      "uiNatures": [],
                                      "uiStyles": {
                                          "isLink": false,
                                          "isHidden": false,
                                          "name": "ViewConfig.Tile",
                                          "attributes": {
                                              "hidden": false,
                                              "readOnly": false,
                                              "submitButton": true,
                                              "showName": true,
                                              "pageSize": 25,
                                              "browserBack": false,
                                              "showAsLink": false,
                                              "size": "Large",
                                              "cssClass": "",
                                              "alias": "Tile",
                                              "imgSrc": "resources/icons/task.svg#Layer_1"
                                          }
                                      },
                                      "type": {
                                          "collection": false,
                                          "nested": true,
                                          "name": "VROwnerLanding.VTOwners",
                                          "model": {
                                              "paramConfigIds": [
                                                  "552",
                                                  "564"
                                              ]
                                          }
                                      }
                                  },
                                  "enabled": true,
                                  "visible": true,
                                  "activeValidationGroups": [],
                                  "collectionParams": [],
                                  "configId": "550",
                                  "path": "/ownerlandingview/vpOwners/vtOwners",
                                  "type": {
                                      "model": {
                                          "params": [
                                              {
                                                  "config": {
                                                      "active": false,
                                                      "required": false,
                                                      "id": "552",
                                                      "code": "vsSearchOwnerCriteria",
                                                      "validations": null,
                                                      "uiNatures": [],
                                                      "uiStyles": {
                                                          "isLink": false,
                                                          "isHidden": false,
                                                          "name": "ViewConfig.Section",
                                                          "attributes": {
                                                              "hidden": false,
                                                              "readOnly": false,
                                                              "submitButton": true,
                                                              "showName": true,
                                                              "pageSize": 25,
                                                              "browserBack": false,
                                                              "showAsLink": false,
                                                              "cssClass": "",
                                                              "alias": "Section",
                                                              "value": "DEFAULT",
                                                              "imgSrc": "",
                                                              "imgType": "FA"
                                                          }
                                                      },
                                                      "type": {
                                                          "collection": false,
                                                          "nested": true,
                                                          "name": "VROwnerLanding.VSSearchOwnerCriteria",
                                                          "model": {
                                                              "paramConfigIds": [
                                                                  "556"
                                                              ]
                                                          }
                                                      }
                                                  },
                                                  "enabled": true,
                                                  "visible": true,
                                                  "activeValidationGroups": [],
                                                  "collectionParams": [],
                                                  "configId": "552",
                                                  "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria",
                                                  "type": {
                                                      "model": {
                                                          "params": [
                                                              {
                                                                  "config": {
                                                                      "active": false,
                                                                      "required": false,
                                                                      "id": "556",
                                                                      "code": "vfSearchOwnerCriteria",
                                                                      "validations": null,
                                                                      "uiNatures": [],
                                                                      "uiStyles": {
                                                                          "isLink": false,
                                                                          "isHidden": false,
                                                                          "name": "ViewConfig.Form",
                                                                          "attributes": {
                                                                              "hidden": false,
                                                                              "readOnly": false,
                                                                              "submitButton": true,
                                                                              "showName": true,
                                                                              "pageSize": 25,
                                                                              "browserBack": false,
                                                                              "showAsLink": false,
                                                                              "submitUrl": "",
                                                                              "b": "",
                                                                              "cssClass": "twoColumn",
                                                                              "showMessages": false,
                                                                              "alias": "Form",
                                                                              "navLink": "",
                                                                              "manualValidation": false
                                                                          }
                                                                      },
                                                                      "type": {
                                                                          "collection": false,
                                                                          "nested": true,
                                                                          "name": "VROwnerLanding.VFSearchOwnerCriteria",
                                                                          "model": {
                                                                              "paramConfigIds": [
                                                                                  "558",
                                                                                  "559",
                                                                                  "560"
                                                                              ]
                                                                          }
                                                                      }
                                                                  },
                                                                  "enabled": true,
                                                                  "visible": true,
                                                                  "activeValidationGroups": [],
                                                                  "collectionParams": [],
                                                                  "configId": "556",
                                                                  "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria",
                                                                  "type": {
                                                                      "model": {
                                                                          "params": [
                                                                              {
                                                                                  "config": {
                                                                                      "active": false,
                                                                                      "required": false,
                                                                                      "id": "558",
                                                                                      "code": "firstName",
                                                                                      "uiNatures": [],
                                                                                      "uiStyles": {
                                                                                          "isLink": false,
                                                                                          "isHidden": false,
                                                                                          "name": "ViewConfig.TextBox",
                                                                                          "attributes": {
                                                                                              "hidden": false,
                                                                                              "readOnly": false,
                                                                                              "submitButton": true,
                                                                                              "showName": true,
                                                                                              "pageSize": 25,
                                                                                              "browserBack": false,
                                                                                              "showAsLink": false,
                                                                                              "help": "",
                                                                                              "cssClass": "",
                                                                                              "dataEntryField": true,
                                                                                              "labelClass": "anthem-label",
                                                                                              "alias": "TextBox",
                                                                                              "controlId": "",
                                                                                              "type": "text",
                                                                                              "postEventOnChange": false,
                                                                                              "cols": ""
                                                                                          }
                                                                                      },
                                                                                      "type": {
                                                                                          "collection": false,
                                                                                          "nested": false,
                                                                                          "name": "string"
                                                                                      },
                                                                                      "validation": {
                                                                                          "constraints": [
                                                                                              {
                                                                                                  "name": "NotNull",
                                                                                                  "attribute": {
                                                                                                      "message": "Field is required.",
                                                                                                      "groups": []
                                                                                                  }
                                                                                              }
                                                                                          ]
                                                                                      }
                                                                                  },
                                                                                  "enabled": true,
                                                                                  "visible": true,
                                                                                  "activeValidationGroups": [],
                                                                                  "collectionParams": [],
                                                                                  "configId": "558",
                                                                                  "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/firstName",
                                                                                  "type": {
                                                                                      "nested": false,
                                                                                      "name": "string",
                                                                                      "collection": false
                                                                                  },
                                                                                  "message": [],
                                                                                  "values": [],
                                                                                  "labels": [
                                                                                      {
                                                                                          "locale": "en-US",
                                                                                          "text": "First Name"
                                                                                      }
                                                                                  ],
                                                                                  "elemLabels": {}
                                                                              },
                                                                              {
                                                                                  "config": {
                                                                                      "active": false,
                                                                                      "required": false,
                                                                                      "id": "559",
                                                                                      "code": "lastName",
                                                                                      "validations": null,
                                                                                      "uiNatures": [],
                                                                                      "uiStyles": {
                                                                                          "isLink": false,
                                                                                          "isHidden": false,
                                                                                          "name": "ViewConfig.TextBox",
                                                                                          "attributes": {
                                                                                              "hidden": false,
                                                                                              "readOnly": false,
                                                                                              "submitButton": true,
                                                                                              "showName": true,
                                                                                              "pageSize": 25,
                                                                                              "browserBack": false,
                                                                                              "showAsLink": false,
                                                                                              "help": "",
                                                                                              "cssClass": "",
                                                                                              "dataEntryField": true,
                                                                                              "labelClass": "anthem-label",
                                                                                              "alias": "TextBox",
                                                                                              "controlId": "",
                                                                                              "type": "text",
                                                                                              "postEventOnChange": false,
                                                                                              "cols": ""
                                                                                          }
                                                                                      },
                                                                                      "type": {
                                                                                          "collection": false,
                                                                                          "nested": false,
                                                                                          "name": "string"
                                                                                      }
                                                                                  },
                                                                                  "enabled": true,
                                                                                  "visible": true,
                                                                                  "activeValidationGroups": [],
                                                                                  "collectionParams": [],
                                                                                  "configId": "559",
                                                                                  "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/lastName",
                                                                                  "type": {
                                                                                      "nested": false,
                                                                                      "name": "string",
                                                                                      "collection": false
                                                                                  },
                                                                                  "message": [],
                                                                                  "values": [],
                                                                                  "labels": [
                                                                                      {
                                                                                          "locale": "en-US",
                                                                                          "text": "Last Name"
                                                                                      }
                                                                                  ],
                                                                                  "elemLabels": {}
                                                                              },
                                                                              {
                                                                                  "config": {
                                                                                      "active": false,
                                                                                      "required": false,
                                                                                      "id": "560",
                                                                                      "code": "vbgSearchOwner",
                                                                                      "validations": null,
                                                                                      "uiNatures": [],
                                                                                      "uiStyles": {
                                                                                          "isLink": false,
                                                                                          "isHidden": false,
                                                                                          "name": "ViewConfig.ButtonGroup",
                                                                                          "attributes": {
                                                                                              "hidden": false,
                                                                                              "readOnly": false,
                                                                                              "submitButton": true,
                                                                                              "showName": true,
                                                                                              "pageSize": 25,
                                                                                              "browserBack": false,
                                                                                              "showAsLink": false,
                                                                                              "cssClass": "text-sm-center",
                                                                                              "alias": "ButtonGroup"
                                                                                          }
                                                                                      },
                                                                                      "type": {
                                                                                          "collection": false,
                                                                                          "nested": true,
                                                                                          "name": "VROwnerLanding.VBGSearchOwner",
                                                                                          "model": {
                                                                                              "paramConfigIds": [
                                                                                                  "562",
                                                                                                  "563"
                                                                                              ]
                                                                                          }
                                                                                      }
                                                                                  },
                                                                                  "enabled": true,
                                                                                  "visible": true,
                                                                                  "activeValidationGroups": [],
                                                                                  "collectionParams": [],
                                                                                  "configId": "560",
                                                                                  "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/vbgSearchOwner",
                                                                                  "type": {
                                                                                      "model": {
                                                                                          "params": [
                                                                                              {
                                                                                                  "enabled": true,
                                                                                                  "visible": true,
                                                                                                  "activeValidationGroups": [],
                                                                                                  "collectionParams": [],
                                                                                                  "configId": "562",
                                                                                                  "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/vbgSearchOwner/search",
                                                                                                  "type": {
                                                                                                      "nested": false,
                                                                                                      "name": "string",
                                                                                                      "collection": false
                                                                                                  },
                                                                                                  "message": [],
                                                                                                  "values": [],
                                                                                                  "labels": [
                                                                                                      {
                                                                                                          "locale": "en-US",
                                                                                                          "text": "Search"
                                                                                                      }
                                                                                                  ],
                                                                                                  "elemLabels": {}
                                                                                              },
                                                                                              {
                                                                                                  "enabled": true,
                                                                                                  "visible": true,
                                                                                                  "activeValidationGroups": [],
                                                                                                  "collectionParams": [],
                                                                                                  "configId": "563",
                                                                                                  "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/vbgSearchOwner/addOwner",
                                                                                                  "type": {
                                                                                                      "nested": false,
                                                                                                      "name": "string",
                                                                                                      "collection": false
                                                                                                  },
                                                                                                  "message": [],
                                                                                                  "values": [],
                                                                                                  "labels": [
                                                                                                      {
                                                                                                          "locale": "en-US",
                                                                                                          "text": "Add Owner"
                                                                                                      }
                                                                                                  ],
                                                                                                  "elemLabels": {}
                                                                                              }
                                                                                          ]
                                                                                      }
                                                                                  },
                                                                                  "message": [],
                                                                                  "values": [],
                                                                                  "labels": [],
                                                                                  "elemLabels": {}
                                                                              }
                                                                          ]
                                                                      }
                                                                  },
                                                                  "message": [],
                                                                  "values": [],
                                                                  "labels": [],
                                                                  "elemLabels": {}
                                                              }
                                                          ]
                                                      }
                                                  },
                                                  "message": [],
                                                  "values": [],
                                                  "labels": [],
                                                  "elemLabels": {}
                                              },
                                              {
                                                  "enabled": true,
                                                  "visible": true,
                                                  "activeValidationGroups": [],
                                                  "collectionParams": [],
                                                  "configId": "564",
                                                  "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners",
                                                  "type": {
                                                      "model": {
                                                          "params": [
                                                              {
                                                                  "enabled": true,
                                                                  "visible": true,
                                                                  "activeValidationGroups": [],
                                                                  "collectionParams": [],
                                                                  "configId": "570",
                                                                  "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners",
                                                                  "type": {
                                                                      "model": {
                                                                          "params": []
                                                                      }
                                                                  },
                                                                  "page": {
                                                                      "last": true,
                                                                      "totalElements": 0,
                                                                      "totalPages": 1,
                                                                      "size": 0,
                                                                      "number": 0,
                                                                      "first": true,
                                                                      "numberOfElements": 0
                                                                  },
                                                                  "gridData": {
                                                                      "collectionParams": []
                                                                  },
                                                                  "message": [],
                                                                  "values": [],
                                                                  "labels": [
                                                                      {
                                                                          "locale": "en-US",
                                                                          "text": "Owners"
                                                                      }
                                                                  ],
                                                                  "elemLabels": {}
                                                              }
                                                          ]
                                                      }
                                                  },
                                                  "message": [],
                                                  "values": [],
                                                  "labels": [],
                                                  "elemLabels": {}
                                              }
                                          ]
                                      }
                                  },
                                  "message": [],
                                  "values": [],
                                  "labels": [],
                                  "elemLabels": {}
                              }
                          ]
                      }
                  },
                  "message": [],
                  "values": [],
                  "labels": [
                      {
                          "locale": "en-US",
                          "text": "Owners"
                      }
                  ],
                  "elemLabels": {}
              }
          ]
      },
      "layout": ""
  }
};

