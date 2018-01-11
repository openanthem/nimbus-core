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

import { Component } from '@angular/core';
import { AccordionGroup } from './accordion-group.component';

@Component( {
    selector: 'accordion',
    template: `
  <ng-content></ng-content>
          `,
    host: {
        'class': 'panel-group'
    }
} )

export class Accordion {
    groups: Array<AccordionGroup> = [];

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
        this.groups.forEach(( group: AccordionGroup ) => {
            if ( group !== openGroup ) {
                group.state = 'openPanel';
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
