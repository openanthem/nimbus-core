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

import { ViewComponent } from './../shared/param-annotations.enum';
import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SESSION_STORAGE, StorageServiceModule } from 'angular-webstorage-service';
import { of as observableOf,  Observable } from 'rxjs';
import { Router } from '@angular/router';

import { LayoutService } from './layout.service';
import { CustomHttpClient } from './httpclient.service';
import { WebContentSvc } from './content-management.service';
import { PageService } from './page.service';
import { ConfigService } from './config.service';
import { LoggerService } from './logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from './session.store';
import { LoaderService } from './loader.service';

let http, backend, service, configService, sessionStoreService, mockHttpClient, logger;

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
  getFlowConfig(a) {
    const layoutConfig = {
      model: {
        params: [
          {
            type: {
              model: {
                params: [
                  {
                    config: {
                      uiStyles: {
                        attributes: {
                          alias: 'b'
                        }
                      }
                    }
                  }
                ]
              }
            }
          }
        ]
      },
      rootDomainId: 'r',
      outputs: [
        {
          value: {
            type: {
              model: 'a'
            }
          }
        }
      ]
    };
    return layoutConfig;
  }

  setLayoutToAppConfigByModel(a, b) {}
}

class MockPageService {
  processEvent(a, b, c, d) {}
}

class MockRouter {
  navigate() {
   }
}

describe('LayoutService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          { provide: LoggerService, useClass: MockLoggerService },
          { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
          { provide: ConfigService, useClass: MockConfigService},
          { provide: CustomHttpClient, useClass: MockHttpClient },
          { provide: PageService, useClass: MockPageService},
          {provide: Router, useClass: MockRouter},
          LayoutService,
          WebContentSvc,
          LoaderService,
          SessionStoreService
        ],
      imports: [ 
          HttpClientTestingModule, 
          HttpModule,
          StorageServiceModule
        ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(LayoutService);
    configService = TestBed.get(ConfigService);
    sessionStoreService = TestBed.get(SessionStoreService);
    mockHttpClient = TestBed.get(CustomHttpClient);
    logger = TestBed.get(LoggerService);
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('getLayout() should call parseLayoutConfig()', async(() => {
    spyOn(service, 'parseLayoutConfig').and.returnValue('');
    service.getLayout('t');
    expect(service.parseLayoutConfig).toHaveBeenCalled();
  }));

  it('getLayout() should call parseLayoutConfig()', async(() => {
    spyOn(configService, 'getFlowConfig').and.returnValue(false);
    sessionStoreService.set('test', 't');
    service.parseLayoutConfig = () => {};
    spyOn(service, 'parseLayoutConfig').and.callThrough();
    service.getLayout('test');
    expect(service.parseLayoutConfig).toHaveBeenCalled();
  }));

  it('getLayout() should call parseLayoutConfig() without sessionId', async(() => {
    spyOn(configService, 'getFlowConfig').and.returnValue(false);
    service.parseLayoutConfig = () => {};
    spyOn(service, 'parseLayoutConfig').and.callThrough();
    service.getLayout('test');
    expect(service.parseLayoutConfig).toHaveBeenCalled();
  }));

  it('getLayout() should call logger.error()', async(() => {
    spyOn(configService, 'getFlowConfig').and.returnValue(false);
    spyOn(mockHttpClient, 'get').and.returnValue(observableOf({
        result: [false]
      }));
    spyOn(logger, 'error').and.callThrough();
    service.parseLayoutConfig = () => {};
    spyOn(service, 'parseLayoutConfig').and.callThrough();
    service.getLayout('test');
    expect(logger.error).toHaveBeenCalled();
  }));

  it('getFooterItems() should return object with disclaimer property', async(() => {
    const layoutConfig = { params: [{ type: { model: { params: [{ config: { uiNatures: [{ name: 'ViewConfig.FooterProperty', attributes: { value: 'DISCLAIMER' } }] } }] } }, config: { uiStyles: { attributes: { alias: 'Footer' } } } }] };
    const res = service.getFooterItems(layoutConfig);
    expect(res.disclaimer).toBeTruthy();
  }));

  it('getFooterItems() should return object with links property', async(() => {
    const layoutConfig = { params: [{ type: { model: { params: [{ config: { uiNatures: [{ name: 'ViewConfig.FooterProperty', attributes: { value: 'LINK' } }] } }] } }, config: { uiStyles: { attributes: { alias: 'Footer' } } } }] };
    const res = service.getFooterItems(layoutConfig);
    expect(res.links).toBeTruthy();
  }));

  it('getFooterItems() should return object with sslCert property', async(() => {
    const layoutConfig = { params: [{ type: { model: { params: [{ config: { uiNatures: [{ name: 'ViewConfig.FooterProperty', attributes: { value: 'SSLCERT' } }] } }] } }, config: { uiStyles: { attributes: { alias: 'Footer' } } } }] };
    const res = service.getFooterItems(layoutConfig);
    expect(res.sslCert).toBeTruthy();
  }));

  it('getTopBar() should return object with headerMenus', async(() => {
    service.parseTopBarConfig = () => {};
    const layoutConfig = { params: [{ type: { model: 'm' }, path: 't', config: { uiStyles: { attributes: { alias: 'Header' } }, initializeComponent: () => {
              return true;
            } } }] };
    const res = service.getTopBar(layoutConfig);
    expect(res).toEqual({ branding: {}, headerMenus: [], accordions: [] });
  }));

  it('parseTopBarConfig() should update branding{} with logo property', async(() => {
    const topBarConfig = { params: [{ type: { model: 'm' }, config: { uiNatures: [{ attributes: { value: 'LOGO' }, name: 'ViewConfig.PageHeader' }], uiStyles: { attributes: { alias: 'SubHeader' } }, type: { nested: false } } }] };
    const branding = {};
    service.parseTopBarConfig(topBarConfig, branding, [], []);
    expect(branding).not.toEqual([]);
    expect(branding['logo']).toBeTruthy();
  }));

  it('parseTopBarConfig() should update branding{} with title property', async(() => {
    const topBarConfig = { params: [{ type: { model: 'm' }, config: { uiNatures: [{ attributes: { value: 'TITLE' }, name: 'ViewConfig.PageHeader' }], uiStyles: { attributes: { alias: 'SubHeader' } }, type: { nested: false } } }] };
    const branding = {};
    service.parseTopBarConfig(topBarConfig, branding, [], []);
    expect(branding).not.toEqual([]);
    expect(branding['title']).toBeTruthy();
  }));

  it('parseTopBarConfig() should update branding{} with appTitle property', async(() => {
    const topBarConfig = { params: [{ type: { model: 'm' }, config: { uiNatures: [{ attributes: { value: 'APPTITLE' }, name: 'ViewConfig.PageHeader' }], uiStyles: { attributes: { alias: 'SubHeader' } }, type: { nested: false } } }] };
    const branding = {};
    service.parseTopBarConfig(topBarConfig, branding, [], []);
    expect(branding).not.toEqual([]);
    expect(branding['appTitle']).toBeTruthy();
  }));

  it('parseTopBarConfig() should update branding{} with subTitle property', async(() => {
    const topBarConfig = { params: [{ type: { model: 'm' }, config: { uiNatures: [{ attributes: { value: 'SUBTITLE' }, name: 'ViewConfig.PageHeader' }], uiStyles: { attributes: { alias: 'SubHeader' } }, type: { nested: false } } }] };
    const branding = {};
    service.parseTopBarConfig(topBarConfig, branding, [], []);
    expect(branding).not.toEqual([]);
    expect(branding['subTitle']).toBeTruthy();
  }));

  it('parseTopBarConfig() should update branding{} with userName property', async(() => {
    const topBarConfig = { params: [{ type: { model: 'm' }, config: { uiNatures: [{ attributes: { value: 'USERNAME' }, name: 'ViewConfig.PageHeader' }], uiStyles: { attributes: { alias: 'SubHeader' } }, type: { nested: false } } }] };
    const branding = {};
    service.parseTopBarConfig(topBarConfig, branding, [], []);
    expect(branding).not.toEqual([]);
    expect(branding['userName']).toBeTruthy();
  }));

  it('parseTopBarConfig() should update branding{} with userRole property', async(() => {
    const topBarConfig = { params: [{ type: { model: 'm' }, config: { uiNatures: [{ attributes: { value: 'USERROLE' }, name: 'ViewConfig.PageHeader' }], uiStyles: { attributes: { alias: 'SubHeader' } }, type: { nested: false } } }] };
    const branding = {};
    service.parseTopBarConfig(topBarConfig, branding, [], []);
    expect(branding).not.toEqual([]);
    expect(branding['userRole']).toBeTruthy();
  }));

  it('parseTopBarConfig() should update branding{} with help property', async(() => {
    const topBarConfig = { params: [{ type: { model: 'm' }, config: { uiNatures: [{ attributes: { value: 'HELP' }, name: 'ViewConfig.PageHeader' }], uiStyles: { attributes: { alias: 'SubHeader' } }, type: { nested: false } } }] };
    const branding = {};
    service.parseTopBarConfig(topBarConfig, branding, [], []);
    expect(branding).not.toEqual([]);
    expect(branding['help']).toBeTruthy();
  }));

  it('parseTopBarConfig() should update branding{} with logOut property', async(() => {
    const topBarConfig = { params: [{ type: { model: 'm' }, config: { uiNatures: [{ attributes: { value: 'LOGOUT' }, name: 'ViewConfig.PageHeader' }], uiStyles: { attributes: { alias: 'SubHeader' } }, type: { nested: false } } }] };
    const branding = {};
    service.parseTopBarConfig(topBarConfig, branding, [], []);
    expect(branding).not.toEqual([]);
    expect(branding['logOut']).toBeTruthy();
  }));

  it('parseTopBarConfig() should update branding{} with linkNotifications property', async(() => {
    const topBarConfig = { params: [{ type: { model: 'm' }, config: { uiNatures: [{ attributes: { value: 'NOTIFICATIONS' }, name: 'ViewConfig.PageHeader' }], uiStyles: { attributes: { alias: 'SubHeader' } }, type: { nested: false } } }] };
    const branding = {};
    service.parseTopBarConfig(topBarConfig, branding, [], []);
    expect(branding).not.toEqual([]);
    expect(branding['linkNotifications']).toBeTruthy();
  }));

  it('parseTopBarConfig() should update branding{} with numOfNotifications property', async(() => {
    const topBarConfig = { params: [{ type: { model: 'm' }, config: { uiNatures: [{ attributes: { value: 'NUMBEROFNOTIFICATIONS' }, name: 'ViewConfig.PageHeader' }], uiStyles: { attributes: { alias: 'SubHeader' } }, type: { nested: false } } }] };
    const branding = {};
    service.parseTopBarConfig(topBarConfig, branding, [], []);
    expect(branding).not.toEqual([]);
    expect(branding['numOfNotifications']).toBeTruthy();
  }));

  it('parseTopBarConfig() should update branding{} and headerMenus[]', async(() => {
    const topBarConfig = { params: [{ type: { model: 'm' }, config: { uiNatures: [{ attributes: { value: 'MENU' }, name: 'ViewConfig.PageHeader' }], uiStyles: { attributes: { alias: 'SubHeader' } }, type: { nested: false } } }] };
    const branding = {};
    const headerMenus = [];
    service.parseTopBarConfig(topBarConfig, branding, headerMenus, []);
    expect(branding).not.toEqual([]);
    expect(headerMenus).not.toEqual([]);
  }));

  it('getActionTrayItems() should return param', async(() => {
    const actionTrayParam = { config: { uiStyles: { attributes: { alias: 'ActionTray' } } } };
    const actionTrayConfig = { params: [actionTrayParam] };
    const res = service.getActionTrayItems(actionTrayConfig);
    expect(res).toEqual(actionTrayParam);
  }));

  it('getActionTrayItems() should return undefined', async(() => {
    const otherparam = { config: { uiStyles: { attributes: { alias: 'Button' } } } };
    const actionTrayConfig = { params: [otherparam] };
    const res = service.getActionTrayItems(actionTrayConfig);
    expect(res).toEqual(undefined);
  }));

  it('parseLayoutConfig() should call getTopBar(), getFooterItems() and getActionTrayItems()', async(() => {
    const flowModel = { params: [{ type: { model: {params: [{config: { uiStyles: { attributes: { alias: '' }}}}]} }, config: { uiStyles: { attributes: { alias: 'Page' } } } }] };
    spyOn(service, 'getTopBar').and.returnValue(2);
    spyOn(service, 'getFooterItems').and.returnValue(4);
    spyOn(service, 'getActionTrayItems').and.returnValue(5);
    service.layout$ = { next: () => {} };
    service.parseLayoutConfig(flowModel);
    expect(service.getTopBar).toHaveBeenCalled();
    expect(service.getFooterItems).toHaveBeenCalled();
    expect(service.getActionTrayItems).toHaveBeenCalled();
  }));

  it('buildMenu() should call createMenuItem() and update menuItems', async(() => {
    const param = { type: { model: { params: [{ config: { uiStyles: { attributes: { alias: 'MenuLink' } } } }] } } };
    const menuItem = { routerLink: '', command: '' };
    spyOn(service, 'createMenuItem').and.returnValue({ routerLink: '' });
    spyOn(service, 'createRouterLink').and.returnValue('routerLink');
    const menuItems = [];
    service.buildMenu(param, menuItems);
    expect(menuItems).not.toEqual([]);
    expect(service.createMenuItem).toHaveBeenCalled();
  }));

  it('buildMenu() should not call createRouterLink()', async(() => {
    const param = { type: { model: { params: [{ config: { uiStyles: { attributes: { alias: ViewComponent.menupanel.toString() } } } }] } } };
    const menuItem = { routerLink: '', command: '' };
    spyOn(service, 'createMenuItem').and.returnValue({ routerLink: '' });
    spyOn(service, 'createRouterLink').and.returnValue('routerLink');
    const menuItems = [];
    spyOn(service, 'buildSubMenu').and.returnValue('');
    service.buildMenu(param, menuItems);
    expect(service.createRouterLink).not.toHaveBeenCalled();
  }));

  it('buildSubMenu() should call createRouterLink()', async(() => {
    const param = { type: { model: { params: [{ config: { uiStyles: { attributes: { alias: 'MenuLink' } } } }] } } };
    const menuItem = { items: '' };
    spyOn(service, 'createMenuItem').and.returnValue({
      routerLink: '',
      command: ''
    });
    spyOn(service, 'createRouterLink').and.returnValue('routerLink');
    service.buildSubMenu(param, menuItem);
    expect(menuItem.items).not.toEqual('');
    expect(service.createRouterLink).toHaveBeenCalled();
  }));

  it('buildSubMenu() should update the menuItem.items', async(() => {
    const param = { type: { model: { params: [{ type: { model: { params: [{ config: { uiStyles: { attributes: { alias: '' } } } }] } }, config: { uiStyles: { attributes: { alias: ViewComponent.menupanel.toString() } } } }] } } };
    const menuItem = { items: '' };
    spyOn(service, 'createMenuItem').and.returnValue({
      routerLink: '',
      command: ''
    });
    spyOn(service, 'createRouterLink').and.returnValue('routerLink');
    service.buildSubMenu(param, menuItem);
    expect(menuItem.items).not.toEqual('');
    expect(service.createRouterLink).not.toHaveBeenCalled();
  }));

  it('createRouterLink() should return url from element', async(() => {
    const element = { config: { uiStyles: { attributes: { url: '/test' } } } };
    expect(service.createRouterLink(element, true)).toEqual('/test');
  }));

  it('createRouterLink() should return updated url', async(() => {
    const element = { config: { uiStyles: { attributes: { url: 'test' } } } };
    expect(service.createRouterLink(element, false)).toEqual('/h/test');
  }));

});