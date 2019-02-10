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
import { SubDomainFlowCmp } from './components/domain/subdomain-flow.component';
import { DomainFlowCmp } from './components/domain/domain-flow.component';
import { HomeLayoutCmp } from './components/home/home-layout.component';
import { LandingPage } from './components/login/auth-landingpage';
import { LayoutResolver } from './components/domain/layout-resolver.service';
import { PageResolver } from './components/platform/content/page-resolver.service';
import { PageNotfoundComponent } from './components/platform/content/page-notfound.component';
import { PageContent } from './components/platform/content/page-content.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
const APPROUTES: Routes = [
    //first landing page routes need to be defined here and the rest will be loaded via dynamic router
   
    // routes redesign - URL pattern: h/:domain/:pageId
    {
        path: 'h',
        component: HomeLayoutCmp,
        data: { layout : 'home'},
        children: [
            {
                path: ':domain', component: DomainFlowCmp,
                resolve: { layout: LayoutResolver },
                children: [
                    {
                        path: ':pageId', component: PageContent,
                        resolve: { page: PageResolver }
                    },
                    {
                        path: ':subdomain', component: SubDomainFlowCmp,
                        children: [
                            { path: ':pageId', component: PageContent, resolve: { page: PageResolver }},
                            { path: 'pnf', component: PageNotfoundComponent },
                            { path: '', component: PageNotfoundComponent }
                        ]
                    },
                    { path: 'pnf', component: PageNotfoundComponent },
                    { path: '', component: PageNotfoundComponent }
                ]
            },
            { path: 'pnf', component: PageNotfoundComponent },
            { path: '', redirectTo: 'f', pathMatch: 'full' },
            { path: '**', component: PageNotfoundComponent }
        ]
    },
    { path: ':auth', component: LandingPage },
    { path: '', component: PageNotfoundComponent }
];

@NgModule({
    imports: [ RouterModule.forRoot(APPROUTES, {enableTracing: false, useHash:true}) ],
    exports: [ RouterModule ],
    providers: [ PageResolver, LayoutResolver ]
})
export class AppRoutingModule {}
