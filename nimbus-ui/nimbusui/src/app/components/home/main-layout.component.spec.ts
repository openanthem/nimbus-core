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

describe('MainLayoutCmp', () => {
  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        declarations: [MainLayoutCmp, HeaderGlobal, Paragraph, Value, Link],
        imports: [HttpModule, RouterTestingModule, HttpClientModule],
        providers: [
          CustomHttpClient,
          WebContentSvc,
          PageService,
          LoaderService,
          ConfigService,
          AuthenticationService,
          BreadcrumbService
        ]
      }).compileComponents();
    })
  );

  it(
    'should create the app',
    async(() => {
      const fixture = TestBed.createComponent(MainLayoutCmp);
      const app = fixture.debugElement.componentInstance;
      expect(app).toBeTruthy();
    })
  );

  it(
    'toggelSideNav should update collapse property',
    async(() => {
      const fixture = TestBed.createComponent(MainLayoutCmp);
      const app = fixture.debugElement.componentInstance;
      app.collapse = true;
      app.toggelSideNav();
      expect(app.collapse).toEqual(false);
    })
  );

});
