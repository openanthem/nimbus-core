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

import { Component, Input, SimpleChanges, ChangeDetectorRef } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ComponentTypes } from '../../../shared/param-annotations.enum';

/**
 *  
 * \@author Vivek.Kamineni
 * \@whatItDoes 
 *      This component can be used when we would display a user message after a certain action.
 *      messageType can be of type SUCESS or DANGER.  Based on the type, a specific CSS class would be applied.
 *      messageText is the message we want to display to the user.
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-message',
    template: `
        <p-toast position="top-right"></p-toast>
        <p-messages *ngIf="messageContext === componentTypes.inline.toString()" 
            [(value)]="messageArray" 
            [closable]="false" 
            [styleClass]="styleClass"
            [showTransitionOptions]="'0ms'" 
            [hideTransitionOptions]="'0ms'">
        </p-messages>
    `
})

export class MessageComponent {
    @Input() messageContext: String;
    @Input() messageArray: any[];
    @Input() life: number;
    @Input() styleClass: String;
    componentTypes = ComponentTypes;

    constructor(private messageService: MessageService, private cdr: ChangeDetectorRef) {

    }

    ngOnInit() {
        this.updateMessageObject();
    }

    // ChangeDetectionRef.detectChanges() will check the view and updates the missing elements
     // as toast component is not getting update in the view
     updateMessageObject() {
        if (this.messageContext === this.componentTypes.toast.toString() && this.messageArray && this.messageArray.length > 0) {
                this.messageService.addAll(this.messageArray);
                setTimeout(() => {
                    this.cdr.detectChanges();
                });
        }
    }

}