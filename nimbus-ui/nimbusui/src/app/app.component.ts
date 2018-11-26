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
 * 
 */
'use strict';
import { Component, ViewEncapsulation } from '@angular/core';
import { HostListener } from '@angular/core';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'app-root',
    encapsulation: ViewEncapsulation.None,
    templateUrl: './app.component.html'
})

export class AppComponent {
    navIsFixed: boolean;

    constructor() {
    }

    @HostListener("window:scroll", [])
    onWindowScroll() {
        if (window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop > 100) {
            this.navIsFixed = true;
        } else if (this.navIsFixed && window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop < 10) { 
            this.navIsFixed = false; 
        } 
    } 
            
    scrollToTop() {(
        function smoothscroll() { 
            var currentScroll = document.documentElement.scrollTop || document.body.scrollTop; 
            if (currentScroll > 0) {
                //window.requestAnimationFrame(smoothscroll);
                window.scrollTo(0, 0);
            }
        })();
    }

    scrollDivToTop() {(
        function smoothscroll() { 
            document.getElementById('page-content').scrollTop = 0;
        })();
    }

    ngOnInit() {
    }

}
