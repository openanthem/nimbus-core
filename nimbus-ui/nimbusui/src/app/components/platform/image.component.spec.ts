'use strict';
import { TestBed, async } from '@angular/core/testing';
import { AngularSvgIconModule } from 'angular-svg-icon';

import { Image } from './image.component';
import { ServiceConstants } from './../../services/service.constants';
import { SvgComponent } from './svg/svg.component';
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite } from 'ng-bullet';

const declarations = [
  Image,
  SvgComponent
 ];
const imports = [  AngularSvgIconModule ];
const providers = [];

describe('Image', () => {

  configureTestSuite();
  setup(Image, declarations, imports, providers);

  beforeEach(async function(this: TestContext<Image>){
  });

  it('should create the Image', function(this: TestContext<Image>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('ngOnInit() should update the imagesPath', function(this: TestContext<Image>) {
    this.hostComponent.ngOnInit();
    expect(this.hostComponent.imagesPath).not.toEqual(null);
  });

});
