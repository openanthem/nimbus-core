/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

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
