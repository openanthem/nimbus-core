import { TestBed, async } from '@angular/core/testing';

import { GridMouseEventDirective } from './gridhover.directive';
import { GridService } from '../services/grid.service';
import { Directive, ElementRef, Input } from '@angular/core';

class MockGridService { }

class MockElementRef {
    nativeElement = {
        style: {
            backgroundColor: ''
        }
    };
}

let directive, elementRef, gridService;

describe('GridMouseEventDirective', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
          declarations: [
            GridMouseEventDirective
           ],
           providers: [
               {provide: GridService, useClass: MockGridService},
               {provide: ElementRef, useClass: MockElementRef}
            ]
        }).compileComponents();
        elementRef = TestBed.get(ElementRef);
        gridService = TestBed.get(GridService);
        directive = new GridMouseEventDirective(elementRef, gridService)
      });

  it('should create an instance', () => {
    expect(directive).toBeTruthy();
  });

  it('onMouseEnter() should call highlight()', () => {
    spyOn(directive, 'highlight').and.callThrough();
    directive.onMouseEnter({});
    expect(directive.highlight).toHaveBeenCalled();
  });

  it('onMouseLeave() should call highlight() and overlayPanel.hide()', () => {
    directive.overlayPanel = { hide: () => {} };
    spyOn(directive, 'highlight').and.callThrough();
    spyOn(directive.overlayPanel, 'hide').and.callThrough();
    directive.onMouseLeave();
    expect(directive.highlight).toHaveBeenCalled();
    expect(directive.overlayPanel.hide).toHaveBeenCalled();
  });

  it('highlight() should update elementRef.nativeElement.style.backgroundColor', () => {
    directive.highlight('black');
    expect(elementRef.nativeElement.style.backgroundColor).toEqual('black');
  });

});