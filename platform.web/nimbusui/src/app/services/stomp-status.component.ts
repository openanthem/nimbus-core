import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { STOMPService, StateLookup } from './stomp.service';

/**
 * STOMP connection status as a component
 */
@Component({
	selector: 'stomp-status',
	template: `
		<div class="col-xs-2 pull-right">
			<p> STOMP Status : <span id="status"> {{state|async}} </span> </p>
		</div>
	`
})
export class STOMPStatusComponent implements OnInit {

	public state: Observable<string>;

	constructor(private _stompService: STOMPService) { }

	ngOnInit() {
		//console.log('Status init');
		this.state = this._stompService.state
			.map( (state:number) => StateLookup[state] );
	}
}
