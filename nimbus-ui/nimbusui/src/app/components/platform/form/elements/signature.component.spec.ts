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
import * as data from '../../../../payload.json';

let logger, param;

class MockControlSubscribers {
  onEnabledUpdateSubscriber(a, b, c) {  }
}

class MockLoggerService {
  debug(a) {  }
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
  Location,
  SessionStoreService,
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  KeyFilterModule,
  AppInitService
 ];

describe('Signature', () => {
  configureTestSuite();
  setup(Signature, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<Signature>) {
    this.hostComponent.element = param;
    logger = TestBed.get(LoggerService);
  });

  it('should create the Signature', async function (this: TestContext<Signature>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('ngOnInit() should update the height and width properties', async function (this: TestContext<Signature>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.width = '100';
      this.hostComponent.element.config.uiStyles.attributes.height = '200';
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.width).toEqual(100);
      expect(this.hostComponent.height).toEqual(200);
    });
  });

  it('zoomCanvas() should update zoomFactor as 2 and zoomClass as zoom', async function (this: TestContext<Signature>) {
    this.hostComponent.zoomCanvas();
    expect(this.hostComponent.zoomFactor).toEqual(2);
    expect(this.hostComponent.zoomClass).toEqual('zoom');
  });

  it('shrinkCanvas() should update zoomFactor as 1 and zoomClass as empty string', async function (this: TestContext<Signature>) {
    this.hostComponent.shrinkCanvas();
    expect(this.hostComponent.zoomFactor).toEqual(1);
    expect(this.hostComponent.zoomClass).toEqual('');
  });

  it('ngAfterViewInit() should call initCanvasElement()', async function (this: TestContext<Signature>) {
    (this.hostComponent as any).initCanvasElement = () => { };
    const spy = spyOn((this.hostComponent as any), 'initCanvasElement').and.callThrough();
    this.hostComponent.ngAfterViewInit();
    expect(spy).toHaveBeenCalled();
  });

  it('initCanvasElement() should call applyContextRules(), renderExistingSignature(), registerCaptureEvents()', async function (this: TestContext<Signature>) {
    (this.hostComponent as any).applyContextRules = () => { };
    (this.hostComponent as any).renderExistingSignature = () => { };
    (this.hostComponent as any).registerCaptureEvents = () => { };
    const spy1 = spyOn((this.hostComponent as any), 'applyContextRules').and.callThrough();
    const spy2 = spyOn((this.hostComponent as any), 'renderExistingSignature').and.callThrough();
    const spy3 = spyOn((this.hostComponent as any), 'registerCaptureEvents').and.callThrough();
    (this.hostComponent as any).initCanvasElement();
    expect(spy1).toHaveBeenCalled();
    expect(spy2).toHaveBeenCalled();
    expect(spy3).toHaveBeenCalled();

  });

  it('initCanvasElement() should update this.hostComponent.canvas.nativeElement.width, this.hostComponent.canvas.nativeElement.height', async function (this: TestContext<Signature>) {
    (this.hostComponent as any).applyContextRules = () => { };
    (this.hostComponent as any).renderExistingSignature = () => { };
    (this.hostComponent as any).registerCaptureEvents = () => { };
    this.hostComponent.canvas = { nativeElement: { width: '', height: '' } };
    this.hostComponent.width = 10;
    this.hostComponent.height = 20;
    (this.hostComponent as any).initCanvasElement();
    expect(this.hostComponent.canvas.nativeElement.width).toEqual(10);
    expect(this.hostComponent.canvas.nativeElement.height).toEqual(20);
  });

  it('renderExistingSignature() should call logger.debug', async function (this: TestContext<Signature>) {
    this.hostComponent.value = 'mockValue';
    const canvas: any = {
      getContext: a => {
        return { drawImage: (a, b, c, d, e) => { } };
      }
    };
    this.hostComponent.canvasEl = canvas;
    spyOn(logger, 'debug').and.callThrough();
    (this.hostComponent as any).renderExistingSignature();
    expect(logger.debug).toHaveBeenCalled();
  });

  it('applyContextRules() should return  {lineCap: round, lineWidth: 3, strokeStyle: #000}', async function (this: TestContext<Signature>) {
    const canvas: any = {
      getContext: a => {
        return { lineWidth: '', lineCap: '', strokeStyle: '' };
      }
    };
    this.hostComponent.canvasEl = canvas;
    expect((this.hostComponent as any).applyContextRules()).toEqual({
      lineCap: 'round',
      lineWidth: 3,
      strokeStyle: '#000'
    });

  });

  it('save() should call clear()', async function (this: TestContext<Signature>) {
    const canvas: any = {
      toDataURL: () => {
        return 'test';
      }
    };
    this.hostComponent.canvasEl = canvas;
    this.hostComponent.defaultEmptyImage = 'test';
    spyOn(this.hostComponent, 'clear').and.returnValue('');
    this.hostComponent.save();
    expect(this.hostComponent.clear).toHaveBeenCalled();
  });

  it('save() should update isSaved without calling clear()', async function (this: TestContext<Signature>) {
    const canvas: any = {
      toDataURL: () => {
        return 'test';
      }
    };
    this.hostComponent.canvasEl = canvas
    this.hostComponent.defaultEmptyImage = 'test1';
    spyOn(this.hostComponent, 'clear').and.returnValue('');
    this.hostComponent.save();
    expect(this.hostComponent.clear).not.toHaveBeenCalled();
    expect(this.hostComponent.isSaved).toBeTruthy();
  });

  it('zoomCanvas() should update zoomfactor and zoomclass', async function (this: TestContext<Signature>) {
    this.hostComponent.zoomCanvas();
    expect(this.hostComponent.zoomFactor).toEqual(2);
    expect(this.hostComponent.zoomClass).toEqual('zoom');
  });

  it('shrinkCanvas() should update zoomFactor and zoomclass', async function (this: TestContext<Signature>) {
    this.hostComponent.shrinkCanvas();
    expect(this.hostComponent.zoomFactor).toEqual(1);
    expect(this.hostComponent.zoomClass).toEqual('');
  });

  it('drawOnCanvas() should call canvasEl.getBoundingClientRect()', async function (this: TestContext<Signature>) {
    const prevEvent = { clientX: 10, clientY: 20 };
    const currentEvent = { clientX: 10, clientY: 20 };
    const canvas: any = {
      getBoundingClientRect: () => {
        return { left: 5, top: 5 };
      }, getContext: a => {
        return { beginPath: () => { }, moveTo: (a, b) => { }, lineTo: (a, b) => { }, stroke: () => { } };
      }
    };
    this.hostComponent.canvasEl = canvas;
    this.hostComponent.zoomFactor = 1;
    spyOn(this.hostComponent.canvasEl, 'getBoundingClientRect').and.callThrough();
    (this.hostComponent as any).drawOnCanvas(prevEvent, currentEvent);
    expect(this.hostComponent.canvasEl.getBoundingClientRect).toHaveBeenCalled();
  });

  it('clear() should update value, isSaved and call canvasEl.getContext', async function (this: TestContext<Signature>) {
    const canvas: any = {
      getContext: a => {
        return { clearRect: (a, b, c, d) => { } };
      }
    };
    this.hostComponent.canvasEl = canvas;
    spyOn(this.hostComponent.canvasEl, 'getContext').and.callThrough();
    this.hostComponent.clear();
    expect(this.hostComponent.value).toEqual('');
    expect(this.hostComponent.isSaved).toBeFalsy();
    expect(this.hostComponent.canvasEl.getContext).toHaveBeenCalled();
  });

  it('captureType should update from element.config.uiStyles.attributes.captureType', async function (this: TestContext<Signature>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.captureType = 'test';
      expect(this.hostComponent.captureType).toEqual('test');
    });
  });

  it('setIsCapturing() should update isCapturing and call logger.debug()', async function (this: TestContext<Signature>) {
    (this.hostComponent as any).isEditable = () => {
      return true;
    };
    spyOn(logger, 'debug').and.callThrough();
    (this.hostComponent as any).setIsCapturing(true);
    expect(this.hostComponent.isCapturing).toBeTruthy();
    expect(logger.debug).toHaveBeenCalled();
  });

  it('isEditable should be true', async function (this: TestContext<Signature>) {
    this.hostComponent.disabled = false;
    this.hostComponent.isSaved = false;
    expect((this.hostComponent as any).isEditable).toBeTruthy();
  });

  it('registerCaptureEvents() should call registerCaptureOnEvent() with mousedown, mouseup', async function (this: TestContext<Signature>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.captureType = 'DEFAULT';
      (this.hostComponent as any).registerCaptureOnEvent = (a, b) => { };
      const spy = spyOn((this.hostComponent as any), 'registerCaptureOnEvent').and.callThrough();
      (this.hostComponent as any).registerCaptureEvents();
      expect(spy).toHaveBeenCalled();
      expect(spy).toHaveBeenCalledWith('mousedown', 'mouseup');
    });
  });

  it('registerCaptureEvents() should call registerCaptureOnEvent() with click, click', async function (this: TestContext<Signature>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.captureType = 'ON_CLICK';
      (this.hostComponent as any).registerCaptureOnEvent = (a, b) => { };
      const spy = spyOn((this.hostComponent as any), 'registerCaptureOnEvent').and.callThrough();
      (this.hostComponent as any).registerCaptureEvents();
      expect(spy).toHaveBeenCalled();
      expect(spy).toHaveBeenCalledWith('click', 'click');
    });
  });

});