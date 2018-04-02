'use strict';
import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
// import {Location} from "@angular/common";
import { Router } from '@angular/router';
import { LandingPage } from './auth-landingpage';

class MockRouter {
  navigate() { }
}

describe('LandingPage', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        LandingPage
        ],
        imports: [ RouterTestingModule ],
        providers: [ {provide: Router, useClass: MockRouter} ]
    }).compileComponents();
  }));

  it('should create the LandingPage', async(() => {
    const fixture = TestBed.createComponent(LandingPage);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

it('routeCaseManager should navigate to /main/f', async(() => {
    const fixture = TestBed.createComponent(LandingPage);
    const app = fixture.debugElement.componentInstance;
    const _router = TestBed.get(Router);
    spyOn(_router, 'navigate');
    app.routeCaseManager();
    expect(_router.navigate).toHaveBeenCalled();
  }));

it('routeAdmin should navigate to /main/a', async(() => {
    const fixture = TestBed.createComponent(LandingPage);
    const app = fixture.debugElement.componentInstance;
    const _router = TestBed.get(Router);
    spyOn(_router, 'navigate');
    app.routeAdmin();
    expect(_router.navigate).toHaveBeenCalled();
  }));

});

