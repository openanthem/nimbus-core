'use strict';
import { TestBed, async } from '@angular/core/testing';
import { CalendarModule } from 'primeng/primeng';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLegend } from './input-legend.component';
import { WebContentSvc } from '../../../../services/content-management.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { Param } from '../../../../shared/param-state';
import { ServiceConstants } from '../../../../services/service.constants';
import { By } from '@angular/platform-browser';
import { WindowRefService } from '../../../../services/window-ref.service';
import { inputLegendElement } from 'mockdata';

const declarations = [
    InputLegend,
  TooltipComponent
 ];
 const imports = [
  CalendarModule,
  FormsModule,
  HttpModule,
  HttpClientModule,
  StorageServiceModule
 ];
 const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  Location,
  PageService,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  LoggerService,
  SessionStoreService,
  AppInitService,
  WebContentSvc,
  WindowRefService
 ];

 let fixture, hostComponent;

describe('InputLegend', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(InputLegend);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = inputLegendElement as Param;
  });

    it('should create the InputLegend', async(() => {
        expect(hostComponent).toBeTruthy();
    }));

    it('legend should be created', async(() => {
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const legendEle = debugElement.query(By.css('legend'));
        expect(legendEle).toBeTruthy();
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

    it('label should not be created if the label is not configured', async(() => {
        ServiceConstants.LOCALE_LANGUAGE = 'en-US';
        hostComponent.element.labels = [];
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const labelEle = debugElement.query(By.css('label'));
        expect(labelEle).toBeFalsy();
    }));

});

