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
import { RouterTestingModule } from '@angular/router/testing'
import { AngularSvgIconModule } from 'angular-svg-icon';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { AppComponent } from './app.component';
import { LoaderService } from './services/loader.service';
import { ServiceConstants } from './services/service.constants';
import { SvgComponent } from './components/platform/svg/svg.component';
import { setup, TestContext } from './setup.spec';
import { configureTestSuite } from 'ng-bullet';
import { Subscription } from 'rxjs';

class MockServiceConstant {
  STOPGAP_APP_HOST: string;
  STOPGAP_APP_PORT: any;
  LOCALE_LANGUAGE: string;
  STOPGAP_APP_PROTOCOL: string;
}

@Component({
  template: '<div></div>',
  selector: 'app-loader'
})
export class LoaderComponent {
  show = false;
  private subscription: Subscription;
}

const declarations = [
  AppComponent,
  LoaderComponent,
  SvgComponent
];
const imports = [ RouterTestingModule, AngularSvgIconModule ];
const providers = [ LoaderService, { provide: ServiceConstants, useClass: MockServiceConstant} ];

let fixture, hostComponent;

describe('AppComponent', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    hostComponent = fixture.debugElement.componentInstance;
  });

  it('should create the AppComponent', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

});
