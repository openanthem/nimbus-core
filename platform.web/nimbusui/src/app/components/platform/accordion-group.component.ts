/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Component, Input, OnDestroy, ElementRef } from '@angular/core';
import { Accordion } from './accordion.component';
import { WebContentSvc } from '../../services/content-management.service';
import { trigger,state,style,transition,animate,keyframes } from '@angular/animations';

@Component( {
    selector: 'accordion-group',
    providers: [ WebContentSvc ],
    template: `
        <div id="{{title}}" class="panel {{panelClass}} {{state}} ">
            <h2 class="panel-heading panel-title">
                  <button attr.aria-expanded="{{isOpen}}" (click)="toggleOpen($event)">{{label}}</button>
              </h2>
            <div class="panel-collapse" [@accordionAnimation]='state' (@accordionAnimation.done)="animationDone($event)" attr.aria-hidden="{{!isOpen}}">
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
               maxHeight: '10000px',
           })),
           state('closedPanel', style({
               maxHeight: '0',
           })),
           transition('closedPanel => openPanel', animate('600ms ease-in')),
           transition('openPanel => closedPanel', animate('200ms ease-out')),
        ]),
   ]
})

export class AccordionGroup implements OnDestroy {
    @Input() title: string;
    @Input() panelClass: String;
    @Input()
    isOpen: boolean = false;    
    set state( value: string ) {
        this._state = value;
    }

    get state() {
        return this._state;
    }

    label: string;
    private _state: string = 'closedPanel';
    
    constructor( private accordion: Accordion, private wcs: WebContentSvc, private elementRef: ElementRef ) {
        this.accordion.addGroup( this );
        wcs.content$.subscribe(result => {
            this.label = result.label;
        });
    }
    
    ngOnInit() {
        this.wcs.getContent(this.title);
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
    animationDone($event) {
        //console.log(this);
        //use this for scroll to focus after open
        if ( this._state =='openPanel') {
            this.accordion.closeOthers(this).then(success => {
                let selElem = this.elementRef.nativeElement.querySelector('#'+this.title);
                selElem.scrollIntoView();
            });
        }

    }
}
