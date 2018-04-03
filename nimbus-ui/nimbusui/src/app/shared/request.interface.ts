import { Observable } from 'rxjs/Observable';
import { Param } from "./app-config.interface";
import { Subject } from 'rxjs/Subject';

export class RequestContainer {
    path: string;
    method: string;
    payload: any;
    subscription: Subject<any>;

    constructor(path:string,method: string, payload: any, subscription: Subject<any>) {
        this.path = path;
        this.method = method;
        this.payload = payload;
        this.subscription = subscription;
    }
  
}