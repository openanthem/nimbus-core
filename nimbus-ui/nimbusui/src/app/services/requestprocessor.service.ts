import { GenericDomain } from './../model/generic-domain.model';
import { Subject } from 'rxjs/Rx';
import { PageService } from "./page.service";
import { Param } from "../shared/app-config.interface";
import { RequestContainer } from '../shared/request.interface';
import { Injectable } from '@angular/core';

@Injectable()
export class RequestProcessorService {

    requestHolder = new Subject<RequestContainer>();
    requestHolder$ = this.requestHolder.asObservable();

    private requestQueue :RequestContainer[] = [];

    constructor(private pageService: PageService) {
        this.requestHolder$.subscribe(holder => this.processRequest(holder));
    }

    invoke(path, method, payload) {
        return this.addRequestToQueue(path, method, payload);
    }

    private processRequest(request:RequestContainer) {
        //const req = this.pageService.processEvent(request.path, '$execute', new GenericDomain(), 'POST');
        // console.log("processing"+request.path);
        //const req = this.pageService.executeCalls(request.path, '$execute', new GenericDomain(), 'POST').finally(() => this.fetchNext());
                     
            // const sub = request.subscription;
            // sub.switchMap(req);
        
    }

    private fetchNext() {
        if (this.requestQueue.length) {
          this.processRequest(this.requestQueue.shift());
        }
    }

    private addRequestToQueue(path:string,method:string,payload:any) {
        const sub = new Subject<any>();
        const request = new RequestContainer(path,'POST',null,sub);
        if (this.requestQueue.length === 0) {
          this.requestHolder.next(request);
        } else {
          this.requestQueue.push(request);
        }
        return sub;
    }
}