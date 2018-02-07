import { BaseElement } from './base-element.component';
import { Component, Input } from '@angular/core';
import { Param } from '../../shared/app-config.interface';
import { WebContentSvc } from '../../services/content-management.service';
import { PageService } from '../../services/page.service';
import { GenericDomain } from './../../model/generic-domain.model';
import { ServiceConstants } from './../../services/service.constants';

@Component({
    selector: 'nm-link',
    providers: [
        WebContentSvc
    ],
    template: `
        <ng-template [ngIf]="imgSrc && imgSrc !== ''">
            <a href="{{url}}" class='{{cssClass}}'>
                <img src="{{imagesPath}}{{imgSrc}}" class=" logo" alt="{{this.label}}" />
            </a>
        </ng-template>
        <ng-template [ngIf]="!imgSrc || imgSrc === ''">

            <!-- External Links -->
            <ng-template [ngIf]="value=='EXTERNAL'">
                <a href="{{url}}" class="{{cssClass}}" target="{{target}}" rel="{{rel}}">{{label}}</a>
            </ng-template>

            <ng-template [ngIf]="value=='MENU'">
                <ng-template [ngIf]="element.mapped==false">
                    <a href="javascript:void(0)" class=" mr-2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                       
                        <svg class="">
                            <use xmlns:xlink="http://www.w3.org/1999/xlink" attr.xlink:href="{{imagesPath}}{{imgSrc}}#Layer_1"></use> 
                        </svg>
                    </a>
                    
                </ng-template>
                <ng-template [ngIf]="element.mapped==true">
                    <a href="javascript:void(0)" class=" mr-2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <ng-template ngFor let-menuParam [ngForOf]="element?.type?.model?.params">
                            <ng-template [ngIf]="menuParam.mapped == true">
                                <div class='sizeTarget'>{{menuParam.leafState}}</div>
                            </ng-template>
                        </ng-template>
                    </a>
                </ng-template>
                <div class="dropdown-menu dropdown-menu-right">
                    <ng-template ngFor let-menuParam [ngForOf]="element?.type?.model?.params">
                        <ng-template [ngIf]="menuParam.config?.uiStyles?.attributes?.alias == 'Menu'">
                            <ng-template ngFor let-linkParam [ngForOf]="menuParam?.type?.model?.params">
                                <nm-link [element]="linkParam" inClass="dropdown-item"></nm-link>
                            </ng-template>
                        </ng-template>
                    </ng-template>
                </div>
            </ng-template>
            <ng-template [ngIf]="value=='DEFAULT'">
                <a class="{{cssClass}}" href="javascript:void(0)" (click)="processOnClick(element.path, method, b)">
                    {{this.label}}
                </a>
            </ng-template>
        </ng-template>
    `
})

export class Link extends BaseElement {

    @Input() element: Param;
    @Input() root: Param;
    @Input() inClass: string;
    private linkClass: string = 'basicView';
    private imagesPath: string;

    constructor(private _wcs: WebContentSvc, private pageSvc: PageService) {
        super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();
        //console.log(this.element.config.uiStyles.attributes.value);
        this.imagesPath = ServiceConstants.IMAGES_URL;
        if (this.inClass) {
            this.linkClass = this.inClass;
        }
    }

    processOnClick(uri: string, httpMethod: string, behaviour: string) {
        let item: GenericDomain = new GenericDomain();
        if(this.root) {
            this.root.type.model.params.forEach(ele => {
                if(ele.path === this.element.path.slice(0,this.element.path.lastIndexOf('/'))+'/id') {
                    item.addAttribute('id', ele.leafState);
                }
            });
        }
        
        this.pageSvc.processEvent(uri, behaviour, item, httpMethod);
    }

    /**
     * The URL attribute for this Param.
     */
    get url(): string {
        return this.element.config.uiStyles.attributes.url;
    }

    /**
     * The value attribute for this Param.
     */
    get value(): string {
        return this.element.config.uiStyles.attributes.value;
    }

    /**
     * The method attribute for this Param.
     */
    get method(): string {
        return this.element.config.uiStyles.attributes.method;
    }

    /**
     * The b attribute for this Param.
     */
    get b(): string {
        return this.element.config.uiStyles.attributes.b;
    }

    /**
     * The target attribute for this Param.
     */
    get target(): string {
        return this.element.config.uiStyles.attributes.target;
    }

    /**
     * The rel attribute for this Param.
     */
    get rel(): string {
        return this.element.config.uiStyles.attributes.rel;
    }
}

