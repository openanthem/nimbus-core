/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

'use strict';

import { async, TestBed } from '@angular/core/testing';
import { JL } from 'jsnlog';
import { chartMockParam } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { ChartModule } from 'primeng/chart';
import { TooltipModule } from 'primeng/primeng';
import { Subject } from 'rxjs';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { setup } from '../../../setup.spec';
import { InputLabel } from '../form/elements/input-label.component';
import { AppInitService } from './../../../services/app.init.service';
import { LoggerService } from './../../../services/logger.service';
import { PageService } from './../../../services/page.service';
import { NmMessageService } from './../../../services/toastmessage.service';
import { TooltipComponent } from './../tooltip/tooltip.component';
import { NmChart } from './chart.component';

let param;

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

let fixture, hostComponent, pageService;
const declarations = [NmChart, InputLabel, TooltipComponent];
const imports = [ChartModule, TooltipModule];
const providers = [
  CustomHttpClient,
  { provide: PageService, useClass: MockPageService },
  { provide: 'JSNLOG', useValue: JL },
  LoggerService,
  NmMessageService,
  AppInitService
];

describe('ChartComponent', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NmChart);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = chartMockParam;
    pageService = TestBed.get(PageService);
  });

  it('should create the Section', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('Convert to chart datastructure', async(() => {
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
