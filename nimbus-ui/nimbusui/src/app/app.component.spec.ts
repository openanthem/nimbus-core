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

import { AppComponent } from './app.component';
import { LoaderComponent } from './components/platform/loader/loader.component';
import { LoaderService } from './services/loader.service';
import { ServiceConstants } from './services/service.constants';

class MockServiceConstant {
  STOPGAP_APP_HOST: string;
  STOPGAP_APP_PORT: any;
  LOCALE_LANGUAGE: string;
  STOPGAP_APP_PROTOCOL: string;
}

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        LoaderComponent
      ],
      imports: [ RouterTestingModule ],
      providers: [ LoaderService, { provide: ServiceConstants, useClass: MockServiceConstant} ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it(`ngoninit() should get values from document`, async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    app.document = {
      location: {
        hostname: 'testing',
        port: '999',
        protocol: 'testProtocol',
        pathname: 'test/test'
      }
    };
    app.ngOnInit();
    expect(app.domain).toEqual('testing');
    expect(app.port).toEqual('999');
    expect(app.protocol).toEqual('testProtocol');
    expect(app.locale).toEqual('en-US');
    expect(ServiceConstants.STOPGAP_APP_HOST).toEqual('testing');
    expect(ServiceConstants.STOPGAP_APP_PORT).toEqual('999');
    expect(ServiceConstants.LOCALE_LANGUAGE).toEqual('en-US');
    expect(ServiceConstants.STOPGAP_APP_PROTOCOL).toEqual('testProtocol');
  }));
});
