/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */
import { Component, Input } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Param } from '../../../shared/app-config.interface';

/**
 * \@author Mayur.Mehta
 * \@whatItDoes Tooltip allows to view help text on any input parameter
 * 
 * \@howToUse 
 * help attribute of an input parameter drives this. 
 *     
 *      eg. On the back end Config class - @TextBox(help="this is help text for textbox")
 *      On the front end component class - <nm-tooltip [helpText]="element.config?.uiStyles?.attributes?.help"></nm-tooltip>
 * 
 */
@Component({
    selector: 'nm-tooltip',
    template: `
        <span class="tooltip-container helpIcon {{widgetPosition}}">
            <button class="tooltip-trigger" (click)="toggleOpen($event)"></button>
            <div class="callout">
                <button class="close" (click)="closeCallout($event)"></button>
                <div [innerHTML]="htmlContent"></div>
            </div>
        </span>
  `
})
export class TooltipComponent {

    //helpText will recieve the text from the component
    @Input() helpText: string;
    widgetPosition: string = 'east';
    private _htmlContent: string;

    constructor(private _sanitizer: DomSanitizer) {
    }

    public get htmlContent(): SafeHtml {
        return this._sanitizer.bypassSecurityTrustHtml(this.helpText);
    } 

    /*
     * function to handle opening of tooltip bubble on mouse click
     * this function also handles the positioning of the tootltip bubble based on the position of the target component. By default, all the bubbles will 
     * appear on the right (west) of the component. However, if a component is on the far right of the screen, the bubble will open on the left (east).
    */ 
    toggleOpen(event: MouseEvent): void {
        event.preventDefault();

        if (window.innerWidth - event.clientX <= 250) {
            this.widgetPosition = 'west';
        } else {
            this.widgetPosition = 'east';
        }
        //retrieve all open bubbles in the document and close them
        let elems = document.querySelectorAll('.tooltip-container.open');
        Array.prototype.forEach.call (elems, function (el) {
            el.classList.remove('open');
        });
        //add open class to root class (.tooltip-container .open) for opening the bubble for selected component
        event.srcElement.parentElement.classList.add('open');

    }

    //function to handle closing of tooltip bubble on X (close) click
    closeCallout(event: MouseEvent): void {
        event.preventDefault();
        //parentElement.parentElement will get the opened bubble component.
        event.srcElement.parentElement.parentElement.classList.remove('open');
    }
}