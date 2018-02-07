import { LabelConfig } from './../../../../shared/app-config.interface';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Param } from '../../../../shared/app-config.interface';
import { FormGroup } from '@angular/forms';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { ServiceConstants } from './../../../../services/service.constants';
import { BaseElement } from '../../base-element.component';

@Component({
    selector: 'nm-filter-button',
    providers: [WebContentSvc],
    template:`
        <button class="{{fbutton.config?.uiStyles?.attributes?.cssClass}}" (click)="emitEvent(this)" type="button">
            {{label}}
            <ng-template [ngIf]="fText?.config?.uiStyles?.attributes?.alias == 'TextBox'">
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

   constructor(private pageService: PageService, private _wcs: WebContentSvc) {
       super(_wcs);
   }

   ngOnInit() {
       this.imagesPath = ServiceConstants.IMAGES_URL;
       for(var p in this.filterButton.type.model.params) {
           let element = this.filterButton.type.model.params[p];
           if (element.config.uiStyles.attributes.alias === 'Button') {
               this.fbutton = element;
               this.loadLabelConfig(element);
           }
           if (element.config.uiStyles.attributes.alias === 'TextBox') {
               this.fText = element;
               //this.filterCount = element.leafState;
           }
       }
       this.buttonClickEvent.subscribe(( $event ) => {
           //console.log( $event );
           this.pageService.processEvent( $event.fbutton.path, $event.fbutton.config.uiStyles.attributes.b,
               null, $event.fbutton.config.uiStyles.attributes.method );
       } );
   }

   emitEvent( $event: any ) {
       this.buttonClickEvent.emit( $event );
   }

}
