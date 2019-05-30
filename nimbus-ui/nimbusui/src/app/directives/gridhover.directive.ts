/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

import { Directive, ElementRef, Input } from '@angular/core';
import { OverlayPanel } from 'primeng/primeng';
import { GridService } from '../services/grid.service';

@Directive({
  selector: '[gridMouseEvents]',
  host: {
    '(mouseenter)': 'onMouseEnter($event)',
    '(mouseleave)': 'onMouseLeave()'
  }
})
export class GridMouseEventDirective {
  @Input('gridMouseEvents') overlayPanel: OverlayPanel;
  @Input('myOptions') data: any;

  private _defaultColor = 'blue';
  private el: HTMLElement;
  private highlightColor: string;
  constructor(el: ElementRef, private gridService: GridService) {
    this.el = el.nativeElement;
  }

  onMouseEnter(event: MouseEvent) {
    this.highlight(this.highlightColor || this._defaultColor);
    //TODO ------- url to come from server config
    let url = 'resources/case-data.json';
    // TODO - Remove this unused code. Replace with actual implementation.
    /*  this.gridService.getSummaryDetails(this.data.rowObj.caseId,url).subscribe((tasks) => {
        if(this.data.col.hover || this.data.rowhover) {
            this.gridService.setSummaryObject(tasks.find(p => p.caseId === this.data.rowObj.caseID ));
            //TODO ---------- pass event.target. Currently it is showing the overlay at the bottom of grid.
            this.overlayPanel.show(event);
        } else {
            this.overlayPanel.hide();
        }
    });*/
  }
  onMouseLeave() {
    this.highlight(null);
    this.overlayPanel.hide();
  }

  private highlight(color: string) {
    this.el.style.backgroundColor = color;
  }
}
