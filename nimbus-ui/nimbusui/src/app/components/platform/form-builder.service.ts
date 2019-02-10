import { ParamUtils } from './../../shared/param-utils';
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
import { Injectable } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ValidatorFn, FormControl } from '@angular/forms';

import { CustomValidators } from './validators/custom.validators';
import { Param } from '../../shared/param-state';
import { ValidationUtils } from './validators/ValidationUtils';
import { ViewComponent } from '../../shared/param-annotations.enum';

/**
 * \@author Dinakar.Meda
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 *  
 * \@howToUse 
 * 
 */
@Injectable()
export class FormElementsService {
  constructor(private _fb: FormBuilder) { }
  
  toFormGroup(elements: Param[],formValidations: ValidatorFn[]) {
    let group: FormGroup;
    if(formValidations.length>0)
      group = this._fb.group(this.buildFormGroup(elements),{validator:CustomValidators.formGroupNotNull(Validators.required)});
     else
      group = this._fb.group(this.buildFormGroup(elements));

    return group;
  }
  
  buildFormGroup(elements: Param[]): {} {
    let group: any = {};
    elements.forEach(element => {
      if(element.config!= null && ((element.config.uiStyles != null && element.config.uiStyles.attributes!= null && element.config.uiStyles.attributes.dataEntryField) || element.config.uiStyles==null)){
        var checks: ValidatorFn[] = [];
        checks = ValidationUtils.buildStaticValidations(element);
        //if the form element's state is a collection we do not create a form group for it
        if(element.type && element.config.type.nested && element.type.model.params.length>0 && !element.config.type.collection) {
          if (element.alias !== ViewComponent.picklist.toString()) {
            group[element.config.code] = this.createNewFormGroup(element);
          } else {
            // Adding this for picklist since form submit does not handle complex type - Revisit
            group[element.config.code] = this.createNewFormGroup(element);
            const picklistparam: Param = element.type.model.params.find( p => 
              p.alias === ViewComponent.selectedPicklist.toString());
            var leafState = this._getTypeSafeLeafState(picklistparam);
            if (checks) {
              group[picklistparam.config.code] = [{value: leafState, disabled: !picklistparam.enabled}, checks];
            } else {
              group[picklistparam.config.code] = [{value: leafState, disabled: !picklistparam.enabled}];
           }
          }  
          //create new formgroup and formcontrol to create checkboxes in form. this is for form binding. TODO validations binding
        } else {
          var leafState = this._getTypeSafeLeafState(element);
          if (checks) {
            group[element.config.code] = [{value: leafState, disabled: !element.enabled}, checks];
          } else {
            group[element.config.code] = [{value: leafState, disabled: !element.enabled}] 
         }
        }
      }
    });
    return group;
  }

  //TODO - Merge buildFormGroup and createNewFormGroup methods to one method. 
  createNewFormGroup(element: Param) : FormGroup {
    let fg = new FormGroup({});
    for (let i = 0; i < element.type.model.params.length; i++) {
      let param = element.type.model.params[i];
      var checks: ValidatorFn[] = [];
      checks = ValidationUtils.buildStaticValidations(param);
      if (param.config.type.nested) {
         fg.addControl(param.config.code, this.createNewFormGroup(param));
      } else {
          var leafState = this._getTypeSafeLeafState(param);
          let formState: any = { value: leafState, disabled: !param.enabled }
          let formControl: FormControl = new FormControl(formState);
          if (checks) {
            formControl = new FormControl(formState, checks);
          }
          fg.addControl(param.config.code, formControl);
        } 
      }
      return fg;
  }

  private _getTypeSafeLeafState(param: Param): any {
    var leafState;
    if (ParamUtils.isKnownDateType(param.config.type.name)) {
      leafState = param.leafState || null;
    } else if(param.alias === 'Grid' && param.gridData.leafState && param.gridData.leafState.length > 0) {
        leafState = param.gridData.leafState;
    } else {
      leafState = param.leafState || '';
    }
    return leafState;
  }

}
