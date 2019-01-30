'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { CheckBox } from './checkbox.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { Param } from '../../../../shared/param-state';
import { checkboxElement } from 'mockdata';
import { By } from '@angular/platform-browser';
import { ServiceConstants } from '../../../../services/service.constants';
import { WindowRefService } from './../../../../services/window-ref.service';

let param: Param;

class MockLoggerService {
  debug() { }
  info() { }
  error() { }
}

const declarations = [
  CheckBox,
  TooltipComponent
 ];
 const imports = [
  FormsModule,
  HttpClientModule,
  HttpModule,
  StorageServiceModule
 ];
 const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  {provide: LoggerService, useClass: MockLoggerService},
  Location,
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  SessionStoreService,
  AppInitService,
  WindowRefService
 ];
 let fixture, hostComponent;
describe('CheckBox', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(CheckBox);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = checkboxElement as Param;
  });

  it('should create the CheckBox', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('input should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputEle = debugElement.query(By.css('input'));
    expect(inputEle).toBeTruthy();
  }));

  it('change event on input should call emitValueChangedEvent()', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    spyOn(hostComponent, 'emitValueChangedEvent').and.callThrough();
    const inputEle = debugElement.query(By.css('input')).nativeElement;
    inputEle.click();
    expect(hostComponent.emitValueChangedEvent).toHaveBeenCalled();
  }));

  it('nm-tooltip should be created if helpText is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const tooltipEle = debugElement.query(By.css('nm-tooltip'));
    expect(tooltipEle).toBeTruthy();
  }));

  it('nm-tooltip should not be created if helpText is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const tooltipEle = debugElement.query(By.css('nm-tooltip'));
    expect(tooltipEle).toBeFalsy();
  }));

});

