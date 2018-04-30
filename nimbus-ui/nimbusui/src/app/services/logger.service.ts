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
import { LogLevel } from './../shared/logLevel';
import { LogEntry } from './../shared/logEntry';
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
export class LoggerService {

    public info(message:string) {
        this.write(message,LogLevel.info);
    }

    public warn(message:string) {
        this.write(message,LogLevel.warn);
    }

    public error(message:string) {
        this.write(message,LogLevel.error)
    }

    public debug(message:string) {
        this.write(message,LogLevel.debug)
    }

    public trace(message:string) {
        this.write(message,LogLevel.trace)
    }

    public write(logMessage: string, logLevel: LogLevel) {

        let logEntry = new LogEntry(logMessage,new Date(), logLevel);
        console.log(logEntry.message + logEntry.time + logEntry.level);
    }

}