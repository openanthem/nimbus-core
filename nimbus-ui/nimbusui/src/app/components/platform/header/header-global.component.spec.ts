'use strict';
import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing'

import { HeaderGlobal } from './header-global.component';
import { Link } from '../link.component';
import { Value } from '../form/elements/value.component';
import { Paragraph } from '../content/paragraph.component';
import { BreadcrumbService } from './../breadcrumb/breadcrumb.service';

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
        Paragraph
        ],
        imports: [ RouterTestingModule ],
        providers: [ { provide: BreadcrumbService, useClass: MockBreadcrumbService} ]
    }).compileComponents();
  }));

  it('should create the HeaderGlobal', async(() => {
    const fixture = TestBed.createComponent( HeaderGlobal);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('get homeRoute() should get home route', async(() => {
    const fixture = TestBed.createComponent( HeaderGlobal);
    const app = fixture.debugElement.componentInstance;
    expect(app.homeRoute).toEqual('testing');
  }));

});

