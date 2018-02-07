import { Component, Input, ViewChild, ViewEncapsulation } from '@angular/core';
import { Param } from '../../../shared/app-config.interface';
import { FormGroup } from '@angular/forms';


@Component({
  selector: 'nm-upload',
  styles: [
      `
    .ui-widget-content {border: 1px solid #ddd; }
    .ui-fileupload-choose.ui-fileupload-choose-selected input[type=file],
    .ui-fileupload-buttonbar .ui-fileupload-choose input { 
         display: block; 
    }
      `],
  encapsulation: ViewEncapsulation.None,
  template: `

    <div [formGroup]="form">
       <p-fileUpload 
        name="myfile[]" url="http://localhost:8080/test" multiple="multiple" accept={{element.config?.uiStyles?.attributes?.type}}>
       </p-fileUpload>
    </div>
 
  `
})
export class FileUploadComponent  { 

  @Input() element: Param;
  @Input() form: FormGroup;


  constructor(){}

  ngOnInit() {}


 }



