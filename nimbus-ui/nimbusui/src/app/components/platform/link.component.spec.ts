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
import { fieldValueParam } from 'mockdata';

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

 let fixture, hostComponent;
 
describe('Link', () => {

    configureTestSuite(() => {
        setup( declarations, imports, providers);
    });
    
  
    beforeEach(() => {
        fixture = TestBed.createComponent(Link);
        hostComponent = fixture.debugElement.componentInstance;
        hostComponent.element = fieldValueParam;
        pageService = TestBed.get(PageService);
        configService = TestBed.get(ConfigService);
    });

    it('should create the Link',  () => {
        expect(hostComponent).toBeTruthy();
    });

    it('processOnClick() should call pageService.processEvent()',  () => {
        spyOn(pageService, 'processEvent').and.callThrough();
        hostComponent.processOnClick('/test', 'GET', 'test');
        expect(pageService.processEvent).toHaveBeenCalled();
    });

    it('processOnClick() should call pageService.processEvent() and call GenericDomain.addAttribute()',  () => {
        spyOn(pageService, 'processEvent').and.callThrough();
        hostComponent.root = new Param(configService);
        hostComponent.root.type = new Type(configService);
        hostComponent.root.type.model = new Model(configService);
        const rootParam: any = { path: 'test/id' };
        hostComponent.root.type.model.params = [rootParam];
        hostComponent.element.path = 'test/t';
        hostComponent.processOnClick('/test', 'GET', 'test');
        expect(pageService.processEvent).toHaveBeenCalled();
    });
    it('processOnClick() should call pageService.processEvent() and should not call GenericDomain.addAttribute()',  () => {
        spyOn(pageService, 'processEvent').and.callThrough();
        hostComponent.root = new Param(configService);
        hostComponent.root.type = new Type(configService);
        hostComponent.root.type.model = new Model(configService);
        const rootParam: any = { path: 'asdsa' };
        hostComponent.root.type.model.params = [rootParam];
        hostComponent.element.path = 'test/t';
        hostComponent.processOnClick('/test', 'GET', 'test');
        expect(pageService.processEvent).toHaveBeenCalled();
    });

    it('url should be update from the element', () => {
        fixture.whenStable().then(() => {
            hostComponent.element.config.uiStyles.attributes.url = '/test';
            hostComponent.ngOnInit();
            expect(hostComponent.url).toEqual('/test');
        });
    });

    it('value should be updated from the element', () => {
        fixture.whenStable().then(() => {
            hostComponent.element.config.uiStyles.attributes.value = 'tvalue';
            expect(hostComponent.value).toEqual('tvalue');
        });
    });

    it('method should BE updated from the element',  () => {
        fixture.whenStable().then(() => {
            hostComponent.element.config.uiStyles.attributes.method = 'tmethod';
            expect(hostComponent.method).toEqual('tmethod');
        });
    });

    it('b property should be updated from the element',  () => {
        fixture.whenStable().then(() => {
            hostComponent.element.config.uiStyles.attributes.b = 'tb'
            expect(hostComponent.b).toEqual('tb');
        });
    });

    it('target should be updated from element', () => {
        fixture.whenStable().then(() => {
            hostComponent.element.config.uiStyles.attributes.target = 'ttarget'
            expect(hostComponent.target).toEqual('ttarget');
        });
    });

    it('rel should be updated from the element',  () => {
        fixture.whenStable().then(() => {
            hostComponent.element.config.uiStyles.attributes.rel = 'trel';
            expect(hostComponent.rel).toEqual('trel');
        });
    });

});