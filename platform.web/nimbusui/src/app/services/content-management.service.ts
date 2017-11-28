'use strict';

import { Injectable, EventEmitter } from '@angular/core';
import { Http, Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/share';
import { ServiceConstants } from './service.constants';

// Web Content Service
@Injectable()
export class WebContentSvc {

    content$: EventEmitter<any>;
	constructor(public http: Http) {
        // Create Observable Stream to output our data
        this.content$ = new EventEmitter();
	}

    logError(err) {
		console.error('Failure making server call : ' + JSON.stringify(err));
	}

    getContent(id: string) {
        let headers = new Headers({'Content-Type': 'application/json'});
        let options = new RequestOptions({headers: headers});
        this.http.get(ServiceConstants.WEB_CONTENT_URL + id, options)
			.map(res => res.json())
			.subscribe(data => {
                // Push the new config into the Observable stream
                this.content$.next(data);
            },
				err =>  {
                    this.logError(err);
                    this.content$.next({
                        'id': id, 
                        'label':id
                    });
                },
				() => console.log('Web Content query completed..')
			);
    }
}
