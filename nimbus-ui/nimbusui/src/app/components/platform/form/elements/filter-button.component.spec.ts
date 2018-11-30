'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { FilterButton } from './filter-button.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import * as data from '../../../../payload.json';
import { Param, Type, Model } from '../../../../shared/param-state';
import { ParamConfig, UiStyle, UiAttribute } from '../../../../shared/param-config';

let pageService, param, configService;

const declarations = [FilterButton];
const imports = [
       HttpModule,
       HttpClientTestingModule,
       StorageServiceModule
];
const providers = [
    { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
    { provide: 'JSNLOG', useValue: JL },
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    Location,
    PageService,
    CustomHttpClient,
    LoaderService,
    ConfigService,
    LoggerService,
    SessionStoreService,
    AppInitService
];
let fixture, hostComponent;
describe('FilterButton', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

       let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);

    beforeEach(() => {
      fixture = TestBed.createComponent(FilterButton);
      hostComponent = fixture.debugElement.componentInstance;
      hostComponent.element = param;
      pageService = TestBed.get(PageService);
      configService = TestBed.get(ConfigService);
    });

    it('should create the FilterButton', async(() => {
      expect(hostComponent).toBeTruthy();
    }));

    // it('ngOnInit() should call loadLabelConfig()', async(() => {
    //   const spy = spyOn(hostComponent as any, 'loadLabelConfig').and.returnValue('');
    //   const config = new ParamConfig(configService);
    //   config.uiStyles = new UiStyle();
    //   config.uiStyles.attributes = new UiAttribute();
    //   config.uiStyles.attributes.alias = 'Button';
    //   spyOn(configService, 'getViewConfigById').and.returnValue(config);
    //   hostComponent.filterButton = new Param(configService);
    //   const nestedParam = new Param(configService);
    //   hostComponent.filterButton.type = new Type(configService);
    //   hostComponent.filterButton.type.model = new Model(configService);
    //   hostComponent.filterButton.type.model.params = [nestedParam];
    //   hostComponent.ngOnInit();
    //   expect(spy).toHaveBeenCalled();
    // }));

    // it('ngOnInit() should update the fText property', async(() => {
    //   spyOn(hostComponent as any, 'loadLabelConfig').and.returnValue('');
    //   const config = new ParamConfig(configService);
    //   config.uiStyles = new UiStyle();
    //   config.uiStyles.attributes = new UiAttribute();
    //   config.uiStyles.attributes.alias = 'TextBox';
    //   spyOn(configService, 'getViewConfigById').and.returnValue(config);
    //   hostComponent.filterButton = new Param(configService);
    //   const nestedParam = new Param(configService);
    //   hostComponent.filterButton.type = new Type(configService);
    //   hostComponent.filterButton.type.model = new Model(configService);
    //   hostComponent.filterButton.type.model.params = [nestedParam];
    //   const test = new Param(configService);
    //   config.uiStyles = new UiStyle();
    //   config.uiStyles.attributes = new UiAttribute();
    //   config.uiStyles.attributes.alias = 'TextBox';
    //   hostComponent.ngOnInit();
    //   expect(hostComponent.fText).toEqual(test);
    // }));

    // it('ngOnInit() should subscribe to buttonClickEvent', async(() => {
    //   spyOn(hostComponent.buttonClickEvent, 'subscribe').and.callThrough();
    //   hostComponent.filterButton = new Param(configService);
    //   hostComponent.filterButton.type = new Type(configService);
    //   hostComponent.filterButton.type.model = new Model(configService);
    //   hostComponent.filterButton.type.model.params = [];
    //   hostComponent.ngOnInit();
    //   expect(hostComponent.buttonClickEvent.subscribe).toHaveBeenCalled();
    // }));

    // it('ngOnInit() should call pageservice.processEvent()', async(() => {
    //   spyOn(pageService, 'processEvent').and.returnValue('');
    //   hostComponent.filterButton = new Param(configService);
    //   hostComponent.filterButton.type = new Type(configService);
    //   hostComponent.filterButton.type.model = new Model(configService);
    //   hostComponent.filterButton.type.model.params = [];
    //   hostComponent.ngOnInit();
    //   const eve = { fbutton: { path: '', config: { uiStyles: { attributes: { b: '', method: '' } } } } };
    //   hostComponent.buttonClickEvent.emit(eve);
    //   expect(pageService.processEvent).toHaveBeenCalled();
    // }));

    it('emitEvent() should call buttonClickEvent.emit()', async(() => {
      spyOn(hostComponent.buttonClickEvent, 'emit').and.callThrough();
      hostComponent.emitEvent('test');
      expect(hostComponent.buttonClickEvent.emit).toHaveBeenCalled();
    }));

});
