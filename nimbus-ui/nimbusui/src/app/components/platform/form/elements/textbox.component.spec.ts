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
import { HttpModule } from '@angular/http';
import { InputLabel } from './input-label.component';
import { TooltipComponent } from './../../tooltip/tooltip.component';
import { TestBed, async, fakeAsync, tick } from '@angular/core/testing';
import { FormsModule, Validators } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';
import { FormGroup, ValidatorFn, FormControl } from '@angular/forms';
import { Param } from '../../../../shared/param-state';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { setup, TestContext } from './../../../../setup.spec';
import { InputText } from './textbox.component';
import { StorageServiceModule } from 'angular-webstorage-service';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

let  param: Param, payload, controlService;

describe('InputText', () => {
  payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';
  param = JSON.parse(payload);

  setup();

  beforeEach(function(this: TestContext<InputText>){
    let declarations = [InputText, TooltipComponent, InputLabel];
    let imports =  [ FormsModule, HttpClientTestingModule, HttpModule, StorageServiceModule ];
    this.create(InputText, declarations, imports);
    let fg= new FormGroup({});
    let checks: ValidatorFn[] = [];
    //controlService = TestBed.get(ControlSubscribers);
    checks.push(Validators.required);
    fg.addControl(param.config.code, new FormControl(param.leafState,checks));
    this.hostComponent.form = fg;
    this.hostComponent.element = param;
  });

  it('form control value with default leafstate', async function(this: TestContext<InputText>){
    this.fixture.detectChanges();
    expect(this.hostComponent.form.controls['firstName'].value).toBe('testData');
    expect(this.hostComponent).toBeTruthy();
  
  });

  it('control validity',async function(this: TestContext<InputText>){
    this.hostComponent.form.controls['firstName'].setValue('');
    this.fixture.detectChanges();
    expect(this.hostComponent.form.controls['firstName'].valid).toBeFalsy();
   
  });

  it('post on focus out', async function(this: TestContext<InputText>) {
    this.fixture.detectChanges();
    spyOn(this.hostComponent, 'emitValueChangedEvent').and.callThrough();
    const textBox = this.fixture.debugElement.children[0].nativeElement;
    textBox.value = 'abcd123';
    textBox.dispatchEvent(new Event('input'));
    textBox.dispatchEvent(new Event('focusout'));
    this.fixture.detectChanges();
    expect(this.hostComponent.emitValueChangedEvent).toHaveBeenCalled();   
  });
  
});