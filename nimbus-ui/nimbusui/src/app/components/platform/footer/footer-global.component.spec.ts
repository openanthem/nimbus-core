'use strict';
import { TestBed, async } from '@angular/core/testing';

import { FooterGlobal } from './footer-global.component';
import { Paragraph } from '../content/paragraph.component';
import { Link } from '../link.component';

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FooterGlobal,
        Link,
        Paragraph
        ]
    }).compileComponents();
  }));

  it('should create the FooterGlobal', async(() => {
    const fixture = TestBed.createComponent(FooterGlobal);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});
