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

/**
 * \@author Purna
 * \@whatItDoes 
 *  Component to capture/display user's signature
 * \@howToUse 
 *  
 */
@Component({
  selector: 'nm-signature-button',
  template: `
    <button (click)="getUpdatedSignature()">Get Updated Signature</button>
   `
})
export class SignatureButton {


    @Input() scriptName: string;
    @Input() canvas: any;

    constructor() {
    }

    getUpdatedDataUrl (updatedDataUrl){
        console.log('updatedDataUrl', updatedDataUrl);
    }

    getUpdatedSignature() {
         const callExternalFun = eval( this.scriptName);
         callExternalFun(this.canvas.toDataURL(), this.getUpdatedDataUrl);
    }

}