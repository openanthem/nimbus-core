'use strict';
import { TestBed, async } from '@angular/core/testing';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { PageNotfoundComponent } from './page-notfound.component';
import { LoggerService } from '../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { AppInitService } from '../../../services/app.init.service';

describe('PageNotfoundComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        PageNotfoundComponent
       ],
       imports: [
        StorageServiceModule,
        HttpModule,
        HttpClientTestingModule
       ],
       providers: [
        { provide: 'JSNLOG', useValue: JL },
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        LoggerService,
        AppInitService
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(PageNotfoundComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});