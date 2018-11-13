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

describe('FilterButton', () => {

    configureTestSuite();
    setup(FilterButton, declarations, imports, providers);
    param = (<any>data).payload;

    beforeEach(async function(this: TestContext<FilterButton>){
    this.hostComponent.element = param;
    pageService = TestBed.get(PageService);
    configService = TestBed.get(ConfigService);
    });

    it('should create the FilterButton', async function(this: TestContext<FilterButton>) {
      expect(this.hostComponent).toBeTruthy();
    });

    it('ngOnInit() should call loadLabelConfig()', async function(this: TestContext<FilterButton>) {
      const spy = spyOn(this.hostComponent as any, 'loadLabelConfig').and.returnValue('');
      const config = new ParamConfig(configService);
      config.uiStyles = new UiStyle();
      config.uiStyles.attributes = new UiAttribute();
      config.uiStyles.attributes.alias = 'Button';
      spyOn(configService, 'getViewConfigById').and.returnValue(config);
      this.hostComponent.filterButton = new Param(configService);
      const nestedParam = new Param(configService);
      this.hostComponent.filterButton.type = new Type(configService);
      this.hostComponent.filterButton.type.model = new Model(configService);
      this.hostComponent.filterButton.type.model.params = [nestedParam];
      this.hostComponent.ngOnInit();
      expect(spy).toHaveBeenCalled();
    });

    it('ngOnInit() should update the fText property', async function(this: TestContext<FilterButton>) {
      spyOn(this.hostComponent as any, 'loadLabelConfig').and.returnValue('');
      const config = new ParamConfig(configService);
      config.uiStyles = new UiStyle();
      config.uiStyles.attributes = new UiAttribute();
      config.uiStyles.attributes.alias = 'TextBox';
      spyOn(configService, 'getViewConfigById').and.returnValue(config);
      this.hostComponent.filterButton = new Param(configService);
      const nestedParam = new Param(configService);
      this.hostComponent.filterButton.type = new Type(configService);
      this.hostComponent.filterButton.type.model = new Model(configService);
      this.hostComponent.filterButton.type.model.params = [nestedParam];
      const test = new Param(configService);
      config.uiStyles = new UiStyle();
      config.uiStyles.attributes = new UiAttribute();
      config.uiStyles.attributes.alias = 'TextBox';
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.fText).toEqual(test);
    });

    it('ngOnInit() should subscribe to buttonClickEvent', async function(this: TestContext<FilterButton>) {
      spyOn(this.hostComponent.buttonClickEvent, 'subscribe').and.callThrough();
      this.hostComponent.filterButton = new Param(configService);
      this.hostComponent.filterButton.type = new Type(configService);
      this.hostComponent.filterButton.type.model = new Model(configService);
      this.hostComponent.filterButton.type.model.params = [];
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.buttonClickEvent.subscribe).toHaveBeenCalled();
    });

    it('ngOnInit() should call pageservice.processEvent()', async function(this: TestContext<FilterButton>) {
      spyOn(pageService, 'processEvent').and.returnValue('');
      this.hostComponent.filterButton = new Param(configService);
      this.hostComponent.filterButton.type = new Type(configService);
      this.hostComponent.filterButton.type.model = new Model(configService);
      this.hostComponent.filterButton.type.model.params = [];
      this.hostComponent.ngOnInit();
      const eve = { fbutton: { path: '', config: { uiStyles: { attributes: { b: '', method: '' } } } } };
      this.hostComponent.buttonClickEvent.emit(eve);
      expect(pageService.processEvent).toHaveBeenCalled();
    });

    it('emitEvent() should call buttonClickEvent.emit()', async function(this: TestContext<FilterButton>) {
      spyOn(this.hostComponent.buttonClickEvent, 'emit').and.callThrough();
      this.hostComponent.emitEvent('test');
      expect(this.hostComponent.buttonClickEvent.emit).toHaveBeenCalled();
    });

});
