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

let fixture, hostComponent;

describe('Image', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(Image);
    hostComponent = fixture.debugElement.componentInstance;
  });

  it('should create the Image',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('ngOnInit() should update the imagesPath',  async(() => {
    hostComponent.ngOnInit();
    expect(hostComponent.imagesPath).not.toEqual(null);
  }));

});
