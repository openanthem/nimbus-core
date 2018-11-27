import { URLUtils } from './../../../../shared/url-utils';
import { Action } from './../../../../shared/command.enum';
import { ServiceConstants } from './../../../../services/service.constants';
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

import { Directive, OnInit, OnDestroy, Output, EventEmitter, HostListener, Input, ElementRef} from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';
import { debounceTime, distinctUntilChanged, distinctUntilKeyChanged } from 'rxjs/operators';
import { Param } from './../../../../shared/param-state';
import { PageService } from './../../../../services/page.service';

/**
 * \@author Sandeep.Mantha
 * 
 */
@Directive({
    selector: '[eventpropagation]'
  })
  export class EventPropagationDirective implements OnInit, OnDestroy{

    @Input() element:Param;
    private clicksubject = new Subject();
    private subscription: Subscription;
    private postProcessing: Subscription;
    private srcElement: any;
  
    @Output() clickEvnt = new EventEmitter();

    constructor(private pageService: PageService, private elementRef: ElementRef) {
    }

    ngOnInit() {
      this.subscription = this.clicksubject.pipe(
        debounceTime(500)
      ).subscribe(e => this.clickEvnt.emit(e));

      this.postProcessing = this.pageService.postResponseProcessing$.subscribe(event => {
        if(this.element && event === this.element.path) {
          this.srcElement.removeAttribute('disabled');
        }
      })
    }

    @HostListener('click', ['$event'])
    clickEvent(event) {
      event.preventDefault();
      event.stopPropagation();
      if(this.element) {
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