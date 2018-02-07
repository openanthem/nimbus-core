import { Component, Input, forwardRef} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'nm-switch',
  template: `<div (click)="switch()" class="switch" [ngClass]="{ 'checked': value }" [attr.title]="label">
                <input type="checkbox" class="switch-input" [value]="value" [attr.checked]="value" [attr.aria-label]="label">
                <span class="switch-label" data-on="on" data-off="off"></span>
                <span class="switch-handle"></span>
            </div>`,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => Switch),
      multi: true
    }
  ]
})
export class Switch implements ControlValueAccessor {
  @Input() label = 'switch';
  @Input('value') _value = false;

  onChange: any = () => { };
  onTouched: any = () => { };
  get value() {
    return this._value;
  }

  set value(val) {
    this._value = val;
    this.onChange(val);
    this.onTouched();
  }

  constructor() { }

  registerOnChange(fn) {
    this.onChange = fn;
  }

  writeValue(value) {
    //console.log(value);
    if (value) {
      this.value = value;
    }
  }

  registerOnTouched(fn) {
    this.onTouched = fn;
  }

  switch() {
    this.value = !this.value;
  }
}
