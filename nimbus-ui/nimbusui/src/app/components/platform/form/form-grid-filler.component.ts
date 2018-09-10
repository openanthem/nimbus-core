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
 * \@author Dinakar.Meda
 * \@whatItDoes Introduces spaces in form grid. This is useful when placing form elements
 * in specific places.
 * 
 * \@howToUse 
 * @FormGridFiller annotation from Configuration drives this. 
 *      Attribute "cols" drives the size of the filler - how many cols it occupies.
 *      Attribute "cssClass" override css class
 * 
 */
@Component({
    selector: 'nm-form-grid-filler',
    template:`
        <span></span>
    `
})

export class FormGridFiller {
    constructor() {
    }

    ngOnInit() {
    }
}

