import { Component, Input } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

import { LayoutService } from '../../../services/layout.service';

@Component({
  selector: 'svg-definitions',
  templateUrl: './svg-definitions.component.html'
})
export class SvgDefinitions {
  definitions: any;

  constructor(
    private layoutService: LayoutService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit() {
    this.layoutService.getDefinitions().subscribe((val) => {
      this.definitions = this.sanitizer.bypassSecurityTrustHtml(val);
    });
  }

}