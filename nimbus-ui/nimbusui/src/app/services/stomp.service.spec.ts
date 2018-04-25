import { TestBed, inject, async, fakeAsync,  tick} from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as Stomp from 'stompjs';

import { STOMPService, STOMPState, StateLookup } from './stomp.service';
import { Subject } from 'rxjs';

let http, backend, service;

describe('STOMPService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          STOMPService
      ],
      imports: [ HttpClientTestingModule, HttpModule ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(STOMPService);
 });

  it('STOMPService should be created', async(() => {
    expect(service).toBeTruthy();
  }));

  it('STOMPService.configure() should throw error', async(() => {
    service.state = {
        getValue: () => {
            return 'TRYING';
        }
    };
    expect(() => {
        service.configure();
    }).toThrow();
  }));

  it('STOMPService.configure() should update client', async(() => {
    service.state.next(STOMPState.CLOSED);
    service.configure();
    expect(service.client).toBeTruthy();
  }));

  it('STOMPService.try_connect() should update state', async(() => {
    service.state.next(STOMPState.CLOSED);
    service.configure();
    service.try_connect();
    expect(service.state.getValue()).toEqual(STOMPState.TRYING);
  }));

  it('STOMPService.try_connect() should throw error if state is not closed', async(() => {
    service.configure();
    service.state.next(STOMPState.TRYING);
    expect(() => {
        service.try_connect();
    }).toThrow();
  }));

  it('STOMPService.try_connect() should throw error if state is not closed', async(() => {
    service.configure();
    service.state.next(null);
    expect(() => {
        service.try_connect();
    }).toThrow();
  }));


  it('STOMPService.disconnect() should update state', async(() => {
    service.state.next(STOMPState.CLOSED);
    service.configure();
    try {
        service.disconnect('test');
    }
    catch(err) {
        console.log('disconnect() error', err);
    }
    expect(service.state.getValue()).toEqual(STOMPState.DISCONNECTING);
  }));

  it('STOMPService.on_connect() should update state', async(() => {
    service.state.next(STOMPState.CLOSED);
    service.configure();
    service.try_connect()
    .then(res => { });
    try {
        service.on_connect();
    }
    catch(err) {
        console.log('on_connect() error', err);
    }
    expect(service.state.getValue()).toEqual(STOMPState.CONNECTED);
  }));

  it('STOMPService.on_error() should update state', fakeAsync(() => {
    service.on_error('Lost connection');
    tick (5100);
    expect(service.state.getValue()).toEqual(STOMPState.TRYING);   
  }));

  it('STOMPService.on_message() should update messages', async(() => {
    const test = {
        body: 123
    }
    spyOn(service.messages, 'next').and.callThrough();
    service.on_message(test);
    expect(service.messages.next).toHaveBeenCalled();
  }));

});