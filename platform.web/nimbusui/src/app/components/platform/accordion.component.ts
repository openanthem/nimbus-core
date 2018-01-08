/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
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
