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

let app, fixture, logger;

class MockControlSubscribers {
  onEnabledUpdateSubscriber(a, b, c) {  }
}

class MockLoggerService {
  debug(a) {  }
}

describe('Signature', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Signature,
        TooltipComponent,
        InputLabel
       ],
       imports: [
        FormsModule,
        HttpClientModule,
        HttpModule,
        StorageServiceModule
       ],
       providers: [
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
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(Signature);
    app = fixture.debugElement.componentInstance;
    logger = TestBed.get(LoggerService);
  }));

    it('should create the app', async(() => {
      expect(app).toBeTruthy();
    }));

    it('ngOnInit() should update the height and width properties', async(() => {
      app.element = { config: { uiStyles: { attributes: { width: 100, height: 200 } } } };
      app.ngOnInit();
      expect(app.width).toEqual(100);
      expect(app.height).toEqual(200);
    }));

    it('zoomCanvas() should update zoomFactor as 2 and zoomClass as zoom', async(() => {
      app.zoomCanvas();
      expect(app.zoomFactor).toEqual(2);
      expect(app.zoomClass).toEqual('zoom');
    }));

    it('shrinkCanvas() should update zoomFactor as 1 and zoomClass as empty string', async(() => {
      app.shrinkCanvas();
      expect(app.zoomFactor).toEqual(1);
      expect(app.zoomClass).toEqual('');
    }));

    it('ngAfterViewInit() should call initCanvasElement()', async(() => {
      app.initCanvasElement = () => {};
      spyOn(app, 'initCanvasElement').and.callThrough();
      app.ngAfterViewInit();
      expect(app.initCanvasElement).toHaveBeenCalled();
    }));

    it('initCanvasElement() should call applyContextRules(), renderExistingSignature(), registerCaptureEvents()', async(() => {
      app.applyContextRules = () => {};
      app.renderExistingSignature = () => {};
      app.registerCaptureEvents = () => {};
      spyOn(app, 'applyContextRules').and.callThrough();
      spyOn(app, 'renderExistingSignature').and.callThrough();
      spyOn(app, 'registerCaptureEvents').and.callThrough();
      app.initCanvasElement();
      expect(app.applyContextRules).toHaveBeenCalled();
      expect(app.renderExistingSignature).toHaveBeenCalled();
      expect(app.registerCaptureEvents).toHaveBeenCalled();
    }));

    it('initCanvasElement() should update app.canvas.nativeElement.width, app.canvas.nativeElement.height', async(() => {
      app.applyContextRules = () => {};
      app.renderExistingSignature = () => {};
      app.registerCaptureEvents = () => {};
      app.canvas = { nativeElement: { width: '', height: '' } };
      app.width = 10;
      app.height = 20;
      app.initCanvasElement();
      expect(app.canvas.nativeElement.width).toEqual(10);
      expect(app.canvas.nativeElement.height).toEqual(20);
    }));

    it('renderExistingSignature() should call logger.debug', async(() => {
      app.value = 'mockValue';
      app.canvasEl = { getContext: a => {
          return { drawImage: (a, b, c, d, e) => {} };
        } };
      spyOn(logger, 'debug').and.callThrough();
      app.renderExistingSignature();
      expect(logger.debug).toHaveBeenCalled();
    }));

    it('applyContextRules() should return  {lineCap: round, lineWidth: 3, strokeStyle: #000}', async(() => {
      app.canvasEl = { getContext: a => {
          return { lineWidth: '', lineCap: '', strokeStyle: '' };
        } };
      expect(app.applyContextRules()).toEqual({
        lineCap: 'round',
        lineWidth: 3,
        strokeStyle: '#000'
      });
    }));

    it('save() should call clear()', async(() => {
      app.canvasEl = { toDataURL: () => {
          return 'test';
        } };
      app.defaultEmptyImage = 'test';
      spyOn(app, 'clear').and.returnValue('');
      app.save();
      expect(app.clear).toHaveBeenCalled();
    }));

    it('save() should update isSaved without calling clear()', async(() => {
      app.canvasEl = { toDataURL: () => {
          return 'test';
        } };
      app.defaultEmptyImage = 'test1';
      spyOn(app, 'clear').and.returnValue('');
      app.save();
      expect(app.clear).not.toHaveBeenCalled();
      expect(app.isSaved).toBeTruthy();
    }));

    it('zoomCanvas() should update zoomfactor and zoomclass', async(() => {
      app.zoomCanvas();
      expect(app.zoomFactor).toEqual(2);
      expect(app.zoomClass).toEqual('zoom');
    }));

    it('shrinkCanvas() should update zoomFactor and zoomclass', async(() => {
      app.shrinkCanvas();
      expect(app.zoomFactor).toEqual(1);
      expect(app.zoomClass).toEqual('');
    }));

    it('drawOnCanvas() should call canvasEl.getBoundingClientRect()', async(() => {
      const prevEvent = { clientX: 10, clientY: 20 };
      const currentEvent = { clientX: 10, clientY: 20 };
      app.canvasEl = { getBoundingClientRect: () => {
          return { left: 5, top: 5 };
        }, getContext: a => {
          return { beginPath: () => {}, moveTo: (a, b) => {}, lineTo: (a, b) => {}, stroke: () => {} };
        } };
      app.zoomFactor = 1;
      spyOn(app.canvasEl, 'getBoundingClientRect').and.callThrough();
      app.drawOnCanvas(prevEvent, currentEvent);
      expect(app.canvasEl.getBoundingClientRect).toHaveBeenCalled();
    }));

    it('clear() should update value, isSaved and call canvasEl.getContext', async(() => {
      app.canvasEl = { getContext: a => {
          return { clearRect: (a, b, c, d) => {} };
        } };
      spyOn(app.canvasEl, 'getContext').and.callThrough();
      app.clear();
      expect(app.value).toEqual('');
      expect(app.isSaved).toBeFalsy();
      expect(app.canvasEl.getContext).toHaveBeenCalled();
    }));

    it('captureType should update from element.config.uiStyles.attributes.captureType', async(() => {
      app.element = { config: { uiStyles: { attributes: { captureType: 'test' } } } };
      expect(app.captureType).toEqual('test');
    }));

    it('setIsCapturing() should update isCapturing and call logger.debug()', async(() => {
      app.isEditable = () => {
        return true;
      };
      spyOn(logger, 'debug').and.callThrough();
      app.setIsCapturing(true);
      expect(app.isCapturing).toBeTruthy();
      expect(logger.debug).toHaveBeenCalled();
    }));

    it('isEditable should be true', async(() => {
      app.disabled = false;
      app.isSaved = false;
      expect(app.isEditable).toBeTruthy();
    }));

    it('registerCaptureEvents() should call registerCaptureOnEvent() with mousedown, mouseup', async(() => {
      app.element = { config: { uiStyles: { attributes: { captureType: 'DEFAULT' } } } };
      app.registerCaptureOnEvent = (a, b) => {};
      spyOn(app, 'registerCaptureOnEvent').and.callThrough();
      app.registerCaptureEvents();
      expect(app.registerCaptureOnEvent).toHaveBeenCalled();
      expect(app.registerCaptureOnEvent).toHaveBeenCalledWith('mousedown', 'mouseup');
    }));

    it('registerCaptureEvents() should call registerCaptureOnEvent() with click, click', async(() => {
      app.element = { config: { uiStyles: { attributes: { captureType: 'ON_CLICK' } } } };
      app.registerCaptureOnEvent = (a, b) => {};
      spyOn(app, 'registerCaptureOnEvent').and.callThrough();
      app.registerCaptureEvents();
      expect(app.registerCaptureOnEvent).toHaveBeenCalled();
      expect(app.registerCaptureOnEvent).toHaveBeenCalledWith('click', 'click');
    }));

});