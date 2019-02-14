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
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { LoggerService } from './../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../services/session.store';
import { LoginCmp } from './login.component';
import { AppInitService } from '../../services/app.init.service'
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../setup.spec';

class MockRouter {
    navigate() { }
  }


  class MockLoggerService {
    debug() { }
    info() { }
    error() { }
}

const declarations = [
    LoginCmp
 ];
 const imports = [
     ReactiveFormsModule,
     RouterTestingModule,
     StorageServiceModule,
     HttpClientModule,
     HttpModule
 ];
 const providers = [ 
     {provide: Router, useClass: MockRouter}, 
     { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
     { provide: 'JSNLOG', useValue: JL },
     {provide: LoggerService, useClass: MockLoggerService},
     AppInitService
  ];

let fixture, hostComponent;

describe('LoginCmp', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


    beforeEach(() => {
      fixture = TestBed.createComponent(LoginCmp);
      hostComponent = fixture.debugElement.componentInstance;
    });

    it('should create the LoginCmp',  async(() => {
      expect(hostComponent).toBeTruthy();
    }));

    it('onSubmit() should navigate to /h/admindashboard for admin',  async(() => {
      hostComponent.loginForm = { value: { username: 'admin' } };
      const _router = TestBed.get(Router);
      spyOn(_router, 'navigate');
      hostComponent.onSubmit();
      expect(_router.navigate).toHaveBeenCalledWith(['/h/admindashboard']);
    }));

    it('onSubmit() should navigate to /cs/a for supervisor',  async(() => {
      hostComponent.loginForm = { value: { username: 'supervisor' } };
      const _router = TestBed.get(Router);
      spyOn(_router, 'navigate');
      hostComponent.onSubmit();
      expect(_router.navigate).toHaveBeenCalledWith(['/cs/a']);
    }));

    it('onSubmit() should navigate to /pc/a for training',  async(() => {
      hostComponent.loginForm = { value: { username: 'training' } };
      const _router = TestBed.get(Router);
      spyOn(_router, 'navigate');
      hostComponent.onSubmit();
      expect(_router.navigate).toHaveBeenCalledWith(['/pc/a']);
    }));

    it('onSubmit() should navigate to /h/vrCSLandingPage for default',  async(() => {
      hostComponent.loginForm = { value: { username: '' } };
      const _router = TestBed.get(Router);
      spyOn(_router, 'navigate');
      hostComponent.onSubmit();
      expect(_router.navigate).toHaveBeenCalledWith(['/h/vrCSLandingPage']);
    }));

    it('onSubmit() should navigate to member for member',  async(() => {
      hostComponent.loginForm = { value: { username: 'member' } };
      const _router = TestBed.get(Router);
      spyOn(_router, 'navigate');
      hostComponent.onSubmit();
      expect(_router.navigate).toHaveBeenCalledWith(['/mem/a']);
    }));

    // it('LoginForm should be created',  async(() => {
    //   hostComponent.ngOnInit();
    //   expect(hostComponent.loginForm).toBeTruthy();
    // }));

});