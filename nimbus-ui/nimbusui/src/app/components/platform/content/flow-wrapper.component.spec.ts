'use strict';
import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {
  Router,
  ActivatedRoute,
  Route,
  ActivatedRouteSnapshot,
  UrlSegment,
  Params,
  Data,
  ParamMap
} from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';

import { FlowWrapper } from './flow-wrapper.component';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { STOMPService } from '../../../services/stomp.service';
import { Subject } from 'rxjs';

let fixture, app, pageService, configService, router;

export class MockActivatedRoute implements ActivatedRoute {
  snapshot: ActivatedRouteSnapshot;
  url: Observable<UrlSegment[]>;
  params: Observable<Params>;
  queryParams: Observable<Params>;
  fragment: Observable<string>;
  outlet: string;
  component: any;
  routeConfig: Route;
  root: any = {
    children: []
  };
  parent: ActivatedRoute;
  firstChild: ActivatedRoute;
  children: ActivatedRoute[];
  pathFromRoot: ActivatedRoute[];
  data = Observable.of({
    domain: 'test'
  });
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

class MockConfigService {
  getFlowConfig(a) {
    return a;
  }
}

class MockRouter {
  navigate() {}
}

describe('FlowWrapper', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [FlowWrapper],
      imports: [RouterTestingModule, HttpModule, HttpClientTestingModule],
      providers: [
        { provide: STOMPService, useClass: MockSTOMPService },
        { provide: PageService, useClass: MockPageService },
        { provide: ConfigService, useClass: MockConfigService },
        { provide: Router, useClass: MockRouter },
        { provide: ActivatedRoute, useClass: MockActivatedRoute },
        CustomHttpClient,
        LoaderService
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(FlowWrapper);
    app = fixture.debugElement.componentInstance;
    pageService = TestBed.get(PageService);
    configService = TestBed.get(ConfigService);
    router = TestBed.get(Router);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('ngOnInit() call router.navigate() and pageService.loadDefaultPageForConfig()', async(() => {
    const test = {
      pageConfig: {
        config: {
          code: 123,
          uiStyles: {
            attributes: {
              route: 'testRoute'
            }
          }
        }
      }
    };
    spyOn(router, 'navigate').and.callThrough();
    spyOn(pageService, 'loadDefaultPageForConfig').and.callThrough();
    app.ngOnInit();
    pageService.logError(test);
    expect(router.navigate).toHaveBeenCalled();
    expect(pageService.loadDefaultPageForConfig).toHaveBeenCalled();
  }));

  it('ngOnInit() call router.navigate() and pageService.loadFlowConfig()', async(() => {
    const test = {
      pageConfig: {
        config: {
          code: 123,
          uiStyles: {
            attributes: {}
          }
        }
      }
    };
    spyOn(router, 'navigate').and.callThrough();
    spyOn(pageService, 'loadFlowConfig').and.callThrough();
    spyOn(configService, 'getFlowConfig').and.returnValue(undefined);
    app.ngOnInit();
    pageService.logError(test);
    expect(router.navigate).toHaveBeenCalled();
    expect(pageService.loadFlowConfig).toHaveBeenCalled();
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
    app.on_next(test);
    expect(pageService.traverseFlowConfig).toHaveBeenCalled();
  }));
});
