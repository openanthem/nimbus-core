import { Component, Input } from '@angular/core';
import { Param } from '../../shared/app-config.interface';

@Component({
    selector: 'nm-menu',
    template: `
    <nav class="nav navbar navbar-toggleable-md navbar-light" role="navigation"> 
        <button class="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarToggler" aria-controls="navbarToggler" aria-expanded="false" aria-label="Toggle navigation"> 
            <span class="navbar-toggler-icon"></span> 
        </button> 
        <div class="navbar-collapse nav-block collapse in" id="navbarToggler" aria-expanded="false"> 
            <ng-template ngFor let-element [ngForOf]="element?.type?.model?.params">
                <nm-link [element]="element" inClass="nav-link"></nm-link>
            </ng-template>
        </div>
    </nav>
    `
})

export class Menu {

    @Input() element: Param;

    constructor() {
    }

    ngOnInit() {
        //console.log(this.element);
    }

}

