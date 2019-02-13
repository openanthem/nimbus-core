/**
 * @license
 * Copyright 2016-2018 the original author or authors.
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
let fixture, hostComponent;
describe('TooltipComponent', () => {

    configureTestSuite(() => {
        setup( declarations, imports, providers);
      });
    

    beforeEach(() => {
        fixture = TestBed.createComponent(TooltipComponent);
        hostComponent = fixture.debugElement.componentInstance;
        domSanitizer = TestBed.get(DomSanitizer);
    });

    it('should create the TooltipComponent', async(() => {
        expect(hostComponent).toBeTruthy();
    }));

    it('closeCallout() should remove class of event', async(() => {
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
        hostComponent.closeCallout(eve);
        expect(eve.preventDefault).toHaveBeenCalled();
        expect(eve.srcElement.parentElement.parentElement.classList.remove).toHaveBeenCalled();
    }));

    it('toggleOpen() should update the widgetPosition property to east', async(() => {
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
        hostComponent.toggleOpen(eve);
        expect(hostComponent.widgetPosition).toEqual('east');
    }));

    it('toggleOpen() should update the widgetPosition property to west', async(() => {
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
        hostComponent.toggleOpen(eve);
        expect(hostComponent.widgetPosition).toEqual('west');
    }));

    it('htmlContent property should be updated with /"DOM/" suffix', async(() => {
        hostComponent.helpText = 'test';
        const test = hostComponent.htmlContent;
        expect(test).toEqual('testDom');
    }));

});
