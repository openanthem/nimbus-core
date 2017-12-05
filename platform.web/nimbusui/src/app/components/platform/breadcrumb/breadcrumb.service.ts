import { Injectable } from '@angular/core';
import { ActivatedRoute, Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { ServiceConstants } from './../../../services/service.constants';
import { PageService } from './../../../services/page.service';

import { IBreadcrumb } from './ibreadcrumb.d';

@Injectable()
export class BreadcrumbService {

    private _breadcrumbs: any;

    constructor(private _pageService: PageService) {

        this._breadcrumbs = {};
    }

    public push(pageId: string, label: string, path: string): boolean {
        if (!this._breadcrumbs.hasOwnProperty(pageId)) {
            this._breadcrumbs[pageId] = {
                id: pageId,
                label: label,
                params: null,
                url: `${ServiceConstants.HOME_ROUTE}${path}`
            };
            return true;
        }
        return false;
    }

    public getAll(): IBreadcrumb[] {
        return this._breadcrumbs;
    }

    public getByPageId(pageId: string): IBreadcrumb {
        return this._breadcrumbs[pageId];
    }

    // TODO - Need to add this to configuration.
    public getHomeBreadcrumb(): IBreadcrumb {
        return {
            id: ServiceConstants.HOME_DOMAIN,
            label: 'Home',
            params: null,
            url: `${ServiceConstants.HOME_ROUTE}/${ServiceConstants.HOME_DOMAIN}/${ServiceConstants.HOME_PAGE}`
        }
    }

    /**
     * Returns true if the provided route is configured as the HOME route in @ViewRoot.
     * @param route the route to check
     */
    public isHomeRoute(route: ActivatedRoute) {
        // TODO - Obtain this value from configuration.
        return route.firstChild.snapshot.params['pageId'] === ServiceConstants.HOME_PAGE;
    }
}
