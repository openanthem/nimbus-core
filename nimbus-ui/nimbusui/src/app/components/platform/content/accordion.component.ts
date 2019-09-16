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

import {
  Component,
  Input,
  SimpleChange,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import { FormGroup } from '@angular/forms';
import { PageService } from '../../../services/page.service';
import {
  ComponentTypes,
  ViewComponent
} from '../../../shared/param-annotations.enum';
import { Param } from '../../../shared/param-state';
import { BaseElement } from '../base-element.component';
import { GenericDomain } from './../../../model/generic-domain.model';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-accordion',
  template: `
    <div
      class="text-sm-right"
      *ngIf="element.config?.uiStyles?.attributes?.showExpandAll"
      [hidden]="!element?.visible"
    >
      <button type="button" class="btn btn-expand" (click)="openAll()">
        Expand All
      </button>
      <span class="btn-pipe">|</span>
      <button type="button" class="btn btn-expand" (click)="closeAll()">
        Collapse All
      </button>
    </div>
    <p-accordion
      #accordion
      [multiple]="multiple"
      [activeIndex]="index"
      *ngIf="element?.visible"
    >
      <ng-template ngFor let-tab [ngForOf]="nestedParams">
        <p-accordionTab
          id="{{ tab?.config?.code }}"
          [selected]="tab?.config?.uiStyles?.attributes?.selected"
          *ngIf="tab?.visible"
        >
          <p-header>
            <nm-label *ngIf="tab" [element]="tab" [size]="labelSize"></nm-label>

            <ng-template
              [ngIf]="tabDatum[tab.config.code]?.tabInfo"
              let-tabInfo
            >
              <span
                *ngIf="tabInfo.visible"
                [ngClass]="{
                  'nm-accordion-headertext': !tabInfo.config?.uiStyles
                    ?.attributes?.cssClass
                }"
              >
                {{
                  tabInfo.leafState
                    ? tabInfo.leafState
                    : tabInfo.config.uiStyles.attributes.info
                }}
              </span>
            </ng-template>

            <ng-template [ngIf]="tabDatum[tab.config.code]?.image" let-image>
              <nm-image
                *ngIf="image.visible"
                [name]="
                  image.visible && image.leafState
                    ? image.leafState
                    : image.config.uiStyles.attributes.imgSrc
                "
                [type]="image.config.uiStyles.attributes.type"
                [title]="image.config.uiStyles.attributes.title"
                [cssClass]="image.config.uiStyles.attributes.cssClass"
                class="nm-accordion-headerimage"
              >
              </nm-image>
            </ng-template>

            <nm-counter-message
              *ngIf="element.config?.uiStyles?.attributes?.showMessages"
              [element]="tab"
              [form]="form"
            ></nm-counter-message>
            <div style="clear: both"></div>
          </p-header>
          <div
            class="accordionBtn"
            *ngIf="tab?.config?.uiStyles?.attributes?.editable"
          >
            <button
              (click)="processOnClick(tab)"
              type="button"
              class="btn btn-plain"
            >
              <i class="fa fa-fw fa-pencil" aria-hidden="true"></i>Edit
            </button>
          </div>
          <!-- Form Elements -->
          <ng-template [ngIf]="form !== undefined">
            <ng-template ngFor let-frmElem [ngForOf]="tab.type?.model?.params">
              <nm-frm-grp
                [element]="frmElem"
                [form]="form"
                [ngClass]="getElementClass(elementCss, frmElem)"
                [position]="position + 1"
              >
              </nm-frm-grp>
            </ng-template>
          </ng-template>
          <ng-template [ngIf]="form === undefined">
            <ng-template
              ngFor
              let-tabElement
              [ngForOf]="tab?.type?.model?.params"
            >
              <!-- ButtonGroup -->
              <ng-template
                [ngIf]="
                  tabElement.alias == componentTypes.buttonGroup.toString()
                "
              >
                <div class="">
                  <nm-button-group
                    [buttonList]="tabElement.type?.model?.params"
                    class="{{
                      tabElement.config?.uiStyles?.attributes?.cssClass
                    }} buttonGroup"
                  >
                  </nm-button-group>
                </div>
              </ng-template>
              <!-- Link -->
              <ng-template
                [ngIf]="tabElement.alias == componentTypes.link.toString()"
              >
                <nm-link
                  id="{{ tabElement.config?.code }}"
                  [element]="tabElement"
                >
                </nm-link>
              </ng-template>
              <!-- Grid Param -->
              <ng-template
                [ngIf]="tabElement.alias == componentTypes.grid.toString()"
              >
                <nm-table
                  id="{{ tabElement.config?.code }}"
                  [element]="tabElement"
                  [params]="
                    tabElement?.config?.type?.elementConfig?.type?.model
                      ?.paramConfigs
                  "
                  (onScrollEvent)="onScrollEvent()"
                  [position]="position + 1"
                  [nmPrint]="tabElement"
                >
                </nm-table>
              </ng-template>
              <!-- Card Content -->
              <ng-template
                [ngIf]="
                  tabElement.alias == componentTypes.cardDetail.toString()
                "
              >
                <nm-card-details
                  id="{{ tabElement.config?.code }}"
                  [element]="tabElement"
                  [position]="position + 1"
                  [nmPrint]="tabElement"
                  [ngClass]="tabElement.config.uiStyles.attributes.cssClass"
                ></nm-card-details>
              </ng-template>
              <!-- Card Detaisl Grid -->
              <ng-template
                [ngIf]="
                  tabElement.alias == componentTypes.cardDetailsGrid.toString()
                "
              >
                <nm-card-details-grid
                  id="{{ tabElement.config?.code }}"
                  [position]="position + 1"
                  [element]="tabElement"
                  [nmPrint]="tabElement"
                ></nm-card-details-grid>
              </ng-template>
              <!-- Form Param -->
              <ng-template
                [ngIf]="tabElement.alias == viewComponent.form.toString()"
              >
                <nm-form
                  [position]="position + 1"
                  id="{{ tabElement.config?.code }}"
                  [element]="tabElement"
                  [model]="tabElement.type?.model"
                  [nmPrint]="tabElement"
                ></nm-form>
              </ng-template>
            </ng-template>
          </ng-template>
        </p-accordionTab>
      </ng-template>
    </p-accordion>
  `
})
export class Accordion extends BaseElement {
  @Input() form: FormGroup;
  @Input() elementCss: string;
  componentTypes = ComponentTypes;
  viewComponent = ViewComponent;
  protected _multiple: boolean;
  index: number[];
  @ViewChild('accordion') accordion: Accordion;

  tabDatum: { [id: string]: TabData } = {};

  constructor(private pageSvc: PageService) {
    super();
  }

  ngOnInit() {
    super.ngOnInit();
    //initialize any configs on accordion
    this.initializeCall(this.element);
    if(this.element.type && this.element.type.model && this.element.type.model.params) {
      //initialize any configs on accordion tabs
      this.element.type.model.params.forEach(p => {
        if(p.alias == ComponentTypes.accordiontab.toString()) {
          this.initializeCall(p);
        }
      });
    }
    this.buildTabDatum();
  }

  initializeCall(param:Param) {
    if (param.config && param.config.initializeComponent()) {
      this.pageSvc.processEvent(
        param.path,
        '$execute',
        new GenericDomain(),
        'POST'
      );
    }
  }
  /** Handling model changes to Accordions **/
  ngOnChanges(changes: SimpleChanges) {
    const model: SimpleChange = changes.model;
  }

  private buildTabDatum() {
    for (let tabParam of this.nestedParams) {
      // TODO Only add for @AccordionTab aliased elements
      this.tabDatum[tabParam.config.code] = new TabData().from(tabParam);
    }
  }

  /**
   * Expand Multiple Tabs?
   */
  public get multiple(): boolean {
    return this.element.config.uiStyles.attributes.multiple;
  }

  /**
   * Close All Tabs
   */
  public closeAll() {
    if (this.accordion['tabs']) {
      this.index = [];
      this.index.push(-1);
    }
  }

  /**
   * Open All Tabs
   */
  public openAll() {
    if (this.accordion['tabs']) {
      this.index = [];
      for (let t = 0; t < this.accordion['tabs'].length; t++) {
        this.index.push(t);
      }
    }
  }

  /**
   * Process configs on the click event
   */
  processOnClick(param: Param) {
    this.pageSvc.processEvent(param.path, '$execute', null, 'POST');
  }

  // TODO Remove and replace with efficient change
  getElementClass(parentCss: String, child: Param) {
    if (child.config.uiStyles) {
      if (
        child.config.uiStyles.attributes &&
        child.config.uiStyles.attributes.cssClass &&
        child.config.uiStyles.attributes.cssClass != ''
      ) {
        return child.config.uiStyles.attributes.cssClass;
      } else {
        if (parentCss) {
          return parentCss;
        } else {
          return '';
        }
      }
    } else {
      return '';
    }
  }
}

export class TabData {
  tabInfo: Param;
  image: Param;

  from(tabParam: Param): TabData {
    // find all the config within the tab params and assign the data to this instance
    for (let param of tabParam.type.model.params) {
      // collect only the first @TabInfo found
      if (!this.tabInfo && param.alias === ViewComponent.tabInfo.toString()) {
        this.tabInfo = param;
      }

      // collect only the first @Image found
      if (!this.image && param.alias === ViewComponent.image.toString()) {
        this.image = param;
      }

      // if we've found everything we need, return
      if (this.tabInfo && this.image) {
        return this;
      }
    }
    return this;
  }
}
