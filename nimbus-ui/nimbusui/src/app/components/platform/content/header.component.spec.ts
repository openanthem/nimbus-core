'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { Header } from './header.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { Param } from '../../../shared/param-state';

let param: Param;

const declarations = [ Header ];
 const imports = [
     HttpClientModule,
      HttpModule
 ];
 const providers = [];

describe('Header', () => {

    configureTestSuite();
    setup(Header, declarations, imports, providers);
    param = (<any>data).payload;
  
    beforeEach(async function(this: TestContext<Header>){
      this.hostComponent.element = param;
    });

    it('should create the Header', async function (this: TestContext<Header>) {
        expect(this.hostComponent).toBeTruthy();
    });

    it('ngOnInit() should update size', async function (this: TestContext<Header>) {
        this.fixture.whenStable().then(() => {
            this.hostComponent.element.config.uiStyles.attributes.size = '1234';
            this.hostComponent.ngOnInit();
            expect((this.hostComponent as any).size).toEqual('1234');
        });
    });

});