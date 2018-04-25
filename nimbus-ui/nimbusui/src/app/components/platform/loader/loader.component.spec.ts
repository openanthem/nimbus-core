'use strict';
import { TestBed, async } from '@angular/core/testing';

import { LoaderComponent } from './loader.component';
import { LoaderService } from './../../../services/loader.service';
import { Subject } from 'rxjs/Rx';

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

describe('LoaderComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        LoaderComponent
       ],
       providers: [ 
           { provide: LoaderService, useClass: MockLoaderService }
        ]
    }).compileComponents();
    loaderService = TestBed.get(LoaderService);
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(LoaderComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(LoaderComponent);
    const app = fixture.debugElement.componentInstance;
    app.ngOnInit();
    loaderService.show();
    expect(app.show).toEqual(true);
  }));

});