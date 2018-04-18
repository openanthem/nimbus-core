import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing'
import { Router, Resolve } from '@angular/router';

import { PageResolver } from './page-resolver.service';
import { PageService } from '../../../services/page.service';
import { WebContentSvc } from './../../../services/content-management.service';
import { BreadcrumbService } from './../breadcrumb/breadcrumb.service'
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';

let http, backend, service, rustate, breadcrumpservice, pageservice, wcservice, router;

const route = {
    _routerState: {
        url: '/test'
    },
    parent: {
        url: [{
            path: 'test'
        }]
    },
    params: {
        pageId: 123
    }
};

class MockPageService {

    getPageConfigById() {
        const res = {
            config: {
                code: 1234
            }
        }
        return new Promise(
            (resolve, reject) => {
                resolve(res);
            }
        );
    }

}

class MockWebContentSvc {
    findLabelContent(a) {
        const res = {
            text: 'testing'
        };
        return res;
    }
}

class MockBreadcrumbService {
    push(a, b, c) {
        console.log('123push is being called...', a, b, c);
     }
}

class MockRouter {
    navigate() {
        console.log('navigate is called');
     }
}

describe('PageResolver', () => {
  beforeEach(() => {
    breadcrumpservice = new MockBreadcrumbService();
    TestBed.configureTestingModule({
      imports: [ 
          HttpClientTestingModule, 
          HttpModule,
          RouterTestingModule
        ],
      providers: [
        {provide: PageService, useClass: MockPageService},
        {provide: WebContentSvc, useClass: MockWebContentSvc},
        {provide: BreadcrumbService, useValue: breadcrumpservice},
        {provide: Router, useClass: MockRouter},
        PageResolver,
        CustomHttpClient,
        LoaderService,
        ConfigService
        ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    pageservice = TestBed.get(PageService);
    wcservice = TestBed.get(WebContentSvc);
    router = TestBed.get(Router);
    service = new PageResolver(pageservice, router, breadcrumpservice, wcservice );
  });

  it('should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('resolve() should call breadcrumpservice.push()', async(() => {
    let spy = spyOn(breadcrumpservice, 'push').and.callThrough();
    let result = service.resolve(route, rustate);
    result.then(data => {
        expect(breadcrumpservice.push).toHaveBeenCalled();
    });
  })); 

  it('resolve() should call breadcrumpservice.push() without labelText', async(() => {
    let spy = spyOn(breadcrumpservice, 'push').and.callThrough();
    spyOn(wcservice, 'findLabelContent').and.returnValue({});
    let result = service.resolve(route, rustate);
    result.then(data => {
        expect(breadcrumpservice.push).toHaveBeenCalled();
    });
  }));


  it('resolve() should call router.navigate()', async(() => {
    spyOn(wcservice, 'findLabelContent').and.returnValue({});
    spyOn(pageservice, 'getPageConfigById').and.returnValue(new Promise(
        (resolve, reject) => {
            resolve(null);
        }
    ));
    spyOn(router, 'navigate').and.callThrough();
    let result = service.resolve(route, rustate);
    result.then(data => {
        expect(router.navigate).toHaveBeenCalled();
    });
  }));

});