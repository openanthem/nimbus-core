'use strict';
import { TestBed, async } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

import { LoginCmp } from './login.component';

class MockRouter {
    navigate() { }
  }

describe('LoginCmp', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          LoginCmp
       ],
       imports: [
           ReactiveFormsModule,
           RouterTestingModule
       ],
       providers: [ {provide: Router, useClass: MockRouter} ]
    }).compileComponents();
  }));

  it('should create the LoginCmp', async(() => {
    const fixture = TestBed.createComponent(LoginCmp);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('onSubmit() should navigate to /h/admindashboard for admin', async(() => {
    const fixture = TestBed.createComponent(LoginCmp);
    const app = fixture.debugElement.componentInstance;
    app.loginForm = {
        value: {
            username: 'admin'
        }
    };
    const _router = TestBed.get(Router);
    spyOn(_router, 'navigate');
    app.onSubmit();
    expect(_router.navigate).toHaveBeenCalledWith(['/h/admindashboard']);
  }));

  it('onSubmit() should navigate to /cs/a for supervisor', async(() => {
    const fixture = TestBed.createComponent(LoginCmp);
    const app = fixture.debugElement.componentInstance;
    app.loginForm = {
        value: {
            username: 'supervisor'
        }
    };
    const _router = TestBed.get(Router);
    spyOn(_router, 'navigate');
    app.onSubmit();
    expect(_router.navigate).toHaveBeenCalledWith(['/cs/a']);
  }));

  it('onSubmit() should navigate to /pc/a for training', async(() => {
    const fixture = TestBed.createComponent(LoginCmp);
    const app = fixture.debugElement.componentInstance;
    app.loginForm = {
        value: {
            username: 'training'
        }
    };
    const _router = TestBed.get(Router);
    spyOn(_router, 'navigate');
    app.onSubmit();
    expect(_router.navigate).toHaveBeenCalledWith(['/pc/a']);
  }));

  it('onSubmit() should navigate to /h/vrCSLandingPage for default', async(() => {
    const fixture = TestBed.createComponent(LoginCmp);
    const app = fixture.debugElement.componentInstance;
    app.loginForm = {
        value: {
            username: ''
        }
    };
    const _router = TestBed.get(Router);
    spyOn(_router, 'navigate');
    app.onSubmit();
    expect(_router.navigate).toHaveBeenCalledWith(['/h/vrCSLandingPage']);
  }));

  it('onSubmit() should navigate to member for member', async(() => {
    const fixture = TestBed.createComponent(LoginCmp);
    const app = fixture.debugElement.componentInstance;
    app.loginForm = {
        value: {
            username: 'member'
        }
    };
    const _router = TestBed.get(Router);
    spyOn(_router, 'navigate');
    app.onSubmit();
    expect(_router.navigate).toHaveBeenCalledWith(['/mem/a']);
  }));

  it('LoginForm should be created', async(() => {
    const fixture = TestBed.createComponent(LoginCmp);
    const app = fixture.debugElement.componentInstance;
    app.ngOnInit();
    expect(app.loginForm).toBeTruthy();
  }));

});