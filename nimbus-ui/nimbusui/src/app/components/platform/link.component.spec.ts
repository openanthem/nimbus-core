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

 let fixture, hostComponent;
 
describe('Link', () => {

    configureTestSuite(() => {
        setup( declarations, imports, providers);
    });
    
       let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);
  
    beforeEach(() => {
        fixture = TestBed.createComponent(Link);
        hostComponent = fixture.debugElement.componentInstance;
        hostComponent.element = param;
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