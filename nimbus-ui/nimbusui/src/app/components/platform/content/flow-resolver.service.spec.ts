import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing'
import { Router, Resolve } from '@angular/router';
import { ActivatedRoute, Route, ActivatedRouteSnapshot, UrlSegment, Params, Data, ParamMap } from '@angular/router';
import { of as observableOf,  Observable } from 'rxjs';

import { FlowResolver } from './flow-resolver.service';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { Subject } from 'rxjs';

let http, backend, service, pageservice, configservice, router, activatedroute;

class MockRouter {
  navigate() { }
}

class MockPageService {
  public config$: Subject<any>;

  constructor() {
    this.config$ = new Subject<any>();
  }

  buildFlowBaseURL() {
    const res = {
      pageConfig: {
        config: {
          uiStyles: {
            attributes: {
              route: 'testRoute'
            }
          }
        }
      }
    };
    this.config$.next(res);
  }

  loadFlowConfig(a) { }
  loadDefaultPageForConfig(a) { }
  loadDomainFlowConfig() {
    const res = {
      pageConfig: {
        config: {
          uiStyles: {
            attributes: {}
          }
        }
      }
    };
    this.config$.next(res);
  }

}

class MockConfigService {
  getFlowConfig(a) {}
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
  data = observableOf({
          layout: 'test'
    });
  paramMap: Observable<ParamMap>;
  queryParamMap: Observable<ParamMap>;
}


describe('FlowResolver', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          {provide: Router, useClass: MockRouter},
          {provide: PageService, useClass: MockPageService},
          {provide: ConfigService, useClass: MockConfigService},
          {provide: ActivatedRouteSnapshot, useValue: {root: ''}},
          { provide: ActivatedRoute, useClass: MockActivatedRoute },
          CustomHttpClient,
          LoaderService
        ],
      imports: [ 
          HttpClientTestingModule, 
          HttpModule, 
          RouterTestingModule 
        ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    pageservice = TestBed.get(PageService);
    configservice = TestBed.get(ConfigService);
    router = TestBed.get(Router);
    activatedroute = TestBed.get(ActivatedRoute);
    service = new FlowResolver(pageservice, configservice, activatedroute, router );
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('resolve() should subscribe the page service. config$', async(() => {
    const route = {
      params: {
        flow: 'testFlow'
      }
    }
    let state;
    spyOn(router, 'navigate').and.returnValue({});
    service.resolve(route, state);
    pageservice.buildFlowBaseURL();
    expect(router.navigate).toHaveBeenCalled();
  }));

  it('resolve() should subscribe the page service. config$ and navigate with out toPage', async(() => {
    const route = {
      params: {
        flow: 'testFlow'
      }
    }
    let state;
    spyOn(router, 'navigate').and.returnValue({});
    service.resolve(route, state);
    pageservice.loadDomainFlowConfig();
    expect(router.navigate).toHaveBeenCalled();
  }));


  it('resolve() should not call pageservice.loadflowconfig()', async(() => {
    const route = {
      params: {
        flow: 'testFlow'
      }
    }
    let state;
    spyOn(service._configSvc, 'getFlowConfig').and.returnValue({test: 123});
    spyOn(service._pageSvc, 'loadFlowConfig').and.callThrough();
    service.resolve(route, state);
    expect(service._pageSvc.loadFlowConfig).not.toHaveBeenCalled();
  }));

});