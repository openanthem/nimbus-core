import { GridService } from '../services/grid.service';
import { OverlayPanel } from 'primeng/primeng';
import { Directive, ElementRef, Input } from '@angular/core';

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
  constructor(el: ElementRef,private gridService: GridService) { this.el = el.nativeElement; }

  onMouseEnter(event:MouseEvent) {
    this.highlight(this.highlightColor || this._defaultColor);
    //TODO ------- url to come from server config
    let url = 'resources/case-data.json';
    this.gridService.getSummaryDetails(this.data.rowObj.caseId,url).subscribe((tasks) => {
        if(this.data.col.hover || this.data.rowhover) {
            this.gridService.setSummaryObject(tasks.find(p => p.caseId === this.data.rowObj.caseID ));
            //TODO ---------- pass event.target. Currently it is showing the overlay at the bottom of grid.
            this.overlayPanel.show(event);
        } else {
            this.overlayPanel.hide();
        }
    });
  }
  onMouseLeave() {
      this.highlight(null);
      this.overlayPanel.hide();
  }

   private highlight(color:string) {
    this.el.style.backgroundColor = color;
  }

}
