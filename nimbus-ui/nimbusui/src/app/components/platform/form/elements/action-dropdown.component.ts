import { ElementModelParam, LabelConfig } from './../../../../shared/app-config.interface';
import { Behavior } from './../../../../shared/command.enum';
import { Component, Input } from '@angular/core';
import { WebContentSvc } from '../../../../services/content-management.service';
import { PageService } from '../../../../services/page.service';
import { Param } from '../../../../shared/app-config.interface';
import { GenericDomain } from './../../../../model/generic-domain.model';
import { HttpMethod } from '../../../../shared/command.enum';
import { BaseElement } from './../../base-element.component';
import { trigger,state,style,transition,animate,keyframes } from '@angular/animations';


@Component({
    selector: 'nm-action-dropdown',
    template: `
    <div class="action-dropdown" >
        <button class="dropdownTrigger" 
            aria-label="action menu" 
            attr.aria-expanded="{{isOpen}}" 
            (click)="toggleOpen($event)">
        </button> 
        <div class="dropdownContent" 
            [ngClass]="{'displayNone': isHidden}" 
            [@dropdownAnimation]='state' 
            (@dropdownAnimation.start)="animationStart($event)" 
            (@dropdownAnimation.done)="animationDone($event)" 
            attr.aria-hidden="{{isHidden}}">
            <nm-action-link 
                [elementPath]="elementPath" 
                [rowData]="rowData" 
                [param]="param" 
                *ngFor="let param of params">
            </nm-action-link>
        </div>
    </div>
  `
  ,
  animations: [
    trigger('dropdownAnimation', [
        state('openPanel', style({
            maxHeight: '300px',
        })),
        state('closedPanel', style({
            maxHeight: '0',
        })),
        
        transition('closedPanel => openPanel', animate('600ms ease-in')),
        transition('openPanel => closedPanel', animate('300ms ease-out')),
     ]),
]
})
export class ActionDropdown {

    @Input() params: ElementModelParam[];
    @Input() elementPath: string;
    @Input() rowData: any;
    isOpen: boolean = false;
    isHidden: boolean = true;
    state: string = "closedPanel";
    constructor(private _wcs: WebContentSvc, private pageSvc: PageService) {
    }

    ngOnInit() {
        // console.log(this.params);
        // console.log(this.elementPath + '/');
    }

  
    toggleOpen( event: MouseEvent ): void {
        event.preventDefault();
        this.isOpen = !this.isOpen;
        if(this.state == 'openPanel'){
            this.state = 'closedPanel';
        }
        else{
            this.state = 'openPanel';
        }
    }
    processOnClick(linkCode: string) {
        let item: GenericDomain = new GenericDomain();
        this.pageSvc.processEvent(this.elementPath + '/' + linkCode, Behavior.execute.value, item, HttpMethod.GET.value);
    }
    animationStart($event) {
        this.isHidden = false;
    }
    animationDone($event) {
        if(this.isOpen == false){
            this.isHidden = true;
        }
    }
    
}

@Component({
    selector: 'nm-action-link',
    providers: [
        WebContentSvc
    ],
    template: `
        <ng-template [ngIf]="param.uiStyles.attributes.value =='EXTERNAL'">
            <a href="{{url}}" class="{{param.uiStyles?.attributes?.cssClass}}" target="{{param.uiStyles?.attributes?.target}}" rel="{{param.uiStyles?.attributes?.rel}}">{{label}}</a>
        </ng-template>
        <ng-template [ngIf]="param.uiStyles.attributes.value !='EXTERNAL'">
            <a href="javascript:void(0)" (click)="processOnClick(this.param.code)">{{label}}</a>
        </ng-template>
    `
})
export class ActionLink extends BaseElement{
    
        @Input() param: ElementModelParam;
        @Input() elementPath: string;
        @Input() rowData: any;
        protected url:string;
        
        constructor(private _wcs: WebContentSvc, private pageSvc: PageService) {
            super(_wcs);
        }
    
        ngOnInit() {
            this.loadLabelConfigByCode(this.param.code, this.param.labelConfigs);

            // replace parameters in url enclosed within {}
            if (this.param.uiStyles && this.param.uiStyles.attributes && this.param.uiStyles.attributes.url) {
                this.url = this.param.uiStyles.attributes.url;
                let urlParams: string[] = this.getAllURLParams(this.param.uiStyles.attributes.url);
                if (urlParams && urlParams.length > 0) {
                    if(urlParams!=null) {
                        for (let urlParam of urlParams) {
                            let p = urlParam.substring(1, urlParam.length-1);
                            if (this.rowData[p]) {
                                this.url = this.url.replace(new RegExp(urlParam, 'g'), this.rowData[p]);
                            }
                        }
                    }
                }
            }
            console.log(this.url);
        }

        getAllURLParams (url: string): string[] {
            var pattern = /{([\s\S]*?)}/g;
            return url.match(pattern);
        }
    
        processOnClick(linkCode: string) {
            let item: GenericDomain = new GenericDomain();
            this.pageSvc.processEvent(this.elementPath + '/' + linkCode, Behavior.execute.value, item, HttpMethod.GET.value);
        }

    }


