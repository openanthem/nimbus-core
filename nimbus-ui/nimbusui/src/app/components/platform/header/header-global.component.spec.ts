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
import { RouterTestingModule } from '@angular/router/testing'
import { AngularSvgIconModule } from 'angular-svg-icon';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { HeaderGlobal } from './header-global.component';
import { Link } from '../link.component';
import { Value } from '../form/elements/value.component';
import { Paragraph } from '../content/paragraph.component';
import { BreadcrumbService } from './../breadcrumb/breadcrumb.service';
// import { Button } from '../../platform/form/elements/button.component';
import { ActionDropdown, ActionLink } from '../../platform/form/elements/action-dropdown.component';
import { Image } from '../../platform/image.component';
import { SvgComponent } from '../../platform/svg/svg.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';

let breadcrumbService;

@Component({
  template: '<div></div>',
  selector: 'nm-button'
})
class Button {

  @Input() element: any;
  @Input() payload: string;
  @Input() form: any;
  @Input() actionTray?: boolean;

  @Output() buttonClickEvent = new EventEmitter();

  @Output() elementChange = new EventEmitter();
  private imagesPath: string;
  private btnClass: string;
  private disabled: boolean;
  files: any;
  differ: any;
  componentTypes;
}

class MockBreadcrumbService {
    getHomeBreadcrumb() {
        const test = {
            url: 'testing'
        }
        return test;
    }
}

const declarations = [
  HeaderGlobal,
  Link,
  Value,
  Paragraph,
  Button,
  ActionDropdown,
  Image,
  ActionLink,
  SvgComponent
  ];
  const imports = [ 
    RouterTestingModule,
    AngularSvgIconModule 
  ];
  const providers = [ { provide: BreadcrumbService, useClass: MockBreadcrumbService} ];
  let fixture, hostComponent;
describe(' HeaderGlobal', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderGlobal);
    hostComponent = fixture.debugElement.componentInstance;
    breadcrumbService = TestBed.get(BreadcrumbService);
  });

  it('should create the HeaderGlobal', async(() => {
      expect(hostComponent).toBeTruthy();
  }));

  it('get homeRoute() should get home route', async(() => {
    expect(hostComponent.homeRoute).toEqual('testing');
  }));

  it('homeRoute should be updated from breadcrumpservice.getHomeBreadcrump()', async(() => {
    spyOn(breadcrumbService, 'getHomeBreadcrumb').and.returnValue('');
    expect(hostComponent.homeRoute).toEqual('');
  }));

  // it('ngOnDestroy() should call mouseEventSubscription.unsubscribe()', async(() => {
  //   const mouseEventSubscription: any = {
  //     unsubscribe: () => {}
  //   };
  //   hostComponent.mouseEventSubscription = mouseEventSubscription;
  //   spyOn(hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
  //   hostComponent.ngOnDestroy();
  //   expect(hostComponent.mouseEventSubscription.unsubscribe).toHaveBeenCalled();  
  // }));

  it('toggleOpen() should call event.state as closedPanel and call mouseEventSubscription.unsubscribe()', async(() => {
    const event = {
      isOpen: true,
      state: 'openPanel',
      selectedItem: true
    };
    const dropdowns: any = {
      toArray: () => {
        return [{
          selectedItem: false,
          isOpen: true,
          state: ''
        }]
      }
    };
    hostComponent.dropDowns = dropdowns;
    const mouseEventSubscription: any = {
      closed: false,
      unsubscribe: () => {}
    };
    hostComponent.mouseEventSubscription = mouseEventSubscription;
    spyOn(hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
    hostComponent.toggleOpen(event);
    expect(event.state).toEqual('closedPanel');
    expect(hostComponent.mouseEventSubscription.unsubscribe).toHaveBeenCalled();  
  }));

  it('toggleOpen() should call event.state as openPanel', async(() => {
    const event = {
      isOpen: true,
      state: 'closedPanel',
      selectedItem: true
    };
    const dropDowns: any = {
      toArray: () => {
        return []
      }
    };
    hostComponent.dropDowns = dropDowns;
    const mouseEventSubscription: any = {
      closed: true,
      unsubscribe: () => {}
    };
    hostComponent.mouseEventSubscription = mouseEventSubscription;
    hostComponent.toggleOpen(event);
    expect(event.state).toEqual('openPanel');  
  }));

});

