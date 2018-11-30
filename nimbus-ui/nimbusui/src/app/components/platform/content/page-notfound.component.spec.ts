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
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';

const declarations = [
  PageNotfoundComponent
 ];
 const imports = [
  StorageServiceModule,
  HttpModule,
  HttpClientTestingModule
 ];
 const providers = [
  { provide: 'JSNLOG', useValue: JL },
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  LoggerService,
  AppInitService
 ];
 let fixture, hostComponent;
describe('PageNotfoundComponent', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(PageNotfoundComponent);
    hostComponent = fixture.debugElement.componentInstance;
  });

  it('should create the PageNotfoundComponent',  async(() => {
  }));

});