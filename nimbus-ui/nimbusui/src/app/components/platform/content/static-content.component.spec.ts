'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

import { StaticText } from './static-content.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import { Param } from '../../../shared/param-state';
import { fieldValueParam } from 'mockdata';

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
 let fixture, hostComponent;
describe('StaticText', () => {
    configureTestSuite(() => {
        setup( declarations, imports, providers);
    });
    
  beforeEach(() => {
    fixture = TestBed.createComponent(StaticText);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
  });

  it('should create the StaticText', async(() => {
      expect(hostComponent).toBeTruthy();
  }));

  it('get htmlContent() should get content DomSanitizer.bypassSecurityTrustHtml()', async(() => {
    expect(hostComponent.htmlContent).toEqual('test');
  }));

});