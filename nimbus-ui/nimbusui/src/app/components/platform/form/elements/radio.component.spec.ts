'use strict';
import { TestBed, async } from '@angular/core/testing';
import { RadioButtonModule } from 'primeng/primeng';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';

import { RadioButton } from './radio.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';

describe('RadioButton', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          RadioButton,
          TooltipComponent
       ],
       imports: [
           RadioButtonModule,
           FormsModule,
           HttpModule,
           HttpClientModule,
           StorageServiceModule
       ],
       providers: [
           { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
           SessionStoreService,
           PageService,
           CustomHttpClient,
           LoaderService,
           ConfigService,
           LoggerService
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(RadioButton);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});