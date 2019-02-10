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
import { textBoxElement } from 'mockdata';
import { ServiceConstants } from '../../../../services/service.constants';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

let fixture, hostComponent, controlService;
const declarations = [InputText, TooltipComponent, InputLabel];
const imports =  [ FormsModule, HttpClientTestingModule, HttpModule, StorageServiceModule ];

describe('InputText', () => {
    configureTestSuite(() => {
        setup(declarations, imports);
    });
  
    beforeEach(() => {
        fixture = TestBed.createComponent(InputText);
        hostComponent = fixture.debugElement.componentInstance;
        this.hostElement = fixture.nativeElement;
        const fg = new FormGroup({});
        const checks: ValidatorFn[] = [];
        checks.push(Validators.required);
        fg.addControl(textBoxElement.config.code, new FormControl(textBoxElement.leafState, checks));
        hostComponent.form = fg;
        hostComponent.element = textBoxElement as Param;
        controlService = TestBed.get(ControlSubscribers);
    });
  
    it('should create the InputText', async () => {
        expect(hostComponent).toBeTruthy();
    });

    it('form control value with default leafstate', async () => {
        fixture.whenStable().then(() => {
            fixture.detectChanges();
            expect(hostComponent.form.controls['firstName'].value).toBe('testing textbox leafState');
            expect(hostComponent).toBeTruthy();
        });
    });

    it('control validity', async () => {
        fixture.whenStable().then(() => {
            hostComponent.form.controls['firstName'].setValue('');
            fixture.detectChanges();
            expect(hostComponent.form.controls['firstName'].valid).toBeFalsy();
        });
    });

    it('post on focus out', async () => {
        const debugElement = fixture.debugElement;
        fixture.whenStable().then(() => {
            fixture.detectChanges();
            spyOn(hostComponent, 'emitValueChangedEvent').and.callThrough();
            const textBox = debugElement.query(By.css('.form-control.text-input'));
            textBox.value = 'abcd123';
            textBox.dispatchEvent(new Event('input'));
            textBox.dispatchEvent(new Event('focusout'));
            fixture.detectChanges();
            expect(hostComponent.emitValueChangedEvent).toHaveBeenCalled();
        });
    });

    it('nm-input-label should be created if the label is configured', async(() => {
        ServiceConstants.LOCALE_LANGUAGE = 'en-US';
        fixture.detectChanges();
        const labelEle = document.getElementsByTagName('nm-input-label');
        expect(labelEle.length).toEqual(1);
    }));

    it('nm-input-label should not be created if the label is not configured', async(() => {
        ServiceConstants.LOCALE_LANGUAGE = 'en-US';
        hostComponent.element.labels = [];
        fixture.detectChanges();
        const labelEle = document.getElementsByTagName('nm-input-label');
        expect(labelEle.length).toEqual(0);
    }));

    it('input should be created if the hidden and readOnly is configured as false', async(() => {
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const textBox = debugElement.query(By.css('.form-control.text-input'));
        expect(textBox).toBeTruthy();
    }));

    it('input should not be created if the hidden and readOnly is configured as true', async(() => {
        hostComponent.element.config.uiStyles.attributes.hidden = true;
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const textBox = debugElement.query(By.css('.form-control.text-input'));
        expect(textBox).toBeFalsy();
    }));

    it('input should be created if the hidden is configured as true', async(() => {
        hostComponent.element.config.uiStyles.attributes.hidden = true;
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const textBox = debugElement.query(By.css('input'));
        expect(textBox).toBeTruthy();
    }));

    it('pre should be created if the hidden and readonly is configured as false and display value', async(() => {
        hostComponent.element.config.uiStyles.attributes.hidden = false;
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const preEle = debugElement.query(By.css('pre'));
        expect(preEle).toBeTruthy();
        expect(preEle.nativeElement.innerText).toEqual('testing textbox leafState');
    }));

    it('pre should be created if the hidden is false and readonly is configured as true', async(() => {
        hostComponent.element.config.uiStyles.attributes.hidden = false;
        hostComponent.element.config.uiStyles.attributes.readOnly = true;
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const preEle = debugElement.query(By.css('pre'));
        expect(preEle).toBeFalsy();
    }));

    it('p should be created if the if readOnly is configured as true and display the leafState', async(() => {
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const pEle = debugElement.query(By.css('p'));
        expect(pEle).toBeTruthy();
        expect(pEle.nativeElement.innerText).toEqual('testing textbox leafState');
    }));

    it('p should not be created if the if readOnly is configured as false', async(() => {
        hostComponent.element.config.uiStyles.attributes.readOnly = false;
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const pEle = debugElement.query(By.css('p'));
        expect(pEle).toBeFalsy();
    }));

});

