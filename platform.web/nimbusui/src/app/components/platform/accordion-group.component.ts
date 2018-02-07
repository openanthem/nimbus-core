import { Component, Input, OnDestroy, ElementRef } from '@angular/core';
import { Accordion } from './accordion.component';
import { BaseElement } from './base-element.component';
import { WebContentSvc } from '../../services/content-management.service';
import { trigger,state,style,transition,animate,keyframes } from '@angular/animations';
import { Param } from './../../shared/app-config.interface';

@Component( {
    selector: 'accordion-group',
    providers: [ WebContentSvc ],
    template: `
        <div id="{{title}}" class="panel {{panelClass}} {{state}} ">
            <div class="panel-heading panel-title">
                <h2>
                  <button attr.aria-expanded="{{isOpen}}" (click)="toggleOpen($event)">{{label}}
                    <!--<span *ngIf="leftElement" class="leftElement">this is optional left</span>-->
                  </button>
              </h2>
              <!-- <span *ngIf="rightElement" class="rightElement">this is optional right</span> -->
            </div>
            <div class="panel-collapse" 
                [ngClass]="{'displayNone': isHidden}" 
                [@accordionAnimation]='state' 
                (@accordionAnimation.start)="animationStart($event)"
                (@accordionAnimation.done)="animationDone($event)"
                attr.aria-hidden="{{!isOpen}}">
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

export class AccordionGroup extends BaseElement implements OnDestroy {
    @Input() param : Param;
    @Input() title: string;
    @Input() panelClass: String;
    @Input()
    isOpen: boolean = false;    
    isHidden: boolean = true;
    set state( value: string ) {
        this._state = value;
    }

    get state() {
        return this._state;
    }

    private _state: string = 'closedPanel';
    
    constructor( private accordion: Accordion, private _wcs: WebContentSvc, private elementRef: ElementRef ) {
        super(_wcs);
        this.accordion.addGroup( this );        
    }
    
    ngOnInit() {
        this.loadLabelConfig(this.param);
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
    animationStart($event) {
        this.isHidden = false;
        
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
        if(this._state =='closedPanel'){
            this.isHidden = true;
        }
    }
}
