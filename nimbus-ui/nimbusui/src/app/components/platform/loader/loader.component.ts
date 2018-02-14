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
import { LoaderState } from './loader.state';
import { LoaderService } from './../../../services/loader.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

/**
 * 
 * \@author reference https://github.com/ivanderbu2/angular-redux
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'app-loader',
    templateUrl: 'loader.component.html',
    styleUrls: ['loader.component.css']
})

export class LoaderComponent implements OnInit {

show = false;
private subscription: Subscription;

constructor(private loaderService: LoaderService) { }

ngOnInit() { 
        this.subscription = this.loaderService.loaderUpdate
            .subscribe((state: LoaderState) => {
                this.show = state.show;
            });
    }

ngOnDestroy() {
        this.subscription.unsubscribe();
    }
}