import { Injectable } from '@angular/core';

function _window(): any {
  // returning the global native browser window object
  return window;
}

@Injectable()
export class WindowRefService {
  get window(): any {
    return _window();
  }
}
