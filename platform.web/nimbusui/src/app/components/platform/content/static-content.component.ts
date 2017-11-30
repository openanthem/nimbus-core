'use strict';
import { Component, Input } from '@angular/core';
import { Param } from '../../../shared/app-config.interface';
import { WebContentSvc } from '../../../services/content-management.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'nm-static-text',
  providers: [WebContentSvc],
  template: `
    <div class="col-sm-12" [innerHTML]="htmlContent"></div>
   `
})
export class StaticText {
    @Input() element: Param;
    private _htmlContent: string;

    constructor(private wcs: WebContentSvc, private _sanitizer: DomSanitizer) {
         wcs.content$.subscribe(result => {
            this._htmlContent = result.label;
        });
    }

    public get htmlContent() : SafeHtml {
       return this._sanitizer.bypassSecurityTrustHtml(this._htmlContent);
    }

    ngOnInit() {
        this.wcs.getContent(this.element.config.uiStyles.attributes.contentId);
    }
}
