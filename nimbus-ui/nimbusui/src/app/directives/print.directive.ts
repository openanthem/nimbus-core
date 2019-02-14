/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

import { Param } from './../shared/param-state';
import { Directive, ElementRef, Input } from '@angular/core';
import { PrintService } from './../services/print.service';
import { PrintEvent } from './../shared/print-event';
import { ServiceConstants } from './../services/service.constants';
import { LoggerService } from './../services/logger.service';
import { Subscription } from 'rxjs';
import { PrintConfig } from './../shared/print-event';

/**
 * \@author Tony Lopez
 * \@whatItDoes 
 *  Renders a button to allow for the printing of specific content.
 * \@howToUse 
 *  This component is driven by the server-side config of @Button(style=Style.PRINT, printPath="..."")
 *  Optional configuration is available by annotating the @Button with @PrintConfig.
 * 
 *  By default the content to be printed is the inner HTML of the element that is decorated with [nmPrint].
 *  [contentSelector] (a selector string) may be provided to overwrite this logic and find the next closest
 *  parent.
 */
@Directive({
  selector: '[nmPrint]',
})
export class PrintDirective {

    @Input("contentSelector") contentSelector: string;

    @Input("isPage") isPage: boolean;

    @Input("nmPrint") element: Param;

    nativeElement: Element;

    subscription: Subscription;

    constructor(
        private elementRef: ElementRef, 
        private loggerService: LoggerService, 
        private printService: PrintService) { 
            this.nativeElement = elementRef.nativeElement;
    }

    ngAfterViewInit() {        
        this.subscription = this.printService.printClickUpdate$.subscribe((event: PrintEvent) => {
            if(event.path === this.element.path) {
                if (event.printConfig.stylesheet || event.printConfig.useAppStyles) {
                    event.printConfig.useDelay = true;
                    event.printConfig.delay = event.printConfig.delay > PrintConfig.DEFAULT_DELAY ? event.printConfig.delay : PrintConfig.DEFAULT_DELAY;
                    this.loggerService.debug(`Stylesheets found. useDelay will be set to true with delay of ${event.printConfig.delay}`);
                }
                this.execute(event);
            }
        });
    }

    ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    execute(event: PrintEvent): void {
        var printWindow = window.open('', '_blank');
        this.applyStylesToWindow(event, printWindow);
        printWindow.document.body.innerHTML = this.getPrintableContent(event);
        if (event.printConfig.useDelay) {
            printWindow.setTimeout(() => this.doPrintActions(event, printWindow), event.printConfig.delay);
        } else {
            this.doPrintActions(event, printWindow);
        }
    }

    private applyStylesToWindow(event: PrintEvent, printWindow: Window): void {
        if (event.printConfig.useAppStyles) {
            let links = window.document.getElementsByTagName('link')
            for(var i = 0; i < links.length; i++) {
                let link = links.item(i);
                if (link.rel === 'stylesheet') {
                    let clonedLink = link.cloneNode(true) as HTMLLinkElement;
                    clonedLink.href = link.href;
                    printWindow.document.head.appendChild(clonedLink);
                }
            }

            let styles = window.document.getElementsByTagName('style')
            for(var i = 0; i < styles.length; i++) {
                printWindow.document.head.appendChild(styles.item(i).cloneNode(true));
            }
        }
        if (event.printConfig.stylesheet) {
            var stylesheetURL = ServiceConstants.APP_BASE_URL + event.printConfig.stylesheet;
            var link = document.createElement('link');
            link.setAttribute('rel', 'stylesheet');
            link.setAttribute('type', 'text/css');
            link.setAttribute('href', stylesheetURL);
            printWindow.document.head.appendChild(link);
            this.loggerService.debug(`Added print stylesheet for: \"${stylesheetURL}\"`);
        }
    }

    private getPrintableContent(event: PrintEvent): string {
        var printableElement = this.nativeElement;
        // BUG Angular does not allow binding directives to route level components, so using a one-off
        // fix below to set the page to the expected element.
        if (this.isPage) {
            printableElement = this.nativeElement.parentElement;
        }
        if (this.contentSelector) {
            this.loggerService.debug(`Print feature is looking for parent via selector: \"${this.contentSelector}\"`);
            printableElement = event.uiEvent.srcElement.closest(this.contentSelector);
            if (!printableElement) {
                throw new Error(`Unable to identify parent DOM element using \"${this.contentSelector}\"`);
            }
        }
        return printableElement.innerHTML;
    }

    private doPrintActions(event: PrintEvent, window: Window): void {
        if (event.printConfig.autoPrint) {
            window.print();
            if (window && event.printConfig.closeAfterPrint) {
                window.close();
            }
        }
    }
}