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
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

import { Paragraph } from './paragraph.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import { Param } from '../../../shared/param-state';
import { fieldValueParam } from 'mockdata';

let param: Param;

class MockDomSanitizer {
    bypassSecurityTrustHtml(a:any) {
        return 'test';
    }
}

const declarations = [
  Paragraph
 ];
 const imports = [
  HttpClientModule,
  HttpModule
 ];
 const providers = [
     { provide: DomSanitizer, useClass: MockDomSanitizer }
 ];

 let fixture, hostComponent;
describe('Paragraph', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Paragraph);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
  });

  it('should create the Paragraph', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('get htmlContent() should get content DomSanitizer.bypassSecurityTrustHtml()', async(() => {
    expect(hostComponent.htmlContent).toEqual('test');
  }));

});