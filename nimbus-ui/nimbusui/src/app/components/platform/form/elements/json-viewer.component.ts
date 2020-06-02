/**
 * @license
 * MIT License
 * Copyright (c) 2017 Vivo Xu
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import { Component, OnChanges, Input, OnInit } from '@angular/core';

export interface Segment {
  key: string;
  value: any;
  type: undefined | string;
  description: string;
  expanded: boolean;
}

@Component({
  selector: 'json-viewer',
  styleUrls: ['./json-view-styles.scss'],
  template: `
  <section class="nm-json-viewer">
  <section
    *ngFor="let segment of segments"
    [ngClass]="['segment', 'segment-type-' + segment.type]">
    <section
      (click)="toggle(segment)"
      [ngClass]="{
        'segment-main': true,
        'expandable': isExpandable(segment),
        'expanded': segment.expanded
      }">
      <div *ngIf="isExpandable(segment)" class="toggler"></div>
      <span class="segment-key">{{ segment.key }}</span>
      <span class="segment-separator">: </span>
      <span *ngIf="!segment.expanded || !isExpandable(segment)" class="segment-value">{{ segment.description }}</span>
    </section>
    <section *ngIf="segment.expanded && isExpandable(segment)" class="children">
      <json-viewer [json]="segment.value" [expanded]="expanded"></json-viewer>
    </section>
  </section>
</section>`
})
export class JsonViewComponent {

  @Input() json: any;
  @Input() expanded = true;

  segments: Segment[] = [];

  ngOnInit() {
    //if json is string then convert to object
    if( typeof this.json == 'string') {
        this.json = JSON.parse(this.json)
    }
    if (typeof this.json === 'object') {
      Object.keys(this.json).forEach( key => {
        this.segments.push(this.parseKeyValue(key, this.json[key]));
      });
    } else {
      this.segments.push(this.parseKeyValue(`(${typeof this.json})`, this.json));
    }
  }

  isExpandable(segment: Segment) {
    return segment.type === 'object' || segment.type === 'array';
  }

  toggle(segment: Segment) {
    if (this.isExpandable(segment)) {
      segment.expanded = !segment.expanded;
    }
  }

  private parseKeyValue(key: any, value: any): Segment {
    const segment: Segment = {
      key: key,
      value: value,
      type: undefined,
      description: '' + value,
      expanded: this.expanded
    };

    switch (typeof segment.value) {
      case 'number': {
        segment.type = 'number';
        break;
      }
      case 'boolean': {
        segment.type = 'boolean';
        break;
      }
      case 'function': {
        segment.type = 'function';
        break;
      }
      case 'string': {
        segment.type = 'string';
        segment.description = '"' + segment.value + '"';
        break;
      }
      case 'undefined': {
        segment.type = 'undefined';
        segment.description = 'undefined';
        break;
      }
      case 'object': {
        if (segment.value === null) {
          segment.type = 'null';
          segment.description = 'null';
        } else if (Array.isArray(segment.value)) {
          segment.type = 'array';
          segment.description = 'Array[' + segment.value.length + '] ' + JSON.stringify(segment.value);
        } else if (segment.value instanceof Date) {
          segment.type = 'date';
        } else {
          segment.type = 'object';
          segment.description = 'Object ' + JSON.stringify(segment.value);
        }
        break;
      }
    }

    return segment;
  }
}