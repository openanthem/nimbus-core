'use strict';
import { TestBed, async } from '@angular/core/testing';

import { Menu } from './menu.component';
import { Link } from '../platform/link.component';

describe('Menu', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Menu,
        Link
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(Menu);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});