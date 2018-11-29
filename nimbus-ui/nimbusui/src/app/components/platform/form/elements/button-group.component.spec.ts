'use strict';
import { TestBed, async } from '@angular/core/testing';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { ButtonGroup } from './button-group.component';
// import { Button } from './button.component';
import { SvgComponent } from '../../svg/svg.component';
import { Image } from '../../image.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';

@Component({
  template: '<div></div>',
  selector: 'nm-button'
})
class Button {

  @Input() element: any;
  @Input() payload: string;
  @Input() form: any;
  @Input() actionTray?: boolean;

  @Output() buttonClickEvent = new EventEmitter();

  @Output() elementChange = new EventEmitter();
  private imagesPath: string;
  private btnClass: string;
  private disabled: boolean;
  files: any;
  differ: any;
  componentTypes;
}

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