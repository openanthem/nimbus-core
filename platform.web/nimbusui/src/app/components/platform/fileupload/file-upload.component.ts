/**
 * @license
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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



