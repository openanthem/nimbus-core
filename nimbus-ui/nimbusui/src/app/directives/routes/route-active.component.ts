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
 * 
 * The MIT License
 * Copyright (c) 2014-2018 Google, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
'use strict';

import { MenuRouteLink } from './route-link.component';
import { AfterContentInit, ChangeDetectorRef, ContentChildren, Directive, ElementRef, Input, OnChanges, OnDestroy, QueryList, Renderer2, SimpleChanges, Output, EventEmitter} from '@angular/core';
import { Subscription } from 'rxjs';
import { URLUtils } from './../../shared/url-utils';
import { RouterLink, Router, NavigationEnd, RouterEvent, RouterLinkWithHref, ActivatedRoute } from '@angular/router';
import { MenuItem } from '../../shared/menuitem';

/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */

@Directive({
    selector: 'a[nmrouterLinkActive]',
    exportAs: 'routerLinkActive',
  })
  export class MenuRouterLinkActive implements OnChanges,
      OnDestroy, AfterContentInit {

    @ContentChildren(MenuRouteLink, {descendants: true})links : QueryList<MenuRouteLink>;
  
    @ContentChildren(RouterLinkWithHref, {descendants: true}) linksWithHrefs : QueryList<RouterLinkWithHref>;

    private classes: string[] = [];
    private subscription: Subscription;
    public readonly isActive: boolean = false;
  
    @Input() routerLinkActiveOptions: {exact: boolean} = {exact: false};
  
    @Input() item: MenuItem = {};

    @Output() toggleParent =new EventEmitter();

    constructor(
        private router: Router, private element: ElementRef, private renderer: Renderer2,
        private cdr: ChangeDetectorRef, private activ: ActivatedRoute) {
      this.subscription = router.events.subscribe((s: RouterEvent) => {
        if (s instanceof NavigationEnd) {
          this.update();
        }
      });
    }
  
  
    ngAfterContentInit(): void {
      this.links.changes.subscribe(_ => this.update());
      this.update();
    }
  
    @Input()
    set nmrouterLinkActive(data: string[]|string) {
      const classes = Array.isArray(data) ? data : data.split(' ');
      this.classes = classes.filter(c => !!c);
    }
  
    ngOnChanges(changes: SimpleChanges): void { this.update(); }
    ngOnDestroy(): void { this.subscription.unsubscribe(); }
  
    private update(): void {
      if (!this.links || !this.router.navigated) return;
      Promise.resolve().then(() => {
        const hasActiveLinks = this.hasActiveLinks();
        if (this.isActive !== hasActiveLinks) {
          (this as any).isActive = hasActiveLinks;
          this.classes.forEach((c) => {
            if (hasActiveLinks) {
              this.toggleParent.emit(this.item);
              this.renderer.addClass(this.element.nativeElement, c);
            } else {
              this.renderer.removeClass(this.element.nativeElement, c);
            }
          });
        }
      });
    }
  
    private isLinkActive(): (link : RouterLink) =>boolean {
      let menuKey = this.item.page;
      if(typeof this.item.routerLink != 'undefined' && this.item.routerLink != '') {
        menuKey = URLUtils.getDomainPage(this.item.routerLink as string);
      }
      return (link : RouterLink) => URLUtils.getDomainPage(this.router.url) == menuKey;
    }
  
    private hasActiveLinks(): boolean {
      return this.links.some(this.isLinkActive());
    }
  }