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

let app, fixture;

describe('Signature', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Signature,
        TooltipComponent
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
        Location,
        SessionStoreService,
        PageService,
        CustomHttpClient,
        LoaderService,
        ConfigService,
        KeyFilterModule,
        LoggerService,
        AppInitService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(Signature);
    app = fixture.debugElement.componentInstance;
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

    it('ngAfterViewInit() should update the imgElement and call initCanvasElement(), captureEvents()', async(() => {
      app.img = { nativeElement: 'test' };
      app.element = { enabled: true };
      spyOn(app, 'initCanvasElement').and.returnValue('');
      spyOn(app, 'captureEvents').and.returnValue('');
      app.ngAfterViewInit();
      expect(app.imgElement).toEqual('test');
      expect(app.initCanvasElement).toHaveBeenCalled();
      expect(app.captureEvents).toHaveBeenCalled();
    }));

    it('initCanvasElement() should update canvas.nativeElement.width and canvas.nativeElement.height', async(() => {
      app.canvas = { nativeElement: { width: '', height: '', getContext: () => {} } };
      spyOn(app.canvas.nativeElement, 'getContext').and.returnValue({
        lineWidth: '',
        lineCap: '',
        strokeStyle: ''
      });
      app.width = 100;
      app.height = 200;
      app.initCanvasElement();
      expect(app.canvas.nativeElement.width).toEqual(100);
      expect(app.canvas.nativeElement.height).toEqual(200);
    }));

  it("drawOnCanvas() should call cx.beginPath(), cx.moveTo(), cx.lineTo(), and cx.stroke()", async(() => {
    app.drawOnCanvas({ getBoundingClientRect: () => {
          return { left: 1, top: 1 };
        } }, { clientX: 10, clientY: 20 }, { clientX: 10, clientY: 20 });
    app.zoomFactor = 1;
    app.cx = { beginPath: () => {}, moveTo: () => {}, lineTo: () => {}, stroke: () => {} };
    spyOn(app.cx, "beginPath").and.callThrough();
    spyOn(app.cx, "moveTo").and.callThrough();
    spyOn(app.cx, "lineTo").and.callThrough();
    spyOn(app.cx, "stroke").and.callThrough();
    app.drawOnCanvas({ getBoundingClientRect: () => {
          return { left: 1, top: 1 };
        } }, { x: 10, y: 20 }, { x: 10, y: 20 });
    expect(app.cx.beginPath).toHaveBeenCalled();
    expect(app.cx.moveTo).toHaveBeenCalled();
    expect(app.cx.lineTo).toHaveBeenCalled();
    expect(app.cx.stroke).toHaveBeenCalled();
  }));

    it('onImgLoad() should call initCanvasElement(), captureEvents(), cx.clearRect(), cx.drawImage() and toggleSave()', async(() => {
      app.element = { enabled: true };
      app.cx = { clearRect: () => {}, drawImage: () => {} };
      app.imgElement = { src: '/t' };
      app.defaultEmptyImage = '';
      app.initCanvasElement = () => {};
      app.captureEvents = () => {};
      app.toggleSave = () => {};
      spyOn(app, 'initCanvasElement').and.callThrough();
      spyOn(app.cx, 'drawImage').and.callThrough();
      spyOn(app, 'toggleSave').and.callThrough();
      app.onImgLoad();
      expect(app.initCanvasElement).toHaveBeenCalled();
      expect(app.cx.drawImage).toHaveBeenCalled();
      expect(app.toggleSave).toHaveBeenCalled();
    }));

    it('clearSignature() should call toggleSave() and cx.clearRect', async(() => {
      app.imgElement = { src: '' };
      app.cx = { clearRect: () => {} };
      app.toggleSave = () => {};
      spyOn(app, 'toggleSave').and.callThrough();
      spyOn(app.cx, 'clearRect').and.callThrough();
      app.clearSignature();
      expect(app.toggleSave).toHaveBeenCalled();
      expect(app.cx.clearRect).toHaveBeenCalled();
    }));

    it('acceptSignature() should call clearSignature()', async(() => {
      app.canvasEl = { toDataURL: () => {
          return 'test';
        } };
      app.defaultEmptyImage = 'test';
      app.clearSignature = () => {};
      app.shrinkCanvas = () => {};
      spyOn(app, 'clearSignature').and.callThrough();
      app.acceptSignature();
      expect(app.clearSignature).toHaveBeenCalled();
    }));

    it('acceptSignature() should call toggleSave() only', async(() => {
      app.canvasEl = { toDataURL: () => {
          return 'test';
        } };
      app.defaultEmptyImage = '';
      app.imgElement = { src: '' };
      app.clearSignature = () => {};
      app.shrinkCanvas = () => {};
      spyOn(app, 'clearSignature').and.callThrough();
      spyOn(app, 'toggleSave').and.callThrough();
      app.acceptSignature();
      expect(app.clearSignature).not.toHaveBeenCalled();
      expect(app.toggleSave).toHaveBeenCalled();
    }));

    it('toggleSave() should update save property', async(() => {
      app.toggleSave(true);
      expect(app.save).toBeTruthy();
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

});