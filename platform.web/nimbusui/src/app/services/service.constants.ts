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

export class ServiceConstants {
    private static hostName: string;
    private static port: string;

    public static get PAGE_INDEX() : number { return 0;}
    public static get PAGE_SIZE() : number { return 20;}
    public static get URL_SEPERATOR(): string { return '/'; }
    public static get WS_SUBSCRIBE_Q(): string { return '/user/queue/updates'; }

    /* Enable for stopgap server */
    public static get BASE_URL(): string    { return this.STOPGAP_APP_HOST_URL+this.WEB_CONTENT_PORT; }
    public static get PLATFORM_BASE_URL(): string    { return this.STOPGAP_APP_HOST_URL+this.STOPGAP_APP_PORT+'/Anthem/fep/p'; }
    public static get CLIENT_BASE_URL(): string    { return this.STOPGAP_APP_HOST_URL+this.STOPGAP_APP_PORT+'/'; }
    public static get WS_BASE_URL(): string { return 'ws://'+this.STOPGAP_APP_HOST+':'+this.WS_PORT+'/updates'; }

    public static get WEB_CONTENT_URL(): string    { return this.BASE_URL+'/web_content_server/'; }
    public static get CONTENT_MANAGEMENT(): string { return this.STOPGAP_APP_HOST_URL+this.WEB_CONTENT_PORT+'/contentManagement'; }

    public static get STOPGAP_APP_HOST() : string { return this.hostName; }
    public static get STOPGAP_APP_HOST_URL() : string { return 'http://'+this.STOPGAP_APP_HOST+':'; }
    public static get STOPGAP_APP_PORT() : string { return this.port; }
    public static get WS_PORT() : string { return '8080';}
    public static get WEB_CONTENT_PORT() : string { return '3004';}
    public static get STATIC_CONTENT_PORT() : string { return '4000';}

    public static set STOPGAP_APP_HOST(name:string) { this.hostName = name;}
    public static set STOPGAP_APP_PORT(port:string) { this.port = port;}

    //static content server constants
    public static get BLUE_THEME_URL(): string  { return 'http://'+this.hostName+':'+this.STATIC_CONTENT_PORT+'/styles/anthem/anthem.blue.theme.css'; }
    public static get BLACK_THEME_URL(): string { return 'http://'+this.hostName+':'+this.STATIC_CONTENT_PORT+'/styles/anthem/anthem.black.theme.css'; }
    public static get IMAGES_URL(): string { return 'http://'+this.hostName+':'+this.STATIC_CONTENT_PORT+'/resources/icons/'; }

    public static get HOME_ROUTE(): string { return '/h'; }
    public static get HOME_DOMAIN(): string { return 'cmdashboard'; }
    public static get HOME_PAGE(): string { return 'vpDashboard'; }
}
