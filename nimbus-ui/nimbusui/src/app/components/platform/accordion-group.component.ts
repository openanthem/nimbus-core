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
import { Component, Input, OnDestroy, ElementRef } from '@angular/core';
import { Accordion } from './accordion.component';
import { BaseElement } from './base-element.component';
import { WebContentSvc } from '../../services/content-management.service';
import { trigger,state,style,transition,animate,keyframes } from '@angular/animations';
import { Param } from './../../shared/param-state';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component( {
    selector: 'accordion-group',
    providers: [ WebContentSvc ],
    template: `
        <div id="{{title}}" class="panel {{panelClass}} {{state}} " [hidden]="!param?.visible" >
            <div class="panel-heading panel-title">
                <h2>
                  <button attr.aria-expanded="{{isOpen}}" (click)="toggleOpen($event)">{{label}}
                    <!--<span *ngIf="leftElement" class="leftElement">this is optional left</span>-->
                  </button>
              </h2>
              <!-- <span *ngIf="rightElement" class="rightElement">this is optional right</span> -->
            </div>
            <div class="panel-collapse" 
                [ngClass]="{'displayNone': isHidden}" 
                [@accordionAnimation]='state' 
                (@accordionAnimation.start)="animationStart($event)"
                (@accordionAnimation.done)="animationDone($event)"
                attr.aria-hidden="{{!isOpen}}">
                    <div class="panel-body">
                        <ng-content></ng-content>
                    </div>
            </div>
        </div>
   `
   ,
   animations: [
       trigger('accordionAnimation', [
           state('openPanel', style({
               height: '*'
           })),
           state('closedPanel', style({
               height: '0'
           })),
           transition('closedPanel => openPanel', [animate(250, style({minHeight: '1200px'})), animate(100)]),
           transition('openPanel => closedPanel', [animate(250, style({minHeight: '0px'})), animate(100)]),
        ]),
   ]
})

export class AccordionGroup extends BaseElement implements OnDestroy {
    @Input() param : Param;
    @Input() title: string;
    @Input() panelClass: String;
    @Input()
    isOpen: boolean = false;    
    isHidden: boolean = true;
    set state( value: string ) {
        this._state = value;
    }

    get state() {
        return this._state;
    }

    private _state: string = 'closedPanel';
    
    constructor( private accordion: Accordion, private _wcs: WebContentSvc, private elementRef: ElementRef ) {
        super(_wcs);
        this.accordion.addGroup( this );        
    }
    
    ngOnInit() {
        this.loadLabelConfig(this.param);
    }

    ngOnDestroy() {
        this.accordion.removeGroup( this );
    }
    toggleOpen( event: MouseEvent ): void {
        event.preventDefault();
        this.isOpen = !this.isOpen;
        if(this.state == 'openPanel'){
            this.state = 'closedPanel';
        }
        else{
            this.state = 'openPanel';
        }
    }
    animationStart($event) {
        this.isHidden = false;
        
    }
    animationDone($event) {
  
        if ( this._state =='openPanel') {
            this.accordion.closeOthers(this).then(success => {
                let selElem = this.elementRef.nativeElement.querySelector('#'+this.title);
                selElem.scrollIntoView();
            });
        }
        if(this._state =='closedPanel'){
            this.isHidden = true;
        }
    }
}
