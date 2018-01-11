/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { ServiceConstants } from './../../services/service.constants';

@Component({
    templateUrl: './login.component.html'
})
export class LoginCmp {

    public loginForm;
    public imagesPath: string;

    constructor(public fb: FormBuilder, private _route: ActivatedRoute, private _router: Router) {
    }

    ngOnInit() {
        this.loginForm = this.fb.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        });
        this.imagesPath = ServiceConstants.IMAGES_URL;
    }

    onSubmit() {
        if(this.loginForm.value.username === 'admin') {
            this._router.navigate(['/h/admindashboard']);
        } else if(this.loginForm.value.username === 'supervisor') {
             this._router.navigate(['/cs/a']);
        } else if(this.loginForm.value.username === 'training') {
            this._router.navigate(['/pc/a']);
        } else if(this.loginForm.value.username === 'member') {
            this._router.navigate(['/mem/a']);
        } else {
           this._router.navigate(['/h/cmdashboard']);
        }
    }

}
