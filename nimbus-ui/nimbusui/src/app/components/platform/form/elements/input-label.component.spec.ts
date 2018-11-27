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
import { InputLabel } from './input-label.component';
import { WebContentSvc } from '../../../../services/content-management.service';

let fixture, app;

describe('InputLabel', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        InputLabel,
        TooltipComponent,
        InputLabel
       ],
       imports: [
        CalendarModule,
        FormsModule,
        HttpModule,
        HttpClientModule,
        StorageServiceModule
       ],
       providers: [
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
        WebContentSvc
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(InputLabel);
    app = fixture.debugElement.componentInstance;
  }));

    it('should create the app', async(() => {
      expect(app).toBeTruthy();
    }));

    it('app.getCssClass() should return required', async(() => {
        app.required = true;        
        expect(app.getCssClass()).toEqual('required');
      }));

});