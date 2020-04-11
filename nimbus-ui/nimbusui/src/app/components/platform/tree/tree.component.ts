import { Param } from './../../../shared/param-state';
import { HttpMethod } from './../../../shared/command.enum';
import { GenericDomain } from './../../../model/generic-domain.model';
import { GridUtils } from './../../../shared/grid-utils';
import { PageService } from './../../../services/page.service';
import { BaseTableElement } from './../base-table-element.component';
import { Component, ViewChild, ChangeDetectorRef } from '@angular/core';
import {MenuItem,TreeNode} from 'primeng/api';
import {TreeDragDropService} from 'primeng/api';
import {MessageService} from 'primeng/api';
import { Tree } from 'primeng/tree';

@Component({
    selector: 'nm-tree',
    templateUrl: './treedemo.html',
    providers: []
})
export class TreeDemo extends BaseTableElement {

    @ViewChild('expandingTree')
    expandingTree: Tree;

    treedata: any;
    
    selectedFiles2: TreeNode[];
    
    items: MenuItem[];
    
    loading: boolean;
    
    constructor(
        private pageSvc: PageService,
        private gridUtils: GridUtils,
        protected cd: ChangeDetectorRef
      ) {
        super(cd);
      }

    ngOnInit() {
        this.loading = true;
        super.ngOnInit();
        this.pageSvc.processEvent(this.element.path,'$execute',new GenericDomain(),HttpMethod.GET.value,undefined);
        this.treedata = [
            {"label": "Accounting/Finance/Audting", "data": "Accounting", "children": []},
            {"label": "Acturial", "data": "Acturial",  "children": []},
            {"label": "Administrative support", "data": "Administrative support", "children": []},
            {"label": "Business Development & Planning", "data": "Business Development & Planning",  "children": []},
            {"label": "Claims", "data": "Claims",  "children": []},
            {"label": "Customer Service", "data": "Customer Service", "children": []},
            {"label": "Education & Training", "data": "Education & Training", "children": []},
            {"label": "Facilities", "data": "Facilities", "children": []},
            {"label": "Information Technology", "data": "Information Technology", 
              "children": [ {"label": "Java", "data": "Java"},
              {"label": ".NET", "data": ".NET"},
              {"label": "Python", "data": "Python"}]}
        ]
    this.subscribers.push(
      this.pageSvc.gridValueUpdate$.subscribe((treeList: Param) => {
        if (this.element.path === treeList.path) {
          this.treedata = this.getTreeStructure(treeList.tableBasedData.values);
        }
      })
    );
    }
    
    getTreeStructure(gridList: any[]) {
        let data: any[] = [];
        data = [
          {
              "label": "Documents",
              "data": "Documents Folder",
              "expandedIcon": "pi pi-folder-open",
              "collapsedIcon": "pi pi-folder",
              "children": [{
                      "label": "Work",
                      "data": "Work Folder",
                      "expandedIcon": "pi pi-folder-open",
                      "collapsedIcon": "pi pi-folder",
                      "children": [{"label": "Expenses.doc", "icon": "pi pi-file", "data": "Expenses Document"}, {"label": "Resume.doc", "icon": "pi pi-file", "data": "Resume Document"}]
                  },
                  {
                      "label": "Home",
                      "data": "Home Folder",
                      "expandedIcon": "pi pi-folder-open",
                      "collapsedIcon": "pi pi-folder",
                      "children": [{"label": "Invoices.txt", "icon": "pi pi-file", "data": "Invoices for this month"}]
                  }]
          },
          {
              "label": "Pictures",
              "data": "Pictures Folder",
              "expandedIcon": "pi pi-folder-open",
              "collapsedIcon": "pi pi-folder",
              "children": [
                  {"label": "barcelona.jpg", "icon": "pi pi-image", "data": "Barcelona Photo"},
                  {"label": "logo.jpg", "icon": "pi pi-image", "data": "PrimeFaces Logo"},
                  {"label": "primeui.png", "icon": "pi pi-image", "data": "PrimeUI Logo"}]
          },
          {
              "label": "Movies",
              "data": "Movies Folder",
              "expandedIcon": "pi pi-folder-open",
              "collapsedIcon": "pi pi-folder",
              "children": [{
                      "label": "Al Pacino",
                      "data": "Pacino Movies",
                      "children": [{"label": "Scarface", "icon": "pi pi-video", "data": "Scarface Movie"}, {"label": "Serpico", "icon": "pi pi-video", "data": "Serpico Movie"}]
                  },
                  {
                      "label": "Robert De Niro",
                      "data": "De Niro Movies",
                      "children": [{"label": "Goodfellas", "icon": "pi pi-video", "data": "Goodfellas Movie"}, {"label": "Untouchables", "icon": "pi pi-video", "data": "Untouchables Movie"}]
                  }]
          }
      ]
        return data;
      }
   
    nodeSelect(event) {
        event.forEach(node => {
            console.log(node.label);
        });
    }
}