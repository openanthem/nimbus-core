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
import { Observable } from 'rxjs/Observable';
import { Param } from './param-state';
import { Subject } from 'rxjs/Subject';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

export class RequestContainer {
    path: string;
    method: string;
    payload: any;
    subscription: Subject<any>;

    constructor(path:string,method: string, payload: any, subscription: Subject<any>) {
        this.path = path;
        this.method = method;
        this.payload = payload;
        this.subscription = subscription;
    }
  
}