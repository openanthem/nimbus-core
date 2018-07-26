'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

import { Paragraph } from './paragraph.component';

class MockDomSanitizer {
    bypassSecurityTrustHtml(a:any) {
        return 'test';
    }
}

describe('Paragraph', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Paragraph
       ],
       imports: [
        HttpClientModule,
        HttpModule
       ],
       providers: [
           { provide: DomSanitizer, useClass: MockDomSanitizer }
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(Paragraph);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('get htmlContent() should get content DomSanitizer.bypassSecurityTrustHtml()', async(() => {
    const fixture = TestBed.createComponent(Paragraph);
    const app = fixture.debugElement.componentInstance;
    expect(app.htmlContent).toEqual('test');
  }));

});