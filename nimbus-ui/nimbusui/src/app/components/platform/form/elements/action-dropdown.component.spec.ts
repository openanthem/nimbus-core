'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ElementRef } from '@angular/core';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';
import { AngularSvgIconModule } from 'angular-svg-icon';

import { ActionLink } from './action-dropdown.component';
import { ActionDropdown } from './action-dropdown.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { Page } from '../../../../shared/app-config.interface';
import { ParamConfig } from '../../../../shared/param-config';
import { Param } from '../../../../shared/param-state';
import { WebContentSvc } from '../../../../services/content-management.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { Image } from '../../image.component';
import { Link } from '../../link.component';
import { SvgComponent } from '../../svg/svg.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import * as data from '../../../../payload.json';

let pageservice, configservice, param;

class MockLoggerService {
    debug() { }
    info() { }
    error() { }
}

const declarations = [
  ActionLink,
  Image,
  Link,
  SvgComponent
 ];
 const imports = [
     HttpModule,
     HttpClientTestingModule,
     StorageServiceModule,
     AngularSvgIconModule
 ];
 const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  {provide: LoggerService, useClass: MockLoggerService},
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  Location,
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  SessionStoreService,
  AppInitService
 ];

describe('ActionLink', () => {

  configureTestSuite();
  setup(ActionLink, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<ActionLink>){
    this.hostComponent.element = param;
    pageservice = TestBed.get(PageService);
    configservice = TestBed.get(ConfigService);
  });

  it('should create the ActionLink', async function (this: TestContext<ActionLink>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('getAllURLParams() should return null', async function (this: TestContext<ActionLink>) {
    expect(this.hostComponent.getAllURLParams('www.test.com')).toBeFalsy();
  });

  it('processOnClick() should call processEvent', async function (this: TestContext<ActionLink>) {
    spyOn(pageservice, 'processEvent').and.callThrough();
    this.hostComponent.element.enabled = true;
    this.hostComponent.processOnClick('test');
    expect(pageservice.processEvent).toHaveBeenCalled();
  });

  it('processOnClick() should not call processEvent', async function (this: TestContext<ActionLink>) {
    spyOn(pageservice, 'processEvent').and.callThrough();
    this.hostComponent.element.enabled = false;
    this.hostComponent.processOnClick('test');
    expect(pageservice.processEvent).not.toHaveBeenCalled();
  });

});

class MockElementRef {

}

class MockPageService {
    processEvent(a, b, c) {}
}

const declarations1 = [
  ActionDropdown,
  ActionLink,
  Image,
  Link,
  SvgComponent
];
const imports1 = [
  HttpModule,
  HttpClientTestingModule,
  BrowserAnimationsModule,
  AngularSvgIconModule
];
const providers1 = [
  {provide: ElementRef, useClass: MockElementRef},
  {provide: PageService, useClass: MockPageService},
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  Location,
  WebContentSvc,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  AppInitService
];

describe('ActionDropdown', () => {
    configureTestSuite();
    setup(ActionDropdown, declarations1, imports1, providers1);
    param = (<any>data).payload;
  
    beforeEach(async function(this: TestContext<ActionDropdown>){
      this.hostComponent.element = param;
      pageservice = TestBed.get(PageService);
    });
  
  it('should create the ActionDropdown', async function (this: TestContext<ActionDropdown>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('app should create elementRef property', async function (this: TestContext<ActionDropdown>) {
    expect(this.hostComponent.elementRef).toBeTruthy();
  });

  it('toggleOpen() should call dropDownClick.emit()', async function (this: TestContext<ActionDropdown>) {
    const eve: any = {
      preventDefault: () => { }
    };
    spyOn(eve, 'preventDefault').and.callThrough();
    spyOn(this.hostComponent.dropDownClick, 'emit').and.callThrough();
    this.hostComponent.toggleOpen(eve);
    expect(eve.preventDefault).toHaveBeenCalled();
    expect(this.hostComponent.dropDownClick.emit).toHaveBeenCalled();
  });

  it('processOnClick() should call pageservice.processEvent()', async function (this: TestContext<ActionDropdown>) {
    spyOn(pageservice, 'processEvent').and.callThrough();
    this.hostComponent.processOnClick('test');
    expect(pageservice.processEvent).toHaveBeenCalled();
  });

  it('animationStart() should not update isHidden property', async function (this: TestContext<ActionDropdown>) {
    this.hostComponent.isHidden = true;
    this.hostComponent.animationStart('test');
    expect(this.hostComponent.isHidden).toBeFalsy();
  });

  it('animationDone() should update isHidden property', async function (this: TestContext<ActionDropdown>) {
    this.hostComponent.isOpen = false;
    this.hostComponent.isHidden = false;
    this.hostComponent.animationDone('');
    expect(this.hostComponent.isHidden).toBeTruthy();
  });

  it('animationDone() should not update isHidden property', async function (this: TestContext<ActionDropdown>) {
    this.hostComponent.isOpen = true;
    this.hostComponent.isHidden = false;
    this.hostComponent.animationDone('');
    expect(this.hostComponent.isHidden).toBeFalsy();
  });

  it('element proprty should be equal to element.enabled', async function (this: TestContext<ActionDropdown>) {
    this.hostComponent.element.enabled = true;
    expect(this.hostComponent.enabled).toEqual(true);
  });

  it('enabled property should not be created', async function (this: TestContext<ActionDropdown>) {
    this.hostComponent.element.enabled = false;
    expect(this.hostComponent.enabled).toBeFalsy();
  });
  
});