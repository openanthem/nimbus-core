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

import { WebContentSvc } from './../../../services/content-management.service';
import { BaseElement } from './../base-element.component';
import { Param } from '../../../shared/param-state';
import { Component, Input } from '@angular/core';
import { PageService } from '../../../services/page.service';
import { ComponentTypes } from '../../../shared/param-annotations.enum';
import { trigger, state, style, transition, animate } from '@angular/animations';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
@Component({
    selector: 'nm-card-details',
    styles: [`
        .hide {
        display: none;
        },
        `
    ],
    templateUrl: './card-details.component.html',
    animations: [
        trigger('accordionAnimation', [
            state('openPanel', style({
                height: '*'
            })),
            state('closedPanel', style({
                height: '0',
            })),
            transition('closedPanel => openPanel', [animate('300ms ease-out')]),
            transition('openPanel => closedPanel', [animate('500ms ease-in')])

        ]),
    ],
    providers: [
        WebContentSvc
    ]
})
export class CardDetailsComponent extends BaseElement {
    @Input() collectionElem: boolean = false;
    @Input() elemId: string = undefined;
    @Input() element: Param;
    @Input() editUrl: string;
    opened: boolean = false;
    componentTypes = ComponentTypes;
    isHidden: boolean = false;
    expandable: boolean = false;
    cardType: string;
    private _state: string = 'openPanel';

    set state(value: string) {
        this._state = value;
    }

    get state() {
        return this._state;
    }

    constructor(private pageSvc: PageService, private _wcs: WebContentSvc) {
        super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();
        this.updatePosition();
        this.setCardConfig();        
    }

    processOnClick() {
        this.pageSvc.processEvent(this.element.path, '$execute', null, 'POST');
    }

    /* look for parameters in URI {} */
    getAllURLParams(uri: string): string[] {
        var pattern = /{([\s\S]*?)}/g;
        return uri.match(pattern);
    }

    toggle() {
        this.opened = !this.opened;
    }

    toggleState() {
        if (this._state == 'closedPanel') {
            this.isHidden = !this.isHidden;
             this._state = 'openPanel';
        }
        else if (this._state == 'openPanel') {
            this._state = 'closedPanel';
        }

    }

    animationStart($event) {
        // this.isHidden = false;
    }

    animationDone($event) {
        if (this._state == 'closedPanel') {
            this.isHidden = true;
        }
    }

    setCardConfig() {
        if (this.element.config.uiStyles.attributes.alias === this.componentTypes.cardDetail.toString() && this.element.config.uiStyles.attributes.expandable) {
            this.expandable = true;
            this.isHidden = true;
            this.state = 'closedPanel';
            this.cardType = 'toggleCard';
        }
        else if (this.element.config.uiStyles.attributes.alias === this.componentTypes.cardDetail.toString() && this.element.config.uiStyles.attributes.border) {
            this.cardType = 'borderCard';
        }
    }

    getFieldClass(parent: Param, child: Param) {
        if (child.config.uiStyles) {
            if (child.config.uiStyles.attributes && child.config.uiStyles.attributes.cssClass && child.config.uiStyles.attributes.cssClass != '') {
                return child.config.uiStyles.attributes.cssClass;
            } else {
                if (parent.config.uiStyles && parent.config.uiStyles.attributes && parent.config.uiStyles.attributes.cssClass) {
                    return parent.config.uiStyles.attributes.cssClass;
                } else {
                    return '';
                }
            }
        } else {
            return '';
        }
    }
}

