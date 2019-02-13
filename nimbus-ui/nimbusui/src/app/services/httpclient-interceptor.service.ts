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
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ServiceConstants } from './service.constants';
import { ExecuteException, ExecuteResponse, MultiOutput } from '../shared/app-config.interface';
import { PageService } from './page.service';
import { ConfigService } from './config.service';
import { SessionStoreService } from './session.store';
import { RedirectHandle } from '../shared/app-redirecthandle.interface';
import { NmMessageService } from './toastmessage.service';
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
    constructor(private msgSvc: NmMessageService, private configSvc: ConfigService, private sessionStore: SessionStoreService) {}
    /**
     * Http interceptor to handle custom implementation of request & error handling.
     * On a failure of http response, the error handler event is emitted based on the
     * exception in response. Framework returns all exceptions in a predefined format of ExecuteException.
     * To clear the event emitter of the web service exceptions, on each successful request an empty
     * exception object is emitted through the error handler event emitter.
     * @param req
     * @param next
     */
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // Token generated for the first time, and not in session yet. Retrieve from header and store in session
        if (! this.sessionStore.get(ServiceConstants.AUTH_TOKEN_KEY)) {
            const token = req.headers.get(ServiceConstants.TOKEN_HEADER_KEY);
            // JWT token will be in the format Authorization : Bearer <token>
            const token_parts = [];
            if (token) {
                token.split(' ').forEach(
                    part => { token_parts.push(part);
                });
                if (token_parts && token_parts[0] === 'Bearer' && token_parts[1]) {
                    this.sessionStore.set(ServiceConstants.AUTH_TOKEN_KEY, token_parts[1]);
                }
            }
        }
        return next.handle(req).pipe(tap((event: HttpEvent<any>) => {
            const exception = new ExecuteException();
            exception.message = null;
            exception.code = null;
            this.msgSvc.notifyErrorEvent(exception);
          }, (err: any) => {
                if (err instanceof HttpErrorResponse) {
                    /*  Currently, the server side websecurityconfig redirects to /login when session in expired.
                        Although the original response is HttpErrorResponse, the redirected url is in 200 status.
                        Hence the additional check for 200 & err.url.
                        This will have to be refactored when Siteminder authentication replaces the form based authentication. */
                    if (err.status === 302 || err.status === 403 || (err.status === 200 && err.url === `${ServiceConstants.LOGIN_URL}`)) {
                        this.sessionStore.removeAll();
                        window.location.href = `${ServiceConstants.LOGOUT_URL}`;
                    }
                    const execResp  = new ExecuteResponse(this.configSvc).deserialize(err.error);
                    if (execResp != null && execResp.result != null && execResp.result[0] != null) {
                        const exception: ExecuteException = execResp.result[0].executeException;
                        this.msgSvc.notifyErrorEvent(exception);
                    }
                }
        }));
    }
}
