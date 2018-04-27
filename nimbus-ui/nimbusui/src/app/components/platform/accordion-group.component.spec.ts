'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ElementRef } from '@angular/core';

import { AccordionGroup } from './accordion-group.component';
import { Accordion } from './accordion.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

class MockElementRef {
    nativeElement = {
        querySelector: () => {
            return {
                scrollIntoView: () => {}};
        }
    };
}

let fixture, app, elementRef;

describe('AccordionGroup', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          AccordionGroup
       ],
       imports: [
        HttpModule,
        HttpClientTestingModule,
        BrowserAnimationsModule
       ],
       providers: [
           {provide: ElementRef, useClass: MockElementRef},
        Accordion
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(AccordionGroup);
    app = fixture.debugElement.componentInstance;
    elementRef = TestBed.get(ElementRef);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('state property should be updated', async(() => {
      app.state = 'test';
    expect(app.state).toEqual('test');
  }));

  it('ngOnInit() should call loadLabelConfig()', async(() => {
      spyOn(app, 'loadLabelConfig').and.returnValue('');
      app.param = 'test';
      app.ngOnInit();
      expect(app.loadLabelConfig).toHaveBeenCalled();
  }));

  it('ngOnDestroy() should call accordion.removeGroup()', async(() => {
    spyOn(app.accordion, 'removeGroup').and.returnValue('');
    app.ngOnDestroy();
    expect(app.accordion.removeGroup).toHaveBeenCalled();
  }));

  it('toggleOpen() should update state to openPanel and isOpen to false', async(() => {
      const eve = {
        preventDefault: () => {}
      };
      app.isOpen = true;
      spyOn(eve, 'preventDefault').and.callThrough();
      app.toggleOpen(eve);
    expect(app.state).toEqual('openPanel');
    expect(app.isOpen).toEqual(false);
    expect(eve.preventDefault).toHaveBeenCalled();
  }));

  it('toggleOpen() should update state property to closedPanel and isOpen to false', async(() => {
    const eve = {
      preventDefault: () => {}
    };
    app.isOpen = true;
    spyOn(eve, 'preventDefault').and.callThrough();
    app.state = 'openPanel';
    app.toggleOpen(eve);
  expect(app.state).toEqual('closedPanel');
  expect(app.isOpen).toEqual(false);
  expect(eve.preventDefault).toHaveBeenCalled();
  }));

  it('animationStart() should update isHidden property', async(() => {
      app.animationStart('t');
    expect(app.isHidden).toEqual(false);
  }));

  it('animationDone() should update isHidden property', async(() => {
    app.state = 'closedPanel';
    app.animationDone('t');
    expect(app.isHidden).toEqual(true);
  }));

  it('animationDone() should call accordion.closeOthers()', async(() => {
    app.state = 'openPanel';
    app.accordion = {
        closeOthers: () => {
            return new Promise(
                (resolve, reject) => {
                    resolve('abcd');
                }
            );
        }
    };
    app.title = 'testing';
    const test = {
        scrollIntoView: () => {}
    };
    spyOn(app.accordion, 'closeOthers').and.callThrough();
    spyOn(app.elementRef.nativeElement, 'querySelector').and.returnValue({
      scrollIntoView: () => {}
    });
    app.animationDone('t');
    expect(app.accordion.closeOthers).toHaveBeenCalled();
  }));

});