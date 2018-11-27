'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { Label } from './label.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { WebContentSvc } from '../../../services/content-management.service';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { AppInitService } from '../../../services/app.init.service';

let fixture, app;

describe('Header', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          Label,
          TooltipComponent
       ],
       imports: [
           HttpClientModule,
            HttpModule,
            StorageServiceModule
       ],
       providers: [
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        { provide: 'JSNLOG', useValue: JL },
        { provide: LocationStrategy, useClass: HashLocationStrategy },
        WebContentSvc,
        PageService,
        CustomHttpClient,
        SessionStoreService,
        LoaderService,
        ConfigService,
        LoggerService,
        AppInitService,
        Location
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(Label);
    app = fixture.debugElement.componentInstance;
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('app.cssClass should return labelClass', async(() => {
      app.labelClass = 'test';
      app.getCssClass = () => { return 'a' }
    expect(app.cssClass).toEqual('test');
  }));

});