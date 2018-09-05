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
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Param } from '../../../../shared/param-state';
import { FormGroup } from '@angular/forms';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { ServiceConstants } from './../../../../services/service.constants';
import { BaseElement } from '../../base-element.component';
import { ComponentTypes } from '../../../../shared/param-annotations.enum';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-filter-button',
    providers: [WebContentSvc],
    template:`
        <button class="{{fbutton.config?.uiStyles?.attributes?.cssClass}}" (click)="emitEvent(this)" type="button">
            {{label}}
            <ng-template [ngIf]="fText?.config?.uiStyles?.attributes?.alias == componentTypes.textBox.toString()">
                <span class="badge badge-default">{{fText?.leafState}}</span>
            </ng-template>
        </button>
    `
})

export class FilterButton extends BaseElement{
   @Input() filterButton: Param;
   @Input() form: FormGroup;
   @Output() buttonClickEvent = new EventEmitter();
   public fbutton: Param;
   public fText: Param;
  // private filterCount: string;
   private imagesPath: string;
   componentTypes = ComponentTypes;

   constructor(private pageService: PageService, private _wcs: WebContentSvc) {
       super(_wcs);
   }

   ngOnInit() {
       this.imagesPath = ServiceConstants.IMAGES_URL;
       for(var p in this.filterButton.type.model.params) {
           let element = this.filterButton.type.model.params[p];
           if (element.config.uiStyles.attributes.alias === 'Button') {
               this.fbutton = element;
               this.loadLabelConfig(this.element);
           }
           if (element.config.uiStyles.attributes.alias === 'TextBox') {
               this.fText = element;
               //this.filterCount = element.leafState;
           }
       }
       this.buttonClickEvent.subscribe(( $event ) => {
           this.pageService.processEvent( $event.fbutton.path, $event.fbutton.config.uiStyles.attributes.b,
               null, $event.fbutton.config.uiStyles.attributes.method );
       } );
   }

   emitEvent( $event: any ) {
       this.buttonClickEvent.emit( $event );
   }

}
