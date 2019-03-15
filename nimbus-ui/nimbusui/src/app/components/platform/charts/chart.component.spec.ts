/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import { NmMessageService } from './../../../services/toastmessage.service';
'use strict';
import { PageService } from './../../../services/page.service';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { Subject } from 'rxjs';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { ChartModule } from 'primeng/chart';
import { WebContentSvc } from './../../../services/content-management.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoggerService } from './../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from './../../../services/session.store';
import { AppInitService } from './../../../services/app.init.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import { NmChart } from './chart.component';
import { chartMockParam } from 'mockdata';
import { TooltipComponent } from './../tooltip/tooltip.component';
import { InputLabel } from '../form/elements/input-label.component';

let  param;

class MockPageService {
    gridValueUpdate$: Subject<any>;
    validationUpdate$: Subject<any>;
    eventUpdate$: Subject<any>;

    constructor() {
        this.gridValueUpdate$ = new Subject();
        this.validationUpdate$ = new Subject();
        this.eventUpdate$ = new Subject();

    }

    emitMockEventUpdate(a) {
        this.eventUpdate$.next(a);
    }
}

class MockWebContentSvc {
    findLabelContent(param1) {
      const test = {
        text: 'testing'
      };
      return test;
    }
}

let fixture, hostComponent, pageService;
const declarations = [ NmChart, InputLabel, TooltipComponent ];
const imports = [ChartModule];
const providers = [
   CustomHttpClient,
   {provide: PageService, useClass: MockPageService},
   { provide: WebContentSvc, useClass: MockWebContentSvc },
   { provide: 'JSNLOG', useValue: JL },
   LoggerService,
   NmMessageService,
   AppInitService
];

describe('ChartComponent', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NmChart);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = chartMockParam;
    pageService = TestBed.get(PageService);
  });

  it('should create the Section',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('Convert to chart datastructure',  async(() => {
    hostComponent.ngOnInit();
    fixture.detectChanges();
    pageService.emitMockEventUpdate(chartMockParam);
    fixture.detectChanges();
    expect(hostComponent.data.labels.length).toEqual(11);
    expect(hostComponent.data.datasets.length).toEqual(2);
    expect(hostComponent.data.datasets[0].backgroundColor).toBeTruthy();
    expect(hostComponent.data.datasets[0].label).toEqual('SEARCH action');
    expect(hostComponent.data.datasets[1].label).toEqual('NEW action');
  }));

});