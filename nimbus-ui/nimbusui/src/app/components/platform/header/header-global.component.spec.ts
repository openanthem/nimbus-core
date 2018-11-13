'use strict';
import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing'
import { AngularSvgIconModule } from 'angular-svg-icon';

import { HeaderGlobal } from './header-global.component';
import { Link } from '../link.component';
import { Value } from '../form/elements/value.component';
import { Paragraph } from '../content/paragraph.component';
import { BreadcrumbService } from './../breadcrumb/breadcrumb.service';
import { Button } from '../../platform/form/elements/button.component';
import { ActionDropdown, ActionLink } from '../../platform/form/elements/action-dropdown.component';
import { Image } from '../../platform/image.component';
import { SvgComponent } from '../../platform/svg/svg.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';

let breadcrumbService;

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

describe(' HeaderGlobal', () => {
  configureTestSuite();
  setup(HeaderGlobal, declarations, imports, providers);

  beforeEach(async function(this: TestContext<HeaderGlobal>){
    breadcrumbService = TestBed.get(BreadcrumbService);
  });

  it('should create the HeaderGlobal', async function (this: TestContext<HeaderGlobal>) {
      expect(this.hostComponent).toBeTruthy();
  });

  it('get homeRoute() should get home route', async function (this: TestContext<HeaderGlobal>) {
    expect(this.hostComponent.homeRoute).toEqual('testing');
  });

  it('homeRoute should be updated from breadcrumpservice.getHomeBreadcrump()', async function (this: TestContext<HeaderGlobal>) {
    spyOn(breadcrumbService, 'getHomeBreadcrumb').and.returnValue('');
    expect(this.hostComponent.homeRoute).toEqual('');
  });

  it('ngOnDestroy() should call mouseEventSubscription.unsubscribe()', async function (this: TestContext<HeaderGlobal>) {
    const mouseEventSubscription: any = {
      unsubscribe: () => {}
    };
    this.hostComponent.mouseEventSubscription = mouseEventSubscription;
    spyOn(this.hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
    this.hostComponent.ngOnDestroy();
    expect(this.hostComponent.mouseEventSubscription.unsubscribe).toHaveBeenCalled();  
  });

  it('toggleOpen() should call event.state as closedPanel and call mouseEventSubscription.unsubscribe()', async function (this: TestContext<HeaderGlobal>) {
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
    this.hostComponent.dropDowns = dropdowns;
    const mouseEventSubscription: any = {
      closed: false,
      unsubscribe: () => {}
    };
    this.hostComponent.mouseEventSubscription = mouseEventSubscription;
    spyOn(this.hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
    this.hostComponent.toggleOpen(event);
    expect(event.state).toEqual('closedPanel');
    expect(this.hostComponent.mouseEventSubscription.unsubscribe).toHaveBeenCalled();  
  });

  it('toggleOpen() should call event.state as openPanel', async function (this: TestContext<HeaderGlobal>) {
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
    this.hostComponent.dropDowns = dropDowns;
    const mouseEventSubscription: any = {
      closed: true,
      unsubscribe: () => {}
    };
    this.hostComponent.mouseEventSubscription = mouseEventSubscription;
    this.hostComponent.toggleOpen(event);
    expect(event.state).toEqual('openPanel');  
  });

});

