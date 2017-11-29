import { Component, Input, OnDestroy, ElementRef } from '@angular/core';
import { Accordion } from './accordion.component';
import { WebContentSvc } from '../../services/content-management.service';
import { trigger,state,style,transition,animate,keyframes } from '@angular/animations';

@Component( {
    selector: 'accordion-group',
    providers: [ WebContentSvc ],
    template: `
        <div id="{{title}}" class="panel {{panelClass}}">
            <div class="panel-heading" (click)="toggleOpen($event)">
              <h4 class="panel-title">
                  <a href tabindex="0"><span>{{label}}</span></a>
              </h4>
            </div>
            <div class="panel-collapse" [@accordionAnimation]='state' (@accordionAnimation.done)="animationDone($event)">
                <div class="panel-body">
                    <ng-content></ng-content>
                </div>
            </div>
        </div>
   `
   ,
   animations: [
       trigger('accordionAnimation', [
           state('open', style({
               maxHeight: '10000px',
           })),
           state('close', style({
               maxHeight: '0',
           })),
           transition('close => open', animate('400ms ease-in')),
           transition('open => close', animate('400ms ease-out')),
        ]),
   ]
})

export class AccordionGroup implements OnDestroy {
    @Input() title: string;
    @Input() panelClass: String;
    @Input()
    set state( value: string ) {
        this._state = value;
    }

    get state() {
        return this._state;
    }

    label: string;
    private _state: string = 'close';
    
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
        if(this.state == 'open'){
            this.state = 'close';
        }
        else{
            this.state = 'open';
        }
    }
    animationDone($event) {
        //console.log(this);
        //use this for scroll to focus after open
        if ( this._state =='open') {
            this.accordion.closeOthers(this).then(success => {
                let selElem = this.elementRef.nativeElement.querySelector('#'+this.title);
                selElem.scrollIntoView();
            });
        }

    }
}
