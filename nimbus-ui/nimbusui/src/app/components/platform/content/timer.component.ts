import { PageService } from './../../../services/page.service';
import { Observable, timer, Subscription } from 'rxjs';
import { ParamUtils } from './../../../shared/param-utils';
import { Param } from './../../../shared/param-state';
/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

import { Component, Input, OnInit, OnDestroy } from '@angular/core';

/**
 * \@author Purnachander.Mashetty
 *
 * \@howToUse
 *
 */
@Component({
  selector: 'nm-timer',
  template: `
  <span [className]="cssClass">{{label}}</span>  
  <span [className]="cssClass">{{timehh}}:{{timemm}}:{{timess}}</span>
  `
})
export class Timer implements OnInit, OnDestroy{
  @Input() element: Param;
  timer: Observable<number>;
  postTimer: Observable<number>;
  subscription: Subscription;
  time: number = 0;
  timeReference: number = 0;
  timehh: string;
  timemm: string;
  timess: string;
  constructor(
    private pageSvc: PageService
  ) {
    
  }

  ngOnInit(){
    let lastSavedTime = this.element.leafState;
    if(lastSavedTime){
      this.timeReference=lastSavedTime;
    }
    this.timer = timer(0, 1000);
    this.subscription = this.timer.subscribe((seconds) => {
      this.time = this.timeReference + seconds;
      this.formatTime();
    })
    if(this.element.config.uiStyles.attributes.postEventOnChange){
      this.postTimer = timer(0,this.element.config.uiStyles.attributes.postInterval);
    }
  }

  formatTime(){
    let seconds: number;
    seconds = this.time;
    let hours = Math.floor(seconds/3600);
    seconds = seconds - (hours * 3600);
    let minutes = Math.floor(seconds/60);
    seconds = seconds - (minutes * 60);

    if(hours>10)
      this.timehh = ""+hours;
    else
      this.timehh = "0"+hours

    if(minutes>10)
      this.timemm = ""+minutes;
    else
      this.timemm = "0"+minutes

    if(seconds>10)
      this.timess = ""+seconds;
    else
      this.timess = "0"+seconds

  }

  public get label(): string {
    return this.element.config.uiStyles.attributes.display;
  }

  public get cssClass(): string {
     return this.element.config.uiStyles.attributes.cssClass;
  }
  
  
  ngOnDestroy(){
    this.pageSvc.postOnChange(
      this.element.path,
      'state',
      JSON.stringify(this.time)
    );
    this.element.leafState=this.time;
    this.subscription.unsubscribe();
 
  }

}
