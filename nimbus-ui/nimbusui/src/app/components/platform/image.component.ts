'use strict';
import { Component, Input } from '@angular/core';
import { Param } from '../../shared/app-config.interface';
import { ServiceConstants } from './../../services/service.constants';

@Component({
  selector: 'nm-image',
  template: `
      <svg class="">
          <use xmlns:xlink="http://www.w3.org/1999/xlink" attr.xlink:href="{{imagesPath}}{{element.config.uiStyles.attributes.imgSrc}}#Layer_1"></use> 
      </svg>
   `
})
export class Image {

    @Input() element: Param;
    public imagesPath: string;

    constructor() {
    }

    ngOnInit() {
        this.imagesPath = ServiceConstants.IMAGES_URL;
    }
}
