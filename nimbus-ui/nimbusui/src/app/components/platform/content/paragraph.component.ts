/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */
'use strict';
import { Component, Input } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { WebContentSvc } from '../../../services/content-management.service';
import { Param } from '../../../shared/app-config.interface';
import { BaseElement } from '../base-element.component';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes Equivalent to <P> tag in HTML. Content specified in this tag will be displayed within a <P>
 * tag on the HTML page.
 * 
 * \@howToUse 
 * @Paragraph annotation from Configuration drives this.
 * <nm-paragraph [element]="element"></nm-paragraph>
 * 
 */

@Component({
  selector: 'nm-paragraph',
  providers: [
      WebContentSvc
  ],
  template: `
      <p *ngIf="visible == true" [class]="cssClass" [innerHTML]="htmlContent"></p>
   `
})
export class Paragraph extends BaseElement {

    private _htmlContent: string;

    constructor(private wcsvc: WebContentSvc, private _sanitizer: DomSanitizer) {
        super(wcsvc);
    }
    
    public get htmlContent() : SafeHtml {
        return this._sanitizer.bypassSecurityTrustHtml(this.label);
    }

}
