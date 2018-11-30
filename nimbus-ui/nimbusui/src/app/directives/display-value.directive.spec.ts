import { TestBed, async } from '@angular/core/testing';

import { DisplayValueDirective } from './display-value.directive';
import { ElementRef, Renderer2 } from '@angular/core';

class MockElementRef {
    nativeElement = {
        style: {
            backgroundColor: ''
        }
    };
}

class MockRenderer {
    addClass(a, b) {    }
    removeClass(a, b) { }
}

let directive, elementRef, renderer;

describe('DisplayValueDirective', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
          declarations: [
            DisplayValueDirective
           ],
           providers: [
               {provide: ElementRef, useClass: MockElementRef},
               {provide: Renderer2, useClass: MockRenderer}
            ]
        }).compileComponents();
        elementRef = TestBed.get(ElementRef);
        renderer = TestBed.get(Renderer2);
        directive = new DisplayValueDirective(elementRef, renderer)
      });

    it('should create an instance', () => {
      expect(directive).toBeTruthy();
    });

    it('ngOnInit() should call renderer.addClass', () => {
      directive.config = { code: 'test', uiStyles: { attributes: { applyValueStyles: true } } };
      spyOn(renderer, 'addClass').and.callThrough();
      directive.ngOnInit();
      expect(renderer.addClass).toHaveBeenCalled();
      expect(renderer.addClass).toHaveBeenCalledWith({ style: { backgroundColor: '' } }, 'test');
      expect(renderer.addClass).toHaveBeenCalledWith({ style: { backgroundColor: '' } }, 'placeholder');
    });

    it('ngOnChanges() should call renderer.addClass(), renderer.removeClass() and directive.,getValue()', () => {
      directive.config = { code: 'test', uiStyles: { attributes: { applyValueStyles: true } } };
      const changes = { displayValue: { previousValue: '123', currentValue: 'current' } };
      spyOn(renderer, 'addClass').and.callThrough();
      spyOn(renderer, 'removeClass').and.callThrough();
      spyOn(directive, 'getValue').and.callThrough();
      directive.ngOnChanges(changes);
      expect(renderer.addClass).toHaveBeenCalled();
      expect(renderer.removeClass).toHaveBeenCalled();
      expect(directive.getValue).toHaveBeenCalled();
    });

});