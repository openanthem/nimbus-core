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

'use strict';
import { TestBed, async } from '@angular/core/testing';

import { LoaderComponent } from './loader.component';
import { LoaderService } from './../../../services/loader.service';
import { Subject, Subscription } from 'rxjs/Rx';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';

class MockLoaderService {
    public loaderUpdate: Subject<any>;
    constructor() {
        this.loaderUpdate = new Subject<any>();
    }
    show() {
        const message = {
            show: true
        };
        this.loaderUpdate.next(message);
    }

}

let loaderService;

const declarations = [  LoaderComponent ];
const providers = [      { provide: LoaderService, useClass: MockLoaderService }  ];
const imports = [];
let fixture, hostComponent;
describe('LoaderComponent', () => {

    configureTestSuite(() => {
        setup( declarations, imports, providers);
    });
    

  beforeEach(() => {
    fixture = TestBed.createComponent(LoaderComponent);
    hostComponent = fixture.debugElement.componentInstance;
    loaderService = TestBed.get(LoaderService);
    (hostComponent as any).subscription = {unsubscribe: () => {}} as Subscription;
  });

  it('should create the LoaderComponent', () => {
      expect(hostComponent).toBeTruthy();
  });
  
//   it('show property should be updated as true', () => {
//     hostComponent.ngOnInit();
//     loaderService.show();
//     expect(hostComponent.show).toEqual(true);
//   });

});