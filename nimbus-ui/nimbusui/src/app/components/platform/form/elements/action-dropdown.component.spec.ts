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
import { fieldValueParam } from 'mockdata';

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
 let fixture, hostComponent;
describe('ActionLink', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActionLink);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
    pageservice = TestBed.get(PageService);
    configservice = TestBed.get(ConfigService);
  });

  it('should create the ActionLink', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('getAllURLParams() should return null', async(() => {
    expect(hostComponent.getAllURLParams('www.test.com')).toBeFalsy();
  }));

  it('processOnClick() should call processEvent', async(() => {
    spyOn(pageservice, 'processEvent').and.callThrough();
    hostComponent.element.enabled = true;
    hostComponent.processOnClick('test');
    expect(pageservice.processEvent).toHaveBeenCalled();
  }));

  it('processOnClick() should not call processEvent', async(() => {
    spyOn(pageservice, 'processEvent').and.callThrough();
    hostComponent.element.enabled = false;
    hostComponent.processOnClick('test');
    expect(pageservice.processEvent).not.toHaveBeenCalled();
  }));

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
  configureTestSuite(() => {
    setup( declarations1, imports1, providers1);
  });  
   
  beforeEach(() => {
      fixture = TestBed.createComponent(ActionDropdown);
      hostComponent = fixture.debugElement.componentInstance;
      hostComponent.element = fieldValueParam;
      pageservice = TestBed.get(PageService);
    });
  
  it('should create the ActionDropdown', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('app should create elementRef property',async(() => {
    expect(hostComponent.elementRef).toBeTruthy();
  }));

  it('toggleOpen() should call dropDownClick.emit()', async(() => {
    const eve: any = {
      preventDefault: () => { }
    };
    spyOn(eve, 'preventDefault').and.callThrough();
    spyOn(hostComponent.dropDownClick, 'emit').and.callThrough();
    hostComponent.toggleOpen(eve);
    expect(eve.preventDefault).toHaveBeenCalled();
    expect(hostComponent.dropDownClick.emit).toHaveBeenCalled();
  }));

  it('processOnClick() should call pageservice.processEvent()',async(() => {
    spyOn(pageservice, 'processEvent').and.callThrough();
    hostComponent.processOnClick('test');
    expect(pageservice.processEvent).toHaveBeenCalled();
  }));

  it('animationStart() should not update isHidden property', async(() => {
    hostComponent.isHidden = true;
    hostComponent.animationStart('test');
    expect(hostComponent.isHidden).toBeFalsy();
  }));

  it('animationDone() should update isHidden property', async(() => {
    hostComponent.isOpen = false;
    hostComponent.isHidden = false;
    hostComponent.animationDone('');
    expect(hostComponent.isHidden).toBeTruthy();
  }));

  it('animationDone() should not update isHidden property', async(() => {
    hostComponent.isOpen = true;
    hostComponent.isHidden = false;
    hostComponent.animationDone('');
    expect(hostComponent.isHidden).toBeFalsy();
  }));

  it('element proprty should be equal to element.enabled', async(() => {
    hostComponent.element.enabled = true;
    expect(hostComponent.enabled).toEqual(true);
  }));

  it('enabled property should not be created', async(() => {
    hostComponent.element.enabled = false;
    expect(hostComponent.enabled).toBeFalsy();
  }));
  
});