'use strict';
import { TestBed, async } from '@angular/core/testing';
import { AngularSvgIconModule } from 'angular-svg-icon';

import { ButtonGroup } from './button-group.component';
import { Button } from './button.component';
import { SvgComponent } from '../../svg/svg.component';
import { Image } from '../../image.component';

describe('ButtonGroup', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ButtonGroup,
        Button,
        SvgComponent,
        Image
       ],
       imports: [
         AngularSvgIconModule
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(ButtonGroup);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});