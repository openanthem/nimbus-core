'use strict';
import { TestBed, async } from '@angular/core/testing';

import { TooltipComponent } from './tooltip.component';
import { WindowRefService } from '../../../services/window-ref.service';
import { DomSanitizer } from '@angular/platform-browser';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';

let domSanitizer;

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

const declarations = [    TooltipComponent   ];
const providers = [
       {provide: WindowRefService, useClass: MockWindowRefService},
       {provide: DomSanitizer, useClass: MockDomSanitizer}
   ];
const imports = [];

describe('TooltipComponent', () => {

    configureTestSuite();
    setup(TooltipComponent, declarations, imports, providers);

    beforeEach(async function(this: TestContext<TooltipComponent>){
      domSanitizer = TestBed.get(DomSanitizer);
    });

    it('should create the TooltipComponent', async function (this: TestContext<TooltipComponent>) {
        expect(this.hostComponent).toBeTruthy();
    });

    it('closeCallout() should remove class of event', async function (this: TestContext<TooltipComponent>) {
        const eve: any = {
            preventDefault: () => { },
            srcElement: {
                parentElement: {
                    parentElement: {
                        classList: {
                            remove: (a) => { }
                        }
                    }
                }
            }
        };
        spyOn(eve, 'preventDefault').and.callThrough();
        spyOn(eve.srcElement.parentElement.parentElement.classList, 'remove').and.callThrough();
        this.hostComponent.closeCallout(eve);
        expect(eve.preventDefault).toHaveBeenCalled();
        expect(eve.srcElement.parentElement.parentElement.classList.remove).toHaveBeenCalled();
    });

    it('toggleOpen() should update the widgetPosition property to east', async function (this: TestContext<TooltipComponent>) {
        const eve: any = {
            preventDefault: () => { },
            clientX: 100,
            srcElement: {
                parentElement: {
                    classList: {
                        add: () => { }
                    }
                }
            }
        };
        this.hostComponent.toggleOpen(eve);
        expect(this.hostComponent.widgetPosition).toEqual('east');
    });

    it('toggleOpen() should update the widgetPosition property to west', async function (this: TestContext<TooltipComponent>) {
        const eve: any = {
            preventDefault: () => { },
            clientX: 900,
            srcElement: {
                parentElement: {
                    classList: {
                        add: () => { }
                    }
                }
            }
        };
        this.hostComponent.toggleOpen(eve);
        expect(this.hostComponent.widgetPosition).toEqual('west');
    });

    it('htmlContent property should be updated with /"DOM/" suffix', async function (this: TestContext<TooltipComponent>) {
        this.hostComponent.helpText = 'test';
        const test = this.hostComponent.htmlContent;
        expect(test).toEqual('testDom');
    });

});
