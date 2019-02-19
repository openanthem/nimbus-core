import { TestBed, async } from '@angular/core/testing';

import { DisplayValueDirective } from './display-value.directive';
import { ElementRef, Renderer2 } from '@angular/core';
import { displayValueDirectiveConfig, displayValueDirectiveChanges } from 'mockdata';

class MockElementRef {
    nativeElement = {
        style: {
            backgroundColor: 'red'
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
        directive.config = displayValueDirectiveConfig;
      });

    it('should create an instance', () => {
        expect(directive).toBeTruthy();
    });

    it('ngOnInit() should call renderer.addClass()', () => {
        spyOn(renderer, 'addClass').and.callThrough();
        directive.ngOnInit();
        expect(renderer.addClass).toHaveBeenCalledWith({ style: { backgroundColor: 'red' } }, 'placeholder');
    });

    it('ngOnInit() should call getValue() and renderer.addClass()', () => {
        directive.displayValue = new String('Active');
        spyOn(renderer, 'addClass').and.callThrough();
        spyOn(directive, 'getValue').and.callThrough();
        directive.ngOnInit();
        expect(renderer.addClass).toHaveBeenCalledWith({ style: { backgroundColor: 'red' } }, 'Active');
        expect(directive.getValue).toHaveBeenCalledWith(directive.displayValue);
    });

    it('ngOnChanges() should call renderer.removeClass()', () => {
        spyOn(renderer, 'removeClass').and.callThrough();
        directive.ngOnChanges(displayValueDirectiveChanges);
        expect(renderer.removeClass).toHaveBeenCalledWith({ style: { backgroundColor: 'red' } }, 'placeholder');
    });

    it('ngOnChanges() should call getValue() and renderer.removeClass()', () => {
        spyOn(renderer, 'removeClass').and.callThrough();
        spyOn(directive, 'getValue').and.callThrough();
        displayValueDirectiveChanges.displayValue['previousValue'] = 'testingPreviousValue';
        directive.ngOnChanges(displayValueDirectiveChanges);
        expect(renderer.removeClass).toHaveBeenCalledWith({ style: { backgroundColor: 'red' } }, 'testingPreviousValue');
        expect(directive.getValue).toHaveBeenCalledWith(displayValueDirectiveChanges.displayValue['previousValue']);
    });

    it('ngOnChanges() should call getValue() and renderer.addClass()', () => {
        spyOn(renderer, 'addClass').and.callThrough();
        spyOn(directive, 'getValue').and.callThrough();
        directive.ngOnChanges(displayValueDirectiveChanges);
        expect(renderer.addClass).toHaveBeenCalledWith({ style: { backgroundColor: 'red' } }, displayValueDirectiveChanges.displayValue['currentValue']);
        expect(directive.getValue).toHaveBeenCalledWith(displayValueDirectiveChanges.displayValue['currentValue']);
    });

    it('ngOnChanges() should call renderer.addClass()', () => {
        spyOn(renderer, 'addClass').and.callThrough();
        displayValueDirectiveChanges.displayValue['currentValue'] = undefined;
        directive.ngOnChanges(displayValueDirectiveChanges);
        expect(renderer.addClass).toHaveBeenCalledWith({ style: { backgroundColor: 'red' } }, 'placeholder');
    });

    it('getValue() should update the string and return it', () => {
        expect(directive.getValue('test getValue')).toEqual('testgetValue');
    });

    it('getValue() should not update the val and return it', () => {
        const val = { 'test': 'getValue' };
        expect(directive.getValue(val)).toEqual(val);
    });
});
