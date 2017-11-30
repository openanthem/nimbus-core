import { Component, Input } from '@angular/core';
import { Param } from '../../../../shared/app-config.interface';

@Component({
    selector: 'nm-dropdown',
    template: `

    <div class="custom-dropdown {{this.widgetPosition}}" [ngClass]="{'open': isOpen}">
    <label>Label for dropdown</label>
    <input type="text">
    <a class="dropdownTrigger" href="javascript:void(0)" (click)="toggleOpen($event)"></a> 
    <div class="dropdownContent">
        <a (click)="toggleOpen($event)">Action 1</a>
        <a href="javascript:void(0)" (click)="processOnClick()" type="submit"> 
        
        </a>
    </div>
</div>
  `
})
export class dropdownComponent {

    @Input() element: string;
    isOpen: boolean = false;
    widgetPosition: string;
    constructor() {
    }

    toggleOpen(event: MouseEvent): void {
        event.preventDefault();
        if(window.innerWidth - event.clientX <= 400){
            this.widgetPosition = "west"
        }
        else{this.widgetPosition = "east"}
        //console.log("x =",event.screenX,"y =",event.screenY, "broswer inner width", window.innerWidth,"broswer outer width", window.outerWidth);
        this.isOpen = !this.isOpen;
        console.log("widgetPosition",this.widgetPosition, window.innerWidth,"-",event.clientX,"=",window.innerWidth-event.clientX  );
    }

    processOnClick() {
        // TODO
    }
}


