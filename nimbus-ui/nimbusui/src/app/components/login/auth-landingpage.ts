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

import { Component } from '@angular/core';
import { Router } from '@angular/router';
/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    template: ` 
    
    <button class = "btn btn-primary" type="button" (click)="routeCaseManager()">Case Manager</button>
    <button type="button"  class = "btn btn-danger" (click)="routeAdmin()">Admin</button>
   `
})
export class LandingPage {

    constructor(private _router: Router) {
    }

    ngOnInit() {
    }

    routeCaseManager() {
         this._router.navigate(['/main/f']);
    }

    routeAdmin() {
         this._router.navigate(['/main/a']);
    }

}
