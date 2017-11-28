import { Component, Input } from '@angular/core';
import { PageService } from '../../../services/page.service';
import { GenericDomain } from '../../../model/generic-domain.model';
import { Param } from '../../../shared/app-config.interface';

@Component({
    selector: 'nm-card-details-grid',
    templateUrl: './card-details-grid.component.html'
})

export class CardDetailsGrid {

    @Input() grid: Param;

    constructor(private pageSvc : PageService) {
    }

    ngOnInit() {
        //console.log(JSON.stringify(this.grid));
        if (this.grid.config.uiStyles.attributes.onLoad === true) {
            this.pageSvc.processEvent(this.grid.path, '$execute', new GenericDomain(), 'GET');
        }
    }
}
