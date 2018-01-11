/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
