'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

import { Paragraph } from './paragraph.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { Param } from '../../../shared/param-state';

let param: Param;

class MockDomSanitizer {
    bypassSecurityTrustHtml(a:any) {
        return 'test';
    }
}

const declarations = [
  Paragraph
 ];
 const imports = [
  HttpClientModule,
  HttpModule
 ];
 const providers = [
     { provide: DomSanitizer, useClass: MockDomSanitizer }
 ];


describe('Paragraph', () => {

  configureTestSuite();
  setup(Paragraph, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<Paragraph>){
    this.hostComponent.element = param;
  });

  it('should create the Paragraph', async function (this: TestContext<Paragraph>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('get htmlContent() should get content DomSanitizer.bypassSecurityTrustHtml()', async function (this: TestContext<Paragraph>) {
    expect(this.hostComponent.htmlContent).toEqual('test');
  });

});