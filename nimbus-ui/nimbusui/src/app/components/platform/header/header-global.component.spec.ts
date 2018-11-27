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

let fixture, app, breadcrumbService;

class MockBreadcrumbService {
    getHomeBreadcrumb() {
        const test = {
            url: 'testing'
        }
        return test;
    }
}

describe(' HeaderGlobal', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        HeaderGlobal,
        Link,
        Value,
        Paragraph,
        Button,
        ActionDropdown,
        Image,
        ActionLink,
        SvgComponent
        ],
        imports: [ 
          RouterTestingModule,
          AngularSvgIconModule 
        ],
        providers: [ { provide: BreadcrumbService, useClass: MockBreadcrumbService} ]
    }).compileComponents();
    fixture = TestBed.createComponent( HeaderGlobal);
    app = fixture.debugElement.componentInstance;
    breadcrumbService = TestBed.get(BreadcrumbService);
  }));

  it('should create the HeaderGlobal', async(() => {
    expect(app).toBeTruthy();
  }));

  it('get homeRoute() should get home route', async(() => {
    expect(app.homeRoute).toEqual('testing');
  }));

  it('homeRoute should be updated from breadcrumpservice.getHomeBreadcrump()', async(() => {
    spyOn(breadcrumbService, 'getHomeBreadcrumb').and.returnValue('');
    expect(app.homeRoute).toEqual('');
  }));

  it('ngOnDestroy() should call mouseEventSubscription.unsubscribe()', async(() => {
    app.mouseEventSubscription = {
      unsubscribe: () => {}
    };
    spyOn(app.mouseEventSubscription, 'unsubscribe').and.callThrough();
    app.ngOnDestroy();
    expect(app.mouseEventSubscription.unsubscribe).toHaveBeenCalled();
  }));

  it('toggleOpen() should call event.state as closedPanel and call mouseEventSubscription.unsubscribe()', async(() => {
    const event = {
      isOpen: true,
      state: 'openPanel',
      selectedItem: true
    };
    app.dropDowns = {
      toArray: () => {
        return [{
          selectedItem: false,
          isOpen: true,
          state: ''
        }]
      }
    };
    app.mouseEventSubscription = {
      closed: false,
      unsubscribe: () => {}
    };
    spyOn(app.mouseEventSubscription, 'unsubscribe').and.callThrough();
    app.toggleOpen(event);
    expect(event.state).toEqual('closedPanel');
    expect(app.mouseEventSubscription.unsubscribe).toHaveBeenCalled();
  }));

  it('toggleOpen() should call event.state as openPanel', async(() => {
    const event = {
      isOpen: true,
      state: 'closedPanel',
      selectedItem: true
    };
    app.dropDowns = {
      toArray: () => {
        return []
      }
    };
    app.mouseEventSubscription = {
      closed: true,
      unsubscribe: () => {}
    };
    app.toggleOpen(event);
    expect(event.state).toEqual('openPanel');
  }));

});

