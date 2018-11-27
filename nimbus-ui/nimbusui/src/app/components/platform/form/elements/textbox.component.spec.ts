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
import { InputText } from './textbox.component';
import { StorageServiceModule } from 'angular-webstorage-service';
import { ControlSubscribers } from '../../../../services/control-subscribers.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext, instantiateComponent } from '../../../../setup.spec';
import * as data from '../../../../payload.json';
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

let  param: Param, controlService;
let fixture, hostComponent;
const declarations = [InputText, TooltipComponent, InputLabel];
const imports =  [ FormsModule, HttpClientTestingModule, HttpModule, StorageServiceModule ];

describe('InputText', () => {
    configureTestSuite(() => {
        setup(declarations, imports);
    });
       let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);
  
    beforeEach(async() => {
        fixture = TestBed.createComponent(InputText);
        hostComponent = fixture.debugElement.componentInstance;
        this.hostElement = fixture.nativeElement;
        const fg = new FormGroup({});
        const checks: ValidatorFn[] = [];
        checks.push(Validators.required);
        fg.addControl(param.config.code, new FormControl(param.leafState, checks));
        hostComponent.form = fg;
        hostComponent.element = param;
        controlService = TestBed.get(ControlSubscribers);
    });
  
    it('should create the InputText', async() =>{
        expect(hostComponent).toBeTruthy();
    });

    it('form control value with default leafstate', async() => {
        fixture.whenStable().then(() => {
            fixture.detectChanges();
            expect(hostComponent.form.controls['firstName'].value).toBe('testData');
            expect(hostComponent).toBeTruthy();
        });
    });

    it('control validity', async() =>{
        fixture.whenStable().then(() => {
            hostComponent.form.controls['firstName'].setValue('');
            fixture.detectChanges();
            expect(hostComponent.form.controls['firstName'].valid).toBeFalsy();
        });
    });

    it('post on focus out', async() =>{
        fixture.whenStable().then(() => {
            fixture.detectChanges();
            spyOn(hostComponent, 'emitValueChangedEvent').and.callThrough();
            const textBox = fixture.debugElement.children[0].nativeElement;
            textBox.value = 'abcd123';
            textBox.dispatchEvent(new Event('input'));
            textBox.dispatchEvent(new Event('focusout'));
            fixture.detectChanges();
            expect(hostComponent.emitValueChangedEvent).toHaveBeenCalled();
        });
    });
  
});