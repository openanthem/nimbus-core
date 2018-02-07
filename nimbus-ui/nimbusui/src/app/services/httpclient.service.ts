import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions} from '@angular/http';

@Injectable()
export class HttpClient {
  constructor(public http: Http) {
    this.http = http;
  }

  createAuthorizationHeader(headers:Headers) {
    // headers.append('X-Csrf-Token', 'RANDOM123'); // W
    headers.append('X-Csrf-Token', this.getCookie('XSRF-Token')); //XSRF-Token - Krishna is setting this in serverside
    //either token will be stored in the cookie or as a keyvalue pair in the localstore
    //localStorage.getItem('XSRF-Token');
    // console.log(headers);
    //console.log('Localstorage - get token:'+localStorage.getItem('XSRF-Token'))
    // headers.append('X-CSRFToken',localStorage.getItem('XSRF-Token'));
  }

  get(url) {
    let headers = new Headers({'Content-Type': 'application/json'});
    this.createAuthorizationHeader(headers);
    let options = new RequestOptions({headers: headers, withCredentials: true});
    return this.http.get(url, options);
  }

  post(url, data) {
    let headers = new Headers({'Content-Type': 'application/json'});
    this.createAuthorizationHeader(headers);
    let options = new RequestOptions({headers: headers, withCredentials: true});
    return this.http.post(url, data, options);
  }

  getCookie(name) {
    let value = '; ' + document.cookie;
    let parts = value.split('; ' + name + '=');

    if (parts.length === 2) {
      return parts.pop().split(';').shift();
    }
  }
}
