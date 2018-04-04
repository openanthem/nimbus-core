/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

import { Component, Input } from '@angular/core';
import { WebContentSvc } from './../../services/content-management.service';
import { BaseElement } from './base-element.component';
import { PageService } from '../../services/page.service';
import { GenericDomain } from './../../model/generic-domain.model';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes Tile is a container. Tile(s) go on a Page.
 * 
 * \@howToUse 
 * @Tile annotation from Configuration drives this. 
 *      Attribute "size = {XSmall, Small, Medium, Large}" drives the size of the Tile.
 *      Attribute "imgSrc" - displays an image on the tile header bar next to the title.
 * <nm-tile [element]="element" tileType="subtile"></nm-tile>
 * 
 */
@Component({
    selector: 'nm-tile',
    styles: [`
        .hide {
        display: none;
        },
        `
    ],
    providers: [
        WebContentSvc
    ],
    templateUrl: './tile.component.html'
})

export class Tile extends BaseElement {
    // Optional tileType = 'subcard' is used for indicating it is a sub-tile. This drives the style of nested tile.
    @Input() tileType?: string;

    // width of tile
    public styleWd: string = 'col-lg-12';

    // height of tile
    public styleHt: string = 'height-lg';

    constructor(private wcsvc: WebContentSvc, private pageSvc: PageService) {
        super(wcsvc);
    }

    ngOnInit() {
        super.ngOnInit();
        // Determine the Tile size based on "size" attribute.
        if (this.element.config.uiStyles.attributes.size === 'XSmall') {
            this.styleWd = 'card-holder col-lg-3 col-md-6 XsmallCard';
            this.styleHt = 'height-md';
        } else if (this.element.config.uiStyles.attributes.size === 'Small') {
            this.styleWd = 'col-xl-6 col-sm-6 smallCard';
            this.styleHt = 'height-md';
        } else if (this.element.config.uiStyles.attributes.size === 'Medium') {
            this.styleWd = 'card-holder col-lg-9 col-md-12 mediumCard';
            this.styleHt = 'height-md';
        } else if (this.element.config.uiStyles.attributes.size === 'Colorbox') {
            this.styleWd = 'my-card-holder col-xs-12 colorBox';
            this.styleHt = 'my-card-block ';
        } else {
            this.styleWd = '';
            this.styleHt = '';
        }
        // SubTile (nested tile) style override
        if (this.tileType === 'subcard') {
            this.styleWd = this.styleWd + ' subcard';
        }
        // Check for initialization
        if (this.element.config && this.element.config.initializeComponent()) {
            this.pageSvc.processEvent(this.element.path, '$execute', new GenericDomain(), 'POST');
        }
    }
}

