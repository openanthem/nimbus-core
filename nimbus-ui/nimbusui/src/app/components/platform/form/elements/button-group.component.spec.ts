'use strict';
import { TestBed, async } from '@angular/core/testing';

import { ButtonGroup } from './button-group.component';
import { Button } from './button.component';

describe('ButtonGroup', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ButtonGroup,
        Button
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(ButtonGroup);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});