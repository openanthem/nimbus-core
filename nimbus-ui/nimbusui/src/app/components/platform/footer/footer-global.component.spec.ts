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
