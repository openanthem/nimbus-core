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
import { ParamUtils } from './../shared/param-utils';
import { PrintService } from './../services/print.service';
import { PrintEvent } from './../shared/print-event';
import { ServiceConstants } from './../services/service.constants';
import { LoggerService } from './../services/logger.service';
import { UiNature } from '../shared/param-config';
import { Converter } from './../shared/object.conversion';
import { Subscription } from 'rxjs';

/**
 * \@author Tony Lopez
 * \@whatItDoes 
 *  Renders a button to allow for the printing of specific content.
 * \@howToUse 
 *  This component is driven by the server-side config of @Button(style=Style.PRINT, printPath="..."")
 *  Optional configuration is available by annotating the target printPath field with @Printable.
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

    @Input("nmPrint") element: Param;

    config = new PrintableConfig();

    nativeElement: Element;

    subscription: Subscription;

    constructor(
        private elementRef: ElementRef, 
        private loggerService: LoggerService, 
        private printService: PrintService) { 
            this.nativeElement = elementRef.nativeElement;
    }

    ngOnInit() {
        let uiNature: UiNature;
        if (this.element && (uiNature = ParamUtils.getPrintable(this.element))) {
            this.config = PrintableConfig.fromNature(uiNature);
        }
        if (this.config.stylesheet) {
            this.config.useDelay = true;
            this.config.delay = this.config.delay > PrintableConfig.DEFAULT_DELAY ? this.config.delay : PrintableConfig.DEFAULT_DELAY;
            this.loggerService.debug(`Stylesheets found. useDelay will be set to true with delay of ${this.config.delay}`);
        }
    }

    ngAfterViewInit() {
        this.subscription = this.printService.printClickUpdate$.subscribe((event: PrintEvent) => {
            if(event.path === this.element.path) {
                this.execute(event.uiEvent);
            }
        });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    execute($event: UIEvent): void {
        var printWindow = window.open('', '_blank');

        // Apply stylesheets to the window
        if (this.config.useAppStyles) {
            // TODO
        }
        if (this.config.stylesheet) {
            var stylesheetURL = ServiceConstants.APP_BASE_URL + this.config.stylesheet;
            var link = document.createElement('link');
            link.setAttribute('rel', 'stylesheet');
            link.setAttribute('type', 'text/css');
            link.setAttribute('href', stylesheetURL);
            printWindow.document.head.appendChild(link);
            this.loggerService.debug(`Added print stylesheet for: \"${stylesheetURL}\"`);
        }

        // Determine the element that should be printed.
        var printableElement = this.nativeElement;
        if (this.contentSelector) {
            this.loggerService.debug(`Print feature is looking for parent via selector: \"${this.contentSelector}\"`);
            printableElement = $event.srcElement.closest(this.contentSelector);
            if (!printableElement) {
                printWindow.close();
                throw new Error(`Unable to identify parent DOM element using \"${this.contentSelector}\"`);
            }
        }
        printWindow.document.body.innerHTML = printableElement.innerHTML;

        // Handle any other final actions leading up to displaying the print dialog
        if (this.config.useDelay) {
            printWindow.setTimeout(() => this.doPrintActions(printWindow), this.config.delay);
        } else {
            this.doPrintActions(printWindow);
        }
    }

    private doPrintActions(window: Window): void {
        if (this.config.autoPrint) {
            window.print();
            if (window && this.config.closeAfterPrint) {
                window.close();
            }
        }
    }
}

export class PrintableConfig {
    public static readonly DEFAULT_DELAY = 300;
    autoPrint: boolean = true;
    closeAfterPrint: boolean = true;
    delay: number = PrintableConfig.DEFAULT_DELAY;
    stylesheet: string;
    useAppStyles: boolean = false; // TODO Not yet implemented. Change to true when implemented.
    useDelay: boolean = true;

    static fromNature(uiNature: UiNature): PrintableConfig {
        return Converter.convert(uiNature.attributes, new PrintableConfig());
    }
}