/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */
import { Component, Input, OnInit } from '@angular/core';
import { Param } from '../../shared/app-config.interface';
import { PageService } from '../../services/page.service';
import { WebContentSvc } from './../../services/content-management.service';
import { GenericDomain } from './../../model/generic-domain.model';
import { BaseElement } from './base-element.component';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes Section is a container. Section(s) go within a Tile. Section can have Sub-Sections
 * 
 * \@howToUse 
 * @Section annotation from Configuration drives this. 
 *      Attribute "type".
 *      Attribute "cssClass" - custom css for the section.
 * <nm-section [element]="element"></nm-section>
 * 
 */
@Component({
    selector: 'nm-section',
    templateUrl: './section.component.html'
})
export class Section extends BaseElement implements OnInit {

    constructor(private wcsvc: WebContentSvc, private pageSvc: PageService) {
        super(wcsvc);
    }

    ngOnInit() {
        // Check for initialization
        if (this.element.config && this.element.config.initializeComponent()) {
            this.pageSvc.processEvent(this.element.path, '$execute', new GenericDomain(), 'POST');
        }
    }

}
