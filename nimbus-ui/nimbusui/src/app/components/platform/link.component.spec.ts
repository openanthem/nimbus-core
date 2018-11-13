'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { Link } from './link.component';
import { PageService } from '../../services/page.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { WebContentSvc } from '../../services/content-management.service';
import { Param, Type, Model } from '../../shared/param-state';
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import * as data from '../../payload.json';

let pageService, configService, param: Param;

class MockWebContentSvc {
    findLabelContentFromConfig(a, b) {

    }
}

class MockPageService {
    processEvent(a, b, c, d) {

    }
}

const declarations = [
    Link
 ];
const  imports = [
     HttpModule,
     HttpClientTestingModule
 ];
const providers = [
     {provide: WebContentSvc, useClass: MockWebContentSvc},
     {provide: PageService, useClass: MockPageService},
     CustomHttpClient,
     LoaderService,
     ConfigService
 ];

describe('Link', () => {

    configureTestSuite();
    setup(Link, declarations, imports, providers);
    param = (<any>data).payload;
  
    beforeEach(async function(this: TestContext<Link>){
        this.hostComponent.element = param;
        pageService = TestBed.get(PageService);
        configService = TestBed.get(ConfigService);
    });

    it('should create the Link', function (this: TestContext<Link>) {
        expect(this.hostComponent).toBeTruthy();
    });

    it('processOnClick() should call pageService.processEvent()', function (this: TestContext<Link>) {
        spyOn(pageService, 'processEvent').and.callThrough();
        this.hostComponent.processOnClick('/test', 'GET', 'test');
        expect(pageService.processEvent).toHaveBeenCalled();
    });

    it('processOnClick() should call pageService.processEvent() and call GenericDomain.addAttribute()', function (this: TestContext<Link>) {
        spyOn(pageService, 'processEvent').and.callThrough();
        this.hostComponent.root = new Param(configService);
        this.hostComponent.root.type = new Type(configService);
        this.hostComponent.root.type.model = new Model(configService);
        const rootParam: any = { path: 'test/id' };
        this.hostComponent.root.type.model.params = [rootParam];
        this.hostComponent.element.path = 'test/t';
        this.hostComponent.processOnClick('/test', 'GET', 'test');
        expect(pageService.processEvent).toHaveBeenCalled();
    });
    it('processOnClick() should call pageService.processEvent() and should not call GenericDomain.addAttribute()', function (this: TestContext<Link>) {
        spyOn(pageService, 'processEvent').and.callThrough();
        this.hostComponent.root = new Param(configService);
        this.hostComponent.root.type = new Type(configService);
        this.hostComponent.root.type.model = new Model(configService);
        const rootParam: any = { path: 'asdsa' };
        this.hostComponent.root.type.model.params = [rootParam];
        this.hostComponent.element.path = 'test/t';
        this.hostComponent.processOnClick('/test', 'GET', 'test');
        expect(pageService.processEvent).toHaveBeenCalled();
    });

    it('url should be update from the element', function (this: TestContext<Link>) {
        this.fixture.whenStable().then(() => {
            this.hostComponent.element.config.uiStyles.attributes.url = '/test';
            expect(this.hostComponent.url).toEqual('/test');
        });
    });

    it('value should be updated from the element', function (this: TestContext<Link>) {
        this.fixture.whenStable().then(() => {
            this.hostComponent.element.config.uiStyles.attributes.value = 'tvalue';
            expect(this.hostComponent.value).toEqual('tvalue');
        });
    });

    it('method should BE updated from the element', function (this: TestContext<Link>) {
        this.fixture.whenStable().then(() => {
            this.hostComponent.element.config.uiStyles.attributes.method = 'tmethod';
            expect(this.hostComponent.method).toEqual('tmethod');
        });
    });

    it('b property should be updated from the element', function (this: TestContext<Link>) {
        this.fixture.whenStable().then(() => {
            this.hostComponent.element.config.uiStyles.attributes.b = 'tb'
            expect(this.hostComponent.b).toEqual('tb');
        });
    });

    it('target should be updated from element', function (this: TestContext<Link>) {
        this.fixture.whenStable().then(() => {
            this.hostComponent.element.config.uiStyles.attributes.target = 'ttarget'
            expect(this.hostComponent.target).toEqual('ttarget');
        });
    });

    it('rel should be updated from the element', function (this: TestContext<Link>) {
        this.fixture.whenStable().then(() => {
            this.hostComponent.element.config.uiStyles.attributes.rel = 'trel';
            expect(this.hostComponent.rel).toEqual('trel');
        });
    });

});