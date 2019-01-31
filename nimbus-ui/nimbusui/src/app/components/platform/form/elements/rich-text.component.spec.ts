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
import { ViewConfig } from './../../../../shared/param-annotations.enum';
import { UiNature } from './../../../../shared/param-config';

/**
 * \@author Tony Lopez
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
  
    it('should create the component', async() =>{
        expect(hostComponent).toBeTruthy();
    });

    it('should render all of the toolbar features', async(() => {
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

    it('should render the default fonts', async(() => {
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('select.ql-font option[selected]')).nativeElement.text).toBe('Sans Serif');
        expect(fixture.debugElement.query(By.css('select.ql-font option:nth-child(1)')).nativeElement.text).toBe('Sans Serif');
        expect(fixture.debugElement.query(By.css('select.ql-font option:nth-child(2)')).nativeElement.text).toBe('Serif');
        expect(fixture.debugElement.query(By.css('select.ql-font option:nth-child(3)')).nativeElement.text).toBe('Monospace');
    }));

    it('should render custom fonts', async(() => {
        hostComponent.element.config.uiNatures = [] as UiNature[];
        hostComponent.element.config.uiNatures.push({
            name: ViewConfig.fonts.toString(),
            attributes: { value: [ 'Arial' ] }
        });
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('select.ql-font option[selected]')).nativeElement.text).toBe('Sans Serif');
        expect(fixture.debugElement.query(By.css('select.ql-font option:nth-child(1)')).nativeElement.text).toBe('Sans Serif');
        expect(fixture.debugElement.query(By.css('select.ql-font option:nth-child(2)')).nativeElement.text).toBe('Arial');
    }));

    it('should render the default headings', async(() => {
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('select.ql-header option[selected]')).nativeElement.text).toBe('Normal');
        expect(fixture.debugElement.query(By.css('select.ql-header option:nth-child(1)')).nativeElement.text).toBe('Normal');
        expect(fixture.debugElement.query(By.css('select.ql-header option:nth-child(2)')).nativeElement.text).toBe('Heading');
        expect(fixture.debugElement.query(By.css('select.ql-header option:nth-child(2)')).nativeElement.value).toBe('1');
        expect(fixture.debugElement.query(By.css('select.ql-header option:nth-child(3)')).nativeElement.text).toBe('Subheading');
        expect(fixture.debugElement.query(By.css('select.ql-header option:nth-child(3)')).nativeElement.value).toBe('2');
    }));

    it('should render custom headings', async(() => {
        hostComponent.element.config.uiNatures = [] as UiNature[];
        hostComponent.element.config.uiNatures.push({
            name: ViewConfig.headings.toString(),
            attributes: { value: [ { key: 'Huge', value: '1' } ] }
        });
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('select.ql-header option[selected]')).nativeElement.text).toBe('Normal');
        expect(fixture.debugElement.query(By.css('select.ql-header option:nth-child(1)')).nativeElement.text).toBe('Normal');
        expect(fixture.debugElement.query(By.css('select.ql-header option:nth-child(2)')).nativeElement.text).toBe('Huge');
        expect(fixture.debugElement.query(By.css('select.ql-header option:nth-child(2)')).nativeElement.value).toBe('1');
    }));

    it('should not render duplicate configs from natures', async(() => {
        hostComponent.element.config.uiNatures = [] as UiNature[];
        hostComponent.element.config.uiNatures.push({
            name: ViewConfig.fonts.toString(),
            attributes: { value: [ 'Arial', 'Arial' ] }
        });
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('select.ql-font option[selected]')).nativeElement.text).toBe('Sans Serif');
        expect(fixture.debugElement.query(By.css('select.ql-font option:nth-child(1)')).nativeElement.text).toBe('Sans Serif');
        expect(fixture.debugElement.query(By.css('select.ql-font option:nth-child(2)')).nativeElement.text).toBe('Arial');
        expect(fixture.debugElement.query(By.css('select.ql-font option:nth-child(3)'))).toBeFalsy;
    }));

    it('should render the id', async(() => {
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('p-editor')).properties.id).toBe('richTextbox');
    }));

    it('should render the placeholder', async(() => {
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('p-editor')).componentInstance.placeholder).toBe('Please enter a value');
    }));

    it('should render the existing state', async(() => {
        let expected = MockRichText.leafState;
        fixture.detectChanges();
        fixture.whenStable().then(() => {
            expect(fixture.debugElement.query(By.css('p-editor')).componentInstance.value).toBe(expected);
        });
    }));

    it('should emit the value on focus out', async(() => {      
        spyOn(hostComponent, 'emitValueChangedEvent');
        fixture.whenStable().then(() => {
            const richText = fixture.debugElement.query(By.css('p-editor.form-control')).nativeElement;
            richText.dispatchEvent(new Event('focusout'));
            expect(hostComponent.emitValueChangedEvent).toHaveBeenCalled();
        });
    }));

    it('should passthrough the styleClass to PrimeNG', async(() => {
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('p-editor')).componentInstance.styleClass).toBe('nm-sample-style-class');
    }));

    it('should passthrough the formats to PrimeNG', async(() => {
        let expected = [ 'bold', 'underline', 'italic' ];
        hostComponent.element.config.uiStyles.attributes.formats = expected;
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('p-editor')).componentInstance.formats).toBe(expected);
    }));

    it('should render the richtext as readonly', async(() => {
        hostComponent.element.config.uiStyles.attributes.readOnly = true;
        fixture.detectChanges();
        expect(fixture.debugElement.query(By.css('p-editor')).componentInstance.readonly).toBeTruthy();
        expect(fixture.debugElement.query(By.css('p-editor')).attributes.readonly).toBeTruthy();
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