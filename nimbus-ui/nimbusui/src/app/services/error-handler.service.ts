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
