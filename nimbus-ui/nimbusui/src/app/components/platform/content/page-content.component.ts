'use strict';
import { ActivatedRoute, Router } from '@angular/router';
import { Component } from '@angular/core';

import { Param } from '../../../shared/app-config.interface';
import { BaseElement } from '../base-element.component';
import { WebContentSvc } from './../../../services/content-management.service';

@Component({
    selector: 'nm-page-content',
    templateUrl: './page-content.component.html'
})

export class PageContent extends BaseElement{
    pageId: string;
    tilesList: any[];
    public pageParam: Param;
    constructor(private router: Router, private route: ActivatedRoute, private _wcs : WebContentSvc) {
        super(_wcs);
        this.router.events.subscribe(path => {
            this.pageId = this.route.snapshot.url[0].path;
        });
    }

    ngOnInit() {
        this.route.data.subscribe((data: { page: Param }) => {
            let page : Param = data.page;
            this.pageParam = page;
            this.loadLabelConfig(this.pageParam);
            this.tilesList = [];
            if(page.type.model != null) {
                page.type.model.params.forEach(element => {
                    if(element.config.uiStyles.attributes.alias === 'Tile') {
                        this.tilesList.push(element);
                    }
                });
            }
        });
    }

}
