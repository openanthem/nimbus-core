'use strict';
import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientModule } from '@angular/common/http';
import {
  HttpModule,
  Http,
  Headers,
  RequestOptions,
  URLSearchParams
} from '@angular/http';
import { Subject } from 'rxjs';
import { Observable } from 'rxjs/Observable';
import { Router, ActivatedRoute, Route, ActivatedRouteSnapshot, UrlSegment, Params, Data, ParamMap } from '@angular/router';


import { MainLayoutCmp } from './main-layout.component';
import { HeaderGlobal } from '../platform/header/header-global.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { Value } from '../platform/form/elements/value.component';
import { Link } from '../platform/link.component';
import { CustomHttpClient } from '../../services/httpclient.service';
import { WebContentSvc } from '../../services/content-management.service';
import { PageService } from '../../services/page.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { AuthenticationService } from '../../services/authentication.service';
import { BreadcrumbService } from '../platform/breadcrumb/breadcrumb.service';
import { LayoutService } from '../../services/layout.service';
import { WindowRefService } from '../../services/window-ref.service';

let app, fixture, layoutService, authenticationService, windowRef, route;

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
          value: {layout: 123}
    };
  paramMap: Observable<ParamMap>;
  queryParamMap: Observable<ParamMap>;
}

export class MockActivatedRoute1 implements ActivatedRoute {
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
          value: {layout: null}
    };
  paramMap: Observable<ParamMap>;
  queryParamMap: Observable<ParamMap>;
}

class MockLayoutService {
  public layout$: Subject<any>;

  constructor() {
    this.layout$ = new Subject();
  }

  parseLayoutConfig(res) {
    this.layout$.next(res);
  }
  
  getLayout() { }
}

class MockWindowRefService {
  window = {
    location: {
      href: ''
    }
  };
}

class MockAuthenticationService {
  logout() {
    return Observable.of('');
  }
}

describe('MainLayoutCmp', () => {
  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        declarations: [MainLayoutCmp, HeaderGlobal, Paragraph, Value, Link],
        imports: [HttpModule, RouterTestingModule, HttpClientModule],
        providers: [
          {provide: LayoutService, useClass: MockLayoutService},
          {provide: WindowRefService, useClass: MockWindowRefService},
          {provide: AuthenticationService, useClass: MockAuthenticationService},
          {provide: ActivatedRoute, useClass: MockActivatedRoute},
          CustomHttpClient,
          WebContentSvc,
          PageService,
          LoaderService,
          ConfigService,
          BreadcrumbService
        ]
      }).compileComponents();
      fixture = TestBed.createComponent(MainLayoutCmp);
      app = fixture.debugElement.componentInstance;
      layoutService = TestBed.get(LayoutService);
      authenticationService = TestBed.get(AuthenticationService);
      windowRef = TestBed.get(WindowRefService);
      route = TestBed.get(ActivatedRoute);
    })
  );

  it('should create the app', async(() => {
      expect(app).toBeTruthy();
    }));

  it('toggelSideNav should update collapse property', async(() => {
      app.collapse = true;
      app.toggelSideNav();
      expect(app.collapse).toEqual(false);
    }));

  it('activeTheme() should return app.activeTheme', async(() => {
    app.activeTheme = 'testing';
    app.activeTheme = 'testing';
    expect(app.activeTheme).toEqual('testing');
  }));

  it('logout() should update the window.location.href', async(() => {
    app.logout();
    expect(windowRef.window.location.href.includes('logout')).toBeTruthy();
  }));

});
