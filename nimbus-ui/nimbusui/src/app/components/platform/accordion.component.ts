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
import { Component } from '@angular/core';
import { AccordionGroup } from './accordion-group.component';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component( {
    selector: 'accordion',
    template: `
        <div class="text-sm-right">
            <button type="button" class="btn btn-expand" (click)="openAll()">Expand All</button>
            <span class="btn-pipe">|</span>
            <button type="button" class="btn btn-expand" (click)="closeAll()">Collapse All</button>
        </div>
        <ng-content></ng-content>

    `,
    host: {
        'class': 'panel-group'
    }
} )

export class Accordion {
    groups: Array<AccordionGroup> = [];
    private _expandAllClicked:boolean = false;

    get expandAllClicked():boolean {
        return this._expandAllClicked;
    }
    set expandAllClicked(expandAll:boolean) {
        this._expandAllClicked = expandAll;
    }

    addGroup( group: AccordionGroup ): void {
        this.groups.push( group );
    }

    closeOthers( openGroup: AccordionGroup ): Promise<boolean> {
        this.groups.forEach(( group: AccordionGroup ) => {
            if ( group !== openGroup ) {
                group.state = 'closedPanel';
            }
        } );
        return Promise.resolve(true);
    }

    openAll( openGroup: AccordionGroup ): void {
        this.expandAllClicked = true;
        this.groups.forEach(( group: AccordionGroup ) => {
            if ( group !== openGroup ) {
                group.state = 'openPanel';
            }
        } );
    }

    closeAll( openGroup: AccordionGroup ): void {
        this.groups.forEach(( group: AccordionGroup ) => {
            if ( group !== openGroup ) {
                group.state = 'closedPanel';
            }
        } );
    }

    removeGroup( group: AccordionGroup ): void {
        const index = this.groups.indexOf( group );
        if ( index !== -1 ) {
            this.groups.splice( index, 1 );
        }
    }
}
