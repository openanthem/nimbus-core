/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

import { Injectable, Type } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';

@Injectable()
export class RouteService {
  constructor(private activatedRoute: ActivatedRoute, private router: Router) {}

  getRoutes() {
    return this.router.config;
  }

  /** The route name should be unique across the application. The name cannot repeat even for a child. */
  addRoute(route: any) {
    // Check if the route exists before adding
    let routes = this.getRoutes();
    let matchedRoute: Route = this.searchRoutes(routes, route.path);
    if (matchedRoute == null) {
      routes.push(route);
      this.router.resetConfig(routes);
    }
  }

  /** Search and return a route if it already exists */
  searchRoutes(routeList: Route[], matchRoute: String): Route {
    let route: Route = null;
    for (let p = 0; p < routeList.length; p++) {
      let element = routeList[p];
      if (element.path === matchRoute) {
        route = element;
        break;
      } else if (element.children != null) {
        route = this.searchRoutes(element.children, matchRoute);
      }
    }
    return route;
  }

  searchAndReplace(routeList: Route[], matchRoute: Route) {
    for (let p = 0; p < routeList.length; p++) {
      if (routeList[p].path === matchRoute.path) {
        routeList[p] = matchRoute;
        break;
      } else if (routeList[p].children != null) {
        this.searchAndReplace(routeList[p].children, matchRoute);
      }
    }
  }
  /** Add child route. First, find the parent. */
  addRouteChildren(route, parentPath) {
    let configRoutes = this.getRoutes();
    configRoutes[0].children[0].children.push(route);
    // let matchedRoute: Route = this.searchRoutes(configRoutes, parentPath);
    // if (matchedRoute == null) {
    //     throw new Error('No matching route found');
    // } else {
    //     if (matchedRoute.children == null) {
    //         matchedRoute.children = [];
    //         matchedRoute.children.push(route);
    //     } else {
    //         matchedRoute.children.push(route);
    //     }
    //     this.searchAndReplace(configRoutes, matchedRoute);
    // }
    this.router.resetConfig(configRoutes);
  }

  /** Get the Component to load using Reflection */
  getComponent(component: string): Type<any> {
    let cmp = null;
    // const annotations = (<any>AppModule).__annotations__;
    // annotations[0].declarations.forEach(element => {
    //     if (element.name === component) {
    //         cmp = element;
    //     }
    // });
    // annotations[0].providers.forEach(element => {
    //     if (element.name === component) {
    //         cmp = element;
    //     }
    // });
    return cmp;
  }

  // /** Get the Component to load using Reflection */
  // getService(service: string): any {
  //     let svc = null;
  //     const annotations = (<any>AppRoutingModule).__annotations__;
  //     annotations[0].providers.forEach(element => {
  //         if (element.name === service) {
  //             svc = element;
  //         }
  //     });
  //     return svc;
  // }
}
