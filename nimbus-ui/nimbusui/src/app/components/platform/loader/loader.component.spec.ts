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