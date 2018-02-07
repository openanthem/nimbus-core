'use strict';

import { Injectable, EventEmitter } from '@angular/core';
import { Http, Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/share';

// Login service
@Injectable()
export class LoginSvc {

    login$: EventEmitter<any>;
    loginDataStore: any[];

	constructor(public http: Http) {
        // Create Observable Stream to output our data
        this.login$ = new EventEmitter();
        this.loginDataStore = [];
	}

    logError(err) {
		console.error('Failure making server call : ' + JSON.stringify(err));
	}

    login(user: string, password: string, remember: boolean) {
        let headers = new Headers({'Content-Type': 'application/x-www-form-urlencoded'});
        let body = 'username='+user+'&password='+password+'&rememberme='+remember;
        let options = new RequestOptions({headers: headers});
        this.http.post('http://30.138.86.234:8080/login/platform', body, options)
			.map(res => res.json())
			.subscribe(data => {
                //console.log('data from login... ' + data);
                // Push the session into the Observable stream
                this.login$.next(data);
            },
				err => this.logError(err),
				() => console.log('Config query completed..')
			);
    }
}
