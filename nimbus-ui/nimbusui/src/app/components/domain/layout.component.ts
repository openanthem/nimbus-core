'use strict';
import { Component, Input, SimpleChanges, forwardRef, OnInit } from '@angular/core';
import { BaseElement } from '../platform/base-element.component';
import { WebContentSvc } from '../../services/content-management.service';
import { Param } from '../../shared/param-state';

@Component({
    selector: 'nm-layout',
    providers: [WebContentSvc],
    templateUrl: './layout.component.html'
})

export class LayoutComponent  {
    @Input() element: Param;
}
