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
import { PageService } from '../../../services/page.service';
import { GenericDomain } from '../../../model/generic-domain.model';
import { Param } from '../../../shared/param-state';
import { ComponentTypes } from '../../../shared/param-annotations.enum';
import { BaseLabel } from './../base-label.component';
import { WebContentSvc } from './../../../services/content-management.service';
import { LabelConfig } from '../../../shared/param-config';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-card-details-grid',
    templateUrl: './card-details-grid.component.html'
})

export class CardDetailsGrid extends BaseLabel {

    @Input() grid: Param;
    public componentTypes = ComponentTypes;
    public labelConfig: LabelConfig;

    constructor(private pageSvc : PageService, private wcsv: WebContentSvc) {
        super(wcsv);
    }

    ngOnInit() {
        super.ngOnInit();
        this.labelConfig = this.wcsv.findLabelContent(this.grid);        
        if (this.grid.config.uiStyles.attributes.onLoad === true) {
            this.pageSvc.processEvent(this.grid.path, '$execute', new GenericDomain(), 'GET');
        }
    }
}
