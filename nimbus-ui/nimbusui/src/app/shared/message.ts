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
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
import { Converter } from './object.conversion';
import { Serializable } from './serializable';

export class Message implements Serializable<Message, string> {
    type: string;
    text: string;
    context: string;
    messageArray: any[] = [];
    life: number;
    
    
    deserialize( inJson ) {

        let obj = this;
        obj = Converter.convert(inJson,obj);

        if(this.context !== undefined){

           let severity: string, summary: string, life: number;
           
            switch (this.type) {               

                case "SUCCESS": 
                    severity = 'success'; summary = 'Success Message';
                    if(this.life != undefined)
                        life = this.life;
                    else    
                        life = 3000;
                    break;
                case "DANGER": 
                    severity = 'error'; summary = 'Error Message';
                    if(this.life != undefined)
                        life = this.life;
                    else    
                        life = 10000;
                    break; 
                case "WARNING": 
                    severity = 'warn'; summary = 'Warn Message';
                    if(this.life != undefined)
                        life = this.life;
                    else    
                        life = 5000;
                    break;
                case "INFO": 
                    severity = 'info'; summary = 'Info Message';
                    if(this.life != undefined)
                        life = this.life;
                    else    
                        life = 3000;
                    break;   
            }
    
            this.messageArray.push({severity: severity,  summary: summary,  detail: this.text});
            obj['messageArray'] = this.messageArray;
            obj['life'] = life;

        }

        return obj;
    }
}
