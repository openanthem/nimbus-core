'use strict';
import { TestBed, async } from '@angular/core/testing';
import * as Stomp from 'stompjs';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { Subject } from 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { EventEmitter } from '@angular/core';
import {
  ActivatedRoute,
  Route,
  ActivatedRouteSnapshot,
  UrlSegment,
  Params,
  Data,
  ParamMap
} from '@angular/router';

import { HomeLayoutCmp } from './home-layout.component';
import { STOMPService } from '../../services/stomp.service';
import { FooterGlobal } from '../platform/footer/footer-global.component';
import { NavMenuGlobal } from '../platform/globalNavMenu/nav-global-menu.component';
import { HeaderGlobal } from '../platform/header/header-global.component';
import { Link } from '../platform/link.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { KeysPipe } from '../../pipes/app.pipe';
import { Value } from '../platform/form/elements/value.component';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { CustomHttpClient } from '../../services/httpclient.service';
import { WebContentSvc } from '../../services/content-management.service';
import { PageService } from '../../services/page.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { AuthenticationService } from '../../services/authentication.service';
import { BreadcrumbService } from '../platform/breadcrumb/breadcrumb.service';
import { LayoutService } from '../../services/layout.service';
import { LoggerService } from '../../services/logger.service';

class MockAuthenticationService {
  logout() {
    const logout = 'testing';
    return Observable.of(logout);
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

let component, layoutService, activatedRoute, pageService;

describe('HomeLayoutCmp', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        HomeLayoutCmp,
        FooterGlobal,
        NavMenuGlobal,
        HeaderGlobal,
        Link,
        Paragraph,
        ComboBox,
        KeysPipe,
        Value,
        SelectItemPipe,
        TooltipComponent
      ],
      providers: [
        { provide: AuthenticationService, useClass: MockAuthenticationService },
        { provide: LayoutService, useClass: MockLayoutService },
        { provide: ActivatedRoute, useClass: MockActivatedRoute },
        { provide: PageService, useClass: MockPageService },
        CustomHttpClient,
        WebContentSvc,
        LoaderService,
        ConfigService,
        BreadcrumbService,
        STOMPService,
        LoggerService
      ],
      imports: [
        RouterTestingModule,
        FormsModule,
        DropdownModule,
        HttpClientModule,
        HttpModule
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(HomeLayoutCmp);
    component = fixture.debugElement.componentInstance;
    layoutService = TestBed.get(LayoutService);
    activatedRoute = TestBed.get(ActivatedRoute);
    pageService = TestBed.get(PageService);
  }));

  it('ngOnInint() should get layout from layout service', async(() => {
    spyOn(component.layoutSvc, 'getLayout').and.callThrough();
    component.ngOnInit();
    const layout = {
      topBar: {
        branding: 'test',
        headerMenus: 'tHeaderMenus'
      },
      leftNavBar: [],
      footer: 'FooterConfig',
      subBar: {
        menuItems: 'tMenuItems',
        menuLinks: 'tMenuLinks',
        organization: 'torganization'
      }
    };
    layoutService.parseLayoutConfig(layout);
    expect(component.layoutSvc.getLayout).toHaveBeenCalled();
  }));

  it('ngOnInint() should not get layout from layout service', async(() => {
    spyOn(component.layoutSvc, 'getLayout').and.callThrough();
    activatedRoute.data['value']['layout'] = null;
    component.ngOnInit();
    const layout = {
      topBar: {
        branding: 'test',
        headerMenus: 'tHeaderMenus'
      },
      leftNavBar: [],
      footer: 'FooterConfig',
      subBar: {
        menuItems: 'tMenuItems',
        menuLinks: 'tMenuLinks',
        organization: 'torganization'
      }
    };
    layoutService.parseLayoutConfig(layout);
    expect(component.layoutSvc.getLayout).not.toHaveBeenCalled();
  }));

  it('ngOnInint() should update the class properties', async(() => {
    component.ngOnInit();
    const layout = {
      topBar: {
        branding: 'test',
        headerMenus: 'tHeaderMenus'
      },
      leftNavBar: [],
      footer: 'FooterConfig',
      subBar: {
        menuItems: 'tMenuItems',
        menuLinks: 'tMenuLinks',
        organization: 'torganization'
      }
    };
    layoutService.parseLayoutConfig(layout);
    expect(component.branding).toEqual('test');
    expect(component.topMenuItems).toEqual('tHeaderMenus');
    expect(component.navMenuBar).toEqual(true);
    expect(component.organization).toEqual('torganization');
    expect(component.menuItems).toEqual('tMenuItems');
    expect(component.menuLinks).toEqual('tMenuLinks');
    expect(component.leftMenuItems).toEqual([]);
  }));

  it('ngOnInint() should not update the class properties', async(() => {
    component.ngOnInit();
    const layout = {};
    layoutService.parseLayoutConfig(layout);
    expect(component.branding).not.toEqual('test');
  }));

  it('ngOnInint() should not update the class properties if layout is null', async(() => {
    component.ngOnInit();
    const layout = {};
    layoutService.parseLayoutConfig();
    expect(component.branding).not.toEqual('test');
  }));

  it('should create the HomeLayoutCmp', async(() => {
    expect(component).toBeTruthy();
  }));

  it('toggelSideNav should update collapse property', async(() => {
    component.collapse = true;
    component.toggelSideNav();
    expect(component.collapse).toEqual(false);
  }));

  it('get activeTheme should return activeTheme property', async(() => {
    component.activeTheme = 'test';
    component.activeTheme = 'test';
    expect(component.activeTheme).toEqual('test');
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
    component.on_next(test);
    expect(pageService.traverseFlowConfig).toHaveBeenCalled();
  }));

});
