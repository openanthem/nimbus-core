import { Component, ViewEncapsulation, Inject } from '@angular/core';
import { HostListener } from '@angular/core/src/metadata/directives';
import { DomainLayoutCmp } from './components/domain/domain-layout.component';
import { WebContentSvc } from './services/content-management.service';
import { ServiceConstants } from './services/service.constants';
import { Router, ActivatedRoute } from '@angular/router';
import { WindowService } from './services/window-resize.service';
import { DOCUMENT } from '@angular/platform-browser';

@Component({
    selector: 'app-root',
    encapsulation: ViewEncapsulation.None,
    templateUrl: './app.component.html',
    providers: [ WindowService ]
})

export class AppComponent {
    domain: string;
    port : string;
    protocol: string;
    locale : string;
    label: string;
    private collapse: boolean = false;

    constructor(private router: Router,
            private activatedRoute: ActivatedRoute, private windowService: WindowService, 
            @Inject(DOCUMENT) private document: any) {
        // subscribe to the window resize event's height
        windowService.height$.subscribe((value:any) => {
        });
        // subscribe to the window resize event's width
        windowService.width$.subscribe((value:any) => {
        });
    }

   ngOnInit() {
        this.domain = this.document.location.hostname;
        this.port=this.document.location.port;
        this.protocol=this.document.location.protocol;
        this.locale = "en-US"; //TODO This locale should be read dynamically. Currently defaulting to en-US
        ServiceConstants.STOPGAP_APP_HOST = this.domain;
        ServiceConstants.STOPGAP_APP_PORT = this.port;
        ServiceConstants.LOCALE_LANGUAGE = this.locale;
        ServiceConstants.STOPGAP_APP_PROTOCOL = this.protocol;
    }

}
