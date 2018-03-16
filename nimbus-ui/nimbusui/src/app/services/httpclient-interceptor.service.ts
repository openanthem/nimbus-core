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
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpEvent, HttpRequest, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { ServiceConstants } from './service.constants';

/**
 * \@author Swetha.Vemuri
 * \@whatItDoes
 * Custom implementation of Angular's HTTPInterceptor, to add custom logic for request/response/error handling
 *
 * \@howToUse
 *
 */
@Injectable()
export class CustomHttpClientInterceptor implements HttpInterceptor {

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).do((event: HttpEvent<any>) => {
          }, (err: any) => {
            if (err instanceof HttpErrorResponse) {
                /*  Currently, the server side websecurityconfig redirects to /login when session in expired.
                    Although the original response is HttpErrorResponse, the redirected url is in 200 status.
                    Hence the additional check for 200 & err.url.
                    This will have to be refactored when Siteminder authentication replaces the form based authentication. */
                if (err.status === 302 || err.status === 403 || (err.status === 200 && err.url === `${ServiceConstants.LOGIN_URL}`)) {
                    window.location.href = `${ServiceConstants.CLIENT_BASE_URL}logout`;
                }
            }
        });
    }
}
