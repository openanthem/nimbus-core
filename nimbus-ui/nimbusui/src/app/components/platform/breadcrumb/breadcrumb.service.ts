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

import { Injectable, Inject } from '@angular/core';
import {
  ActivatedRoute,
  Router,
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import { ServiceConstants } from './../../../services/service.constants';
import { PageService } from './../../../services/page.service';
import { Breadcrumb } from '../../../model/breadcrumb.model';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from './../../../services/session.store';

/**
 * \@author Tony.Lopez
 * \@author Dinakar.Meda
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Injectable()
export class BreadcrumbService {
  private static BREADCRUMBKEY: string = 'breadcrumbs';
  //private _homePageId: string = '';

  constructor(
    private _pageService: PageService,
    @Inject(CUSTOM_STORAGE) private sessionstore: SessionStoreService
  ) {}

  public addCrumb(pageId: string, label: string, path: string): Breadcrumb {
    let crumb = {
      id: pageId,
      label: label,
      params: null,
      url: `${path}`
    } as Breadcrumb;
    return crumb;
  }

  public addBreadCrumb(cr: Breadcrumb): Breadcrumb {
    let crumb = {
      id: cr.id,
      label: cr.label,
      params: null,
      url: cr.url
    } as Breadcrumb;
    return crumb;
  }

  public push(pageId: string, label: string, path: string): boolean {
    /** Push the first crumb as home crumb */
    let bCrumbs = this.sessionstore.get(BreadcrumbService.BREADCRUMBKEY);
    if (bCrumbs == undefined) {
      let breadCrumbs: any = {};
      breadCrumbs['homePage'] = pageId;
      let crumbs: any = {};
      breadCrumbs['crumbs'] = crumbs;
      crumbs[pageId] = this.addCrumb(pageId, label, path);
      this.sessionstore.set(BreadcrumbService.BREADCRUMBKEY, breadCrumbs);
    } else {
      // Place the crumb in the right place
      let tempCrumbs = {};
      // Does the crumb already exist?
      if (bCrumbs.crumbs && bCrumbs.crumbs[pageId]) {
        // Add up to this crumb and remove rest
        for (let p in bCrumbs.crumbs) {
          let crumb = bCrumbs.crumbs[p];
          tempCrumbs[crumb['id']] = crumb;
          if (crumb['id'] == pageId) {
            break;
          }
        }
        bCrumbs['crumbs'] = tempCrumbs;
      } else {
        // Add this crumb to the end;
        bCrumbs.crumbs[pageId] = this.addCrumb(pageId, label, path);
      }
      this.sessionstore.set(BreadcrumbService.BREADCRUMBKEY, bCrumbs);
    }
    return false;
  }

  public getAll(): Breadcrumb[] {
    let bCrumbs = this.sessionstore.get(BreadcrumbService.BREADCRUMBKEY);
    if (bCrumbs) {
      return bCrumbs.crumbs;
    } else {
      return undefined;
    }
  }

  public getByPageId(pageId: string): Breadcrumb {
    let bCrumbs = this.sessionstore.get(BreadcrumbService.BREADCRUMBKEY);
    if (bCrumbs && bCrumbs.crumbs) {
      return bCrumbs.crumbs[pageId];
    } else {
      return undefined;
    }
  }

  /** Return home route url */
  public getHomeBreadcrumb(): Breadcrumb {
    let bCrumbs = this.sessionstore.get(BreadcrumbService.BREADCRUMBKEY);
    if (bCrumbs && bCrumbs.crumbs) {
      return bCrumbs.crumbs[bCrumbs.homePage];
    } else {
      return undefined;
    }
  }

  /**
   * Returns true if the provided route is configured as the HOME route in @ViewRoot.
   * @param route the route to check
   */
  public isHomeRoute(route: ActivatedRoute) {
    let bCrumbs = this.sessionstore.get(BreadcrumbService.BREADCRUMBKEY);
    if (bCrumbs && bCrumbs.crumbs) {
      return route.firstChild.snapshot.params['pageId'] === bCrumbs.homePage;
    } else {
      return false;
    }
  }
}
