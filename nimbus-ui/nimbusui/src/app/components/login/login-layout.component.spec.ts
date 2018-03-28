'use strict';
import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { EventEmitter } from '@angular/core';

import { Link } from '../platform/link.component';
import { LoginLayoutCmp } from './login-layout.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { CustomHttpClient } from '../../services/httpclient.service';
import { WebContentSvc } from '../../services/content-management.service';
import { PageService } from '../../services/page.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { LayoutService } from '../../services/layout.service';

class MockLayoutService {
  layout$: EventEmitter<any>;

  constructor() {
    this.layout$ = new EventEmitter<any>();
  }

  loadLayout() {
    const result = {
      topBar: {
        branding: 'testBrand',
        headerMenus: 'testHeaderMenus'
      },
      leftNavBar: 456,
      footer: 789,
      subBar: 111
    };
    this.layout$.next(result);
  }
  getLayout(param) {
    return 'test';
  }
}

describe('LoginLayoutCmp', () => {
  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        declarations: [LoginLayoutCmp, Link, Paragraph],
        imports: [RouterTestingModule, HttpClientModule, HttpModule],
        providers: [
          {provide: LayoutService, useClass: MockLayoutService},
          CustomHttpClient,
          WebContentSvc,
          PageService,
          LoaderService,
          ConfigService
        ]
      }).compileComponents();
    })
  );

  it(
    'should create the LoginLayoutCmp',
    async(() => {
      const fixture = TestBed.createComponent(LoginLayoutCmp);
      const app = fixture.debugElement.componentInstance;
      expect(app).toBeTruthy();
    })
  );

it(
  'ngoninit() should get branding, footer, topMenuItems',
  async(() => {
    const fixture = TestBed.createComponent(LoginLayoutCmp);
    const app = fixture.debugElement.componentInstance;
    const service = TestBed.get(LayoutService);
    app.ngOnInit();
    service.loadLayout();
    expect(app.branding).toEqual('testBrand');
    expect(app.footer).toEqual(789);
    expect(app.topMenuItems).toEqual('testHeaderMenus');
  })
);

});
