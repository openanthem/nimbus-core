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

import { EventEmitter, Injectable, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { throwError as observableThrowError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { CustomHttpClient } from './httpclient.service';
import { LoggerService } from './logger.service';

/**
 * \@author Dinakar.Meda
 * \@author Vivek.Kamineni
 * \@whatItDoes
 *
 * \@howToUse
 *
 */
@Injectable()
export class FileService {
  addFile$: EventEmitter<any>;
  removeFile$: EventEmitter<any>;
  @Output() errorEmitter$ = new EventEmitter();

  private _metaData: string[];

  get metaData(): string[] {
    return this._metaData;
  }
  set metaData(metaData: string[]) {
    this._metaData = metaData;
  }

  constructor(public http: CustomHttpClient, private logger: LoggerService) {
    this.addFile$ = new EventEmitter<any>();
    this.removeFile$ = new EventEmitter<any>();
  }

  uploadFile(file: File, form: FormGroup) {
    const formData: FormData = new FormData();
    formData.append('pfu', file, file.name);
    this.metaData.forEach(item => formData.append(item, form.value[item]));

    var url = file['postUrl'];

    return this.http.postFileData(url, formData).pipe(
      map(data => data['fileId']),
      catchError(err => {
        const errObj = {
          url: url,
          message: 'upload is failing',
          error: err
        };
        this.errorEmitter$.next(err);
        this.logger.error(JSON.stringify(errObj));
        return observableThrowError(err);
      })
    );
  }
}
