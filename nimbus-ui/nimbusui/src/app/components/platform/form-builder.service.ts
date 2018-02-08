import { Injectable } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ValidatorFn, FormArray, FormControl,ValidationErrors } from '@angular/forms';

import { CustomValidators } from './validators/custom.validators';
import { Param } from '../../shared/app-config.interface';

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
      if(element.config!= null && ((element.config.uiStyles != null && element.config.uiStyles.attributes!= null && element.config.uiStyles.attributes.alias !== 'Button'
          && element.config.uiStyles.attributes.alias !== 'ButtonGroup') || element.config.uiStyles==null)){
        var checks: ValidatorFn[] = [];
        checks = this.buildValidations(element);
        //if the form element's state is a collection we do not create a form group for it
        if(element.type.nested && element.type.model.params.length>0 && !element.collection) {
          group[element.config.code] = this.createNewFormGroup(element);
          //create new formgroup and formcontrol to create checkboxes in form. this is for form binding. TODO validations binding
        } else {
          group[element.config.code] = checks ? [{value: (element.alias === 'Calendar') ? element.leafState= new Date(element.leafState) : element.leafState || '', disabled: !element.enabled.currState}, checks] : [{value: element.leafState || '', disabled: !element.enabled.currState}];
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
      checks = this.buildValidations(param);
      if (param.type.nested) {
         fg.addControl(param.config.code, this.createNewFormGroup(param));
      } else {
          //Ternary operator is for converting Calendar string into Date to support @Calendar component
          if (checks) {
          fg.addControl(param.config.code, new FormControl({value: (param.alias === 'Calendar') ? param.leafState= new Date(param.leafState) : param.leafState || '', disabled: !param.enabled.currState}, checks));
          } else {
            fg.addControl(param.config.code, new FormControl({value: (param.alias === 'Calendar') ? param.leafState= new Date(param.leafState) : param.leafState || '', disabled: !param.enabled.currState}));
        } 
      }
    }
    return fg;
  }

  buildValidations(element:Param) :ValidatorFn[] {
     var checks: ValidatorFn[] = [];
      if (element.config.validation) {
        element.config.validation.constraints.forEach(validator => {
          if (validator.name === 'NotNull') {
            if(element.config.uiStyles.attributes.alias == 'CheckBox') {
              checks.push(Validators.requiredTrue);
            } else {
              checks.push(Validators.required);
            }
          }
          if (validator.name === 'Pattern') {
            checks.push(Validators.pattern(validator.attribute.regexp));
          }
          if (validator.name === 'Size') {
            checks.push(CustomValidators.minMaxSelection(element.config.uiStyles.attributes.alias, validator.attribute));
          }
          if (validator.name === 'isNumber') {
            checks.push(CustomValidators.isNumber);
          }
          if (validator.name === 'isZip') {
            checks.push(CustomValidators.isZip);
          }
        });
      }
      return checks;
  }

}
