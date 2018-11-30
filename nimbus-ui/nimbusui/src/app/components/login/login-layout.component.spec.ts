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
import { setup, TestContext } from '../../setup.spec';
import { AppBranding, FooterConfig } from '../../model/menu-meta.interface';
import { Param } from '../../shared/param-state';
import { configureTestSuite } from 'ng-bullet';

let layoutService, configService;

class MockLayoutService {
  layout$: EventEmitter<any>;

  constructor() {
    this.layout$ = new EventEmitter<any>();
  }

  loadLayout(val) {
    this.layout$.next(val);
  }
  getLayout(param) {
    return 'test';
  }
}

const declarations = [LoginLayoutCmp, Link, Paragraph];
const imports = [RouterTestingModule, HttpClientModule, HttpModule];
const providers = [
  {provide: LayoutService, useClass: MockLayoutService},
  CustomHttpClient,
  WebContentSvc,
  PageService,
  LoaderService,
  ConfigService
];
let fixture, hostComponent;

describe('LoginLayoutCmp', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(LoginLayoutCmp);
    hostComponent = fixture.debugElement.componentInstance;
    layoutService = TestBed.get(LayoutService);
    configService = TestBed.get(ConfigService);
  });

  it('should create the LoginLayoutCmp',  async(() => {
    expect(hostComponent).toBeTruthy();  
  }));

  // it('ngoninit() should get branding, footer, topMenuItems',  async(() => {
  //   const logo = new Param(configService);
  //   let branding: AppBranding = {} as AppBranding;
  //   branding.logo = logo;
  //   let headerMenusItem: Param;
  //   const headerMenus: Param[] = [headerMenusItem]
  //   let Footer: FooterConfig;
  //   const result = {
  //     topBar: {
  //       branding: branding,
  //       headerMenus: headerMenus
  //     },
  //     menu: 456,
  //     footer: Footer
  //   };
  //   hostComponent.ngOnInit();
  //   layoutService.loadLayout(result);
  //   expect(hostComponent.branding).toEqual(branding);
  //   expect(hostComponent.footer).toEqual(Footer);
  //   expect(hostComponent.topMenuItems).toEqual(headerMenus);
  // }));

});
