import {
  NgModule,
  Component,
  Pipe,
  OnInit,
  DoCheck,
  HostListener,
  Input,
  ElementRef,
  Output,
  EventEmitter,
  forwardRef,
  IterableDiffers
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { Param } from '../../../../shared/app-config.interface';
import { PageService } from '../../../../services/page.service';

const MULTISELECT_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => MultiselectDropdown),
  multi: true
};

export interface IMultiSelectOption {
  code: any;
  label: string;
}

export interface IMultiSelectSettings {
  pullRight?: boolean;
  enableSearch?: boolean;
  checkedStyle?: 'checkboxes' | 'glyphicon' | 'fontawsome';
  buttonClasses?: string;
  selectionLimit?: number;
  closeOnSelect?: boolean;
  autoUnselect?: boolean;
  showCheckAll?: boolean;
  showUncheckAll?: boolean;
  dynamicTitleMaxItems?: number;
  maxHeight?: string;
}

export interface IMultiSelectTexts {
  checkAll?: string;
  uncheckAll?: string;
  checked?: string;
  checkedPlural?: string;
  searchPlaceholder?: string;
  defaultTitle?: string;
}

@Pipe({
  name: 'searchFilter'
})
export class MultiSelectSearchFilter {
  transform(options: Array<IMultiSelectOption>, args: string): Array<IMultiSelectOption> {
    return options.filter((option: IMultiSelectOption) =>
      option.label
        .toLowerCase()
        .indexOf((args || '').toLowerCase()) > -1);
  }
}

@Component({
  selector: 'nm-multiselect-dropdown',
  providers: [MULTISELECT_VALUE_ACCESSOR],
  styles: [`
	   a { outline: none !important; }
  `],
  template: `
	<div class="dropdown">
	    <button type="button" class="dropdown-toggle" [ngClass]="settings.buttonClasses"
	    (click)="toggleDropdown()">{{ title }}&nbsp;<span class="caret"></span></button>
	    <ul *ngIf="isVisible" class="dropdown-menu" [class.pull-right]="settings.pullRight" [class.dropdown-menu-right]="settings.pullRight"
	    [style.max-height]="settings.maxHeight" style="display: block; height: auto; overflow-y: auto;">
        <li class="dropdown-item" *ngIf="settings.enableSearch">
		    <div class="input-group input-group-sm">
			<span class="input-group-addon" id="sizing-addon3"><i class="fa fa-search"></i></span>
			<input type="text" class="form-control" placeholder="{{ texts.searchPlaceholder }}"
			aria-describedby="sizing-addon3" >
			<span class="input-group-btn" *ngIf="searchFilterText.length > 0">
			    <button class="btn btn-default" type="button" (click)="clearSearch()"><i class="fa fa-times"></i></button>
			</span>
		    </div>
		</li>
		<li class="dropdown-divider divider" *ngIf="settings.enableSearch"></li>
		<li class="dropdown-item" *ngIf="settings.showCheckAll">
		    <a href="javascript:;" role="menuitem" tabindex="-1" (click)="checkAll()">
			<span style="width: 16px;" class="glyphicon glyphicon-ok"></span>
			{{ texts.checkAll }}
		    </a>
		</li>
		<li class="dropdown-item" *ngIf="settings.showUncheckAll">
		    <a href="javascript:;" role="menuitem" tabindex="-1" (click)="uncheckAll()">
			<span style="width: 16px;" class="glyphicon glyphicon-remove"></span>
			{{ texts.uncheckAll }}
		    </a>
		</li>
		<li *ngIf="settings.showCheckAll || settings.showUncheckAll" class="dropdown-divider divider"></li>
		<li class="dropdown-item" style="cursor: pointer;"  *ngFor="let option of options | searchFilter:searchFilterText" (click)="setSelected($event, option)">
		    <a href="javascript:;" role="menuitem" tabindex="-1" >
			<input *ngIf="settings.checkedStyle === 'checkboxes'" type="checkbox" [checked]="isSelected(option)" />
			<span *ngIf="settings.checkedStyle === 'glyphicon'" style="width: 16px;"
			class="glyphicon" [class.glyphicon-ok]="isSelected(option)"></span>
			<span *ngIf="settings.checkedStyle === 'fontawsome'" style="width: 16px;display: inline-block;">
			    <i *ngIf="isSelected(option)" class="fa fa-check" aria-hidden="true"></i>
			</span>
			{{ option.label }}
		    </a>
		</li>
	    </ul>
	</div>
`
})
export class MultiselectDropdown implements OnInit, DoCheck, ControlValueAccessor {
  @Input() element: Param;
  @Input() form: FormGroup;
  options: Array<IMultiSelectOption>;
  @Input() settings: IMultiSelectSettings;
  @Input() texts: IMultiSelectTexts;
  @Output() selectionLimitReached = new EventEmitter();
  @Output() dropdownClosed = new EventEmitter();

  @HostListener('document: click', ['$event.target'])

  model: String[];
  title: string;
  differ: any;
  numSelected: number = 0;
  isVisible: boolean = false;
  searchFilterText: string = '';
  defaultSettings: IMultiSelectSettings = {
    pullRight: false,
    enableSearch: false,
    checkedStyle: 'checkboxes',
    buttonClasses: 'btn btn-default btn-secondary',
    selectionLimit: 0,
    closeOnSelect: false,
    autoUnselect: false,
    showCheckAll: false,
    showUncheckAll: false,
    dynamicTitleMaxItems: 3,
    maxHeight: '300px'
  };
  defaultTexts: IMultiSelectTexts = {
    checkAll: 'Check all',
    uncheckAll: 'Uncheck all',
    checked: 'checked',
    checkedPlural: 'checked',
    searchPlaceholder: 'Search...',
    defaultTitle: 'Select'
  };

  onClick(target: HTMLElement) {
    let parentFound = false;
    while (target != null && !parentFound) {
      if (target === this.elementRef.nativeElement) {
        parentFound = true;
      }
      target = target.parentElement;
    }
    if (!parentFound) {
      this.isVisible = false;
    }
  }

  constructor(private elementRef: ElementRef,
    differs: IterableDiffers, private pageService: PageService) {
    this.differ = differs.find([]).create(null);
  }
  setState(event:any,frmInp:any) {
    frmInp.element.leafState = event;
    console.log(frmInp.element.leafState);
  }
  ngOnInit() {
    this.options=this.element.values;
    this.model = this.element.leafState;
    this.settings = Object.assign(this.defaultSettings, this.settings);
    this.texts = Object.assign(this.defaultTexts, this.texts);
    this.title = this.texts.defaultTitle;
     if( this.form.controls[this.element.config.code]!= null) {
      this.form.controls[this.element.config.code].valueChanges.subscribe(($event) => this.setState($event,this));
      this.pageService.eventUpdate$.subscribe(event => {
        let frmCtrl = this.form.controls[event.config.code];
        if(frmCtrl!=null && event.path.startsWith(this.element.path)) {
            frmCtrl.setValue(event.leafState);
        }
    });
     }
  }

  onModelChange: Function = (_: any) => { };
  onModelTouched: Function = () => { };


  writeValue(value: any): void {
    if (value !== undefined) {
      this.model = value;
    }
  }

  registerOnChange(fn: Function): void {
    this.onModelChange = fn;
  }

  registerOnTouched(fn: Function): void {
    this.onModelTouched = fn;
  }

  ngDoCheck() {
    let changes = this.differ.diff(this.model);
    if (changes) {
      this.updateNumSelected();
      this.updateTitle();
    }
  }

  clearSearch() {
    this.searchFilterText = '';
  }

  toggleDropdown() {
    this.isVisible = !this.isVisible;
    if (!this.isVisible) {
      this.dropdownClosed.emit();
    }
  }

  isSelected(option: IMultiSelectOption): boolean {
    return this.model && this.model.indexOf(option.code) > -1;
  }

  setSelected(event: Event, option: IMultiSelectOption) {
    if (!this.model) {
      this.model = [];
    }
    let index = this.model.indexOf(option.code);
    if (index > -1) {
      this.model.splice(index, 1);
    } else {
      if (this.settings.selectionLimit === 0 || this.model.length < this.settings.selectionLimit) {
        this.model.push(option.code);
      } else {
        if (this.settings.autoUnselect) {
          this.model.push(option.code);
          this.model.shift();
        } else {
          this.selectionLimitReached.emit(this.model.length);
          return;
        }
      }
    }
    if (this.settings.closeOnSelect) {
      this.toggleDropdown();
    }
    this.onModelChange(this.model);
  }

  updateNumSelected() {
    this.numSelected = this.model && this.model.length || 0;
  }

  updateTitle() {
    if (this.numSelected === 0) {
      this.title = this.texts.defaultTitle;
    } else if (this.settings.dynamicTitleMaxItems >= this.numSelected) {
      this.title = this.options
        .filter((option: IMultiSelectOption) =>
          this.model && this.model.indexOf(option.code) > -1
        )
        .map((option: IMultiSelectOption) => option.label)
        .join(', ');
    } else {
      this.title = this.numSelected
        + ' '
        + (this.numSelected === 1 ? this.texts.checked : this.texts.checkedPlural);
    }
  }

  checkAll() {
    this.model = this.options.map(option => option.code);
    this.onModelChange(this.model);
  }

  uncheckAll() {
    this.model = [];
    this.onModelChange(this.model);
  }
}

@NgModule({
  imports: [CommonModule],
  exports: [MultiselectDropdown],
  declarations: [MultiselectDropdown, MultiSelectSearchFilter]
})
export class MultiselectDropdownModule {}
