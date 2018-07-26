'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { Link } from './link.component';
import { PageService } from '../../services/page.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { WebContentSvc } from '../../services/content-management.service';
import { Param } from '../../shared/param-state';

let fixture, app, pageService, configService;

class MockWebContentSvc {
    findLabelContentFromConfig(a, b) {

    }
}

class MockPageService {
    processEvent(a, b, c, d) {

    }
}

describe('Link', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          Link
       ],
       imports: [
           HttpModule,
           HttpClientTestingModule
       ],
       providers: [
           {provide: WebContentSvc, useClass: MockWebContentSvc},
           {provide: PageService, useClass: MockPageService},
           CustomHttpClient,
           LoaderService,
           ConfigService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(Link);
    app = fixture.debugElement.componentInstance;
    pageService = TestBed.get(PageService);
    configService = TestBed.get(ConfigService);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

//   xit('1should create the app', async(() => {
//       app.inClass = true;
//       app.ngOnInit();
//       console.log('app.linkClass..app.linkClass...app.linkClass', app.linkClass);
//   }));

  it('processOnClick() should call pageService.processEvent()', async(() => {
    spyOn(pageService, 'processEvent').and.callThrough();  
    app.processOnClick('/test', 'GET', 'test');
    expect(pageService.processEvent).toHaveBeenCalled();
  }));

  it('processOnClick() should call pageService.processEvent() and call GenericDomain.addAttribute()', async(() => {
    spyOn(pageService, 'processEvent').and.callThrough();  
    app.root = new Param(configService);
    app.root.type = {
        model: {
            params: [{
                path: 'test/id'
            }]
        }
    };
    app.element = {
        path: 'test/t'
    };
    app.processOnClick('/test', 'GET', 'test');
    expect(pageService.processEvent).toHaveBeenCalled();
  }));

  it('processOnClick() should call pageService.processEvent() and should not call GenericDomain.addAttribute()', async(() => {
    spyOn(pageService, 'processEvent').and.callThrough();  
    app.root = new Param(configService);
    app.root.type = {
        model: {
            params: [{
                path: 3
            }]
        }
    };
    app.element = {
        path: 'test/t'
    };
    app.processOnClick('/test', 'GET', 'test');
    expect(pageService.processEvent).toHaveBeenCalled();
  }));

  it('url should be update from the element', async(() => {
      app.element = {
          config: {
              uiStyles: {
                  attributes: {
                      url: '/test'
                  }
              }
          }
      };
    expect(app.url).toEqual('/test');
  }));

  it('value should be updated from the element', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    value: 'tvalue'
                }
            }
        }
    };
  expect(app.value).toEqual('tvalue');
  }));

  it('method should BE updated from the element', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    method: 'tmethod'
                }
            }
        }
    };
  expect(app.method).toEqual('tmethod');
  }));

  it('b property should be updated from the element', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    b: 'tb'
                }
            }
        }
    };
  expect(app.b).toEqual('tb');
  }));

  it('target should be updated from element', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    target: 'ttarget'
                }
            }
        }
    };
  expect(app.target).toEqual('ttarget');
  }));

  it('rel should be updated from the element', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    rel: 'trel'
                }
            }
        }
    };
  expect(app.rel).toEqual('trel');
  }));

});