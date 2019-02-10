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
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { StorageServiceModule } from 'angular-webstorage-service';
import { configureTestSuite } from 'ng-bullet';
import { setup } from '../../../../setup.spec';
import { RichText } from './rich-text.component';
import { EditorModule } from 'primeng/editor';
import { By } from '@angular/platform-browser';
import { MockRichText } from './../../../../mockdata/rich-text.component.mockdata.spec';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

let fixture, hostComponent, controlService;
const declarations = [ RichText, TooltipComponent, InputLabel ];
const imports =  [ FormsModule, HttpClientTestingModule, HttpModule, StorageServiceModule, EditorModule ];

describe('RichText', () => {
    configureTestSuite(() => {
        setup(declarations, imports);
    });
  
    beforeEach(() => {
        fixture = TestBed.createComponent(RichText);
        hostComponent = fixture.debugElement.componentInstance;
        this.hostElement = fixture.nativeElement;
        hostComponent.element = MockRichText;
    });
  
    it('should create the RichText component', async() =>{
        expect(hostComponent).toBeTruthy();
    });

    it('should render the toolbar features when configured as a toolbarFeature', async(() => {
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-align'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-background'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-blockquote'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-bold'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-clean'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-code-block'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-color'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-direction'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-font'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-header'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-image'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-indent[value="-1"]'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-indent[value="+1"]'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-italic'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-link'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-list[value="ordered"]'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-list[value="bullet"]'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-script[value="sub"]'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-script[value="super"]'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-header[value="1"]'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-header[value="2"]'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-strike'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-underline'))).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-video'))).toBeTruthy();
    }));

    it('should not render the toolbar features when not configured as a toolbarFeature', async(() => {
        hostComponent.element.config.uiStyles.attributes.toolbarFeatures = [];
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-align'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-background'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-blockquote'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-bold'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-clean'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-code-block'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-color'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-direction'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-font'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-header'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-image'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-indent[value="-1"]'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-indent[value="+1"]'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-italic'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-link'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-list[value="ordered"]'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-list[value="bullet"]'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-script[value="sub"]'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-script[value="super"]'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-header[value="1"]'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-header[value="2"]'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-strike'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-underline'))).toBeNull();
        expect(fixture.debugElement.query(By.css('.ui-editor-toolbar .ql-video'))).toBeNull();
    }));
});