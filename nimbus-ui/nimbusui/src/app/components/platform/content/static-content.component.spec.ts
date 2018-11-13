'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

import { StaticText } from './static-content.component';
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
  StaticText
 ];
 const imports = [
  HttpClientModule,
  HttpModule
 ];
 const providers = [
     { provide: DomSanitizer, useClass: MockDomSanitizer }
 ];

describe('StaticText', () => {
  configureTestSuite();
  setup(StaticText, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<StaticText>){
    this.hostComponent.element = param;
  });

  it('should create the StaticText', async function (this: TestContext<StaticText>) {
      expect(this.hostComponent).toBeTruthy();
  });

  it('get htmlContent() should get content DomSanitizer.bypassSecurityTrustHtml()', async function (this: TestContext<StaticText>) {
    expect(this.hostComponent.htmlContent).toEqual('test');
  });

});