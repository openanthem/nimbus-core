import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Rx';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import * as Stomp from 'stompjs';

import { ServiceConstants } from './service.constants';

/** possible states for the STOMP service */
export enum STOMPState {
    CLOSED,
    TRYING,
    CONNECTED,
    SUBSCRIBED,
    DISCONNECTING
};

/** look up states for the STOMP service */
export const StateLookup: string[] = [
    'CLOSED',
    'TRYING',
    'CONNECTED',
    'SUBSCRIBED',
    'DISCONNECTING'
];

/**
 * Angular2 STOMP Service using stomp.js
 *
 * @description This service handles subscribing to a
 * message queue using the stomp.js library, and returns
 * values via the ES6 Observable specification for
 * asynchronous value streaming by wiring the STOMP
 * messages into a Subject observable.
 */
@Injectable()
export class STOMPService {

    /* Service parameters */

    // State of the STOMPService
    public state: BehaviorSubject<STOMPState>;

    // Publishes new messages to Observers
    public messages: Subject<Stomp.Message>;

    // STOMP Client from stomp.js
    private client: Stomp.Client;

    // Resolve Promise made to calling class, when connected
    private resolvePromise: { (...args: any[]): void };

    /** Constructor */
    public constructor() {
        this.messages = new Subject<Stomp.Message>();
        this.state = new BehaviorSubject<STOMPState>(STOMPState.CLOSED);
    }

    /** Set up configuration */
    public configure(): void {

        // Check for errors:
        if (this.state.getValue() !== STOMPState.CLOSED) {
            throw Error('Already running!');
        }

        // Attempt connection, passing in a callback
        //let theClientFunction;
        //jit-aot conflict,  same typing, same angular version
        //but the ts code has to be written differently like below
        let stompAny : any = Stomp;
        if (stompAny.client) {
            this.client = Stomp.client(ServiceConstants.WS_BASE_URL, ['v10.stomp', 'v11.stomp', 'v12.stomp']);
        } else {
            this.client = stompAny.Stomp.client(ServiceConstants.WS_BASE_URL, ['v10.stomp', 'v11.stomp', 'v12.stomp']);
        }
        // Configure client heartbeating
        this.client.heartbeat.incoming = 20000;
        this.client.heartbeat.outgoing = 0;

        // Set function to debug print messages
        this.client.debug = this.debug;
    }

    /** 
     * Perform connection to STOMP broker, returning a Promise
     * which is resolved when connected. 
     */
    public try_connect(): Promise<{}> {

        if(this.state.getValue() !== STOMPState.CLOSED) {
            throw Error('Can\'t try_connect if not CLOSED!');
        }
        if(this.client === null) {
            throw Error('Client not configured!');
        }

        // Attempt connection, passing in a callback 
        this.client.connect(
            {login: '', passcode: ''},
            this.on_connect,
            this.on_error
        );

        console.log('connecting...');
        this.state.next(STOMPState.TRYING);

        return new Promise(
            (resolve, reject) => this.resolvePromise = resolve
        );
    }

    /** Disconnect the STOMP client and clean up */
    public disconnect(message?: string) {

        // Notify observers that we are disconnecting!
        this.state.next( STOMPState.DISCONNECTING );

        // Disconnect. Callback will set CLOSED state
        this.client.disconnect(
            () => this.state.next( STOMPState.CLOSED ),
            message
        );
    }

    /** Send a message to all topics */
    public publish(message: string) {
    }

    /** 
     * Callback Functions
     *
     * Note the method signature: () => preserves lexical scope
     * if we need to use this.x inside the function
     */
    public debug(...args: any[]) {

        // Push arguments to this function into console.log
        if (window.console && console.log && console.log.apply) {
            console.log.apply(console, args);
        }
    }


    // Callback run on successfully connecting to server
    public on_connect = () => {

        // Indicate our connected state to observers
        this.state.next( STOMPState.CONNECTED );

        // Subscribe to message queues
        this.subscribe();

        // Resolve our Promise to the caller
        this.resolvePromise();

        // Clear callback
        this.resolvePromise = null;
    }

    // Handle errors from stomp.js
    public on_error = (error: string) => {

        console.error('Error: ' + error);

        // Check for dropped connection and try reconnecting
        if (error.indexOf('Lost connection') !== -1) {

            // Reset state indicator
            this.state.next( STOMPState.CLOSED );

            // Attempt reconnection
            //console.log('Reconnecting in 5 seconds...');
            setTimeout(() => {
                this.configure();
                this.try_connect();
            }, 5000);
        }
    }

    // On message RX, notify the Observable with the message object
    public on_message = (message: Stomp.Message) => {

        if (message.body) {
            this.messages.next(message);
        } else {
            console.error('Empty message received!');
        }
    }

    /** Subscribe to server message queues */
    private subscribe(): void {
        // Subscribe to our configured queues
        this.client.subscribe(ServiceConstants.WS_SUBSCRIBE_Q, this.on_message, <any>{ ack: 'auto' });
        // Update the state
        this.state.next( STOMPState.SUBSCRIBED );
    }
}
