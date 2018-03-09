import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { Message } from 'stompjs';
import { Component } from '@angular/core';
import { LayoutService } from '../../services/layout.service';
import { AppBranding, Layout, LinkConfig, FooterConfig } from '../../model/menu-meta.interface';
import { ExecuteOutput, ModelEvent, Param } from '../../shared/app-config.interface';
import { AuthenticationService } from '../../services/authentication.service';
import { STOMPService } from '../../services/stomp.service';
import { PageService } from '../../services/page.service';
import { ServiceConstants } from '../../services/service.constants';
import {FooterGlobal} from '../platform/footer/footer-global.component'
import { MenuItem } from 'primeng/primeng';

@Component({
    templateUrl: './home-layout.component.html',
    providers: [ STOMPService, LayoutService ],
    
})

export class HomeLayoutCmp {
    public leftMenuItems: LinkConfig[];
    public topMenuItems: Param[];
    public branding: AppBranding;
    public footer: FooterConfig;
    public userName: any;
    //TODO: Determine the strategy for global nav
    //public subBar: GlobalNavConfig;
    // public organization: Param;
    // public menus: MenuItem[][];

    public organizations: Param[];
    public collapse: boolean = false;
    public themes: any[] = [];
    // Stream of messages
    public messages: Observable<Message>;
    public organization: Param;
    public menuItems: Map<string, Param[]>;
    public menuLinks: Param[] = [];
    private _activeTheme = '';

    constructor(
        private _authenticationService: AuthenticationService,
        private layoutSvc: LayoutService,
        private _stompService: STOMPService,
        private _pageSvc: PageService,
        private _route: ActivatedRoute,
        private _router: Router) {

        // initialize
        this.themes.push({link:'styles/vendor/anthem.blue.theme.css',label:'Blue Theme'});
        this.themes.push({link:'styles/vendor/anthem.black.theme.css',label:'Black Theme'});

        this.layoutSvc.layout$.subscribe(
            data => {
                let layout: Layout = data;
                if(layout != null ) {
                    if(layout.topBar != null && layout.topBar.branding != null) {
                        this.branding = layout.topBar.branding;
                        this.topMenuItems = layout.topBar.headerMenus;
                    }
                    if(layout.subBar!=null){
                        this.organization = layout.subBar.organization;
                        this.menuItems = layout.subBar.menuItems;
                        this.menuLinks = layout.subBar.menuLinks;
                    }
                    this.leftMenuItems = layout.leftNavBar;
                    this.footer = layout.footer;
                }
                //this._router.navigate([this.body['defaultFlow']], { relativeTo: this._route });
            }
        );
        if(this._route.data['value']['layout'] != null) {
            this.layoutSvc.getLayout(this._route.data['value']['layout']);
        }

//        this.initWebSocket();
    }

    logout() {
        this._authenticationService.logout().subscribe(success => {
            window.location.href=`${ServiceConstants.CLIENT_BASE_URL}logout`;
        });
    }

    // Side navigation bar toggle effect
    toggelSideNav() {
        this.collapse = !this.collapse;
    }

    get activeTheme(): string {
        return this._activeTheme;
    }

    set activeTheme(value: string) {
        if (this._activeTheme !== value) {
            this._activeTheme = value;
            let themeLink = <HTMLLinkElement>document.getElementById('activeThemeLink');
            if (themeLink) {
                themeLink.href = value;
            }
        }
    }

    /** Initialize the WebSocket */
    initWebSocket() {
        this._stompService.configure();
        this._stompService.try_connect().then(this.on_connect);
    }

    /** Cleanup on Destroy of Component */
    ngOnDestroy() {
        this._stompService.disconnect();
    }

    /** Callback on_connect to queue */
    public on_connect = () => {

        // Store local reference to Observable
        // for use with template ( | async )
        this.messages = this._stompService.messages;
        // Subscribe a function to be run on_next message
        this.messages.subscribe(this.on_next);
    }

    /** Consume a message from the _stompService */
    public on_next = (message: Message) => {
        let executeOutput : ExecuteOutput = JSON.parse(message.body);
        let outputModel : any = executeOutput.result;
        let count = 0;
        let _p = this;
        //let foundMatchInCurrentPage: boolean = false;
        //let ROOTNODE = 0;
        // loop thru all the results and process 1 by 1.
        while(outputModel[count]) {
            let eventModel : ModelEvent = outputModel[count].result;
            // Find flow name from eventmodel result path
            let flowName = eventModel.value.path.split('/')[1];
            _p._pageSvc.traverseFlowConfig(eventModel, flowName);

            count ++;
        }
    }

}
