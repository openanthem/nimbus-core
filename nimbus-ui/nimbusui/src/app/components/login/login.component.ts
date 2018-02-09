import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { ServiceConstants } from './../../services/service.constants';

@Component({
    templateUrl: './login.component.html'
})
export class LoginCmp {

    public loginForm;
    public imagesPath: string;

    constructor(public fb: FormBuilder, private _route: ActivatedRoute, private _router: Router) {
    }

    ngOnInit() {
        this.loginForm = this.fb.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        });
        this.imagesPath = ServiceConstants.IMAGES_URL;
    }

    onSubmit() {
        if(this.loginForm.value.username === 'admin') {
            this._router.navigate(['/h/admindashboard']);
        } else if(this.loginForm.value.username === 'supervisor') {
             this._router.navigate(['/cs/a']);
        } else if(this.loginForm.value.username === 'training') {
            this._router.navigate(['/pc/a']);
        } else if(this.loginForm.value.username === 'member') {
            this._router.navigate(['/mem/a']);
        } else {
           this._router.navigate(['/h/cmdashboard']);
        }
    }

}
