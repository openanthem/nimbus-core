/**
 * @license
 * Copyright 2016-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {
  HashLocationStrategy,
  Location,
  LocationStrategy
} from '@angular/common';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ElementRef } from '@angular/core';
import { async, TestBed } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import {
  actionDropDownElement,
  ActionDropDownLinkElement,
  actionDropDownLinkParams,
  actionDropDownParams,
  actionDropDownRowData,
  MockActionDropdownLink
} from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { Subject } from 'rxjs';
import { AppInitService } from '../../../../services/app.init.service';
import { ConfigService } from '../../../../services/config.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { LoggerService } from '../../../../services/logger.service';
import { PageService } from '../../../../services/page.service';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from '../../../../services/session.store';
import { setup } from '../../../../setup.spec';
import { ParamConfig } from '../../../../shared/param-config';
import { Param } from '../../../../shared/param-state';
import { Image } from '../../image.component';
import { Link } from '../../link.component';
import { SvgComponent } from '../../svg/svg.component';
import { CounterMessageService } from './../../../../services/counter-message.service';
import { ServiceConstants } from './../../../../services/service.constants';
import { NmMessageService } from './../../../../services/toastmessage.service';
import { ComponentTypes } from './../../../../shared/param-annotations.enum';
import { ActionDropdown, ActionLink } from './action-dropdown.component';
'use strict';

let pageservice, configservice;

class MockLoggerService {
  debug() {}
  info() {}
  error() {}
}

const declarations = [ActionLink, Image, Link, SvgComponent];
const imports = [
  HttpModule,
  HttpClientTestingModule,
  StorageServiceModule,
  AngularSvgIconModule
];
const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LoggerService, useClass: MockLoggerService },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  Location,
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  SessionStoreService,
  NmMessageService,
  AppInitService,
  CounterMessageService
];
let fixture, hostComponent;
describe('ActionLink', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture = TestBed.createComponent(ActionLink);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = MockActionDropdownLink;
    hostComponent.param = MockActionDropdownLink.config;
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

  it('should have a label set', async(() => {
    fixture.detectChanges();
    expect(
      fixture.debugElement.query(By.css('a.mockLink')).nativeElement.innerText
    ).toBe('Edit');
  }));

  it('should render an external link that opens a new page to google', async(() => {
    hostComponent.element.enabled = true;
    hostComponent.element.config.uiStyles.attributes.value = ComponentTypes.external.toString();
    hostComponent.element.config.uiStyles.attributes.target = '_blank';
    hostComponent.element.config.uiStyles.attributes.rel = 'nofollow';
    hostComponent.url = 'https://google.com/';
    fixture.detectChanges();
    let linkElement = fixture.debugElement.query(By.css('a.mockLink'));
    expect(linkElement.nativeElement.target).toBe('_blank');
    expect(linkElement.nativeElement.rel).toBe('nofollow');
    expect(linkElement.nativeElement.href).toBe('https://google.com/');
  }));

  it('should render an disabled link that is nonfunctional', async(() => {
    hostComponent.element.enabled = false;
    hostComponent.element.config.uiStyles.attributes.value = ComponentTypes.external.toString();
    hostComponent.element.config.uiStyles.attributes.rel = 'nofollow';
    fixture.detectChanges();
    let linkElement = fixture.debugElement.query(By.css('a.mockLink'));
    expect(linkElement.nativeElement.rel).toBe('nofollow');
    expect(linkElement.nativeElement.href).toBeFalsy();
    expect(
      linkElement.nativeElement.classList.contains('disabled')
    ).toBeTruthy();
  }));

  it('should show nm-action-link when a link param visible property is true', async(() => {
    hostComponent.element.visible = true;
    fixture.detectChanges();
    expect(hostComponent.visible).toBeTruthy();
    expect(fixture.debugElement.query(By.css('a.mockLink'))).toBeDefined();
  }));

  it('should hide nm-action-link when a link param visible property is false', async(() => {
    hostComponent.element.visible = false;
    fixture.detectChanges();
    expect(hostComponent.visible).toBeFalsy();
    expect(fixture.debugElement.query(By.css('a.mockLink'))).toBeNull();
  }));
});

class MockElementRef {}

class MockPageService {
  eventUpdate$: Subject<any>;
  constructor() {
    this.eventUpdate$ = new Subject();
  }
  processEvent(a, b, c) {}
}

const declarations1 = [ActionDropdown, ActionLink, Image, Link, SvgComponent];
const imports1 = [
  HttpModule,
  HttpClientTestingModule,
  BrowserAnimationsModule,
  AngularSvgIconModule
];
const providers1 = [
  { provide: ElementRef, useClass: MockElementRef },
  { provide: PageService, useClass: MockPageService },
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  Location,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  AppInitService,
  CounterMessageService
];

describe('ActionDropdown', () => {
  configureTestSuite(() => {
    setup(declarations1, imports1, providers1);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActionDropdown);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = actionDropDownElement as Param;
    hostComponent.params = actionDropDownParams as ParamConfig[];
    pageservice = TestBed.get(PageService);
  });

  it('should create the ActionDropdown', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('app should create elementRef property', async(() => {
    expect(hostComponent.elementRef).toBeTruthy();
  }));

  it('toggleOpen() should call dropDownClick.emit()', async(() => {
    const eve: any = {
      preventDefault: () => {}
    };
    spyOn(eve, 'preventDefault').and.callThrough();
    spyOn(hostComponent.dropDownClick, 'emit').and.callThrough();
    hostComponent.toggleOpen(eve);
    expect(eve.preventDefault).toHaveBeenCalled();
    expect(hostComponent.dropDownClick.emit).toHaveBeenCalled();
  }));

  it('processOnClick() should call pageservice.processEvent()', async(() => {
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

  it('Button should be created if the element.visible is configured as true', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    expect(buttonEle).toBeTruthy();
    expect(buttonEle.nativeElement.classList[0].toString()).toEqual(
      hostComponent.element.config.uiStyles.attributes.cssClass
    );
  }));

  it('OnClick of the button the toggleOpen() should be called', async(() => {
    hostComponent.params = actionDropDownLinkParams as ParamConfig[];
    hostComponent.element = actionDropDownElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    spyOn(hostComponent, 'toggleOpen').and.callThrough();
    const buttonEle = debugElement.query(By.css('button'));
    buttonEle.nativeElement.click();
    expect(hostComponent.toggleOpen).toHaveBeenCalled();
  }));

  it('OnClick of the button the dropdownContent should be visible', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    buttonEle.nativeElement.click();
    const divContentEle = debugElement.query(By.css('.dropdownContent'));
    expect(divContentEle).toBeTruthy();
  }));

  it('Nm-image should be created if the imgSrc is configured', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmImageEle = debugElement.query(By.css('nm-image'));
    expect(nmImageEle).toBeTruthy();
  }));

  it('Nm-image should not be created if the imgSrc is not configured', async(() => {
    hostComponent.element.config.uiStyles.attributes.imgSrc = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmImageEle = debugElement.query(By.css('nm-image'));
    expect(nmImageEle).toBeFalsy();
  }));

  it('If rowData is available then nm-action-link should be created', async(() => {
    hostComponent.rowData = actionDropDownRowData;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const actionLinkEle = debugElement.query(By.css('nm-action-link'));
    expect(actionLinkEle).toBeTruthy();
  }));

  it('If rowData is not available then nm-action-link should not be created', async(() => {
    hostComponent.element = ActionDropDownLinkElement as Param;
    hostComponent.params = actionDropDownLinkParams as ParamConfig[];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const actionLinkEle = debugElement.query(By.css('nm-action-link'));
    expect(actionLinkEle).toBeFalsy();
  }));

  it('If rowData is available then nm-link should not be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeFalsy();
  }));

  it('If rowData is not available then nm-link should be created', async(() => {
    hostComponent.element = ActionDropDownLinkElement as Param;
    hostComponent.params = actionDropDownLinkParams as ParamConfig[];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeTruthy();
  }));

  it('Button should not be created if the element.visible is configured as false', async(() => {
    hostComponent.element.visible = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    expect(buttonEle).toBeFalsy();
  }));

  it('enabled property should not be created', async(() => {
    hostComponent.element.enabled = false;
    expect(hostComponent.enabled).toBeFalsy();
  }));
});
