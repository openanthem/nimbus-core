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
/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
export class ServiceConstants {
    private static hostName: string;
    private static port: string;
    private static appcontext: string;
    private static protocol: string;
    private static locale : string;
    private static logOptions: Object;
    private static commandurl: string;

    public static get PAGE_INDEX() : number { return 0;}
    public static get PAGE_SIZE() : number { return 20;}
    public static get URL_SEPERATOR(): string { return '/'; }
    public static get PATH_SEPARATOR(): string { return '/'; }
    public static get WS_SUBSCRIBE_Q(): string { return '/user/queue/updates'; }
    public static get LOCALE_LANGUAGE() : string { return this.locale;}
    public static set LOCALE_LANGUAGE(locale : string ) {this.locale = locale;}

    /* Enable for stopgap server */
    public static get BASE_URL(): string    { return this.APP_HOST_URL+this.WEB_CONTENT_PORT; }
    public static get LOGIN_URL(): string { return this.APP_HOST_URL+this.APP_PORT+'/'+this.APP_CONTEXT+'/login'; }
    public static get IMAGE_URL(): string { return this.APP_HOST_URL+this.APP_PORT+'/'+this.APP_CONTEXT+'/'; }
    public static get LOGOUT_URL(): string { return this.APP_HOST_URL+this.APP_PORT+'/'+this.APP_CONTEXT+'/logout'; }
    public static get APP_REFRESH(): string { return this.APP_HOST_URL+this.APP_PORT+'/'+this.APP_CONTEXT+'/processLogin'; }
    public static get PLATFORM_BASE_URL(): string    { return this.APP_BASE_URL + this.APP_COMMAND_URL + this.PLATFORM_SEPARATOR; }
    public static get CLIENT_BASE_URL(): string    { return this.APP_HOST_URL+this.APP_PORT+'/'; }
    public static get WS_BASE_URL(): string { return 'ws://'+this.APP_HOST+this.WS_PORT+'/updates'; }
    public static get APP_LOGS(): string { return this.APP_HOST_URL+this.APP_PORT+'/log'; }
    public static get APP_LOG_OPTIONS(): string { return this.APP_HOST_URL+this.APP_PORT+'/log/options'; }
    public static get WEB_CONTENT_URL(): string    { return this.BASE_URL+'/web_content_server/'; }
    public static get CONTENT_MANAGEMENT(): string { return this.APP_HOST_URL+this.WEB_CONTENT_PORT+'/contentManagement'; }

    public static set APP_HOST(name:string) { this.hostName = name;}
    public static set APP_PORT(port:string) { this.port = port;}
    public static set APP_PROTOCOL(protocol:string) {this.protocol = protocol;}
    public static set APP_CONTEXT(appcontext:string) { this.appcontext = appcontext;}
    public static get APP_HOST() : string { return this.hostName; }
    public static get APP_PORT() : string { return this.port; }
    public static get APP_PROTOCOL() : string { return this.protocol; }
    public static get APP_CONTEXT() : string { return this.appcontext; }
    public static set APP_COMMAND_URL( url: string) { this.commandurl = url ; }
    public static get APP_COMMAND_URL(): string { return this.commandurl; }

    public static set LOG_OPTIONS(obj: Object){ this.logOptions = obj;}
    public static get LOG_OPTIONS(): Object { return this.logOptions;}

    public static get APP_HOST_URL() : string { 
        return this.APP_PROTOCOL+'//'+this.APP_HOST+':'; 
    }
    
    public static get WS_PORT() : string { return '8080'; }
    public static get WEB_CONTENT_PORT() : string { return '3004'; }
    public static get STATIC_CONTENT_PORT() : string { return '4000'; }
    public static get PLATFORM_SEPARATOR() : string { return '/p'; }
    public static get SEPARATOR_URI_PARENT(): string { return '..'; }
    public static get SEPARATOR_URI_ROOT_DOMAIN(): string { return '.d'; }
    public static get MARKER_COMMAND_PARAM_CURRENT_SELF(): string { return '<!#this!>'; }
    
    public static get SESSIONKEY(): string  {
        { return 'sessionId';}
    }

    //static content server constants
    public static get BLUE_THEME_URL(): string  { return 'http://'+this.hostName+':'+this.STATIC_CONTENT_PORT+'/styles/anthem/anthem.blue.theme.css'; }
    public static get BLACK_THEME_URL(): string { return 'http://'+this.hostName+':'+this.STATIC_CONTENT_PORT+'/styles/anthem/anthem.black.theme.css'; }
    public static get IMAGES_URL(): string { return this.APP_HOST_URL+this.APP_PORT+'/'+this.APP_CONTEXT; }

     /* Changes for running nimbus-ui independantly*/
    public static get WEB_APP_POST_LOGIN_URL(): string { return this.APP_HOST_URL+this.APP_PORT+'/handlelogin'; } // to be provided by clients
    public static get APP_BASE_URL(): string { return this.APP_HOST_URL+this.APP_PORT+'/'+this.APP_CONTEXT; }
    public static get AUTH_TOKEN_KEY(): string { return 'AuthToken'; } // this should come from the node server
    public static get TOKEN_HEADER_KEY(): string { return 'Authorization'; } // this should come from the node server
    public static get LANDING_ROUTE(): string { return 'authlanding' ; } // this should come from the node server and set in app.init.service

    // public static get LOG_APPENDER_OPTIONS() : any { 
    //     var obj;
    //     obj['bufferSize'] = '20';
    //     obj['url'] = 'http://localhost:8000/log';
    //     obj['level'] = 1000;
    //     obj['sendWithBufferLevel'] = 6000;
    //     return obj
    // }
    // public static get DEFAULT_ENDPOINT() : any { 
    //     var obj;
    //     obj['defaultAjaxUrl'] = 'htpp://localhost:8000/log';
    //     return obj;
    // }
}
