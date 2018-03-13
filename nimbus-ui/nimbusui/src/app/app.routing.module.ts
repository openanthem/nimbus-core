import { DomainLayoutCmp } from './components/domain/domain-layout.component';
import { DomainFlowCmp } from './components/domain/domain-flow.component';
import { StyleGuideCmp } from './styleguide/style-guide.component';
import { MainLayoutCmp } from './components/home/main-layout.component';
import { HomeLayoutCmp } from './components/home/home-layout.component';
import { LoginLayoutCmp } from './components/login/login-layout.component';
import { LoginCmp } from './components/login/login.component';
import { LandingPage } from './components/login/auth-landingpage';
import { LayoutResolver } from './components/domain/layout-resolver.service';
import { PageResolver } from './components/platform/content/page-resolver.service';
import { PageNotfoundComponent } from './components/platform/content/page-notfound.component';
import { PageContent } from './components/platform/content/page-content.component';
import { FlowWrapper } from './components/platform/content/flow-wrapper.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SelectivePreloadingStrategy } from './selective.preloading.strategy';

const APPROUTES: Routes = [
    //first landing page routes need to be defined here and the rest will be loaded via dynamic router
    {
      path: 'user',
      children: [
        {
          path: '', component: LoginLayoutCmp,
          children: [
            { path: 'login', component: LoginCmp },
            { path: 'styles', component: StyleGuideCmp},
            { path: '', redirectTo: 'login', pathMatch: 'full' },
            { path: '**', component: PageNotfoundComponent }
          ]
        }
      ]
    },
    // routes redesign - URL pattern: h/:domain/:pageId
    {
        path: 'h', component: HomeLayoutCmp, data: {'layout': 'home'},
        children: [
                   {
                       path: ':domain', component: DomainFlowCmp,
                       resolve: { layout: LayoutResolver },
                       children: [
                                     {
                                         path: ':pageId', component: PageContent,
                                         resolve: { page: PageResolver }
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
    // To remove these after a discuss on deployment of clients
    {
        path: 'a', component: HomeLayoutCmp, data: {'layout': 'adminhome'},
        children: [
                   {
                       path: ':domain', component: DomainFlowCmp,
                       resolve: { layout: LayoutResolver },
                       children: [
                                     {
                                         path: ':id', component: PageContent,
                                         resolve: { page: PageResolver }
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
    // hand coded routes
//    {
//        path: 'ca', component: MainLayoutCmp, data: {'layout': 'adminhome'},
//        children: [
//            {
//              path: 'a', component: FlowWrapper, data: {'domain' : 'admindashboard'},
//              children: [
//                {
//                  path: ':id', component: PageContent,
//                  resolve: { page: PageResolver }
//                },
//                {
//                  path: 'orgdashboard', component: DomainLayoutCmp, data: {'domain' : 'orgdashboard', 'layout': 'orglayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                  path: 'nestedorgdashboard', component: DomainLayoutCmp, data: {'domain' : 'nestedorgdashboard', 'layout': 'orglayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                  path: 'usergroupR', component: DomainLayoutCmp, data: {'domain' : 'usergroupview', 'layout': 'orglayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                  path: 'rolesR', component: DomainLayoutCmp, data: {'domain' : 'rolesview', 'layout': 'orglayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                  path: 'orgR', component: DomainLayoutCmp, data: {'domain' : 'orgview', 'layout': 'orglayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                    path: 'userR', component: DomainLayoutCmp, data: {'domain' : 'userview', 'layout': 'orglayout'},
//                    children: [
//                      {
//                        path: ':id', component: PageContent,
//                        resolve: { page: PageResolver }
//                      },
//                      { path: '', component: PageNotfoundComponent }
//                    ]
//                },
//                {
//                  path: 'clientuser', component: DomainLayoutCmp, data: {'domain' : 'clientuser', 'layout': 'orglayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                  path: 'jobs', component: DomainLayoutCmp, data: {'domain' : 'jobmanagement', 'layout': 'orglayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                { path: '', component: PageNotfoundComponent }
//              ]
//            }
//         ]
//    },
//    {
//        path: 'cs', component: MainLayoutCmp, data: {'layout': 'supervisorhome'},
//        children: [
//          {
//            path: 'a', component: FlowWrapper, data: {'domain' : 'cmsdashboard'},
//            children: [
//              {
//                path: ':id', component: PageContent,
//                resolve: { page: PageResolver }
//              },
//              { path: '', component: PageNotfoundComponent }
//            ]
//          }
//        ]
//    },
//    {
//      path: 'pc', component: MainLayoutCmp, data: {'layout': 'home'},
//      children: [
//        {
//          path: 'a', component: FlowWrapper, data: {'domain' : 'petclinicdashboard'},
//          children: [
//           {
//             path: ':id', component: PageContent,
//             resolve: { page: PageResolver }
//           },
//          {
//            path: 'visitview', component: SubFlowWrapper, data: {'domain' : 'visitview'},
//            children: [
//              {
//                path: ':id', component: PageContent,
//                resolve: { page: PageResolver }
//              },
//              { path: '', component: PageNotfoundComponent }
//            ]
//          }
//          ]
//        },
//        {
//            path: 'v', component: FlowWrapper, data: {'domain' : 'veterinarianlandingview'},
//            children: [
//              {
//                path: ':id', component: PageContent,
//                resolve: { page: PageResolver }
//              },
//              {
//                path: 'veterinarianview', component: SubFlowWrapper, data: {'domain' : 'veterinarianview'},
//                children: [
//                  {
//                    path: ':id', component: PageContent,
//                    resolve: { page: PageResolver }
//                  },
//                  { path: '', component: PageNotfoundComponent }
//                ]
//              }
//            ]
//         },
//         {
//            path: 'o', component: FlowWrapper, data: {'domain' : 'ownerlandingview'},
//            children: [
//              {
//                path: ':id', component: PageContent,
//                resolve: { page: PageResolver }
//              },
//              {
//                path: 'ownerview', component: SubFlowWrapper, data: {'domain' : 'ownerview'},
//                children: [
//                  {
//                    path: ':id', component: PageContent,
//                    resolve: { page: PageResolver }
//                  },
//                  { path: '', component: PageNotfoundComponent }
//                ]
//              },
//              {
//                  path: 'petview', component: SubFlowWrapper, data: {'domain' : 'petview'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//              },
//              {
//                  path: 'visitview', component: SubFlowWrapper, data: {'domain' : 'visitview'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//              },
//              { path: '', component: PageNotfoundComponent }
//            ]
//         },
//        { path: 'auth', component: LandingPage },
//        { path: '', redirectTo: 'f', pathMatch: 'full' },
//        { path: '**', component: PageNotfoundComponent }
//      ]
//    },
//    {
//        path: 'mem', component: MainLayoutCmp, data: {'layout': 'memberhome'},
//        children: [
//          {
//            path: 'a', component: FlowWrapper, data: {'domain' : 'memdashboard'},
//            children: [
//              {
//                path: ':id', component: PageContent,
//                resolve: { page: PageResolver }
//              },
//              { path: '', component: PageNotfoundComponent }
//            ]
//          }
//        ]
//    },
//    {
//      path: 'cm', component: MainLayoutCmp, data: {'layout': 'home'},
//      children: [
//        {
//          path: 'a', component: FlowWrapper, data: {'domain' : 'cmdashboard'},
//          children: [
//           {
//             path: ':id', component: PageContent,
//             resolve: { page: PageResolver }
//           },
//           {
//             path: 'cmcaseR', component: DomainLayoutCmp, data: {'domain' : 'cmcaseview', 'layout':'caseoverviewlayout'},
//             children: [
//               {
//                 path: 'patientenrollmentview', component: SubFlowWrapper, data: {'domain' : 'patientenrollmentview'},
//                 children: [
//                   {
//                     path: ':id', component: PageContent,
//                     resolve: { page: PageResolver }
//                   },
//                   { path: '', component: PageNotfoundComponent }
//                 ]
//               },
//               {
//                  path: 'patienteligibilityview', component: DomainLayoutCmp, data: {'domain' : 'patienteligibilityview', 'layout':'caseoverviewlayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                  path: 'assignmenttaskview', component: DomainLayoutCmp, data: {'domain' : 'assignmenttaskview','layout':'caseoverviewlayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                   path: ':id', component: PageContent,
//                   resolve: { page: PageResolver }
//                },
//                {
//                  path: 'phq2Assessmentview', component: DomainLayoutCmp, data: {'domain' : 'phq2Assessmentview','layout':'caseoverviewlayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                  path: 'diabetesAssessmentview', component: DomainLayoutCmp, data: {'domain' : 'diabetesAssessmentview','layout':'caseoverviewlayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                { 
//                  path: 'caseassignmentview', component: DomainLayoutCmp, data: {'domain' : 'caseassignmentview','layout':'caseoverviewlayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                { path: '', component: PageNotfoundComponent }
//             ]
//            },
//            {
//              path: 'patientenrollmentview', component: DomainLayoutCmp, data: {'domain' : 'patientenrollmentview', 'layout':'caseoverviewlayout'},
//              children: [
//                {
//                  path: ':id', component: PageContent,
//                  resolve: { page: PageResolver }
//                },
//                { path: '', component: PageNotfoundComponent }
//              ]
//            },
//            { 
//              path: 'caseassignmentview', component: DomainLayoutCmp, data: {'domain' : 'caseassignmentview', 'layout':'caseoverviewlayout'},
//              children: [
//                {
//                  path: ':id', component: PageContent,
//                  resolve: { page: PageResolver }
//                },
//                { path: '', component: PageNotfoundComponent }
//              ]
//            }
//          ]
//        },
//        { path: 'auth', component: LandingPage },
//        { path: '', redirectTo: 'f', pathMatch: 'full' },
//        { path: '**', component: PageNotfoundComponent }
//      ]
//    },
//    {
//      path: 'ltss', component: MainLayoutCmp, data: {'layout': 'home'},
//      children: [
//        {
//          path: 'a', component: FlowWrapper, data: {'domain' : 'ltssdashboard'},
//          children: [
//           {
//             path: ':id', component: PageContent,
//             resolve: { page: PageResolver }
//           },
//           {
//             path: 'cmcaseR', component: DomainLayoutCmp, data: {'domain' : 'cmcaseview', 'layout':'caseoverviewlayout'},
//             children: [
//               {
//                 path: 'patientenrollmentview', component: SubFlowWrapper, data: {'domain' : 'patientenrollmentview'},
//                 children: [
//                   {
//                     path: ':id', component: PageContent,
//                     resolve: { page: PageResolver }
//                   },
//                   { path: '', component: PageNotfoundComponent }
//                 ]
//               },
//               {
//                  path: 'patienteligibilityview', component: DomainLayoutCmp, data: {'domain' : 'patienteligibilityview', 'layout':'caseoverviewlayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                  path: 'assignmenttaskview', component: DomainLayoutCmp, data: {'domain' : 'assignmenttaskview','layout':'caseoverviewlayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                   path: ':id', component: PageContent,
//                   resolve: { page: PageResolver }
//                },
//                {
//                  path: 'phq2Assessmentview', component: DomainLayoutCmp, data: {'domain' : 'phq2Assessmentview','layout':'caseoverviewlayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                {
//                  path: 'diabetesAssessmentview', component: DomainLayoutCmp, data: {'domain' : 'diabetesAssessmentview','layout':'caseoverviewlayout'},
//                  children: [
//                    {
//                      path: ':id', component: PageContent,
//                      resolve: { page: PageResolver }
//                    },
//                    { path: '', component: PageNotfoundComponent }
//                  ]
//                },
//                { path: '', component: PageNotfoundComponent }
//             ]
//            },
//            {
//              path: 'patientenrollmentview', component: DomainLayoutCmp, data: {'domain' : 'patientenrollmentview', 'layout':'caseoverviewlayout'},
//              children: [
//                {
//                  path: ':id', component: PageContent,
//                  resolve: { page: PageResolver }
//                },
//                { path: '', component: PageNotfoundComponent }
//              ]
//            }
//          ]
//        },
//        { path: 'auth', component: LandingPage },
//        { path: '', redirectTo: 'f', pathMatch: 'full' },
//        { path: '**', component: PageNotfoundComponent }
//      ]
//    },
//    {
//        path: 'hw', component: MainLayoutCmp, data: {},
//        children: [
//         {
//             path: 'a', component: FlowWrapper, data: {'domain': 'helloworlddashboard' },
//             children: [
//                 {
//                     path: ':id', component: PageContent,
//                     resolve: { page: PageResolver }
//                 }
//             ]
//         },
//         { path: 'auth', component: LandingPage },
//         { path: '', redirectTo: 'a', pathMatch: 'full' },
//         { path: '**', component: PageNotfoundComponent }
//        ]
//     },
    { path: '', redirectTo: 'user', pathMatch: 'full' }

];

@NgModule({
    imports: [ RouterModule.forRoot(APPROUTES, {enableTracing: false}) ],
    exports: [ RouterModule ],
    providers: [
      SelectivePreloadingStrategy, PageResolver, LayoutResolver
    ]
})
export class AppRoutingModule {}
