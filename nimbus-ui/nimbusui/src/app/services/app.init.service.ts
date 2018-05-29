import { ServiceConstants } from './service.constants';
import { Injectable, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/platform-browser';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/toPromise';

@Injectable()
export class AppInitService {
    headers: Headers;
    options: RequestOptions;
    logOptions:any;

    constructor(@Inject(DOCUMENT) private document: any, private http: Http) { 
        this.headers = new Headers({ 'Content-Type': 'application/json'});
        this.options = new RequestOptions({ headers: this.headers });
    }

    loadConfig(): Promise<any> {
        ServiceConstants.APP_HOST = this.document.location.hostname;
        ServiceConstants.APP_PORT = this.document.location.port;
        ServiceConstants.APP_CONTEXT = this.document.location.pathname.split('/').splice(1, 1);
        ServiceConstants.LOCALE_LANGUAGE = "en-US"; //TODO This locale should be read dynamically. Currently defaulting to en-US
        ServiceConstants.APP_PROTOCOL = this.document.location.protocol;
        const docObj = {
            message: 'Values are updated from document',
            host: ServiceConstants.APP_HOST,
            port: ServiceConstants.APP_PORT,
            appContext: ServiceConstants.APP_CONTEXT,
            appProtocol: ServiceConstants.APP_PROTOCOL
        };
        console.log(JSON.stringify(docObj));
        
        return this.http
            .get(ServiceConstants.APP_LOG_OPTIONS, this.options)
            .toPromise()
            .then(res => {
                this.logOptions = res.json();
            })
            .catch(err => {
                this.logOptions = {
                    'bufferSize': 20, 
                    'storeInBufferLevel': 1000,
                    'logToConsole': true,
                    'level': 4000,
                    'sendWithBufferLevel': 6000
                };
            });
    }

    getLogOptions() {
        return this.logOptions;
    }
}
