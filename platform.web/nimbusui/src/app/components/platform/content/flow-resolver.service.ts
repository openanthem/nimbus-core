import { PageService } from '../../../services/page.service';
import { Param, Page } from '../../../shared/app-config.interface';
import { Injectable } from '@angular/core';
import { ActivatedRoute, Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class FlowResolver implements Resolve<Param> {
    page : Page;

    constructor(private _pageSvc: PageService, private _route: ActivatedRoute, private _router: Router) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<Param> {
        let flowName = route.params['flow'];

        this._pageSvc.config$.subscribe(result => {
            this.page = result;
            // Navigate to page with pageId
            let toPage = this.page.pageConfig.config.code;
            let parentRoute = this.page.pageConfig.config.uiStyles.attributes.route;
            if (parentRoute) {
                toPage = parentRoute + '/' + toPage;
            }
            //this._router.navigate([toPage], { queryParams: { flow: this.page.flow }, relativeTo: this._route.parent.parent });
            this._router.navigate([toPage], { relativeTo: this._route });
        });

        // Check if this is a new flow to be loaded.
        if (this._pageSvc.getFlowConfig(flowName) === undefined) {
            this._pageSvc.loadFlowConfig(flowName);
        } else { // load page from pre loaded config
            this._pageSvc.loadDefaultPageForConfig(flowName);
        }

        return null;
    }
}
