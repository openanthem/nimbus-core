import { BreadcrumbService } from './../breadcrumb/breadcrumb.service';
import { PageService } from '../../../services/page.service';
import { Param } from '../../../shared/app-config.interface';
import { Injectable } from '@angular/core';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class PageResolver implements Resolve<Param> {

    constructor(
        private _pageSvc: PageService, 
        private _router: Router,
        private _breadcrumbService: BreadcrumbService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<Param> {
        let pageId = route.params['pageId'];
        //let flow = route.parent.data['domain'];
        let flow = route.parent.url[0]['path'];
        return this._pageSvc.getPageConfigById(pageId, flow).then(page => {
            if (page) {

                // Push the home breadcrumb into memory under the domain name.
                this._breadcrumbService.push(page.config.code, page.config.code, page.path);
                
                return page;
            } else { // page not found
                this._router.navigate([`/h/${flow}`]);
                return null;
            }
        });
    }
}
