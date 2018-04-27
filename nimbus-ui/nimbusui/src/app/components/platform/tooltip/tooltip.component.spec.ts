'use strict';
import { TestBed, async } from '@angular/core/testing';

import { TooltipComponent } from './tooltip.component';
import { WindowRefService } from '../../../services/window-ref.service';
import { DomSanitizer } from '@angular/platform-browser';

let fixture, app, domSanitizer;

class MockWindowRefService {
    window = {
        innerWidth: 900
    };
}

class MockDomSanitizer {
    bypassSecurityTrustHtml(a) {
        return 'testDom'
    }
}

describe('TooltipComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        TooltipComponent
       ],
       providers: [
           {provide: WindowRefService, useClass: MockWindowRefService},
           {provide: DomSanitizer, useClass: MockDomSanitizer}
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(TooltipComponent);
    app = fixture.debugElement.componentInstance;
    domSanitizer = TestBed.get(DomSanitizer);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('closeCallout() should remove class of event', async(() => {
    const eve = {
        preventDefault: () => {},
        srcElement: {
            parentElement: {
                parentElement: {
                    classList: {
                        remove: (a) => {}
                    }
                }
            }
        }
    };
    spyOn(eve, 'preventDefault').and.callThrough();
    spyOn(eve.srcElement.parentElement.parentElement.classList, 'remove').and.callThrough();
    app.closeCallout(eve);
    expect(eve.preventDefault).toHaveBeenCalled();
    expect(eve.srcElement.parentElement.parentElement.classList.remove).toHaveBeenCalled();
  }));

  it('toggleOpen() should update the widgetPosition property to east', async(() => {
      const eve = {
        preventDefault: () => {},
          clientX: 100,
          srcElement: {
              parentElement: {
                  classList: {
                      add: () => {}
                  }
              }
          }
      };
      app.toggleOpen(eve);
    expect(app.widgetPosition).toEqual('east');
  }));

  it('toggleOpen() should update the widgetPosition property to west', async(() => {
    const eve = {
      preventDefault: () => {},
        clientX: 900,
        srcElement: {
            parentElement: {
                classList: {
                    add: () => {}
                }
            }
        }
    };
    app.toggleOpen(eve);
  expect(app.widgetPosition).toEqual('west');
}));

it('htmlContent property should be updated with /"DOM/" suffix', async(() => {
    app.helpText = 'test';
    const test = app.htmlContent;
    expect(test).toEqual('testDom');
  }));

});
