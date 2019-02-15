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