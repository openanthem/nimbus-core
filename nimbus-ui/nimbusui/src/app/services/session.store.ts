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
 *
 */

'use strict';

import { Inject, Injectable, InjectionToken } from '@angular/core';
import { StorageService } from 'angular-webstorage-service';
import { ServiceConstants } from './service.constants';

export const CUSTOM_STORAGE = new InjectionToken<StorageService>(
  'CUSTOM_STORAGE'
);

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Injectable()
export class SessionStoreService {
  constructor(@Inject(CUSTOM_STORAGE) private storage: StorageService) {}

  setSessionId(sessionId: string) {
    let sessionIdInStore = this.storage.get(ServiceConstants.SESSIONKEY);
    // New session, store the Session Id.
    if (sessionIdInStore === undefined || sessionIdInStore === null) {
      if (sessionId !== undefined) {
        this.storage.set(ServiceConstants.SESSIONKEY, sessionId);
      }
    } else if (sessionIdInStore !== sessionId) {
      // Check for Stale session in store. Clean all.
      this.removeAll();
    }
  }

  set(key: string, value: any) {
    this.storage.set(key, value);
  }

  get(key: string): any {
    let val = this.storage.get(key);
    return val;
  }

  remove(key: string) {
    this.storage.remove(key);
  }

  removeAll() {
    let obj = this.storage['storage'];
    if (obj) {
      for (var key in obj) {
        this.remove(key);
      }
    }
  }
}
