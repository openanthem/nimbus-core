'use strict';
import { TestBed, async } from '@angular/core/testing';
import { AngularSvgIconModule } from 'angular-svg-icon';

import { ButtonGroup } from './button-group.component';
import { Button } from './button.component';
import { SvgComponent } from '../../svg/svg.component';
import { Image } from '../../image.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';

const declarations = [
  ButtonGroup,
  Button,
  SvgComponent,
  Image
 ];
 const imports = [
   AngularSvgIconModule
 ];
 const providers = []
 let fixture, hostComponent;
describe('ButtonGroup', () => {

  configureTestSuite();
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(ButtonGroup);
    hostComponent = fixture.debugElement.componentInstance;
  });

  it('should create the ButtonGroup', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

});