'use strict';
import { TestBed, async } from '@angular/core/testing';
import { AngularSvgIconModule } from 'angular-svg-icon';

import { Image } from './image.component';
import { ServiceConstants } from './../../services/service.constants';
import { SvgComponent } from './svg/svg.component';

describe('Image', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Image,
        SvgComponent
       ],
       imports: [
        AngularSvgIconModule
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(Image);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('ngOnInit() should update the imagesPath', async(() => {
    const fixture = TestBed.createComponent(Image);
    const app = fixture.debugElement.componentInstance;
    app.ngOnInit();
    expect(app.imagesPath).not.toEqual(null);
  }));

});
