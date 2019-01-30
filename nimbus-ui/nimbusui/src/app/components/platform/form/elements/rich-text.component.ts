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
import { NgModel, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Component, ViewChild, forwardRef, ChangeDetectorRef } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { BaseControl } from './base-control.component';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';
import { UiNature } from './../../../../shared/param-config';
import { ParamUtils } from './../../../../shared/param-utils';
import { StringUtils } from './../../../../shared/string-utils';
import { KeyValuePair } from './../../../../model/key-value-pair.model';
import { ViewConfig } from './../../../../shared/param-annotations.enum';

declare var Quill: any;

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RichText),
    multi: true
};

/**
 * \@author Tony Lopez
 * \@whatItDoes RichText is rich text editor component based on Quill.
 * \@howToUse <nm-input-rich-text></nm-input-rich-text>
 * 
 */
@Component({
    selector: 'nm-input-rich-text',
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR, WebContentSvc, ControlSubscribers],
    template: `
        <nm-input-label
            [element]="element" 
            [for]="element?.config?.code"
            [required]="requiredCss">
        </nm-input-label>

        <p-editor
            class="form-control"
            [(ngModel)]="value"
            [id]="element?.config?.code" 
            (focusout)="emitValueChangedEvent(this, value)"
            [placeholder]="element?.config?.uiStyles?.attributes?.placeholder"
            [disabled]="!element?.enabled"
            [readonly]="!element?.enabled || element?.config?.uiStyles?.attributes?.readOnly"
            [attr.readonly]="element?.config?.uiStyles?.attributes?.readOnly ? true : null"
            [styleClass]="element?.config?.uiStyles?.attributes?.cssClass"
            [formats]="element?.config?.uiStyles?.attributes?.formats"
            >
            
            <p-header [hidden]="element?.config?.uiStyles?.attributes?.readOnly">
                <ng-template ngFor let-toolbarFeature [ngForOf]="element?.config?.uiStyles?.attributes?.toolbarFeatures">

                    <ng-template [ngIf]="toolbarFeature === 'HEADER'">
                        <select class="ql-header" title="Text Heading">
                            <option selected>Normal</option>
                            <option *ngFor="let heading of headings" value="{{heading.value}}">{{heading.key}}</option>
                        </select>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'FONT'">
                        <select class="ql-font" style="Text Font">
                            <option selected>Sans Serif</option>
                            <option *ngFor="let font of fonts" value="{{font.value}}">{{font.key}}</option>
                        </select>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'BOLD'">
                        <button class="ql-bold" title="Bold"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'ITALIC'">
                        <button class="ql-italic" title="Italic"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'UNDERLINE'">
                        <button class="ql-underline" title="Underline"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'STRIKE'">
                        <button class="ql-strike" title="Strikethrough"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'COLOR'">
                        <select class="ql-color" title="Text Color"></select>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'BACKGROUND'">
                        <select class="ql-background" title="Background Color"></select>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'SCRIPT'">
                        <button class="ql-script" value="sub" title="Subscript"></button>
                        <button class="ql-script" value="super" title="Superscript"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'SIZE'">
                        <button class="ql-header" value="1" title="Heading Size 1"></button>
                        <button class="ql-header" value="2" title="Heading Size 2"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'BLOCKQUOTE'">
                        <button class="ql-blockquote" title="Insert Blockquote"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'CODE_BLOCK'">
                        <button class="ql-code-block" title="Insert Code Block"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'LIST'">
                        <button class="ql-list" value="ordered" title="Ordered List"></button>
                        <button class="ql-list" value="bullet" title="Unordered List"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'INDENT'">
                        <button class="ql-indent" value="-1" title="Unindent"></button>
                        <button class="ql-indent" value="+1" title="Indent"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'DIRECTION'">
                        <button class="ql-direction" value="rtl" title="Direction" type="button"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'ALIGN'">
                        <select class="ql-align" title="Align">
                            <option selected></option>
                            <option value="center"></option>
                            <option value="right"></option>
                            <option value="justify"></option>
                        </select>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'LINK'">
                        <button class="ql-link" title="Insert Link"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'IMAGE'">
                        <button class="ql-image" title="Insert Image"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'VIDEO'">
                        <button class="ql-video" title="Insert Video"></button>
                    </ng-template>

                    <ng-template [ngIf]="toolbarFeature === 'CLEAN'">
                        <button class="ql-clean" title="Remove Styles"></button>
                    </ng-template>
                    
                </ng-template>
            </p-header>
            
        </p-editor>
   `
})
export class RichText extends BaseControl<String> {

    @ViewChild(NgModel) model: NgModel;
    
    public fonts: KeyValuePair[] = [
        { key: "Serif", value: "serif" },
        { key: "Monospace", value: "monospace" }
    ];

    public headings: KeyValuePair[] = [
        { key: "Heading", value: "1" },
        { key: "Subheading", value: "2" }
    ];

    constructor(wcs: WebContentSvc, controlService: ControlSubscribers, cd: ChangeDetectorRef) {
        super(controlService, wcs, cd);
    }

    ngOnInit() {
        super.ngOnInit();
        if (this.element.config.uiStyles.attributes.readOnly) {
            return;
        }

        let fontConfig = this.buildConfigFromNature(ViewConfig.fonts.toString());
        if (fontConfig) {
            this.fonts = fontConfig;
        }

        var Font = Quill.import('formats/font');
        Font.whitelist = [];
        for(let font of this.fonts) {
            Font.whitelist.push(font.value);
        }
        Quill.register(Font, true);

        let headingConfig = this.buildConfigFromNature(ViewConfig.headings.toString());
        if (headingConfig) {
            this.headings = headingConfig;
        }
    }

    private buildConfigFromNature(uiNatureName: string): KeyValuePair[] {
        let uiNature: UiNature = ParamUtils.getUiNature(this.element, uiNatureName);
        if (!uiNature || !uiNature.attributes || !uiNature.attributes.value) {
            return undefined;
        }
        
        let config: KeyValuePair[] = [];
        for(let value of uiNature.attributes.value) {
            let item: KeyValuePair;
            if (typeof value === 'string') {
                item = {
                    key: value,
                    value: StringUtils.toKebabCase(value)
                }
            } else {
                item = value;
            }
            
            if (!config.find(c => c.key === item.key)) {
                config.push(item);
            }
        }
        return config;
    }
}