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

import { Component, Input } from '@angular/core';
import { LoggerService } from '../../../services/logger.service';
import { UiNature } from '../../../shared/param-config';
import { ServiceConstants } from './../../../services/service.constants';

/**
 * \@author Tony Lopez
 * \@whatItDoes 
 *  Renders a button to allow for the printing of specific content.
 * \@howToUse 
 *  This component is driven by the server-side config of @Printable and the component
 *  that implements <nm-print-button>.
 * 
 *  The following @Input fields are required:
 *   - [config]:            The @Printable config that decorates the component invoking 
 *                          this <nm-print-button> component.
 *   - [contentSelector]:   A selector string that that identifies the closest parent 
 *                          relative to this <nm-print-button> instance.
 */
@Component({
    selector: 'nm-print-button',
    template: `
        <button pButton 
            (click)="execute($event)"
            type="button" 
            icon="fa fa-print" 
            class="ui-button-info ui-button-icon"></button>
    `
})
export class PrintButton {

    private readonly DEFAULT_DELAY = 300;

    @Input('config') config: UiNature;

    @Input('contentSelector') contentSelector: string;

    closeAfterPrint: boolean = true;

    delay: number = this.DEFAULT_DELAY;

    stylesheets: string[];

    useDelay: boolean = true;

    constructor(private loggerService: LoggerService) {
        
    }

    ngOnInit() {
        this.closeAfterPrint = this.config.attributes.closeAfterPrint;
        this.delay = this.config.attributes.delay;
        this.stylesheets = this.config.attributes.stylesheets;
        this.useDelay = this.config.attributes.useDelay;
        if (this.stylesheets && this.stylesheets.length > 0) {
            this.useDelay = true;
            this.delay = this.delay > this.DEFAULT_DELAY ? this.delay : this.DEFAULT_DELAY;
            this.loggerService.debug(`Stylesheets found. useDelay will be set to true with delay of ${this.delay}`);
        }
    }

    execute($event: UIEvent): void {
        this.loggerService.debug(`Print feature is looking for parent via selector: \"${this.contentSelector}\"`);
        var printableElement = $event.srcElement.closest(this.contentSelector);
        if (!printableElement) {
            throw new Error(`Unable to identify parent DOM element using \"${this.contentSelector}\"`);
        }
        var printWindow = window.open('', '_blank');
        if (this.stylesheets && this.stylesheets.length > 0) {
            for(var stylesheet of this.stylesheets) {
                var stylesheetURL = ServiceConstants.APP_BASE_URL + stylesheet;
                var link = document.createElement('link');
                link.setAttribute('rel', 'stylesheet');
                link.setAttribute('type', 'text/css');
                link.setAttribute('href', stylesheetURL);
                printWindow.document.head.appendChild(link);
                this.loggerService.debug(`Added print stylesheet for: \"${stylesheetURL}\"`);
            }
        }
        printWindow.document.body.innerHTML = printableElement.innerHTML;
        if (this.useDelay) {
            window.setTimeout(() => this.doPrintActions(printWindow), this.delay);
        } else {
            this.doPrintActions(printWindow);
        }
    }

    private doPrintActions(window: Window): void {
        window.print();
        if (this.closeAfterPrint) {
            window.close();
        }
    }
}