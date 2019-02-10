'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { Header } from './header.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import { Param } from '../../../shared/param-state';
import { fieldValueParam } from 'mockdata';

const declarations = [ Header ];
 const imports = [
     HttpClientModule,
      HttpModule
 ];
 const providers = [];
 let fixture, hostComponent;
describe('Header', () => {

    configureTestSuite(() => {
        setup( declarations, imports, providers);
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(Header);
        hostComponent = fixture.debugElement.componentInstance;
        hostComponent.element = fieldValueParam;
    });

    it('should create the Header',  async(() => {
        expect(hostComponent).toBeTruthy();
    }));

    // it('ngOnInit() should update size',  () => {
    //     fixture.whenStable().then(() => {
    //         hostComponent.element.config.uiStyles.attributes.size = '1234';
    //         hostComponent.ngOnInit();
    //         expect((hostComponent as any).size).toEqual('1234');
    //     });
    // });

});