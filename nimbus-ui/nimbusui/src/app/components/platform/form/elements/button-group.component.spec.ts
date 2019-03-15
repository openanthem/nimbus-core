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
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { ButtonGroup } from './button-group.component';
import { SvgComponent } from '../../svg/svg.component';
import { Image } from '../../image.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { FormControl, FormGroup } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { buttonGroupbuttonList } from 'mockdata';

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
    hostComponent.buttonList = buttonGroupbuttonList;
    hostComponent.cssClass = 'text-sm-center';
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
      txt1: new FormControl()
   });
  });

  it('should create the ButtonGroup', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('nm-button should be created if @Button is configured', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('nm-button'));
    expect(buttonEle).toBeTruthy();
  }));

  it('On updating the param the button need to be added', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allButtonEle = debugElement.queryAll(By.css('nm-button'));
    expect(allButtonEle.length).toEqual(1);
    hostComponent.buttonList.push(buttonGroupbuttonList[0]);
    fixture.detectChanges();
    const updatedAllButtonEle = debugElement.queryAll(By.css('nm-button'));
    expect(updatedAllButtonEle.length).toEqual(2);
  }));

  it('nm-button should not be created if @Button is nmot configured', async(() => {
    hostComponent.buttonList[0].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('nm-button'));
    expect(buttonEle).toBeFalsy();
  }));

  it('nm-button should not be created if @Button is not configured', async(() => {
    hostComponent.buttonList[0].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('nm-button'));
    expect(buttonEle).toBeFalsy();
  }));

});

