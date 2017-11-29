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
        <ng-template [ngIf]="element.config?.uiStyles?.attributes?.imgSrc !== undefined && element.config?.uiStyles?.attributes?.imgSrc !== ''">
            <a href="{{element.config.uiStyles.attributes.url}}" class='{{element.config.uiStyles.attributes.cssClass}}'>
                <img src="{{imagesPath}}{{element.config.uiStyles.attributes.imgSrc}}" class=" logo" alt="{{this.label}}" />
            </a>
        </ng-template>
        <ng-template [ngIf]="element.config?.uiStyles?.attributes?.imgSrc === undefined || element.config?.uiStyles?.attributes?.imgSrc === ''">
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.value=='MENU'">
                <ng-template [ngIf]="element.mapped==false">
                    <a href="javascript:void(0)" class=" mr-2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                       
                        <svg class="">
                            <use xmlns:xlink="http://www.w3.org/1999/xlink" attr.xlink:href="{{imagesPath}}{{element.config.uiStyles.attributes.imgSrc}}#Layer_1"></use> 
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
            <ng-template [ngIf]="element.config?.uiStyles?.attributes?.value=='DEFAULT'">
                <a class="{{linkClass}}" href="javascript:void(0)" (click)="processOnClick(element.path, 
                    element.config?.uiStyles?.attributes?.method, element.config?.uiStyles?.attributes?.b)">
                    {{this.label}}
                </a>
            </ng-template>
        </ng-template>
    `
})

export class Link {

    @Input() element: Param;
    @Input() root: Param;
    @Input() inClass: string;
    private label: string;
    private linkClass: string = 'basicView';
    private imagesPath: string;

    constructor(private wcs: WebContentSvc, private pageSvc: PageService) {
        wcs.content$.subscribe(result => {
            this.label = result.label;
        });
    }

    ngOnInit() {
        //console.log(this.element.config.uiStyles.attributes.value);
        this.imagesPath = ServiceConstants.IMAGES_URL;
        this.wcs.getContent(this.element.config.code);
        if (this.inClass) {
            this.linkClass = this.inClass;
        }
    }

    processOnClick(uri: string, httpMethod: string, behaviour: string) {
        let item: GenericDomain = new GenericDomain();
        this.root.type.model.params.forEach(ele => {
            if(ele.path === this.element.path.slice(0,this.element.path.lastIndexOf('/'))+'/id') {
                item.addAttribute('id', ele.leafState);
            }
        });
        this.pageSvc.processEvent(uri, behaviour, item, httpMethod);
    }
}

