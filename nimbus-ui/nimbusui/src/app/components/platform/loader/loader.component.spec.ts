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

describe('LoaderComponent', () => {

  configureTestSuite();
  setup(LoaderComponent, declarations, imports, providers);

  beforeEach(async function(this: TestContext<LoaderComponent>){
    loaderService = TestBed.get(LoaderService);
    (this.hostComponent as any).subscription = {unsubscribe: () => {}} as Subscription;
  });

  it('should create the LoaderComponent', async function (this: TestContext<LoaderComponent>) {
      expect(this.hostComponent).toBeTruthy();
  });
  
  it('show property should be updated as true', async function (this: TestContext<LoaderComponent>) {
    this.hostComponent.ngOnInit();
    loaderService.show();
    expect(this.hostComponent.show).toEqual(true);
  });

});