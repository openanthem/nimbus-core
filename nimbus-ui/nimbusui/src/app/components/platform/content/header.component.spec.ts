'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { Header } from './header.component';

describe('Header', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          Header
       ],
       imports: [
           HttpClientModule,
            HttpModule
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(Header);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

 it('ngOnInit() should update size', async(() => {
    const fixture = TestBed.createComponent(Header);
    const app = fixture.debugElement.componentInstance;
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    size: 1234
                }
            }
        }
    };
    app.ngOnInit();
    expect(app.size).toEqual(1234);
  }));


});