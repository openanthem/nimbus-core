import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class WindowService {
    height$: Observable<number>;
    width$: Observable<number>;
    //create more Observables as and when needed for various properties
    hello: string = 'Hello';
    constructor() {
        let windowSize$ = new BehaviorSubject(getWindowSize());

        this.height$ = (windowSize$.pluck('height') as Observable<number>).distinctUntilChanged();
        this.width$ = (windowSize$.pluck('width') as Observable<number>).distinctUntilChanged();
        Observable.fromEvent(window, 'resize')
            .map(getWindowSize)
            .subscribe(windowSize$);
    }

}

function getWindowSize() {
    return {
        height: window.innerHeight,
        width: window.innerWidth
        //you can sense other parameters here
    };
}
