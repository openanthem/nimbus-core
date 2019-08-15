import { HttpClient } from '@angular/common/http';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { Router } from '@angular/router';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import {
  configServiceFlowConfigs,
  configServiceParamConfigs,
  layoutServicePageParam
} from 'mockdata';
import { of as observableOf } from 'rxjs';
import { GenericDomain } from '../model/generic-domain.model';
import { URLUtils } from './../shared/url-utils';
import { ConfigService } from './config.service';
import { CustomHttpClient } from './httpclient.service';
import { LayoutService } from './layout.service';
import { LoaderService } from './loader.service';
import { LoggerService } from './logger.service';
import { PageService } from './page.service';
import { ServiceConstants } from './service.constants';
import { CUSTOM_STORAGE, SessionStoreService } from './session.store';

let http,
  backend,
  service,
  configService,
  sessionStoreService,
  mockHttpClient,
  logger,
  pageService;

class MockHttpClient {
  get(a) {
    const data = {
      result: [
        {
          result: {
            value: {
              config: null,
              type: {
                model: 123
              }
            },
            outputs: [
              {
                value: {
                  config: null,
                  type: {
                    model: 456
                  }
                }
              }
            ]
          },
          b: 'b'
        }
      ]
    };
    return observableOf(data);
  }
}

class MockLoggerService {
  error(a) {}
  info(a) {}
  debug(a) {}
}

class MockSessionStoreService {
  get(a) {
    return 123;
  }
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
    };
  }
  setLayoutToAppConfig(a, b) {}
  setViewConfigToParamConfigMap(a, b) {}
  setLayoutToAppConfigByModel(a, b) {}
}

class MockPageService {
  processEvent(a, b, c, d) {}
}

class MockRouter {
  navigate() {}
}

describe('LayoutService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: LoggerService, useClass: MockLoggerService },
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        { provide: ConfigService, useClass: MockConfigService },
        { provide: CustomHttpClient, useClass: MockHttpClient },
        { provide: PageService, useClass: MockPageService },
        { provide: Router, useClass: MockRouter },
        LayoutService,
        LoaderService,
        SessionStoreService
      ],
      imports: [HttpClientTestingModule, HttpModule, StorageServiceModule]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(LayoutService);
    configService = TestBed.get(ConfigService);
    sessionStoreService = TestBed.get(SessionStoreService);
    mockHttpClient = TestBed.get(CustomHttpClient);
    logger = TestBed.get(LoggerService);
    pageService = TestBed.get(PageService);
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('getLayout() should call parseLayoutConfig()', async(() => {
    spyOn(service, 'parseLayoutConfig').and.returnValue('');
    service.getLayout('ownerlandingview');
    expect(service.parseLayoutConfig).toHaveBeenCalledWith(
      configServiceFlowConfigs.ownerlandingview.model
    );
  }));

  it('getLayout() should call parseLayoutConfig() even if flowName is not found', async(() => {
    sessionStoreService.set('test', 't');
    spyOn(configService, 'getFlowConfig').and.returnValue(false);
    spyOn(service, 'parseLayoutConfig').and.returnValue('');
    spyOn(configService, 'setLayoutToAppConfigByModel').and.callThrough();
    service.getLayout('test');
    expect(service.parseLayoutConfig).toHaveBeenCalledWith(456);
    expect(configService.setLayoutToAppConfigByModel).toHaveBeenCalledWith(
      'test',
      456
    );
  }));

  it('getLayout() should call parseLayoutConfig() even if flowName, flowRoodtId is not found', async(() => {
    sessionStoreService.set('test', 't');
    spyOn(configService, 'getFlowConfig').and.returnValue(false);
    spyOn(service, 'parseLayoutConfig').and.returnValue('');
    spyOn(configService, 'setLayoutToAppConfigByModel').and.callThrough();
    service.getLayout('test123');
    expect(service.parseLayoutConfig).toHaveBeenCalledWith(456);
    expect(configService.setLayoutToAppConfigByModel).toHaveBeenCalledWith(
      'test123',
      456
    );
  }));

  it('getLayout() should call logger.error()', async(() => {
    spyOn(configService, 'getFlowConfig').and.returnValue(false);
    spyOn(mockHttpClient, 'get').and.returnValue(
      observableOf({
        result: [false]
      })
    );
    spyOn(logger, 'error').and.callThrough();
    service.getLayout('test');
    expect(logger.error).toHaveBeenCalledWith(
      'ERROR: Unknown response for Layout config call - undefined'
    );
  }));

  it('getFooterItems() should return object with disclaimer property', async(() => {
    const res = service.getFooterItems(layoutServicePageParam.type.model);
    expect(res.disclaimer).toBeTruthy();
  }));

  it('getFooterItems() should return object with links property', async(() => {
    layoutServicePageParam.type.model.params[0].type.model.params[0].config.uiNatures[0].attributes.value =
      'LINK';
    const res = service.getFooterItems(layoutServicePageParam.type.model);
    expect(res.links).toBeTruthy();
  }));

  it('getFooterItems() should return object with sslCert property', async(() => {
    layoutServicePageParam.type.model.params[0].type.model.params[0].config.uiNatures[0].attributes.value =
      'SSLCERT';
    const res = service.getFooterItems(layoutServicePageParam.type.model);
    expect(res.sslCert).toBeTruthy();
  }));

  it('getTopBar() should return object with headerMenus', async(() => {
    spyOn(service, 'parseTopBarConfig').and.returnValue('');
    spyOn(pageService, 'processEvent').and.returnValue('');
    layoutServicePageParam.type.model.params[0].config.uiStyles.attributes = {
      alias: 'Header'
    };
    layoutServicePageParam.type.model.params[0].config[
      'initializeComponent'
    ] = () => {
      return true;
    };
    const res = service.getTopBar(layoutServicePageParam.type.model);
    expect(res).toEqual({ branding: {}, headerMenus: [], accordions: [] });
    expect(service.parseTopBarConfig).toHaveBeenCalledWith(
      layoutServicePageParam.type.model.params[0].type.model,
      {},
      [],
      []
    );
    expect(pageService.processEvent).toHaveBeenCalledWith(
      layoutServicePageParam.type.model.params[0].path,
      '$execute',
      new GenericDomain(),
      'POST'
    );
  }));

  it('parseTopBarConfig() should update branding{} with logo property', async(() => {
    layoutServicePageParam.type.model.params[0].config.type = { nested: true };
    layoutServicePageParam.type.model.params[0].config.uiNatures = [
      { attributes: { value: 'LOGO' }, name: 'ViewConfig.PageHeader' }
    ];
    const branding = {};
    service.parseTopBarConfig(
      layoutServicePageParam.type.model,
      branding,
      [],
      []
    );
    expect(branding['logo']).toEqual(
      layoutServicePageParam.type.model.params[0]
    );
  }));

  it('parseTopBarConfig() should update branding{} with title property', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiNatures[0].attributes.value =
      'TITLE';
    const branding = {};
    service.parseTopBarConfig(
      layoutServicePageParam.type.model,
      branding,
      [],
      []
    );
    expect(branding['title']).toEqual(
      layoutServicePageParam.type.model.params[0]
    );
  }));

  it('parseTopBarConfig() should update branding{} with appTitle property', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiNatures[0].attributes.value =
      'APPTITLE';
    const branding = {};
    service.parseTopBarConfig(
      layoutServicePageParam.type.model,
      branding,
      [],
      []
    );
    expect(branding['appTitle']).toEqual(
      layoutServicePageParam.type.model.params[0]
    );
  }));

  it('parseTopBarConfig() should update branding{} with subTitle property', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiNatures[0].attributes.value =
      'SUBTITLE';
    const branding = {};
    service.parseTopBarConfig(
      layoutServicePageParam.type.model,
      branding,
      [],
      []
    );
    expect(branding['subTitle']).toEqual(
      layoutServicePageParam.type.model.params[0]
    );
  }));

  it('parseTopBarConfig() should update branding{} with userName property', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiNatures[0].attributes.value =
      'USERNAME';
    const branding = {};
    service.parseTopBarConfig(
      layoutServicePageParam.type.model,
      branding,
      [],
      []
    );
    expect(branding['userName']).toEqual(
      layoutServicePageParam.type.model.params[0]
    );
  }));

  it('parseTopBarConfig() should update branding{} with userRole property', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiNatures[0].attributes.value =
      'USERROLE';
    const branding = {};
    service.parseTopBarConfig(
      layoutServicePageParam.type.model,
      branding,
      [],
      []
    );
    expect(branding['userRole']).toEqual(
      layoutServicePageParam.type.model.params[0]
    );
  }));

  it('parseTopBarConfig() should update branding{} with help property', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiNatures[0].attributes.value =
      'HELP';
    const branding = {};
    service.parseTopBarConfig(
      layoutServicePageParam.type.model,
      branding,
      [],
      []
    );
    expect(branding['help']).toEqual(
      layoutServicePageParam.type.model.params[0]
    );
  }));

  it('parseTopBarConfig() should update branding{} with logOut property', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiNatures[0].attributes.value =
      'LOGOUT';
    const branding = {};
    service.parseTopBarConfig(
      layoutServicePageParam.type.model,
      branding,
      [],
      []
    );
    expect(branding['logOut']).toEqual(
      layoutServicePageParam.type.model.params[0]
    );
  }));

  it('parseTopBarConfig() should update branding{} with linkNotifications property', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiNatures[0].attributes.value =
      'NOTIFICATIONS';
    const branding = {};
    service.parseTopBarConfig(
      layoutServicePageParam.type.model,
      branding,
      [],
      []
    );
    expect(branding['linkNotifications']).toEqual(
      layoutServicePageParam.type.model.params[0]
    );
  }));

  it('parseTopBarConfig() should update branding{} with numOfNotifications property', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiNatures[0].attributes.value =
      'NUMBEROFNOTIFICATIONS';
    const branding = {};
    service.parseTopBarConfig(
      layoutServicePageParam.type.model,
      branding,
      [],
      []
    );
    expect(branding['numOfNotifications']).toEqual(
      layoutServicePageParam.type.model.params[0]
    );
  }));

  it('parseTopBarConfig() should update branding{} and headerMenus[]', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiNatures[0].attributes.value =
      'MENU';
    const branding = {};
    const headerMenus = [];
    service.parseTopBarConfig(
      layoutServicePageParam.type.model,
      branding,
      headerMenus,
      []
    );
    expect(headerMenus[0]).toEqual(layoutServicePageParam.type.model.params[0]);
  }));

  it('getMenu() should return menuItems', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiStyles.attributes.alias =
      'MenuPanel';
    spyOn(service, 'buildMenu').and.returnValue('');
    const res = service.getMenu(layoutServicePageParam.type.model);
    expect(service.buildMenu).toHaveBeenCalledWith(
      layoutServicePageParam.type.model.params[0],
      []
    );
    expect(res.menuItems).toEqual([]);
  }));

  it('parseLayoutConfig() should call getTopBar(), getFooterItems(), getMenu(), getModalItems(), getActionTrayItems() and updaate layout$ subject', async(() => {
    const flowModel: any = { params: [layoutServicePageParam] };
    const layout: any = {
      fixLayout: false,
      menuPanel: 5,
      topBar: 2,
      footer: 4,
      modalList: 5,
      actiontray: 5
    };
    spyOn(service, 'getTopBar').and.returnValue(2);
    spyOn(service, 'getFooterItems').and.returnValue(4);
    spyOn(service, 'getActionTrayItems').and.returnValue(5);
    spyOn(service, 'getMenu').and.returnValue(5);
    spyOn(service, 'getModalItems').and.returnValue(5);
    spyOn(service.layout$, 'next').and.callThrough();
    service.parseLayoutConfig(flowModel);
    expect(service.getTopBar).toHaveBeenCalledWith(
      flowModel.params[0].type.model
    );
    expect(service.getFooterItems).toHaveBeenCalledWith(
      flowModel.params[0].type.model
    );
    expect(service.getActionTrayItems).toHaveBeenCalledWith(
      flowModel.params[0].type.model
    );
    expect(service.getMenu).toHaveBeenCalledWith(
      flowModel.params[0].type.model
    );
    expect(service.getModalItems).toHaveBeenCalledWith(
      flowModel.params[0].type.model
    );
    expect(service.layout$.next).toHaveBeenCalledWith(layout);
  }));

  it('buildMenu() should call createMenuItem(), createRouterLink() and update menuItems', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiStyles.attributes.alias =
      'MenuLink';
    spyOn(service, 'createMenuItem').and.returnValue({
      routerLink: '',
      command: '',
      code: ''
    });
    spyOn(service, 'createRouterLink').and.returnValue('routerLink');
    const menuItems = [];
    service.buildMenu(layoutServicePageParam, menuItems);
    expect(menuItems[0]['routerLink']).toEqual('routerLink');
    expect(service.createMenuItem).toHaveBeenCalledWith(
      layoutServicePageParam.type.model.params[0]
    );
    expect(service.createRouterLink).toHaveBeenCalledWith(
      layoutServicePageParam.type.model.params[0]
    );
  }));

  it('buildMenu() should not call createRouterLink()', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiStyles.attributes.alias =
      'MenuPanel';
    const createMenuItemResponse = { routerLink: '', command: '', code: '' };
    spyOn(service, 'createMenuItem').and.returnValue(createMenuItemResponse);
    spyOn(service, 'createRouterLink').and.returnValue('routerLink');
    spyOn(service, 'buildSubMenu').and.returnValue('');
    const menuItems = [];
    service.buildMenu(layoutServicePageParam, menuItems);
    expect(service.createMenuItem).toHaveBeenCalledWith(
      layoutServicePageParam.type.model.params[0]
    );
    expect(service.createRouterLink).not.toHaveBeenCalled();
    expect(service.buildSubMenu).toHaveBeenCalledWith(
      layoutServicePageParam.type.model.params[0],
      createMenuItemResponse
    );
    expect(menuItems).toEqual([createMenuItemResponse]);
  }));

  it('buildSubMenu() should call createRouterLink()', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiStyles.attributes.alias =
      'MenuLink';
    const createMenuItemResponse = { routerLink: '', command: '', code: '' };
    spyOn(service, 'createMenuItem').and.returnValue(createMenuItemResponse);
    const menuItem = { items: [] };
    spyOn(service, 'createRouterLink').and.returnValue('routerLink');
    service.buildSubMenu(layoutServicePageParam, menuItem);
    expect(menuItem.items[0]['routerLink']).toEqual('routerLink');
    expect(service.createRouterLink).toHaveBeenCalledWith(
      layoutServicePageParam.type.model.params[0],
      true
    );
  }));

  it('buildSubMenu() should update the menuItem.items', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiStyles.attributes.alias =
      'MenuPanel';
    const createMenuItemResponse = { routerLink: '', command: '', code: '' };
    const menuItem = { items: [] };
    spyOn(service, 'createMenuItem').and.returnValue(createMenuItemResponse);
    spyOn(service, 'buildSubMenu').and.callThrough();
    service.buildSubMenu(layoutServicePageParam, menuItem);
    expect(menuItem.items).toEqual([createMenuItemResponse]);
  }));

  it('createMenuItem() should return item', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    layoutServicePageParam.config.uiStyles.attributes.page = 'testing page';
    layoutServicePageParam.config.uiStyles.attributes.imgSrc = 'testing imgSrc';
    layoutServicePageParam.config.uiStyles.attributes.imgType =
      'testing imgType';
    layoutServicePageParam.config.uiStyles.attributes.url = 'testing url';
    layoutServicePageParam.config.uiStyles.attributes.type = 'testing type';
    layoutServicePageParam.config.uiStyles.attributes.target = 'testing target';
    layoutServicePageParam.config.uiStyles.attributes.rel = 'testing rel';
    const res = service.createMenuItem(layoutServicePageParam);
    expect(res).toEqual({
      label: 'testing label',
      path: '/home/vpHome',
      page: 'testing page',
      icon: 'testing imgSrc',
      imgType: 'testing imgType',
      url: 'testing url',
      type: 'testing type',
      target: 'testing target',
      rel: 'testing rel',
      visible: true
    });
  }));

  it('createRouterLink() should return url from element', async(() => {
    layoutServicePageParam.config.uiStyles.attributes.url = '/test';
    expect(service.createRouterLink(layoutServicePageParam, true)).toEqual(
      '/test'
    );
  }));

  it('createRouterLink() should return updated url', async(() => {
    layoutServicePageParam.config.uiStyles.attributes.url = '/test';
    expect(service.createRouterLink(layoutServicePageParam, false)).toEqual(
      '/h//test'
    );
  }));

  it('processClick() should call pageService.processEvent()', async(() => {
    spyOn(URLUtils, 'getDomainPage').and.returnValue('test');
    spyOn(pageService, 'processEvent').and.returnValue('');
    service.processClick('', { routerLink: '', page: '', path: 'testPath' });
    expect(pageService.processEvent).toHaveBeenCalledWith(
      'testPath',
      '$execute',
      new GenericDomain(),
      'GET'
    );
  }));

  it('processClick() should not call pageService.processEvent()', async(() => {
    spyOn(URLUtils, 'getDomainPage').and.returnValue('test');
    spyOn(pageService, 'processEvent').and.returnValue('');
    service.processClick('', {
      routerLink: '',
      page: 'test',
      path: 'testPath'
    });
    expect(pageService.processEvent).not.toHaveBeenCalled();
  }));

  it('getActionTrayItems() should return param', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiStyles.attributes.alias =
      'ActionTray';
    expect(
      service.getActionTrayItems(layoutServicePageParam.type.model)
    ).toEqual(layoutServicePageParam.type.model.params[0]);
  }));

  it('getActionTrayItems() should return undefined', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiStyles.attributes.alias =
      'Header';
    expect(
      service.getActionTrayItems(layoutServicePageParam.type.model)
    ).toBeFalsy();
  }));

  it('getModalItems() should return modalList with param', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiStyles.attributes.alias =
      'Modal';
    expect(
      service.getModalItems(layoutServicePageParam.type.model, [])
    ).toEqual([layoutServicePageParam.type.model.params[0]]);
  }));

  it('getModalItems() should return empty modalList', async(() => {
    layoutServicePageParam.type.model.params[0].config.uiStyles.attributes.alias =
      'Header';
    expect(
      service.getModalItems(layoutServicePageParam.type.model, [])
    ).toEqual([]);
  }));
});
