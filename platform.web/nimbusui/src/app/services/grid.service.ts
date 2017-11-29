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
