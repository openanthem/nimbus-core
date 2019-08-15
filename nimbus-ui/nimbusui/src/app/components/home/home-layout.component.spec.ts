/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

import { HttpClientModule } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { async, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import {
  ActivatedRoute,
  ActivatedRouteSnapshot,
  ParamMap,
  Params,
  Route,
  UrlSegment
} from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Homelayout } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { DropdownModule } from 'primeng/primeng';
import { ToastModule } from 'primeng/toast';
import { Observable, of as observableOf } from 'rxjs';
import { Subject } from 'rxjs/Rx';
import { KeysPipe } from '../../pipes/app.pipe';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { AppInitService } from '../../services/app.init.service';
import { AuthenticationService } from '../../services/authentication.service';
import { ConfigService } from '../../services/config.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LayoutService } from '../../services/layout.service';
import { LoaderService } from '../../services/loader.service';
import { LoggerService } from '../../services/logger.service';
import { PageService } from '../../services/page.service';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from '../../services/session.store';
import { setup } from '../../setup.spec';
import { BreadcrumbService } from '../platform/breadcrumb/breadcrumb.service';
import { Paragraph } from '../platform/content/paragraph.component';
import { FooterGlobal } from '../platform/footer/footer-global.component';
import {
  ActionDropdown,
  ActionLink
} from '../platform/form/elements/action-dropdown.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { InputLabel } from '../platform/form/elements/input-label.component';
import { Value } from '../platform/form/elements/value.component';
import { HeaderGlobal } from '../platform/header/header-global.component';
import { Image } from '../platform/image.component';
import { Link } from '../platform/link.component';
import { ToastMessageComponent } from '../platform/message/toastmessage.component';
import { SvgComponent } from '../platform/svg/svg.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { NmMessageService } from './../../services/toastmessage.service';
import { NavigationComponent } from './../navigation/navigation.component';
import { HomeLayoutCmp } from './home-layout.component';

@Component({
  template: '<div></div>',
  selector: 'nm-panelMenuSub'
})
export class NmPanelMenuSub {
  @Input() item: any;
  @Input() expanded: boolean;
}

@Component({
  template: '<div></div>',
  selector: 'nm-panelMenu'
})
export class NmPanelMenu {
  @Input() model: any[];
  @Input() style: any;
  @Input() styleClass: string;
  @Input() multiple: boolean = true;
}

@Component({
  template: '<div></div>',
  selector: 'nm-button'
})
class Button {
  @Input() element: any;
  @Input() payload: string;
  @Input() form: any;
  @Input() actionTray?: boolean;

  @Output() buttonClickEvent = new EventEmitter();

  @Output() elementChange = new EventEmitter();
  private imagesPath: string;
  private btnClass: string;
  private disabled: boolean;
  files: any;
  differ: any;
  componentTypes;
}

class MockAuthenticationService {
  logout() {
    const logout = 'testing';
    return observableOf(logout);
  }
}
class MockPageService {
  public config$: Subject<any>;

  constructor() {
    this.config$ = new Subject();
  }

  loadFlowConfig(a) {}
  loadDefaultPageForConfig(a) {}
  logError(a) {
    this.config$.next(a);
  }
  traverseFlowConfig(a, b) {}
}
class MockLayoutService {
  public layout$: Subject<any>;

  constructor() {
    this.layout$ = new Subject<any>();
  }

  parseLayoutConfig(layout) {
    this.layout$.next(layout);
  }

  getLayout() {}
}

class MockLoggerService {
  debug() {}
  info() {}
  error() {}
}

export class MockActivatedRoute implements ActivatedRoute {
  snapshot: ActivatedRouteSnapshot;
  url: Observable<UrlSegment[]>;
  params: Observable<Params>;
  queryParams: Observable<Params>;
  fragment: Observable<string>;
  outlet: string;
  component: any;
  routeConfig: Route;
  root: ActivatedRoute;
  parent: ActivatedRoute;
  firstChild: ActivatedRoute;
  children: ActivatedRoute[];
  pathFromRoot: ActivatedRoute[];
  data: any = {
    value: {
      layout: 'test'
    }
  };
  paramMap: Observable<ParamMap>;
  queryParamMap: Observable<ParamMap>;
}

class MockSTOMPService {
  configure() {}
  try_connect() {
    return new Promise((resolve, reject) => {
      resolve('abcd');
    });
  }
  disconnect() {}
}

let layoutService, activatedRoute, pageService;

const declarations = [
  HomeLayoutCmp,
  FooterGlobal,
  HeaderGlobal,
  Link,
  Paragraph,
  ComboBox,
  KeysPipe,
  Value,
  SelectItemPipe,
  TooltipComponent,
  SvgComponent,
  Button,
  ActionDropdown,
  InputLabel,
  Image,
  ActionLink,
  NmPanelMenu,
  ToastMessageComponent,
  NmPanelMenuSub,
  NavigationComponent
];
const providers = [
  { provide: AuthenticationService, useClass: MockAuthenticationService },
  { provide: LayoutService, useClass: MockLayoutService },
  { provide: ActivatedRoute, useClass: MockActivatedRoute },
  { provide: PageService, useClass: MockPageService },
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LoggerService, useClass: MockLoggerService },
  CustomHttpClient,
  LoaderService,
  ConfigService,
  BreadcrumbService,
  NmMessageService,
  SessionStoreService,
  AppInitService
];
const imports = [
  RouterTestingModule,
  FormsModule,
  DropdownModule,
  HttpClientModule,
  HttpModule,
  StorageServiceModule,
  AngularSvgIconModule,
  ToastModule
];

let fixture, hostComponent;
describe('HomeLayoutCmp', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeLayoutCmp);
    hostComponent = fixture.debugElement.componentInstance;
    layoutService = TestBed.get(LayoutService);
    activatedRoute = TestBed.get(ActivatedRoute);
    pageService = TestBed.get(PageService);
  });

  it('should create the app', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('ngOnInint() should get layout from layout service', async(() => {
    spyOn(layoutService, 'getLayout').and.callThrough();
    hostComponent.ngOnInit();
    layoutService.parseLayoutConfig(Homelayout);
    expect(layoutService.getLayout).toHaveBeenCalled();
  }));

  it('ngOnInint() should not get layout from layout service', async(() => {
    spyOn(layoutService, 'getLayout').and.callThrough();
    activatedRoute.data['value']['layout'] = null;
    hostComponent.ngOnInit();
    layoutService.parseLayoutConfig(Homelayout);
    expect(layoutService.getLayout).not.toHaveBeenCalled();
  }));

  it('ngOnInint() should update the class properties', async(() => {
    hostComponent.ngOnInit();
    layoutService.parseLayoutConfig(Homelayout);
    const res: any = 'test';
    expect(hostComponent.branding).toEqual(Homelayout.topBar.branding);
    expect(hostComponent.topMenuItems).toEqual(Homelayout.topBar.headerMenus);
  }));

  it('ngOnInint() should not update the class properties', async(() => {
    hostComponent.ngOnInit();
    const layout1 = {};
    layoutService.parseLayoutConfig(layout1);
    const res: any = 'test';
    expect(hostComponent.branding).not.toEqual(res);
  }));

  it('ngOnInint() should not update the class properties if layout is null', async(() => {
    hostComponent.ngOnInit();
    layoutService.parseLayoutConfig();
    const res: any = 'test';
    expect(hostComponent.branding).not.toEqual(res);
  }));

  it('toggelSideNav should update collapse property', async(() => {
    hostComponent.collapse = true;
    hostComponent.toggelSideNav();
    expect(hostComponent.collapse).toEqual(false);
  }));

  it('get activeTheme should return activeTheme property', async(() => {
    hostComponent.activeTheme = 'test';
    hostComponent.activeTheme = 'test';
    expect(hostComponent.activeTheme).toEqual('test');
  }));

  it('on_next() should call pageService.traverseFlowConfig()', async(() => {
    const test1 = JSON.stringify({
      result: [
        {
          result: {
            value: {
              path: 'test/t'
            }
          }
        }
      ]
    });
    const test = {
      body: test1
    };
    spyOn(pageService, 'traverseFlowConfig').and.callThrough();
    hostComponent.on_next(test);
    expect(pageService.traverseFlowConfig).toHaveBeenCalled();
  }));
});
