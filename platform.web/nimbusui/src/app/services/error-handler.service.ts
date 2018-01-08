/**
 * @license
 * Copyright Anthem Inc. All Rights Reserved.
 *
 * This source code is released under version 2.0 of the Apache License.
 * The LICENSE information can be found at http://www.apache.org/licenses/LICENSE-2.0
 */

// trackjs.handler.ts
import { ErrorHandler } from '@angular/core';
declare var trackJs: any;
//TODO convert to service
class TrackJsErrorHandler extends ErrorHandler {
  handleError(error:any) {
    // Add the error message to the telemetry timeline. 
    // It can occasionally have useful additional context.
    console.error(error);

    // Assumes we have already loaded and configured TrackJS*
    if (trackJs) {
      trackJs.track(error.originalError); // Send the native error object to TrackJS
    }
  }
}
export default TrackJsErrorHandler;
