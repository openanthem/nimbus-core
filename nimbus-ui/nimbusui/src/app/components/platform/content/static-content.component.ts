'use strict';
import { Component, Input } from '@angular/core';
import { Param } from '../../../shared/app-config.interface';
import { WebContentSvc } from '../../../services/content-management.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { BaseElement } from '../base-element.component';

@Component({
  selector: 'nm-static-text',
  providers: [WebContentSvc],
  template: `
    <div class="col-sm-12" [innerHTML]="label"></div>
   `
})
export class StaticText extends BaseElement{
    @Input() element: Param;
    private _htmlContent: string;

    constructor(private _wcs: WebContentSvc, private _sanitizer: DomSanitizer) {
         super(_wcs);
    }

    public get htmlContent() : SafeHtml {
       return this._sanitizer.bypassSecurityTrustHtml(this._htmlContent);
    }

    ngOnInit() {
        super.ngOnInit();
    }
}
