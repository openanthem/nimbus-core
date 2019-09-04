/*The MIT License (MIT)

Copyright (c) 2017 Ivan Radunovic <ivan@codingo.me>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/

'use strict';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { LoaderState } from './../components/platform/loader/loader.state';

/**
 * \@author reference https://github.com/ivanderbu2/angular-redux
 * \@whatItDoes
 *
 * \@howToUse
 *
 */

@Injectable()
export class LoaderService {
  loaderUpdate = new Subject<LoaderState>();
  loaderUpdate$ = this.loaderUpdate.asObservable();

  gridLoaderUpdate = new Subject<LoaderState>();
  gridLoaderUpdate$ = this.gridLoaderUpdate.asObservable();

  constructor() {}

  show() {
    this.loaderUpdate.next(<LoaderState>{ show: true });
  }

  hide() {
    this.loaderUpdate.next(<LoaderState>{ show: false });
  }

  showGridLoader(path: string) {
    this.gridLoaderUpdate.next(<LoaderState>{ show: true, path:path });
  }

  hideGridLoader(path: string) {
    this.gridLoaderUpdate.next(<LoaderState>{ show: false, path: path });
  }
}
