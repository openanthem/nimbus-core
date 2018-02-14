/**
 * @license
 * Copyright 2016-2018 the original author or authors.
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
import { CustomHttpClient } from './httpclient.service';
import { Injectable, EventEmitter } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { HttpHeaders } from '@angular/common/http';
import {RequestOptions, Request, RequestMethod} from '@angular/http';

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

    constructor(public http: CustomHttpClient) {
        this.addFile$ = new EventEmitter<any>();
        this.removeFile$ = new EventEmitter<any>();
    }

    uploadFile(file: File) {

        const formData: FormData = new FormData();
        formData.append('pfu', file, file.name);
        var url = file['postUrl'];
        
       return this.http.postFileData(url, formData)
            .subscribe(data => {
                // console.log("data", data);

                file['fileId'] = data;
                this.addFile$.next(file);
            },
                err => console.log(err),
                () => console.log('File uploaded ..')
            );
    }

}
