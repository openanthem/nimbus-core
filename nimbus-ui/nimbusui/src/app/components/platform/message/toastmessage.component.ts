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

import { Component, Input, NgZone, ChangeDetectorRef } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ComponentTypes } from '../../../shared/param-annotations.enum';
import { NmMessageService } from './../../../services/toastmessage.service';
/**
 *  
 * \@author Sandeep Mantha
 * \@whatItDoes 
 *      This component can be used when we would display a user message after a certain action.
 *      messageType can be of type SUCESS or DANGER.  Based on the type, a specific CSS class would be applied.
 *      messageText is the message we want to display to the user.
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-toast-message',
    template: `
        <p-toast position="top-right"></p-toast>
    `,
    providers: [MessageService]
})

export class ToastMessageComponent {
    @Input() messageContext: String;
    @Input() messageArray: any[];
    @Input() life: number;
    @Input() styleClass: String;
    componentTypes = ComponentTypes;

    constructor(private messageService: MessageService, private cdr: ChangeDetectorRef, private ngZone: NgZone, private _messageservice: NmMessageService) {}

    ngOnInit() {

        this._messageservice.messageEvent$.subscribe(messages => {
            this.messageService.clear();
            messages.forEach(msg => {
                this.ngZone.run(() => {
                    setTimeout(() => {
                        this.messageService.addAll(msg.messageArray);
                        this.cdr.detectChanges();
                    });
                });
            });
           

        });
        // this.ngZone.run(() => {
        //     setTimeout(() => {
        //         this.messageService.addAll(this.messageArray);
        //         this.cdr.detectChanges();
        //     });
        // });
    }

}