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
import { JL } from 'jsnlog';
import { Inject } from '@angular/core';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
export class LoggerService {

    JL: JL.JSNLog;

    constructor(@Inject('JSNLOG') JL: JL.JSNLog, private sessionStore){
        this.JL = JL;
        JL.setOptions({"defaultAjaxUrl" : "htpp://localhost:8000/log"})
                var appender = JL.createAjaxAppender("custom appender");
                appender.setOptions({
                    "bufferSize": 20,
                    "url" :  "http://localhost:8000/log",
                    "storeInBufferLevel": 1000,
                    "level": 3000,
                    "sendWithBufferLevel": 6000
                });
                JL().setOptions({
                    "appenders": [appender]
                });
    }

    beforeSendOverride(xhr: XMLHttpRequest, json: any, sessionId: string) {
       json.r = 
    };

    public info(message:string) {
        this.write(message,this.JL.getInfoLevel());
    }

    public warn(message:string) {
        this.write(message,this.JL.getWarnLevel());
    }

    public error(message:string) {
        this.write(message,this.JL.getErrorLevel())
    }

    public debug(message:string) {
        this.write(message,this.JL.getDebugLevel())
    }

    public trace(message:string) {
        this.write(message,this.JL.getTraceLevel())
    }

    public write(logMessage: string, logLevel: number) {
        this.JL().log(logLevel,logMessage);
    }
}