'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { Label } from './label.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { WebContentSvc } from '../../../services/content-management.service';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { AppInitService } from '../../../services/app.init.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { Param } from '../../../shared/param-state';

let param: Param;

const declarations = [
  Label,
  TooltipComponent
];
const imports = [
   HttpClientModule,
    HttpModule,
    StorageServiceModule
];
const providers = [
{ provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
{ provide: 'JSNLOG', useValue: JL },
{ provide: LocationStrategy, useClass: HashLocationStrategy },
WebContentSvc,
PageService,
CustomHttpClient,
SessionStoreService,
LoaderService,
ConfigService,
LoggerService,
AppInitService,
Location
];
let fixture, hostComponent;
describe('Label', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

     let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);

  beforeEach( async(() => {
    fixture = TestBed.createComponent(Label);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = param;
  }));

  it('should create the Label', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('app.cssClass should return labelClass',async(() => {
    hostComponent.labelClass = 'test';
    hostComponent.getCssClass = () => { return 'a' }
    expect(hostComponent.cssClass).toEqual('test')
  }));

});