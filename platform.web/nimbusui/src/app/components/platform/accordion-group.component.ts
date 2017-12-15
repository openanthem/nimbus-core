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
