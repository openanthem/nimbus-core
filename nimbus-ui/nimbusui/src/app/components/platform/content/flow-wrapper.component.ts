'use strict';

import { ActivatedRoute, Router } from '@angular/router';

import { Component } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Message } from 'stompjs';

import { PageService } from '../../../services/page.service';
import { STOMPService } from '../../../services/stomp.service';
import { ExecuteOutput, ModelEvent, Page } from '../../../shared/app-config.interface';

@Component({
    selector: 'nm-flow',
    providers: [STOMPService],
    templateUrl: './flow-wrapper.component.html'
})

export class FlowWrapper {
    page : Page;
    routeParams: any;
    // Stream of messages
    public messages: Observable<Message>;

    constructor(private _pageSvc: PageService, private _stompService: STOMPService, private _route: ActivatedRoute, private _router: Router) {
        // Get the Flow Domain
        _route.data.subscribe( data => {
                this.routeParams = data;
        });

        this.initWebSocket();
    }

    ngOnInit() {
        this._pageSvc.config$.subscribe(result => {
            this.page = result;
            // Navigate to page with pageId
            let toPage = this.page.pageConfig.config.code;
            let parentRoute = this.page.pageConfig.config.uiStyles.attributes.route;
            if (parentRoute) {
                toPage = parentRoute + '/' + toPage;
            }
//            document.getElementById("main-content").classList.remove("withInfoBar");
            //this._router.navigate([toPage], { queryParams: { flow: this.page.flow }, relativeTo: this._route.parent.parent });
            this._router.navigate([toPage], { relativeTo: this._route });
        });
        // IF this is a new flow to be loaded.

        if (this._pageSvc.getFlowConfig(this.routeParams['domain']) === undefined) {
            this._pageSvc.loadFlowConfig(this.routeParams);
        } else { // load page from pre loaded config
            this._pageSvc.loadDefaultPageForConfig(this.routeParams['domain']);
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
