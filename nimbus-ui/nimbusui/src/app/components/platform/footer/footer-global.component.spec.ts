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
import { AngularSvgIconModule } from 'angular-svg-icon';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { FooterGlobal } from './footer-global.component';
import { Paragraph } from '../content/paragraph.component';
import { Link } from '../link.component';
import { SvgComponent } from '../svg/svg.component';
import { Header } from '../content/header.component';
import { InputText } from '../form/elements/textbox.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { InputLabel } from '../form/elements/input-label.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';

const declarations = [
  FooterGlobal,
  Link,
  Paragraph,
  SvgComponent,
  Header,
  InputText,
  TooltipComponent,
  InputLabel
  ];
  const imports = [
    AngularSvgIconModule,
    HttpModule,
    HttpClientModule,
    ReactiveFormsModule, FormsModule
  ];
const providers = [];
let fixture, hostComponent;

describe('FooterGlobal', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(FooterGlobal);
    hostComponent = fixture.debugElement.componentInstance;
  });

  it('should create the FooterGlobal', async(() => {
      expect(hostComponent).toBeTruthy();
  }));

});
