/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Subject }    from 'rxjs/Subject';

import 'rxjs/Rx';

@Injectable()
export class GridService {

    eventUpdate = new Subject<any>();
    eventUpdate$ = this.eventUpdate.asObservable();
    constructor(private http: Http) {}

    setSummaryObject(object: any) {
         this.eventUpdate.next(object);
    }

    getSummaryDetails(id:string,url:string) {
        // return this.http.get('app/resources/data/cars-medium.json')
        //             .toPromise()
        //             .then(res => <Car[]> res.json().data)
        //             .then(data => { return data; });
        return this.http.get(url).map(res =>  res.json().data);
    }
}
