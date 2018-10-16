import { Component, Input } from '@angular/core';
import { ServiceConstants } from '../../../services/service.constants';

@Component({
  selector: 'nm-svg',
  templateUrl: './svg.component.html'
})
export class SvgComponent {
  updatedSrc: string;
  @Input() name: String;

  constructor() {}

  ngOnInit() {
    this.updatedSrc = ServiceConstants.IMAGE_URL + this.name ;
  }

}