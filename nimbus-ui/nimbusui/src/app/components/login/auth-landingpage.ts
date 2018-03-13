import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    template: ` 
    
    <button class = "btn btn-primary" type="button" (click)="routeCaseManager()">Case Manager</button>
    <button type="button"  class = "btn btn-danger" (click)="routeAdmin()">Admin</button>
   `
})
export class LandingPage {

    constructor(private _route: ActivatedRoute, private _router: Router) {
    }

    ngOnInit() {
    }

    routeCaseManager() {
         this._router.navigate(['/main/f']);
    }

    routeAdmin() {
         this._router.navigate(['/main/a']);
    }

}
