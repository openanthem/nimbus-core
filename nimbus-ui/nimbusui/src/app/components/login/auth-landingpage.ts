/**
 * @license
 * Copyright 2016-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoggerService } from '../../services/logger.service';
import { ServiceConstants } from '../../services/service.constants';
import { SessionStoreService } from '../../services/session.store';
import { RedirectHandle } from '../../shared/app-redirecthandle.interface';

/**
 * \@author Dinakar.Meda
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Component({
  template: ``
})
export class LandingPage {
  constructor(
    private http: CustomHttpClient,
    private _router: Router,
    private _logger: LoggerService,
    private sessionStore: SessionStoreService
  ) {
    const url: string = ServiceConstants.WEB_APP_POST_LOGIN_URL;
    this.http.get(url).subscribe(data => {
      const redirect = new RedirectHandle().deserialize(data);
      this.sessionStore.set(ServiceConstants.AUTH_TOKEN_KEY, redirect.token);
      ServiceConstants.APP_COMMAND_URL = redirect.commandUrl;
      this._router.navigateByUrl(redirect.redirectRoute);
    });
  }
}
