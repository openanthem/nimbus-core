import { Behavior } from './../../../../shared/command.enum';
import { Component, Input } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/app-config.interface';
import { GenericDomain } from './../../../../model/generic-domain.model';
import { HttpMethod } from '../../../../shared/command.enum';

@Component({
    selector: 'nm-action-dropdown',
    template: `
    <div class="custom-dropdown {{this.widgetPosition}}" [ngClass]="{'open': isOpen}">
        <label></label>
        <input type="text">
        <a class="dropdownTrigger" href="javascript:void(0)" (click)="toggleOpen($event)"></a> 
        <div class="dropdownContent">
            <a href="javascript:void(0)" (click)="processOnClick(link.code)" *ngFor="let link of params">{{link.code}}</a>
        </div>
    </div>
  `
})
export class ActionDropdown {

    @Input() params: Param[];
    @Input() elementPath: string;
    isOpen: boolean = false;
    widgetPosition: string;
    
    constructor(private _wcs: WebContentSvc, private pageSvc: PageService) {
    }

    ngOnInit() {
        //console.log(this.params);
        // console.log(this.elementPath + '/');
    }

    toggleOpen(event: MouseEvent): void {
        event.preventDefault();
        if(window.innerWidth - event.clientX <= 400){
            this.widgetPosition = "west"
        }
        else{this.widgetPosition = "east"}
        //console.log("x =",event.screenX,"y =",event.screenY, "broswer inner width", window.innerWidth,"broswer outer width", window.outerWidth);
        this.isOpen = !this.isOpen;
        //console.log("widgetPosition",this.widgetPosition, window.innerWidth,"-",event.clientX,"=",window.innerWidth-event.clientX  );
    }

    processOnClick(linkCode: string) {
        let item: GenericDomain = new GenericDomain();
        this.pageSvc.processEvent(this.elementPath + '/' + linkCode, Behavior.execute.value, item, HttpMethod.GET.value);
    }
}


