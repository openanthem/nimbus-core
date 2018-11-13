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

describe('ButtonGroup', () => {

  configureTestSuite();
  setup(ButtonGroup, declarations, imports, providers);

  beforeEach(async function(this: TestContext<ButtonGroup>){
  });

  it('should create the ButtonGroup', async function (this: TestContext<ButtonGroup>) {
    expect(this.hostComponent).toBeTruthy();
  });

});