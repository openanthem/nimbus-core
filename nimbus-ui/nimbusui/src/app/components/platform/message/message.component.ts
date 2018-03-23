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

import { Component, Input, SimpleChanges} from '@angular/core';

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
    <div *ngIf="(displayType === 'INLINE' && showMessage === true); else growl" class="message {{messageType}}" role="alert">   {{messageText}} <button title="select to close alert" class="btn btn-plain closeIcon" (click)="showMessage=false"></button> </div>
     
     <ng-template #growl>
     <p-growl [(value)]="messages"></p-growl>
     </ng-template>

	          `
})

export class MessageComponent {

    @Input() messageType: String;
    @Input() messageText: String;
    @Input() messageContext: String;
    displayType: string;
    showMessage: boolean = true;
    messages: any[];


    ngOnChanges(changes: SimpleChanges) {

        let severity, summary;
        if(changes['messageContext'].currentValue !== undefined){
        if(changes['messageContext'].currentValue === "INLINE") this.displayType = "INLINE";
        if(changes['messageContext'].currentValue === "GROWL"){ 
        this.displayType = "GROWL"
        this.messages=[];

        if(changes['messageType'].currentValue === "SUCCESS") { severity = 'success'; summary = 'Success Message';}
        if(changes['messageType'].currentValue === "DANGER") { severity = 'error'; summary = 'Error Message';}
        if(changes['messageType'].currentValue === "WARNING") { severity = 'warn'; summary = 'Warn Message';}       
        if(changes['messageType'].currentValue === "INFO") { severity = 'info'; summary = 'Info Message';}

        this.messages.push({severity: severity,  summary: summary,  detail: changes['messageText'].currentValue});
        }
      
        }
    }

    constructor() {

    }

    ngOnInit() {

    }

}