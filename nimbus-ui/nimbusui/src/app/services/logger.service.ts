/**
 * @license
 * Copyright 2016-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

import { Inject, Injectable } from '@angular/core';
import { JL } from 'jsnlog';
import { AppInitService } from './app.init.service';
import { ServiceConstants } from './service.constants';
import { CUSTOM_STORAGE, SessionStoreService } from './session.store';
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Injectable()
export class LoggerService {
  JL: JL.JSNLog;
  promiseDone: boolean = false;

  constructor(
    @Inject('JSNLOG') JL: JL.JSNLog,
    @Inject(CUSTOM_STORAGE) sessionStorage: SessionStoreService,
    private appInitService: AppInitService
  ) {
    this.JL = JL;
    var beforeSendOverride = function(xhr: XMLHttpRequest, json: any) {
      json.r = sessionStorage.get(ServiceConstants.SESSIONKEY);
      json.a = window.navigator.appVersion;
      json.v = window.navigator.vendor;
    };

    var logOptions = this.appInitService.getLogOptions();
    if (logOptions) {
      this.promiseDone = true;
      logOptions['url'] = ServiceConstants.APP_LOGS;
      logOptions['beforeSend'] = beforeSendOverride;
      if (logOptions['logToConsole']) {
        var consoleAppender = JL.createConsoleAppender('consoleAppender');
        consoleAppender.setOptions(logOptions);
        JL().setOptions({ appenders: [consoleAppender] });
      } else {
        var ajaxAppender = JL.createAjaxAppender('ajaxAppender');
        ajaxAppender.setOptions(logOptions);
        this.JL().setOptions({ appenders: [ajaxAppender] });
      }
    }
  }

  public info(message: string) {
    this.write(message, this.JL.getInfoLevel());
  }

  public warn(message: string) {
    this.write(message, this.JL.getWarnLevel());
  }

  public error(message: string) {
    this.write(message, this.JL.getErrorLevel());
  }

  public debug(message: string) {
    this.write(message, this.JL.getDebugLevel());
  }

  public trace(message: string) {
    this.write(message, this.JL.getTraceLevel());
  }

  public write(logMessage: string, logLevel: number) {
    if (this.promiseDone) {
      this.JL().log(logLevel, logMessage);
    } else {
      console.log(
        'Logger not initialized, printing to console. ' +
          'For session id ' +
          sessionStorage.get(ServiceConstants.SESSIONKEY) +
          ' : ' +
          logMessage
      );
    }
  }
}
