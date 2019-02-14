/**
 * @license
 * Copyright 2016-2018 the original author or authors.
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

import { NmMessageService } from './../../../../services/toastmessage.service';
import { Param } from './../../../../shared/param-state';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { KeyFilterModule } from 'primeng/keyfilter';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { Signature } from './signature.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { ControlSubscribers } from '../../../../services/control-subscribers.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { signatureElement } from 'mockdata';
import { By } from '@angular/platform-browser';
import { ServiceConstants } from '../../../../services/service.constants';

let logger, param;

class MockControlSubscribers {
  onEnabledUpdateSubscriber(a, b, c) {  }
}

class MockLoggerService {
  debug(a) {  }
  info(a) {}
  error(a) {}
}

const declarations = [
  Signature,
  TooltipComponent,
  InputLabel
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
  { provide: ControlSubscribers, useClass: MockControlSubscribers},
  { provide: LoggerService, useClass: MockLoggerService},
  NmMessageService,
  Location,
  SessionStoreService,
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  KeyFilterModule,
  AppInitService
 ];

 let fixture, hostComponent;

describe('Signature', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Signature);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = signatureElement as Param;
    logger = TestBed.get(LoggerService);
  });

  it('should create the Signature', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('nm-input-label should be created on configuring the label', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle.name).toEqual('nm-input-label');
  }));

  it('nm-input-label should not be created if elment.labels is not provided', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle).toBeFalsy;
  }));

  it('canvas should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const canvasEle = debugElement.query(By.css('canvas'));
    expect(canvasEle).toBeTruthy();
  }));

  it('on click of the clear button the clear() should be called', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const clearButtonEle = debugElement.query(By.css('.btn.btn-secondary.post-btn'));
    spyOn(hostComponent, 'clear').and.callThrough();
    expect(clearButtonEle).toBeTruthy();
    clearButtonEle.nativeElement.click();
    expect(hostComponent.clear).toHaveBeenCalled();
  }));

  it('on click of the save button the save() should be called', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allButtonEle = debugElement.queryAll(By.css('button'));
    spyOn(hostComponent, 'save').and.callThrough();
    expect(allButtonEle.length > 0).toBeTruthy();
    allButtonEle[0].nativeElement.click();
    expect(hostComponent.save).toHaveBeenCalled();
  }));

  it('on click of the zoom in button the zoomCanvas() should be called', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allButtonEle = debugElement.queryAll(By.css('button'));
    spyOn(hostComponent, 'zoomCanvas').and.callThrough();
    expect(allButtonEle.length > 0).toBeTruthy();
    allButtonEle[2].nativeElement.click();
    expect(hostComponent.zoomCanvas).toHaveBeenCalled();
  }));

  it('on click of the zoom out button the shrinkCanvas() should be called', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allButtonEle = debugElement.queryAll(By.css('button'));
    expect(allButtonEle.length > 0).toBeTruthy();
    allButtonEle[2].nativeElement.click();
    fixture.detectChanges();
    spyOn(hostComponent, 'shrinkCanvas').and.callThrough();
    fixture.whenStable().then(() => {
      const allButtonEle1 = debugElement.queryAll(By.css('button'));
      allButtonEle1[2].nativeElement.click();
      expect(hostComponent.shrinkCanvas).toHaveBeenCalled();
      expect(hostComponent.zoomFactor).toEqual(1);
    });
  }));

  it('on click of the getUpdatesignature in button the getUpdatedSignature() should be called', async(() => {
    hostComponent.getUpdatedSignature = () => { };
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allButtonEle = debugElement.queryAll(By.css('button'));
    spyOn(hostComponent, 'getUpdatedSignature').and.callThrough();
    expect(allButtonEle.length > 0).toBeTruthy();
    allButtonEle[3].nativeElement.click();
    expect(hostComponent.getUpdatedSignature).toHaveBeenCalled();
  }));

  it('ngOnInit() should update the height and width properties', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.width = '100';
      hostComponent.element.config.uiStyles.attributes.height = '200';
      hostComponent.ngOnInit();
      expect(hostComponent.width).toEqual(100);
      expect(hostComponent.height).toEqual(200);
    });
  });

  it('zoomCanvas() should update zoomFactor as 2 and zoomClass as zoom', async(() => {
    hostComponent.zoomCanvas();
    expect(hostComponent.zoomFactor).toEqual(2);
    expect(hostComponent.zoomClass).toEqual('zoom');
  }));

  it('shrinkCanvas() should update zoomFactor as 1 and zoomClass as empty string', async(() => {
    hostComponent.shrinkCanvas();
    expect(hostComponent.zoomFactor).toEqual(1);
    expect(hostComponent.zoomClass).toEqual('');
  }));

  it('ngAfterViewInit() should call initCanvasElement()', async(() => {
    (hostComponent as any).initCanvasElement = () => { };
    const spy = spyOn((hostComponent as any), 'initCanvasElement').and.callThrough();
    hostComponent.ngAfterViewInit();
    expect(spy).toHaveBeenCalled();
  }));

  it('initCanvasElement() should call applyContextRules(), renderExistingSignature(), registerCaptureEvents()', async(() => {
    (hostComponent as any).applyContextRules = () => { };
    (hostComponent as any).renderExistingSignature = () => { };
    (hostComponent as any).registerCaptureEvents = () => { };
    const spy1 = spyOn((hostComponent as any), 'applyContextRules').and.callThrough();
    const spy2 = spyOn((hostComponent as any), 'renderExistingSignature').and.callThrough();
    const spy3 = spyOn((hostComponent as any), 'registerCaptureEvents').and.callThrough();
    (hostComponent as any).initCanvasElement();
    expect(spy1).toHaveBeenCalled();
    expect(spy2).toHaveBeenCalled();
    expect(spy3).toHaveBeenCalled();

  }));

  it('initCanvasElement() should update hostComponent.canvas.nativeElement.width, hostComponent.canvas.nativeElement.height', async(() => {
    (hostComponent as any).applyContextRules = () => { };
    (hostComponent as any).renderExistingSignature = () => { };
    (hostComponent as any).registerCaptureEvents = () => { };
    hostComponent.canvas = { nativeElement: { width: '', height: '' } };
    hostComponent.width = 10;
    hostComponent.height = 20;
    (hostComponent as any).initCanvasElement();
    expect(hostComponent.canvas.nativeElement.width).toEqual(10);
    expect(hostComponent.canvas.nativeElement.height).toEqual(20);
  }));

  it('renderExistingSignature() should call logger.debug', async(() => {
    hostComponent.value = 'mockValue';
    const canvas: any = {
      getContext: a => {
        return { drawImage: (a, b, c, d, e) => { } };
      }
    };
    hostComponent.canvasEl = canvas;
    spyOn(logger, 'debug').and.callThrough();
    (hostComponent as any).renderExistingSignature();
    expect(logger.debug).toHaveBeenCalled();
  }));

  it('applyContextRules() should return  {lineCap: round, lineWidth: 3, strokeStyle: #000}', async(() => {
    const canvas: any = {
      getContext: a => {
        return { lineWidth: '', lineCap: '', strokeStyle: '' };
      }
    };
    hostComponent.canvasEl = canvas;
    expect((hostComponent as any).applyContextRules()).toEqual({
      lineCap: 'round',
      lineWidth: 3,
      strokeStyle: '#000'
    });

  }));

  it('save() should call clear()', async(() => {
    const canvas: any = {
      toDataURL: () => {
        return 'test';
      }
    };
    hostComponent.canvasEl = canvas;
    hostComponent.defaultEmptyImage = 'test';
    spyOn(hostComponent, 'clear').and.returnValue('');
    hostComponent.save();
    expect(hostComponent.clear).toHaveBeenCalled();
  }));

  it('save() should update isSaved without calling clear()', async(() => {
    const canvas: any = {
      toDataURL: () => {
        return 'test';
      }
    };
    hostComponent.canvasEl = canvas
    hostComponent.defaultEmptyImage = 'test1';
    spyOn(hostComponent, 'clear').and.returnValue('');
    hostComponent.save();
    expect(hostComponent.clear).not.toHaveBeenCalled();
    expect(hostComponent.isSaved).toBeTruthy();
  }));

  it('zoomCanvas() should update zoomfactor and zoomclass', async(() => {
    hostComponent.zoomCanvas();
    expect(hostComponent.zoomFactor).toEqual(2);
    expect(hostComponent.zoomClass).toEqual('zoom');
  }));

  it('shrinkCanvas() should update zoomFactor and zoomclass', async(() => {
    hostComponent.shrinkCanvas();
    expect(hostComponent.zoomFactor).toEqual(1);
    expect(hostComponent.zoomClass).toEqual('');
  }));

  it('drawOnCanvas() should call canvasEl.getBoundingClientRect()', async(() => {
    const prevEvent = { clientX: 10, clientY: 20 };
    const currentEvent = { clientX: 10, clientY: 20 };
    const canvas: any = {
      getBoundingClientRect: () => {
        return { left: 5, top: 5 };
      }, getContext: a => {
        return { beginPath: () => { }, moveTo: (a, b) => { }, lineTo: (a, b) => { }, stroke: () => { } };
      }
    };
    hostComponent.canvasEl = canvas;
    hostComponent.zoomFactor = 1;
    spyOn(hostComponent.canvasEl, 'getBoundingClientRect').and.callThrough();
    (hostComponent as any).drawOnCanvas(prevEvent, currentEvent);
    expect(hostComponent.canvasEl.getBoundingClientRect).toHaveBeenCalled();
  }));

  it('clear() should update value, isSaved and call canvasEl.getContext', async(() => {
    const canvas: any = {
      getContext: a => {
        return { clearRect: (a, b, c, d) => { } };
      }
    };
    hostComponent.canvasEl = canvas;
    spyOn(hostComponent.canvasEl, 'getContext').and.callThrough();
    hostComponent.clear();
    expect(hostComponent.value).toEqual('');
    expect(hostComponent.isSaved).toBeFalsy();
    expect(hostComponent.canvasEl.getContext).toHaveBeenCalled();
  }));

  it('captureType should update from element.config.uiStyles.attributes.captureType', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.captureType = 'test';
      expect(hostComponent.captureType).toEqual('test');
    });
  });

  it('setIsCapturing() should update isCapturing and call logger.debug()', async(() => {
    (hostComponent as any).isEditable = () => {
      return true;
    };
    spyOn(logger, 'debug').and.callThrough();
    (hostComponent as any).setIsCapturing(true);
    expect(hostComponent.isCapturing).toBeTruthy();
    expect(logger.debug).toHaveBeenCalled();
  }));

  it('isEditable should be true', async(() => {
    hostComponent.disabled = false;
    hostComponent.isSaved = false;
    expect((hostComponent as any).isEditable).toBeTruthy();
  }));

  it('registerCaptureEvents() should call registerCaptureOnEvent() with mousedown, mouseup', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.captureType = 'DEFAULT';
      (hostComponent as any).registerCaptureOnEvent = (a, b) => { };
      const spy = spyOn((hostComponent as any), 'registerCaptureOnEvent').and.callThrough();
      (hostComponent as any).registerCaptureEvents();
      expect(spy).toHaveBeenCalled();
      expect(spy).toHaveBeenCalledWith('mousedown', 'mouseup');
    });
  });

  it('registerCaptureEvents() should call registerCaptureOnEvent() with click, click', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.captureType = 'ON_CLICK';
      (hostComponent as any).registerCaptureOnEvent = (a, b) => { };
      const spy = spyOn((hostComponent as any), 'registerCaptureOnEvent').and.callThrough();
      (hostComponent as any).registerCaptureEvents();
      expect(spy).toHaveBeenCalled();
      expect(spy).toHaveBeenCalledWith('click', 'click');
    });
  });

});