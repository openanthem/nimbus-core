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

import { Directive, OnInit, OnDestroy, Output, EventEmitter, HostListener, Input } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';
import { debounceTime } from 'rxjs/operators';
import { PageService } from './../../../../services/page.service';
import { LoggerService } from './../../../../services/logger.service';

/**
 * \@author Sandeep.Mantha
 * 
 */
@Directive({
    selector: '[eventpropagation]'
  })
  export class EventPropagationDirective implements OnInit, OnDestroy{

    @Input() path:string;
    private clicksubject = new Subject();
    private subscription: Subscription;
    private postProcessing: Subscription;
    private srcElement: any;
  
    @Output() clickEvnt = new EventEmitter();

    constructor(private pageService: PageService, private logger: LoggerService) {
    }

    ngOnInit() {
      this.subscription = this.clicksubject.pipe(
        debounceTime(500)
      ).subscribe(
        e => { this.clickEvnt.emit(e) },
        err => { this.logger.error('Failed to emit click event' + JSON.stringify(err))}
      );

      this.postProcessing = this.pageService.postResponseProcessing$.subscribe(
        event => {
                    if(event === this.path) {
                      this.srcElement.removeAttribute('disabled');
                    }
                 },
        err => { this.logger.error('Failed on processing eventpropagation for path ' + event + ' with '+ JSON.stringify(err));}
      )
    }

    @HostListener('click', ['$event'])
    clickEvent(event) {
      event.preventDefault();
      event.stopPropagation();
      if(this.path) {
        this.srcElement =  event.srcElement;
        this.srcElement.setAttribute('disabled', true);
      }
      this.clicksubject.next(event);
    }

    ngOnDestroy() {
      if(this.subscription) {
        this.subscription.unsubscribe();
      }
      if(this.postProcessing) {
        this.postProcessing.unsubscribe();
      }
    }

  }