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
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Message } from './../shared/message';
import { ComponentTypes } from './../shared/param-annotations.enum';
import { ExecuteException } from './../shared/app-config.interface';
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Injectable()
export class NmMessageService {
    
    constructor() {}

    messageEvent = new Subject<Message[]>();
    messageEvent$ = this.messageEvent.asObservable();

    notifyErrorEvent(exec: ExecuteException) {
        if (exec.message) {
                let messageList: Message[] = [];
                let msg = new Message();
                let messages = [];
                msg.context = ComponentTypes.toast.toString();
                messages.push({severity: 'error',  summary: 'Error Message',  detail: exec.message, life: 10000});
                msg.messageArray = messages;
                messageList.push(msg);
                this.emitMessageEvent(messageList);
        }
    }

    createMessage(msgContext: String, messages: any[], life: number) {
        let messageList: Message[] = [];
        let msg = new Message();
        msg.context = ComponentTypes.toast.toString();
        msg.messageArray = messages;
        messageList.push(msg);
        this.emitMessageEvent(messageList);
    }

    emitMessageEvent(messageList: Message[]) {
        this.messageEvent.next(messageList);
    }
}