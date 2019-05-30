/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

import { Component, Input } from '@angular/core';
import { Subscription } from 'rxjs';
import { Param } from '../../shared/param-state';
import { ParamUtils } from '../../shared/param-utils';
import { ValidationConstraint } from './../../shared/validationconstraints.enum';
import { ValidationUtils } from './validators/ValidationUtils';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-base-element',
  template: ``
})
/**
 * \@author Dinakar.Meda
 * \@whatItDoes A Base class for params. Example - Page, Tile, Section, Form, FormElements etc.
 *
 * \@howToUse
 *
 * Every UI Component will extend this base class. This class provides the accessors for generic properties
 * and behaviors for all params
 */
export class BaseElement {
  @Input() element: Param;
  protected _nestedParams: Param[];
  protected _imgSrc: string;
  protected _cols: string;
  protected _code: string;
  protected _visible: any;
  protected _enabled: any;
  protected _cssClass: string;
  protected _type: string;
  protected _elementStyle: string;
  public requiredCss: boolean = false;
  labelSize: String;
  @Input() position: number;

  subscribers: Subscription[] = [];

  /**
   * Initialization activities this Param
   */
  ngOnInit() {
    this.labelSize = this.determineLabelSize(this.position);
    this.requiredCss = ValidationUtils.applyelementStyle(this.element);
  }

  /**
   * Nested Params if any within this Param
   */
  public get nestedParams(): Param[] {
    if (this.element.type && this.element.type.model) {
      return this.element.type.model.params;
    }
    return undefined;
  }

  /**
   * Image source (if any) that goes with this Param
   */
  public get imgSrc(): string {
    return this.element.config.uiStyles.attributes.imgSrc;
  }

  /**
   * The cols attribute for this param
   */
  public get cols(): string {
    return this.element.config.uiStyles.attributes.cols;
  }

  /**
   * Unique code for this Param
   */
  public get code(): string {
    return this.element.config.code;
  }

  /**
   *  Get visibility of this Param. Visible by default
   */
  public get visible(): boolean {
    return this.element.visible;
  }

  /**
   * Get enabled property of this Param. Enabled by default.
   */
  public get enabled(): boolean {
    return this.element.enabled;
  }

  /**
   * Set visibility of this Param.
   */
  public set visible(visible: boolean) {
    this._visible = visible;
  }

  /**
   * Set enabled property of this Param.
   */
  public set enabled(enabled: boolean) {
    this._enabled = enabled;
  }

  /**
   * Custom css for this Param
   */
  public get cssClass(): string {
    return this.element.config.uiStyles.attributes.cssClass;
  }

  /**
   * Type definition for the Param
   */
  public get type(): string {
    return this.element.config.uiStyles.attributes.type;
  }

  /**
   * Get the tooltip help text for this element.
   */
  public get helpText(): string {
    return ParamUtils.getHelpText(this.element);
  }

  /**
   * Get the label text for this element.
   */
  public get label(): string {
    return ParamUtils.getLabelText(this.element);
  }

  /**
   * Determine if the label for this element is empty or not.
   */
  public get isLabelEmpty(): boolean {
    if (this.label) {
      return this.label.trim().length === 0;
    }
    return true;
  }

  /**
   * Check if control is required
   */
  public get elementStyle(): string {
    let style = '';
    if (this.element.config.validation) {
      this.element.config.validation.constraints.forEach(validator => {
        if (validator.name === ValidationConstraint._notNull.value) {
          style = 'required';
        }
      });
    }
    return style;
  }

  isDate(dataType: string): boolean {
    return ParamUtils.isKnownDateType(dataType);
  }

  public get placeholder(): string {
    if (
      this.element &&
      this.element.config &&
      this.element.config.uiStyles &&
      this.element.config.uiStyles.attributes
    ) {
      return this.element.config.uiStyles.attributes.placeholder;
    }
    return undefined;
  }

  updatePosition() {
    if (this.isLabelEmpty) {
      this.position--;
    }
  }

  determineLabelSize(position) {
    if (!position) {
      return undefined;
    }
    if (position > 6) {
      return 'H6';
    }
    return 'H' + position;
  }

  ngOnDestroy() {
    if (this.subscribers && this.subscribers.length > 0) {
      this.subscribers.forEach(s => s.unsubscribe());
    }
    if (this.element.message) {
      this.element.message = this.element.message.filter(m => !m.transient);
    }
    delete this.element;
  }
}
