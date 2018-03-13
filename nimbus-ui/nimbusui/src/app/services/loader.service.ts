import { LoaderState } from './../components/platform/loader/loader.state';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class LoaderService {

loaderUpdate = new Subject<LoaderState>();
loaderUpdate$ = this.loaderUpdate.asObservable();

constructor() { }

show() {
        this.loaderUpdate.next(<LoaderState>{show: true});
    }

hide() {
        this.loaderUpdate.next(<LoaderState>{show: false});
    }

}